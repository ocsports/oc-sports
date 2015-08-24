/*
 * Title         GameDisplayModel.java
 * Created       May 11, 2009
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class GameDisplayModel extends GameModel implements BaseModel {
    private TeamModel    awayTeam;
    private TeamModel    homeTeam;
    private int          selectedTeamId;

    public GameDisplayModel() {
        super();
    }
    
    public void setHomeTeam(TeamModel homeTeam) {
        this.homeTeam = homeTeam;
    }
    
    public TeamModel getHomeTeam() {
        return this.homeTeam;
    }
    
    public void setAwayTeam(TeamModel awayTeam) {
        this.awayTeam = awayTeam;
    }
    
    public TeamModel getAwayTeam() {
        return this.awayTeam;
    }
    
    public String getAwayTeamDisplay() {
        String s = "";
        if(this.awayTeam != null)
            s = this.awayTeam.getCity() + " " + this.awayTeam.getName();
        return s;
    }
    
    public String getHomeTeamDisplay() {
        String s = "";
        if(this.homeTeam != null)
            s = this.homeTeam.getCity() + " " + this.homeTeam.getName();
        return s;
    }
    
    public void setSelectedTeamId(int selectedTeamId) {
        this.selectedTeamId = selectedTeamId;
    }
    
    public int getSelectedTeamId() {
        return this.selectedTeamId;
    }
}
