/*
 * Title         TeamModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class TeamModel implements BaseModel {
    private int         id;
    private String      city;
    private String      name;
    private String      abrv;
    private int         division;
    private boolean     dome;
    private boolean     turf;
    private String      weatherURL;

    public TeamModel() {
        this(-1, "", "", "", -1, false, false, "");
    }
    
    public TeamModel(int _id, String _city, String _name, String _abrv, 
                     int _division, boolean _dome, boolean _turf, String _weatherURL) {
        setId( _id );
        setCity( _city );
        setName( _name );
        setAbrv( _abrv );
        setDivision( _division );
        setDome( _dome );
        setTurf( _turf );
        setWeatherURL( _weatherURL );
    }
    
    public void setId(int _id) {
        this.id = _id;
    }
    
    public void setCity(String _city) {
        this.city = _city;
    }
    
    public void setName(String _name) {
        this.name = _name;
    }
    
    public void setAbrv(String _abrv) {
        this.abrv = _abrv;
    }
    
    public void setDivision(int _division) {
        this.division = _division;
    }
    
    public void setDome(boolean _dome) {
        this.dome = _dome;
    }
    
    public void setTurf(boolean _turf) {
        this.turf = _turf;
    }
    
    public void setWeatherURL(String _weatherURL) {
        this.weatherURL = _weatherURL;
    }

    public int getId() {
        return this.id;
    }
    
    public String getCity() {
        return this.city;
    }

    public String getName() {
        return this.name;
    }

    public String getAbrv() {
        return this.abrv;
    }

    public int getDivision() {
        return this.division;
    }

    public boolean isDome() {
        return this.dome;
    }

    public boolean isTurf() {
        return this.turf;
    }

    public String getWeatherURL() {
        return this.weatherURL;
    }

    public String getAbrvFormatted() {
        String s = this.abrv;
        while(s.length() < 3) {
            s += " ";
        }
        return s;
    }
}
