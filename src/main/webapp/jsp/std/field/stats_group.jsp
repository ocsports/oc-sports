<%@page contentType="text/html"%>
<%@page import="java.util.*,java.text.*"%>
<%@page import="com.ocsports.models.*"%>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    TeamStatsModel[] teamStats = (TeamStatsModel[])request.getAttribute("TeamStatsModels");
    Collection series = (Collection)request.getAttribute("SeasonSeriesModels");
    SeasonModel season = (SeasonModel)request.getAttribute("SeasonModel");
    int seriesId = Integer.parseInt( (String)request.getAttribute("seriesId") );
    
    Iterator iter = null;
    SimpleDateFormat fmt = new SimpleDateFormat();
    
    String statsURL = response.encodeURL("goStats?r=byTeamGroup");
%>

<div style="margin-left:1em">
    <table cellspacing="0" cellpadding="0" border="0" style="padding-bottom:1em">
        <tr>
            <td style="padding-left:1.5em">View <%=season.getSeriesPrefix()%>:</td>
                <%
                fmt.applyPattern("MMMM d");
                String linkTitle = "";
                String linkDisplay = "";
                iter = series.iterator();
                while( iter.hasNext() ) {
                    SeasonSeriesModel ssm = (SeasonSeriesModel)iter.next();
                    linkTitle = fmt.format( new java.util.Date(ssm.getStartDate()) ) + " - " + fmt.format( new java.util.Date(ssm.getEndDate()) );
                    linkDisplay = String.valueOf( ssm.getSequence() );
                    if( ssm.getId() == seriesId ) linkDisplay = "[" + linkDisplay + "]";
                    %>
                    <td style="text-align:center; padding-left:0.5em"><a href="<%=statsURL%>&seriesId=<%=ssm.getId()%>" title="<%=linkTitle%>"><%=linkDisplay%></a></td>
                    <%
                }
                linkDisplay = "All";
                if( seriesId <= 0 ) linkDisplay = "[" + linkDisplay + "]";
                %>
            <td style="text-align:center; padding-left:0.5em"><a href="<%=statsURL%>" title="Entire season"><%=linkDisplay%></a></td>
        </tr>
    </table>

    <table cellspacing="0" cellpadding="3" border="0" class="league" style="padding:0">
        <tr class="hdr">
            <td>&nbsp;</td>
            <td colspan="7">W-L-T (No Spread)</td>
            <td></td>
            <td colspan="7">W-L-T (With Spread)</td>
        </tr>
        <tr class="hdr">
            <td>Team</td>
            <td>Overall</td>
            <td>Home</td>
            <td>Away</td>
            <td>Indoors</td>
            <td>Outdoors</td>
            <td>Grass</td>
            <td>Turf</td>
            <td></td>
            <td>Overall</td>
            <td>Home</td>
            <td>Away</td>
            <td>Indoors</td>
            <td>Outdoors</td>
            <td>Grass</td>
            <td>Turf</td>
        </tr>
        <%
        if( teamStats != null && teamStats.length > 0 ) {
            String trClass = "";
            int counter = 0;
            for(int i=0; i < teamStats.length; i++) {
                TeamStatsModel tsm = teamStats[i];
                trClass = (counter == 0 ? "row2" : "row1");
                %>
                <tr class="<%=trClass%>">
                    <td nowrap="true" style="font-size:10pt; text-align:left"><%=tsm.getTeamName()%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.OVERALL)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.HOME)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.AWAY)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.DOME)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.OUTSIDE)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.GRASS)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.TURF)%></td>
                    <td>&nbsp;</td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.SPREAD_OVERALL)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.SPREAD_HOME)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.SPREAD_AWAY)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.SPREAD_DOME)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.SPREAD_OUTSIDE)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.SPREAD_GRASS)%></td>
                    <td nowrap="true"><%=tsm.getRecord(TeamStatsModel.SPREAD_TURF)%></td>
                </tr>
                <%
                if(++counter == 2) {
                    %>
                    <tr style="height:0.75em">
                        <td colspan="16"></td>
                    </tr>
                    <%
                    counter = 0;
                }
            }
        }
        %>
        <tr>
            <td colspan="20" style="border:0; font-size:7pt; text-align:left">** Indoor/Outdoor and Turf/Grass - individual teams are grouped according to their home field</td>
        </tr>
    </table>
</div>
