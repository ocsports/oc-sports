<%@ page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.LeagueModel" %>
<%@ page import="com.ocsports.models.UserModel" %>
<%@ page import="com.ocsports.servlets.AdminServlet" %>
<%@ page import="com.ocsports.sql.UserSQLController" %>
<%
    int leagueId = Integer.parseInt( (String)request.getAttribute("leagueId") );
    int orderBy = Integer.parseInt( (String)request.getAttribute("orderBy") );
    Collection errors = (Collection)request.getAttribute("errors");
    Collection players = (Collection)request.getAttribute("UserModels");
    if( players == null) players = new ArrayList();
    
    Iterator iter = null;
    int counter = 0;
    
    String playersURL = response.encodeURL( AdminServlet.ALIAS + "?r=players" );
    String savePlayerURL = response.encodeURL( AdminServlet.ALIAS + "?r=savePlayer" );
    String savePaymentURL = response.encodeURL( AdminServlet.ALIAS + "?r=savePlayerPayment" );
    String deletePlayerURL = response.encodeURL( AdminServlet.ALIAS + "?r=deletePlayer" );
%>

<h2>Player List (<%=players.size()%> players)</h2>
<div>
    <table cellspacing="0" cellpadding="5" border="0" class="trtable" style="padding:0; font-size:8pt; width:100%">
        <form name="frmPlayersList" action="<%=playersURL%>" method="POST">
            <input type="hidden" name="leagueId" value="<%=leagueId%>" />
            <input type="hidden" name="orderBy" value="<%=orderBy%>" />
            <tr class="hdr">
                <td nowrap="true"><a href="#" onclick="return sortPlayerList('<%=UserSQLController.USERS_ORDER_BY_NAME%>')">Name</a></td>
                <td>Address</td>
                <td><a href="#" onclick="return sortPlayerList('<%=UserSQLController.USERS_ORDER_BY_EMAIL%>')">Email Address(s)</a></td>
                <td><a href="#" onclick="return sortPlayerList('<%=UserSQLController.USERS_ORDER_BY_LOGIN%>')">Login Id</a></td>
                <td style="text-align:center"><a href="#" onclick="return sortPlayerList('<%=UserSQLController.USERS_ORDER_BY_STATUS%>')">Status</a></td>
                <td style="text-align:center"><a href="#" onclick="return sortPlayerList('<%=UserSQLController.USERS_ORDER_BY_PAID%>')">Paid</a></td>
            </tr>
            <%
            iter = players.iterator();
            counter = 0;
            String trClass = "";
            String emailDisplay = "";
            while( iter.hasNext() ) {
                UserModel um = (UserModel)iter.next();
                emailDisplay = (um.getEmail() == null ? "&nbsp;" : um.getEmail());
                if(um.getEmail2() != null && !um.getEmail2().equals("")) emailDisplay += "<br/>" + um.getEmail2() + "(2)";
                trClass = (trClass.equals("row1") ? "row2" : "row1");
                %>
                <tr class="<%=trClass%>" style="vertical-align:top">
                    <td><%=(counter+1)%>. <a href="#" title="ID: <%=um.getUserId()%>" style="padding-left:1em" onclick="return showPlayerDetail('<%=um.getUserId()%>', '<%=um.getFirstName()%>', '<%=um.getLastName()%>', '<%=counter++%>', '<%=um.getEmail()%>', '<%=um.getEmail2()%>', '<%=um.getLoginId()%>', '<%=(um.isLoginDisabled() ? "Y" : "")%>')"><%=um.getFullName()%></a></td>
                    <td><pre style="margin:0; padding:0"><%=um.getAddress()%>&nbsp;</pre></td>
                    <td><%=emailDisplay%></td>
                    <td><%=um.getLoginId()%></td>
                    <td style="text-align:center"><%=(um.isLoginDisabled() ? "Disabled" : "OK")%></td>
                    <td style="text-align:center"><a href="#" onclick="return showPlayerPayStatus('<%=um.getUserId()%>', '<%=um.getFullName()%>', '<%=(um.isPaid() ? 1 : 0)%>')"><%=(um.isPaid() ? "Paid" : "Not Paid")%></a></td>
                </tr>
                <%
            }
%>
        </form>
    </table>
</div>
<div id="divPayment" class="msg">
    <form name="frmPayment" action="<%=savePaymentURL%>" method="POST">
        <input type="hidden" name="userId" value="-1" />
        <table cellspacing="0" cellpadding="5" border="0">
            <tr>
                <td>Player Name</td>
                <td><input type="text" name="nm" readonly="true" style="read-only:true; font-weight:bold; text-align:center; background-color:#CCD7B7; border:0; width:12em"></td>
            </tr>
            <tr>
                <td>Payment Status</td>
                <td>
                    <input type="radio" name="payStatus" value="1" CHECKED style="margin-left:0em">&nbsp;Paid
                    <input type="radio" name="payStatus" value="0" CHECKED style="margin-left:2em">&nbsp;Not Paid
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="checkbox" name="em" CHECKED>&nbsp;Notify player via email</td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:left; padding-top:0.5em; padding-bottom:1em">
                    <input type="submit" name="btnPaySave" value="Save" onclick="hideDiv('divPayment')" class="formbutton" />
                    <input type="button" name="btnPayCancel" value="Cancel" onclick="hideDiv('divPayment')" class="formbutton" />
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="divPlayer" class="msg">
    <form name="frmPlayer" action="<%=savePlayerURL%>" method="POST">
        <input type="hidden" name="userId" value="-1" />
        <table cellspacing="0" cellpadding="2" border="0">
            <tr>
                <td>First Name</td>
                <td><input type="text" name="firstName" maxlength="30" style="width:10em" /></td>
            </tr>
            <tr>
                <td>Last Name</td>
                <td><input type="text" name="lastName" maxlength="30" style="width:20em" /></td>
            </tr>
            <tr valign="top">
                <td>Address</td>
                <td><textarea cols="30" rows="4" name="addr" style="width:20em"></textarea></td>
            </tr>
            <tr>
                <td>Email Address</td>
                <td><input type="text" name="email" maxlength="64" style="width:20em" /></td>
            </tr>
            <tr>
                <td>Alt. Email Address</td>
                <td><input type="text" name="email2" maxlength="64" style="width:20em" /></td>
            </tr>
            <tr valign="top">
                <td>Login Id</td>
                <td><input type="text" name="loginId" maxlength="20" style="width:8em; text-align:center" /><span style="padding-left:1em">{player nickname}</span></td>
            </tr>
            <tr valign="top">
                <td>&nbsp;</td>
                <td><input type="checkbox" name="disabld" value="Y" /><span style="padding-left:0.25em">Disable Profile ?</span></td>
            </tr>
            <%
            if( errors != null && !errors.isEmpty() ) {
                String errMsg = "";
                Iterator iterPlayer = errors.iterator();
                while( iterPlayer.hasNext() ) {
                    errMsg += (String)iterPlayer.next() + "<br/>";
                }
                %>
                <tr>
                    <td colspan="2" class="errortext"><%=errMsg%></td>
                </tr>
                <%
            }
            %>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Save" onclick="hideDiv('divPlayer')" class="formbutton" />
                    <input type="button" value="Cancel" onclick="hideDiv('divPlayer')" class="formbutton" />
                    <input type="button" value="Delete Player" onclick="return deletePlayer()" class="formbutton2" />
                </td>
            </tr>
        </table>
    </form>
</div>
<script language="javascript">
    sDeletePlayerAction = "<%=deletePlayerURL%>";
    <% 
    if( players.size() > 0 ) { 
        %>
        aAddrs = new Array(<%=players.size()%>);
        <%
        counter = 0;
        iter = players.iterator();
        String s = System.getProperty( "line.separator" );
        while( iter.hasNext() ) {
            UserModel um = (UserModel)iter.next();
            String addr = um.getAddress();
            addr = addr.replaceAll("\"", "'");
            addr = addr.replaceAll(s, "|");
            addr = addr.replaceAll("\n", "|");
            addr = addr.replaceAll(String.valueOf( (char)10 ), "|");
            addr = addr.replaceAll(String.valueOf( (char)13 ), "|");
            %>
            aAddrs[<%=counter++%>] = "<%=addr%>";
            <%
        }
    }
    %>
</script>
