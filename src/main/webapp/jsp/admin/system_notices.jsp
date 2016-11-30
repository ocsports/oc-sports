<%@ page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.SystemNoticeModel" %>
<%@ page import="com.ocsports.servlets.AdminServlet" %>
<%
    Collection notices = (Collection)request.getAttribute("SystemNoticeModels");
    Collection errors = (Collection)request.getAttribute("errors");
    if(notices == null) notices = new ArrayList();
    
    SimpleDateFormat fmt = new SimpleDateFormat();
    Iterator iter = null;
    int counter = 0;
    
    String saveNoticeURL = response.encodeURL("goAdmin?r=saveSystemNotice");
    String deleteNoticeURL = response.encodeURL("goAdmin?r=deleteSystemNotice");
%>

<h2>System Notices <a href="" title="Create New Notice" style="padding-left: 0.5em; font-size: 10pt; font-weight: bold" onclick="return createSysNotice()">Create New Notice</a></h2>
<div>
    <table cellspacing="0" cellpadding="5" border="0" class="trtable" style="padding:0; font-size:8pt; width:100%">
        <tr class="hdr">
            <td>Address</td>
            <td style="width:12em">Last Updated</td>
            <td style="width:2em; text-align:center">Published</td>
        </tr>
        <%
        if( notices != null && !notices.isEmpty() ) {
            fmt.applyPattern("MMM d, yyyy hh:mm a");
            String trClass = "";
            counter = 0;
            iter = notices.iterator();
            while( iter.hasNext() ) {
                SystemNoticeModel snm = (SystemNoticeModel)iter.next();
                trClass = (trClass.equals("row1") ? "row2" : "row1");
                %>
                <tr class="<%=trClass%>">
                    <td><%=snm.getMessage()%></td>
                    <td style="width:12em"><a href="#" title="ID: <%=snm.getId()%>" onclick="return showSysNotice('<%=snm.getId()%>', '<%=counter++%>', '<%=(snm.isPublished() ? 1 : 0)%>')"><%=fmt.format( new java.util.Date(snm.getUpdated()) )%></a></td>
                    <td style="width:2em; text-align:center"><%=( snm.isPublished() ? "Yes" : "No" )%></td>
                </tr>
                <%
            }
        }
        %>
    </table>
</div>
<div id="divSysNotice" class="msg">
    <form name="frmSysNotice" action="<%=saveNoticeURL%>" method="POST">
        <input type="hidden" name="noticeId" value="-1" />
        <table cellspacing="0" cellpadding="2" border="0">
            <tr valign="top">
                <td><textarea rows="10" name="msg" style="width:40em"></textarea></td>
            </tr>
            <tr>
                <td><input type="checkbox" name="pub" value="Y">&nbsp;Published</td>
            </tr>
            <%
            if( errors != null && !errors.isEmpty() ) {
                String errMsg = "";
                iter = errors.iterator();
                while( iter.hasNext() ) {
                    errMsg += (String)iter.next() + "<br/>";
                }
                %>
                <tr>
                    <td class="errortext"><%=errMsg%></td>
                </tr>
                <%
            }
            %>
            <tr>
                <td>
                    <input type="submit" value="Save" onclick="hideDiv('divSysNotice')" class="formbutton" />
                    <input type="button" value="Cancel" onclick="hideDiv('divSysNotice')" class="formbutton" />
                    <input type="button" value="Delete" onclick="deleteSysNotice()" class="formbutton2" />
                </td>
            </tr>
        </table>
    </form>
</div>
<script language="javascript">
    var deleteNoticeURL = "<%=deleteNoticeURL%>";
    <%
    if( notices.size() > 0 ) {
        %>
        aMsgs = new Array(<%=notices.size()%>);
        <%
        counter = 0;
        iter = notices.iterator();
        String s = System.getProperty( "line.separator" );
        while( iter.hasNext() ) {
            SystemNoticeModel snm = (SystemNoticeModel)iter.next();
            String msg = snm.getMessage();
            msg = msg.replaceAll("\"", "'");
            msg = msg.replaceAll(s, "|");
            %>
            aMsgs[<%=counter++%>] = "<%=msg%>";
            <%
        }
    }
    %>
</script>
