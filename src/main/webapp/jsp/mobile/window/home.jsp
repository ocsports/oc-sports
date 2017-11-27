<%@page contentType="text/html"%>
<%
    String loginError = (String)request.getAttribute("loginError");
    String loginId = (String)request.getAttribute("loginId");
    if(loginId == null) loginId = "";
%>
<jsp:include page="/jsp/mobile/window/page_header.jsp" />

<div id="divLogin" style="padding-top:1em; border-bottom:0px solid darkgreen">
    <form name="frmLogin" action="<%=request.getContextPath()%>/servlet/goUser?r=login" method="POST">
        <table cellspacing="0" cellpadding="3" border="0" class="trtable">
            <tr>
                <td>Login Id</td>
                <td><input type="text" name="loginId" value="<%=loginId%>" style="text-align:center; width:8em"></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" name="loginPwd" style="text-align:center; width:8em"></td>
            </tr>
            <%if(loginError != null && loginError.length() > 0) {%>
            <tr>
                <td colspan="2" class="errortext"><%=loginError%></td>
            </tr>
            <%}%>
            <tr>
                <td colspan="2" style="text-align:left; padding-top:0.5em; padding-bottom:1em">
                    <input type="submit" name="btnLogin" value="Login" class="formbutton" />
                </td>
            </tr>
        </table>
    </form>
</div>
<div style="margin-top:1em; border-bottom:0px solid darkgreen">
    OC Sports is dedicated to provide its participants an enhanced football experience while watching your favorite NFL teams from week to week,
    and perhaps root for those not-so-favorite teams you get stuck watching on cable or broadcast TV.
    <p>Read all about us by visiting our <a href="<%=request.getContextPath()%>/jsp/mobile/window/faqs.jsp">FAQs</a> page or read the <a href="<%=request.getContextPath()%>/jsp/mobile/window/rules.jsp">official rules</a>
</div>
<div id="divMessages" style="width:98%; margin-top:1em">
    <jsp:include page="/jsp/common/published_notices.jsp" />
</div>

<jsp:include page="/jsp/mobile/window/page_footer.jsp" />
