/*
 * Title         LeagueModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class LeagueModel implements BaseModel {
    private int     leagueId;
    private String  leagueName;
    private int     seasonId;
    private boolean leaguePublic;
    private String  leaguePassword;
    private int     leagueAdministrator;
    
    public LeagueModel() {
        this( -1, "", -1, false, "", -1 );
    }

    public LeagueModel(int _id, String _name, int _seasonId, boolean _public, String _password, int _administrator) {
        setId( _id );
        setName( _name );
        setSeasonId( _seasonId );
        setPublic( _public );
        setPassword( _password );
        setAdministrator( _administrator );
    }
    
    public void setId( int _id ) {
        this.leagueId = _id;
    }
    
    public void setName( String _name ) {
        this.leagueName = _name;
    }
    
    public void setSeasonId( int _seasonId ) {
        this.seasonId = _seasonId;
    }
    
    public void setPublic(boolean _public) {
        this.leaguePublic = _public;
    }
    
    public void setPassword(String _password) {
        this.leaguePassword = _password;
    }
    
    public void setAdministrator(int _administrator) {
        this.leagueAdministrator = _administrator;
    }
    
    public int getId() {
        return this.leagueId;
    }
    
    public String getName() {
        return this.leagueName;
    }
    
    public int getSeasonId() {
        return this.seasonId;
    }
    
    public boolean isPublic() {
        return this.leaguePublic;
    }
    
    public String getPassword() {
        return this.leaguePassword;
    }
    
    public int getAdministrator() {
        return this.leagueAdministrator;
    }
}
