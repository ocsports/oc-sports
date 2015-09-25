<%@page contentType="text/html"%>
<%@page import="java.util.*, java.text.*"%>
<%@page import="com.ocsports.models.*"%>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    
    Collection series = (Collection)request.getAttribute("SeriesModels");
    SortedMap standings = (SortedMap)request.getAttribute("standings");
    if(standings == null) standings = new TreeMap();
    
    Iterator iter = null;
%>

<table cellspacing="0" cellpadding="3" border="0" class="league">
    <tr class="hdr">
        <td colspan="2">&nbsp;</td>
        <td>Season</td>
        <%
        iter = series.iterator();
        while( iter.hasNext() ) {
            SeasonSeriesModel ssm = (SeasonSeriesModel)iter.next();
            %>
            <td style="width:2em"><%=ssm.getSequence()%></td>
            <%
        }
        %>
    </tr>
    <%
    iter = standings.keySet().iterator();
    String trClass = "";
    String trStyle = "";
    int counter = 0;
    while( iter.hasNext() ) {
        StandingsModel sm = (StandingsModel)standings.get((String)iter.next());
        UserModel um = sm.getUserModel();
        HashMap seriesWins = sm.getSeriesMap();
        if(seriesWins == null) seriesWins = new HashMap();
        trClass = (trClass.equals("row1") ? "row2" : "row1");
        trStyle = (um.getUserId() == loginUser.getUserId() ? "background-color:#FFF380" : ""); 
        %>
        <tr class="<%=trClass%>" style="<%=trStyle%>">
            <td style="text-align:left"><%=++counter%>.</td>
            <td style="font-size:10pt; width:10em; text-align:left"><%=um.getLoginId()%></td>
            <td nowrap="true" style="font-weight:bold"><%=sm.getWins() + "-" + sm.getLosses()%></td>
            <%
            Iterator iter2 = series.iterator();
            while( iter2.hasNext() ) {
                SeasonSeriesModel ssm = (SeasonSeriesModel)iter2.next();
                String wins = (String)seriesWins.get(String.valueOf(ssm.getId()));
                String winsDisplay = "&nbsp;";
                if( wins != null && !wins.equals("") ) {
                    String[] winsLosses = wins.split(",");
                    winsDisplay = winsLosses[0];
                }
                %>
                <td style="width:2em"><%=winsDisplay%></td>
                <%
            }
            %>
        </tr>
        <%
    }
    %>
</table>
