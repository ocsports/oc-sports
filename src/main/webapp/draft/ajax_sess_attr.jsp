<%@page contentType="text/plain"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    String attrName = request.getParameter("attrName");
    String attrValue = request.getParameter("attrValue");
    
    if(attrName != null && attrName.length() > 0 && attrValue != null) {
        session.setAttribute(attrName, attrValue);
    }
    out.print("SUCCESS");
%>
