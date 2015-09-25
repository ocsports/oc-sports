<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.DraftPick" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    Collection draftPicks = null;
    DraftSQLController draftSqlCtrlr = new DraftSQLController();
    try {
        draftPicks = draftSqlCtrlr.getDraftPicks(0, 0);
    }
    catch(Exception e) {
        throw e;
    }
    finally {
        draftSqlCtrlr.closeConnection();
    }
    if( draftPicks != null && draftPicks.size() > 0 ) {
        String pickLine = "";
        Iterator iter = draftPicks.iterator();
        while( iter.hasNext() ) {
            DraftPick dp = (DraftPick)iter.next();
            pickLine = dp.pickKey + "," + dp.pickRound + "," + dp.teamKey + "," + dp.playerKey;
            out.print( pickLine + String.valueOf("<br/>") );
        }
    }
%>