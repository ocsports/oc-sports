package com.ocsports.sql;

import com.ocsports.core.ProcessException;
import com.ocsports.core.SportTypes;
import com.ocsports.models.TeamModel;
import com.ocsports.models.TeamStatsModel;
import com.ocsports.views.PickStatsView;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

public class StatsSQLController extends SQLBase {

    public TeamStatsModel[] getTeamStats(int seasonId) throws ProcessException {
        SeasonSQLController seasonSql = null;

        TeamStatsModel[] teamStats = null;
        try {
            seasonSql = new SeasonSQLController();
            Collection teams = seasonSql.getTeamList(SportTypes.TYPE_NFL_FOOTBALL, SeasonSQLController.TEAMS_ORDER_BY_CITY);
            if (teams == null || teams.isEmpty()) {
                throw new ProcessException("teams are missing from database");
            }

            teamStats = new TeamStatsModel[teams.size()];
            Iterator iter = teams.iterator();
            int counter = 0;
            while (iter.hasNext()) {
                TeamModel tm = (TeamModel) iter.next();
                teamStats[counter++] = new TeamStatsModel(tm.getId(), tm.getCity() + " " + tm.getName());
            }
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
        }

        String query = "SELECT gm.home_team_in"
                + ", gm.away_team_in"
                + ", gm.home_score_in"
                + ", gm.away_score_in"
                + ", gm.game_spread_fl"
                + ", t.team_dome_si"
                + ", t.team_turf_si"
                + " FROM game_tbl gm"
                + ", team_tbl t"
                + ", season_series_tbl ss"
                + " WHERE ss.series_no_in = gm.series_no_in"
                + " AND t.team_no_in = gm.home_team_in"
                + " AND gm.game_posted_si = 1"
                + " AND ss.season_no_in = ?";

        try {
            Object[] args = new Object[]{new Integer(seasonId)};
            this.executeQuery(query, args);
            if (rs != null) {
                while (rs.next()) {
                    int homeTeamId = rs.getInt("home_team_in");
                    int awayTeamId = rs.getInt("away_team_in");
                    boolean isDome = (rs.getInt("team_dome_si") == 1);
                    boolean isTurf = (rs.getInt("team_turf_si") == 1);

                    TeamStatsModel tsmHome = null;
                    TeamStatsModel tsmAway = null;
                    for (int i = 0; i < teamStats.length; i++) {
                        if (teamStats[i].getTeamId() == homeTeamId) {
                            tsmHome = teamStats[i];
                            tsmHome.addGame(rs.getInt("home_score_in"),
                                    rs.getInt("away_score_in"),
                                    rs.getFloat("game_spread_fl"),
                                    true,
                                    isDome,
                                    isTurf);
                        }
                        if (teamStats[i].getTeamId() == awayTeamId) {
                            tsmAway = teamStats[i];
                            tsmAway.addGame(rs.getInt("away_score_in"),
                                    rs.getInt("home_score_in"),
                                    (rs.getFloat("game_spread_fl") * -1),
                                    false,
                                    isDome,
                                    isTurf);
                        }
                        if (tsmHome != null && tsmAway != null) {
                            break;
                        }
                    }
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
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

        String query = "SELECT gm.home_team_in"
                + ", gm.away_team_in"
                + ", gm.home_score_in"
                + ", gm.away_score_in"
                + ", gm.game_spread_fl"
                + ", home.team_dome_si"
                + ", home.team_turf_si"
                + ", away.team_dome_si"
                + ", away.team_turf_si"
                + " FROM game_tbl gm"
                + ", team_tbl home"
                + ", team_tbl away"
                + ", season_series_tbl ss"
                + " WHERE gm.game_posted_si = 1"
                + " AND gm.home_team_in = home.team_no_in"
                + " AND gm.away_team_in = away.team_no_in"
                + " AND gm.series_no_in = ss.series_no_in"
                + " AND ss.season_no_in = ?";

        Object[] args;
        if (seriesId > 0) {
            query += " AND gm.series_no_in = ?";
            args = new Object[]{new Integer(seasonId), new Integer(seriesId)};
        } else {
            args = new Object[]{new Integer(seasonId)};
        }

        try {
            this.executeQuery(query, args);
            if (rs != null) {
                while (rs.next()) {
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
                    teamStats[(spread <= 0 ? 2 : 3)].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);
                    teamStats[(homeDome ? 4 : 5)].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);
                    teamStats[(homeTurf ? 6 : 7)].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);

                    teamStats[1].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                    teamStats[(spread > 0 ? 2 : 3)].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                    teamStats[(awayDome ? 4 : 5)].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                    teamStats[(awayTurf ? 6 : 7)].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);

                    teamStats[(spread <= 0 ? 8 : 9)].addGame(homeScore, awayScore, spread, true, homeDome, homeTurf);
                    teamStats[(spread > 0 ? 10 : 11)].addGame(awayScore, homeScore, (spread * -1), false, homeDome, homeTurf);
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
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
                + " AND game_start_dt < ?"
                + " GROUP BY ugx.team_no_in, ss.series_no_in, ss.season_seq_no"
                + " ORDER BY 2, 3";

        PickStatsView pickStats = null;
        try {
            this.executeQuery(query, new Object[]{new Integer(seasonId), new java.util.Date()});
            if (rs != null) {
                pickStats = new PickStatsView();
                while (rs.next()) {
                    int teamId = rs.getInt("ugx.team_no_in");
                    String teamName = rs.getString("team_name");
                    int seriesId = rs.getInt("ss.series_no_in");
//					int seqNo = rs.getInt( "ss.season_seq_no" );
                    int pickCount = rs.getInt("pick_count");
                    pickStats.addPicks(teamId, seriesId, pickCount, teamName);
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return pickStats;
    }
}
