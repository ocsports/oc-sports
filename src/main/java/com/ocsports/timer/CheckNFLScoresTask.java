/*
 * Title         CheckNFLScoresTask.java
 * Created       September 26, 2006
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.timer;
                 2016-11-14 - moved url to nfl.com and parse json instead of html source code
 */
package com.ocsports.timer;

import java.util.*;
import org.apache.log4j.Logger;

import com.ocsports.core.*;
import com.ocsports.sql.*;
import com.ocsports.models.*;
import com.ocsports.servlets.TimerServlet;
import java.text.SimpleDateFormat;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class CheckNFLScoresTask extends TimerTask implements ITimerTask {
    private static final String URL = "http://www.nfl.com/liveupdate/scorestrip/scorestrip.json";
    private static final String CLASS_NAME = CheckNFLScoresTask.class.getName();
    private static final Logger log = Logger.getLogger( CLASS_NAME );

    private SeasonSQLController seasonSQL;
    private PoolSQLController poolSQL;
    private Collection gamesInProgress;

    public void run(boolean ignoreTimes) {
        run();
    }

    public void run() {
        try {
            seasonSQL = new SeasonSQLController();
            poolSQL = new PoolSQLController();
            
            gamesInProgress = seasonSQL.findGamesInProgress( SportTypes.TYPE_NFL_FOOTBALL );
            if(gamesInProgress == null || gamesInProgress.size() <= 0) {
                return;
            }
            addTaskMessage(gamesInProgress.size() + " games in progress...");

            postScores();
            addTaskMessage(CLASS_NAME + " task completed.");
        }
        catch(ProcessException ex) {
            addTaskMessage(CLASS_NAME + " task failed: " + ex.getExceptionTypeClass().getName() + "-" + ex.getMessage());
        }
        catch(Throwable t) {
            addTaskMessage(CLASS_NAME + " task failed: Throwable caught: " + t.getMessage());
        }
        finally {
            seasonSQL = null;
            poolSQL = null;
        }
    }

    private void addTaskMessage(String msg) {
        log.debug(msg);
        TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, msg));
    }

    private void postScores() throws ProcessException {
        String pageSource = MyHTTPClient.getURL(URL);
        if(pageSource == null || pageSource.length() == 0) {
            throw new ProcessException("Unable to retrieve web page (" + URL + ")");
        }
        // some array elements are missing a value, replace with empty string
        String properJson = pageSource.replaceAll(",,", ",\"\",");
        JSONObject jsonRoot;

        try {
            JSONParser parser = new JSONParser();
            jsonRoot = (JSONObject)parser.parse(properJson);
        } catch (org.json.simple.parser.ParseException ex) {
            addTaskMessage("unable to parse NFL json: " + ex.getMessage());
            return;
        }
        JSONArray jsonGames = (JSONArray)jsonRoot.get("ss");
        if (jsonGames == null || jsonGames.isEmpty()) {
            addTaskMessage("no current games found in json");
            return;
        }

        // loop through each current game 
		Iterator it = jsonGames.iterator();
		while (it.hasNext()) {
            JSONArray gameData = (JSONArray)it.next();
            
            String gameStatus = (String)gameData.get(2);
            if (gameStatus.toUpperCase().indexOf("FINAL") > -1) {
                // game is stil in progress or has not started; skip it
                continue;
            }

            String awayAbrv;
            int awayScore;
            String homeAbrv;
            int homeScore;
            try {
                awayAbrv = (String)gameData.get(4);
                awayScore = Integer.parseInt( (String)gameData.get(5) );
                homeAbrv = (String)gameData.get(6);
                homeScore = Integer.parseInt( (String)gameData.get(7) );
            }
            catch (NumberFormatException ex) {
                addTaskMessage("Unable to parse game json: " + ex.getMessage());
                continue;
            }

            TeamModel awayTeam = seasonSQL.findTeam(null, null, awayAbrv);
            TeamModel homeTeam = seasonSQL.findTeam(null, null, homeAbrv);

            Iterator iter = gamesInProgress.iterator();
            while(iter.hasNext()) {
                GameModel gm = (GameModel)iter.next();
                if(gm.getHomeTeamId() == homeTeam.getId() && gm.getAwayTeamId() == awayTeam.getId()) {
                    gm.setHomeScore(homeScore);
                    gm.setAwayScore(awayScore);
                    gm.setPosted(true);
                    addTaskMessage("Posting score for game " + gm.getId() + ": " + awayTeam.getAbrv() + " @ " + homeTeam.getAbrv() + "," + gm.getAwayScore() + "-" + gm.getHomeScore());
                    try {
                        seasonSQL.updateGame(gm);
                        poolSQL.postGame(gm);
                        
                        sendGamePostedEmail( gm, awayTeam, homeTeam );
                    }
                    catch(ProcessException pe2) {
                        addTaskMessage("failed to post score for game " + gm.getId() + ": " + pe2.getMessage());
                    }
                    break;
                }
            }
        }
    }
    
    private boolean sendGamePostedEmail(GameModel gm, TeamModel awayTeam, TeamModel homeTeam) {
		String[] TOs = PropertiesHelper.getProperty( PropList.ADMIN_POST_SCORES_EMAIL ).split(",");

		SimpleDateFormat fmt = new SimpleDateFormat();
		fmt.applyPattern("h:mm a");
		String currTime = fmt.format( new java.util.Date() );

		String subject = "Game score posted for: " + awayTeam.getAbrv() + " at " + homeTeam.getAbrv();
        String msgScore;
		if( gm.getHomeScore() > gm.getAwayScore() ) {
			msgScore = homeTeam.getAbrv() + " " + gm.getHomeScore() + " - " + awayTeam.getAbrv() + " " + gm.getAwayScore();
		}
		else {
			msgScore = awayTeam.getAbrv() + " " + gm.getAwayScore() + " - " + homeTeam.getAbrv() + " " + gm.getHomeScore();
		}
        String msgHeader = "Final score: ";
        String msgFooter = "\n\n" + "NFL game score has been automatically posted at " + currTime + ".";

        StringBuffer msg = new StringBuffer();
		msg.append( msgHeader );
        msg.append( msgScore );
		msg.append( msgFooter );

        return MyEmailer.sendEmailMsg(TOs, null, null, subject, msg.toString(), null);
	}
}
