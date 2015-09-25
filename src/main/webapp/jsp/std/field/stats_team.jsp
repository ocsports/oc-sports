<%@page contentType="text/html"%>
<%@page import="java.util.*,java.text.*"%>
<%@page import="com.ocsports.models.*"%>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    
    TeamStatsModel[] teamStats = (TeamStatsModel[])request.getAttribute("TeamStatsModels");
%>

<div style="margin-left:1em">
    <table cellspacing="0" cellpadding="3" border="0" class="league" style="padding:0">
        <tr class="hdr">
            <td>&nbsp;</td>
            <td colspan="7">W-L-T (No Spread)</td>
            <td></td>
            <td colspan="7">W-L-T (With Spread)</td>
        </tr>
        <tr class="hdr">
            <td style="text-align:left">Team</td>
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
        if(teamStats != null && teamStats.length > 0) {
            String trClass = "";
            for(int i=0; i < teamStats.length; i++) {
                TeamStatsModel tsm = teamStats[i];
                trClass = (trClass.equals("row1") ? "row2" : "row1");
                %>
                <tr class="<%=trClass%>">
                    <td style="font-size:10pt; text-align:left"><%=tsm.getTeamName()%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.OVERALL)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.HOME)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.AWAY)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.DOME)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.OUTSIDE)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.GRASS)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.TURF)%></td>
                    <td>&nbsp;</td>
                    <td><%=tsm.getRecord(TeamStatsModel.SPREAD_OVERALL)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.SPREAD_HOME)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.SPREAD_AWAY)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.SPREAD_DOME)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.SPREAD_OUTSIDE)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.SPREAD_GRASS)%></td>
                    <td><%=tsm.getRecord(TeamStatsModel.SPREAD_TURF)%></td>
                </tr>
                <%
            }
        }
    %>
    </table>
</div>
