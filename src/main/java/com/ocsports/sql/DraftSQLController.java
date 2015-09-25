/*
 * Title         DraftSQLController.java
 * Created       March, 2010
 * Author        Paul Charlton
 */
package com.ocsports.sql;

import com.ocsports.models.DraftChat;
import com.ocsports.models.DraftPick;
import com.ocsports.models.DraftPlayer;
import com.ocsports.models.DraftTeamPos;
import java.util.ArrayList;
import java.util.Collection;

public class DraftSQLController extends SQLBase {

    public int getCurrentPick() {
        int pickKey = 0;
        String query = "SELECT MIN(pick_key) \"PICK_KEY\" FROM draft_order_tbl WHERE IFNULL(player_key, 0) = 0";
        try {
            this.executeQuery(query, null);
            if (rs != null && rs.next()) {
                pickKey = rs.getInt("pick_key");
            }
        } catch (Exception e) {
            //
        }
        return pickKey;
    }

    public int getLastChat() {
        int chatKey = 0;
        String query = "SELECT MAX(chat_key) \"CHAT_KEY\" FROM draft_chat_tbl";
        try {
            this.executeQuery(query, null);
            if (rs != null && rs.next()) {
                chatKey = rs.getInt("chat_key");
            }
        } catch (Exception e) {
            //
        }
        return chatKey;
    }

    public void addChat(String msg, String teamName) {
        try {
            int chatKey = SQLBase.getNextKey(this, "draft_chat_tbl", "chat_key");

            String query = "INSERT INTO draft_chat_tbl(chat_key, chat_dt, chat_msg, team_name) VALUES(?,?,?,?)";
            Object[] args = new Object[]{new Integer(chatKey), new java.util.Date(), msg, teamName};
            this.executeUpdate(query, args);
        } catch (Exception e) {
            //
        }
    }

    public Collection getChat() {
        Collection chatMsgs = null;
        String query = "SELECT chat_key, chat_msg, team_name FROM draft_chat_tbl ORDER BY chat_dt DESC";
        try {
            this.executeQuery(query.toString(), null);
            if (rs != null) {
                chatMsgs = new ArrayList();
                while (rs.next()) {
                    chatMsgs.add(new DraftChat(rs));
                }
            }
        } catch (Exception e) {
            //
        }
        return chatMsgs;
    }

    public Collection getDraftPicks(int teamKey, int pickRound) {
        Collection draftPicks = null;

        StringBuffer query = new StringBuffer();
        query.append("SELECT IFNULL(plyr.player_key, 0) \"PLAYER_KEY\" ");
        query.append(" , plyr.rank");
        query.append(" , plyr.fname");
        query.append(" , plyr.lname");
        query.append(" , plyr.pos");
        query.append(" , plyr.team_name");
        query.append(" , plyr.team_abrv");
        query.append(" , plyr.statcats");
        query.append(" , plyr.stats");
        query.append(" , plyr.img");
        query.append(" , plyr.jersey");
        query.append(" , ord.pick_key");
        query.append(" , ord.pick_round");
        query.append(" , ord.team_key");
        query.append(" , IFNULL(ord.player_key, 0) \"DRAFT_PLAYER\" ");
        query.append(" , team.team_name \"DRAFT_TEAM\" ");
        query.append(" , ord.team_key \"PICK_TEAM\" ");
        query.append(" , team.team_name \"PICK_TEAM_NAME\" ");
        query.append(" FROM ((draft_order_tbl ord");
        query.append(" INNER JOIN draft_team_tbl team ON ord.team_key = team.team_key)");
        query.append(" LEFT JOIN draft_player_tbl plyr ON ord.player_key = plyr.player_key)");
        query.append(" WHERE ord.pick_key > 0");
        if (teamKey > 0) {
            query.append(" AND ord.team_key = " + teamKey);
        }
        if (pickRound > 0) {
            query.append(" AND ord.pick_round = " + pickRound);
        }
        query.append(" ORDER BY 12");

        try {
            this.executeQuery(query.toString(), null);
            if (rs != null) {
                draftPicks = new ArrayList();
                while (rs.next()) {
                    draftPicks.add(new DraftPick(rs));
                }
            }
        } catch (Exception e) {
            //
        }
        return draftPicks;
    }

    public Collection getPlayers() {
        Collection players = null;

        StringBuffer query = new StringBuffer();
        query.append("SELECT plyr.player_key");
        query.append(" , plyr.rank");
        query.append(" , plyr.fname");
        query.append(" , plyr.lname");
        query.append(" , plyr.pos");
        query.append(" , plyr.team_name");
        query.append(" , plyr.team_abrv");
        query.append(" , plyr.statcats");
        query.append(" , plyr.stats");
        query.append(" , plyr.img");
        query.append(" , plyr.jersey");
        query.append(" FROM draft_player_tbl plyr");
        query.append(" ORDER BY 2");

        try {
            this.executeQuery(query.toString(), null);
            if (rs != null) {
                players = new ArrayList();
                while (rs.next()) {
                    players.add(new DraftPlayer(rs));
                }
            }
        } catch (Exception e) {
            //
        }
        return players;
    }

    public Collection getTeams() {
        StringBuffer query = new StringBuffer();
        query.append(" SELECT DISTINCT team_key");
        query.append(" , team_name");
        query.append(" FROM draft_team_tbl");
        query.append(" ORDER BY 2");

        ArrayList teams = new ArrayList();
        try {
            this.executeQuery(query.toString(), null);
            if (rs != null) {
                while (rs.next()) {
                    teams.add(rs.getString("team_name"));
                }
            }
        } catch (Exception e) {
            //
        }
        return teams;
    }

    public Collection getPlayersByTeam(String draftTeam) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT plyr.player_key");
        query.append(" , plyr.rank");
        query.append(" , plyr.fname");
        query.append(" , plyr.lname");
        query.append(" , plyr.pos");
        query.append(" , plyr.team_name");
        query.append(" , plyr.team_abrv");
        query.append(" , plyr.statcats");
        query.append(" , plyr.stats");
        query.append(" , plyr.img");
        query.append(" , plyr.jersey");
        query.append(" , ord.pick_key");
        query.append(" , ord.pick_round");
        query.append(" , ord.team_key \"PICK_TEAM\" ");
        query.append(" , team.team_name \"PICK_TEAM_NAME\" ");
        query.append(" FROM draft_player_tbl plyr");
        query.append(" INNER JOIN draft_order_tbl ord ON plyr.player_key = ord.player_key");
        query.append(" INNER JOIN draft_team_tbl team ON ord.team_key = team.team_key");
        query.append(" WHERE team.team_name = ?");
        query.append(" ORDER BY 12");

        ArrayList players = new ArrayList();
        try {
            Object[] args = new Object[]{draftTeam};
            this.executeQuery(query.toString(), args);
            if (rs != null) {
                while (rs.next()) {
                    players.add(new DraftPlayer(rs));
                }
            }
        } catch (Exception e) {
            //
        }

        Collection teamPlayers = new ArrayList();

        String[] pos = new String[]{"C", "1B", "2B", "3B", "SS", "OF", "OF", "OF", "UTIL", "SP", "SP", "SP", "RP", "RP", "P", "P", "BN", "BN", "BN", "BN"};
        for (int i = 0; i < pos.length; i++) {
            String currPos = pos[i];
            DraftPlayer dp = null;
            int k;

            for (k = 0; k < players.size(); k++) {
                dp = (DraftPlayer) players.get(k);
                if (dp.pos.indexOf(currPos) > -1) {
                    break;
                } else if (currPos == "UTIL" && dp.pos.indexOf("SP") == -1 && dp.pos.indexOf("RP") == -1) {
                    break;
                } else if (currPos == "P" && (dp.pos.indexOf("SP") > -1 || dp.pos.indexOf("RP") > -1)) {
                    break;
                } else if (currPos == "BN") {
                    break;
                }

                dp = null;
            }
            if (dp != null) {
                players.remove(k);
                teamPlayers.add(new DraftTeamPos(currPos, (dp.fname + " " + dp.lname), dp.teamAbrv));
            } else {
                teamPlayers.add(new DraftTeamPos(currPos, null, null));
            }
        }
        return teamPlayers;
    }

    public Collection getPlayers(int sortBy) {
        return getPlayers(null, null, null, null, sortBy);
    }

    public Collection getPlayers(String pos, String teamAbrv, String hideSelected, int sortBy) {
        return getPlayers(pos, teamAbrv, hideSelected, null, sortBy);
    }

    public Collection getPlayers(String pos, String teamAbrv, String hideSelected, String playerName, int sortBy) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT plyr.player_key");
        query.append(" , plyr.rank");
        query.append(" , plyr.fname");
        query.append(" , plyr.lname");
        query.append(" , plyr.pos");
        query.append(" , plyr.team_name");
        query.append(" , plyr.team_abrv");
        query.append(" , plyr.statcats");
        query.append(" , plyr.stats");
        query.append(" , plyr.img");
        query.append(" , plyr.jersey");
        query.append(" , IFNULL(ord.pick_key, 0) \"PICK_KEY\" ");
        query.append(" , IFNULL(ord.pick_round, 0) \"PICK_ROUND\" ");
        query.append(" , IFNULL(ord.team_key, 0) \"PICK_TEAM\" ");
        query.append(" , team.team_name \"PICK_TEAM_NAME\" ");
        query.append(" FROM ((draft_player_tbl plyr");
        query.append(" LEFT JOIN draft_order_tbl ord ON plyr.player_key = ord.player_key)");
        query.append(" LEFT JOIN draft_team_tbl team ON ord.team_key = team.team_key)");
        query.append(" WHERE plyr.rank > 0");

        if (pos != null && pos.length() > 0 && !pos.equals("ALL")) {
            query.append(" AND INSTR(plyr.pos, '" + pos + "') > 0");
        }

        if (teamAbrv != null && teamAbrv.length() > 0 && !teamAbrv.equals("ALL")) {
            query.append(" AND UPPER(plyr.team_abrv) = '" + teamAbrv.toUpperCase() + "'");
        }

        if (playerName != null && playerName.length() > 0) {
            String pUpper = playerName.toUpperCase();
            query.append(" AND ( INSTR(UPPER(plyr.fname), '" + pUpper + "') > 0 OR INSTR(UPPER(plyr.lname), '" + pUpper + "') > 0)");
        }

        if (hideSelected != null && hideSelected.length() > 0) {
            query.append(" AND IFNULL(ord.pick_key, 0) = 0");
        }

        if (sortBy == 1) {
            query.append(" ORDER BY 2, 3, 4");
        } else {
            query.append(" ORDER BY 4, 3, 2");
        }

        ArrayList players = new ArrayList();
        try {
            this.executeQuery(query.toString(), null);
            if (rs != null) {
                while (rs.next()) {
                    players.add(new DraftPlayer(rs));
                }
            }
        } catch (Exception e) {
            //
        }
        return players;
    }

    /*
     public Collection getPicks() throws ProcessException {
     String query = "SELECT do.pick_id, do.pick_round, do.pick_team, IFNULL(do.player_key, 0) \"player_key\" " +
     " , IFNULL(dp.player_rank, 0) \"player_rank\", dp.player_fname, dp.player_lname, dp.player_pos" +
     " , dp.team_name, dp.team_abrv, dp.player_statcats, dp.player_stats, dp.player_img, dp.player_num" +
     " FROM draft_order_tbl do" +
     " LEFT JOIN draft_player_tbl dp ON do.player_key = dp.player_key" +
     " ORDER BY 1";

     ArrayList picks = new ArrayList();
     try {
     this.executeQuery( query, null );
     if( rs != null ) {
     while( rs.next() ) {
     picks.add( new DraftPick(rs) );
     }
     }
     }
     catch(java.sql.SQLException sqle) {
     throw new ProcessException(sqle);
     }
     return picks;
     }
     */
    public void draftPlayer(int pickId, int playerId) {
        String query = "UPDATE draft_order_tbl "
                + " SET player_key = ?"
                + " WHERE pick_key = ?";
        Object[] args = new Object[]{new Integer(playerId), new Integer(pickId)};
        try {
            this.executeUpdate(query, args);
        } catch (Exception e) {
            //
        }
    }
}
