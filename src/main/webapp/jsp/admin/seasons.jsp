<%@ page contentType="text/html" %>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.core.SportTypes" %>
<%@ page import="com.ocsports.models.SeasonModel" %>
<%@ page import="com.ocsports.models.SeasonSeriesModel" %>
<%@ page import="com.ocsports.models.GameModel" %>
<%@ page import="com.ocsports.models.TeamModel" %>
<%@ page import="com.ocsports.servlets.AdminServlet" %>

<%
    int seasonId = Integer.parseInt( (String)request.getAttribute("seasonId") );
    int seriesId = Integer.parseInt( (String)request.getAttribute("seriesId") );
    Collection seasons = (Collection)request.getAttribute("SeasonModels");
    Collection series  = (Collection)request.getAttribute("SeriesModels");
    Collection games   = (Collection)request.getAttribute("GameModels");
    Collection errors  = (Collection)request.getAttribute("errors");
    HashMap teamMap = (HashMap)session.getAttribute( "teamMap" );
    
    Iterator iter = null;
    SimpleDateFormat fmt = new SimpleDateFormat();
    String seriesName = "";
    
    String seasonsURL = response.encodeURL("goAdmin?r=seasons");
    String saveSeasonURL = response.encodeURL("goAdmin?r=saveSeason");
    String saveSeriesURL = response.encodeURL("goAdmin?r=saveSeries");
    String saveGameURL = response.encodeURL("goAdmin?r=saveGame");
%>

<h2>Season List</h2>
<div>
    <table cellspacing="0" cellpadding="4" border="0" class="trtable" style="padding:0; width:100%">
        <form name="frmList" action="<%=seasonsURL%>" method="POST">
            <input type="hidden" name="seasonId" value="<%=seasonId%>" />
            <input type="hidden" name="seriesId" value="<%=seriesId%>" />
            <tr class="hdr">
                <td style="width:2em">&nbsp;</td>
                <td width="100%">&nbsp;</td>
            </tr>
            <%
            if( seasons != null && !seasons.isEmpty() ) {
                String trClass = "";
                String trStyle = "";
                String seasonImg = "";
                Iterator seasonIter = seasons.iterator();
                while( seasonIter.hasNext() ) {
                    SeasonModel sm = (SeasonModel)seasonIter.next();
                    seasonImg = ( sm.getId() == seasonId ? "minus.gif" : "plus.gif" );
                    trClass = ( trClass.equals("row1") ? "row2" : "row1" );
                    trStyle = ( sm.isActive() ? "font-weight:bold" : "color:#FF0000" );
                    %>
                    <tr class="<%=trClass%>" style="<%=trStyle%>">
                        <td style="width:2em; text-align:center"><a href="#" onclick="return changeSeason('<%=sm.getId()%>'); return false;"><img src="<%=request.getContextPath()%>/images/<%=seasonImg%>" border="0"/></a></td>
                        <td style="width:100%"><a href="#" title="ID: <%=sm.getId()%>" onclick="return showSeason('<%=sm.getId()%>', '<%=sm.getName()%>', '<%=sm.getSportType()%>', '<%=sm.getSeriesPrefix()%>', '<%=(sm.isActive() ? "1" : "0")%>')"><%=sm.getName()%></a><%=(sm.isActive() ? "" : " - Inactive")%></td>
                    </tr>
                    <%
                    if( seasonId == sm.getId() && series != null && !series.isEmpty() ) {
                        %>
                        <tr>
                            <td colspan="2">
                                <table width="100%" cellspacing="0" cellpadding="2" border="0" class="trtable">
                                    <%
                                    Iterator seriesIter = series.iterator();
                                    String trClass2 = "";
                                    String seriesImg = "";
                                    String seriesDisplay = "";
                                    while( seriesIter.hasNext() ) {
                                        SeasonSeriesModel ssm = (SeasonSeriesModel)seriesIter.next();
                                        trClass2 = ( trClass2.equals("row1") ? "row2" : "row1" );
                                        seriesImg = ( ssm.getId() == seriesId ? "minus.gif" : "plus.gif" );
                                        fmt.applyPattern( "MMM d, yyyy" );
                                        seriesDisplay = sm.getSeriesPrefix() + " " + ssm.getSequence() + " (" + fmt.format( new java.util.Date(ssm.getStartDate()) ) + " - " + fmt.format( new java.util.Date(ssm.getEndDate()) ) + ")";
                                        fmt.applyPattern( "MM/dd/yyyy" );
                                        %>
                                        <tr>
                                            <td style="width:2em; border:0"><a href="#" onclick="changeSeries('<%=ssm.getId()%>'); return false"><img src="<%=request.getContextPath()%>/images/<%=seriesImg%>" border="0"></a></td>
                                            <td style="width:100%; border:0"><a href="#" title="ID: <%=ssm.getId()%>" onclick="return showSeries('<%=ssm.getId()%>','<%=ssm.getSeasonId()%>','<%=fmt.format(new java.util.Date(ssm.getStartDate()))%>','<%=fmt.format(new java.util.Date(ssm.getEndDate()))%>','<%=(ssm.isSpreadPublished() ? "1" : "0")%>','<%=(ssm.isUserCleanup() ? "1" : "0")%>','<%=(ssm.isReminderEmail() ? "1" : "0")%>')"><%=seriesDisplay%></a></td>
                                        </tr>
                                        <%
                                        if( seriesId == ssm.getId() && games != null && !games.isEmpty() ) {
                                            %>
                                            <tr>
                                                <td colspan="2">
                                                    <table width="100%" cellspacing="0" cellpadding="2" border="0" class="trtable">
                                                        <tr class="hdr">
                                                            <td>Kickoff</td>
                                                            <td>Teams</td>
                                                            <td style="text-align:center">Spread</td>
                                                            <td>Score</td>
                                                        </tr>
                                                        <%
                                                        Iterator gamesIter = games.iterator();
                                                        String trClass3 = "";
                                                        while( gamesIter.hasNext() ) {
                                                            GameModel gm = (GameModel)gamesIter.next();
                                                            TeamModel homeTeam = (TeamModel)teamMap.get( String.valueOf(gm.getHomeTeamId()) );
                                                            TeamModel awayTeam = (TeamModel)teamMap.get( String.valueOf(gm.getAwayTeamId()) );
                                                            float spread = gm.getSpread();
                                                            fmt.applyPattern( "M/d EEE, h:mm a" );
                                                            String gameStart = fmt.format( new java.util.Date(gm.getStartDate()) );
                                                            String gameSpread = "--";
                                                            String gameScore = "Not Posted";
                                                            if( gm.getSpread() != 0 ) {
                                                                gameSpread = (spread < 0 ? homeTeam.getAbrv() + " (" + spread + ")" : awayTeam.getAbrv() + " (" + (spread*-1) + ")" );
                                                            }
                                                            if( gm.isPosted() ) {
                                                                int homeScore = gm.getHomeScore();
                                                                int awayScore = gm.getAwayScore();
                                                                if( homeScore == awayScore )
                                                                    gameScore = "Tied " + awayScore + " - " + homeScore;
                                                                else if( homeScore > awayScore )
                                                                    gameScore = homeTeam.getAbrv() + " " + homeScore + " - " + awayScore;
                                                                else
                                                                    gameScore = awayTeam.getAbrv() + " " + awayScore + " - " + homeScore;
                                                                    
                                                            }
                                                            fmt.applyPattern( "MM/dd/yyyy h:mm a" );
                                                            %>
                                                            <tr class="row2">
                                                                <td style="border:0"><%=gameStart%></td>
                                                                <td style="border:0"><a href="#" title="ID: <%=gm.getId()%>" onclick="return showGame('<%=gm.getId()%>', '<%=fmt.format( new java.util.Date(gm.getStartDate()) )%>', '<%=gm.getAwayTeamId()%>', '<%=gm.getHomeTeamId()%>', '<%=gm.getSpread()%>', '<%=gm.getAwayScore()%>', '<%=gm.getHomeScore()%>', '<%=(gm.isPosted() ? "1": "0")%>')"><%=awayTeam.getCity() + " " + awayTeam.getName()%> at <%=homeTeam.getCity() + " " + homeTeam.getName()%></a></td>
                                                                <td style="border:0; text-align:center"><%=gameSpread%></td>
                                                                <td style="border:0"><%=gameScore%></td>
                                                            </tr>
                                                            <%
                                                        }
                                                        %>
                                                    </table>
                                                </td>
                                            </tr>
                                            <%
                                        }
                                    }
                                    %>
                                </table>
                            </td>
                        </tr>
                        <%
                    }
                }
            }
            %>
        </form>
    </table>
</div>
<div id="divSeason" class="msg">
    <form name="frmSeason" action="<%=saveSeasonURL%>" method="POST">
        <input type="hidden" name="updateSeasonId" value="-1" />
        <input type="hidden" name="seasonId" value="<%=seasonId%>" />
        <input type="hidden" name="seriesId" value="<%=seriesId%>" />
        <table cellspacing="0" cellpadding="5" border="0">
            <tr>
                <td>Season Name</td>
                <td><input type="text" name="name" maxlength="30" style="width:8em"></td>
            </tr>
            <tr>
                <td>Series Prefix</td>
                <td><input type="text" name="prefix" maxlength="30" style="width:8em"></td>
            </tr>
            <tr>
                <td>Sport Type</td>
                <td>
                    <select name="sportType" style="width:15em">
                        <option value="<%=SportTypes.TYPE_NFL_FOOTBALL%>" SELECTED>NFL Football</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="checkbox" name="act" CHECKED>&nbsp;Active</td>
            </tr>
            <%
            if( errors != null && !errors.isEmpty() ) {
                Iterator errorsIter = errors.iterator();
                while( errorsIter.hasNext() ) {
                    %>
                    <tr>
                        <td colspan="2" class="errortext"><%=(String)errorsIter.next()%></td>
                    </tr>
                    <%
                }
            }
            %>
            <tr>
                <td colspan="2" style="text-align:left; padding-top:0.5em; padding-bottom:1em">
                    <input type="submit" name="btnSeasonSave" value="Save" onclick="hideDiv('divSeason')" class="formbutton" />
                    <input type="button" name="btnSeasonCancel" value="Cancel" onclick="hideDiv('divSeason')" class="formbutton" />
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="divSeries" class="msg">
    <form name="frmSeries" action="<%=saveSeriesURL%>" method="POST">
        <input type="hidden" name="updateSeasonId" value="-1" />
        <input type="hidden" name="updateSeriesId" value="-1" />
        <input type="hidden" name="seasonId" value="<%=seasonId%>" />
        <input type="hidden" name="seriesId" value="<%=seriesId%>" />
        <table cellspacing="0" cellpadding="5" border="0">
            <tr>
                <td>Start Date</td>
                <td><input type="text" name="startDate" maxlength="30" style="width:8em; text-align:center"></td>
            </tr>
            <tr>
                <td>End Date</td>
                <td><input type="text" name="endDate" maxlength="30" style="width:8em; text-align:center"></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <input type="checkbox" name="pub" CHECKED>&nbsp;Spreads Published?
                    <br/><input type="checkbox" name="cleanup" CHECKED>&nbsp;Series Cleanup Ran Successfully?
                    <br/><input type="checkbox" name="remind" CHECKED>&nbsp;Reminder Emails Sent?
                </td>
            </tr>
            <%
            if( errors != null && !errors.isEmpty() ) {
                Iterator errorsIter = errors.iterator();
                while( errorsIter.hasNext() ) {
                    %>
                    <tr>
                        <td colspan="2" class="errortext"><%=(String)errorsIter.next()%></td>
                    </tr>
                    <%
                }
            }
            %>
            <tr>
                <td colspan="2" style="text-align:left; padding-top:0.5em; padding-bottom:1em">
                    <input type="submit" name="btnSeriesSave" value="Save" onclick="hideDiv('divSeries')" class="formbutton" />
                    <input type="button" name="btnSeriesCancel" value="Cancel" onclick="hideDiv('divSeries')" class="formbutton" />
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="divGame" class="msg">
    <form name="frmGame" action="<%=saveGameURL%>" method="POST">
        <input type="hidden" name="seasonId" value="<%=seasonId%>" />
        <input type="hidden" name="seriesId" value="<%=seriesId%>" />
        <input type="hidden" name="gameId" value="-1" />
        <table cellspacing="0" cellpadding="5" border="0">
            <tr>
                <td>Kickoff</td>
                <td><input type="text" name="start" style="width:12em; text-align:center"></td>
            </tr>
            <tr>
                <td>Away Team</td>
                <td>
                    <select name="awayTeam" style="width:15em">
                        <%
                        Iterator teamIter = teamMap.values().iterator();
                        while( teamIter.hasNext() ) {
                            TeamModel gtm = (TeamModel)teamIter.next();
                            %>
                            <option value="<%=gtm.getId()%>"><%=gtm.getCity() + " " + gtm.getName()%></option>
                            <%
                        }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Home Team</td>
                <td>
                    <select name="homeTeam" style="width:15em">
                        <%
                        teamIter = teamMap.values().iterator();
                        while( teamIter.hasNext() ) {
                            TeamModel gtm = (TeamModel)teamIter.next();
                            %>
                            <option value="<%=gtm.getId()%>"><%=gtm.getCity() + " " + gtm.getName()%></option>
                            <%
                        }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Spread</td>
                <td><input type="text" name="spread" style="width:5em; text-align:center"></td>
            </tr>
            <tr>
                <td>Score</td>
                <td>
                    <table cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>Away</td>
                            <td style="padding-left:2em">Home</td>
                        </tr>
                        <tr>
                            <td><input type="text" name="awayScore" style="width:5em; text-align:center"></td>
                            <td style="padding-left:2em"><input type="text" name="homeScore" style="width:5em; text-align:center"></td>
                        </tr>
                        <tr>
                            <td colspan="2"><input type="checkbox" name="posted" value="Y">&nbsp;Post Score?</td>
                        </tr>
                    </table>
                </td>
            </tr>
            <%
            if( errors != null && !errors.isEmpty() ) {
                Iterator errorsIter = errors.iterator();
                while( errorsIter.hasNext() ) {
                    %>
                    <tr>
                        <td colspan="2" class="errortext"><%=(String)errorsIter.next()%></td>
                    </tr>
                    <%
                }
            }
            %>
            <tr>
                <td colspan="2" style="text-align:left; padding-top:0.5em; padding-bottom:1em">
                    <input type="submit" name="btnGameSave" value="Save" onclick="hideDiv('divGame')" class="formbutton" />
                    <input type="button" name="btnGameCancel" value="Cancel" onclick="hideDiv('divGame')" class="formbutton" />
                </td>
            </tr>
        </table>
    </form>
</div>
