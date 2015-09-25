/*
 * Title         UserSeriesXrefModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class UserSeriesXrefModel implements BaseModel {
    private int             userId;
    private int             seriesId;
    private int             survivorTeamId;
    private int             lockTeamId;
    private String          notes;
    private int             lockStatus;
    private int             survivorStatus;
    private java.util.Date  timestamp;

    public UserSeriesXrefModel() {
        this(-1, -1, -1, -1, "", -1, -1, null);
    }

    public UserSeriesXrefModel(int _userId, int _seriesId, int _survivor, int _lock, String _notes, int _lockStatus, int _survivorStatus, java.util.Date _timestamp) {
        setUserId(_userId);
        setSeriesId(_seriesId);
        setSurvivor(_survivor);
        setLock(_lock);
        setNotes(_notes);
        setLockStatus(_lockStatus);
        setSurvivorStatus(_survivorStatus);
        setTimestamp(_timestamp);
    }

    public void setUserId(int _userId) {
        this.userId = _userId;
    }

    public void setSeriesId(int _seriesId) {
        this.seriesId = _seriesId;
    }

    public void setSurvivor(int _survivor) {
        this.survivorTeamId = _survivor;
    }

    public void setLock(int _lock) {
        this.lockTeamId = _lock;
    }

    public void setNotes(String _notes) {
        this.notes = _notes;
    }

    public void setLockStatus(int _lockStatus) {
        this.lockStatus = _lockStatus;
    }
    
    public void setSurvivorStatus(int _survivorStatus) {
        this.survivorStatus = _survivorStatus;
    }
    
    public void setTimestamp(java.util.Date _timestamp) {
        this.timestamp = _timestamp;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getSeriesId() {
        return this.seriesId;
    }

    public int getSurvivor() {
        return this.survivorTeamId;
    }

    public int getLock() {
        return this.lockTeamId;
    }

    public String getNotes() {
        return this.notes;
    }

    public int getLockStatus() {
        return this.lockStatus;
    }
    
    public int getSurvivorStatus() {
        return this.survivorStatus;
    }
    
    public java.util.Date getTimestamp() {
        return this.timestamp;
    }
}
