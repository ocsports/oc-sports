package com.ocsports.timer;

import com.ocsports.core.MyEmailer;
import com.ocsports.core.ProcessException;
import com.ocsports.models.GameModel;
import com.ocsports.models.LeagueModel;
import com.ocsports.models.SeasonModel;
import com.ocsports.models.SeasonSeriesModel;
import com.ocsports.models.UserModel;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;
import com.ocsports.sql.UserSQLController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Scheduled task to send email reminders to players who have made no selections
 * for the upcoming games.
 *
 * @author paulcharlton
 */
public class SendReminderTask extends TimerTask {

    private SeasonSQLController seasonSql;
    private PoolSQLController poolSql;
    private UserSQLController userSql;

    /**
     * execute the task and do not let exceptions leak as they will destroy the
     * timer object
     */
    public void run() {
        try {
            seasonSql = new SeasonSQLController();
            poolSql = new PoolSQLController();
            userSql = new UserSQLController();

            this.getCurrentSeries();
            timerTaskCompleted();
        } catch (ProcessException pe) {
            timerTaskFailed(pe);
        } catch (Throwable t) {
            timerTaskFailed(t);
        } finally {
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
            if (poolSql != null) {
                poolSql.closeConnection();
            }
            if (userSql != null) {
                userSql.closeConnection();
            }
        }
    }

    private void getCurrentSeries() throws ProcessException {
        Collection seasons = seasonSql.getSeasons(0, true);
        if (seasons == null || seasons.isEmpty()) {
            return;
        }

        Iterator iter = seasons.iterator();
        while (iter.hasNext()) {
            SeasonModel sm = (SeasonModel) iter.next();
            int seriesId = seasonSql.getCurrentSeries(sm.getId());
            SeasonSeriesModel ssm = seasonSql.getSeasonSeriesModel(seriesId);
            //have we already sent reminders for this series?
            if (!ssm.isReminderEmail() && isTimeToSendReminders(seriesId)) {
                addTaskMessage("Sending reminders for season " + sm.getId() + ", series " + seriesId);
                this.checkUserReminderStatus(seriesId, sm.getId());
                seasonSql.setSeriesReminderStatus(seriesId, 1);
            }
        }
    }

    private boolean isTimeToSendReminders(int seriesId) throws ProcessException {
        Collection games = seasonSql.getGamesBySeries(seriesId);
        if (games == null || games.isEmpty()) {
            return false;
        }

        Iterator iter = games.iterator();
        while (iter.hasNext()) {
            GameModel gm = (GameModel) iter.next();
            //if we are less than 24 hours away from kickoff of any game in this series
            // its time to send reminders
            long gameReminder = gm.getStartDate() - (1000l * 60l * 60l * 24l);
            if (gameReminder < new java.util.Date().getTime()) {
                return true;
            }
        }
        return false;
    }

    private void checkUserReminderStatus(int seriesId, int seasonId) throws ProcessException {
        ArrayList userEmails = new ArrayList();

        Collection leagues = userSql.getLeaguesBySeason(seasonId);
        if (leagues != null && !leagues.isEmpty()) {
            Iterator iter = leagues.iterator();
            while (iter.hasNext()) {
                LeagueModel lm = (LeagueModel) iter.next();
                Collection users = userSql.getUsersByLeague(lm.getId(), -1);
                if (users != null && !users.isEmpty()) {
                    Iterator iter2 = users.iterator();
                    while (iter2.hasNext()) {
                        UserModel um = (UserModel) iter2.next();
                        if (um.sendWarning() && poolSql.userNeedsReminder(um, seriesId)) {
                            userEmails.add(um.getEmail().trim());
                            if (um.getEmail2() != null && !um.getEmail2().trim().equals("")) {
                                userEmails.add(um.getEmail2().trim());
                            }
                        }
                    }
                }
            }
        }
        this.sendUserReminders(userEmails);
    }

    private void sendUserReminders(Collection userEmails) throws ProcessException {
        if (userEmails == null || userEmails.isEmpty()) {
            return;
        }

        String[] BCCs = new String[userEmails.size()];
        int i = 0;
        Iterator iter = userEmails.iterator();
        while (iter.hasNext()) {
            BCCs[i] = (String) iter.next();
            i++;
        }
        addTaskMessage("Sending " + BCCs.length + " reminders");

        StringBuffer msg = new StringBuffer();
        msg.append("\n\nThis is your 24 Hour reminder notice indicating you have not completed your picks for the current week. You have until kick-off to make your pick selections. If you are unable to make your selections, the system will choose a team for you based on the 'Default Pick' option you selected on your Profile page.");
        msg.append("\n\n\nIf you do not wish to receive these reminder emails, login and update your profile by un-selecting the 'Send Reminder if picks not selected' option.");
        msg.append("\n\n\nOC Sports Administrator");
        msg.append("\nadministrator@oc-sports.com");

        String subject = "OC Sports - Weekly Reminder";
        MyEmailer.sendEmailMsg(null, null, BCCs, subject, msg.toString(), null);
    }
}
