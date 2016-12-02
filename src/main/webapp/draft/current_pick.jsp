<%@page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.DraftPick" %>
<%@ page import="com.ocsports.models.DraftPlayer" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    int currPick = 0;
    int currTeam = 0;
    String currTeamName = "";
    StringBuffer selectedPlayerIds = new StringBuffer();
    
    DraftSQLController draftSqlCtrlr = null;
    try {
        draftSqlCtrlr = new DraftSQLController();
        Collection picks = draftSqlCtrlr.getDraftPicks(0, 0);
        Iterator iter = picks.iterator();
        while( iter.hasNext() ) {
            DraftPick dp = (DraftPick)iter.next();
            if( dp.playerKey == 0 ) {
                currPick = dp.pickKey;
                currTeam = dp.teamKey;
                currTeamName = dp.pickTeam;
                break;
            }
            else {
                selectedPlayerIds.append( dp.playerKey + "," );
            }
        }
    }
    catch(Exception e) {
        //
    }
    finally {
        draftSqlCtrlr.closeConnection();
    }
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Fantasy Baseball 2010</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/draft.css" type="text/css"></link>
</head>
<body style="background-color:#FFFFFF">
    <table width="99%" cellpadding="3" cellspacing="0">
        <form>
        <tr valign="middle">
            <td align="center">
                <h4><u>Current Selection</u></h4>
                <h2><%=currPick%>. <%=currTeamName%></h2>
                <input type="button" name="btnMakePick" value="Make Selection" class="yellowbtn" onclick="parent.showDraftSelection('<%=currPick%>','<%=currTeamName%>',playersTaken);"/>
            </td>
        </tr>
        </form>
    </table>
</body>
<script language="javascript">
    var playersTaken = "<%=selectedPlayerIds.toString()%>";
</script>
</html>
