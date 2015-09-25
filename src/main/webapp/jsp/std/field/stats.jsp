<%@page contentType="text/html"%>
<%@ page import="com.ocsports.core.JSPPages" %>
<%
    String teamURL = response.encodeURL("goStats?r=byTeam");
    String teamGroupURL = response.encodeURL("goStats?r=byTeamGroup");
    String picksURL = response.encodeURL("goStats?r=byTeamPicks");

    String menuItem = (String)request.getAttribute("menuItem");
    if(menuItem == null) menuItem = "";

    String detailPage = (String)request.getAttribute("detailPage");
    if( detailPage == null || detailPage.length() == 0 ) detailPage = JSPPages.STANDINGS_PICKS;
    
    String pageTitle = "Season Statistics";
%>
<jsp:include page="/jsp/std/field/page_header.jsp" />

<h2><%=pageTitle%></h2>
<div style="padding-left:0.5em; padding-top:0.5em; padding-bottom:2em; margin-right:1em">
    <table cellspacing="0" cellpadding="0" border="0">
        <tr valign="top">
            <!-- MENU -->
            <td>
                <table cellspacing="0" cellpadding="0" border="0" class="leftmenu">
                    <tr>
                        <td class="menu"><a href="<%=teamURL%>" style="<%=(menuItem.equals("Team") ? "font-weight:bold" : "")%>">- Teams (Individual)</a></td>
                    </tr>
                    <tr>
                        <td class="menu"><a href="<%=teamGroupURL%>" style="<%=(menuItem.equals("TeamGroup") ? "font-weight:bold" : "")%>">- Teams (Grouped)</a></td>
                    </tr>
                    <tr>
                        <td class="menu"><a href="<%=picksURL%>" style="<%=(menuItem.equals("Picks") ? "font-weight:bold" : "")%>">- Picks By Team</a></td>
                    </tr>
                </table>
            </td>

            <!-- DETAIL SECTION -->
            <td>
                <jsp:include page="<%=detailPage%>" />
            </td>
        </tr>
    </table>
</div>

<jsp:include page="/jsp/std/field/page_footer.jsp"/>
