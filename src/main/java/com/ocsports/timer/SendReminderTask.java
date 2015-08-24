/*
 * Title         SendReminderTask.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.timer;
 */
package com.ocsports.timer;

import java.util.*;
import org.apache.log4j.Logger;

import com.ocsports.core.*;
import com.ocsports.models.*;
import com.ocsports.servlets.TimerServlet;
import com.ocsports.sql.*;

public class SendReminderTask extends TimerTask implements ITimerTask {
    private static final  String CLASS_NAME = SendReminderTask.class.getName();
    private static final  Logger log = Logger.getLogger( CLASS_NAME );

    private SeasonSQLController   seasonSQL = null;
    private PoolSQLController     poolSQL = null;
    private UserSQLController     userSQL = null;
    private String                msg = null;

    public void run() {
        this.run(false);
    }

    public void run(boolean ignoreTimes) {
        try {
            log.debug("STARTING");

            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date());
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            cal = null;

            //Check the time, only run this task if between 10:00AM and 8:00PM
            if(ignoreTimes || (currentHour >= 10 && currentHour <= 20)) {
                this.getCurrentSeries();
                TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, "Task completed."));
            }
            log.debug("COMPLETED");
        }
        catch(ProcessException pe) {
            log.debug(pe.getStackTraceAsString());
            TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_ERROR, CLASS_NAME, "Task failed: " + pe.getExceptionTypeClass().getName() + "-" + pe.getMessage()));
        }
        catch(Throwable t) {
            log.debug("Throwable caught: " + t.getMessage());
            TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_ERROR, CLASS_NAME, "Task failed: Throwable caught: " + t.getMessage()));
        }
    }

    private void getCurrentSeries() throws ProcessException {
        try {
            seasonSQL = new SeasonSQLController();
            poolSQL = new PoolSQLController();
            userSQL = new UserSQLController();

            Collection seasons = seasonSQL.getSeasons(0, true);
            if(seasons != null && !seasons.isEmpty()) {
                Iterator iter = seasons.iterator();
                int seriesId = -1;
                while(iter.hasNext()) {
                    SeasonModel sm = (SeasonModel)iter.next();
                    seriesId = seasonSQL.getCurrentSeries(sm.getId());
                    SeasonSeriesModel ssm = seasonSQL.getSeasonSeriesModel(seriesId);
                    //have we already sent reminders for this series?
                    if(!ssm.isReminderEmail() && isTimeToSendReminders(seriesId)) {
                        msg = "Sending reminders for season " + sm.getId() + ", series " + seriesId;
                        log.debug(msg);
                        TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_ERROR, CLASS_NAME, msg));

                        this.checkUserReminderStatus(seriesId, sm.getId());
                        seasonSQL.setSeriesReminderStatus(seriesId, 1);
                    }
                }
            }
        }
        catch(Exception e) {
            if(e instanceof ProcessException) 
                throw (ProcessException)e;
            else
                throw new ProcessException(e);
        }
        finally {
            if(seasonSQL != null) seasonSQL.closeConnection();
            if(poolSQL != null) poolSQL.closeConnection();
            if(userSQL != null) userSQL.closeConnection();
        }
    }
    
    private boolean isTimeToSendReminders(int seriesId) throws ProcessException {
        boolean sendReminders = false;

        Collection games = seasonSQL.getGamesBySeries(seriesId);
        if(games != null && !games.isEmpty()) {
            Iterator iter = games.iterator();
            java.util.Date firstGameStart = null;
            while(iter.hasNext()) {
                GameModel gm = (GameModel)iter.next();
                //if we are less than 24 hours away from kickoff of any game in this series
                // its time to send reminders
                long gameReminder = gm.getStartDate() - (1000l * 60l * 60l * 24l);
                if(gameReminder < new java.util.Date().getTime()) {
                    sendReminders = true;
                    break;
                }
            }
        }

        return sendReminders;
    }
    
    private void checkUserReminderStatus(int seriesId, int seasonId) throws ProcessException {
        ArrayList userEmails = new ArrayList();

        Collection leagues = userSQL.getLeaguesBySeason(seasonId);
        if(leagues != null && !leagues.isEmpty()) {
            Iterator iter = leagues.iterator();
            while(iter.hasNext()) {
                LeagueModel lm = (LeagueModel)iter.next();
                Collection users = userSQL.getUsersByLeague(lm.getId(), -1);
                if(users != null && !users.isEmpty()) {
                Iterator iter2 = users.iterator();
                    while(iter2.hasNext()) {
                        UserModel um = (UserModel)iter2.next();
                        if(um.sendWarning() && poolSQL.userNeedsReminder(um, seriesId)) {
                            userEmails.add(um.getEmail().trim());
                            if(um.getEmail2() != null && !um.getEmail2().trim().equals(""))  {
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
        if(userEmails == null || userEmails.isEmpty()) return;
        
        String[] BCCs = new String[userEmails.size()];
        msg = "Sending " + BCCs.length + " reminders.";
        log.debug(msg);
        TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, msg));
        
        int i = 0;
        Iterator iter = userEmails.iterator();
        while(iter.hasNext()) {
            BCCs[i] = (String)iter.next();
            i++;
        }

        StringBuffer msg = new StringBuffer();
        msg.append("\n\nThis is your 24 Hour reminder notice indicating you have not completed your picks for the current week. You have until kick-off to make your pick selections. If you are unable to make your selections, the system will choose a team for you based on the 'Default Pick' option you selected on your Profile page.");
        msg.append("\n\n\nIf you do not wish to receive these reminder emails, login and update your profile by un-selecting the 'Send Reminder if picks not selected' option.");
        msg.append("\n\n\nOC Sports Administrator");
        msg.append("\nadministrator@oc-sports.com");

        String subject = "OC Sports - Weekly Reminder";
        boolean emailSent = MyEmailer.sendEmailMsg(null, null, BCCs, subject, msg.toString(), null);
    }
}
