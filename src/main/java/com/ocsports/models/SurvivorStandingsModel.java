/*
 * Title         SurvivorStandingsModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

import java.util.HashMap;

public class SurvivorStandingsModel implements BaseModel {
    private UserModel  user;
    private HashMap    seriesMap;

    public SurvivorStandingsModel(UserModel _user, HashMap _seriesMap) {
        this.user = _user;
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

    public HashMap getSeriesMap() {
        return this.seriesMap;
    }
}
