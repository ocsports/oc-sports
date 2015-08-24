/*
 * Title         ForumSQLController.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      6/23/2006 - PC - Removed Tpoics from Forums Page;
 *               now we will just use the leagueID as the topic ID
 *               for all messages created within that league
 *               7/30/2009 - moved to package com.ocsports.sql;
 */
package com.ocsports.sql;

import com.ocsports.core.ProcessException;
import com.ocsports.models.ForumMessageModel;
import java.util.ArrayList;
import java.util.Collection;

public class ForumSQLController extends SQLBase {

    public Collection getMessages(int leagueId) throws ProcessException {
        String query = "SELECT fm.*, u.user_login_id_vc"
                + " FROM forum_message_tbl fm, user_tbl u"
                + " WHERE fm.msg_created_by_in = u.user_no_in"
                + " AND fm.league_no_in = ?"
                + " ORDER BY fm.msg_created_dt DESC";
        ArrayList msgs = new ArrayList();
        try {
            this.executeQuery(query, new Object[]{new Integer(leagueId)});
            if (rs != null) {
                while (rs.next()) {
                    msgs.add(this.loadForumMessageModel(rs));
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return msgs;
    }

    public int createMessage(String msg, int userId, int leagueId) throws ProcessException {
        String query = "INSERT INTO forum_message_tbl(forum_msg_no_in"
                + ", league_no_in"
                + ", msg_text_vc"
                + ", msg_created_dt"
                + ", msg_created_by_in"
                + ") VALUES(?,?,?,?,?)";

        int msgId = SQLBase.getNextKey(this, "forum_message_tbl", "forum_msg_no_in");

        if (msgId <= 0) {
            throw new ProcessException("Unable to retrieve next key for forum_message_tbl - forum_msg_no_in");
        }

        Object[] args = new Object[5];
        args[0] = new Integer(msgId);
        args[1] = new Integer(leagueId);
        args[2] = msg;
        args[3] = new java.util.Date();
        args[4] = new Integer(userId);

        this.executeUpdate(query, args);

        return msgId;
    }

    public Collection getLatestMessages(int lastXMessages, int leagueId) throws ProcessException {
        String query = "SELECT fm.*, u.user_login_id_vc"
                + " FROM forum_message_tbl fm, user_tbl u"
                + " WHERE fm.msg_created_by_in = u.user_no_in"
                + " AND fm.league_no_in = ?"
                + " ORDER BY fm.msg_created_dt DESC";
        ArrayList msgs = new ArrayList();
        try {
            int counter = 0;
            this.executeQuery(query, new Object[]{new Integer(leagueId)});
            if (rs != null) {
                while (rs.next() && (lastXMessages <= 0 || counter++ < lastXMessages)) {
                    msgs.add(this.loadForumMessageModel(rs));
                }
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        }
        return msgs;
    }

    public void updateMessage(int msgId, String msgText) throws ProcessException {
        String query = "UPDATE forum_message_tbl SET msg_text_vc = ?"
                + " WHERE forum_msg_no_in = ?";

        this.executeUpdate(query, new Object[]{msgText, new Integer(msgId)});
    }

    public static ForumMessageModel loadForumMessageModel(java.sql.ResultSet rs) throws java.sql.SQLException {
        ForumMessageModel fm = new ForumMessageModel();
        fm.setId(rs.getInt("forum_msg_no_in"));
        fm.setLeagueId(rs.getInt("league_no_in"));
        fm.setText(rs.getString("msg_text_vc"));
        fm.setCreatedDate(rs.getTimestamp("msg_created_dt").getTime());
        fm.setCreatedBy(rs.getInt("msg_created_by_in"));
        if (rs.getObject("user_login_id_vc") != null) {
            fm.setCreatedByName(rs.getString("user_login_id_vc"));
        }
        return fm;
    }
}
