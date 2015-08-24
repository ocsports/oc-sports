/*
 * Title         CheckNFLScoresTask.java
 * Created       September 26, 2006
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.timer;
 */
package com.ocsports.timer;

import java.util.*;
import org.apache.log4j.Logger;

import com.ocsports.core.*;
import com.ocsports.sql.*;
import com.ocsports.models.*;
import com.ocsports.servlets.TimerServlet;

public class CheckNFLScoresTask extends TimerTask implements ITimerTask {
    private static final String URL = "http://scores.espn.go.com/nfl/scoreboard";
    private static final String CLASS_NAME = CheckNFLScoresTask.class.getName();
    private static final Logger log = Logger.getLogger( CLASS_NAME );

    private SeasonSQLController   seasonSQL = null;
    private PoolSQLController     poolSQL = null;
    private TeamModel             homeTeam;
    private TeamModel             awayTeam;
    private GameModel             gameModel;

    public void run() {
        this.run(false);
    }

    public void run(boolean ignoreTimes) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date());
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            int weekday = cal.get(Calendar.DAY_OF_WEEK);
            cal = null;

            //Check the time, only run this task if between 12:00PM and 10:00PM
            if(ignoreTimes || (currentHour >= 12 && currentHour <= 22)) {
                getScores();
                addTaskMessage(CLASS_NAME + " completed.");
            }
        }
        catch(ProcessException pe) {
            addTaskMessage("Task failed: " + pe.getExceptionTypeClass().getName() + "-" + pe.getMessage());
        }
        catch(Throwable t) {
            addTaskMessage("Task failed: Throwable caught: " + t.getMessage());
        }
    }

    private void addTaskMessage(String msg) {
        log.debug(msg);
        TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, msg));
    }

    private void getScores() throws ProcessException {
        try {
            seasonSQL = new SeasonSQLController();
            poolSQL = new PoolSQLController();

            Collection gamesInProgress = seasonSQL.findGamesInProgress( SportTypes.TYPE_NFL_FOOTBALL );
            if(gamesInProgress == null || gamesInProgress.size() <= 0) return;
            addTaskMessage(gamesInProgress.size() + " games in progress...");

            String pageSource = MyHTTPClient.getURL(URL);
            if(pageSource == null || pageSource.length() == 0) {
                throw new ProcessException("Unable to retrieve web page (" + URL + ")");
            }

            String[] lines = pageSource.split("class=\"teamTop_inGame\"");
            for(int i=0, len=lines.length; i < len; i++) {
                if(lines[i].indexOf("<tr class=\"interior-odd\">") > 0) {
                    awayTeam = null;
                    homeTeam = null;
                    gameModel = null;

                    if( !parseTeams(lines[i+3]) ) {
                        continue;
                    }
                    else if( !parseScores(lines[i+4]) ) {
                        continue;
                    }
                    else {
                        Iterator iter = gamesInProgress.iterator();
                        while(iter.hasNext()) {
                            GameModel gm = (GameModel)iter.next();
                            if(gm.getHomeTeamId() == gameModel.getHomeTeamId() && gm.getAwayTeamId() == gameModel.getAwayTeamId()) {
                                gm.setHomeScore(gameModel.getHomeScore());
                                gm.setAwayScore(gameModel.getAwayScore());
                                gm.setPosted(true);
                                addTaskMessage("Posting score for game " + gm.getId() + ": " + awayTeam.getAbrv() + " @ " + homeTeam.getAbrv() + "," + gameModel.getAwayScore() + "-" + gameModel.getHomeScore());
                                try {
                                    seasonSQL.updateGame(gm);
                                    poolSQL.postGame(gm);
                                }
                                catch(ProcessException pe2) {
                                    addTaskMessage("failed to post score for game " + gm.getId() + ": " + pe2.getMessage());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch(ProcessException pe) {
            throw pe;
        }
        finally {
            if(seasonSQL != null) seasonSQL.closeConnection();
            if(poolSQL != null) poolSQL.closeConnection();
        }
    }

    private boolean parseTeams(String data) {
        try {
            String search1 = "<td class=\"interior-team-content\">";
            String search2 = "<br />";
            int pos = data.indexOf(search1);
            int pos2 = data.indexOf(search2, pos + search1.length());
            if(pos == -1) return false;

            String sAwayTeam = data.substring(pos + search1.length(), pos + search1.length() + 3);
            sAwayTeam = sAwayTeam.trim();

            String sHomeTeam = data.substring(pos2 + search2.length(), pos2 + search2.length() + 3);
            sHomeTeam = sHomeTeam.trim();

            awayTeam = seasonSQL.findTeam(null, null, sAwayTeam);
            homeTeam = seasonSQL.findTeam(null, null, sHomeTeam);
            //addTaskMessage("Teams: A=" + awayTeam.getId() + ", H=" + homeTeam.getId());
        }
        catch(Exception e) {
            addTaskMessage("parseTeams Failed: " + e.getMessage());
        }
        return (awayTeam != null && homeTeam != null);
    }
    
    private boolean parseScores(String data) {
        try {
            String search1 = "<font class=\"status-final\">";
            String search2 = "<font class=\"status-final\">";
            int pos = data.indexOf(search1);
            int pos2 = data.indexOf(search2, pos + search1.length());
            if(pos == -1) return false;

            String sAwayScore = data.substring(pos + search1.length(), pos + search1.length() + 2);
            sAwayScore = sAwayScore.replaceAll("<", "");
            int awayScore = Integer.parseInt(sAwayScore);

            String sHomeScore = data.substring(pos2 + search2.length(), pos2 + search2.length() + 2);
            sHomeScore = sHomeScore.replaceAll("<", "");
            int homeScore = Integer.parseInt(sHomeScore);

            gameModel = new GameModel();
            gameModel.setHomeTeamId( homeTeam.getId() );
            gameModel.setHomeScore( homeScore );
            gameModel.setAwayTeamId( awayTeam.getId() );
            gameModel.setAwayScore( awayScore );
            //addTaskMessage("Scores: A=" + gameModel.getAwayScore() + ", H=" + gameModel.getHomeScore());
        }
        catch(Exception e) {
            addTaskMessage("parseScores Failed: " + e.getMessage());
        }
        return (gameModel != null);
    }
}
