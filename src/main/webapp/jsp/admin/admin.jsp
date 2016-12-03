<%@page import="com.ocsports.core.PropList"%>
<%@page import="com.ocsports.core.PropertiesHelper"%>
<%@page import="com.ocsports.servlets.TimerServlet"%>
<%@ page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.servlets.AdminServlet" %>
<%@ page import="com.ocsports.servlets.UserServlet" %>
<%
    String loginAttr = (String)session.getAttribute( AdminServlet.LOGIN_SESSION_ATTR );
    String detailPage = (String)request.getAttribute( AdminServlet.DETAIL_PAGE_ATTR );
    String loginId = (String)request.getAttribute("loginId");
    Collection loginErrors = (Collection)request.getAttribute("loginErrors");
    
    boolean loggedIn = ( loginAttr != null && loginAttr.length() > 0 );
    if( loginId == null ) loginId = "";
    
    String loginURL = response.encodeURL("goAdmin?r=login");
    String logoutURL = response.encodeURL("goAdmin?r=logout");
    String seasonsURL = response.encodeURL("goAdmin?r=seasons");
    String playersURL = response.encodeURL("goAdmin?r=players");
    String noticesURL = response.encodeURL("goAdmin?r=systemNotices");
    String teamsURL = response.encodeURL("goAdmin?r=teams");
    String timerURL = response.encodeURL("goTimer?r=timerStatus");
    String htmlURL = response.encodeURL("goAdmin?r=viewHtml");
    String auditLoginsURL = response.encodeURL("goAdmin?r=auditLogin");
    String sqlURL = response.encodeURL("goAdmin?r=executeSQL");
    String emailURL = response.encodeURL("goAdmin?r=email");
    String forumsURL = response.encodeURL("goAdmin?r=forums");
    String websiteURL = response.encodeURL("goUser?r=beginLogin");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>OC Sports Administration</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/oc-sports.css" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/oc-sports.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/admin.js"></script>
</head>

<body>
    <div style="width:100%">
        <table width="100%" cellspacing="0" cellpadding="0" border="0">
            <tr class="header" valign="top">
                <td>
                    <span class="headertext1">OC</span>
                    <span class="headertext2" style="padding-left:0.5em">Sports</span>
                    <span class="headertext2" style="padding-left:4em">- Administration</span>
                </td>
                <td align="right">
                    <div class="topmenu">
                        <a href="<%=websiteURL%>">Go To Website</a>
                        <a href="<%=logoutURL%>">Logout</a>
                    </div>
                </td>
            </tr>
        </table>
    <table width="100%" cellpadding="0" cellspacing="2" border="0" class="leftmenu">
            <tr>
                <td class="menu">
                    <a href="<%=seasonsURL%>">Seasons</a>
                    <br/><a href="<%=playersURL%>">Players</a>
                    <br/><a href="<%=noticesURL%>">System Notices</a>
                    <br/><a href="<%=teamsURL%>">Teams</a>
                    <br/><a href="<%=forumsURL%>">Forum Messages</a>
                    <br/><a href="<%=timerURL%>">Timers</a>
                    <br/><a href="<%=emailURL%>" style="margin-top:3em">Send Player Email</a>
                    <br/><a href="<%=htmlURL%>">Html Browser</a>
                    <br/><a href="<%=auditLoginsURL%>">Audit Logins</a>
                    <br/><a href="<%=sqlURL%>">Run SQL</a>
                </td>
                <td>
                    <%if(loggedIn && detailPage != null ) {%>
                        <jsp:include page="<%=detailPage%>" />
                    <%}%>
                    &nbsp;
                </td>
            </tr>
        </table>
    </div>
	<div id="divAdminLogin" class="msg">
        <h3>Administration</h3>
		<form name="frmAdminLogin" action="<%=loginURL%>" method="POST">
			<table cellspacing="0" cellpadding="5" border="0">
				<tr>
					<td>Password</td>
					<td><input type="password" name="adminPwd" style="width:15em; text-align:center"></td>
				</tr>
				<%
                if( loginErrors != null && !loginErrors.isEmpty() ) {
                    Iterator iter = loginErrors.iterator();
                    while( iter.hasNext() ) {
                        %>
                        <tr>
                            <td colspan="2" class="errortext"><%=(String)iter.next()%></td>
                        </tr>
                        <%
                    }
                }
                %>
				<tr>
					<td colspan="2" style="text-align:left; padding-top:0.5em; padding-bottom:1em">
						<input type="submit" name="btnLogin" value="Login" class="formbutton" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="divBlockScreen" class="blockScreen">&nbsp;</div>
    <%if( !loggedIn ) {%>
        <script language="javascript">showAdminLogin();</script>
    <%}%>
</body>
</html>
