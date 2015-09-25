/*
 * Title         ForumMessageModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class ForumMessageModel implements BaseModel {
    private int      msgId;
    private int      leagueId;
    private String   msgText;
    private long     created;
    private int      createdBy;
    private String   createdByName;

    public ForumMessageModel() {
        this(-1, -1, "", -1, -1, "");
    }

    public ForumMessageModel(int _id, int _leagueId, String _text, long _created, 
                             int _createdBy, String _createdByName) {
        setId(_id);
        setLeagueId(_leagueId);
        setText(_text);
        setCreatedDate(_created);
        setCreatedBy(_createdBy);
        setCreatedByName(_createdByName);
    }

    public void setId(int _id) {
        this.msgId = _id;
    }
    
    public void setLeagueId(int _leagueId) {
        this.leagueId = _leagueId;
    }

    public void setText(String _text) {
        this.msgText = _text;
    }
    
    public void setCreatedDate(long _created) {
        this.created = _created;
    }
    
    public void setCreatedBy(int _createdBy) {
        this.createdBy = _createdBy;
    }
    
    public void setCreatedByName(String _name) {
        this.createdByName = _name;
    }
    
    public int getId() {
        return this.msgId;
    }
    
    public int getLeagueId() {
        return this.leagueId;
    }
    
    public String getText() {
        return this.msgText;
    }
    
    public long getCreatedDate() {
        return this.created;
    }
    
    public int getCreatedBy() {
        return this.createdBy;
    }
    
    public String getCreatedByName() {
        return this.createdByName;
    }
}
