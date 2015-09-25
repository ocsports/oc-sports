<%@page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.*" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    DraftSQLController draftSqlCtrlr = new DraftSQLController();
    Collection players = null;
    Collection picks = null;
    String currPick = "";
    int currTeam = 0;
    try {
        players = draftSqlCtrlr.getPlayers(1);
        picks = draftSqlCtrlr.getDraftPicks(0, 0);

        Iterator iter = picks.iterator();
        while( iter.hasNext() ) {
            DraftPick dp = (DraftPick)iter.next();
            if( dp.playerKey == 0 ) {
                currPick = String.valueOf( dp.pickKey );
                currTeam = dp.teamKey;
                break;
            }
        }

    }
    catch(Exception e) {
        throw e;
    }
    finally {
        draftSqlCtrlr.closeConnection();
    }

    String pos = (String)session.getAttribute("playerPos");
    String team = (String)session.getAttribute("teamAbrv");
    if( pos == null ) pos = "";
    if( team == null ) team = "";

    String currPickURL = response.encodeURL(request.getContextPath() + "/nfl_draft/current_pick.jsp");
    String playerListURL = response.encodeURL(request.getContextPath() + "/nfl_draft/player_list.jsp");
    String draftListURL = response.encodeURL(request.getContextPath() + "/nfl_draft/draft_list.jsp");
    String teamRosterURL = response.encodeURL(request.getContextPath() + "/nfl_draft/team_roster.jsp");
    String makePickURL = response.encodeURL(request.getContextPath() + "/nfl_draft/make_selection.jsp");
    String showDraftURL = response.encodeURL(request.getContextPath() + "/nfl_draft/view_draft.jsp");

    String leftViewType = request.getParameter("leftViewType");
    String leftViewURL = draftListURL;
    if( leftViewType != null && leftViewType.equals("ROSTER") )
        leftViewURL = teamRosterURL;
    String switchLeftView = (leftViewURL.equals(draftListURL) ? "ROSTER" : "DRAFT");
    String switchLeftViewDisp = (leftViewURL.equals(draftListURL) ? "View Team Rosters" : "View Draftboard");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Fantasy Baseball 2010</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/draft.css" type="text/css"></link>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/oc-sports.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/draft.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/ajax.js"></script>
</head>
<body>
    <div id="divDraft" style="width:100%">
        <table align="center" style="width:1000px; height:1000px; text-align:center; background-color:#163216">
            <tr valign="top">
                <td>
                    <table width="100%" cellpadding="2">
                        <form name="frmDraft" action="<%=showDraftURL%>" method="GET">
                        <input type="hidden" name="leftViewType" value="<%=leftViewType%>" />
                        <tr valign="top">
                            <td style="text-align:center">
                                <a href="javascript.void(0)" style="font-size:12pt; font-weight:bold; text-decoration:underline; color:white" onclick="document.frmDraft.leftViewType.value='<%=switchLeftView%>'; document.frmDraft.submit(); return false"><%=switchLeftViewDisp%></a><p/>
                                <iframe id="frameDraftList" name="frameDraftList" src="<%=leftViewURL%>" frameborder="0" style="padding:0; margin:0 0 0 0.5em; width:450px; height:550px; border:1px solid darkgreen">
                                    <p><b>This browser does not support iFrame tags!</b>
                                </iframe>
                            </td>
                            <td style="text-align:center">
                                <iframe id="frameCurrentPick" name="frameCurrentPick" src="<%=currPickURL%>" frameborder="0" style="padding:0; margin:0 0 0 0.5em; width:450px; height:120px; border:0px solid darkgreen" onload="refreshDraftLists()">
                                    <p><b>This browser does not support iFrame tags!</b>
                                </iframe>
                                <iframe id="framePlayerList" name="framePlayerList" src="<%=playerListURL%>" frameborder="0" style="padding:0; margin:2em 0 0 0.5em; width:450px; height:430px; border:1px solid darkgreen">
                                    <p><b>This browser does not support iFrame tags!</b>
                                </iframe>
                            </td>
                        </tr>
                        </form>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <div id="divSelection" class="msg">
        <table align="center" cellspacing="0" cellpadding="5" border="0" style="padding:5px; text-align:center">
            <form name="frmCurrPick" action="<%=makePickURL%>" method="GET">
            <input type="hidden" name="pickId" value="<%=currPick%>"/>
            <input type="hidden" name="pickTeam" value="<%=String.valueOf(currTeam)%>"/>
            <tr>
                <td><h4><u>Current Selection</u></h4></td>
            </tr>
            <tr>
                <td><span id="currPickDisp" style="font-size:14pt; color:red"><%=currPick%>. <%=currTeam%></span></td>
            </tr>
            <tr>
                <td style="text-align:left; padding-bottom:1em; border-right:0px">
                    <select name="playerPos" style="font-size:10pt; width:12em" onchange="setPlayerList()">
                        <option value="ALL" style="font-size:10pt">&lt;all players&gt;</option>
                        <option value="C" style="font-size:10pt" <%=(pos.equals("C") ? "SELECTED" : "")%>>Catchers (C)</option>
                        <option value="1B" style="font-size:10pt" <%=(pos.equals("1B") ? "SELECTED" : "")%>>First Basemen (1B)</option>
                        <option value="2B" style="font-size:10pt" <%=(pos.equals("2B") ? "SELECTED" : "")%>>Second Basemen (2B)</option>
                        <option value="3B" style="font-size:10pt" <%=(pos.equals("3B") ? "SELECTED" : "")%>>Third Basemen (3B)</option>
                        <option value="SS" style="font-size:10pt" <%=(pos.equals("SS") ? "SELECTED" : "")%>>Shortstops (SS)</option>
                        <option value="OF" style="font-size:10pt" <%=(pos.equals("OF") ? "SELECTED" : "")%>>Outfielders (OF)</option>
                        <option value="UTIL" style="font-size:10pt" <%=(pos.equals("UTIL") ? "SELECTED" : "")%>>Utility (UTIL)</option>
                        <option value="SP" style="font-size:10pt" <%=(pos.equals("SP") ? "SELECTED" : "")%>>Starting Pitchers (SP)</option>
                        <option value="RP" style="font-size:10pt" <%=(pos.equals("RP") ? "SELECTED" : "")%>>Relief Pitchers (RP)</option>
                    </select>
                    <select name="teamAbrv" style="font-size:10pt; width:8em; margin-left:0.5em" onchange="setPlayerList()">
                        <option value="ALL" style="font-size:10pt">&lt;all teams&gt;</option>
                        <option value="BAL" style="font-size:10pt" <%=(team.equals("BAL") ? "SELECTED" : "")%>>BAL</option>
                        <option value="BOS" style="font-size:10pt" <%=(team.equals("BOS") ? "SELECTED" : "")%>>BOS</option>
                        <option value="NYY" style="font-size:10pt" <%=(team.equals("NYY") ? "SELECTED" : "")%>>NYY</option>
                        <option value="TB" style="font-size:10pt" <%=(team.equals("TB") ? "SELECTED" : "")%>>TB</option>
                        <option value="TOR" style="font-size:10pt" <%=(team.equals("TOR") ? "SELECTED" : "")%>>TOR</option>
                        <option value="ATL" style="font-size:10pt" <%=(team.equals("ATL") ? "SELECTED" : "")%>>ATL</option>
                        <option value="FL" style="font-size:10pt" <%=(team.equals("FL") ? "SELECTED" : "")%>>FL</option>
                        <option value="NYM" style="font-size:10pt" <%=(team.equals("NYM") ? "SELECTED" : "")%>>NYM</option>
                        <option value="PHI" style="font-size:10pt" <%=(team.equals("PHI") ? "SELECTED" : "")%>>PHI</option>
                        <option value="WAS" style="font-size:10pt" <%=(team.equals("WAS") ? "SELECTED" : "")%>>WAS</option>
                        <option value="CHW" style="font-size:10pt" <%=(team.equals("CHW") ? "SELECTED" : "")%>>CHW</option>
                        <option value="CLE" style="font-size:10pt" <%=(team.equals("CLE") ? "SELECTED" : "")%>>CLE</option>
                        <option value="DET" style="font-size:10pt" <%=(team.equals("DET") ? "SELECTED" : "")%>>DET</option>
                        <option value="KC" style="font-size:10pt" <%=(team.equals("KC") ? "SELECTED" : "")%>>KC</option>
                        <option value="MIN" style="font-size:10pt" <%=(team.equals("MIN") ? "SELECTED" : "")%>>MIN</option>
                        <option value="CHC" style="font-size:10pt" <%=(team.equals("CHC") ? "SELECTED" : "")%>>CHC</option>
                        <option value="CIN" style="font-size:10pt" <%=(team.equals("CIN") ? "SELECTED" : "")%>>CIN</option>
                        <option value="HOU" style="font-size:10pt" <%=(team.equals("HOU") ? "SELECTED" : "")%>>HOU</option>
                        <option value="MIL" style="font-size:10pt" <%=(team.equals("MIL") ? "SELECTED" : "")%>>MIL</option>
                        <option value="PIT" style="font-size:10pt" <%=(team.equals("PIT") ? "SELECTED" : "")%>>PIT</option>
                        <option value="STL" style="font-size:10pt" <%=(team.equals("STL") ? "SELECTED" : "")%>>STL</option>
                        <option value="LAA" style="font-size:10pt" <%=(team.equals("LAA") ? "SELECTED" : "")%>>LAA</option>
                        <option value="OAK" style="font-size:10pt" <%=(team.equals("OAK") ? "SELECTED" : "")%>>OAK</option>
                        <option value="SEA" style="font-size:10pt" <%=(team.equals("SEA") ? "SELECTED" : "")%>>SEA</option>
                        <option value="TEX" style="font-size:10pt" <%=(team.equals("TEX") ? "SELECTED" : "")%>>TEX</option>
                        <option value="ARI" style="font-size:10pt" <%=(team.equals("ARI") ? "SELECTED" : "")%>>ARI</option>
                        <option value="COL" style="font-size:10pt" <%=(team.equals("COL") ? "SELECTED" : "")%>>COL</option>
                        <option value="LAD" style="font-size:10pt" <%=(team.equals("LAD") ? "SELECTED" : "")%>>LAD</option>
                        <option value="SD" style="font-size:10pt" <%=(team.equals("SD") ? "SELECTED" : "")%>>SD</option>
                        <option value="SF" style="font-size:10pt" <%=(team.equals("SF") ? "SELECTED" : "")%>>SF</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <select name="playerId" size="12" style="width:22em">
                    </select>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="button" name="btnSave" value="Make Selection" class="yellowbtn" onclick="return makePickSelection()"/>
                    <input type="button" name="btnCancel" value="Cancel" class="yellowbtn" onclick="return cancelSelection()" />
                </td>
            </tr>
            </form>
        </table>
    </div>
    <div id="divBlockScreen" class="blockScreen">&nbsp;</div>
</body>
<script language="javascript">
<!--
var pageCurrPick = "<%=currPick%>";
var pickSelectionURL = "<%=makePickURL%>";

var players = new Array();
<%
if( players != null ) {
    Iterator iter = players.iterator();
    int i = 0;
    String dataList = "";
    while( iter.hasNext() ) {
        DraftPlayer dp = (DraftPlayer)iter.next();
        if( dp.pickKey <= 0 ) {
            dataList = dp.pickKey + "|" + dp.playerKey + "|" + dp.rank + "|" + dp.lname + "|" + dp.fname + "|" + dp.pos + "|" + dp.teamAbrv;
            out.print("players[" + i + "] = \"" + dataList + "\";\n");
            i++;
        }
    }
}
%>

function setPlayerList() {
    oForm = document.frmCurrPick;

    //clear the list
    oForm.playerId.options.length = 0;

    selPos = oForm.playerPos.options[oForm.playerPos.selectedIndex].value.toUpperCase();
    selTeam = oForm.teamAbrv.options[oForm.teamAbrv.selectedIndex].value.toUpperCase();

    for(i=0; i < players.length; i++) {
        dataElem = players[i].split("|");
        showPlayer = true;

        if( selectedPlayerKeys.indexOf("," + dataElem[1] + ",") >= 0 )
            showPlayer = false;
        if( selPos != "ALL" && dataElem[5].toUpperCase().indexOf(selPos) == -1 )
            showPlayer = false;
        if( selTeam != "ALL" && dataElem[6].toUpperCase() != selTeam )
            showPlayer = false;

        if( showPlayer ) {
            pValue = dataElem[1];
            pText = dataElem[2] + ".  " + dataElem[4] + " " + dataElem[3] + " (" + dataElem[6] + " - " + dataElem[5] + ")";
            oForm.playerId.options[oForm.playerId.options.length] = new Option(pText, pValue);
        }
    }
}

function makePickSelection() {
    hideDiv("divSelection");
    document.frmCurrPick.submit();

    //var pickId = document.frmCurrPick.pickId.value;
    //var playerId = document.frmCurrPick.playerId.value;
    //var newUrl = pickSelectionURL + "?pickId=" + pickId + "&playerId=" + playerId;
    //oFrame = document.getElementById('frameCurrentPick');
    //oFrame.src = newUrl;
}

refreshTimer = window.setTimeout("window.location.reload()", 30000);

function cancelSelection() {
    hideDiv('divSelection');
    refreshTimer = window.setTimeout("window.location.reload()", 30000);
}

// -->
</script>
</html>
