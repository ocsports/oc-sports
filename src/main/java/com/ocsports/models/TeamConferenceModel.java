/*
 * Title         TeamConferenceModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class TeamConferenceModel implements BaseModel {
    private int      id;
    private String   name;
    private int      sportType;

    public TeamConferenceModel() {
        this(-1, "", -1);
    }
    
    public TeamConferenceModel(int _id, String _name, int _sport) {
        setId(_id);
        setName(_name);
        setSport(_sport);
    }
    
    public void setId(int _id) {
        this.id = _id;
    }
    
    public void setName(String _name) {
        this.name = _name;
    }
    
    public void setSport(int _sport) {
        this.sportType = _sport;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSport() {
        return this.sportType;
    }
}
