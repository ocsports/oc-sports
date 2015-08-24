/*
 * Title         AuditPickModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class AuditPickModel implements BaseModel {
    public static final String TYPE_GAME = "GAME";
    public static final String TYPE_LOCK = "LOCK";
    public static final String TYPE_SURVIVOR = "SURVIVOR";

    private java.util.Date   timestamp;
    private String           type;
    private int              userId;
    private int              gameId;
    private int              seriesId;
    private int              teamId;

    public AuditPickModel() {
        this(null, "", -1, -1, -1, -1);
    }

    public AuditPickModel(java.util.Date _timestamp, String _type, int _userId, 
                          int _gameId, int _seriesId, int _teamId) {
        setTimestamp(_timestamp);
        setType(_type);
        setUserId(_userId);
        setGameId(_gameId);
        setSeriesId(_seriesId);
        setTeamId(_teamId);
    }
    
    public void setTimestamp(java.util.Date _timestamp) {
        this.timestamp = _timestamp;
    }
    
    public void setType(String _type) {
        this.type = _type;
    }
    
    public void setUserId(int _userId) {
        this.userId = _userId;
    }
    
    public void setGameId(int _gameId) {
        this.gameId = _gameId;
    }
    
    public void setSeriesId(int _seriesId) {
        this.seriesId = _seriesId;
    }
    
    public void setTeamId(int _teamId) {
        this.teamId = _teamId;
    }
    
    public java.util.Date getTimestamp() {
        return this.timestamp;
    }
    
    public String getType() {
        return this.type;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public int getGameId() {
        return this.gameId;
    }
    
    public int getSeriesId() {
        return this.seriesId;
    }
    
    public int getTeamId() {
        return this.teamId;
    }
}
