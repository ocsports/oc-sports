package com.ocsports.servlets;

import com.ocsports.core.JSPPages;
import com.ocsports.core.MyEmailer;
import com.ocsports.core.ProcessException;
import com.ocsports.core.Status;
import com.ocsports.models.GameModel;
import com.ocsports.models.LeagueModel;
import com.ocsports.models.SeasonModel;
import com.ocsports.models.SeasonSeriesModel;
import com.ocsports.models.TeamModel;
import com.ocsports.models.UserGameXrefModel;
import com.ocsports.models.UserModel;
import com.ocsports.models.UserSeriesXrefModel;
import com.ocsports.sql.ForumSQLController;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;
import com.ocsports.sql.UserSQLController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PoolServlet extends ServletBase {

    public static final String DETAIL_PAGE_ATTR = "detailPage";
    public static final long FORUM_MAX_LENGTH = 2000;

    public void defaultAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        picks(request, response, session);
    }

    public void picks(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        PoolSQLController sqlCtrlr = null;
        SeasonSQLController seasonSql = null;
        try {
            sqlCtrlr = new PoolSQLController();
            seasonSql = new SeasonSQLController();

            LeagueModel lm = (LeagueModel) session.getAttribute("LeagueModel");
            UserModel um = (UserModel) session.getAttribute("UserModel");
            if (lm == null || um == null) {
                sendRedirect("goUser", request, response, session);
                return;
            }

            int seriesId;
            String s = request.getParameter("seriesId");
            if (s != null && !s.equals("")) {
                seriesId = Integer.parseInt(s);
            } else {
                s = (String) request.getAttribute("seriesId");
                if (s != null && !s.equals("")) {
                    seriesId = Integer.parseInt(s);
                } else {
                    seriesId = seasonSql.getCurrentSeries(lm.getSeasonId());
                }
            }
            SeasonModel sm = seasonSql.getSeasonModel(lm.getSeasonId());
            request.setAttribute("SeasonModel", sm);

            SeasonSeriesModel ssm = seasonSql.getSeasonSeriesModel(seriesId);
            request.setAttribute("SeasonSeriesModel", ssm);

            Collection series = seasonSql.getSeriesBySeason(lm.getSeasonId());
            request.setAttribute("SeriesModels", series);

            Collection games = seasonSql.getGamesBySeries(seriesId);
            request.setAttribute("GameModels", games);

            HashMap teamMap = seasonSql.getTeamMap(sm.getSportType());
            request.setAttribute("TeamMap", teamMap);

            Collection userPicks = sqlCtrlr.getUserPicksBySeries(seriesId, um.getUserId());
            request.setAttribute("UserGameXrefModels", userPicks);

            Collection allUserSeries = sqlCtrlr.getUserSeries(um.getUserId());
            request.setAttribute("allUserSeries", allUserSeries);

            Iterator iter = allUserSeries.iterator();
            while (iter.hasNext()) {
                UserSeriesXrefModel usm = (UserSeriesXrefModel) iter.next();
                if (usm.getSeriesId() == seriesId) {
                    request.setAttribute("UserSeriesXrefModel", usm);
                }
                if (usm.getSurvivorStatus() == Status.SURVIVOR_STATUS_LOST) {
                    request.setAttribute("survivorEliminated", "Y");
                }
            }

            sendToJSP(JSPPages.VIEW_PICKS, request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
        }
    }

    public void standings(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        PoolSQLController sqlCtrlr = null;
        SeasonSQLController seasonSql = null;
        UserSQLController userSql = null;
        try {
            sqlCtrlr = new PoolSQLController();
            seasonSql = new SeasonSQLController();
            userSql = new UserSQLController();

            LeagueModel lm = (LeagueModel) session.getAttribute("LeagueModel");
            if (lm == null) {
                sendRedirect("goUser", request, response, session);
                return;
            }

            SeasonModel sm = seasonSql.getSeasonModel(lm.getSeasonId());

            Collection series = seasonSql.getSeriesBySeason(lm.getSeasonId());
            request.setAttribute("SeriesModels", series);

            SortedMap standings = sqlCtrlr.getStandings(lm.getId(), -1);
            request.setAttribute("standings", standings);

            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.STANDINGS_PICKS);
            request.setAttribute("menuItem", "Picks");

            sendToJSP(JSPPages.STANDINGS, request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
            if (userSql != null) {
                userSql.closeConnection();
            }
        }
    }

    public void lockStandings(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        LeagueModel lm = (LeagueModel) session.getAttribute("LeagueModel");

        PoolSQLController sqlCtrlr = null;
        SeasonSQLController seasonSql = null;
        UserSQLController userSql = null;
        try {
            sqlCtrlr = new PoolSQLController();
            seasonSql = new SeasonSQLController();
            userSql = new UserSQLController();

            SeasonModel sm = seasonSql.getSeasonModel(lm.getSeasonId());

            Collection series = seasonSql.getSeriesBySeason(lm.getSeasonId());
            request.setAttribute("SeriesModels", series);

            SortedMap standings = sqlCtrlr.getLockStandings(lm.getId(), -1);
            request.setAttribute("standings", standings);

            HashMap teams = seasonSql.getTeamMap(sm.getSportType());
            request.setAttribute("teamMap", teams);

            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.STANDINGS_LOCKS);
            request.setAttribute("menuItem", "Locks");

            sendToJSP(JSPPages.STANDINGS, request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
            if (userSql != null) {
                userSql.closeConnection();
            }
        }
    }

    public void survivorStandings(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        LeagueModel lm = (LeagueModel) session.getAttribute("LeagueModel");

        PoolSQLController sqlCtrlr = null;
        SeasonSQLController seasonSql = null;
        UserSQLController userSql = null;
        try {
            sqlCtrlr = new PoolSQLController();
            seasonSql = new SeasonSQLController();
            userSql = new UserSQLController();

            SeasonModel sm = seasonSql.getSeasonModel(lm.getSeasonId());

            Collection series = seasonSql.getSeriesBySeason(lm.getSeasonId());
            request.setAttribute("SeriesModels", series);

            SortedMap standings = sqlCtrlr.getSurvivorStandings(lm.getId(), -1);
            request.setAttribute("standings", standings);

            HashMap teams = seasonSql.getTeamMap(sm.getSportType());
            request.setAttribute("teamMap", teams);

            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.STANDINGS_SURVIVOR);
            request.setAttribute("menuItem", "Survivor");

            sendToJSP(JSPPages.STANDINGS, request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
            if (userSql != null) {
                userSql.closeConnection();
            }
        }
    }

    public void leaguePicks(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        LeagueModel lm = (LeagueModel) session.getAttribute("LeagueModel");
        UserModel um = (UserModel) session.getAttribute("UserModel");

        String series = request.getParameter("series");
        int seriesId = (series == null || series.equals("") ? -1 : Integer.parseInt(series));

        PoolSQLController sqlCtrlr = null;
        SeasonSQLController seasonSql = null;
        UserSQLController userSql = null;
        try {
            sqlCtrlr = new PoolSQLController();
            seasonSql = new SeasonSQLController();
            userSql = new UserSQLController();

            if (seriesId == -1) {
                seriesId = seasonSql.getCurrentSeries(lm.getSeasonId());
            }
            request.setAttribute("selectedSeriesId", String.valueOf(seriesId));

            SeasonModel sm = seasonSql.getSeasonModel(lm.getSeasonId());
            request.setAttribute("SeasonModel", sm);

            Collection seriesList = seasonSql.getSeriesBySeason(lm.getSeasonId());
            request.setAttribute("SeriesModels", seriesList);

            Collection games = seasonSql.getGamesBySeries(seriesId);
            request.setAttribute("GameModels", games);

            //Collection standings = sqlCtrlr.getSeasonWinsLossesByUser(um.getLeagueId());
            SortedMap standings = sqlCtrlr.getStandings(lm.getId(), seriesId);
            request.setAttribute("standings", standings);

            HashMap gamePicks = sqlCtrlr.getUserPicksXrefMap(um.getLeagueId(), seriesId, -1);
            request.setAttribute("UserGameXrefModels", gamePicks);

            HashMap seriesPicks = sqlCtrlr.getUserSeriesXrefMap(um.getLeagueId(), seriesId);
            request.setAttribute("UserSeriesXrefModels", seriesPicks);

            HashMap teamMap = seasonSql.getTeamMap(sm.getSportType());
            request.setAttribute("TeamMap", teamMap);

            sendToJSP(JSPPages.LEAGUE_PICKS, request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
            if (userSql != null) {
                userSql.closeConnection();
            }
        }
    }

    public void updatePicks(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserModel um = (UserModel) session.getAttribute("UserModel");

        int seriesId = Integer.parseInt(request.getParameter("seriesId"));
        int gameCount = Integer.parseInt(request.getParameter("gameCount"));

        int[] gameIds = new int[gameCount];
        int[] picks = new int[gameCount];
        for (int i = 1; i <= gameCount; i++) {
            gameIds[i - 1] = Integer.parseInt(request.getParameter("gameId" + i));
            picks[i - 1] = -1;
            String pick = (String) request.getParameter("gamePick" + i);
            if (pick != null && pick.length() > 0) {
                picks[i - 1] = Integer.parseInt(pick);
            }
        }

        String survivorPick = request.getParameter("survivorPick");
        int survivor = (survivorPick == null || survivorPick.equals("") ? 0 : Integer.parseInt(survivorPick));

        String lockPick = request.getParameter("lockPick");
        int lockOfWeek = (lockPick == null || lockPick.equals("") ? 0 : Integer.parseInt(lockPick));

        String userNotes = "";

        PoolSQLController sqlCtrlr = null;
        SeasonSQLController seasonSql = null;
        try {
            sqlCtrlr = new PoolSQLController();
            seasonSql = new SeasonSQLController();

            UserSeriesXrefModel usx = sqlCtrlr.getUserSeriesXrefModel(seriesId, um.getUserId());
            //no pick was selected; use previous pick or set to empty
            if (lockOfWeek == 0) {
                if (usx != null && usx.getLock() > 0) {
                    lockOfWeek = usx.getLock();
                } else {
                    lockOfWeek = -1;
                }
            }

            //no pick was selected; use previous pick or set to empty
            if (survivor == 0) {
                if (usx != null && usx.getSurvivor() > 0) {
                    survivor = usx.getSurvivor();
                } else {
                    survivor = -1;
                }
            }

            // don't bother to update; just remove and re-create
            sqlCtrlr.removeUserSeriesXref(seriesId, um.getUserId());
            sqlCtrlr.createUserSeriesXref(seriesId, um.getUserId(), lockOfWeek, survivor, userNotes);
            if (usx != null) {
                //since we re-created, must re-assign the previous status'
                sqlCtrlr.updateUserLockStatus(seriesId, um.getUserId(), usx.getLockStatus());
                sqlCtrlr.updateUserSurvivorStatus(seriesId, um.getUserId(), usx.getSurvivorStatus());
            }

            long currentTime = new java.util.Date().getTime();

            UserGameXrefModel ugm = null;
            for (int k = 0, lenK = gameIds.length; k < lenK; k++) {
                GameModel gm = seasonSql.getGameModel(gameIds[k]);
                if (picks[k] >= 0 && gm.getStartDate() > currentTime) {
                    sqlCtrlr.removeUserGameXref(gameIds[k], um.getUserId());
                    sqlCtrlr.createUserGameXref(gameIds[k], um.getUserId(), picks[k], 0);
                }
            }

            boolean emailSuccess = true;
            String emailPicks = request.getParameter("emailPicks");
            if (emailPicks != null && emailPicks.equals("Y")) {
                emailSuccess = this.emailPicks(request, response, session);
            }
            this.sendRedirect("goPool?r=picks&seriesId=" + seriesId + (!emailSuccess ? "&emailError=Y" : ""), request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
        }
    }

    public boolean emailPicks(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserModel um = (UserModel) session.getAttribute("UserModel");
        LeagueModel lm = (LeagueModel) session.getAttribute("LeagueModel");

        int seriesId = Integer.parseInt(request.getParameter("seriesId"));

        SeasonSQLController seasonSql = null;
        PoolSQLController poolSql = null;
        try {
            seasonSql = new SeasonSQLController();
            poolSql = new PoolSQLController();

            SeasonModel sm = seasonSql.getSeasonModel(lm.getSeasonId());
            SeasonSeriesModel ssm = seasonSql.getSeasonSeriesModel(seriesId);
            Collection games = seasonSql.getGamesBySeries(seriesId);
            HashMap teamMap = seasonSql.getTeamMap(sm.getSportType());

            HashMap picks = poolSql.getUserPicksXrefMap(lm.getId(), seriesId, um.getUserId());
            UserSeriesXrefModel usm = poolSql.getUserSeriesXrefModel(seriesId, um.getUserId());

            StringBuffer msg = new StringBuffer();
            msg.append(um.getFullName()).append(",\n");
            msg.append("   Here are your picks for ").append(sm.getSeriesPrefix()).append(" ").append(ssm.getSequence());
            msg.append("\n\n");

            if (games != null && !games.isEmpty()) {
                boolean defaultPicksMade = false;

                Iterator iter = games.iterator();
                while (iter.hasNext()) {
                    GameModel gm = (GameModel) iter.next();
                    TeamModel home = (TeamModel) teamMap.get(String.valueOf(gm.getHomeTeamId()));
                    TeamModel away = (TeamModel) teamMap.get(String.valueOf(gm.getAwayTeamId()));
                    String sGame = away.getAbrvFormatted() + " at " + home.getAbrvFormatted();
                    String sSpread = (gm.getSpread() == 0 ? "PICK" : (gm.getSpread() > 0 ? "+" + gm.getSpread() : String.valueOf(gm.getSpread())));
                    String sPick = "";

                    UserGameXrefModel ugm = (UserGameXrefModel) picks.get(um.getUserId() + "^" + gm.getId());
                    if (ugm != null && ugm.getSelectedTeamId() > 0) {
                        if (ugm.getSelectedTeamId() == home.getId()) {
                            sPick = home.getCity() + " " + home.getName();
                        } else if (ugm.getSelectedTeamId() == away.getId()) {
                            sPick = away.getCity() + " " + away.getName();
                        }
                        if (ugm.isDefaultPick()) {
                            defaultPicksMade = true;
                            sPick += "**";
                        }
                    }
                    if (sPick.length() == 0) {
                        sPick = "<not selected>";
                    }
                    msg.append(sGame).append("(").append(sSpread).append(")");
                    msg.append(" - ").append(sPick);
                    msg.append("\n");
                }
                if (defaultPicksMade) {
                    msg.append("\n\n* Default picks based on user preference\n");
                }
                msg.append("\n");
            }

            msg.append("LOCK: ");
            if (usm != null && usm.getLock() > 0) {
                TeamModel lockTeam = (TeamModel) teamMap.get(String.valueOf(usm.getLock()));
                msg.append(lockTeam.getCity()).append(" ").append(lockTeam.getName());
            } else {
                msg.append("<not selected>");
            }
            msg.append("\n");

            msg.append("SURVIVOR: ");
            if (usm != null && usm.getSurvivor() > 0) {
                TeamModel survivorTeam = (TeamModel) teamMap.get(String.valueOf(usm.getSurvivor()));
                msg.append(survivorTeam.getCity()).append(" ").append(survivorTeam.getName());
            } else {
                msg.append("<not selected>");
            }
            msg.append("\n\n");

            msg.append("NOTES: ");
            if (usm != null && usm.getNotes() != null) {
                msg.append(usm.getNotes());
            }
            msg.append("\n\n\n");

            msg.append("OC Sports Administrator\n");
            msg.append("administrator@oc-sports.com\n");
            msg.append("www.oc-sports.com\n");

            String subject = "OC Sports - Picks Confirmation";
            String[] toRecips = new String[]{um.getEmail()};
            String[] ccRecips = null;
            if (um.getEmail2() != null && !um.getEmail2().equals("")) {
                ccRecips = new String[]{um.getEmail2()};
            }
            return MyEmailer.sendEmailMsg(toRecips, ccRecips, null, subject, msg.toString(), null);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (poolSql != null) {
                poolSql.closeConnection();
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
        }
    }

    public void forums(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserModel um = (UserModel) session.getAttribute("UserModel");
        ForumSQLController sqlCtrlr = null;
        try {
            sqlCtrlr = new ForumSQLController();

            Collection messages = sqlCtrlr.getMessages(um.getLeagueId());
            request.setAttribute("ForumMessageModels", messages);
            sendToJSP(JSPPages.FORUMS, request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
        }
    }

    public void addForumMessage(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        ForumSQLController sqlCtrlr = null;
        try {
            sqlCtrlr = new ForumSQLController();

            UserModel um = (UserModel) session.getAttribute("UserModel");
            int userId = um.getUserId();

            String msg = request.getParameter("msg");
            if (msg == null) {
                msg = "";
            }

            request.setAttribute("msgText", msg);
            if (msg.length() == 0) {
                request.setAttribute("msgError", "Post aborted. No message was found to process.");
                this.forums(request, response, session);
            } else if (msg.length() > 2000) {
                request.setAttribute("msgError", "Post aborted. Message length exceeds maximum 2000 characters.");
                this.forums(request, response, session);
            } else {
                sqlCtrlr.createMessage(msg, userId, um.getLeagueId());
                this.sendRedirect("goPool?r=forums", request, response, session);
            }
        } catch (Exception e) {
            throw new ProcessException(e);
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
        }
    }
}
