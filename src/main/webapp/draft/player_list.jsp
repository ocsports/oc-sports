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

    Collection players = new ArrayList();
    DraftSQLController draftSqlCtrlr = new DraftSQLController();
    try {
        players = draftSqlCtrlr.getPlayers( pos, team, hideSelected, pname, 1 );
    }
    catch(Exception e) {
        throw e;
    }
    finally {
        draftSqlCtrlr.closeConnection();
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
                        <option value="C" style="font-size:10pt" <%=(pos.equals("C") ? "SELECTED" : "")%>>Catchers (C)</option>
                        <option value="1B" style="font-size:10pt" <%=(pos.equals("1B") ? "SELECTED" : "")%>>First Basemen (1B)</option>
                        <option value="2B" style="font-size:10pt" <%=(pos.equals("2B") ? "SELECTED" : "")%>>Second Basemen (2B)</option>
                        <option value="3B" style="font-size:10pt" <%=(pos.equals("3B") ? "SELECTED" : "")%>>Third Basemen (3B)</option>
                        <option value="SS" style="font-size:10pt" <%=(pos.equals("SS") ? "SELECTED" : "")%>>Shortstops (SS)</option>
                        <option value="OF" style="font-size:10pt" <%=(pos.equals("OF") ? "SELECTED" : "")%>>Outfielders (OF)</option>
                        <option value="UTIL" style="font-size:10pt" <%=(pos.equals("UTIL") ? "SELECTED" : "")%>>Utility (UTIL)</option>
                        <option value="SP" style="font-size:10pt" <%=(pos.equals("SP") ? "SELECTED" : "")%>>Starting Pitchers (SP)</option>
                        <option value="RP" style="font-size:10pt" <%=(pos.equals("RP") ? "SELECTED" : "")%>>Relief Pitchers (RP)</option>
                    </select>
                    <select name="teamAbrv" style="font-size:10pt; width:8em; margin-left:1em" onchange="document.frmPlayers.submit()">
                        <option value="ALL" style="font-size:10pt">&lt;all teams&gt;</option>
                        <option value="BAL" style="font-size:10pt" <%=(team.equals("BAL") ? "SELECTED" : "")%>>BAL</option>
                        <option value="BOS" style="font-size:10pt" <%=(team.equals("BOS") ? "SELECTED" : "")%>>BOS</option>
                        <option value="NYY" style="font-size:10pt" <%=(team.equals("NYY") ? "SELECTED" : "")%>>NYY</option>
                        <option value="TB" style="font-size:10pt" <%=(team.equals("TB") ? "SELECTED" : "")%>>TB</option>
                        <option value="TOR" style="font-size:10pt" <%=(team.equals("TOR") ? "SELECTED" : "")%>>TOR</option>
                        <option value="ATL" style="font-size:10pt" <%=(team.equals("ATL") ? "SELECTED" : "")%>>ATL</option>
                        <option value="FL" style="font-size:10pt" <%=(team.equals("FL") ? "SELECTED" : "")%>>FL</option>
                        <option value="NYM" style="font-size:10pt" <%=(team.equals("NYM") ? "SELECTED" : "")%>>NYM</option>
                        <option value="PHI" style="font-size:10pt" <%=(team.equals("PHI") ? "SELECTED" : "")%>>PHI</option>
                        <option value="WAS" style="font-size:10pt" <%=(team.equals("WAS") ? "SELECTED" : "")%>>WAS</option>
                        <option value="CHW" style="font-size:10pt" <%=(team.equals("CHW") ? "SELECTED" : "")%>>CHW</option>
                        <option value="CLE" style="font-size:10pt" <%=(team.equals("CLE") ? "SELECTED" : "")%>>CLE</option>
                        <option value="DET" style="font-size:10pt" <%=(team.equals("DET") ? "SELECTED" : "")%>>DET</option>
                        <option value="KC" style="font-size:10pt" <%=(team.equals("KC") ? "SELECTED" : "")%>>KC</option>
                        <option value="MIN" style="font-size:10pt" <%=(team.equals("MIN") ? "SELECTED" : "")%>>MIN</option>
                        <option value="CHC" style="font-size:10pt" <%=(team.equals("CHC") ? "SELECTED" : "")%>>CHC</option>
                        <option value="CIN" style="font-size:10pt" <%=(team.equals("CIN") ? "SELECTED" : "")%>>CIN</option>
                        <option value="HOU" style="font-size:10pt" <%=(team.equals("HOU") ? "SELECTED" : "")%>>HOU</option>
                        <option value="MIL" style="font-size:10pt" <%=(team.equals("MIL") ? "SELECTED" : "")%>>MIL</option>
                        <option value="PIT" style="font-size:10pt" <%=(team.equals("PIT") ? "SELECTED" : "")%>>PIT</option>
                        <option value="STL" style="font-size:10pt" <%=(team.equals("STL") ? "SELECTED" : "")%>>STL</option>
                        <option value="LAA" style="font-size:10pt" <%=(team.equals("LAA") ? "SELECTED" : "")%>>LAA</option>
                        <option value="OAK" style="font-size:10pt" <%=(team.equals("OAK") ? "SELECTED" : "")%>>OAK</option>
                        <option value="SEA" style="font-size:10pt" <%=(team.equals("SEA") ? "SELECTED" : "")%>>SEA</option>
                        <option value="TEX" style="font-size:10pt" <%=(team.equals("TEX") ? "SELECTED" : "")%>>TEX</option>
                        <option value="ARI" style="font-size:10pt" <%=(team.equals("ARI") ? "SELECTED" : "")%>>ARI</option>
                        <option value="COL" style="font-size:10pt" <%=(team.equals("COL") ? "SELECTED" : "")%>>COL</option>
                        <option value="LAD" style="font-size:10pt" <%=(team.equals("LAD") ? "SELECTED" : "")%>>LAD</option>
                        <option value="SD" style="font-size:10pt" <%=(team.equals("SD") ? "SELECTED" : "")%>>SD</option>
                        <option value="SF" style="font-size:10pt" <%=(team.equals("SF") ? "SELECTED" : "")%>>SF</option>
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
