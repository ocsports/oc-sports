<%@page contentType="text/html"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="com.ocsports.models.UserModel"%>
<%@page import="com.ocsports.models.TeamModel"%>
<%@page import="com.ocsports.models.StandingsModel"%>
<%@page import="com.ocsports.models.ForumMessageModel"%>
<%@page import="com.ocsports.models.LockStandingsModel"%>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");

    Collection sysNotices = (Collection)request.getAttribute("SystemNotices");
    Collection forums = (Collection)request.getAttribute("ForumMessageModels");

    SortedMap standings = (SortedMap)request.getAttribute("standings");
    SortedMap lockStandings = (SortedMap)request.getAttribute("lockStandings");

    String survivorCount = (String)request.getAttribute("survivorCount");

    SimpleDateFormat fmt = new SimpleDateFormat();

    String standingsURL = response.encodeURL("goPool?r=standings");
    String lockStandingsURL = response.encodeURL("goPool?r=lockStandings");
    String survivorURL = response.encodeURL("goPool?r=survivorStandings");
    String forumsURL = response.encodeURL("goPool?r=forums");

    Iterator iter = null;
    String trClass = "";
    String trStyle = "";
    int counter = 0;
%>
<jsp:include page="/jsp/std/field/page_header.jsp"/>

<table cellspacing="20" cellpadding="0" border="0" style="width:100%">
    <tr style="vertical-align:top">
        <td style="width:175px">
            <table width="100%" cellspacing="0" cellpadding="5" class="league" style="padding:0; border:1px solid darkgreen">
                <tr class="hdr">
                    <td colspan="2" style="text-align:left">League Leaders</td>
                </tr>
                <%
                iter = standings.keySet().iterator();
                counter = 0;
                while( counter++ < 15 ) {
                    StandingsModel psm = null;
                    String uName = "&nbsp;";
                    String uRecord = "&nbsp;";
                    trClass = (trClass.equals("row1") ? "row2" : "row1");
                    trStyle = "";
                    if( iter.hasNext() ) {
                        psm = (StandingsModel)standings.get((String)iter.next());
                        uName = psm.getUserName();
                        uRecord = psm.getWins() + "-" + psm.getLosses();
                        if( psm.getUserId() == loginUser.getUserId() ) trStyle = "background-color:#FFF380";
                    }
                    %>
                    <tr class="<%=trClass%>" style="<%=trStyle%>">
                        <td width="100%" style="text-align:left"><%=uName%></td>
                        <td nowrap="true"><%=uRecord%></td>
                    </tr>
                    <%
                }
                %>
                <tr>
                    <td colspan="2" style="font-weight:bold; text-align:left"><a href="<%=standingsURL%>">Complete Standings...</a></td>
                </tr>
            </table>
        </td>

        <td>
			<!--
			<div id="divPmtInfo" style="margin:1em; font-size:10pt" onmouseover="getElementById('divPayment').style.display=''" onmouseout="getElementById('divPayment').style.display='none'">
				Payment Information
				<div id="divPayment" style="font-size:8pt; width:400px; display:none">
					Entry Fee per player:  $110.00 (due by Wednesday, Sept. 10th)<br/>
					Send your entry fee via PayPal to paypal@oc-sports.com<br/>
					- OR -<br/>
					Send a check or money order to:<br/>
					  PAUL CHARLTON<br/>
					  PO BOX 687<br/>
					  PLACENTIA, CA  92871<br/>
				</div>
			<div/>
			-->
            <table width="100%" cellspacing="0" cellpadding="5" class="league" style="padding:0; border:1px solid darkgreen">
                <tr class="hdr">
                    <td style="text-align:left">Recent Forum Messages</td>
                </tr>
                <%
                counter = 0;
                trClass = "";
                if(forums != null && !forums.isEmpty()) {
                    fmt.applyPattern( "MMMM d, yyyy h:mm a" );
                    iter = forums.iterator();
                    while( iter.hasNext() ) {
                        counter++;
                        ForumMessageModel fmm = (ForumMessageModel)iter.next();
                        trClass = (trClass.equals("row1") ? "row2" : "row1");
                        %>
                        <tr class="<%=trClass%>">
                            <td style="text-align:left">
                                <span style="font-size:10pt"><%=fmm.getText()%></span>
                                <br/><span style="font-size:8pt; font-weight:normal; font-style:italic">&lt;<%=fmm.getCreatedByName()%>&nbsp;<%=fmt.format( new java.util.Date(fmm.getCreatedDate()) )%>&gt;</span>
                            </td>
                        </tr>
                        <%
                    }
                }
                if( counter == 0 ) {
                    %>
                    <tr>
                        <td align="center">No messages have been posted</td>
                    </tr>
                    <%
                }
                %>
                <tr>
                    <td style="font-weight:bold; text-align:left"><a href="<%=forumsURL%>">View all messages...</a></td>
                </tr>
            </table>
        </td>

        <td style="width:175px">
            <table width="100%" cellspacing="0" cellpadding="5" class="league" style="padding:0; border:1px solid darkgreen">
                <tr class="hdr">
                    <td colspan="2" style="text-align:left">Current Standings (Locks)</td>
                </tr>
                <%
                iter = lockStandings.keySet().iterator();
                trClass = "";
                counter = 0;
                while( counter++ < 10 ) {
                    LockStandingsModel lsm = null;
                    String lockDisplay = "&nbsp;";
                    String lockRecord = "&nbsp;";
                    trClass = (trClass.equals("row1") ? "row2" : "row1");
                    trStyle = "";
                    if( iter.hasNext() ) {
                        lsm = (LockStandingsModel)lockStandings.get((String)iter.next());
                        lockDisplay = lsm.getUserName();
                        lockRecord = lsm.getWins() + "-" + lsm.getLosses() + "-" + lsm.getTies();
                        if( lsm.getUserId() == loginUser.getUserId() ) trStyle = "background-color:#FFF380";
                    }
                    %>
                    <tr class="<%=trClass%>" style="<%=trStyle%>">
                        <td width="100%"><%=lockDisplay%></td>
                        <td nowrap align="center"><%=lockRecord%></td>
                    </tr>
                    <%
                }
                %>
                <tr>
                    <td colspan="2" style="font-weight:bold; text-align:left"><a href="<%=lockStandingsURL%>">Complete Standings...</a></td>
                </tr>
            </table>
            <table width="100%" cellspacing="0" cellpadding="5" class="league" style="margin-top:3em; padding:0; border:1px solid darkgreen">
                <tr class="hdr">
                    <td colspan="2">Survivor Pool</td>
                </tr>
                <tr>
                    <td width="100%" style="text-align:left; font-weight:bold;"><a href="<%=survivorURL%>">Remaining Players</a></td>
                    <td nowrap align="center"><%=survivorCount%></td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<jsp:include page="/jsp/std/field/page_footer.jsp"/>
