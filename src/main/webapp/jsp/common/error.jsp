<%@ page contentType="text/html" %>
<%@ page import="com.ocsports.core.ProcessException" %>
<%
	ProcessException pe = (ProcessException)request.getAttribute("exception");
    String loginURL = response.encodeURL("goUser");
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>OC Sports - Error Page</title>
	<style type="text/css">
		A.login { text-decoration:none; font-size:10pt; color:maroon; font-weight:bold; }
		BODY { background-color:#FFFFDD; color:maroon; font-size:10pt; font-weight:normal; margin-left:1; margin-right:1; margin-top:1; margin-bottom:1; }
		TR { vertical-align:top; }
	</style>
</head>

<body>
	<h2>A Fatal error has occurred on the site</h2>
	<p>The site administrator has been sent a detailed description of this error.</p>
	<p>Error Details:<br/><%=pe.getExceptionType()%> - <%=pe.getMessage()%></p>
	<p>
		You will be re-directed back to the home page shortly, or you can click on the link below.
		<br/><br/><a class="login" href="<%=loginURL%>">Continue to home page</a>
	</p>
</body>
</html>
