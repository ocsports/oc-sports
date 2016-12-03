<%@ page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*"%>
<%@ page import="com.ocsports.servlets.AdminServlet" %>
<%@ page import="com.ocsports.models.*" %>
<%
    String url = (String)request.getAttribute("url");
    String source = (String)request.getAttribute("source");

    String formURL = response.encodeURL("goAdmin?r=viewHtml");
%>

<form name='frmHTML' action='<%=formURL%>' method='POST'>
	<table width='100%' cellspacing='0' cellpadding='5' border='0' class='raised'>
		<tr class='tabletitle'>
			<td colspan='2'>View HTML</td>
		</tr>
		<tr class='rowdata2'>
			<td>URL</td>
			<td><input type='text' name='url' size='50' value='<%=(url == null ? "" : url)%>'/></td>
		</tr>
		<tr class='rowdata2'>
			<td colspan='2'><input type='submit' name='submitBtn' value='Run'/></td>
		</tr>
	</table>
</form>

<%if(source != null) {%>
<img src="../images/spacer.gif" width='1' height='10'/>

<div>
	<textarea name="sourceText" rows="20" cols="150">
		<%=source%>
	</textarea>
</div>
<%}%>
