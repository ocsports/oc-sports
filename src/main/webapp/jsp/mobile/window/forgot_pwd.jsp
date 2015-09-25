<%@page contentType="text/html"%>
<%
    String emailAddr = (String)request.getAttribute("email");
    if(emailAddr == null) emailAddr = "";
    String emailMsg = (String)request.getAttribute("emailMsg");
%>

<jsp:include page="/jsp/mobile/window/page_header.jsp" />

<h2>Forget Your Password ?</h2>
<div>
    Have you forgotten your password? It is quite difficult trying to remember 100's of passwords for all sorts of websites, home and office workstations, mobile devices, online banking, etc... 
    Fortunately we have your email address and can quickly send your password for OC Sports.
    Just enter your registered email address below and you'll be back making picks in no time!
</div>

<div id="divMessages" style="width:98%; margin-top:2em; padding:2px; border:0px solid #26370A">
    <h4 style="padding-left:1em">Enter your email address:</h4>
    <form name="frmEmail" action="<%=request.getContextPath()%>/servlet/goUser?r=forgotPwd" method="POST">
        <table cellspacing="0" cellpadding="0" border="0" class="trtable">
            <tr style="height:30px">
                <td><input type="text" name="email" value="<%=emailAddr%>" style="width:20em" /></td>
            </tr>
            <tr style="height:40px">
                <td><input type="submit" name="btnSave" value="Send My Password" class="formbutton" /></td>
            </tr>
            <%if(emailMsg != null && emailMsg.length() > 0) {%>
            <tr style="height:40px">
                <td class="errortext"><%=emailMsg%></td>
            </tr>
            <%}%>
        </table>
    </form>
</div>

<jsp:include page="/jsp/mobile/window/page_footer.jsp" />
