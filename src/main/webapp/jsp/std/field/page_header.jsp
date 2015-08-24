<%@page contentType="text/html"%>
<%@page import="com.ocsports.models.*"%>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    
    String homeURL = response.encodeURL("goUser?r=home");
    String picksURL = response.encodeURL("goPool?r=picks");
    String leaguePicksURL = response.encodeURL("goPool?r=leaguePicks");
    String standingsURL = response.encodeURL("goPool?r=standings");
    String statsURL = response.encodeURL("goStats");
    String forumsURL = response.encodeURL("goPool?r=forums");
    String profileURL = response.encodeURL("goUser?r=viewProfile");
    String logoutURL = response.encodeURL("goUser?r=logout");
%>
<!DOCTYPE HTML>
<html>
<head>
    <title>OC Sports</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/oc-sports.css" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/oc-sports.js"></script>
</head>

<body>
	<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">
		<tr class="header" valign="top">
			<td>
                <span class="headertext1">OC</span>
                <span class="headertext2">&nbsp;Sports</span>
            </td>
            <td align="right">
                <div class="topmenu">
					<a href="" id="divServerTime" title="Server Time" onclick="return false"></a>
                    <a href="<%=profileURL%>">Edit Profile</a>
                    <a href="<%=logoutURL%>">Logout</a>
                </div>
            </td>
		</tr>
		<tr>
            <td colspan="2" style="padding-left:0.5em; padding-top:0.25em; padding-bottom:1em">
				<div class="gamemenu">
                    <a href="<%=homeURL%>">Home</a>
                    <a href="<%=picksURL%>">Make Picks</a>
                    <a href="<%=leaguePicksURL%>">League Picks</a>
                    <a href="<%=standingsURL%>">Standings</a>
                    <a href="<%=statsURL%>">Team Statistics</a>
                    <a href="<%=forumsURL%>">Smack Talk</a>
				</div>
				<span style="font-size:10pt; margin-left:1em">- Welcome <%=loginUser.getFirstName()%></span>
            </td>
		</tr>
		<tr valign="top">
			<td colspan="2">
