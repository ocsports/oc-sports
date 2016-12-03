<%@page contentType="text/html"%>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    String chatMsg = request.getParameter("msg");
    String chatTeam = request.getParameter("screenName");
    if( chatMsg == null ) chatMsg = "";
    if( chatTeam == null ) chatTeam = "";
    
    DraftSQLController draftSqlCtrlr = null;
    try {
        draftSqlCtrlr = new DraftSQLController();
        if( chatMsg.length() > 0 || chatTeam.length() > 0 ) {
            draftSqlCtrlr.addChat(chatMsg, chatTeam);
        }
    }
    catch (Exception e) {
    }
    finally {
        if (draftSqlCtrlr != null) draftSqlCtrlr.closeConnection();
    }
    
    response.sendRedirect("draft2012.jsp");
%>
