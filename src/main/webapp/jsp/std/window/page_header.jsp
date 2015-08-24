<%@page contentType="text/html"%>
<%@page import="com.ocsports.core.PropertiesHelper"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>OC Sports</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/oc-sports.css" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/oc-sports.js"></script>
</head>

<body>
	<table width="100%" height="100%" cellspacing="0" cellpadding="4" border="0">
		<tr valign="top" class="header">
			<td colspan="2">
				<span class="headertext1">OC</span>
				<span class="headertext2">&nbsp;Sports</span>
			</td>
		</tr>
		<tr valign="top">
			<td style="border-right:1px solid #CCD7B7">
				<div id="windowmenu" name="windowmenu" style="width:13em">
					<ul>
						<li><a href="<%=request.getContextPath()%>/jsp/std/window/home.jsp">Home</a></li>
						<li><a href="<%=request.getContextPath()%>/jsp/std/window/faqs.jsp">FAQs</a></li>
						<li><a href="<%=request.getContextPath()%>/jsp/std/window/rules.jsp">League Rules</a></li>
						<li><a href="<%=request.getContextPath()%>/jsp/std/window/winners.jsp">Winners Circle</a></li>
						<li><a href="<%=request.getContextPath()%>/jsp/std/window/contact.jsp">Contact Administrator</a></li>
						<li><a href="<%=request.getContextPath()%>/jsp/std/window/forgot_pwd.jsp">Forgot Password</a></li>
						<li style="margin-top:1em"><a href="javascript:void(0)" onclick="showLogin()">Login</a></li>
					</ul>
				</div>
				<div id="divImg" style="margin-top:1em; width:13em; text-align:center">
                    <% if( PropertiesHelper.isShowSignup() ) { %>
                        <a href="#" onclick="return showSignup()"><img src="<%=request.getContextPath()%>/images/oc-sports/silhouette_fb.jpg" width="140" height="180" border="0" /></a>
                    <% } else {%>
                        <a href="javascript:void(0)" onclick="showLogin()"><img src="<%=request.getContextPath()%>/images/oc-sports/silhouette_fb.jpg" width="140" height="180" border="0" /></a>
                    <% } %>
				</div>
			</td>
			<td width="100%">
