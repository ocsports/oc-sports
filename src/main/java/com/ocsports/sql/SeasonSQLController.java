/*
 * Title         SeasonSQLController.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.sql;
 */
package com.ocsports.sql;

import com.ocsports.core.ProcessException;
import com.ocsports.models.GameModel;
import com.ocsports.models.SeasonModel;
import com.ocsports.models.SeasonSeriesModel;
import com.ocsports.models.TeamConferenceModel;
import com.ocsports.models.TeamDivisionModel;
import com.ocsports.models.TeamModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SeasonSQLController extends SQLBase {

    public static final int TEAMS_ORDER_BY_ID = 1;
    public static final int TEAMS_ORDER_BY_CITY = 2;
    public static final int TEAMS_ORDER_BY_NAME = 3;
    public static final int TEAMS_ORDER_BY_ABRV = 4;
    public static final int TEAMS_ORDER_BY_DIV = 5;

    public int getCurrentSeries(int seasonId) throws ProcessException {
        String query = "SELECT series_no_in"
                + " FROM season_series_tbl"
                + " WHERE season_no_in = ?"
                + " AND series_start_dt < ?"
                + " ORDER BY series_end_dt DESC";
        try {
            int nextSeriesId = -1;

            this.executeQuery(query, new Object[]{new Integer(seasonId), new java.util.Date()});

            if (rs != null && rs.next()) {
                nextSeriesId = rs.getInt("series_no_in");
            } else {
                //if the season has not started get the first series
                query = "SELECT series_no_in"
                        + " FROM season_series_tbl"
                        + " WHERE season_no_in = ?"
                        + " ORDER BY series_start_dt";
                this.executeQuery(query, new Object[]{new Integer(seasonId)});
                if (rs != null && rs.next()) {
                    nextSeriesId = rs.getInt("series_no_in");
                }
            }

            return nextSeriesId;
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public void updateTeam(TeamModel tm) throws ProcessException {
        String query = "UPDATE team_tbl SET team_city_vc = ?"
                + ", team_name_vc = ?"
                + ", team_abrv_vc = ?"
                + ", team_div_no_in = ?"
                + ", team_dome_si = ?"
                + ", team_turf_si = ?"
                + ", team_weather_url_vc = ?"
                + " WHERE team_no_in = ?";

        Object[] args = new Object[]{tm.getCity(),
            tm.getName(),
            tm.getAbrv(),
            new Integer(tm.getDivision()),
            new Integer((tm.isDome() ? 1 : 0)),
            new Integer((tm.isTurf() ? 1 : 0)),
            tm.getWeatherURL(),
            new Integer(tm.getId())};
        this.executeUpdate(query, args);
    }

    public void updateSeason(int seasonId, int sportType, String name, String prefix, boolean active) throws ProcessException {
        String query = "UPDATE season_tbl"
                + " SET sport_type_si = ?"
                + " , season_active_si = ?"
                + " , season_name_vc = ?"
                + " , series_prefix_vc = ?"
                + " WHERE season_no_in = ?";

        Object[] args = new Object[]{new Integer(sportType),
            new Integer((active ? 1 : 0)),
            name,
            prefix,
            new Integer(seasonId)};
        this.executeUpdate(query, args);
    }

    public void updateSeasonSeries(int seriesId, long startDate, long endDate, boolean publishSpreads, boolean cleanup, boolean reminder) throws ProcessException {
        String query = "UPDATE season_series_tbl"
                + " SET series_start_dt = ?"
                + " , series_end_dt = ?"
                + " , spread_published_si = ?"
                + " , series_cleanup_si = ?"
                + " , series_reminder_si = ?"
                + " WHERE series_no_in = ?";

        Object[] args = new Object[]{new java.util.Date(startDate),
            new java.util.Date(endDate),
            new Integer((publishSpreads ? 1 : 0)),
            new Integer((cleanup ? 1 : 0)),
            new Integer((reminder ? 1 : 0)),
            new Integer(seriesId)};
        this.executeUpdate(query, args);
    }

    public void updateGame(GameModel gm) throws ProcessException {
        String query = "UPDATE game_tbl SET game_start_dt = ?"
                + ", away_team_in = ?"
                + ", home_team_in = ?"
                + ", game_spread_fl = ?"
                + ", away_score_in = ?"
                + ", home_score_in = ?"
                + ", game_notes_vc = ?"
                + ", game_posted_si = ?"
                + " WHERE game_no_in = ?";

        Object[] args = new Object[]{new java.util.Date(gm.getStartDate()),
            new Integer(gm.getAwayTeamId()),
            new Integer(gm.getHomeTeamId()),
            new Float(gm.getSpread()),
            new Integer(gm.getAwayScore()),
            new Integer(gm.getHomeScore()),
            (gm.getNotes() == null ? "" : gm.getNotes()),
            new Integer((gm.isPosted() ? 1 : 0)),
            new Integer(gm.getId())};
        this.executeUpdate(query, args);
    }

    public int createGame(GameModel gm) throws ProcessException {
        String query = "INSERT INTO game_tbl( game_no_in "
                + ", series_no_in"
                + ", game_start_dt"
                + ", away_team_in"
                + ", home_team_in"
                + ", game_spread_fl"
                + ", game_notes_vc"
                + ", game_posted_si"
                + ", default_picks_si )"
                + "VALUES( ?,?,?,?,?,?,?,?,? )";
        int newGameId = SQLBase.getNextKey(this, "game_tbl", "game_no_in");
        if (newGameId <= 0) {
            throw new ProcessException("Unable to retrieve next key for game_tbl - game_no_in");
        }
        Object[] args = new Object[]{new Integer(newGameId),
            new Integer(gm.getSeriesId()),
            new java.util.Date(gm.getStartDate()),
            new Integer(gm.getAwayTeamId()),
            new Integer(gm.getHomeTeamId()),
            new Float(gm.getSpread()),
            gm.getNotes(),
            new Boolean(false),
            new Boolean(false)};
        this.executeUpdate(query, args);
        return newGameId;
    }

    public SeasonModel getSeasonModel(int seasonId) throws ProcessException {
        String query = "SELECT * FROM season_tbl "
                + "WHERE season_no_in = ?";

        SeasonModel sm = null;
        try {
            this.executeQuery(query, new Object[]{new Integer(seasonId)});
            if (rs != null && rs.next()) {
                sm = loadSeasonModel(rs);
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }

        return sm;
    }

    public SeasonSeriesModel getSeasonSeriesModel(int seriesId) throws ProcessException {
        String query = "SELECT * FROM season_series_tbl "
                + "WHERE series_no_in = ?";

        SeasonSeriesModel sm = null;
        try {
            this.executeQuery(query, new Object[]{new Integer(seriesId)});
            if (rs != null && rs.next()) {
                sm = loadSeasonSeriesModel(rs);
                //what is the sequence for this series???
                query = "SELECT COUNT(*) FROM season_series_tbl WHERE season_no_in = ? and series_no_in <= ?";
                this.executeQuery(query, new Object[]{new Integer(sm.getSeasonId()), new Integer(seriesId)});
                if (rs != null && rs.next()) {
                    sm.setSequence(rs.getInt(1));
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }

        return sm;
    }

    public GameModel getGameModel(int gameId) throws ProcessException {
        String query = "SELECT * FROM game_tbl "
                + "WHERE game_no_in = ?";

        GameModel gm = null;
        try {
            this.executeQuery(query, new Object[]{new Integer(gameId)});
            if (rs != null && rs.next()) {
                gm = loadGameModel(rs);
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }

        return gm;
    }

    public Collection getSeasons(int sportType, boolean activeOnly) throws ProcessException {
        ArrayList seasons = new ArrayList();
        try {
            StringBuffer query = new StringBuffer();
            Object[] args = null;

            query.append("SELECT * FROM season_tbl");
            if (activeOnly && sportType > 0) {
                query.append(" WHERE season_active_si = 1");
                query.append(" AND sport_type_si = ?");
                args = new Object[]{new Integer(sportType)};
            } else if (activeOnly) {
                query.append(" WHERE season_active_si = 1");
            } else if (sportType > 0) {
                query.append(" WHERE sport_type_si = ?");
                args = new Object[]{new Integer(sportType)};
            }
            query.append(" ORDER BY season_active_si DESC, season_name_vc");

            this.executeQuery(query.toString(), args);
            if (rs != null) {
                while (rs.next()) {
                    seasons.add(this.loadSeasonModel(rs));
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        } catch (Exception e) {
            throw new ProcessException(e);
        }
        return seasons;
    }

    public Collection getSeriesBySeason(int seasonId) throws ProcessException {
        String query = "SELECT * FROM season_series_tbl "
                + "WHERE season_no_in = ? "
                + "ORDER BY series_no_in";

        ArrayList series = new ArrayList();
        try {
            this.executeQuery(query, new Object[]{new Integer(seasonId)});
            if (rs != null) {
                int seriesSeq = 0;
                while (rs.next()) {
                    SeasonSeriesModel ssm = loadSeasonSeriesModel(rs);
                    ssm.setSequence(++seriesSeq);
                    series.add(ssm);
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return series;
    }

    public Collection getGamesBySeries(int seriesId) throws ProcessException {
        String query = "SELECT * FROM game_tbl WHERE series_no_in = ? ORDER BY game_start_dt, game_no_in";

        ArrayList games = new ArrayList();
        try {
            this.executeQuery(query, new Object[]{new Integer(seriesId)});
            if (rs != null) {
                while (rs.next()) {
                    games.add(loadGameModel(rs));
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return games;
    }

    public void deleteGame(int gameId) throws ProcessException {
        String query = "DELETE FROM game_tbl where game_no_in = ?";

        this.executeUpdate(query, new Object[]{new Integer(gameId)});
    }

    public TeamModel getTeamModel(int teamId) throws ProcessException {
        String query = "SELECT * FROM team_tbl "
                + "WHERE team_no_in = ?";

        TeamModel tm = null;
        try {
            this.executeQuery(query, new Object[]{new Integer(teamId)});
            if (rs != null && rs.next()) {
                tm = this.loadTeamModel(rs);
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }

        return tm;
    }

    public HashMap getTeamMap(int sportType) throws ProcessException {
        String query = "SELECT * FROM team_tbl WHERE sport_type_si = ? ORDER by team_no_in";

        LinkedHashMap map = new LinkedHashMap();

        try {
            this.executeQuery(query, new Object[]{new Integer(sportType)});

            if (rs != null) {
                while (rs.next()) {
                    TeamModel tm = this.loadTeamModel(rs);
                    map.put(String.valueOf(tm.getId()), tm);
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }

        return map;
    }

    public Collection getTeamList(int sportType, int orderBy) throws ProcessException {
        String query = "SELECT * FROM team_tbl WHERE sport_type_si = ?";

        switch (orderBy) {
            case TEAMS_ORDER_BY_ID:
                query += " ORDER BY team_no_in";
                break;
            case TEAMS_ORDER_BY_CITY:
                query += " ORDER BY team_city_vc, team_name_vc";
                break;
            case TEAMS_ORDER_BY_NAME:
                query += " ORDER BY team_name_vc, team_no_in";
                break;
            case TEAMS_ORDER_BY_ABRV:
                query += " ORDER BY team_abrv_vc, team_no_in";
                break;
            case TEAMS_ORDER_BY_DIV:
                query += " ORDER BY team_div_no_in, team_no_in";
                break;
            default:
                query += " ORDER BY team_no_in";
                break;
        }

        ArrayList teams = new ArrayList();

        try {
            this.executeQuery(query, new Object[]{new Integer(sportType)});

            if (rs != null) {
                while (rs.next()) {
                    TeamModel tm = this.loadTeamModel(rs);
                    teams.add(tm);
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }

        return teams;
    }

    public TeamModel findTeam(String city, String name, String abrv) throws ProcessException {
        try {
            TeamModel tm = null;
            ArrayList args = new ArrayList();

            String query = "SELECT * FROM team_tbl WHERE team_no_in > 0";
            if (city != null && city.length() > 0) {
                query += " AND team_city_vc LIKE ?";
                args.add(city + "%");
            }
            if (name != null && name.length() > 0) {
                query += " AND team_name_vc LIKE ?";
                args.add(name + "%");
            }
            if (abrv != null && abrv.length() > 0) {
                query += " AND team_abrv_vc LIKE ?";
                args.add(abrv + "%");
            }
            query += " ORDER BY team_city_vc, team_name_vc";

            this.executeQuery(query, args.toArray());
            if (rs != null && rs.next()) {
                tm = this.loadTeamModel(rs);
            }
            return tm;
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public Collection getTeamConferenceList(int sportType) throws ProcessException {
        String query = "SELECT * FROM team_conference_tbl"
                + " WHERE sport_type_si = ?"
                + " ORDER by team_conf_name_vc";

        ArrayList conferenceModels = new ArrayList();

        try {
            this.executeQuery(query, new Object[]{new Integer(sportType)});

            if (rs != null) {
                while (rs.next()) {
                    TeamConferenceModel tcm = this.loadTeamConferenceModel(rs);
                    conferenceModels.add(tcm);
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }

        return conferenceModels;
    }

    public Collection getTeamDivisionList(int sportType, int conferenceId) throws ProcessException {
        String query = "SELECT td.* FROM team_division_tbl td, team_conference_tbl tc"
                + " WHERE tc.sport_type_si = ?"
                + " AND td.team_conf_no_in = tc.team_conf_no_in";
        if (conferenceId > 0) {
            query += " and td.team_conf_no_in = ?";
        }
        query += " ORDER BY team_div_name_vc";

        ArrayList divisionModels = new ArrayList();

        try {
            Object[] args = null;
            if (conferenceId > 0) {
                args = new Object[]{new Integer(sportType), new Integer(conferenceId)};
            } else {
                args = new Object[]{new Integer(sportType)};
            }
            this.executeQuery(query, args);

            if (rs != null) {
                while (rs.next()) {
                    TeamDivisionModel tcm = this.loadTeamDivisionModel(rs);
                    divisionModels.add(tcm);
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }

        return divisionModels;
    }

    public Collection findGamesToSetDefaultPicks() throws ProcessException {
        String query = "SELECT * from game_tbl"
                + " WHERE game_start_dt < ?"
                + " AND (default_picks_si IS NULL OR default_picks_si <= 0)"
                + " ORDER BY game_start_dt, game_no_in";

        ArrayList gameModels = new ArrayList();

        try {
            Object[] args = new Object[]{new java.util.Date()};
            this.executeQuery(query, args);

            if (rs != null) {
                while (rs.next()) {
                    gameModels.add(this.loadGameModel(rs));
                }
            }
            return gameModels;
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public void setGameDefaultStatus(int gameId, int status) throws ProcessException {
        String query = "UPDATE game_tbl set default_picks_si = ? WHERE game_no_in = ?";

        this.executeUpdate(query, new Object[]{new Integer(status), new Integer(gameId)});
    }

    public void setSeriesCleanupStatus(int seriesId, int status) throws ProcessException {
        String query = "UPDATE season_series_tbl SET series_cleanup_si = ?"
                + " WHERE series_no_in = ?";

        this.executeUpdate(query, new Object[]{new Integer(status), new Integer(seriesId)});
    }

    public void setSeriesReminderStatus(int seriesId, int status) throws ProcessException {
        String query = "UPDATE season_series_tbl SET series_reminder_si = ?"
                + " WHERE series_no_in = ?";

        this.executeUpdate(query, new Object[]{new Integer(status), new Integer(seriesId)});
    }

    public boolean allSeriesGamesStarted(int seriesId) throws ProcessException {
        String query = "SELECT COUNT(*) FROM game_tbl"
                + " WHERE series_no_in = ?"
                + " AND game_start_dt > ?";

        try {
            this.executeQuery(query, new Object[]{new Integer(seriesId), new java.util.Date()});
            if (rs != null && rs.next()) {
                return (rs.getInt(1) <= 0);
            } else {
                return false;
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public GameModel findGameByTeams(int seriesId, int homeTeamId, int awayTeamId) throws ProcessException {
        String query = "SELECT * FROM game_tbl"
                + " WHERE series_no_in = ?"
                + " AND away_team_in = ?"
                + " AND home_team_in = ?";
        try {
            GameModel gm = null;

            Object[] args = new Object[]{new Integer(seriesId),
                new Integer(awayTeamId),
                new Integer(homeTeamId)};
            this.executeQuery(query, args);
            if (rs != null && rs.next()) {
                gm = this.loadGameModel(rs);
            }

            return gm;
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public Collection findGamesInProgress(int sportType) throws ProcessException {
        ArrayList gameModels = new ArrayList();
        try {
            String query = "SELECT g.*"
                    + " FROM game_tbl g"
                    + " , season_series_tbl ss"
                    + " , season_tbl s"
                    + " WHERE ss.series_no_in = g.series_no_in"
                    + " AND s.season_no_in = ss.season_no_in"
                    + " AND s.season_active_si = 1"
                    + " AND g.game_posted_si = 0"
                    + " AND s.sport_type_si = ?"
                    + " AND g.game_start_dt <= ?"
                    + " ORDER BY g.game_start_dt";
            Object[] args = new Object[]{new Integer(sportType), new java.util.Date()};
            this.executeQuery(query, args);

            if (rs != null) {
                while (rs.next()) {
                    gameModels.add(this.loadGameModel(rs));
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return gameModels;
    }

    public static GameModel loadGameModel(java.sql.ResultSet rs) throws java.sql.SQLException {
        GameModel gm = new GameModel();
        gm.setId(rs.getInt("game_no_in"));
        gm.setSeriesId(rs.getInt("series_no_in"));
        gm.setStartDate(rs.getTimestamp("game_start_dt").getTime());
        gm.setHomeTeamId(rs.getInt("home_team_in"));
        gm.setAwayTeamId(rs.getInt("away_team_in"));
        gm.setSpread(rs.getFloat("game_spread_fl"));
        gm.setNotes(rs.getString("game_notes_vc"));
        gm.setDefaultPicks((rs.getInt("default_picks_si") == 1));

        gm.setPosted(rs.getInt("game_posted_si") == 1);
        gm.setHomeScore(rs.getInt("home_score_in"));
        gm.setAwayScore(rs.getInt("away_score_in"));
        return gm;
    }

    public static TeamModel loadTeamModel(java.sql.ResultSet rs) throws java.sql.SQLException {
        TeamModel tm = new TeamModel();
        tm.setId(rs.getInt("team_no_in"));
        tm.setCity(rs.getString("team_city_vc"));
        tm.setName(rs.getString("team_name_vc"));
        tm.setAbrv(rs.getString("team_abrv_vc"));
        tm.setDivision(rs.getInt("team_div_no_in"));
        tm.setDome(rs.getInt("team_dome_si") == 1);
        tm.setTurf(rs.getInt("team_turf_si") == 1);
        tm.setWeatherURL(rs.getString("team_weather_url_vc"));
        return tm;
    }

    public static SeasonSeriesModel loadSeasonSeriesModel(java.sql.ResultSet rs) throws java.sql.SQLException {
        SeasonSeriesModel sm = new SeasonSeriesModel();
        sm.setId(rs.getInt("series_no_in"));
        sm.setSeasonId(rs.getInt("season_no_in"));
        sm.setStartDate(rs.getTimestamp("series_start_dt").getTime());
        sm.setEndDate(rs.getTimestamp("series_end_dt").getTime());
        sm.setSpreadPublished(rs.getInt("spread_published_si") == 1);
        sm.setReminderEmail(rs.getInt("series_reminder_si") == 1);
        sm.setUserCleanup(rs.getInt("series_cleanup_si") == 1);
        sm.setSequence(rs.getInt("season_seq_no"));
        return sm;
    }

    public static SeasonModel loadSeasonModel(java.sql.ResultSet rs) throws java.sql.SQLException {
        SeasonModel sm = new SeasonModel();
        sm.setId(rs.getInt("season_no_in"));
        sm.setName(rs.getString("season_name_vc"));
        sm.setSeriesPrefix(rs.getString("series_prefix_vc"));
        sm.setSportType(rs.getInt("sport_type_si"));
        sm.setActive(rs.getInt("season_active_si") == 1);
        return sm;
    }

    public static TeamDivisionModel loadTeamDivisionModel(java.sql.ResultSet rs) throws java.sql.SQLException {
        TeamDivisionModel tdm = new TeamDivisionModel();
        tdm.setId(rs.getInt("team_div_no_in"));
        tdm.setName(rs.getString("team_div_name_vc"));
        tdm.setConference(rs.getInt("team_conf_no_in"));
        return tdm;
    }

    public static TeamConferenceModel loadTeamConferenceModel(java.sql.ResultSet rs) throws java.sql.SQLException {
        TeamConferenceModel tcm = new TeamConferenceModel();
        tcm.setId(rs.getInt("team_conf_no_in"));
        tcm.setName(rs.getString("team_conf_name_vc"));
        tcm.setSport(rs.getInt("sport_type_si"));
        return tcm;
    }
}
