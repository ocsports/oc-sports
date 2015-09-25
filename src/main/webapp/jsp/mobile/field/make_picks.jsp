<%@page contentType="text/html"%>
<%@page import="java.util.*, java.text.*"%>
<%@ page import="com.ocsports.models.*" %>
<%
    UserModel loginUser = (UserModel)session.getAttribute("UserModel");
    SeasonModel season = (SeasonModel)request.getAttribute("SeasonModel");
    Collection series = (Collection)request.getAttribute("SeriesModels");
    Collection games = (Collection)request.getAttribute("GameModels");
    HashMap teamMap = (HashMap)request.getAttribute("TeamMap");
    Collection userPicks = (Collection)request.getAttribute("UserGameXrefModels");
    Collection allUserSeries = (Collection)request.getAttribute("allUserSeries");
    UserSeriesXrefModel usm = (UserSeriesXrefModel)request.getAttribute("UserSeriesXrefModel");
    SeasonSeriesModel selectedSeriesModel = (SeasonSeriesModel)request.getAttribute("SeasonSeriesModel");
    boolean survivorEliminated = (request.getAttribute("survivorEliminated") != null);
    
    SimpleDateFormat fmt = new SimpleDateFormat();
    Iterator iter = null;
    if( usm == null ) usm = new UserSeriesXrefModel();
    int selectedSeriesId = selectedSeriesModel.getId();
    
    boolean lockGameStarted = false;
    boolean survivorGameStarted = false;
    int lockTeamId = usm.getLock();
    int survivorTeamId = usm.getSurvivor();
    
    SortedMap lockTeams = new TreeMap();
    if(lockTeamId > 0) {
        TeamModel lockTeamModel = (TeamModel)teamMap.get(String.valueOf(lockTeamId));
        lockTeams.put(lockTeamModel.getCity()+lockTeamModel.getName(), lockTeamModel);
    }
    
    SortedMap survivorTeams = new TreeMap();
    if(survivorTeamId > 0) {
        TeamModel survivorTeamModel = (TeamModel)teamMap.get(String.valueOf(survivorTeamId));
        survivorTeams.put(survivorTeamModel.getCity()+survivorTeamModel.getName(), survivorTeamModel);
    }
    
    String formURL = response.encodeURL("goPool?r=updatePicks");
    String seriesURL = response.encodeURL("goPool?r=picks");
    
    fmt.applyPattern("MMM d");
    String pageTitle = "Picks for " + season.getSeriesPrefix() + "&nbsp;" + selectedSeriesModel.getSequence() + " (" + fmt.format( new java.util.Date(selectedSeriesModel.getStartDate()) ) + " - " + fmt.format( new java.util.Date(selectedSeriesModel.getEndDate()) ) + ")";
%>
<jsp:include page="/jsp/mobile/field/page_header.jsp" />

<% if(request.getParameter("emailError") != null) { %>
    <div style="margin:1em">
        <span class="errortext">Your picks have been updated.  However, there was an error sending your e-mail confirmation. Please check your e-mail address to ensure you receive future confirmation e-mails</span>
    </div>
<% } %>

<h2><%=pageTitle%></h2>
<div id="divPickTime" style="padding:1em"></div>
<div id="divSeries" style="padding-top:0em">
    <table cellspacing="0" cellpadding="0" border="0">
        <tr>
            <td>View <%=season.getSeriesPrefix()%>:</td>
                <%
                fmt.applyPattern("MMMM d");
                String linkTitle = "";
                String linkDisplay = "";
                iter = series.iterator();
                while( iter.hasNext() ) {
                    SeasonSeriesModel ssm = (SeasonSeriesModel)iter.next();
                    linkTitle = fmt.format( new java.util.Date(ssm.getStartDate()) ) + " - " + fmt.format( new java.util.Date(ssm.getEndDate()) );
                    linkDisplay = String.valueOf( ssm.getSequence() );
                    if( selectedSeriesId == ssm.getId() ) linkDisplay = "[" + linkDisplay + "]";
                    %>
                    <td style="text-align:center; padding-left:0.5em"><a href="<%=seriesURL%>&seriesId=<%=ssm.getId()%>" title="<%=linkTitle%>"><%=linkDisplay%></a></td>
                    <%
                }
                %>
        </tr>
    </table>
</div>

<div id="divGames" style="width:100%; padding-top:1em">
    <form name="frmPicks" action="<%=formURL%>" method="POST">
        <input type="hidden" name="seriesId" value="<%=selectedSeriesId%>"/>
        <input type="hidden" name="gameCount" value="<%=games.size()%>"/>
        <table cellspacing="0" cellpadding="6" class="trtable">
            <tr valign="top" class="hdr">
                <td colspan="2">Kick Off (PST)</td>
                <td>Away Team</td>
                <td>&nbsp;</td>
                <td>Home Team</td>
            </tr>
            <%
            iter = games.iterator();
            int counter = 0;
            String trClass = "";
            String homeSel = "";
            String awaySel = "";
            boolean allGamesStarted = true;
            String dayFmt = "";
            String timeFmt = "";
            String lastDayFmt = "";
            boolean gameDisabled = false;
            
            while( iter.hasNext() ) {
                counter++;
                GameModel gm = (GameModel)iter.next();
                TeamModel home = (TeamModel)teamMap.get( String.valueOf(gm.getHomeTeamId()) );
                TeamModel away = (TeamModel)teamMap.get( String.valueOf(gm.getAwayTeamId()) );
                String awayDisplay = away.getCity() + " " + away.getName();
                String homeDisplay = home.getCity() + " " + home.getName();
                if( selectedSeriesModel.isSpreadPublished() ) {
                    if( gm.getSpread() > 0 ) {
                        awayDisplay = "<strong>" + awayDisplay + " (" + String.valueOf(gm.getSpread() * -1) + ")</strong>";
                        homeDisplay = homeDisplay + " (+" + String.valueOf(gm.getSpread()) + ")";
                    }
                    else {
                        homeDisplay = "<strong>" + homeDisplay + " (" + String.valueOf(gm.getSpread()) + ")</strong>";
                        awayDisplay = awayDisplay + " (+" + String.valueOf(gm.getSpread() * -1) + ")";
                    }
                }
                
                int selectedTeamId = -1;
                if(userPicks != null && !userPicks.isEmpty()) {
                    Iterator iter2 = userPicks.iterator();
                    while( iter2.hasNext() ) {
                        UserGameXrefModel ugm = (UserGameXrefModel)iter2.next();
                        if(ugm.getGameId() == gm.getId()) {
                            selectedTeamId = ugm.getSelectedTeamId();
                            break;
                        }
                    }
                }
                
                if(lockTeamId == home.getId() || lockTeamId == away.getId()) {
                    lockGameStarted = (gm.getStartDate() <= new java.util.Date().getTime());
                }
                if(survivorTeamId == home.getId() || survivorTeamId == away.getId()) {
                    survivorGameStarted = (gm.getStartDate() <= new java.util.Date().getTime());
                }
                if(gm.getStartDate() > new java.util.Date().getTime()) {
                    lockTeams.put(away.getCity()+away.getName(), away);
                    lockTeams.put(home.getCity()+home.getName(), home);
                    boolean awayFound = false;
                    boolean homeFound = false;
                    Iterator iter2 = allUserSeries.iterator();
                    while( iter2.hasNext() ) {
                        UserSeriesXrefModel usxm = (UserSeriesXrefModel)iter2.next();
                        if( usxm.getSurvivor() == away.getId() ) awayFound = true;
                        if( usxm.getSurvivor() == home.getId() ) homeFound = true;
                        if( awayFound && homeFound) break;
                    }
                    if( awayFound == false ) survivorTeams.put(away.getCity()+away.getName(), away);
                    if( homeFound == false ) survivorTeams.put(home.getCity()+home.getName(), home);
                    allGamesStarted = false;
                }
                
                String weatherLink = "";
                if(home.isDome()) {
                    weatherLink = "<a href=\"javascript:void(0);\" onclick=\"alert('Home team plays in a dome.');\" class=\"smalllabel\" title=\"Click for weather information\">";
                }
                else {
                    weatherLink = "<a href=\"" + home.getWeatherURL() + "\" target=\"new\" class=\"smalllabel\" title=\"Click for weather information\">";
                }
                
                fmt.applyPattern( "EEE, MMM d" );
                dayFmt = fmt.format( new java.util.Date(gm.getStartDate()) );
                if( lastDayFmt.equals(dayFmt) ) dayFmt = "";
                lastDayFmt = fmt.format( new java.util.Date(gm.getStartDate()) );
                
                fmt.applyPattern( "h:mm a" );
                timeFmt = fmt.format( new java.util.Date(gm.getStartDate()) );
                
                gameDisabled = (gm.getStartDate() <= (new java.util.Date().getTime()));
                awaySel = (selectedTeamId == away.getId() ? "CHECKED" : "");
                homeSel = (selectedTeamId == home.getId() ? "CHECKED" : "");
                trClass = (trClass.equals("row1") ? "row2" : "row1");
                %>
                <tr class="<%=trClass%>">
                    <td nowrap="true"><%=dayFmt%>&nbsp;</td>
                    <td nowrap="true" style="text-align:right"><%=timeFmt%>&nbsp;</td>
                    <td nowrap="true"><input type="radio" name="gamePick<%=counter%>" value="<%=away.getId()%>" <%=awaySel%> <%=(gameDisabled ? "DISABLED" : "")%> />&nbsp;<%=awayDisplay%></td>
                    <td style="width:2em; text-align:center">at</td>
                    <td nowrap="true"><input type="radio" name="gamePick<%=counter%>" value="<%=home.getId()%>" <%=homeSel%> <%=(gameDisabled ? "DISABLED" : "")%> />&nbsp;<%=homeDisplay%></td>
                    <input type="hidden" name="gameId<%=counter%>" value="<%=gm.getId()%>"/>
                </tr>
            <%}%>
            <tr>
                <td colspan="6" style="padding-bottom:3em">
                    <span class="smalllabel">Team in BOLD is favored</span><br/>
                </td>
            </tr>
            <tr class="hdr">
                <td colspan="6">Lock of the Week</td>
            </tr>
            <tr style="height:75px; vertical-align:top">
                <td colspan="6" style="padding-top:1em; padding-bottom:1em">
                    <select name="lockPick" style="margin-left:1em; width:18em" <%=(allGamesStarted || lockGameStarted ? "DISABLED" : "")%>>
                        <option value="-1">&lt;no selection&gt;</option>
                        <%
                        TeamModel tm = null;
                        String sSel = "";
                        iter = lockTeams.keySet().iterator();
                        while( iter.hasNext() ) {
                            tm = (TeamModel)lockTeams.get( (String)iter.next() );
                            sSel = (tm.getId() == lockTeamId ? "SELECTED" : "");
                            %>
                            <option value="<%=tm.getId()%>" <%=sSel%>><%=tm.getCity()%>&nbsp;<%=tm.getName()%></option>
                            <%
                        }
                        %>
                    </select>
                </td>
            </tr>
            <tr class="hdr">
                <td colspan="6">Survivor Pool</td>
            </tr>
            <tr>
                <td colspan="6" style="padding-top:1em; padding-bottom:1em">
                    <% if( survivorEliminated && survivorTeamId <= 0 ) { %>
                        <span class="errortext">You have been eliminated from the survivor pool</span>
                    <% } else { %>
                        <table cellspacing="0" cellpadding="2" border="0" style="margin-left:1em">
                            <tr>
                                <td style="border-bottom:0px">
                                    <select name="survivorPick" style="width:18em" <%=(allGamesStarted || survivorGameStarted ? "DISABLED" : "")%>>
                                        <option value="-1">&lt;no selection&gt;</option>
                                        <%
                                        iter = survivorTeams.keySet().iterator();
                                        while( iter.hasNext() ) {
                                            tm = (TeamModel)survivorTeams.get( (String)iter.next() );
                                            if( tm != null ) {
                                                sSel = (tm.getId() == survivorTeamId ? "SELECTED" : "");
                                                %>
                                                <option value="<%=tm.getId()%>" <%=sSel%>><%=(tm.getCity() + " " + tm.getName())%></option>
                                                <%
                                            }
                                        }
                                        %>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td style="border-bottom:0px; padding-top:1.5em">Teams Previously Selected</td>
                            </tr>
                            <tr>
                                <td style="border-bottom:0px">
                                    <select name="teamsUsed" size="3" multiple="Y" style="width:18em">
                                        <%
                                        iter = allUserSeries.iterator();
                                        while( iter.hasNext() ) {
                                            usm = (UserSeriesXrefModel)iter.next();
                                            if( usm.getSeriesId() != selectedSeriesId && usm.getSurvivor() > 0 ) {
                                                SeasonSeriesModel ssm3 = null;
                                                Iterator iter2 = series.iterator();
                                                while( iter2.hasNext() ) {
                                                    ssm3 = (SeasonSeriesModel)iter2.next();
                                                    if( ssm3.getId() == usm.getSeriesId() ) break;
                                                }
                                                tm = (TeamModel)teamMap.get( String.valueOf( usm.getSurvivor() ) );
                                                if( tm != null ) {
                                                    %>
                                                    <option value="<%=tm.getId()%>"><%=tm.getCity()%>&nbsp;<%=tm.getName()%>&nbsp;(<%=season.getSeriesPrefix()%>&nbsp;<%=ssm3.getSequence()%>)</option>
                                                    <%
                                                }
                                            }
                                        }
                                        %>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    <% } %>
                </td>
            </tr>
            <tr>
                <td colspan="6" style="padding:2em">
                    <input type="submit" name="btnSave" value="Save My Changes" class="formbutton" />
                    <input type="checkbox" name="emailPicks" value="Y" style="margin-left:2em" CHECKED />&nbsp;Send me an e-mail confirmation
                </td>
            </tr>
        </table>
    </form>
</div>

<script language="javascript">serverTime("divPickTime", <%=new java.util.Date().getTime()%>);</script>
<jsp:include page="/jsp/mobile/field/page_footer.jsp" />
