<%@ page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.core.MyEmailer" %>
<%@ page import="com.ocsports.models.UserModel" %>
<%
    Collection users = (Collection)request.getAttribute("UserModels");
    Collection errors = (Collection)request.getAttribute("errors");
    int leagueId = Integer.parseInt( (String)request.getAttribute("leagueId") );
    
    String emailURL = response.encodeURL("goAdmin?r=sendEmail");
%>

<h2>Send Player Email</h2>
<div>
    <table cellspacing="0" cellpadding="5" border="0" class="trtable" style="padding:0; font-size:8pt; width:99%; border:1px solid darkgreen">
        <form name="frmList" action="<%=emailURL%>" method="POST">
            <input type="hidden" name="leagueId" value="<%=leagueId%>" />
            <tr class="row1">
                <td>Email BCC</td>
                <td>
                    <select name="userIds" size="8" multiple="true" style="font-size:10pt; width:30em">
                        <option value="-99" SELECTED>&lt;all users&gt;</option>
                        <%
                        if( users != null && !users.isEmpty() ) {
                            Iterator iter = users.iterator();
                            while( iter.hasNext() ) {
                                UserModel um = (UserModel)iter.next();
                                %>
                                <option value="<%=um.getUserId()%>"><%=um.getFullName()%> (<%=um.getLoginId()%>)</option>
                                <%
                            }
                        }
                        %>
                    </select>
                </td>
            </tr>
            <tr class="row1">
                <td>Content Type</td>
                <td>
                    <input type="radio" name="contentType" value="<%=MyEmailer.CONTENT_HTML%>" CHECKED style="margin-left:0">&nbsp;HTML
                    <input type="radio" name="contentType" value="<%=MyEmailer.CONTENT_PLAIN%>" style="margin-left:1.5em">&nbsp;Plain Text
                </td>
            </tr>
            <tr class="row1">
                <td>Subject</td>
                <td><input type="text" name="subject" maxlength="100" style="width:30em"/></td>
            </tr>
            <tr class="row1">
                <td colspan="2"><textarea name="msg" rows="20" style="width:99%"></textarea></td>
            </tr>
            <tr class="row1">
                <td colspan="2">
                    <input type="submit" value="Send Email" class="formbutton" />
                </td>
            </tr>
        </form>
    </table>
</div>
