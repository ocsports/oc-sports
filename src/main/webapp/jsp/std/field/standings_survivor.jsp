<%@page contentType="text/html"%>
<%@page import="java.util.*, java.text.*"%>
<%@page import="com.ocsports.core.Status"%>
<%@page import="com.ocsports.models.*"%>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    
    Collection series = (Collection)request.getAttribute("SeriesModels");
    SortedMap standings = (SortedMap)request.getAttribute("standings");
    if(standings == null) standings = new TreeMap();
    
    long currentTime = new java.util.Date().getTime();
    HashMap teamMap = (HashMap)request.getAttribute("teamMap");
    
    Iterator iter = null;
    Iterator iter2 = null;
%>

<table cellspacing="0" cellpadding="3" border="0" class="league">
    <tr class="hdr">
        <td colspan="2">&nbsp;</td>
        <td>Status</td>
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
    int counter = 0;
    String userStatus = "";
    String statusStyle = "";
    String trClass = "";
    String trStyle = "";
    iter = standings.keySet().iterator();
    while( iter.hasNext() ) {
        String iterKey = (String)iter.next();
        SurvivorStandingsModel sm = (SurvivorStandingsModel)standings.get( iterKey );
        UserModel um = sm.getUserModel();
        HashMap userSeries = sm.getSeriesMap();
        if(userSeries == null) userSeries = new HashMap();
        userStatus = "OK";
        statusStyle = "font-weight:bold; color:darkgreen";
        
        iter2 = series.iterator();
        while( iter2.hasNext() ) {
            SeasonSeriesModel ssm = (SeasonSeriesModel)iter2.next();
            UserSeriesXrefModel usx = (UserSeriesXrefModel)userSeries.get( String.valueOf( ssm.getId() ) );
            if(usx != null && ssm.getEndDate() <= currentTime && usx.getSurvivorStatus() != Status.SURVIVOR_STATUS_WON) {
                userStatus = "OUT";
                statusStyle = "font-weight:bold; color:red";
            }
        }
        
        trClass = (trClass.equals("row1") ? "row2" : "row1");
        trStyle = (um.getUserId() == loginUser.getUserId() ? "background-color:#FFF380" : ""); 
        %>
        <tr class="<%=trClass%>" style="<%=trStyle%>">
            <td style="text-align:left"><%=++counter%>.</td>
            <td style="font-size:10pt; width:10em; text-align:left"><%=um.getLoginId()%></td>
            <td nowrap="true" style="<%=statusStyle%>"><%=userStatus%></td>
            <%
            iter2 = series.iterator();
            while( iter2.hasNext() ) {
                SeasonSeriesModel ssm = (SeasonSeriesModel)iter2.next();
                UserSeriesXrefModel usx = (UserSeriesXrefModel)userSeries.get( String.valueOf( ssm.getId() ) );
                String pickStyle = "";
                String pickDisplay = "&nbsp;";
                if(usx != null && ssm.getEndDate() <= currentTime) {
                    if(usx.getSurvivor() <= 0) {
                        pickDisplay = "--";
                        pickStyle = "font-weight:normal; color:red";
                    } else {
                        TeamModel survivorTeam = (TeamModel)teamMap.get(String.valueOf(usx.getSurvivor()));
                        pickDisplay = survivorTeam.getAbrv();
                        if(usx.getSurvivorStatus() == Status.SURVIVOR_STATUS_WON) {
                            pickStyle = "font-weight:normal; color:darkgreen";
                        } else {
                            pickStyle = "font-weight:normal; color:red; text-decoration:line-through";
                        }
                    }
                }
                %>
                <td style="<%=pickStyle%>"><%=pickDisplay%></td>
                <%
            }
            %>
        </tr>
        <%
    }
%>
</table>
