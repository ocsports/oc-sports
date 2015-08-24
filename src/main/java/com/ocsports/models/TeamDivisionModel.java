/*
 * Title         TeamDivisionModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class TeamDivisionModel implements BaseModel {
    private int      id;
    private String   name;
    private int      conferenceId;

    public TeamDivisionModel() {
        this(-1, "", -1);
    }
    
    public TeamDivisionModel(int _id, String _name, int _conference) {
        setId(_id);
        setName(_name);
        setConference(_conference);
    }
    
    public void setId(int _id) {
        this.id = _id;
    }
    
    public void setName(String _name) {
        this.name = _name;
    }
    
    public void setConference(int _conference) {
        this.conferenceId = _conference;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getConference() {
        return this.conferenceId;
    }
}
