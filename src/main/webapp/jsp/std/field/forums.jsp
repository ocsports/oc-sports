<%@page contentType="text/html"%>
<%@page import="java.util.*,java.text.*"%>
<%@page import="com.ocsports.models.ForumMessageModel"%>

<%
    Collection messages = (Collection)request.getAttribute("ForumMessageModels");
    String msgText = (String)request.getAttribute("msgText");
    String msgError = (String)request.getAttribute("msgError");
    long msgMaxLength = 2000;

    if( msgText == null ) msgText = "";
    if( msgError == null ) msgError = "";
    
    Iterator iter = null;
    SimpleDateFormat fmt = new SimpleDateFormat();
    
    String addMsgURL = response.encodeURL("goPool?r=addForumMessage");
    String pageTitle = "Smack Talk";
%>
<jsp:include page="/jsp/std/field/page_header.jsp" />

<h2><%=pageTitle%></h2>
<div style="width:98%; padding-left:0.5em; padding-top:0.5em">
    <table cellspacing="0" cellpadding="5" border="0" class="trtable" style="width:100%">
        <form name="frm1">
        <tr class="hdr">
            <td style="width:15em"><input type="button" name="btnAdd" value="Add Message" class="formbutton" onclick="return showForum()" /></td>
            <td style="text-align:left">&nbsp;</td>
        </tr>
    <%
    fmt.applyPattern( "MMM d, yyyy h:mma (z)" );
    String trClass = "";
    if( messages != null && !messages.isEmpty() ) {
        iter = messages.iterator();
        while( iter.hasNext() ) {
            ForumMessageModel fmm = (ForumMessageModel)iter.next();
            trClass = (trClass.equals("row1") ? "row2" : "row1");
            %>
            <tr class="<%=trClass%>">
                <td style="width:15em; font-size:8pt">
                    <strong><%=fmm.getCreatedByName()%></strong>
                    <br/>
                    <%=fmt.format(new java.util.Date(fmm.getCreatedDate()))%>
                </td>
                <td style="text-align:left"><%=fmm.getText()%></td>
            </tr>
            <%
        }
    } else {
        %>
        <tr class="row2" style="height:75px; vertical-align:middle">
            <td colspan="2" style="font-size:12pt; font-weight:bold; text-align:center">No messages have been posted</td>
        </tr>
        <%
    }
    %>
        </form>
    </table>
</div>

<div id="divAddForum" class="msg">
    <form name="frmMsg" action="<%=addMsgURL%>" method="POST">
        <table cellspacing="0" cellpadding="0" border="0">
            <tr>
                <td>Enter your message:</td>
            </tr>
            <tr>
                <td style="font-size:8pt; color:#AAAAAA">
                *** Note: please do not use profanity, racial or sexual content, or any comments
                <br/>which may 'seriously' offend others. Forums are monitored and offensive
                <br/>messages will be removed.</td>
            </tr>
            <%if( msgError.length() > 0 ) {%>
            <tr>
                <td class="errortext"><%=msgError%></td>
            </tr>
            <%}%>
            <tr>
                <td>
                    <textarea name="msg" rows="5" cols="60" onkeyup="forumMsgChanged('<%=msgMaxLength%>')" onkeydown="forumMsgChanged('<%=msgMaxLength%>')"><%=msgText%></textarea>
                    <br/><input type="text" name="msgChars" value="<%=msgMaxLength%>" readonly="true" style="read-only:true; width:3em; background-color:#CCD7B7; color:red; font-weight:bold; font-size:8pt; border:0; text-align:right"/>
                    <span style="font-size:8pt"> characters remaining (<%=msgMaxLength%> max)</span>
                </td>
            </tr>
            <tr>
                <td style="text-align:left; padding-top:0.5em; padding-bottom:1em">
                    <input type="submit" name="btnSave" value="Smack Now!" onclick="hideDiv('divAddForum')" class="formbutton" />
                    <input type="button" name="btnCancel" value="Cancel" onclick="hideDiv('divAddForum')" class="formbutton" />
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="divBlockScreen" class="blockScreen">&nbsp;</div>

<script language="javascript">forumMsgChanged('<%=msgMaxLength%>');</script>
<% if( msgError.length() > 0 ) { %>
    <script language="javascript">showForum();</script>
<% } %>
<!-- PAGE FOOTER -->
<jsp:include page="/jsp/std/field/page_footer.jsp"/>