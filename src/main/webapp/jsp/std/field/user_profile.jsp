<%@page contentType="text/html"%>
<%@page import="java.util.*"%>
<%@page import="com.ocsports.core.Status"%>
<%@page import="com.ocsports.servlets.AdminServlet"%>
<%@page import="com.ocsports.models.UserModel"%>
<%@page import="com.ocsports.models.TeamModel"%>
<%@page import="com.ocsports.core.PropertiesHelper"%>
<%
    UserModel um = (UserModel)request.getAttribute("UserModel");
    Collection errors = (Collection)request.getAttribute("profileErrors");
    HashMap teams = (HashMap)request.getAttribute("teamMap");
    if(um == null) um = new UserModel();
    if(teams == null) teams = new HashMap();
    int userId = um.getUserId();

    String saveURL = response.encodeURL("goUser?r=saveProfile");
    String homeURL = response.encodeURL("goUser?r=home");
    String pageTitle = "Change Player Profile";
%>
<jsp:include page="/jsp/std/field/page_header.jsp" />

<h2><%=pageTitle%></h2>
<div style="padding-top:0em; padding-left:4em">
    <form name="frmProfile" action="<%=saveURL%>" method="POST">
        <input type="hidden" name="userId" value="<%=um.getUserId()%>" />
        <input type="hidden" name="leagueId" value="<%=um.getLeagueId()%>" />
        <input type="hidden" name="paid" value="<%=(um.isPaid() ? "Y" : "N")%>" />
        <table cellspacing="0" cellpadding="3" border="0">
            <%
            if( errors != null && !errors.isEmpty() ) {
                String errMsg = "";
                Iterator iter = errors.iterator();
                while( iter.hasNext() ) {
                    errMsg += (String)iter.next() + "<br/>";
                }
                %>
                <tr>
                    <td colspan="2" class="redtext"><%=errMsg%></td>
                </tr>
                <%
            }
            %>
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
            <%
            String paymentDisplay = (um.isPaid() ? "Paid" : "Not Paid");
            String paymentColor = (um.isPaid() ? "darkgreen" : "red");
            %>
            <tr>
                <td>Payment Status</td>
                <td style="font-weight:bold; color:<%=paymentColor%>"><%=paymentDisplay%></td>
            </tr>
            <tr valign="top">
                <td style="padding-top:1em">Login Id *</td>
                <% if( PropertiesHelper.isShowSignup() ) { %>
                    <td style="padding-top:1em"><input type="text" name="loginId" maxlength="20" value="<%=um.getLoginId()%>" style="width:8em; text-align:center" /><span style="padding-left:1em">{player nickname}</span></td>
                <% } else{ %>
                    <td style="padding-top:1em"><%=um.getLoginId()%></td>
                    <input type="hidden" name="loginId" value="<%=um.getLoginId()%>" />
                <% } %>
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
                            <option value="<%=tm.getId()%>^<%=tm.getName().toLowerCase()%>" <%=(um.getFavoriteTeam() == tm.getId() ? "SELECTED" : "")%>><%=tm.getCity() + " " + tm.getName()%></option>
                        <%}%>
                    </select>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td class="smalllabel">* Required Fields</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td style="padding:1em"><input type="submit" value="Save My Changes" class="formbutton" /></td>
            </tr>
        </table>
    </form>
</div>

<jsp:include page="/jsp/std/field/page_footer.jsp"/>
