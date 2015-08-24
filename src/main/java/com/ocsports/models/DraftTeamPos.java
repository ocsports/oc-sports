/*
 * Title         DraftTeamPos.java
 * Created       March, 2010
 * Author        Paul Charlton
 */
package com.ocsports.models;

public class DraftTeamPos implements BaseModel {
    public String     pos;
    public String     playerName;
    public String     playerTeam;

    public DraftTeamPos(String pos, String playerName, String playerTeam) {
        this.pos = pos;
        this.playerName = playerName;
        this.playerTeam = playerTeam;
    }
}
