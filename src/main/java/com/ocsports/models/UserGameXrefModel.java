/*
 * Title         UserGameXrefModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

import com.ocsports.core.Status;

public class UserGameXrefModel implements BaseModel {
    private int             userId;
    private int             gameId;
    private int             selectedTeamId;
    private String          notes;
    private int             status;
    private boolean         defaultPick;
    private java.util.Date  timestamp;

    public UserGameXrefModel() {
        this(-1, -1, -1, "", Status.GAME_STATUS_NO_DECISION, false, null);
    }

    public UserGameXrefModel(int _userId, int _gameId, int _teamId, 
        String _notes, int _status, boolean _defPick, java.util.Date _timestamp) {
        setUserId(_userId);
        setGameId(_gameId);
        setSelectedTeamId(_teamId);
        setNotes(_notes);
        setStatus(_status);
        setDefaultPick(_defPick);
        setTimestamp(_timestamp);
    }

    public void setUserId(int _userId) {
        this.userId = _userId;
    }

    public void setGameId(int _gameId) {
        this.gameId = _gameId;
    }

    public void setSelectedTeamId(int _teamId) {
        this.selectedTeamId = _teamId;
    }

    public void setNotes(String _notes) {
        this.notes = _notes;
    }

    public void setStatus(int _status) {
        this.status = _status;
    }
    
    public void setDefaultPick(boolean _defPick) {
        this.defaultPick = _defPick;
    }
    
    public void setTimestamp(java.util.Date _timestamp) {
        this.timestamp = _timestamp;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getGameId() {
        return this.gameId;
    }

    public int getSelectedTeamId() {
        return this.selectedTeamId;
    }

    public String getNotes() {
        return this.notes;
    }

    public int getStatus() {
        return this.status;
    }
    
    public boolean isDefaultPick() {
        return this.defaultPick;
    }
    
    public java.util.Date getTimestamp() {
        return this.timestamp;
    }
}
