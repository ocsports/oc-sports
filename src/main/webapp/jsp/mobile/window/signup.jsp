<%@page contentType="text/html"%>
<%@page import="java.util.*"%>
<%@page import="com.ocsports.sql.SeasonSQLController"%>
<%@page import="com.ocsports.core.*"%>
<%@page import="com.ocsports.models.UserModel"%>
<%@page import="com.ocsports.models.TeamModel"%>
<%
    Collection signupErrors = (Collection)request.getAttribute("profileErrors");
    
    UserModel um = (UserModel)request.getAttribute("UserModel");
    if(um == null) um = new UserModel();
    
    HashMap teams = null;
    SeasonSQLController seasonSql = null;
    try {
        seasonSql = new SeasonSQLController();
        teams = seasonSql.getTeamMap( SportTypes.TYPE_NFL_FOOTBALL );
        if(teams != null) request.setAttribute("teamMap", teams);
    }
    catch(Exception ex) {
    }
    finally {
        if (seasonSql != null) seasonSql.closeConnection();
    }
%>
<jsp:include page="/jsp/mobile/window/page_header.jsp" />

<div id="divSignup">
    <form name="frmSignup" action="<%=request.getContextPath()%>/servlet/goUser?r=saveProfile" method="POST">
        <input type="hidden" name="userId" value="<%=um.getUserId()%>" />
        <input type="hidden" name="leagueId" value="<%=um.getLeagueId()%>" />
        <table cellspacing="0" cellpadding="2" border="0" class="trtable">
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
                    <input type="button" value="Cancel" onclick="hideDiv('divSignup')" class="formbutton2" />
                </td>
            </tr>
        </table>
    </form>
</div>

<jsp:include page="/jsp/mobile/window/page_footer.jsp" />
