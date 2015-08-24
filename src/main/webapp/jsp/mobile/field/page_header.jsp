<%@page contentType="text/html"%>
<%@page import="com.ocsports.models.UserModel"%>
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
    <title>OC Sports - Mobile</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/mobile.css" type="text/css" />
</head>

<body>
	<table cellspacing="0" cellpadding="4" border="0" style="width:40em">
		<tr class="header">
			<td>
                <span class="headertext1">OC</span>
                <span class="headertext2">&nbsp;Sports</span>
            </td>
		</tr>
		<tr>
			<td style="border-bottom:1px solid darkgreen">
                <a href="<%=homeURL%>">Home</a>
                <br/><a href="<%=picksURL%>">Make Picks</a>
                <br/><a href="<%=leaguePicksURL%>">League Picks</a>
                <br/><a href="<%=standingsURL%>">Standings</a>
                <br/><a href="<%=statsURL%>">Team Statistics</a>
                <br/><a href="<%=forumsURL%>">Smack Talk</a>
                <br/><br/><a href="<%=profileURL%>">Edit Profile</a>
                <br/><a href="<%=logoutURL%>">Logout</a>
            </td>
		</tr>
		<tr>
			<td>
