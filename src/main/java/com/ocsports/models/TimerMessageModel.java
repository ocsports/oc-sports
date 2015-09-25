/*
 * Title         TimerMessageModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class TimerMessageModel implements BaseModel {
    private String          subject;
    private String          taskName;
    private String          msg;
    private java.util.Date  timestamp;

    public TimerMessageModel(String _subject, String _taskName, String _msg) {
        this.subject = _subject;
        this.taskName = _taskName;
        this.msg = _msg;
        this.timestamp = new java.util.Date();
    }
    
    public String getSubject() {
        return this.subject;
    }
    
    public String getTaskName() {
        return taskName.substring( taskName.lastIndexOf(".")+1 );
    }
    
    public String getMessage() {
        return this.msg;
    }
    
    public java.util.Date getTimestamp() {
        return this.timestamp;
    }
}
