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
    detailPage = detailPage.replaceAll("/jsp/std/", "/jsp/mobile/");
    
    String pageTitle = "Season Statistics";
%>
<jsp:include page="/jsp/mobile/field/page_header.jsp" />

<h2><%=pageTitle%></h2>
<div style="padding:0.5em 0 2em 0.5em">
    <table cellspacing="0" cellpadding="0" border="0">
        <tr valign="top">
            <!-- MENU -->
            <td>
                <a href="<%=teamURL%>" style="<%=(menuItem.equals("Team") ? "font-weight:bold" : "")%>">Teams (Individual)</a>
                <a href="<%=teamGroupURL%>" style="margin-left:2em; <%=(menuItem.equals("TeamGroup") ? "font-weight:bold" : "")%>">Teams (Grouped)</a>
                <a href="<%=picksURL%>" style="margin-left:2em; <%=(menuItem.equals("Picks") ? "font-weight:bold" : "")%>">Picks By Team</a>
            </td>
        </tr>
        <tr>
            <!-- DETAIL SECTION -->
            <td>
                <jsp:include page="<%=detailPage%>" />
            </td>
        </tr>
    </table>
</div>

<jsp:include page="/jsp/mobile/field/page_footer.jsp"/>
