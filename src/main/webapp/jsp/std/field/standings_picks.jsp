<%@page contentType="text/html"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.SortedMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.ocsports.models.LeagueSeriesXrefModel"%>
<%@page import="com.ocsports.models.SeasonSeriesModel"%>
<%@page import="com.ocsports.models.StandingsModel"%>
<%@page import="com.ocsports.models.UserModel"%>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    Collection leagueSeries = (Collection)request.getAttribute("LeagueSeriesModels");
    Collection series = (Collection)request.getAttribute("SeriesModels");
    SortedMap standings = (SortedMap)request.getAttribute("standings");
    if(standings == null) standings = new TreeMap();
%>

<table cellspacing="0" cellpadding="3" border="0" class="league">
    <tr class="hdr">
        <td colspan="2">&nbsp;</td>
        <td>Season</td>
        <%
        Iterator iter = series.iterator();
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
    String tdStyle = "";
    int counter = 0;
    int userWins = -1;
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
                userWins = 0;
                SeasonSeriesModel ssm = (SeasonSeriesModel)iter2.next();
                String wins = (String)seriesWins.get(String.valueOf(ssm.getId()));
                String winsDisplay = "&nbsp;";
                if( wins != null && !wins.equals("") ) {
                    String[] winsLosses = wins.split(",");
                    winsDisplay = winsLosses[0];
                    userWins = Integer.parseInt(winsDisplay);
                }
                // BOLD the high scores for the league
                tdStyle = "";
                if(leagueSeries != null && leagueSeries.size() > 0) {
                    Iterator iter3 = leagueSeries.iterator();
                    while(iter3.hasNext()) {
                        LeagueSeriesXrefModel lsx = (LeagueSeriesXrefModel)iter3.next();
                        if (lsx.getSeriesId() == ssm.getId()) {
                            int highScore = lsx.getLeagueHighScore();
                            if(highScore > 0 && highScore == userWins) {
                                // bold + light-green; or use row style
                                tdStyle = "font-weight:bold; background-color:#A3E4D7;";
                            }
                        }
                    }
                }
                %>
                <td style="<%=tdStyle%>width:2em"><%=winsDisplay%></td>
                <%
            }
            %>
        </tr>
        <%
    }
    %>
</table>
