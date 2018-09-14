package com.ocsports.models;

public class LeagueSeriesXrefModel implements BaseModel {
    private int     leagueId;
    private int     seriesId;
    private String  leagueWinners;
    private int     leagueHighScore;

    public LeagueSeriesXrefModel() {
        this( -1, -1, "", -1 );
    }

    public LeagueSeriesXrefModel(int _leagueId, int _seriesId, String _leagueWinners, int _leagueHighScore) {
        setLeagueId( _leagueId );
        setSeriesId( _seriesId );
        setLeagueWinners( _leagueWinners );
        setLeagueHighScore( _leagueHighScore );
    }

    public void setLeagueId( int _leagueId ) {
        this.leagueId = _leagueId;
    }

    public void setSeriesId( int _seriesId ) {
        this.seriesId = _seriesId;
    }

    public void setLeagueWinners(String _leagueWinners) {
        this.leagueWinners = _leagueWinners;
    }

    public void setLeagueHighScore(int _leagueHighScore) {
        this.leagueHighScore = _leagueHighScore;
    }

    public int getLeagueId() {
        return this.leagueId;
    }

    public int getSeriesId() {
        return this.seriesId;
    }

    public String getLeagueWinners() {
        return this.leagueWinners;
    }

    public int getLeagueHighScore() {
        return this.leagueHighScore;
    }
}
