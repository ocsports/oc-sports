<%@ page contentType="text/html"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.ocsports.servlets.AdminServlet" %>
<%@ page import="com.ocsports.models.*" %>
<%
    int leagueId = Integer.parseInt( (String)request.getAttribute("leagueId") );
    int userId = Integer.parseInt( (String)request.getAttribute("userId") );

    String timestamp = (String)request.getAttribute("timestamp");
    if(timestamp == null) timestamp = "";

    Collection leagues = (Collection)request.getAttribute("LeagueModels");
    Collection users  = (Collection)request.getAttribute("UserModels");
    Collection logins = (Collection)request.getAttribute("AuditLoginModels");

    SimpleDateFormat fmt = new SimpleDateFormat();

    String viewURL = response.encodeURL("goAdmin?r=auditLogin");
%>

<table width='100%' cellspacing='0' cellpadding='5' border='0' class='raised'>
    <form name='frmLeague' action='<%=viewURL%>' method='POST'>
    <input type='hidden' name='changeLeague' value=''/>
    <tr class='tabletitle'>
        <td colspan='2'>Audit Login Attempts</td>
    </tr>
    <tr class='rowdata2'>
        <td width='50px' class='label'>League</td>
        <td>
            <select name='leagueId' class='wide' onChange='document.frmLeague.changeLeague.value="Y"; document.frmLeague.submit();'>
                <option value='-1'>&nbsp;&nbsp;&nbsp;</option>
<%
                if(leagues != null && !leagues.isEmpty()) {
                    Iterator iter = leagues.iterator();
                    while(iter.hasNext()) {
                        LeagueModel lm = (LeagueModel)iter.next();
%>
                        <option value='<%=lm.getId()%>' <%=(lm.getId() == leagueId ? "SELECTED" : "")%>><%="(" + lm.getId() + ") " + lm.getName()%></option>
<%
                    }
                }
%>
            </select>
        </td>
    </tr>
    <%if(leagueId > 0) {%>
    <tr class='rowdata2'>
        <td width='50px' class='label'>User</td>
        <td>
            <select name='userId' class='wide' onChange='document.frmLeague.submit();'>
                <option value='-1'>&nbsp;&nbsp;&nbsp;</option>
<%
                if(users != null && !users.isEmpty()) {
                    Iterator iter = users.iterator();
                    while(iter.hasNext()) {
                        UserModel um = (UserModel)iter.next();
%>
                        <option value='<%=um.getUserId()%>' <%=(um.getUserId() == userId ? "SELECTED" : "")%>><%="(" + um.getUserId() + ") " + um.getFullName()%></option>
<%
                    }
                }
%>
            </select>
        </td>
    </tr>
    <%}%>
    <tr class='rowdata2'>
        <td width='50px' class='label'>Since</td>
        <td>
            <input type='text' name='timestamp' size='16' maxlength='16' value='<%=timestamp%>'/>
            &nbsp;
            <i>{ MM/dd/yyyy HH:mm }</i>
            &nbsp;
            <input type='submit' name='submitBtn' value='Go' class='button'/>
        </td>
    </tr>
    </form>
</table>

<%if(logins != null && !logins.isEmpty()) {%>
<img src="../images/spacer.gif" width='1' height='10'/>

<div style="overflow:auto; height:450; width:99%; border-style:inset; border-width:0;">
<table width='400' cellspacing='0' cellpadding='1' border='1'>
    <tr class='tabletitle'>
        <td nowrap=''>Date/Time</td>
        <td nowrap=''>Login Id</td>
        <td nowrap=''>Status (User Id)</td>
    </tr>
<%
    String tdClass = "";

    Iterator iter3 = logins.iterator();
    while(iter3.hasNext()) {
        AuditLoginModel alm = (AuditLoginModel)iter3.next();
        tdClass = (tdClass.equals("rowdata1") ? "rowdata2" : "rowdata1");
        boolean isValid = alm.getUserId() > 0;
%>
        <tr class='<%=tdClass%>'>
            <td nowrap=''><%=fmt.format(alm.getTimestamp())%></td>
            <td nowrap=''><%=alm.getLoginId()%></td>
            <td nowrap=''><span style='color:<%=(isValid ? "darkgreen" : "red")%>;'><%=(isValid ? "Valid (" + alm.getUserId() + ")" : "INVALID")%></span></td>
        </tr>
<%
    }
%>
</table>
</div>
<%}%>