<%@page contentType="text/html"%>
<%@page import="java.util.*, java.text.*"%>
<%@page import="com.ocsports.core.Status"%>
<%@ page import="com.ocsports.models.*" %>

<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    SeasonModel season = (SeasonModel)request.getAttribute("SeasonModel");
    Collection series = (Collection)request.getAttribute("SeriesModels");
    Collection games  = (Collection)request.getAttribute("GameModels");
    SortedMap standings = (SortedMap)request.getAttribute("standings");
    HashMap teamMap = (HashMap)request.getAttribute("TeamMap");
    HashMap gamePicks = (HashMap)request.getAttribute("UserGameXrefModels");
    HashMap seriesPicks = (HashMap)request.getAttribute("UserSeriesXrefModels");
    int selectedSeriesId = Integer.parseInt( (String)request.getAttribute("selectedSeriesId") );
    SimpleDateFormat fmt = new SimpleDateFormat();
    Iterator iter = null;
    
    SeasonSeriesModel ssm = null;
    if(teamMap == null) teamMap = new HashMap();
    if(gamePicks == null) gamePicks = new HashMap();
    if(seriesPicks == null) seriesPicks = new HashMap();
    
    iter = series.iterator();
    while( iter.hasNext() ) {
        SeasonSeriesModel seriesModel  = (SeasonSeriesModel)iter.next();
        if( seriesModel.getId() == selectedSeriesId ) {
            ssm = seriesModel;
            break;
        }
    }

    String seriesURL = response.encodeURL("goPool?r=leaguePicks");

    fmt.applyPattern("MMM d");
    String pageTitle = "League Picks for " + season.getSeriesPrefix() + "&nbsp;" + ssm.getSequence() + " (" + fmt.format( new java.util.Date(ssm.getStartDate()) ) + " - " + fmt.format( new java.util.Date(ssm.getEndDate()) ) + ")";
%>

<jsp:include page="/jsp/mobile/field/page_header.jsp" />

<h2><%=pageTitle%></h2>
<div id="divSeries" style="padding-top:0em">
    <table cellspacing="0" cellpadding="0" border="0">
        <tr>
            <td style="padding-left:3em">View <%=season.getSeriesPrefix()%>:</td>
            <%
            fmt.applyPattern("MMMM d");
            String linkTitle = "";
            String linkDisplay = "";
            iter = series.iterator();
            while( iter.hasNext() ) {
                SeasonSeriesModel ssm2 = (SeasonSeriesModel)iter.next();
                linkTitle = fmt.format( new java.util.Date(ssm2.getStartDate()) ) + " - " + fmt.format( new java.util.Date(ssm2.getEndDate()) );
                linkDisplay = String.valueOf( ssm2.getSequence() );
                if( selectedSeriesId == ssm2.getId() ) linkDisplay = "[" + linkDisplay + "]";
                %>
                <td style="text-align:center; padding-left:0.5em"><a href="<%=seriesURL%>&series=<%=ssm2.getId()%>" title="<%=linkTitle%>"><%=linkDisplay%></a></td>
                <%
            }
            %>
        </tr>
    </table>
</div>

<div id="divPicks" style="padding-top:1em; padding-bottom:3em">
    <table cellspacing="0" cellpadding="5" class="league">
        <%
        String teamDisplay = "";
        String scoreDisplay = "";
        String spreadDisplay = "";
        boolean allScoresPosted = true;
        iter = games.iterator();
        while( iter.hasNext() ) {
            GameModel gm = (GameModel)iter.next();
            TeamModel homeModel = (TeamModel)teamMap.get(String.valueOf(gm.getHomeTeamId()));
            TeamModel awayModel = (TeamModel)teamMap.get(String.valueOf(gm.getAwayTeamId()));
            teamDisplay += "<td>" + awayModel.getAbrv() + "<br/>at<br/>" + homeModel.getAbrv() + "</td>";
            if( gm.isPosted() ) {
                if(gm.getAwayScore() > gm.getHomeScore())
                    scoreDisplay += "<td>" + awayModel.getAbrv() + "<br/>" + gm.getAwayScore() + "-" + gm.getHomeScore() + "</td>";
                else
                    scoreDisplay += "<td>" + homeModel.getAbrv() + "<br/>" + gm.getHomeScore() + "-" + gm.getAwayScore() + "</td>";
            }
            else {
                allScoresPosted = false;
                if( gm.getStartDate() >= System.currentTimeMillis() ) {
                    fmt.applyPattern("EEE h:mm");
                    scoreDisplay += "<td>" + fmt.format( new java.util.Date(gm.getStartDate()) ) + "</td>";
                } else {
                    scoreDisplay += "<td>&nbsp;</td>";
                }
            }
            if(ssm.isSpreadPublished()) {
                if(gm.getSpread() > 0)
                    spreadDisplay += "<td>" + awayModel.getAbrv() + "<br/>-" + gm.getSpread() + "</td>";
                else
                    spreadDisplay += "<td>" + homeModel.getAbrv() + "<br/>" + gm.getSpread() + "</td>";
            }
        }
        %>
        <tr class="hdr">
            <td colspan="2">&nbsp;</td>
            <%=scoreDisplay%>
            <td colspan="3" style="font-size:6pt; vertical-align:top; text-align:right">* all times PST</td>
        </tr>
        <%
        if( ssm.isSpreadPublished() ) {
            %>
            <tr class="hdr">
                <td colspan="2">&nbsp;</td>
                <%=spreadDisplay%>
                <td colspan="3">&nbsp;</td>
            </tr>
            <%
        } 
        %>
        <tr class="hdr">
            <td colspan="2">&nbsp;</td>
            <%=teamDisplay%>
            <td>Lock</td>
            <td>Survivor</td>
            <td>Wins</td>
        </tr>
        <%
        if( standings != null && standings.size() > 0 ) {
            Iterator iter2 = standings.keySet().iterator();
            int counter = 0;
            String trClass = "";
            String trStyle = "";
            while( iter2.hasNext() ) {
                StandingsModel stm = (StandingsModel)standings.get((String)iter2.next());
                trClass = (trClass.equals("row1") ? "row2" : "row1");
                trStyle = (stm.getUserId() == loginUser.getUserId() ? "background-color:#FFF380" : "");
                %>
                <tr class="<%=trClass%>" style="<%=trStyle%>">
                    <td style="text-align:left"><%=++counter%>.</td>
                    <td style="text-align:left"><%=stm.getUserName()%></td>
                    <%
                    iter = games.iterator();
                    int wins = 0;
                    int losses = 0;
                    while( iter.hasNext() ) {
                        GameModel gm = (GameModel)iter.next();
                        UserGameXrefModel ugx = null;
                        TeamModel teamPick = null;
                        if( gm.getStartDate() < new java.util.Date().getTime() ) {
                            String pickIdentifier = String.valueOf(stm.getUserId()) + "^" + gm.getId();
                            ugx = (UserGameXrefModel)gamePicks.get(pickIdentifier);
                            if(ugx != null) {
                                int selectedTeam = ugx.getSelectedTeamId();
                                teamPick = (TeamModel)teamMap.get(String.valueOf(selectedTeam));
                            }
                            String pickStyle = "";
                            if( teamPick != null ) {
                                if(ugx.getStatus() == Status.GAME_STATUS_WON) {
                                    wins++;
                                    pickStyle = "font-weight:bold; color:darkgreen";
                                }
                                else if(ugx.getStatus() == Status.GAME_STATUS_LOST) {
                                    losses++;
                                    pickStyle = "font-weight:bold; color:red; text-decoration:line-through";
                                }
                                else {
                                    pickStyle = "";
                                }
                                %>
                                <td style="<%=pickStyle%>"><%=teamPick.getAbrv()%></td>
                                <%
                            } else {
                                %>
                                <td>&nbsp;</td>
                                <%
                            }
                        } else {
                            %>
                            <td>&nbsp;</td>
                            <%
                        }
                    }
                    TeamModel lockTeam = null;
                    TeamModel survivorTeam = null;
                    String lockStyle = "";
                    String survivorStyle = "";
                    if(allScoresPosted) {
                        UserSeriesXrefModel usx = (UserSeriesXrefModel)seriesPicks.get(stm.getUserId()+","+selectedSeriesId);
                        if(usx != null) {
                            if(usx.getLock() > 0) {
                                lockTeam = (TeamModel)teamMap.get(String.valueOf(usx.getLock()));
                                if(usx.getLockStatus() == Status.LOCK_STATUS_WON) lockStyle = "font-weight:bold; color:darkgreen";
                                if(usx.getLockStatus() == Status.LOCK_STATUS_LOST) lockStyle = "font-weight:bold; color:red; text-decoration:line-through";
                                if(usx.getLockStatus() == Status.LOCK_STATUS_TIE) lockStyle = "font-weight:bold; color:blue";
                            }
                            if(usx.getSurvivor() > 0) {
                                survivorTeam = (TeamModel)teamMap.get(String.valueOf(usx.getSurvivor()));
                                if(usx.getSurvivorStatus() == Status.SURVIVOR_STATUS_WON) {
                                    survivorStyle = "font-weight:bold; color:darkgreen";
                                }
                                else if(usx.getSurvivorStatus() == Status.SURVIVOR_STATUS_LOST) {
                                    survivorStyle = "font-weight:bold; color:red; text-decoration:line-through";
                                }
                            }
                        }
                    }
                    %>
                    <td style="<%=lockStyle%>"><%=(lockTeam != null ? lockTeam.getAbrv() : (allScoresPosted ? "--" : "&nbsp;"))%></td>
                    <td style="<%=survivorStyle%>"><%=(survivorTeam != null ? survivorTeam.getAbrv() : (allScoresPosted ? "--" : "&nbsp;"))%></td>
                    <td style="font-weight:bold"><%=wins%></td>
                </tr>
                <%
            }
        }
        %>
    </table>
</div>

<jsp:include page="/jsp/mobile/field/page_footer.jsp" />
