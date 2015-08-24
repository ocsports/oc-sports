<%@page import="com.ocsports.core.PropertiesHelper"%>
<%@page contentType="text/html"%>
<%@page import="java.util.*"%>
<%@page import="com.ocsports.servlets.AdminServlet"%>
<%@page import="com.ocsports.sql.UserSQLController"%>
<%@page import="com.ocsports.models.SystemNoticeModel"%>
<%
    UserSQLController userSql = new UserSQLController();
    Collection notices = userSql.getSystemNotices(-1, true);
%>

<jsp:include page="/jsp/std/window/page_header.jsp" />

<h2>Welcome to OC Sports</h2>
<span class="windowmsg">
    OC Sports is dedicated to provide its participants an enhanced football experience while watching your favorite NFL teams from week to week,
    and perhaps root for those not-so-favorite teams you get stuck watching on cable or broadcast TV.
    <p>Read all about us by visiting our <a href="<%=request.getContextPath()%>/jsp/std/window/faqs.jsp">FAQs</a> page or read the <a href="<%=request.getContextPath()%>/jsp/std/window/rules.jsp">official rules</a>
</span>
<% if( PropertiesHelper.isShowSignup() ) { %>
    <div id="divCreateUser" style="margin-left:1em; margin-top:1em; padding:2px">
        <h2 style="color:#995533; font-size:18pt;">2015 Season is now posted. Sign-Up today!</h2>
        <a href="#" onclick="return showSignup()" style="margin-left:5em"><img src="<%=request.getContextPath()%>/images/signup_now.jpg" width="240" height="40" border="0" /></a>
        <br/><span style="color:#995533; font-size:10pt; padding-left:5em">** Please remember, <b>ALL PLAYERS</b> must sign up each season.  You cannot use your profile from last year</span>
    </div>
<% } %>
<div id="divMessages" style="width:98%; margin-left:1em; margin-top:1em; padding:2px">
    <h4>Recent News / League Information</h4>
    <div id="msglist" style="width:100%; overflow:auto">
        <ul>
            <%if(notices == null || notices.size() == 0) {%>
                <li>No messages</li>
            <%} else { %>
                <%
                Iterator iter = notices.iterator();
                while( iter.hasNext() ) {
                    SystemNoticeModel snm = (SystemNoticeModel)iter.next();
                    %>
                    <li><%=snm.getMessage()%></li>
                    <%
                }
                %>
            <%}%>
        </ul>
    </div>
</div>

<jsp:include page="/jsp/std/window/page_footer.jsp" />
