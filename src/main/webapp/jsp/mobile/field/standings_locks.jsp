<%@page contentType="text/html"%>
<%@page import="java.util.*, java.text.*"%>
<%@page import="com.ocsports.core.Status"%>
<%@ page import="com.ocsports.models.*" %>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    
    Collection series = (Collection)request.getAttribute("SeriesModels");
    SortedMap standings = (SortedMap)request.getAttribute("standings");
    if(standings == null) standings = new TreeMap();
    
    long currentTime = new java.util.Date().getTime();
    HashMap teamMap = (HashMap)request.getAttribute("teamMap");
    
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
            <td style="width:3em"><%=ssm.getSequence()%></td>
            <%
        }
        %>
    </tr>
    <%
    String trClass = "";
    String trStyle = "";
    int counter = 0;
    iter = standings.keySet().iterator();
    while( iter.hasNext() ) {
        String iterKey = (String)iter.next();
        LockStandingsModel lsm = (LockStandingsModel)standings.get(iterKey);
        UserModel um = lsm.getUserModel();
        HashMap userSeries = lsm.getSeriesMap();
        if(userSeries == null) userSeries = new HashMap();
        trClass = (trClass.equals("row1") ? "row2" : "row1");
        trStyle = (um.getUserId() == loginUser.getUserId() ? "background-color:#FFF380" : ""); 
        %>
        <tr class="<%=trClass%>" style="<%=trStyle%>">
            <td style="text-align:left"><%=++counter%>.</td>
            <td nowrap="true" style="width:10em; text-align:left"><%=um.getLoginId()%></td>
            <td><%=lsm.getWins() + "-" + lsm.getLosses() + "-" + lsm.getTies()%></td>
            <%
            Iterator iter2 = series.iterator();
            while( iter2.hasNext() ) {
                SeasonSeriesModel ssm = (SeasonSeriesModel)iter2.next();
                UserSeriesXrefModel usx = (UserSeriesXrefModel)userSeries.get(String.valueOf(ssm.getId()));
                String lockStyle = "";
                String lockDisplay = "&nbsp;";
                if(usx != null && ssm.getEndDate() <= currentTime) {
                    if(usx.getLock() <= 0) {
                        lockDisplay = "--";
                    }
                    else {
                        TeamModel lockTeam = (TeamModel)teamMap.get(String.valueOf(usx.getLock()));
                        if(usx.getLockStatus() == Status.LOCK_STATUS_WON) lockStyle = "font-weight:bold; color:darkgreen";
                        if(usx.getLockStatus() == Status.LOCK_STATUS_LOST) lockStyle = "font-weight:bold; color:red; text-decoration:line-through";
                        if(usx.getLockStatus() == Status.LOCK_STATUS_TIE) lockStyle = "font-weight:bold; color:blue";
                        lockDisplay = lockTeam.getAbrv();
                    }
                }
                %>
                <td style="<%=lockStyle%>"><%=lockDisplay%></td>
                <%
            }
            %>
        </tr>
        <%
    }
    %>
</table>
