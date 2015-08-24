<%@page contentType="text/html"%>
<%@page import="com.ocsports.core.PropertiesHelper"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>OC Sports - Mobile</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/mobile.css" type="text/css" />
</head>

<body>
	<table cellspacing="0" cellpadding="3" border="0" style="width:30em">
		<tr class="header">
			<td>
				<span class="headertext1">OC</span>
				<span class="headertext2" style="padding-left:0.5em">Sports</span>
			</td>
		</tr>
		<tr>
			<td style="border-bottom:1px solid darkgreen">
                <a href="<%=request.getContextPath()%>/jsp/mobile/window/home.jsp">Home</a>
                <br/><a href="<%=request.getContextPath()%>/jsp/mobile/window/faqs.jsp">FAQs</a>
                <br/><a href="<%=request.getContextPath()%>/jsp/mobile/window/rules.jsp">League Rules</a>
                <br/><a href="<%=request.getContextPath()%>/jsp/mobile/window/winners.jsp">Winners Circle</a>
                <br/><a href="<%=request.getContextPath()%>/jsp/mobile/window/contact.jsp">Contact Administrator</a>
                <br/><a href="<%=request.getContextPath()%>/jsp/mobile/window/forgot_pwd.jsp">Forgot Password</a>
				<% if( PropertiesHelper.isShowSignup() ) { %>
                <br/><br/><a href="<%=request.getContextPath()%>/jsp/mobile/window/signup.jsp">Signup Now!</a>
                <% } %>
			</td>
		</tr>
		<tr>
			<td>
