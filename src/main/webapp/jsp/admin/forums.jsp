<%@ page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.servlets.AdminServlet" %>
<%@ page import="com.ocsports.models.ForumMessageModel" %>
<%
    int leagueId = Integer.parseInt( (String)request.getAttribute("leagueId") );
    int msgCount = Integer.parseInt( (String)request.getAttribute("msgCount") );
    Collection messages = (Collection)request.getAttribute( "ForumMessageModels" );
    
    SimpleDateFormat fmt = new SimpleDateFormat();
    Iterator iter = null;
    
    String forumsURL = response.encodeURL( AdminServlet.ALIAS + "?r=forums" );
    String saveMsgURL = response.encodeURL( AdminServlet.ALIAS + "?r=saveForumMsg" );
%>

<h2>Forum Messages (showing last <%=messages.size()%> posts)</h2>
<div>
    <table cellspacing="0" cellpadding="5" border="0" class="trtable" style="padding:0; font-size:8pt; width:100%">
        <form name="frmForums" action="<%=forumsURL%>" method="POST">
            <input type="hidden" name="leagueId" value="<%=leagueId%>" />
            <tr class="hdr">
                <td>Created</td>
                <td>Message</td>
            </tr>
            <%
            if(messages != null && !messages.isEmpty()) {
                fmt.applyPattern("MMM d, yyyy hh:mm a");
                String trClass = "";
                iter = messages.iterator();
                while( iter.hasNext() ) {
                    ForumMessageModel fmm = (ForumMessageModel)iter.next();
                    trClass = ( trClass.equals("row1") ? "row2" : "row1" );
                    %>
                    <tr class="<%=trClass%>" valign="top">
                        <td nowrap="Y"><a href="#" title="ID: <%=fmm.getId()%>" onclick="return showForumMsg('<%=fmm.getId()%>','<%=fmm.getText().replaceAll("'", "^")%>')"><%=fmt.format(new java.util.Date(fmm.getCreatedDate()))%></a><br/><i>by <%=fmm.getCreatedByName()%></i></td>
                        <td><%=fmm.getText()%></td>
                    </tr>
                    <%
                }
            }
            %>
        </form>
    </table>
</div>
<div id="divForumMsg" class="msg">
    <form name="frmForumMsg" action="<%=saveMsgURL%>" method="POST">
        <input type="hidden" name="leagueId" value="<%=leagueId%>" />
        <input type="hidden" name="msgId" value="-1" />
        <table cellspacing="0" cellpadding="2" border="0">
            <tr>
                <td><textarea rows="10" name="msg" style="width:40em"></textarea></td>
            </tr>
            <tr>
                <td style="text-align:left; padding-top:0.5em; padding-bottom:1em">
                    <input type="submit" name="btnSave" value="Save" onclick="hideDiv('divForumMsg')" class="formbutton" />
                    <input type="button" name="btnCancel" value="Cancel" onclick="hideDiv('divForumMsg')" class="formbutton" />
                </td>
            </tr>
        </table>
    </form>
</div>
