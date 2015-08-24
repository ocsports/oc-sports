<%@page contentType="text/plain"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    
    DraftSQLController draftSqlCtrlr = new DraftSQLController();
    int currPick = draftSqlCtrlr.getCurrentPick();
    int lastChat = draftSqlCtrlr.getLastChat();
    out.print(currPick + "," + lastChat);
%>
