/*
 * Title         DraftPlayer.java
 * Created       March, 2010
 * Author        Paul Charlton
 */
package com.ocsports.models;

public class DraftPlayer implements BaseModel {
    public int      playerKey;
    public int      rank;
    public String   fname;
    public String   lname;
    public String   pos;
    public String   team;
    public String   teamAbrv;
    public String   statCats;
    public String   stats;
    public String   img;
    public String   num;
    
    public int      pickKey;
    public int      pickTeam;
    public int      pickRound;
    public String   pickTeamName;

    /** Creates a new instance of DraftPlayer */
    public DraftPlayer() {
    }

    public DraftPlayer(java.sql.ResultSet rs) throws java.sql.SQLException {
        this.playerKey = rs.getInt("player_key");
        this.rank = rs.getInt("rank");
        this.fname = rs.getString("fname");
        this.lname = rs.getString("lname");
        this.pos = rs.getString("pos");
        this.team = rs.getString("team_name");
        this.teamAbrv = rs.getString("team_abrv");
        this.statCats = rs.getString("statcats");
        this.stats = rs.getString("stats");
        this.img = rs.getString("img");
        this.num = rs.getString("jersey");

        this.pickKey = rs.getInt("pick_key");
        this.pickTeam = rs.getInt("pick_team");
        this.pickRound = rs.getInt("pick_round");
        this.pickTeamName = rs.getString("pick_team_name");
    }
    
    public String playerDetail() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.playerKey + ",");
        buf.append(this.rank + ",");
        buf.append(this.fname + ",");
        buf.append(this.lname + ",");
        buf.append(this.pos + ",");
        buf.append(this.team + ",");
        buf.append(this.teamAbrv + ",");
        buf.append(this.statCats + ",");
        buf.append(this.stats + ",");
        buf.append(this.img + ",");
        buf.append(this.num + ",");
        return buf.toString();
    }
}
