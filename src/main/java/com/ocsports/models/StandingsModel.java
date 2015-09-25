/*
 * Title         StandingsModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

import java.util.HashMap;

public class StandingsModel implements BaseModel {
    private UserModel  user;
    private int        seasonWins;
    private int        seasonLosses;
    private HashMap    seriesMap;

    public StandingsModel(UserModel _user, int _wins, int _losses, HashMap _seriesMap) {
        this.user = _user;
        this.seasonWins = _wins;
        this.seasonLosses = _losses;
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
        return this.seasonWins;
    }

    public int getLosses() {
        return this.seasonLosses;
    }
    
    public HashMap getSeriesMap() {
        return this.seriesMap;
    }
}
