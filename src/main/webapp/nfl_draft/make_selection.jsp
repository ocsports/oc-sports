<%@page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    int currPick = Integer.parseInt( request.getParameter("currPick") );
    int currTeam = Integer.parseInt( request.getParameter("currTeam") );
    int playerKey = Integer.parseInt( request.getParameter("playerKey") );
    String currTeamName = request.getParameter("currTeamName");
    
    DraftSQLController draftSqlCtrlr = new DraftSQLController();
    if( currPick > 0 && playerKey > 0 ) {
        draftSqlCtrlr.draftPlayer(currPick, playerKey);
    }
    response.sendRedirect("draft2012.jsp");
%>
