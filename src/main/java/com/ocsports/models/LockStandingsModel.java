/*
 * Title         LockStandingsModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

import java.util.HashMap;

public class LockStandingsModel implements BaseModel {
    private UserModel  user;
    private int        wins;
    private int        losses;
    private int        ties;
    private HashMap    seriesMap;

    public LockStandingsModel(UserModel _user, int _wins, int _losses, int _ties, HashMap _seriesMap) {
        this.user = _user;
        this.wins = _wins;
        this.losses = _losses;
        this.ties = _ties;
        this.seriesMap = _seriesMap;
    }
    
    public UserModel getUserModel() {
        return this.user;
    }

    public int getUserId() {
        return this.user.getUserId();
    }

    public String getUserName() {
        return this.user.getNameDisplay();
    }

    public int getWins() {
        return this.wins;
    }

    public int getLosses() {
        return this.losses;
    }
    
    public int getTies() {
        return this.ties;
    }
    
    public HashMap getSeriesMap() {
        return this.seriesMap;
    }
}
