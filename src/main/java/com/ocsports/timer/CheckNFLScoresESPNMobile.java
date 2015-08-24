package com.ocsports.timer;

import com.ocsports.core.MyEmailer;
import com.ocsports.core.MyHTTPClient;
import com.ocsports.core.ProcessException;
import com.ocsports.core.PropList;
import com.ocsports.core.PropertiesHelper;
import com.ocsports.core.SportTypes;
import com.ocsports.models.GameModel;
import com.ocsports.models.TeamModel;
import com.ocsports.models.TimerMessageModel;
import com.ocsports.servlets.TimerServlet;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import org.apache.log4j.Logger;

public class CheckNFLScoresESPNMobile extends TimerTask implements ITimerTask {
    private static final String SCORES_URL = "http://m.espn.go.com/nfl/scoreboard";
    private static final Logger log = Logger.getLogger( TimerServlet.class.getName() );

	private SeasonSQLController seasonSql = null;
	private PoolSQLController poolSql = null;
	private Collection gamesInProgress = null;

	public CheckNFLScoresESPNMobile() {
	}

	// @Override
	public void run() {
		run( false );
	}

	// @Override
	public void run(boolean ignoreTimes) {
		try {
			seasonSql = new SeasonSQLController();
			poolSql = new PoolSQLController();

			// first check if we are waiting for scores to be input
			gamesInProgress = seasonSql.findGamesInProgress( SportTypes.TYPE_NFL_FOOTBALL );
			// no games waiting for scores;  just exit gracefully
            if(gamesInProgress.size() <= 0) return;

            String httpSource = MyHTTPClient.getURL( SCORES_URL );
            if(httpSource == null || httpSource.length() == 0) {
                addTaskMessage( "Unable to retrieve http text from url '" + SCORES_URL + "'");
				return;
            }
			
			List httpTables = getHttpTableList( httpSource );
			// no scores on the website; exit gracefully
			if( httpTables.isEmpty() ) return;
			
			for( int i=0; i < httpTables.size(); i++ ) {
				String tbl = (String)httpTables.get(i);
				if( isFinalScore(tbl) ) {
					processScore( tbl );
				}
			}
		}
		catch( ProcessException pe ) {
            addTaskMessage("Task failed: " + pe.getExceptionTypeClass().getName() + "-" + pe.getMessage());
		}
		catch( Exception e ) {
			ProcessException pe = new ProcessException(e);
            addTaskMessage("Task failed: " + pe.getExceptionTypeClass().getName() + "-" + pe.getMessage());
		}
	}

	private List getHttpTableList( String httpSource ) throws Exception {
		List tables = new ArrayList();
		
		int startIndex = 0;
		int tblStart = 0;
		int tblEnd = 0;
		String tblHttp = null;
		while( startIndex < httpSource.length() ) {
			tblStart = httpSource.indexOf( "table class='match'", startIndex );
			// did not find any more occurrences; exit gracefully
			if( tblStart == -1 ) break;
			
			tblEnd = httpSource.indexOf( "</table>", startIndex );
			if( tblEnd == -1 ) break;
			
			tblHttp = httpSource.substring(tblStart, tblEnd);
			tables.add( tblHttp );

			startIndex = tblEnd + 1;
		}

		return tables;
	}

	private boolean isFinalScore(String gameTable) {
		return( gameTable.indexOf( ">Final<" ) > 0 );
	}

	private void processScore( String http ) throws Exception {
		String awayTeam = getAwayTeam( http );
		String homeTeam = getHomeTeam( http );
		if( awayTeam == null || awayTeam.length() == 0 || homeTeam == null || homeTeam.length() == 0 ) {
			return;
		}
		
		String awayScore = getAwayScore( http );
		String homeScore = getHomeScore( http );
//		addTaskMessage( "Game score found: " + awayTeam + " " + awayScore + " - " + homeTeam + " " + homeScore );

		// locate the GameModel where these 2 teams are playing
		TeamModel awayModel = seasonSql.findTeam(null, null, awayTeam);
		TeamModel homeModel = seasonSql.findTeam(null, null, homeTeam);
		GameModel gm = getGameModel( awayModel, homeModel );
		// either the score is already posted or the game does not exist; just exit gracefully
		if( gm == null ) {
			return;
		}

		gm.setAwayScore( Integer.parseInt(awayScore) );
		gm.setHomeScore( Integer.parseInt(homeScore) );
		gm.setPosted(true);
		seasonSql.updateGame(gm);
		poolSql.postGame(gm);
		
		String msg = "Game Posted: " + awayTeam + " " + gm.getAwayScore() + " - " + homeTeam + " " + gm.getHomeScore();
		addTaskMessage( msg );

		sendGamePostedEmail( gm, awayModel, homeModel );
	}

	private GameModel getGameModel( TeamModel awayModel, TeamModel homeModel ) throws Exception {
		if( awayModel == null || homeModel == null ) {
			return null;
		}

		Iterator iter = gamesInProgress.iterator();
		while(iter.hasNext()) {
			GameModel gm = (GameModel)iter.next();
			if( gm.getAwayTeamId() == awayModel.getId() && gm.getHomeTeamId() == homeModel.getId() && !gm.isPosted() ) {
				// found a match based on teams and the score has not been posted yet
				return gm;
			}
		}

		// could not find a matching game;
		return null;
	}

	private String getAwayTeam( String http ) {
		int start = http.indexOf( "<strong>", 0 );
		int end = http.indexOf( "</strong>", start );
		return http.substring( start+8, end );
	}

	private String getHomeTeam( String http ) {
		int start0 = http.indexOf( "<strong>", 0 );
		int start = http.indexOf( "<strong>", start0+20 );
		int end = http.indexOf( "</strong>", start );
		return http.substring( start+8, end );
	}

	private String getAwayScore( String http ) {
		int start1 = http.indexOf( "<td class='snap-", 0 );
		int start2 = http.indexOf( ">", start1 );
		int start3 = http.indexOf( "<", start2 );
		return http.substring( start2+1, start3 );
	}

	private String getHomeScore( String http ) {
		int start0 = http.indexOf( "<td class='snap", 0 );
		int start1 = http.indexOf( "<td class='snap-", start0+20 );
		int start2 = http.indexOf( ">", start1 );
		int start3 = http.indexOf( "<", start2 );
		return http.substring( start2+1, start3 );
	}

    private void addTaskMessage(String msg) {
        log.debug(msg);
        TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, this.getClass().getName(), msg));
    }
	
	private boolean sendGamePostedEmail(GameModel gm, TeamModel awayTeam, TeamModel homeTeam) {
		String[] TOs = PropertiesHelper.getProperty( PropList.ADMIN_POST_SCORES_EMAIL ).split(",");

		SimpleDateFormat fmt = new SimpleDateFormat();
		fmt.applyPattern("h:mm a");
		String currTime = fmt.format( new java.util.Date() );

		String subject = "Game score posted for: " + awayTeam.getAbrv() + " at " + homeTeam.getAbrv();

		StringBuffer msg = new StringBuffer();
		msg.append("Final score: ");
		if( gm.getHomeScore() > gm.getAwayScore() ) {
			msg.append(  homeTeam.getAbrv() + " " + gm.getHomeScore() + " - " + awayTeam.getAbrv() + " " + gm.getAwayScore() );
		}
		else {
			msg.append(  awayTeam.getAbrv() + " " + gm.getAwayScore() + " - " + homeTeam.getAbrv() + " " + gm.getHomeScore() );
		}
		msg.append( "\n\n" );
		msg.append( "NFL game score has been automatically posted at " + currTime + ".");

        return MyEmailer.sendEmailMsg(TOs, null, null, subject, msg.toString(), null);
	}

}
