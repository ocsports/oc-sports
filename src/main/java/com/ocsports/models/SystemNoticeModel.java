/*
 * Title         SystemNoticeModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class SystemNoticeModel implements BaseModel {
    private int     noticeId;
    private long    createdDt;
	private long    updatedDt;
    private String  message;
    private boolean published;

    public SystemNoticeModel() {
        this(-1, -1, "", false, -1);
    }
    
    public SystemNoticeModel(int _id, long _created, String _message, boolean _published, long _updated) {
        setId(_id);
        setCreatedDate(_created);
        setMessage(_message);
        setPublished(_published);
		setUpdated( _updated);
    }
    
    public void setId(int _id) {
        this.noticeId = _id;
    }
    
    public void setCreatedDate(long _created) {
        this.createdDt = _created;
    }
    
    public void setMessage(String _message) {
        this.message = _message;
    }
    
    public void setPublished(boolean _published) {
        this.published = _published;
    }
	
	public void setUpdated(long _updated) {
		this.updatedDt = _updated;
	}
    
    public int getId() {
        return this.noticeId;
    }
    
    public long getCreatedDate() {
        return this.createdDt;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public boolean isPublished() {
        return this.published;
    }
	
	public long getUpdated() {
		return this.updatedDt;
	}

}
