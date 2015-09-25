/*
 * Title         SeasonModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class SeasonModel implements BaseModel {
    private int         seasonId;
    private String      seasonName;
    private int         sportType;
    private String      seriesPrefix;
    private boolean     active;

    public SeasonModel() {
        this(-1, "", -1, "", false);
    }

    public SeasonModel(int _id, String _name, int _sportType, String _seriesPrefix, boolean _active) {
        setId( _id );
        setName( _name );
        setSportType( _sportType );
        setSeriesPrefix( _seriesPrefix );
        setActive( _active );
    }
    
    public void setId(int _id) {
        this.seasonId = _id;
    }
    
    public void setName(String _name) {
        this.seasonName = _name;
    }
    
    public void setSportType(int _sportType) {
        this.sportType = _sportType;
    }
    
    public void setSeriesPrefix(String _prefix) {
        this.seriesPrefix = _prefix;
    }
    
    public void setActive(boolean _active) {
        this.active = _active;
    }

    public int getId() {
        return this.seasonId;
    }
    
    public String getName() {
        return this.seasonName;
    }
    
    public int getSportType() {
        return this.sportType;
    }
    
    public String getSeriesPrefix() {
        return this.seriesPrefix;
    }
    
    public boolean isActive() {
        return this.active;
    }
}
