/*
 * Title         UserModel.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.models;
 */
package com.ocsports.models;

public class UserModel implements BaseModel {
    private int      userId;
    private String   firstName;
    private String   middleName;
    private String   lastName;
    private String   address;
    private String   email;
    private String   email2;
    private String   loginId;
    private String   loginPwd;
    private int      leagueId;
    private int      defaultPick;
    private boolean  emailPicks;
    private boolean  sendWarning;
    private boolean  paid;
    private int      favoriteTeamId;
    private String   favoriteTeam;
    private String   colorTheme;
    private boolean  loginDisabled;

    public UserModel() {
        this(-1, "", "", "", "", "", "", "", "", -1, -1, true, true, false, 0, "", "", false);
    }
    
    public UserModel(int _userId, String _firstName, String _middleName, 
                     String _lastName, String _address, String _email, String _email2, 
                     String _loginId, String _loginPwd, int _leagueId, 
                     int _defaultPick, boolean _emailPicks, boolean _sendWarning,
                     boolean _paid, int _favoriteTeamId,
                     String _favoriteTeam, String _colorTheme, boolean _loginDisabled) {
        setUserId( _userId );
        setFirstName( _firstName );
        setMiddleName( _middleName );
        setLastName( _lastName );
        setAddress( _address );
        setEmail( _email );
        setEmail2( _email2 );
        setLoginId( _loginId );
        setLoginPwd( _loginPwd );
        setLeagueId( _leagueId );
        setDefaultPick( _defaultPick );
        setEmailPicks( _emailPicks );
        setSendWarning( _sendWarning );
        setPaid( _paid );
        setFavoriteTeam( _favoriteTeamId, _favoriteTeam );
        setColorTheme( _colorTheme );
        setLoginDisabled( _loginDisabled );
    }

    public void setUserId( int _userId ) {
        this.userId = _userId;
    }
    
    public void setFirstName( String _firstName ) {
        this.firstName = _firstName;
    }
    
    public void setMiddleName( String _middleName ) {
        this.middleName = _middleName;
    }
    
    public void setLastName( String _lastName ) {
        this.lastName = _lastName;
    }
    
    public void setAddress(String _address) {
        this.address = _address;
    }
    
    public void setEmail( String _email ) {
        this.email = _email;
    }
    
    public void setEmail2(String _email) {
        this.email2 = _email;
    }
    
    public void setLoginId( String _loginId ) {
        this.loginId = _loginId;
    }
    
    public void setLoginPwd( String _loginPwd ) {
        this.loginPwd = _loginPwd;
    }
    
    public void setLeagueId( int _leagueId ) {
        this.leagueId = _leagueId;
    }
    
    public void setDefaultPick(int _defaultPick) {
        this.defaultPick = _defaultPick;
    }
    
    public void setEmailPicks(boolean _emailPicks) {
        this.emailPicks = _emailPicks;
    }
    
    public void setSendWarning(boolean _sendWarning) {
        this.sendWarning = _sendWarning;
    }
    
    public void setPaid(boolean _paid) {
        this.paid = _paid;
    }
    
    public void setFavoriteTeam(int _favoriteTeamId, String _favoriteTeam) {
        this.favoriteTeamId = _favoriteTeamId;
        this.favoriteTeam = _favoriteTeam;
    }
    
    public void setColorTheme(String _colorTheme) {
        this.colorTheme = _colorTheme;
    }
    
    public void setLoginDisabled(boolean _loginDisabled) {
        this.loginDisabled = _loginDisabled;
    }

    public int getUserId() {
        return this.userId;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public String getMiddleName() {
        return this.middleName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public String getAddressJS() {
        return this.address.replaceAll("\n", " ");
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getEmail2() {
        return this.email2;
    }
    
    public String getLoginId() {
        return this.loginId;
    }
    
    public String getLoginPwd() {
        return this.loginPwd;
    }
    
    public int getLeagueId() {
        return this.leagueId;
    }
    
    public int getDefaultPick() {
        return this.defaultPick;
    }

    public boolean emailPicks() {
        return this.emailPicks;
    }
    
    public boolean sendWarning() {
        return this.sendWarning;
    }

    public String getFullName() {
        return (this.firstName + " " + this.middleName).trim() + " " + this.lastName;
    }
    
    public boolean isPaid() {
        return this.paid;
    }
    
    public int getFavoriteTeam() {
        return this.favoriteTeamId;
    }
    
    public String getFavoriteTeamName() {
        return this.favoriteTeam;
    }
    
    public String getColorTheme() {
        return this.colorTheme;
    }
    
    public boolean isLoginDisabled() {
        return this.loginDisabled;
    }
    
    public String getNameDisplay() {
        String s = "";
        //if(this.favoriteTeamId > 0) {
        //    s = "<img valign='middle' src='../images/teams/" + this.favoriteTeam + "_helmet.gif' width='20' height='20'/>";
        //}
        s += this.loginId;
        return s;
    }
}
