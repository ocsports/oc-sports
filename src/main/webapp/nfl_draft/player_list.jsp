<%@page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.DraftPlayer" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    String pos = request.getParameter("playerPos");
    String team = request.getParameter("teamAbrv");
    String pname = request.getParameter("pname");

    String hideSelected = request.getParameter("hideSelected");
    if(pos != null && pos.length() > 0) {
        session.setAttribute("playerPos", pos);
        session.setAttribute("hideSelected", hideSelected);
    }
    else {
        pos = (String)session.getAttribute("playerPos");
        hideSelected = (String)session.getAttribute("hideSelected");
    }

    if(team != null && team.length() > 0) {
        session.setAttribute("teamAbrv", team);
    }
    else {
        team = (String)session.getAttribute("teamAbrv");
    }

    if(pname != null && pname.length() > 0) {
        session.setAttribute("playerName", pname);
    }
    else {
        pname = (String)session.getAttribute("playerName");
    }

    if( pos == null ) pos = "";
    if( pname == null ) pname = "";
    if( team == null ) team = "";
    if( hideSelected == null ) hideSelected = "";

    Collection players = null;
    DraftSQLController draftSqlCtrlr = null;
    try {
        draftSqlCtrlr = new DraftSQLController();
        players = draftSqlCtrlr.getPlayers( pos, team, hideSelected, pname, 1 );
    }
    catch(Exception e) {
    }
    finally {
        if (draftSqlCtrlr != null) draftSqlCtrlr.closeConnection();
        if( players == null ) players = new ArrayList();
    }
    String playerListURL = response.encodeURL(request.getContextPath() + "/draft/player_list.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Fantasy Baseball 2010</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/draft.css" type="text/css"></link>
</head>
<body style="background-color:#FFFFFF">
    <form name="frmPlayers" action="<%=playerListURL%>" method="GET">
        <table width="99%" cellpadding="1" cellspacing="0" class="league">
            <tr class="row2">
                <td colspan="5" style="text-align:left; padding-bottom:1em; border-right:0px">
                    <select name="playerPos" style="font-size:10pt; width:12em" onchange="document.frmPlayers.submit()">
                        <option value="ALL" style="font-size:10pt">&lt;all players&gt;</option>
                        <option value="QB" style="font-size:10pt" <%=(pos.equals("QB") ? "SELECTED" : "")%>>Quarterback (QB)</option>
                        <option value="RB" style="font-size:10pt" <%=(pos.equals("RB") ? "SELECTED" : "")%>>Running Back (RB)</option>
                        <option value="WR" style="font-size:10pt" <%=(pos.equals("WR") ? "SELECTED" : "")%>>Wide Receiver (WR)</option>
                        <option value="TE" style="font-size:10pt" <%=(pos.equals("TE") ? "SELECTED" : "")%>>Tight End (TE)</option>
                        <option value="K" style="font-size:10pt" <%=(pos.equals("K") ? "SELECTED" : "")%>>Kickers (K)</option>
                        <option value="DEF" style="font-size:10pt" <%=(pos.equals("DEF") ? "SELECTED" : "")%>>Defense (DEF)</option>
                    </select>
                    <select name="teamAbrv" style="font-size:10pt; width:8em; margin-left:1em" onchange="document.frmPlayers.submit()">
                        <option value="ALL" style="font-size:10pt">&lt;all teams&gt;</option>
                        <option value="ATL" style="font-size:10pt" <%=(team.equals("ATL") ? "SELECTED" : "")%>>ATL</option>
                        <option value="BAL" style="font-size:10pt" <%=(team.equals("BAL") ? "SELECTED" : "")%>>BAL</option>
                        <option value="CIN" style="font-size:10pt" <%=(team.equals("CIN") ? "SELECTED" : "")%>>CIN</option>
                        <option value="DEN" style="font-size:10pt" <%=(team.equals("DEN") ? "SELECTED" : "")%>>DEN</option>
                        <option value="GB" style="font-size:10pt" <%=(team.equals("GB") ? "SELECTED" : "")%>>GB</option>
                        <option value="HOU" style="font-size:10pt" <%=(team.equals("HOU") ? "SELECTED" : "")%>>HOU</option>
                        <option value="IND" style="font-size:10pt" <%=(team.equals("IND") ? "SELECTED" : "")%>>IND</option>
                        <option value="MIN" style="font-size:10pt" <%=(team.equals("MIN") ? "SELECTED" : "")%>>MIN</option>
                        <option value="NE" style="font-size:10pt" <%=(team.equals("NE") ? "SELECTED" : "")%>>NE</option>
                        <option value="SEA" style="font-size:10pt" <%=(team.equals("SEA") ? "SELECTED" : "")%>>SEA</option>
                        <option value="SF" style="font-size:10pt" <%=(team.equals("SF") ? "SELECTED" : "")%>>SF</option>
                        <option value="WAS" style="font-size:10pt" <%=(team.equals("WAS") ? "SELECTED" : "")%>>WAS</option>
                    </select>
                    <input type="text" name="pname" value="<%=pname%>" style="width:5em"/>
                    <br/>
                    <span style="padding-left:2em">
                        <input type="checkbox" name="hideSelected" value="Y" onclick="document.frmPlayers.submit()" <%=(hideSelected.equals("Y") ? "CHECKED" : "")%>>&nbsp;Hide selected
                    </span>
                </td>
            </tr>
            <tr class="hdr">
                <td style="text-align:left; width:3em">Rank</td>
                <td style="text-align:left; width:12em">&nbsp;Player</td>
                <td style="width:5em">Pos</td>
                <td style="width:5em">Team</td>
                <td>Draft</td>
            </tr>
            <%
            Iterator iter = players.iterator();
            while( iter.hasNext() ) {
                DraftPlayer dp = (DraftPlayer)iter.next();
                String trClass = "row2";
                String draftDisp = "&nbsp;";
                if( dp.pickKey > 0 ) {
                    trClass = "used";
                    draftDisp = dp.pickTeam + " (" + dp.pickRound + ")";
                }
                %>
                <tr class="<%=trClass%>">
                    <td style="text-align:left; width:3em"><%=dp.rank%>.</td>
                    <td style="text-align:left; width:12em"><%=dp.fname%> <%=dp.lname%></td>
                    <td style="width:5em"><%=dp.pos%></td>
                    <td style="width:5em"><%=dp.teamAbrv%></td>
                    <td><%=draftDisp%></td>
                </tr>
                <%
            }
            %>
        </table>
    </form>
</body>
</html>
