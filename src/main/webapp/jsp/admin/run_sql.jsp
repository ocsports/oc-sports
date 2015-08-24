<%@ page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*"%>
<%@ page import="com.ocsports.models.*" %>
<%
    String query = (String)request.getAttribute("query");
    Collection messages = (Collection)request.getAttribute("messages");

    boolean hasResults = request.getAttribute("hasResults") != null;

    String executeURL = response.encodeURL("goAdmin?r=executeSQL");
%>

<table width='100%' cellspacing='0' cellpadding='5' border='0' class='raised'>
    <form name='frmSQL' action='<%=executeURL%>' method='POST'>
    <tr class='tabletitle'>
        <td>Run SQL Statement(s)</td>
    </tr>
    <tr class='rowdata2'>
        <td><textarea name='query' rows='6' cols='100'><%=(query == null ? "" : query)%></textarea></td>
    </tr>
    <tr class='rowdata2'>
        <td><input type='submit' name='submitBtn' value='Execute SQL'/></td>
    </tr>
    </form>
</table>

<%if(messages != null && !messages.isEmpty()) {%>
<img src="../images/spacer.gif" width='1' height='10'/>

<div style="overflow:auto; height:350; width:99%; border-style:inset; border-width:1;">
    <table width='98%' cellspacing='0' cellpadding='1' border='1'>
        <tr class='tabletitle'>
            <td>&nbsp;</td>
            <td width='100%'>DB Messages</td>
        </tr>
<%
        String tdClass = "";
        Iterator iter = messages.iterator();
        int i = 0;
        while(iter.hasNext()) {
            String msgText = (String)iter.next();
            tdClass = (tdClass.equals("rowdata1") ? "rowdata2" : "rowdata1");
%>
            <tr class='<%=tdClass%>'>
                <td><%=++i%>.</td>
                <td><%=msgText%></td>
            </tr>
<%
        }
%>
    </table>
</div>
<%}%>

<%if(hasResults) {%>
<img src="../images/spacer.gif" width='1' height='10'/>

<div style="overflow:auto; height:350; width:99%; border-style:inset; border-width:1;">
    <table cellspacing='0' cellpadding='1' border='1'>
<%
        int rowCount = Integer.parseInt( (String)request.getAttribute("rowCount") );
        int colCount = Integer.parseInt( (String)request.getAttribute("colCount") );
        String tdClass = "";
%>
        <tr class='tabletitle'>
        <%for(int h=1; h <= colCount; h++) {%>
            <td><%=(String)request.getAttribute("header_col" + h)%></td>
        <%}%>
        </tr>

<%
        for(int r=0; r < rowCount; r++) {
            tdClass = (tdClass.equals("rowdata1") ? "rowdata2" : "rowdata1");
%>
            <tr class='<%=tdClass%>' valign='top'>
<%
            for(int c=1; c <= colCount; c++) {
                String data = (String)request.getAttribute("data_row" + r + "_col" + c);
%>
                <td><%=(data == null ? "&lt;null&gt;" : (data.equals("") ? "&nbsp;" : data))%></td>
<%
            }
%>
            </tr>
<%
        }
%>
    </table>
</div>
<%}%>
