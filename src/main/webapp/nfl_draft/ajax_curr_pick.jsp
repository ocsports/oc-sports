<%@page contentType="text/plain"%>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    
    DraftSQLController draftSqlCtrlr = null;
    try {
        draftSqlCtrlr = new DraftSQLController();
        int currPick = draftSqlCtrlr.getCurrentPick();
        int lastChat = draftSqlCtrlr.getLastChat();
        out.print(currPick + "," + lastChat);
    }
    catch (Exception e) {
    }
    finally {
        if (draftSqlCtrlr != null) draftSqlCtrlr.closeConnection();
    }
%>
