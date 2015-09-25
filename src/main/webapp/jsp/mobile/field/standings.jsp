<%@page contentType="text/html"%>
<%@ page import="com.ocsports.core.JSPPages" %>
<%
    String picksURL = response.encodeURL("goPool?r=standings");
    String locksURL = response.encodeURL("goPool?r=lockStandings");
    String survivorURL = response.encodeURL("goPool?r=survivorStandings");
    
    String menuItem = (String)request.getAttribute("menuItem");
    if(menuItem == null) menuItem = "";
    
    String detailPage = (String)request.getAttribute("detailPage");
    if( detailPage == null || detailPage.length() == 0 ) detailPage = JSPPages.STANDINGS_PICKS;
    detailPage = detailPage.replaceAll("/jsp/std/", "/jsp/mobile/");
    
    String pageTitle = "Season Standings";
%>
<jsp:include page="/jsp/mobile/field/page_header.jsp" />

<h2><%=pageTitle%></h2>
<div style="padding-left:0.5em; padding-top:0.5em; padding-bottom:2em">
    <table cellspacing="0" cellpadding="0" border="0">
        <tr valign="top">
            <!-- MENU -->
            <td style="padding:0 0 0.25em 0">
                <a href="<%=picksURL%>" style="<%=(menuItem.equals("Picks") ? "font-weight:bold" : "")%>">Picks</a>
                <a href="<%=locksURL%>" style="margin-left:2em; <%=(menuItem.equals("Locks") ? "font-weight:bold" : "")%>">Locks</a>
                <a href="<%=survivorURL%>" style="margin-left:2em; <%=(menuItem.equals("Survivor") ? "font-weight:bold" : "")%>">Survivor</a>
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
