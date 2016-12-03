<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.DraftPick" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    Collection draftPicks = null;
    DraftSQLController draftSqlCtrlr = null;
    try {
        draftSqlCtrlr = new DraftSQLController();
        draftPicks = draftSqlCtrlr.getDraftPicks(0, 0);
    }
    catch(Exception e) {
        //
    }
    finally {
        if (draftSqlCtrlr != null) draftSqlCtrlr.closeConnection();
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