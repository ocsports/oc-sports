/*
 * Title         SeasonSeriesModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class SeasonSeriesModel implements BaseModel {
    private int         id;
    private int         seasonId;
    private long        startDate;
    private long        endDate;
    private boolean     spreadPublished;
    private boolean     userCleanup;
    private boolean     reminderEmail;
    private int         seq;
    private boolean     gamesCompleted;

    public SeasonSeriesModel() {
        this(-1, -1, -1, -1, false, false, false, -1, false);
    }

    public SeasonSeriesModel(int _id, int _seasonId, long _startDt, long _endDt,
                             boolean _spreadPublished, boolean _userCleanup,
                             boolean _reminderEmail, int _sequence, boolean _gamesCompleted) {
        setId( _id );
        setSeasonId( _seasonId );
        setStartDate( _startDt );
        setEndDate( _endDt );
        setSpreadPublished( _spreadPublished );
        setUserCleanup( _userCleanup );
        setReminderEmail( _reminderEmail );
        setSequence( _sequence );
        setGamesCompleted( _gamesCompleted );
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public void setSeasonId(int _seasonId) {
        this.seasonId = _seasonId;
    }

    public void setStartDate(long _startDate) {
        this.startDate = _startDate;
    }

    public void setEndDate(long _endDate) {
        this.endDate = _endDate;
    }

    public void setSpreadPublished(boolean _spreadPublished) {
        this.spreadPublished = _spreadPublished;
    }

    public void setReminderEmail(boolean _reminderEmail) {
        this.reminderEmail = _reminderEmail;
    }

    public void setUserCleanup(boolean _userCleanup) {
        this.userCleanup = _userCleanup;
    }

    public void setSequence(int _sequence) {
        this.seq = _sequence;
    }

    public void setGamesCompleted(boolean _gamesCompleted) {
        this.gamesCompleted = _gamesCompleted;
    }

    public int getId() {
        return this.id;
    }

    public int getSeasonId() {
        return this.seasonId;
    }

    public long getStartDate() {
        return this.startDate;
    }

    public long getEndDate() {
        return this.endDate;
    }

    public boolean isSpreadPublished() {
        return this.spreadPublished;
    }

    public boolean isReminderEmail() {
        return this.reminderEmail;
    }

    public boolean isUserCleanup() {
        return this.userCleanup;
    }

    public int getSequence() {
        return this.seq;
    }

    public boolean isGamesCompleted() {
        return this.gamesCompleted;
    }
}
