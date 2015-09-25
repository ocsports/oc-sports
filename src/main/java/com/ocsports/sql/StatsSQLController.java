/*
 * Title         StatsSQLController.java
 * Created       April 11, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.sql;
 */
package com.ocsports.sql;

import com.ocsports.views.PickStatsView;
import java.sql.ResultSet;
import java.util.*;
import java.text.*;

import com.ocsports.core.ProcessException;
import com.ocsports.core.SportTypes;
import com.ocsports.models.*;

public class StatsSQLController extends SQLBase {

    public StatsSQLController() throws ProcessException {
        this.openConnection();
    }
    
    public TeamStatsModel[] getTeamStats(int seasonId) throws ProcessException {
        SeasonSQLController seasonSQL = new SeasonSQLController();
        Collection teams = seasonSQL.getTeamList(SportTypes.TYPE_NFL_FOOTBALL, SeasonSQLController.TEAMS_ORDER_BY_CITY);
        seasonSQL.closeConnection();
        seasonSQL = null;
        
        TeamStatsModel[] teamStats = new TeamStatsModel[teams.size()];
        Iterator iter = teams.iterator();
        int counter = 0;
        while(iter.hasNext()) {
            TeamModel tm = (TeamModel)iter.next();
            teamStats[counter++] = new TeamStatsModel(tm.getId(), tm.getCity() + " " + tm.getName());
        }

        String query = "SELECT gm.home_team_in" +
                       ", gm.away_team_in" +
                       ", gm.home_score_in" +
                       ", gm.away_score_in" +
                       ", gm.game_spread_fl" +
                       ", t.team_dome_si" +
                       ", t.team_turf_si" +
                       " FROM game_tbl gm" +
                       ", team_tbl t" +
                       ", season_series_tbl ss" +
                       " WHERE ss.series_no_in = gm.series_no_in" +
                       " AND t.team_no_in = gm.home_team_in" +
                       " AND gm.game_posted_si = 1" +
                       " AND ss.season_no_in = ?";

        try {
            Object[] args = new Object[] {new Integer(seasonId)};
            this.executeQuery( query, args );
            if(rs != null) {
                while(rs.next()) {
                    int homeTeamId = rs.getInt("home_team_in");
                    int awayTeamId = rs.getInt("away_team_in");
                    boolean isDome = (rs.getInt("team_dome_si") == 1);
                    boolean isTurf = (rs.getInt("team_turf_si") == 1);

                    TeamStatsModel tsmHome = null;
                    TeamStatsModel tsmAway = null;
                    for(int i=0; i < teamStats.length; i++) {
                        if(teamStats[i].getTeamId() == homeTeamId) tsmHome = teamStats[i];
                        if(teamStats[i].getTeamId() == awayTeamId) tsmAway = teamStats[i];
                        if(tsmHome != null && tsmAway != null) break;
                    }

                    tsmHome.addGame(rs.getInt("home_score_in"), 
                                    rs.getInt("away_score_in"),
                                    rs.getFloat("game_spread_fl"),
                                    true,
                                    isDome,
                                    isTurf );

                    tsmAway.addGame(rs.getInt("away_score_in"), 
                                    rs.getInt("home_score_in"),
                                    (rs.getFloat("game_spread_fl") * -1),
                                    false,
                                    isDome,
                                    isTurf );
                }
            }
        }
        catch(java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        finally {
            if(rs != null) try{ rs.close(); } catch(Exception e){}
        }
        
        return teamStats;
    }
    
    public TeamStatsModel[] getTeamGroupStats(int seasonId, int seriesId) throws ProcessException {
        TeamStatsModel[] teamStats = new TeamStatsModel[12];
        teamStats[0] = new TeamStatsModel(0, "Home Teams");
        teamStats[1] = new TeamStatsModel(1, "Away Teams");
        teamStats[2] = new TeamStatsModel(2, "Favored Teams");
        teamStats[3] = new TeamStatsModel(3, "Underdog Teams");
        teamStats[4] = new TeamStatsModel(4, "Indoor Teams");
        teamStats[5] = new TeamStatsModel(5, "Outdoor Teams");
        teamStats[6] = new TeamStatsModel(6, "Turf Teams");
        teamStats[7] = new TeamStatsModel(7, "Grass Teams");

        teamStats[8] = new TeamStatsModel(8, "Home Favored");
        teamStats[9] = new TeamStatsModel(9, "Home 'Dogs");
        teamStats[10] = new TeamStatsModel(10, "Away Favored");
        teamStats[11] = new TeamStatsModel(11, "Away 'Dogs");

        String query = "SELECT gm.home_team_in" +
                       ", gm.away_team_in" +
                       ", gm.home_score_in" +
                       ", gm.away_score_in" +
                       ", gm.game_spread_fl" +
                       ", home.team_dome_si" +
                       ", home.team_turf_si" +
                       ", away.team_dome_si" +
                       ", away.team_turf_si" +
                       " FROM game_tbl gm" +
                       ", team_tbl home" +
                       ", team_tbl away" +
                       ", season_series_tbl ss" +
                       " WHERE gm.game_posted_si = 1" +
                       " AND gm.home_team_in = home.team_no_in" +
                       " AND gm.away_team_in = away.team_no_in" +
                       " AND gm.series_no_in = ss.series_no_in" +
                       " AND ss.season_no_in = ?";

        Object[] args = null;
        if(seriesId > 0) {
            query += " AND gm.series_no_in = ?";
            args = new Object[] {new Integer(seasonId), new Integer(seriesId)};
        }
        else {
            args = new Object[] {new Integer(seasonId)};
        }

        try {
            this.executeQuery( query, args );
            if(rs != null) {
                while(rs.next()) {
                    int homeTeamId = rs.getInt("home_team_in");
                    int awayTeamId = rs.getInt("away_team_in");
                    int homeScore = rs.getInt("home_score_in");
                    int awayScore = rs.getInt("away_score_in");
                    float spread = rs.getFloat("game_spread_fl");
                    boolean homeDome = (rs.getInt("home.team_dome_si") == 1);
                    boolean homeTurf = (rs.getInt("home.team_turf_si") == 1);
                    boolean awayDome = (rs.getInt("away.team_dome_si") == 1);
                    boolean awayTurf = (rs.getInt("away.team_turf_si") == 1);

                    teamStats[0].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);
                    teamStats[(spread <= 0 ? 2: 3)].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);
                    teamStats[(homeDome ? 4 : 5)].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);
                    teamStats[(homeTurf ? 6 : 7)].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);

                    teamStats[1].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                    teamStats[(spread > 0 ? 2: 3)].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                    teamStats[(awayDome ? 4 : 5)].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                    teamStats[(awayTurf ? 6 : 7)].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                    
                    teamStats[(spread <= 0 ? 8 : 9)].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);
                    teamStats[(spread > 0 ? 10 : 11)].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                }
            }
        }
        catch(java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        finally {
            if(rs != null) try{ rs.close(); } catch(Exception e){}
        }
        
        return teamStats;
    }
    
	public PickStatsView getPicksStats(int seasonId) throws ProcessException {
		String query = "SELECT ugx.team_no_in, MIN(CONCAT(tm.team_city_vc, ' ', tm.team_name_vc)) as team_name"
				+ " , ss.series_no_in, ss.season_seq_no, COUNT(0) as pick_count"
				+ " FROM user_game_xref_tbl ugx"
				+ " , game_tbl gm"
				+ " , season_series_tbl ss"
				+ ", team_tbl tm"
				+ " WHERE gm.game_no_in = ugx.game_no_in"
				+ " AND ss.series_no_in = gm.series_no_in"
				+ " AND tm.team_no_in = ugx.team_no_in"
				+ " AND ss.season_no_in = ?"
				+ " AND gm.game_posted_si = 1"
				+ " GROUP BY ugx.team_no_in, ss.series_no_in, ss.season_seq_no"
				+ " ORDER BY 2, 3";
		
		PickStatsView pickStats = null;
		try {
            this.executeQuery( query, new Object[] {new Integer(seasonId)} );
            if(rs != null) {
				pickStats = new PickStatsView();
                while(rs.next()) {
					int teamId = rs.getInt( "ugx.team_no_in" );
					String teamName = rs.getString( "team_name" );
					int seriesId = rs.getInt( "ss.series_no_in" );
//					int seqNo = rs.getInt( "ss.season_seq_no" );
					int pickCount = rs.getInt( "pick_count" );
					
					pickStats.addPicks(teamId, seriesId, pickCount, teamName);
				}
			}
        }
        catch(java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        finally {
            if(rs != null) try{ rs.close(); } catch(Exception e){}
        }

		return pickStats;
	}
/*
    public PickStatsView[] getPicksStats(int seasonId, int orderBy) throws ProcessException {
        SeasonSQLController seasonSQL = new SeasonSQLController();
        Collection teams = seasonSQL.getTeamList(SportTypes.TYPE_NFL_FOOTBALL, SeasonSQLController.TEAMS_ORDER_BY_CITY);
        int seriesCount = seasonSQL.getSeriesBySeason(seasonId).size();
        seasonSQL.closeConnection();
        seasonSQL = null;

        PickStatsView[] pickStats = new PickStatsView[teams.size()];
        Iterator iter = teams.iterator();
        int counter = 0;
        while(iter.hasNext()) {
            TeamModel tm = (TeamModel)iter.next();
            pickStats[counter++] = new PickStatsView(tm.getId(), tm.getCity() + " " + tm.getName(), seriesCount);
        }

        String query = "SELECT ug.team_no_in" +
                       ", gm.series_no_in" +
                       " FROM user_game_xref_tbl ug" +
                       ", game_tbl gm" +
                       ", season_series_tbl ss" +
                       " WHERE ug.game_no_in = gm.game_no_in" +
                       " AND gm.series_no_in = ss.series_no_in" +
                       " AND ss.season_no_in = ?" +
                       " AND gm.game_posted_si = 1" +
                       " ORDER BY gm.series_no_in DESC";

        try {
            this.executeQuery( query, new Object[] {new Integer(seasonId)} );
            if(rs != null) {
                while(rs.next()) {
                    int teamId = rs.getInt("ug.team_no_in");
                    int seriesId = rs.getInt("gm.series_no_in");
                    int seriesSeq = seriesId % seriesCount;
                    if(seriesSeq == 0) seriesSeq = seriesCount;

                    for(int i=0; i < pickStats.length; i++) {
                        if(pickStats[i].getTeamId() == teamId) {
                            pickStats[i].addPick(seriesSeq);
                            break;
                        }
                    }
                }
            }
        }
        catch(java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        finally {
            if(rs != null) try{ rs.close(); } catch(Exception e){}
        }

        return pickStats;
    }
*/
}
