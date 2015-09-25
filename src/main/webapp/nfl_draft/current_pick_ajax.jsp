<%@ page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.DraftPick" %>
<%@ page import="com.ocsports.models.DraftPlayer" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    int currPick = 0;
    DraftSQLController draftSqlCtrlr = new DraftSQLController();
    try {
        Collection picks = draftSqlCtrlr.getDraftPicks(0, 0);
        Iterator iter = picks.iterator();
        while( iter.hasNext() ) {
            DraftPick dp = (DraftPick)iter.next();
            if( dp.playerKey == 0 ) {
                currPick = dp.pickKey;
                break;
            }
        }
    }
    catch(Exception e) {
        throw e;
    }
    finally {
        draftSqlCtrlr.closeConnection();
    }
    out.print(currPick);
%>