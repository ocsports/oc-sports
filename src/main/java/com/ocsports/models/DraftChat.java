/*
 * Title         DraftChat.java
 * Created       March, 2012
 * Author        Paul Charlton
 */
package com.ocsports.models;

public class DraftChat implements BaseModel {
    public int        chatKey;
    public String     msg;
    public String     teamName;

    public DraftChat() {
    }

    public DraftChat(java.sql.ResultSet rs) throws java.sql.SQLException {
        this.chatKey = rs.getInt("chat_key");
        this.msg = rs.getString("chat_msg");
        this.teamName = rs.getString("team_name");
    }
}
