/*
 * Title         GameModel.java
 * Created       April 11, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class GameModel implements BaseModel {
    private int         id;
    private int         seriesId;
    private long        startDate;
    private int         homeTeamId;
    private int         awayTeamId;
    private float       spread;
    private String      notes;
    private boolean     defaultPicksSet;

    private boolean     posted;
    private int         homeScore;
    private int         awayScore;

    public GameModel() {
        this(-1, -1, 0, -1, -1, -1, "", false, false, -1, -1);
    }

    public GameModel(int _id, int _seriesId, long _startDate, int _homeTeamId, 
                     int _awayTeamId, float _spread, String _notes, 
                     boolean _defPicksSet, boolean _posted, int _homeScore, int _awayScore) {
        setId( _id );
        setSeriesId( _seriesId );
        setStartDate( _startDate );
        setHomeTeamId( _homeTeamId );
        setAwayTeamId( _awayTeamId );
        setSpread( _spread );
        setNotes( _notes );
        setDefaultPicks( _defPicksSet );
        setPosted( _posted );
        setHomeScore(_homeScore);
        setAwayScore(_awayScore);
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public void setSeriesId(int _seriesId) {
        this.seriesId = _seriesId;
    }

    public void setStartDate(long _startDate) {
        this.startDate = _startDate;
    }

    public void setHomeTeamId(int _homeTeamId) {
        this.homeTeamId = _homeTeamId;
    }

    public void setAwayTeamId(int _awayTeamId) {
        this.awayTeamId = _awayTeamId;
    }

    public void setSpread(float _spread) {
        this.spread = _spread;
    }

    public void setNotes(String _notes) {
        this.notes = _notes;
    }

    public void setDefaultPicks(boolean _defPicksSet) {
        this.defaultPicksSet = _defPicksSet;
    }

    public void setPosted(boolean _posted) {
        this.posted = _posted;
    }

    public void setHomeScore(int _homeScore) {
        this.homeScore = _homeScore;
    }

    public void setAwayScore(int _awayScore) {
        this.awayScore = _awayScore;
    }

    public int getId() {
        return this.id;
    }

    public int getSeriesId() {
        return this.seriesId;
    }

    public long getStartDate() {
        return this.startDate;
    }

    public int getHomeTeamId() {
        return this.homeTeamId;
    }

    public int getAwayTeamId() {
        return this.awayTeamId;
    }

    public float getSpread() {
        return this.spread;
    }

    public String getNotes() {
        return this.notes;
    }

    public boolean isDefaultPicksSet() {
        return this.defaultPicksSet;
    }

    public boolean isPosted() {
        return this.posted;
    }

    public int getHomeScore() {
        return this.homeScore;
    }

    public int getAwayScore() {
        return this.awayScore;
    }

    public int getFavoredTeam() {
        return (this.getSpread() <= 0 ? this.homeTeamId : this.awayTeamId);
    }

    public int getUnderdogTeam() {
        return (this.getSpread() > 0 ? this.homeTeamId : this.awayTeamId);
    }
}
