<%@page contentType="text/html"%>
<%@page import="com.ocsports.core.PropertiesHelper"%>
<%@page import="com.ocsports.core.DateHelper"%>

<jsp:include page="/jsp/std/window/page_header.jsp" />

<h2>Welcome to OC Sports</h2>
<span class="windowmsg">
    OC Sports is dedicated to provide its participants an enhanced football experience while watching your favorite NFL teams from week to week,
    and perhaps root for those not-so-favorite teams you get stuck watching on cable or broadcast TV.
    <p>Read all about us by visiting our <a href="<%=request.getContextPath()%>/jsp/std/window/faqs.jsp">FAQs</a> page or read the <a href="<%=request.getContextPath()%>/jsp/std/window/rules.jsp">official rules</a>
</span>
<% if( PropertiesHelper.isShowSignup() ) { %>
    <div id="divCreateUser" style="margin-left:1em; margin-top:1em; padding:2px">
        <h2 style="color:#995533; font-size:18pt;"><%=DateHelper.getCurrentYear()%> Season is now posted. Sign-Up today!</h2>
        <a href="#" onclick="return showSignup()" style="margin-left:5em"><img src="<%=request.getContextPath()%>/images/signup_now.jpg" width="240" height="40" border="0" /></a>
        <br/><span style="color:#995533; font-size:10pt; padding-left:5em">** Please remember, <b>ALL PLAYERS</b> must sign up each season.  You cannot use your profile from last year</span>
    </div>
<% } %>
<div id="divMessages" style="width:98%; margin-left:1em; margin-top:1em; padding:2px">
    <jsp:include page="/jsp/common/published_notices.jsp" />
</div>

<jsp:include page="/jsp/std/window/page_footer.jsp" />
