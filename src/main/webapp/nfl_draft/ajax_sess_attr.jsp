<%@page contentType="text/plain"%>
<%
    String attrName = request.getParameter("attrName");
    String attrValue = request.getParameter("attrValue");
    if(attrName != null && attrName.length() > 0 && attrValue != null) {
        session.setAttribute(attrName, attrValue);
    }
    out.print("SUCCESS");
%>
