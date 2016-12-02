<%@page contentType="text/html"%>
<%@page import="java.util.*"%>
<%@page import="com.ocsports.core.*"%>
<%@page import="com.ocsports.sql.SeasonSQLController"%>
<%@page import="com.ocsports.models.TeamModel"%>
<%@page import="com.ocsports.models.UserModel"%>
<%
    String loginError = (String)request.getAttribute("loginError");
    Collection signupErrors = (Collection)request.getAttribute("profileErrors");
    String loginId = (String)request.getAttribute("loginId");
    if(loginId == null) loginId = "";
    
    UserModel um = (UserModel)request.getAttribute("UserModel");
    if(um == null) um = new UserModel();
    
    HashMap teams = null;
    SeasonSQLController seasonSql = null;
    try {
        seasonSql = new SeasonSQLController();
        teams = seasonSql.getTeamMap( SportTypes.TYPE_NFL_FOOTBALL );
    }
    catch(Exception ex) {
    }
    finally {
        if (seasonSql != null) seasonSql.closeConnection();
    }
%>
			</td>
		</tr>
	</table>
	<div style="margin-left:1em; margin-top:1em; border-top:1px solid #DDDDDD">
        <a href="<%=request.getContextPath()%>/servlet/goUser?r=switchView" style="font-size:7pt; padding-left:1em">view mobile version</a>
	</div>
        <div id="divLogin" class="msg">
		<form name="frmLogin" action="<%=request.getContextPath()%>/servlet/goUser?r=login" method="POST">
			<table cellspacing="0" cellpadding="5" border="0">
				<tr>
					<td>Login Id</td>
					<td><input type="text" name="loginId" value="<%=loginId%>" style="text-align:center; width:8em"></td>
				</tr>
				<tr>
					<td>Password</td>
					<td><input type="password" name="loginPwd" style="text-align:center; width:8em"></td>
				</tr>
				<%if(loginError != null && loginError.length() > 0) {%>
				<tr>
                    <td colspan="2" class="errortext"><%=loginError%></td>
				</tr>
				<%}%>
				<tr>
					<td colspan="2" style="text-align:left; padding-top:0.5em; padding-bottom:1em">
						<input type="submit" name="btnLogin" value="Login" onclick="hideDiv('divLogin')" class="formbutton" />
						<input type="button" name="btnCancel" value="Cancel" onclick="hideDiv('divLogin')" class="formbutton" />
					</td>
				</tr>
			</table>
		</form>
	</div>
    <div id="divSignup" class="msg">
        <form name="frmSignup" action="<%=request.getContextPath()%>/servlet/goUser?r=saveProfile" method="POST">
            <input type="hidden" name="userId" value="<%=um.getUserId()%>" />
            <input type="hidden" name="leagueId" value="<%=um.getLeagueId()%>" />
            <table cellspacing="0" cellpadding="2" border="0">
                <tr>
                    <td width="150px">First Name *</td>
                    <td><input type="text" name="firstName" maxlength="30" value="<%=um.getFirstName()%>" style="width:10em" /></td>
                </tr>
                <tr>
                    <td>Last Name *</td>
                    <td><input type="text" name="lastName" maxlength="30" value="<%=um.getLastName()%>" style="width:20em" /></td>
                </tr>
                <tr valign="top">
                    <td>Address</td>
                    <td><textarea cols="30" rows="4" name="addr" style="width:20em"><%=um.getAddress()%></textarea></td>
                </tr>
                <tr>
                    <td>Email Address *</td>
                    <td><input type="text" name="email" maxlength="64" value="<%=(um.getEmail() == null ? "" : um.getEmail())%>" style="width:20em" /></td>
                </tr>
                <tr>
                    <td>Alt. Email Address</td>
                    <td><input type="text" name="email2" maxlength="64" value="<%=(um.getEmail2() == null ? "" : um.getEmail2())%>" style="width:20em" /></td>
                </tr>
                <tr valign="top">
                    <td style="padding-top:1em">Login Id *</td>
                    <td style="padding-top:1em"><input type="text" name="loginId" maxlength="20" value="<%=um.getLoginId()%>" style="width:8em; text-align:center" /><span style="padding-left:1em">{player nickname}</span></td>
                </tr>
                <tr>
                    <td>Password *</td>
                    <td><input type="password" name="loginPwd" maxlength="20" value="<%=um.getLoginPwd()%>" style="width:8em; text-align:center" /></td>
                </tr>
                <tr>
                    <td>Confirm Password *</td>
                    <td><input type="password" name="loginPwd2" maxlength="20" value="<%=um.getLoginPwd()%>" style="width:8em; text-align:center" /></td>
                </tr>
                <tr>
                    <td style="padding-top:1em">Default Pick *</td>
                    <td style="padding-top:1em">
                        <select name="defaultPick" style="width:10em">
                            <option value="<%=Status.PICK_HOME_TEAM%>" <%=(um.getDefaultPick() == Status.PICK_HOME_TEAM ? "SELECTED" : "")%>>Home Team</option>
                            <option value="<%=Status.PICK_AWAY_TEAM%>" <%=(um.getDefaultPick() == Status.PICK_AWAY_TEAM ? "SELECTED" : "")%>>Away Team</option>
                            <option value="<%=Status.PICK_FAVORED%>" <%=(um.getDefaultPick() == Status.PICK_FAVORED ? "SELECTED" : "")%>>Favored</option>
                            <option value="<%=Status.PICK_UNDERDOG%>" <%=(um.getDefaultPick() == Status.PICK_UNDERDOG ? "SELECTED" : "")%>>Underdog</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Favorite Team</td>
                    <td>
                        <select name="favoriteTeam" style="width:15em">
                            <option value="">&lt;none&gt;</option>
                            <%
                            Iterator teamIter = teams.keySet().iterator();
                            while( teamIter.hasNext() ) {
                                String teamId = (String)teamIter.next();
                                TeamModel tm = (TeamModel)teams.get(teamId);
                            %>
                                <option value='<%=tm.getId()%>^<%=tm.getName().toLowerCase()%>' <%=(um.getFavoriteTeam() == tm.getId() ? "SELECTED" : "")%>><%=tm.getCity() + " " + tm.getName()%></option>
                            <%}%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td class="smalllabel">* Required Fields</td>
                </tr>
                <%
                if( signupErrors != null && !signupErrors.isEmpty() ) {
                    String errMsg = "";
                    Iterator iterSignup = signupErrors.iterator();
                    while( iterSignup.hasNext() ) {
                        errMsg += (String)iterSignup.next() + "<br/>";
                    }
                    %>
                    <tr>
                        <td colspan="2" class="errortext"><%=errMsg%></td>
                    </tr>
                    <%
                }
                %>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Join Now" class="formbutton" />
                        <input type="button" value="Cancel" class="formbutton2" onclick="hideDiv('divSignup')" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
	<div id="divBlockScreen" class="blockScreen">&nbsp;</div>
    <%if( loginError != null && loginError.length() > 0 ) {%>
        <script language="javascript">showLogin();</script>
    <%} else if( signupErrors != null && !signupErrors.isEmpty() ) {%>
        <script language="javascript">showSignup();</script>
    <%}%>
</body>
</html>
