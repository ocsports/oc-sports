/*
 * Title         GameStatsModel.java
 * Created       April 11, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class GameStatsModel implements BaseModel {
    private int     gameId;
    private int     teamId;
    private int     score;
    private int     rushAttempts;
    private int     rushYards;
    private int     passAttempts;
    private int     passYards;
    private int     firstDowns;
    private int     sacks;
    private int     turnovers;

    public GameStatsModel() {
        this(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
    }

    public GameStatsModel(int _gameId, int _teamId, int _score, int _rushAttempts, 
            int _rushYards, int _passAttempts, int _passYards, int _firstDowns, 
            int _sacks, int _turnovers) {
        setGameId(_gameId);
        setTeamId(_teamId);
        setScore(_score);
        setRushAttempts(_rushAttempts);
        setRushYards(_rushYards);
        setPassAttempts(_passAttempts);
        setPassYards(_passYards);
        setFirstDowns(_firstDowns);
        setSacks(_sacks);
        setTurnovers(_turnovers);
    }

    public void setGameId(int _gameId) {
        this.gameId = _gameId;
    }

    public void setTeamId(int _teamId) {
        this.teamId = _teamId;
    }

    public void setScore(int _score) {
        this.score = _score;
    }

    public void setRushAttempts(int _rushAttempts) {
        this.rushAttempts = _rushAttempts;
    }

    public void setRushYards(int _rushYards) {
        this.rushYards = _rushYards;
    }

    public void setPassAttempts(int _passAttempts) {
        this.passAttempts = _passAttempts;
    }

    public void setPassYards(int _passYards) {
        this.passYards = _passYards;
    }

    public void setFirstDowns(int _firstDowns) {
        this.firstDowns = _firstDowns;
    }

    public void setSacks(int _sacks) {
        this.sacks = _sacks;
    }

    public void setTurnovers(int _turnovers) {
        this.turnovers = _turnovers;
    }

    public int getGameId() {
        return this.gameId;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public int getScore() {
        return this.score;
    }

    public int getRushAttempts() {
        return this.rushAttempts;
    }

    public int getRushYards() {
        return this.rushYards;
    }

    public int getPassAttempts() {
        return this.passAttempts;
    }

    public int getPassYards() {
        return this.passYards;
    }

    public int getFirstDowns() {
        return this.firstDowns;
    }

    public int getSacks() {
        return this.sacks;
    }

    public int getTurnovers() {
        return this.turnovers;
    }
}
