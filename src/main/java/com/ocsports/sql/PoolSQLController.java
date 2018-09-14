package com.ocsports.sql;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import com.ocsports.core.ProcessException;
import com.ocsports.core.Status;
import com.ocsports.models.AuditPickModel;
import com.ocsports.models.GameModel;
import com.ocsports.models.LeagueModel;
import com.ocsports.models.LeagueSeriesXrefModel;
import com.ocsports.models.LockStandingsModel;
import com.ocsports.models.StandingsModel;
import com.ocsports.models.SurvivorStandingsModel;
import com.ocsports.models.UserGameXrefModel;
import com.ocsports.models.UserModel;
import com.ocsports.models.UserSeriesXrefModel;
import com.ocsports.reports.ReportHelper;


public class PoolSQLController extends SQLBase {

    public SortedMap getStandings(int leagueId, int seriesId) throws ProcessException {
        String query = "SELECT *"
                + " FROM user_game_xref_tbl ugx, game_tbl g, user_tbl u"
                + " WHERE ugx.game_no_in = g.game_no_in"
                + " AND ugx.user_no_in = u.user_no_in"
                + " AND u.league_no_in = ?"
                + " AND ugx.ugame_status_si IN (?,?)";
        if (seriesId > 0) {
            query += " AND g.series_no_in = ?";
        }
        query += " ORDER BY ugx.user_no_in, g.series_no_in, ugx.game_no_in";

        SortedMap standings = new TreeMap();
        String usersList = "";
        try {
            Object[] args = null;
            if (seriesId > 0) {
                args = new Object[]{new Integer(leagueId),
                    new Integer(Status.GAME_STATUS_WON),
                    new Integer(Status.GAME_STATUS_LOST),
                    new Integer(seriesId)};
            } else {
                args = new Object[]{new Integer(leagueId),
                    new Integer(Status.GAME_STATUS_WON),
                    new Integer(Status.GAME_STATUS_LOST)};
            }
            executeQuery(query, args);

            NumberFormat fmt = NumberFormat.getInstance();
            fmt.setMinimumIntegerDigits(4);
            fmt.setMaximumIntegerDigits(4);
            fmt.setParseIntegerOnly(true);
            fmt.setGroupingUsed(false);

            int seriesWins = 0;
            int seriesLosses = 0;
            int seriesTies = 0;
            int userWins = 0;
            int userLosses = 0;
            int userTies = 0;

            int lastUserId = -1;
            int lastSeriesId = -1;
            UserModel um = null;
            HashMap userSeries = new HashMap();
            if (rs != null) {
                while (rs.next()) {
                    int userId = rs.getInt("user_no_in");
                    int seriesKey = rs.getInt("series_no_in");

                    if (lastUserId != userId) {
                        if (lastUserId != -1) {
                            //System.out.println("series=" + lastSeriesId + "; wins=" + seriesWins);
                            userSeries.put(String.valueOf(lastSeriesId), seriesWins + "," + seriesLosses);
                            userWins += seriesWins;
                            userLosses += seriesLosses;
                            StandingsModel sm = new StandingsModel(um, userWins, userLosses, userSeries);
                            standings.put(fmt.format(1000 - userWins) + um.getLoginId().toLowerCase(), sm);
                            //System.out.println("standings: user==" + fmt.format(1000-userWins) + um.getLoginId() + "; totalWins=" + userWins + "; totalLosses=" + userLosses);
                            seriesWins = 0;
                            seriesLosses = 0;
                            userWins = 0;
                            userLosses = 0;
                            lastSeriesId = -1;
                            userSeries = new HashMap();
                        }
                        um = UserSQLController.loadUserModel(rs);
                        if (!usersList.equals("")) {
                            usersList += ",";
                        }
                        usersList += String.valueOf(um.getUserId());
                    } else if (lastSeriesId != seriesKey && lastSeriesId != -1) {
                        //System.out.println("series=" + lastSeriesId + "; wins=" + seriesWins);
                        userSeries.put(String.valueOf(lastSeriesId), seriesWins + "," + seriesLosses);
                        userWins += seriesWins;
                        userLosses += seriesLosses;
                        seriesWins = 0;
                        seriesLosses = 0;
                    }

                    int status = rs.getInt("ugame_status_si");
                    if (status == Status.GAME_STATUS_WON) {
                        seriesWins++;
                    }
                    if (status == Status.GAME_STATUS_LOST) {
                        seriesLosses++;
                    }

                    lastUserId = userId;
                    lastSeriesId = seriesKey;
                }
                // process last record
                if (lastUserId != -1) {
                    //System.out.println("series=" + lastSeriesId + "; wins=" + seriesWins);
                    userSeries.put(String.valueOf(lastSeriesId), seriesWins + "," + seriesLosses);
                    userWins += seriesWins;
                    userLosses += seriesLosses;
                    StandingsModel sm = new StandingsModel(um, userWins, userLosses, userSeries);
                    standings.put(fmt.format(1000 - userWins) + um.getLoginId().toLowerCase(), sm);
                    //System.out.println("standings: user==" + um.getUserId() + "; totalWins=" + userWins + "; totalLosses=" + userLosses);
                }
            }

            //NOW, must retrieve users without any picks
            if (usersList == null || usersList.equals("")) {
                query = "SELECT * FROM user_tbl WHERE league_no_in = ?";
            } else {
                query = "SELECT * FROM user_tbl where league_no_in = ? AND user_no_in NOT IN (" + usersList + ")";
            }

            executeQuery(query, new Object[]{new Integer(leagueId)});
            if (rs != null) {
                while (rs.next()) {
                    UserModel um2 = UserSQLController.loadUserModel(rs);
                    StandingsModel sm2 = new StandingsModel(um2, 0, 0, null);
                    standings.put(fmt.format(1000) + um2.getLoginId().toLowerCase(), sm2);
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return standings;
    }

    public SortedMap getLockStandings(int leagueId, int seriesId) throws ProcessException {
        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMinimumIntegerDigits(4);
        fmt.setMaximumIntegerDigits(4);
        fmt.setParseIntegerOnly(true);
        fmt.setGroupingUsed(false);

        SortedMap standings = new TreeMap();
        String query = "SELECT * FROM user_series_xref_tbl usx, user_tbl u"
                + " WHERE usx.user_no_in = u.user_no_in"
                + " AND u.league_no_in = ?";
        if (seriesId > 0) {
            query += " AND usx.series_no_in = ?";
        }
        query += " ORDER BY usx.user_no_in, usx.series_no_in";
        try {
            if (seriesId > 0) {
                executeQuery(query, new Object[]{new Integer(leagueId), new Integer(seriesId)});
            } else {
                executeQuery(query, new Object[]{new Integer(leagueId)});
            }

            String usersList = "";
            if (rs != null) {
                HashMap seriesMap = new HashMap();
                int lastUserId = -1;
                UserModel lastUser = null;
                int wins = 0;
                int losses = 0;
                int ties = 0;
                while (rs.next()) {
                    int userId = rs.getInt("user_no_in");
                    int series = rs.getInt("series_no_in");

                    if (!usersList.equals("")) {
                        usersList += ",";
                    }
                    usersList += String.valueOf(userId);

                    if (userId != lastUserId) {
                        if (lastUserId != -1) {
                            LockStandingsModel lsm = new LockStandingsModel(lastUser, wins, losses, ties, seriesMap);
                            standings.put(fmt.format(1000 - wins) + fmt.format(1000 - ties) + fmt.format(losses) + lastUser.getLoginId().toLowerCase(), lsm);
                            wins = 0;
                            losses = 0;
                            ties = 0;
                            seriesMap = new HashMap();
                        }
                        lastUser = UserSQLController.loadUserModel(rs);
                    }
                    seriesMap.put(String.valueOf(series), this.loadUserSeriesXrefModel(rs));

                    int status = rs.getInt("lock_status_si");
                    if (status == Status.LOCK_STATUS_WON) {
                        wins++;
                    }
                    if (status == Status.LOCK_STATUS_LOST) {
                        losses++;
                    }
                    if (status == Status.LOCK_STATUS_TIE) {
                        ties++;
                    }
                    lastUserId = userId;
                }
                if (lastUserId != -1) {
                    LockStandingsModel lsm = new LockStandingsModel(lastUser, wins, losses, ties, seriesMap);
                    standings.put(fmt.format(1000 - wins) + fmt.format(1000 - ties) + fmt.format(losses) + lastUser.getLoginId().toLowerCase(), lsm);
                }
            }

            //now must get remaining users in league, if any
            if (usersList.equals("")) {
                query = "SELECT * FROM user_tbl WHERE league_no_in = ? ORDER BY user_login_id_vc";
            } else {
                query = "SELECT * FROM user_tbl WHERE league_no_in = ? AND user_no_in NOT IN (" + usersList + ") ORDER BY user_login_id_vc";
            }
            executeQuery(query, new Object[]{new Integer(leagueId)});
            if (rs != null) {
                while (rs.next()) {
                    UserModel um2 = UserSQLController.loadUserModel(rs);
                    LockStandingsModel lsm = new LockStandingsModel(um2, 0, 0, 0, null);
                    standings.put(fmt.format(1000) + fmt.format(1000) + fmt.format(0) + um2.getLoginId().toLowerCase(), lsm);
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return standings;
    }

    public SortedMap getSurvivorStandings(int leagueId, int seriesId) throws ProcessException {
        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMinimumIntegerDigits(4);
        fmt.setMaximumIntegerDigits(4);
        fmt.setParseIntegerOnly(true);
        fmt.setGroupingUsed(false);

        SortedMap standings = new TreeMap();
        String query = "SELECT * FROM user_series_xref_tbl usx, user_tbl u"
                + " WHERE usx.user_no_in = u.user_no_in"
                + " AND u.league_no_in = ?";
        if (seriesId > 0) {
            query += " AND usx.series_no_in = ?";
        }
        query += " ORDER BY usx.user_no_in, usx.series_no_in DESC";
        try {
            if (seriesId > 0) {
                executeQuery(query, new Object[]{new Integer(leagueId), new Integer(seriesId)});
            } else {
                executeQuery(query, new Object[]{new Integer(leagueId)});
            }
            String usersList = "";
            if (rs != null) {
                HashMap seriesMap = new HashMap();
                int lastUserId = -1;
                boolean lastUserEliminated = false;
                int lastUserSeriesEliminated = 0;
                UserModel lastUser = null;
                while (rs.next()) {
                    int userId = rs.getInt("user_no_in");
                    int series = rs.getInt("series_no_in");

                    if (!usersList.equals("")) {
                        usersList += ",";
                    }
                    usersList += String.valueOf(userId);

                    if (userId != lastUserId) {
                        if (lastUserId != -1) {
                            SurvivorStandingsModel ssm = new SurvivorStandingsModel(lastUser, seriesMap);
                            standings.put((lastUserEliminated ? String.valueOf(9999 - lastUserSeriesEliminated) : "0") + lastUser.getLoginId().toLowerCase(), ssm);
                            seriesMap = new HashMap();
                        }
                        lastUser = UserSQLController.loadUserModel(rs);
                        lastUserEliminated = false;
                    }
                    UserSeriesXrefModel usxm = this.loadUserSeriesXrefModel(rs);
                    seriesMap.put(String.valueOf(series), usxm);

                    lastUserId = userId;
                    if (usxm.getSurvivorStatus() == Status.SURVIVOR_STATUS_LOST) {
                        lastUserEliminated = true;
                        lastUserSeriesEliminated = series;
                    }
                }
                if (lastUserId != -1) {
                    SurvivorStandingsModel ssm = new SurvivorStandingsModel(lastUser, seriesMap);
                    standings.put((lastUserEliminated ? String.valueOf(9999 - lastUserSeriesEliminated) : "0") + lastUser.getLoginId().toLowerCase(), ssm);
                }
            }

            //now must get remaining users, if any
            if (usersList.equals("")) {
                query = "SELECT * FROM user_tbl WHERE league_no_in = ? ORDER BY user_login_id_vc";
            } else {
                query = "SELECT * FROM user_tbl WHERE league_no_in = ? AND user_no_in NOT IN (" + usersList + ") ORDER BY user_login_id_vc";
            }
            executeQuery(query, new Object[]{new Integer(leagueId)});
            if (rs != null) {
                while (rs.next()) {
                    UserModel um2 = UserSQLController.loadUserModel(rs);
                    SurvivorStandingsModel ssm = new SurvivorStandingsModel(um2, null);
                    standings.put("0" + um2.getLoginId().toLowerCase(), ssm);
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return standings;
    }

    public SortedMap getTeamStandings(int sportType) throws ProcessException {
        SortedMap map = new TreeMap();
        try {
            String query = "SELECT t.team_no_in, '1', count(*)"
                    + " FROM team_tbl t, game_tbl g"
                    + " WHERE t.sport_type_si = ?"
                    + " AND t.team_no_in = g.winning_team_in"
                    + " GROUP BY t.team_no_in"
                    + " UNION"
                    + " SELECT t.team_no_in, '2', count(*)"
                    + " FROM team_tbl t, game_tbl g"
                    + " WHERE t.sport_type_si = ?"
                    + " AND (t.team_no_in = g.home_team_in OR t.team_no_in = g.away_team_in)"
                    + " AND g.winning_team_in > 0"
                    + " AND t.team_no_in != g.winning_team_in"
                    + " GROUP BY t.team_no_in"
                    + " UNION"
                    + " SELECT t.team_no_in, '3', count(*)"
                    + " FROM team_tbl t, game_tbl g"
                    + " WHERE t.sport_type_si = ?"
                    + " AND (t.team_no_in = g.home_team_in OR t.team_no_in = g.away_team_in)"
                    + " AND g.home_score_in = g.away_score_in"
                    + " GROUP BY t.team_no_in"
                    + " UNION"
                    + " SELECT t.team_no_in, '4', count(*)"
                    + " FROM team_tbl t, game_tbl g"
                    + " WHERE t.sport_type_si = ?"
                    + " AND (  (t.team_no_in = g.home_team_in AND (g.home_score_in + g.game_spread_fl) >= g.away_score_in)"
                    + "     OR (t.team_no_in = g.away_team_in AND (g.home_score_in + g.game_spread_fl) <= g.away_score_in)"
                    + "     )"
                    + " GROUP BY t.team_no_in"
                    + " ORDER BY 1, 2";
            Integer sportTypeID = new Integer(sportType);
            Object[] args = new Object[]{sportTypeID, sportTypeID, sportTypeID, sportTypeID};

            executeQuery(query, args);
            if (rs != null) {
                int wins = 0, losses = 0, ties = 0, beatSpread = 0;
                int lastTeamId = 0, type = 0;
                while (rs.next()) {
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return map;
    }

    public HashMap getUserPicksXrefMap(int leagueId, int seriesId, int userId) throws ProcessException {
        String query = "SELECT ugx.*"
                + " FROM user_game_xref_tbl ugx, game_tbl g, user_tbl u"
                + " WHERE ugx.user_no_in = u.user_no_in"
                + " AND ugx.game_no_in = g.game_no_in"
                + " AND u.league_no_in = ?"
                + " AND g.series_no_in = ?";
        if (userId > 0) {
            query += " AND ugx.user_no_in = ?";
        }

        HashMap picks = new HashMap();
        try {
            Object[] args;
            if (userId > 0) {
                args = new Object[]{new Integer(leagueId), new Integer(seriesId), new Integer(userId)};
            } else {
                args = new Object[]{new Integer(leagueId), new Integer(seriesId)};
            }
            executeQuery(query, args);
            if (rs != null) {
                while (rs.next()) {
                    UserGameXrefModel ugxm = loadUserGameXrefModel(rs);
                    picks.put(String.valueOf(ugxm.getUserId() + "^" + ugxm.getGameId()), ugxm);
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return picks;
    }

    public HashMap getUserSeriesXrefMap(int leagueId, int seriesId) throws ProcessException {
        String query = "SELECT usx.*"
                + " FROM user_series_xref_tbl usx, user_tbl u"
                + " WHERE usx.user_no_in = u.user_no_in"
                + " AND u.league_no_in = ?";
        if (seriesId > 0) {
            query += " AND usx.series_no_in = ?";
        }
        query += " ORDER BY usx.series_no_in";

        HashMap picks = new HashMap();
        try {
            Object[] args;
            if (seriesId > 0) {
                args = new Object[]{new Integer(leagueId), new Integer(seriesId)};
            } else {
                args = new Object[]{new Integer(leagueId)};
            }
            executeQuery(query, args);
            if (rs != null) {
                while (rs.next()) {
                    UserSeriesXrefModel usxm = loadUserSeriesXrefModel(rs);
                    picks.put(usxm.getUserId() + "," + usxm.getSeriesId(), usxm);
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return picks;
    }

    public Collection getUserSeries(int userId) throws ProcessException {
        String query = "SELECT usx.*"
                + " FROM user_series_xref_tbl usx"
                + " WHERE usx.user_no_in = ?"
                + " ORDER BY usx.series_no_in";
        Collection models = new ArrayList();
        try {
            Object[] args;
            args = new Object[]{new Integer(userId)};
            executeQuery(query, args);
            if (rs != null) {
                while (rs.next()) {
                    UserSeriesXrefModel usxm = loadUserSeriesXrefModel(rs);
                    models.add(usxm);
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return models;
    }

    public Collection getUserPicksBySeries(int seriesId, int userId) throws ProcessException {
        String query = "select ugx.* from user_game_xref_tbl ugx, game_tbl g "
                + "WHERE g.game_no_in = ugx.game_no_in "
                + "AND g.series_no_in = ? AND ugx.user_no_in = ? "
                + "ORDER BY ugx.game_no_in";

        ArrayList userPicks = new ArrayList();
        try {
            Object[] args = new Object[]{new Integer(seriesId), new Integer(userId)};
            executeQuery(query, args);
            if (rs != null) {
                while (rs.next()) {
                    userPicks.add(loadUserGameXrefModel(rs));
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return userPicks;
    }

    public UserGameXrefModel getUserGameXrefModel(int gameId, int userId) throws ProcessException {
        String query = "SELECT * FROM user_game_xref_tbl "
                + "WHERE game_no_in = ? "
                + "AND user_no_in = ?";

        UserGameXrefModel ugm = null;
        try {
            Object[] args = new Object[]{new Integer(gameId), new Integer(userId)};
            executeQuery(query, args);
            if (rs != null && rs.next()) {
                ugm = loadUserGameXrefModel(rs);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return ugm;
    }

    public UserSeriesXrefModel getUserSeriesXrefModel(int seriesId, int userId) throws ProcessException {
        String query = "select * from user_series_xref_tbl "
                + "WHERE series_no_in = ? "
                + "AND user_no_in = ?";

        UserSeriesXrefModel usm = null;
        try {
            Object[] args = new Object[]{new Integer(seriesId), new Integer(userId)};
            executeQuery(query, args);
            if (rs != null && rs.next()) {
                usm = loadUserSeriesXrefModel(rs);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return usm;
    }

    public void createUserGameXref(int gameId, int userId, int selectedTeamId, int defaultPick) throws ProcessException {
        String query = "INSERT INTO user_game_xref_tbl( game_no_in "
                + ", user_no_in "
                + ", team_no_in "
                + ", ugame_notes_vc"
                + ", ugame_default_si"
                + ", ugame_status_si"
                + ", ugame_timestamp_dt) "
                + "VALUES( ?,?,?,?,?,?,? )";

        Object[] args = new Object[]{new Integer(gameId),
            new Integer(userId),
            new Integer(selectedTeamId),
            "",
            new Integer(defaultPick),
            new Integer(Status.GAME_STATUS_NO_DECISION),
            new java.util.Date()};
        executeUpdate(query, args);
        createAuditPick(new java.util.Date(), userId, gameId, -1, selectedTeamId, AuditPickModel.TYPE_GAME);
    }

    public void updateUserGameStatus(int gameId, int teamId, int status) throws ProcessException {
        String query = "UPDATE user_game_xref_tbl SET ugame_status_si = ?"
                + " WHERE game_no_in = ?"
                + " AND team_no_in = ?";
        Object[] args = new Object[]{new Integer(status), new Integer(gameId), new Integer(teamId)};
        executeUpdate(query, args);
    }

    public void updateUserSurvivorStatus(int seriesId, int userId, int status) throws ProcessException {
        String query = "UPDATE user_series_xref_tbl"
                + " SET survivor_status_si = ?"
                + " WHERE series_no_in = ?"
                + " AND user_no_in = ?";
        Object[] args = new Object[]{new Integer(status), new Integer(seriesId), new Integer(userId)};
        executeUpdate(query, args);
    }

    public void updateUserLockStatus(int seriesId, int userId, int status) throws ProcessException {
        String query = "UPDATE user_series_xref_tbl"
                + " SET lock_status_si = ?"
                + " WHERE series_no_in = ?"
                + " AND user_no_in = ?";
        Object[] args = new Object[]{new Integer(status), new Integer(seriesId), new Integer(userId)};
        executeUpdate(query, args);
    }

    public void updateSurvivorStatus(int seriesId, int teamId, int status) throws ProcessException {
        String query = "UPDATE user_series_xref_tbl"
                + " SET survivor_status_si = ?"
                + " WHERE series_no_in = ?"
                + " AND survivor_team_in = ?";
        Object[] args = new Object[]{new Integer(status), new Integer(seriesId), new Integer(teamId)};
        executeUpdate(query, args);
    }

    public void updateLockStatus(int seriesId, int teamId, int status) throws ProcessException {
        String query = "UPDATE user_series_xref_tbl"
                + " SET lock_status_si = ?"
                + " WHERE series_no_in = ?"
                + " AND lock_team_in = ?";
        Object[] args = new Object[]{new Integer(status), new Integer(seriesId), new Integer(teamId)};
        executeUpdate(query, args);
    }

    public void removeUserGameXref(int gameId, int userId) throws ProcessException {
        String query = "DELETE FROM user_game_xref_tbl "
                + "WHERE user_no_in = ? AND game_no_in = ?";
        Object[] args = new Object[]{new Integer(userId), new Integer(gameId)};
        executeUpdate(query, args);
    }

    public void createUserSeriesXref(int seriesId, int userId, int lockOfWeek, int survivor, String notes) throws ProcessException {
        java.util.Date currentDt = new java.util.Date();
        String query = "INSERT INTO user_series_xref_tbl( user_no_in"
                + ", series_no_in"
                + ", survivor_team_in"
                + ", lock_team_in"
                + ", useries_notes_vc"
                + ", survivor_status_si"
                + ", lock_status_si"
                + ", useries_timestamp_dt)"
                + " VALUES( ?,?,?,?,?,?,?,? )";

        Object[] args = new Object[]{new Integer(userId),
            new Integer(seriesId),
            new Integer(survivor),
            new Integer(lockOfWeek),
            (notes.length() > 255 ? notes.substring(0, 255) : notes),
            new Integer(Status.SURVIVOR_STATUS_NO_DECISION),
            new Integer(Status.LOCK_STATUS_NO_DECISION),
            currentDt};
        executeUpdate(query, args);
        if (lockOfWeek > 0) {
            createAuditPick(currentDt, userId, -1, seriesId, lockOfWeek, AuditPickModel.TYPE_LOCK);
        }
        if (survivor > 0) {
            createAuditPick(currentDt, userId, -1, seriesId, survivor, AuditPickModel.TYPE_SURVIVOR);
        }
    }

    public void removeUserSeriesXref(int seriesId, int userId) throws ProcessException {
        String query = "DELETE FROM user_series_xref_tbl"
                + " WHERE series_no_in = ?"
                + " AND user_no_in = ?";
        executeUpdate(query, new Object[]{new Integer(seriesId), new Integer(userId)});
    }

    public void setGameDefaultPicks(GameModel gm) throws ProcessException {
        try {
            String userIds = "";
            //get all users who HAVE made a pick for this game
            String query = "SELECT user_no_in FROM user_game_xref_tbl"
                    + " WHERE game_no_in = ?"
                    + " AND team_no_in > 0"
                    + " ORDER BY user_no_in ASC";
            executeQuery(query, new Object[]{new Integer(gm.getId())});
            if (rs != null) {
                while (rs.next()) {
                    if (userIds.equals("")) {
                        userIds = String.valueOf(rs.getInt("user_no_in"));
                    } else {
                        userIds += "," + String.valueOf(rs.getInt("user_no_in"));
                    }
                }
            }

            //get all users who HAVE NOT made a pick for this game
            query = "SELECT u.* FROM user_tbl u"
                    + "  , league_tbl l"
                    + "  , season_series_tbl ss"
                    + " WHERE ss.series_no_in = ?"
                    + " AND l.season_no_in = ss.season_no_in"
                    + " AND u.league_no_in = l.league_no_in";
            if (userIds.length() > 0) {
                query += " AND u.user_no_in NOT IN (" + userIds + ")";
            }
            query += " ORDER BY u.league_no_in, u.user_no_in";
            Object[] args = new Object[]{new Integer(gm.getSeriesId())};
            executeQuery(query, args);

            //must put in a list to complete the result set
            //since we will re-use for each user
            Collection userModels = null;
            if (rs != null) {
                userModels = new ArrayList();
                while (rs.next()) {
                    userModels.add(UserSQLController.loadUserModel(rs));
                }
            }

            if (userModels != null) {
                Iterator iter = userModels.iterator();
                while (iter.hasNext()) {
                    UserModel um = (UserModel) iter.next();
                    int pickTeam = -1;
                    switch (um.getDefaultPick()) {
                        case Status.PICK_HOME_TEAM:
                            pickTeam = gm.getHomeTeamId();
                            break;
                        case Status.PICK_AWAY_TEAM:
                            pickTeam = gm.getAwayTeamId();
                            break;
                        case Status.PICK_FAVORED:
                            pickTeam = gm.getFavoredTeam();
                            break;
                        case Status.PICK_UNDERDOG:
                            pickTeam = gm.getUnderdogTeam();
                            break;
                    }
                    if (pickTeam > 0) {
                        System.out.println("updating default pick: user=" + um.getUserId() + "; defaultPick: " + um.getDefaultPick() + "; game=" + gm.getId() + "; pickTeam=" + pickTeam);
                        removeUserGameXref(gm.getId(), um.getUserId());
                        createUserGameXref(gm.getId(), um.getUserId(), pickTeam, 1);
                    }
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public HashMap getUsedSurvivorTeams(int userId, int seriesOrder) throws ProcessException {
        String query = "SELECT survivor_team_in"
                + " FROM user_series_xref_tbl usx, season_series_tbl s"
                + " where usx.series_no_in = s.series_no_in"
                + " AND user_no_in = ?"
                + " AND s.series_no_in < ?"
                + " AND usx.survivor_team_in > 0";

        HashMap teamsAlreadyUsed = new HashMap();
        try {
            executeQuery(query, new Object[]{new Integer(userId), new Integer(seriesOrder)});
            if (rs != null) {
                while (rs.next()) {
                    teamsAlreadyUsed.put(String.valueOf(rs.getInt(1)), "");
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return teamsAlreadyUsed;
    }

    public int getUserSurvivorStatus(int userId) throws ProcessException {
        String query = "SELECT count(*) FROM user_series_xref_tbl"
                + " WHERE user_no_in = ?"
                + " AND survivor_status_si = ?";
        int count = 0;
        try {
            executeQuery(query, new Object[]{new Integer(userId), new Integer(Status.SURVIVOR_STATUS_LOST)});
            if (rs != null && rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return (count > 0 ? Status.SURVIVOR_STATUS_LOST : Status.SURVIVOR_STATUS_NO_DECISION);
    }

    public void createAuditPick(java.util.Date dt, int userId, int gameId, int seriesId, int teamId, String type) throws ProcessException {
        String query = "INSERT INTO audit_picks_tbl( pick_timestamp_dt"
                + ", pick_type_vc"
                + ", user_no_in"
                + ", game_no_in"
                + ", series_no_in"
                + ", team_no_in"
                + ") VALUES(?,?,?,?,?,?)";

        Object[] args = new Object[]{(dt != null ? dt : new java.util.Date()),
            type,
            new Integer(userId),
            new Integer(gameId),
            new Integer(seriesId),
            new Integer(teamId)};
        executeUpdate(query, args);
    }

    public void cleanupUserSeries(int seriesId) throws ProcessException {
        try {
            //get users WITH user-series xref records
            String query = "SELECT user_no_in FROM user_series_xref_tbl WHERE series_no_in = ?";
            String userIN = "";
            this.executeQuery(query, new Object[]{new Integer(seriesId)});
            if (rs != null) {
                while (rs.next()) {
                    if (!userIN.equals("")) {
                        userIN += ",";
                    }
                    userIN += String.valueOf(rs.getInt("user_no_in"));
                }
            }

            //get users WITHOUT user-series xref records
            query = "SELECT u.user_no_in FROM user_tbl u"
                    + " , league_tbl l"
                    + " , season_series_tbl ss"
                    + " WHERE ss.series_no_in = ?"
                    + " AND l.season_no_in = ss.season_no_in"
                    + " AND u.league_no_in = l.league_no_in";
            if (!userIN.equals("")) {
                query += " AND u.user_no_in NOT IN (" + userIN + ")";
            }
            Object[] args = new Object[]{new Integer(seriesId)};
            executeQuery(query, args);

            //must put in a list to complete the result set
            //since we will re-use for each user
            Collection userList = null;
            if (rs != null) {
                userList = new ArrayList();
                while (rs.next()) {
                    userList.add(new Integer(rs.getInt("user_no_in")));
                }
            }

            if (userList != null) {
                Iterator iter = userList.iterator();
                while (iter.hasNext()) {
                    int userId = ((Integer) iter.next()).intValue();
                    System.out.println("Creating User-Series-Xref records for userId=" + userId);
                    createUserSeriesXref(seriesId, userId, -1, -1, "");
                    updateUserLockStatus(seriesId, userId, Status.LOCK_STATUS_LOST);
                    updateUserSurvivorStatus(seriesId, userId, Status.SURVIVOR_STATUS_LOST);
                }
            }

            //2nd step - set status for anyone who did not pick a lock
            query = "UPDATE user_series_xref_tbl SET lock_status_si = ? WHERE series_no_in = ? AND lock_status_si = ?";
            Object[] args2 = new Object[]{new Integer(Status.LOCK_STATUS_LOST),
                new Integer(seriesId),
                new Integer(Status.LOCK_STATUS_NO_DECISION)};
            executeUpdate(query, args2);

            //3rd step - set status for anyone who did notpick a survivor
            query = "UPDATE user_series_xref_tbl SET survivor_status_si = ? WHERE series_no_in = ? AND survivor_status_si = ?";
            Object[] args3 = new Object[]{new Integer(Status.SURVIVOR_STATUS_LOST),
                new Integer(seriesId),
                new Integer(Status.SURVIVOR_STATUS_NO_DECISION)};
            executeUpdate(query, args3);
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public boolean userNeedsReminder(UserModel um, int seriesId) throws ProcessException {
        if (!um.sendWarning()) {
            return false;
        }

        String query = "SELECT 1, count(*) FROM game_tbl WHERE series_no_in = ?"
                + " UNION ALL "
                + "SELECT 2, count(*) FROM user_game_xref_tbl x, game_tbl g WHERE x.game_no_in = g.game_no_in AND g.series_no_in = ? and x.user_no_in = ?"
                + " ORDER BY 1 ASC";
        try {
            Object[] args = new Object[]{new Integer(seriesId),
                new Integer(seriesId),
                new Integer(um.getUserId())};
            executeQuery(query, args);
            rs.next();
            int gameCount = rs.getInt(2);
            rs.next();
            int userGameCount = rs.getInt(2);
            return (gameCount > userGameCount);
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public void postGame(GameModel gm) throws ProcessException {
        if (!gm.isPosted()) {
            return;
        }

        float adjHomeScore = gm.getHomeScore() + gm.getSpread();
        float adjAwayScore = gm.getAwayScore();
        int homeStatus = (adjHomeScore >= adjAwayScore ? Status.GAME_STATUS_WON : Status.GAME_STATUS_LOST);
        int awayStatus = (adjAwayScore >= adjHomeScore ? Status.GAME_STATUS_WON : Status.GAME_STATUS_LOST);
        updateUserGameStatus(gm.getId(), gm.getHomeTeamId(), homeStatus);
        updateUserGameStatus(gm.getId(), gm.getAwayTeamId(), awayStatus);

        int homeLockStatus = Status.LOCK_STATUS_NO_DECISION;
        int awayLockStatus = Status.LOCK_STATUS_NO_DECISION;
        if (adjHomeScore == adjAwayScore) {
            homeLockStatus = Status.LOCK_STATUS_TIE;
            awayLockStatus = Status.LOCK_STATUS_TIE;
        } else if (adjHomeScore > adjAwayScore) {
            homeLockStatus = Status.LOCK_STATUS_WON;
            awayLockStatus = Status.LOCK_STATUS_LOST;
        } else if (adjAwayScore > adjHomeScore) {
            homeLockStatus = Status.LOCK_STATUS_LOST;
            awayLockStatus = Status.LOCK_STATUS_WON;
        }
        updateLockStatus(gm.getSeriesId(), gm.getHomeTeamId(), homeLockStatus);
        updateLockStatus(gm.getSeriesId(), gm.getAwayTeamId(), awayLockStatus);

        int homeSurvivorStatus = Status.SURVIVOR_STATUS_NO_DECISION;
        int awaySurvivorStatus = Status.SURVIVOR_STATUS_NO_DECISION;
        if (gm.getHomeScore() == gm.getAwayScore()) {
            homeSurvivorStatus = Status.SURVIVOR_STATUS_LOST;
            awaySurvivorStatus = Status.SURVIVOR_STATUS_LOST;
        } else if (gm.getHomeScore() > gm.getAwayScore()) {
            homeSurvivorStatus = Status.SURVIVOR_STATUS_WON;
            awaySurvivorStatus = Status.SURVIVOR_STATUS_LOST;
        } else if (gm.getAwayScore() > gm.getHomeScore()) {
            homeSurvivorStatus = Status.SURVIVOR_STATUS_LOST;
            awaySurvivorStatus = Status.SURVIVOR_STATUS_WON;
        }
        updateSurvivorStatus(gm.getSeriesId(), gm.getHomeTeamId(), homeSurvivorStatus);
        updateSurvivorStatus(gm.getSeriesId(), gm.getAwayTeamId(), awaySurvivorStatus);
    }

    // -----------------------
    // LEAGUE SERIES
    // -----------------------
    public void setLeagueSeriesWinners(int leagueId, int seriesId) throws ProcessException {
        String sql = "SELECT u.user_login_id_vc, COUNT(*)" +
                     " FROM user_tbl u, user_game_xref_tbl ugx, game_tbl g" +
                     " WHERE u.user_no_in = ugx.user_no_in" +
                     " AND g.game_no_in = ugx.game_no_in" +
                     " AND g.series_no_in = ?" +
                     " AND u.league_no_in = ?" +
                     " AND ugx.ugame_status_si = ?" +
                     " GROUP BY u.user_login_id_vc" +
                     " ORDER BY 2 DESC, 1 ASC";
        String winners = "";
        int highScore = -1;
        try {
            Object[] args = new Object[]{new Integer(seriesId),
                            new Integer(leagueId),
                            new Integer(Status.GAME_STATUS_WON)};
            executeQuery(sql, args);
            if(rs != null) {
                while(rs.next()) {
                    if(highScore == -1) {
                        highScore = rs.getInt(2);
                    }
                    if(rs.getInt(2) < highScore) {
                        // take the high score winners only, then exit
                        break;
                    }
                    if(winners.length() > 0) winners += ", ";
                    winners += rs.getString(1);
                }
            }
            // update the current db record, if exists; otherwise create anew
            LeagueSeriesXrefModel lsx = getLeagueSeries(seriesId, leagueId);
            if(lsx == null) {
                lsx = new LeagueSeriesXrefModel(leagueId, seriesId, winners, highScore);
                createLeagueSeries(lsx);
            } else {
                lsx.setLeagueWinners(winners);
                lsx.setLeagueHighScore(highScore);
                updateLeagueSeries(lsx);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public LeagueSeriesXrefModel getLeagueSeries(int seriesId, int leagueId) throws ProcessException {
        String sql = "SELECT * from league_series_xref_tbl" +
                     " WHERE league_no_in = ?" +
                     " AND series_no_in = ?";
        LeagueSeriesXrefModel lsx = null;
        try {
            Object[] args = new Object[]{new Integer(leagueId),
                            new Integer(seriesId)};
            executeQuery(sql, args);
            if(rs != null && rs.next()) {
                this.loadLeagueSeriesXrefModel(rs);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return lsx;
    }

    public Collection getLeagueSeriesBySeason(int leagueId, int seasonId) throws ProcessException {
        String sql = "SELECT lsx.* FROM league_series_xref_tbl lsx" +
                     ", season_series_tbl ss" +
                     " WHERE lsx.series_no_in = ss.series_no_in" +
                     " AND lsx.league_no_in = ?" +
                     " AND ss.season_no_in = ?" +
                     " ORDER BY lsx.series_no_in";
        Collection models = null;
        try {
            Object[] args = new Object[]{new Integer(leagueId),
                            new Integer(seasonId)};
            executeQuery(sql, args);
            if(rs != null) {
                models = new ArrayList();
                while(rs.next()) {
                    models.add(this.loadLeagueSeriesXrefModel(rs));
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return models;
    }

    public void createLeagueSeries(LeagueSeriesXrefModel lsx) throws ProcessException {
        String sql = "INSERT INTO league_series_xref_tbl(league_winners_vc" +
                     ", league_high_score_si" +
                     ", series_no_in" +
                     ", league_no_in" +
                     ") VALUES(?, ?, ?, ?)";

        Object[] args = new Object[]{lsx.getLeagueWinners(),
                        new Integer(lsx.getLeagueHighScore()),
                        new Integer(lsx.getSeriesId()),
                        new Integer(lsx.getLeagueId())};
        executeUpdate(sql, args);
        ReportHelper.setReportStale(ReportHelper.RPT_PUBLISHED_WINNERS);
    }

    public void updateLeagueSeries(LeagueSeriesXrefModel lsx) throws ProcessException {
        String sql = "UPDATE league_series_xref_tbl" +
                     " SET league_winners_vc = ?" +
                     " , league_high_score_si = ?" +
                     " WHERE series_no_in = ?" +
                     " AND league_no_in = ?";

        Object[] args = new Object[]{lsx.getLeagueWinners(),
                        new Integer(lsx.getLeagueHighScore()),
                        new Integer(lsx.getSeriesId()),
                        new Integer(lsx.getLeagueId())};
        executeUpdate(sql, args);
        ReportHelper.setReportStale(ReportHelper.RPT_PUBLISHED_WINNERS);
    }

    public static UserGameXrefModel loadUserGameXrefModel(java.sql.ResultSet rs) throws SQLException {
        UserGameXrefModel ugm = new UserGameXrefModel();
        ugm.setUserId(rs.getInt("user_no_in"));
        ugm.setGameId(rs.getInt("game_no_in"));
        ugm.setSelectedTeamId(rs.getInt("team_no_in"));
        ugm.setNotes(rs.getString("ugame_notes_vc"));
        ugm.setStatus(rs.getInt("ugame_status_si"));
        ugm.setDefaultPick((rs.getInt("ugame_default_si") == 1));
        ugm.setTimestamp(new java.util.Date(rs.getTimestamp("ugame_timestamp_dt").getTime()));
        return ugm;
    }

    public static UserSeriesXrefModel loadUserSeriesXrefModel(java.sql.ResultSet rs) throws SQLException {
        UserSeriesXrefModel usm = new UserSeriesXrefModel();
        usm.setUserId(rs.getInt("user_no_in"));
        usm.setSeriesId(rs.getInt("series_no_in"));
        usm.setSurvivor(rs.getInt("survivor_team_in"));
        usm.setLock(rs.getInt("lock_team_in"));
        usm.setNotes(rs.getString("useries_notes_vc"));
        usm.setLockStatus(rs.getInt("lock_status_si"));
        usm.setSurvivorStatus(rs.getInt("survivor_status_si"));
        usm.setTimestamp(new java.util.Date(rs.getTimestamp("useries_timestamp_dt").getTime()));
        return usm;
    }

    public static LeagueSeriesXrefModel loadLeagueSeriesXrefModel(java.sql.ResultSet rs) throws SQLException {
        LeagueSeriesXrefModel lsxm = new LeagueSeriesXrefModel();
        lsxm.setLeagueId(rs.getInt("league_no_in"));
        lsxm.setSeriesId(rs.getInt("series_no_in"));
        lsxm.setLeagueWinners(rs.getString("league_winners_vc"));
        lsxm.setLeagueHighScore(rs.getInt("league_high_score_si"));
        return lsxm;
    }

    public static AuditPickModel loadAuditPickModel(java.sql.ResultSet rs) throws SQLException {
        AuditPickModel apm = new AuditPickModel();
        apm.setTimestamp(rs.getTimestamp("pick_timestamp_dt"));
        apm.setType(rs.getString("pick_type_vc"));
        apm.setUserId(rs.getInt("user_no_in"));
        apm.setGameId(rs.getInt("game_no_in"));
        apm.setSeriesId(rs.getInt("series_no_in"));
        apm.setTeamId(rs.getInt("teame_no_in"));
        return apm;
    }
}
