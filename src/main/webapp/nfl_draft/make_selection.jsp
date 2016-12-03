<%@page contentType="text/html"%>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    int currPick = Integer.parseInt( request.getParameter("currPick") );
    int playerKey = Integer.parseInt( request.getParameter("playerKey") );
    
    DraftSQLController draftSqlCtrlr = null;
    try {
        draftSqlCtrlr = new DraftSQLController();
        if( currPick > 0 && playerKey > 0 ) {
            draftSqlCtrlr.draftPlayer(currPick, playerKey);
        }
    }
    catch (Exception e) {
    }
    finally {
        if (draftSqlCtrlr != null) draftSqlCtrlr.closeConnection();
    }
    response.sendRedirect("draft2012.jsp");
%>
