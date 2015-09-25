/*
 * Title         AuditLoginModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class AuditLoginModel implements BaseModel {
    private java.util.Date   timestamp;
    private String           loginId;
    private String           loginPwd;
    private int              userId;  //for successful logins only

    public AuditLoginModel() {
        this(null, "", "", -1);
    }

    public AuditLoginModel(java.util.Date _timestamp, String _loginId, String _pwd, int _userId) {
        setTimestamp(_timestamp);
        setLoginId(_loginId);
        setLoginPwd(_pwd);
        setUserId(_userId);
    }
    
    public void setTimestamp(java.util.Date _timestamp) {
        this.timestamp = _timestamp;
    }
    
    public void setLoginId(String _loginId) {
        this.loginId = _loginId;
    }
    
    public void setLoginPwd(String _pwd) {
        this.loginPwd = _pwd;
    }
    
    public void setUserId(int _userId) {
        this.userId = _userId;
    }
    
    public java.util.Date getTimestamp() {
        return this.timestamp;
    }
    
    public String getLoginId() {
        return this.loginId;
    }
    
    public String getLoginPwd() {
        return this.loginPwd;
    }
    
    public int getUserId() {
        return this.userId;
    }
}
