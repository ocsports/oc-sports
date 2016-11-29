package com.ocsports.timer;

import com.ocsports.core.MyEmailer;
import com.ocsports.core.MyHTTPClient;
import com.ocsports.core.ProcessException;
import com.ocsports.core.PropList;
import com.ocsports.core.PropertiesHelper;
import com.ocsports.core.SportTypes;
import com.ocsports.models.GameModel;
import com.ocsports.models.TeamModel;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

public class CheckNFLScoresTask extends TimerTask {
    private final String url = "http://www.nfl.com/liveupdate/scorestrip/scorestrip.json";
    private SeasonSQLController seasonSql;
    private PoolSQLController poolSql;

    /**
     * execute the task and do not let exceptions leak as they will destroy the timer object
     */
    public void run() {
        try {
            initTask();
            seasonSql = new SeasonSQLController();
            poolSql = new PoolSQLController();
            
            Collection gamesInProgress = seasonSql.findGamesInProgress( SportTypes.TYPE_NFL_FOOTBALL );
            if(gamesInProgress == null || gamesInProgress.size() <= 0) {
                return;
            }

            addTaskMessage(gamesInProgress.size() + " games in progress");
            postScores(gamesInProgress);
            timerTaskCompleted();
        }
        catch(ProcessException pe) {
            timerTaskFailed(pe);
        }
        catch(Throwable t) {
            timerTaskFailed(t);
        }
        finally {
            seasonSql = null;
            poolSql = null;
        }
    }

    /**
     * retrieve external json data and parse each game, searching for games which
     * have completed since the last iteration; if found, post scores and update the
     * game record
     * @param gamesInProgress  the collection of GameModels which are currently in progress
     * @throws ProcessException 
     */
    private void postScores(Collection gamesInProgress) throws ProcessException {
        String pageSource = MyHTTPClient.getURL(url);
        if(pageSource == null || pageSource.length() == 0) {
            throw new ProcessException("Unable to retrieve web page (" + url + ")");
        }
        // some array elements are missing a value, replace with empty string
        String properJson = pageSource.replaceAll(",,", ",\"\",");
        JSONObject jsonRoot;

        try {
            JSONParser parser = new JSONParser();
            jsonRoot = (JSONObject)parser.parse(properJson);
        } catch (org.json.simple.parser.ParseException ex) {
            throw new ProcessException("unable to parse json: " + ex.getMessage());
        }
        JSONArray jsonGames = (JSONArray)jsonRoot.get("ss");
        if (jsonGames == null || jsonGames.isEmpty()) {
            throw new ProcessException("no current games found in json");
        }

        // loop through each current game 
		Iterator it = jsonGames.iterator();
		while (it.hasNext()) {
            JSONArray gameData = (JSONArray)it.next();
            
            String gameStatus = (String)gameData.get(2);
            if (gameStatus.toUpperCase().indexOf("FINAL") > -1 ||
                gameStatus.toUpperCase().indexOf("F/") > -1) {
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
                addTaskMessage("unable to parse final game scores: " + ex.getMessage());
                continue;
            }

            TeamModel awayTeam = seasonSql.findTeam(null, null, awayAbrv);
            TeamModel homeTeam = seasonSql.findTeam(null, null, homeAbrv);
            if (awayTeam == null || homeTeam == null) {
                addTaskMessage("unable to identify team(s) for game " + awayAbrv + " at " + homeAbrv);
                continue;
            }

            Iterator iter = gamesInProgress.iterator();
            while(iter.hasNext()) {
                GameModel gm = (GameModel)iter.next();
                if(gm.getHomeTeamId() == homeTeam.getId() && gm.getAwayTeamId() == awayTeam.getId()) {
                    gm.setHomeScore(homeScore);
                    gm.setAwayScore(awayScore);
                    gm.setPosted(true);
                    addTaskMessage("Posting score for game " + gm.getId() + ": " + awayTeam.getAbrv() + " @ " + homeTeam.getAbrv() + "," + gm.getAwayScore() + "-" + gm.getHomeScore());
                    try {
                        seasonSql.updateGame(gm);
                        poolSql.postGame(gm);
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
    
    /**
     * send an email to the site administrators informing that a game score was posted
     * @param gm  the game record just posted
     * @param awayTeam  away team information
     * @param homeTeam  home team information
     * @return  boolean if the email was sent successfully
     */
    private boolean sendGamePostedEmail(GameModel gm, TeamModel awayTeam, TeamModel homeTeam) {
		String[] TOs = PropertiesHelper.getProperty( PropList.ADMIN_POST_SCORES_EMAIL ).split(",");

		SimpleDateFormat fmt = new SimpleDateFormat();
		fmt.applyPattern("h:mm a");
		String currTime = fmt.format( new java.util.Date() );

		String msgSubject = "Game score posted for: " + awayTeam.getAbrv() + " at " + homeTeam.getAbrv();
        String msgScore;
		if( gm.getHomeScore() > gm.getAwayScore() ) {
			msgScore = homeTeam.getAbrv() + " " + gm.getHomeScore() + " - " + awayTeam.getAbrv() + " " + gm.getAwayScore();
		}
		else {
			msgScore = awayTeam.getAbrv() + " " + gm.getAwayScore() + " - " + homeTeam.getAbrv() + " " + gm.getHomeScore();
		}
        String msgHeader = "Final score: ";
        String msgFooter = "\n\n" + "NFL game score has been automatically posted at " + currTime + ".";

        StringBuffer msgBody = new StringBuffer();
		msgBody.append( msgHeader );
        msgBody.append( msgScore );
		msgBody.append( msgFooter );

        return MyEmailer.sendEmailMsg(TOs, null, null, msgSubject, msgBody.toString(), null);
	}
}
