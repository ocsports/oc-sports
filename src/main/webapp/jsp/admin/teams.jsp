<%@ page contentType="text/html"%>
<%@ page import="java.util.*" %>
<%@ page import="com.ocsports.models.TeamModel" %>
<%@ page import="com.ocsports.models.TeamDivisionModel" %>
<%
    Collection divisions = (Collection)request.getAttribute("TeamDivisionModels");
    Collection teams = (Collection)request.getAttribute("TeamModels");
    Collection errors = (Collection)request.getAttribute("errors");
    
    if( divisions == null ) divisions = new ArrayList();
    if( teams == null ) teams = new ArrayList();
    
    String saveTeamURL = response.encodeURL( "goAdmin?r=saveTeam" );
%>

<h2>NFL Teams</h2>
<div>
    <table cellspacing="0" cellpadding="5" border="0" class="trtable" style="padding:0; font-size:8pt; width:100%">
        <tr class="hdr">
            <td>Name</td>
            <td>Division</td>
            <td style="text-align:center">Dome</td>
            <td style="text-align:center">Turf</td>
            <td>Weather URL</td>
        </tr>
        <%
        String trClass = "";
        String divDisplay = "";
        Iterator iter = teams.iterator();
        while( iter.hasNext() ) {
            TeamModel tm = (TeamModel)iter.next();
            divDisplay = "";
            Iterator iter2 = divisions.iterator();
            while( iter2.hasNext() ) {
                TeamDivisionModel tdm = (TeamDivisionModel)iter2.next();
                if( tdm.getId() == tm.getDivision() ) {
                    divDisplay = tdm.getName();
                    break;
                }
            }
            trClass = (trClass.equals("row1") ? "row2" : "row1");
            %>
            <tr class="<%=trClass%>">
                <td><a href="#" title="ID: <%=tm.getId()%>" onclick="return showTeamDetail('<%=tm.getId()%>', '<%=tm.getAbrv()%>', '<%=tm.getCity()%>', '<%=tm.getName()%>', '<%=tm.getDivision()%>', '<%=tm.getWeatherURL()%>', '<%=(tm.isDome() ? "Y" : "N")%>', '<%=(tm.isTurf() ? "Y" : "N")%>')">(<%=tm.getAbrv()%>)&nbsp;<%=tm.getCity()%>&nbsp;<%=tm.getName()%></a></td>
                <td><%=divDisplay%>&nbsp;</td>
                <td style="text-align:center"><%=(tm.isDome() ? "<b>Yes</b>" : "No")%></td>
                <td style="text-align:center"><%=(tm.isTurf() ? "<b>Yes</b>" : "No")%></td>
                <td><%=tm.getWeatherURL()%>&nbsp;</td>
            </tr>
            <%
        }%>
    </table>
</div>
<div id="divTeam" class="msg">
    <form name="frmTeam" action="<%=saveTeamURL%>" method="POST">
        <input type="hidden" name="teamId" value="-1" />
        <table cellspacing="0" cellpadding="2" border="0">
            <tr>
                <td>Abrv</td>
                <td><input type="text" name="abrv" maxlength="3" style="width:4em" /></td>
            </tr>
            <tr>
                <td>City</td>
                <td><input type="text" name="city" maxlength="30" style="width:20em" /></td>
            </tr>
            <tr>
                <td>Name</td>
                <td><input type="text" name="nm" maxlength="30" style="width:20em" /></td>
            </tr>
            <tr>
                <td>Division</td>
                <td>
                    <select name="div" style="width:15em">
                        <%
                        Iterator iter2 = divisions.iterator();
                        while( iter2.hasNext() ) {
                            TeamDivisionModel tdm = (TeamDivisionModel)iter2.next();
                            %>
                            <option value="<%=tdm.getId()%>"><%=tdm.getName()%></option>
                            <%
                        }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Weather</td>
                <td><input type="text" name="weather" maxlength="60" style="width:25em" /></td>
            </tr>
            <tr valign="top">
                <td>&nbsp;</td>
                <td><input type="checkbox" name="dome" value="Y" /><span style="padding-left:0.25em">Home field is indoors ?</span></td>
            </tr>
            <tr valign="top">
                <td>&nbsp;</td>
                <td><input type="checkbox" name="turf" value="Y" /><span style="padding-left:0.25em">Home field is artificial ?</span></td>
            </tr>
            <%
            if( errors != null && !errors.isEmpty() ) {
                String errMsg = "";
                Iterator iterPlayer = errors.iterator();
                while( iterPlayer.hasNext() ) {
                    errMsg += (String)iterPlayer.next() + "<br/>";
                }
                %>
                <tr>
                    <td colspan="2" class="errortext"><%=errMsg%></td>
                </tr>
                <%
            }
            %>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Save" onclick="hideDiv('divTeam')" class="formbutton" />
                    <input type="button" value="Cancel" onclick="hideDiv('divTeam')" class="formbutton2" />
                </td>
            </tr>
        </table>
    </form>
</div>
