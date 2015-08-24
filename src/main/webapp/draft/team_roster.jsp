<%@page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.DraftTeamPos" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    String currTeam = request.getParameter("teamKey");
    if( currTeam == null || currTeam.length() == 0 ) {
        currTeam = (String)session.getAttribute("rosterTeam");
    }

    Collection teams = null;
    Collection teamPlayers = null;
    DraftSQLController draftSqlCtrlr = new DraftSQLController();
    try {
        teams = draftSqlCtrlr.getTeams();
        if( currTeam == null || currTeam.length() == 0 ) {
            currTeam = (String)((ArrayList)teams).get(0);
        }
        teamPlayers = draftSqlCtrlr.getPlayersByTeam(currTeam);
    }
    catch(Exception e) {
        throw e;
    }
    finally {
        draftSqlCtrlr.closeConnection();
    }
    session.setAttribute("rosterTeam", currTeam);
    
    String teamPlayersURL = response.encodeURL(request.getContextPath() + "/draft/team_roster.jsp");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Fantasy Baseball 2010</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/draft.css" type="text/css"></link>
</head>
<body style="background-color:#FFFFFF">
    <form name="frmTeam" action="<%=teamPlayersURL%>" method="GET">
        <table width="99%" cellpadding="1" cellspacing="0" class="league">
            <tr class="row2">
                <td colspan="5" style="text-align:left; padding-bottom:1em; border-right:0px">
                    <select name="teamKey" style="font-size:10pt; width:12em" onchange="document.frmTeam.submit()">
                        <%
                        Iterator iter = teams.iterator();
                        while( iter.hasNext() ) {
                            String teamName = (String)iter.next();
                            String sel = (teamName.equals(currTeam) ? "SELECTED" : "");
                            %>
                            <option value="<%=teamName%>" <%=sel%>><%=teamName%></option>
                            <%
                        }
                        %>
                </td>
            </tr>
            <tr class="hdr">
                <td style="text-align:left; width:5em">Pos</td>
                <td style="text-align:left">Player</td>
                <td style="width:5em">Team</td>
            </tr>
            <%
            iter = teamPlayers.iterator();
            while( iter.hasNext() ) {
                DraftTeamPos dpos = (DraftTeamPos)iter.next();
                String trStyle = "";
                if(dpos.playerName == null) {
                    dpos.playerName = "- none -";
                    dpos.playerTeam = "&nbsp;";
                }
                else {
                    trStyle = "font-weight:bold; color:darkblue";
                }
                %>
                <tr class="row2" style="<%=trStyle%>">
                    <td style="text-align:left; width:5em"><%=dpos.pos%></td>
                    <td style="text-align:left"><%=dpos.playerName%></td>
                    <td style="width:5em"><%=dpos.playerTeam%>&nbsp;</td>
                </tr>
                <%
            }
            %>
        </table>
    </form>
</body>
</html>
