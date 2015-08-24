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
    
    String pageTitle = "Season Standings";
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
                        <td class="menu"><a href="<%=picksURL%>" style="<%=(menuItem.equals("Picks") ? "font-weight:bold" : "")%>">- Picks</a></td>
                    </tr>
                    <tr>
                        <td class="menu"><a href="<%=locksURL%>" style="<%=(menuItem.equals("Locks") ? "font-weight:bold" : "")%>">- Locks</a></td>
                    </tr>
                    <tr>
                        <td class="menu"><a href="<%=survivorURL%>" style="<%=(menuItem.equals("Survivor") ? "font-weight:bold" : "")%>" >- Survivor</a></td>
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
