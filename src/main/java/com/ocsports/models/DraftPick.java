/*
 * Title         DraftPick.java
 * Created       March, 2010
 * Author        Paul Charlton
 */
package com.ocsports.models;

public class DraftPick implements BaseModel {
    public int        pickKey;
    public int        pickRound;
    public int        teamKey;
    public int        playerKey;
    public String     pickTeam;

    //after a selection is made
    public DraftPlayer  selectedPlayer;

    /** Creates a new instance of DraftPick */
    public DraftPick() {
    }

    public DraftPick(java.sql.ResultSet rs) throws java.sql.SQLException {
        this.pickKey = rs.getInt("pick_key");
        this.pickRound = rs.getInt("pick_round");
        this.teamKey = rs.getInt("team_key");
        this.pickTeam = rs.getString("draft_team");
        this.playerKey = rs.getInt("draft_player");
        if( this.playerKey > 0 ) {
            this.selectedPlayer = new DraftPlayer(rs);
        }
    }
}
