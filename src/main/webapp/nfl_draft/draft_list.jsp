<%@page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.DraftPlayer" %>
<%@ page import="com.ocsports.models.DraftPick" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    int currPickId = 0;
    Collection picks = null;
    DraftSQLController draftSqlCtrlr = null;
    try {
        draftSqlCtrlr = new DraftSQLController();
        picks = draftSqlCtrlr.getDraftPicks(0, 0);
    }
    catch(Exception e) {
    }
    finally {
        if (draftSqlCtrlr != null) draftSqlCtrlr.closeConnection();
        if( picks == null ) picks = new ArrayList();
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Fantasy Baseball 2010</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/draft.css" type="text/css"></link>
</head>
<body style="background-color:#FFFFFF">
    <table width="99%" cellpadding="0" cellspacing="0" class="trtable">
        <tr class="hdr">
            <td>&nbsp;</td>
            <td>Team</td>
            <td>Player Selected</td>
        </tr>
        <%
        Iterator iter = picks.iterator();
        while( iter.hasNext() ) {
            DraftPick dp = (DraftPick)iter.next();
            String playerDisp = "&nbsp;";
            String trStyle = "";
            if( dp.playerKey == 0 && currPickId == 0 ) {
                trStyle = "color:darkblue; background-color:#FFFF00; font-weight:bold";
                playerDisp = "- CURRENT SELECTION -";
                currPickId = dp.pickKey;
            }
            else if( dp.playerKey != 0 ) {
                playerDisp = dp.selectedPlayer.rank + ". " + dp.selectedPlayer.fname + " " + dp.selectedPlayer.lname + " (" + dp.selectedPlayer.teamAbrv + " - " + dp.selectedPlayer.pos + ")";
                trStyle = "color:red; font-weight:bold";
            }
            %>
            <tr style="height:20px; <%=trStyle%>">
                <td><%=dp.pickKey%>.<a id="draftPickId<%=dp.pickKey%>" name="#pickId<%=dp.pickKey%>"/></td>
                <td><%=dp.pickTeam%></td>
                <td><%=playerDisp%></td>
            </tr>
            <%
        }
        %>
    </table>
</body>
<script language="javascript">
    var nScroll = Math.floor((<%=currPickId%>+1)*20) - 160;
    if( nScroll > 0 ) window.scrollTo(0, nScroll);
</script>
</html>
