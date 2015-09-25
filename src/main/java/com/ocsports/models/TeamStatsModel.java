/*
 * Title         TeamStatsModel.java
 * Created       April 11, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class TeamStatsModel implements BaseModel {
    public static final int OVERALL = 0;
    public static final int HOME = 1;
    public static final int AWAY = 2;
    public static final int GRASS = 3;
    public static final int TURF = 4;
    public static final int DOME = 5;
    public static final int OUTSIDE = 6;
    public static final int SPREAD_OVERALL = 7;
    public static final int SPREAD_HOME = 8;
    public static final int SPREAD_AWAY = 9;
    public static final int SPREAD_GRASS = 10;
    public static final int SPREAD_TURF = 11;
    public static final int SPREAD_DOME = 12;
    public static final int SPREAD_OUTSIDE = 13;

    private int       teamId;
    private String    teamName;
    private int[]     wins = new int[14];
    private int[]     losses = new int[14];
    private int[]     ties = new int[14];

    public TeamStatsModel(int _teamId, String _teamName) {
        this.teamId = _teamId;
        this.teamName = _teamName;
        for(int i=0; i < 14; i++) {
            wins[i] = 0;
            losses[i] = 0;
            ties[i] = 0;
        }
    }
    
    public void addGame(int score, int oppScore, float spread, boolean isHome, boolean isDome, boolean isTurf) {
        if(score > oppScore) {
            this.wins[OVERALL]++;
            int i = (isHome ? this.wins[HOME]++ : this.wins[AWAY]++);
            i = (isDome ? this.wins[DOME]++ : this.wins[OUTSIDE]++);
            i = (isTurf ? this.wins[TURF]++ : this.wins[GRASS]++);
        }
        else if(score < oppScore) {
            this.losses[OVERALL]++;
            int i = (isHome ? this.losses[HOME]++ : this.losses[AWAY]++);
            i = (isDome ? this.losses[DOME]++ : this.losses[OUTSIDE]++);
            i = (isTurf ? this.losses[TURF]++ : this.losses[GRASS]++);
        }
        else {
            this.ties[OVERALL]++;
            int i = (isHome ? this.ties[HOME]++ : this.ties[AWAY]++);
            i = (isDome ? this.ties[DOME]++ : this.ties[OUTSIDE]++);
            i = (isTurf ? this.ties[TURF]++ : this.ties[GRASS]++);
        }

        if((score+spread) > oppScore) {
            this.wins[SPREAD_OVERALL]++;
            int i = (isHome ? this.wins[SPREAD_HOME]++ : this.wins[SPREAD_AWAY]++);
            i = (isDome ? this.wins[SPREAD_DOME]++ : this.wins[SPREAD_OUTSIDE]++);
            i = (isTurf ? this.wins[SPREAD_TURF]++ : this.wins[SPREAD_GRASS]++);
        }
        else if((score+spread) < oppScore) {
            this.losses[SPREAD_OVERALL]++;
            int i = (isHome ? this.losses[SPREAD_HOME]++ : this.losses[SPREAD_AWAY]++);
            i = (isDome ? this.losses[SPREAD_DOME]++ : this.losses[SPREAD_OUTSIDE]++);
            i = (isTurf ? this.losses[SPREAD_TURF]++ : this.losses[SPREAD_GRASS]++);
        }
        else {
            this.ties[SPREAD_OVERALL]++;
            int i = (isHome ? this.ties[SPREAD_HOME]++ : this.ties[SPREAD_AWAY]++);
            i = (isDome ? this.ties[SPREAD_DOME]++ : this.ties[SPREAD_OUTSIDE]++);
            i = (isTurf ? this.ties[SPREAD_TURF]++ : this.ties[SPREAD_GRASS]++);
        }
    }

    public int getTeamId() {
        return this.teamId;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public int getWins(int type) {
        return this.wins[type];
    }

    public int getLosses(int type) {
        return this.losses[type];
    }

    public int getTies(int type) {
        return this.ties[type];
    }

    public String getRecord(int type) {
        if(this.wins[type] == 0 && this.losses[type] == 0 && this.ties[type] == 0) {
            return "&nbsp;";
        }
        else if(this.ties[type] == 0) {
            return this.wins[type] + "-" + this.losses[type];
        }
        else {
            return this.wins[type] + "-" + this.losses[type] + "-" + this.ties[type];
        }
    }
}
