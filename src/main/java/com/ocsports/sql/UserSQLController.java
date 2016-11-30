package com.ocsports.sql;

import com.ocsports.core.ProcessException;
import com.ocsports.core.SportTypes;
import com.ocsports.core.Status;
import com.ocsports.models.AuditLoginModel;
import com.ocsports.models.LeagueModel;
import com.ocsports.models.SeasonModel;
import com.ocsports.models.SystemNoticeModel;
import com.ocsports.models.UserModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UserSQLController extends SQLBase {

    public static final int USERS_ORDER_BY_NAME = 1;
    public static final int USERS_ORDER_BY_LOGIN = 2;
    public static final int USERS_ORDER_BY_EMAIL = 3;
    public static final int USERS_ORDER_BY_PAID = 4;
    public static final int USERS_ORDER_BY_STATUS = 5;

    public int login(String loginId, String loginPwd) throws ProcessException {
        String query = "SELECT u.user_no_in FROM user_tbl u"
                + ", league_tbl l"
                + ", season_tbl s"
                + " WHERE l.league_no_in = u.league_no_in"
                + " AND s.season_no_in = l.season_no_in"
                + " AND s.season_active_si = 1"
                + " AND LOWER(u.user_login_id_vc) = ?"
                + " AND LOWER(u.user_login_pwd_vc) = ?";
        int userId = -1;
        try {
            executeQuery(query, new Object[]{loginId.toLowerCase(), loginPwd.toLowerCase()});
            if (rs != null && rs.next()) {
                userId = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return userId;
    }

    public UserModel getUserModel(int userId) throws ProcessException {
        String query = "SELECT * FROM user_tbl WHERE user_no_in = ?";
        UserModel u = null;
        try {
            executeQuery(query, new Object[]{new Integer(userId)});
            if (rs != null && rs.next()) {
                u = loadUserModel(rs);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return u;
    }

    public int getActiveLeagueId() throws ProcessException {
        int leagueId = -1;
        int seasonId = -1;
        SeasonSQLController seasonCtrlr = null;
        try {
            seasonCtrlr = new SeasonSQLController();
            Collection seasons = seasonCtrlr.getSeasons(SportTypes.TYPE_NFL_FOOTBALL, false);
            Iterator iter = seasons.iterator();
            while (iter.hasNext()) {
                SeasonModel sm = (SeasonModel) iter.next();
                if (sm.isActive()) {
                    seasonId = sm.getId();
                    break;
                }
            }

            Collection leagues = getLeaguesBySeason(seasonId);
            iter = leagues.iterator();
            while (iter.hasNext()) {
                leagueId = ((LeagueModel) iter.next()).getId();
                break;
            }
        } catch (Exception e) {
            throw new ProcessException(e);
        } finally {
            if (seasonCtrlr != null) {
                seasonCtrlr.closeConnection();
            }
        }
        return leagueId;
    }

    public Collection findUsersByEmail(String email) throws ProcessException {
        String query = "SELECT u.* FROM user_tbl u"
                + ", league_tbl l"
                + ", season_tbl s"
                + " WHERE l.league_no_in = u.league_no_in"
                + " AND s.season_no_in = l.season_no_in"
                + " AND s.season_active_si = 1"
                + " AND (LOWER(user_email_vc) = ? OR LOWER(user_email2_vc) = ?)"
                + " ORDER BY user_login_id_vc";

        ArrayList userModels = null;
        try {
            executeQuery(query, new Object[]{email.toLowerCase(), email.toLowerCase()});
            if (rs != null) {
                userModels = new ArrayList();
                while (rs.next()) {
                    userModels.add(loadUserModel(rs));
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return userModels;
    }

    public void updateProfile(UserModel uModel) throws ProcessException {
        String query = "UPDATE user_tbl SET user_fname_vc = ?"
                + ", user_mname_vc = ?"
                + ", user_lname_vc = ?"
                + ", user_login_id_vc = ?"
                + ", user_login_pwd_vc = ?"
                + ", user_address_vc = ?"
                + ", user_email_vc = ?"
                + ", user_email2_vc = ?"
                + ", user_default_pick_si = ?"
                + ", user_email_picks_si = ?"
                + ", user_send_warning_si = ?"
                + ", user_pref_team_in = ?"
                + ", user_pref_team_vc = ?"
                + ", user_theme_vc = ?"
                + ", user_login_disabled_si = ?"
                + ", user_paid_si = ?"
                + " WHERE user_no_in = ?";

        Object[] args = new Object[]{uModel.getFirstName(),
            uModel.getMiddleName(),
            uModel.getLastName(),
            uModel.getLoginId(),
            uModel.getLoginPwd(),
            uModel.getAddress(),
            uModel.getEmail(),
            uModel.getEmail2(),
            new Integer(uModel.getDefaultPick()),
            Boolean.valueOf(uModel.emailPicks()),
            Boolean.valueOf(uModel.sendWarning()),
            new Integer(uModel.getFavoriteTeam()),
            uModel.getFavoriteTeamName(),
            uModel.getColorTheme(),
            new Integer((uModel.isLoginDisabled() ? 1 : 0)),
            new Integer((uModel.isPaid() ? 1 : 0)),
            new Integer(uModel.getUserId())};
        executeUpdate(query, args);
    }

    public int createProfile(UserModel uModel) throws ProcessException {
        int newUserId = SQLBase.getNextKey(this, "user_tbl", "user_no_in");
        if (newUserId <= 0) {
            throw new ProcessException("Unable to retrieve next key for user_tbl - user_no_in");
        }

        String query = "INSERT INTO user_tbl(user_no_in"
                + ", user_fname_vc"
                + ", user_mname_vc"
                + ", user_lname_vc"
                + ", user_login_id_vc"
                + ", user_login_pwd_vc"
                + ", user_address_vc"
                + ", user_email_vc"
                + ", user_email2_vc"
                + ", user_default_pick_si"
                + ", user_email_picks_si"
                + ", user_send_warning_si"
                + ", user_paid_si"
                + ", user_pref_team_in"
                + ", user_pref_team_vc"
                + ", user_theme_vc"
                + ", user_login_disabled_si)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] args = new Object[]{new Integer(newUserId),
            uModel.getFirstName(),
            uModel.getMiddleName(),
            uModel.getLastName(),
            uModel.getLoginId(),
            uModel.getLoginPwd(),
            uModel.getAddress(),
            uModel.getEmail(),
            uModel.getEmail2(),
            new Integer(uModel.getDefaultPick()),
            Boolean.valueOf(uModel.emailPicks()),
            Boolean.valueOf(uModel.sendWarning()),
            Boolean.valueOf(false),
            new Integer(uModel.getFavoriteTeam()),
            uModel.getFavoriteTeamName(),
            uModel.getColorTheme(),
            Boolean.valueOf(uModel.isLoginDisabled())};
        executeUpdate(query, args);
        return newUserId;
    }

    public boolean loginExists(int userId, String loginId) throws ProcessException {
        String query = "SELECT u.user_no_in FROM user_tbl u"
                + ", league_tbl l"
                + ", season_tbl s"
                + " WHERE u.league_no_in = l.league_no_in"
                + " AND l.season_no_in = s.season_no_in"
                + " AND s.season_active_si = 1"
                + " AND LOWER(u.user_login_id_vc) = ? and u.user_no_in != ?";
        try {
            executeQuery(query, new Object[]{loginId.toLowerCase(), new Integer(userId)});
            return (rs != null && rs.next());
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public Collection getLeaguesBySeason(int seasonId) throws ProcessException {
        String query = "SELECT * FROM league_tbl WHERE season_no_in = ? ORDER BY league_name_vc";
        ArrayList leagues = new ArrayList();
        try {
            executeQuery(query, new Object[]{new Integer(seasonId)});
            if (rs != null) {
                while (rs.next()) {
                    leagues.add(loadLeagueModel(rs));
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return leagues;
    }

    public Collection getLeagues() throws ProcessException {
        String query = "SELECT * FROM league_tbl ORDER BY league_name_vc";
        ArrayList leagues = new ArrayList();
        try {
            executeQuery(query, null);
            if (rs != null) {
                while (rs.next()) {
                    leagues.add(loadLeagueModel(rs));
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return leagues;
    }

    public void setLeague(int userId, int leagueId) throws ProcessException {
        String query = "UPDATE user_tbl set league_no_in = ? WHERE user_no_in = ?";
        executeUpdate(query, new Object[]{new Integer(leagueId), new Integer(userId)});
    }

    public LeagueModel getLeagueModel(int leagueId) throws ProcessException {
        String query = "SELECT * FROM league_tbl WHERE league_no_in = ?";
        LeagueModel lm = null;
        try {
            executeQuery(query, new Object[]{new Integer(leagueId)});
            if (rs != null && rs.next()) {
                lm = loadLeagueModel(rs);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return lm;
    }

    public Collection getUsersByLeague(int leagueId, int orderBy) throws ProcessException {
        String query = "SELECT * FROM user_tbl WHERE league_no_in = ?";
        switch (orderBy) {
            case UserSQLController.USERS_ORDER_BY_NAME:
                query += " ORDER BY user_fname_vc, user_lname_vc";
                break;
            case UserSQLController.USERS_ORDER_BY_LOGIN:
                query += " ORDER BY user_login_id_vc";
                break;
            case UserSQLController.USERS_ORDER_BY_EMAIL:
                query += " ORDER BY user_email_vc";
                break;
            case UserSQLController.USERS_ORDER_BY_PAID:
                query += " ORDER BY user_paid_si, user_fname_vc, user_lname_vc";
                break;
            case UserSQLController.USERS_ORDER_BY_STATUS:
                query += " ORDER BY user_login_disabled_si DESC, user_fname_vc, user_lname_vc";
                break;
            default:
                query += " ORDER BY user_no_in";
                break;
        }

        ArrayList users = new ArrayList();
        try {
            executeQuery(query, new Object[]{new Integer(leagueId)});
            if (rs != null) {
                while (rs.next()) {
                    users.add(loadUserModel(rs));
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return users;
    }

    public void deleteUser(int userId) throws ProcessException {
        Object[] args = new Object[]{new Integer(userId)};
        executeUpdate("DELETE FROM user_game_xref_tbl WHERE user_no_in = ?", args);
        executeUpdate("DELETE FROM user_series_xref_tbl WHERE user_no_in = ?", args);
        executeUpdate("DELETE FROM forum_message_tbl WHERE msg_created_by_in = ?", args);
        executeUpdate("DELETE FROM audit_login_tbl WHERE user_no_in = ?", args);
        executeUpdate("DELETE FROM audit_picks_tbl WHERE user_no_in = ?", args);
        executeUpdate("DELETE FROM user_tbl WHERE user_no_in = ?", args);
    }

    public int getSurvivorCount(int leagueId) throws ProcessException {
        int userCount = -1;
        try {
            String userIds = "";
            String query = "SELECT DISTINCT u.user_no_in "
                    + " FROM user_tbl u, user_series_xref_tbl usx "
                    + " WHERE u.user_no_in = usx.user_no_in "
                    + " AND u.league_no_in = ? "
                    + " AND usx.survivor_status_si = ?";
            executeQuery(query, new Object[]{new Integer(leagueId), new Integer(Status.SURVIVOR_STATUS_LOST)});
            if (rs != null) {
                while (rs.next()) {
                    if (userIds.length() > 0) {
                        userIds += ",";
                    }
                    userIds += String.valueOf(rs.getInt(1));
                }
            }

            query = "SELECT COUNT(*) FROM user_tbl WHERE league_no_in = ?";
            if (userIds.length() > 0) {
                query += " AND user_no_in NOT IN (" + userIds + ")";
            }

            executeQuery(query, new Object[]{new Integer(leagueId)});
            if (rs != null && rs.next()) {
                userCount = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return userCount;
    }

    public Collection getSystemNotices(int lastXMessages, boolean publishedOnly) throws ProcessException {
        ArrayList notices = new ArrayList();
        try {
            String query = "SELECT * FROM system_notice_tbl"
                    + " WHERE notice_updated_dt IS NOT NULL"
                    + (publishedOnly ? " AND notice_publish_si = 1" : "")
                    + " ORDER BY notice_publish_si DESC"
                    + " , notice_updated_dt DESC";

            executeQuery(query, null);

            int count = 0;
            if (rs != null) {
                while (rs.next() && (lastXMessages <= 0 || count < lastXMessages)) {
                    notices.add(loadSystemNoticeModel(rs));
                    count++;
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return notices;
    }

    public void createSystemNotice(String msg, boolean publish) throws ProcessException {
        int noticeId = SQLBase.getNextKey(this, "system_notice_tbl", "notice_no_in");
        String query = "INSERT INTO system_notice_tbl( notice_no_in"
                + " , notice_created_dt"
                + " , notice_updated_dt"
                + " , notice_text_vc"
                + " , notice_publish_si"
                + " ) VALUES(?,?,?,?,?)";

        Object[] args = new Object[]{new Integer(noticeId),
            new java.util.Date(),
            new java.util.Date(),
            msg,
            new Integer(publish ? 1 : 0)
        };
        executeUpdate(query, args);
    }

    public void updateSystemNotice(int noticeId, String msgText, boolean publish) throws ProcessException {
        String query = "UPDATE system_notice_tbl SET notice_text_vc = ?"
                + " , notice_publish_si = ?"
                + " , notice_updated_dt = ?"
                + " WHERE notice_no_in = ?";
        executeUpdate(query, new Object[]{msgText, Boolean.valueOf(publish), new java.util.Date(), new Integer(noticeId)});
    }

    public void deleteSystemNotice(int noticeId) throws ProcessException {
        String query = "DELETE FROM system_notice_tbl WHERE notice_no_in = ?";
        executeUpdate(query, new Object[]{new Integer(noticeId)});
    }

    public Collection getAuditLoginModels(int userId, java.util.Date sinceDt, int lastXLogins) throws ProcessException {
        Object[] args = null;
        String query = "SELECT * FROM audit_login_tbl";
        if (userId > 0 && sinceDt != null) {
            query += " WHERE user_no_in = ? AND login_timestamp_dt >= ?";
            args = new Object[]{new Integer(userId), sinceDt};
        } else if (userId > 0) {
            query += " WHERE user_no_in = ?";
            args = new Object[]{new Integer(userId)};
        } else if (sinceDt != null) {
            query += " WHERE login_timestamp_dt >= ?";
            args = new Object[]{sinceDt};
        }
        query += " ORDER BY login_timestamp_dt DESC";

        ArrayList logins = new ArrayList();
        try {
            int count = 0;
            executeQuery(query, args);
            if (rs != null) {
                while (rs.next() && (lastXLogins <= 0 || count < lastXLogins || sinceDt != null)) {
                    logins.add(loadAuditLoginModel(rs));
                    count++;
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return logins;
    }

    public void createAuditLogin(String loginId, String pwd, int userId) throws ProcessException {
        String query = "INSERT INTO audit_login_tbl( login_timestamp_dt"
                + ", login_id_vc"
                + ", login_pwd_vc"
                + ", user_no_in"
                + ") VALUES(?,?,?,?)";
        Object[] args = new Object[]{new java.util.Date(), loginId, pwd, new Integer(userId)};
        executeUpdate(query, args);
    }

    public void setUserPaid(int userId, boolean isPaid) throws ProcessException {
        String query = "UPDATE user_tbl set user_paid_si = ? where user_no_in = ?";
        Object[] args = new Object[]{Boolean.valueOf(isPaid), new Integer(userId)};
        executeUpdate(query, args);
    }

    public static LeagueModel loadLeagueModel(ResultSet rs) throws SQLException {
        LeagueModel lm = new LeagueModel();
        lm.setId(rs.getInt("league_no_in"));
        lm.setName(rs.getString("league_name_vc"));
        lm.setSeasonId(rs.getInt("season_no_in"));
        lm.setAdministrator(rs.getInt("league_admin_in"));
        lm.setPublic(rs.getInt("league_no_in") == 1);
        lm.setPassword(rs.getString("league_pwd_vc"));
        return lm;
    }

    public static UserModel loadUserModel(ResultSet rs) throws SQLException {
        UserModel um = new UserModel();
        um.setUserId(rs.getInt("user_no_in"));
        um.setFirstName(rs.getString("user_fname_vc"));
        um.setMiddleName(rs.getString("user_mname_vc"));
        um.setLastName(rs.getString("user_lname_vc"));
        um.setAddress(rs.getString("user_address_vc"));
        um.setEmail(rs.getString("user_email_vc"));
        um.setEmail2(rs.getString("user_email2_vc"));
        um.setLoginId(rs.getString("user_login_id_vc"));
        um.setLoginPwd(rs.getString("user_login_pwd_vc"));
        um.setLeagueId(rs.getInt("league_no_in"));
        um.setDefaultPick(rs.getInt("user_default_pick_si"));
        um.setEmailPicks(rs.getInt("user_email_picks_si") == 1);
        um.setSendWarning(rs.getInt("user_send_warning_si") == 1);
        um.setPaid(rs.getInt("user_paid_si") == 1);
        um.setFavoriteTeam(rs.getInt("user_pref_team_in"), rs.getString("user_pref_team_vc"));
        um.setColorTheme(rs.getString("user_theme_vc"));
        um.setLoginDisabled(rs.getInt("user_login_disabled_si") == 1);
        return um;
    }

    public static SystemNoticeModel loadSystemNoticeModel(ResultSet rs) throws SQLException {
        SystemNoticeModel sm = new SystemNoticeModel();
        sm.setId(rs.getInt("notice_no_in"));
        sm.setCreatedDate(rs.getTimestamp("notice_created_dt").getTime());
        sm.setMessage(rs.getString("notice_text_vc"));
        sm.setPublished(rs.getInt("notice_publish_si") == 1);
        sm.setUpdated(rs.getTimestamp("notice_updated_dt").getTime());
        return sm;
    }

    public static AuditLoginModel loadAuditLoginModel(ResultSet rs) throws SQLException {
        AuditLoginModel alm = new AuditLoginModel();
        alm.setTimestamp(rs.getTimestamp("login_timestamp_dt"));
        alm.setLoginId(rs.getString("login_id_vc"));
        alm.setLoginPwd(rs.getString("login_pwd_vc"));
        alm.setUserId(rs.getInt("user_no_in"));
        return alm;
    }
}
