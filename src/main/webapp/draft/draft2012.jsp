<%@page contentType="text/html"%>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.*" %>
<%@ page import="com.ocsports.sql.DraftSQLController" %>
<%
    String playerPos = request.getParameter("playerPos");
    String playerTeam = request.getParameter("playerTeam");
    String playerName = request.getParameter("playerName");
    String hideSelected = request.getParameter("hideSelected");
    String rosterTeam = request.getParameter("rosterTeam");
    String screenName = request.getParameter("screenName");
	if( playerPos == null || playerPos.length() == 0 ) playerPos = (String)session.getAttribute("playerPos");
    if( playerTeam == null || playerTeam.length() == 0 ) playerTeam = (String)session.getAttribute("playerTeam");
    if( playerName == null || playerName.length() == 0 ) playerName = (String)session.getAttribute("playerName");
    if( hideSelected == null || hideSelected.length() == 0 ) hideSelected = (String)session.getAttribute("hideSelected");
    if( rosterTeam == null || rosterTeam.length() == 0 ) rosterTeam = (String)session.getAttribute("rosterTeam");
    if( screenName == null || screenName.length() == 0 ) screenName = (String)session.getAttribute("screenName");
    if( playerPos == null ) playerPos = "";
    if( playerTeam == null ) playerTeam = "";
    if( playerName == null ) playerName = "";
    if( hideSelected == null ) hideSelected = "";
    if( rosterTeam == null ) rosterTeam = "";
    if( screenName == null ) screenName = "";

    Iterator iter;
    int currPick = 0;
    int currTeam = 0;
    String currTeamName = "";
    Date lastUpdate = new java.util.Date();

    DraftSQLController draftSqlCtrlr = new DraftSQLController();
    Collection allPlayers = draftSqlCtrlr.getPlayers(1);
    Collection picks = draftSqlCtrlr.getDraftPicks(0, 0);
    Collection chat = draftSqlCtrlr.getChat();
    Collection teams = draftSqlCtrlr.getTeams();
    int lastChat = draftSqlCtrlr.getLastChat();

    iter = picks.iterator();
    while( iter.hasNext() ) {
        DraftPick dp = (DraftPick)iter.next();
        if( dp.playerKey == 0 ) {
            currPick = dp.pickKey;
            currTeam = dp.teamKey;
            currTeamName = dp.pickTeam;
            break;
        }
    }

    if( rosterTeam == "" ) rosterTeam = (String)((ArrayList)teams).get(0);

	session.setAttribute("playerPos", playerPos);
	session.setAttribute("playerTeam", playerTeam);
	session.setAttribute("playerName", playerName);
	session.setAttribute("hideSelected", hideSelected);
	session.setAttribute("rosterTeam", rosterTeam);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Fantasy Baseball 2012</title>
    <link rel="stylesheet" href="../css/draft.css" type="text/css"></link>
</head>
<body>

<div id="divDraft" style="width:100%">
	<table cellspacing="20">
		<tr style="vertical-align:top">
			<td>
				<div id="divDraftList" class="win" style="width:500px; height:400px; overflow:auto">
					<table width="99%" cellpadding="0" cellspacing="0" class="trtable">
						<tr class="hdr">
                            <td>Round</td>
							<td>Pick</td>
							<td>Team</td>
							<td>Player Selected</td>
						</tr>
						<%
						iter = picks.iterator();
						String playerDisp = "";
						String trStyle = "";
						while( iter.hasNext() ) {
							DraftPick dp = (DraftPick)iter.next();
							playerDisp = "&nbsp;";
							trStyle = "";
							if( dp.pickKey == currPick ) {
								trStyle = "background-color:#EEEEEE; color:#0000AA; font-size:16pt; font-weight:bold; vertical-align:middle";
								playerDisp = "<input type='button' name='btnMakePick' value='SELECT YOUR PLAYER' class='yellowbtn' onclick='makeDraftSelection();' />";
							}
							else if( dp.playerKey > 0 ) {
								playerDisp = dp.selectedPlayer.fname + " " + dp.selectedPlayer.lname + " (" + dp.selectedPlayer.teamAbrv + " - " + dp.selectedPlayer.pos + ")";
								trStyle = "vertical-align:middle; color:#00AA00; font-weight:bold";
							}
							%>
							<tr class="det" style="height:20px; <%=trStyle%>">
                                <td>Rd <%=dp.pickRound%><a name="draftPick<%=dp.pickKey%>" /></td>
								<td><%=dp.pickKey%>.</td>
								<td><%=dp.pickTeam%></td>
								<td><%=playerDisp%></td>
							</tr>
							<%
						}
						%>
					</table>
				</div>
				<div id="divChat" class="win" style="margin-top:2em; width:500px">
                    <div id="divChatMsgs" style="padding:3px; margin-bottom:0.5em; background-color:#DDDDDD; font-size:9pt; border:1px solid #BBBBBB; width:490px; height:150px; overflow:auto">
                        <%
                        iter = chat.iterator();
                        while( iter.hasNext() ) {
                            DraftChat dc = (DraftChat)iter.next();
                            %>
                            <b><%=dc.teamName%></b>&nbsp;&nbsp;&nbsp;<%=dc.msg%><br/>
                            <%
                        }
                        %>
                    </div>
                    <form name="frmChat" action="add_chat.jsp" method="POST">
                        <table>
                            <tr>
                                <td>
                                    <input type="text" name="msg" value="" style="font-size:8pt; width:400px; text-align:left" />
                                    <input type="button" name="btnGo" value="send" onclick="document.frmChat.submit()" style="font-size:8pt" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label for="screenName" style="font-size:7pt; margin-right:0.5em">Your name:</label>
                                    <input id="screenName" type="text" name="screenName" value="<%=screenName%>" onchange="setScreenHandle(this)" style="font-size:7pt; width:15em; text-align:left" />
                                </td>
                            </tr>
                        </table>
                    </form>
				</div>
			</td>
			<td>
				<div id="divPlayers" class="win" style="width:450px">
                    <table cellspacing="0" cellpadding="5">
                        <tr>
                            <td style="text-align:left; padding-bottom:1em; border-right:0px">
                                <select id="playerPos" name="playerPos" style="font-size:10pt; width:12em" onchange="setPlayerList('POS')">
                                    <option value="ALL" style="font-size:10pt">&lt;all players&gt;</option>
                                    <option value="C" style="font-size:10pt" <%=(playerPos.equals("C") ? "SELECTED" : "")%>>Catchers (C)</option>
                                    <option value="1B" style="font-size:10pt" <%=(playerPos.equals("1B") ? "SELECTED" : "")%>>First Basemen (1B)</option>
                                    <option value="2B" style="font-size:10pt" <%=(playerPos.equals("2B") ? "SELECTED" : "")%>>Second Basemen (2B)</option>
                                    <option value="3B" style="font-size:10pt" <%=(playerPos.equals("3B") ? "SELECTED" : "")%>>Third Basemen (3B)</option>
                                    <option value="SS" style="font-size:10pt" <%=(playerPos.equals("SS") ? "SELECTED" : "")%>>Shortstops (SS)</option>
                                    <option value="OF" style="font-size:10pt" <%=(playerPos.equals("OF") ? "SELECTED" : "")%>>Outfielders (OF)</option>
                                    <option value="UTIL" style="font-size:10pt" <%=(playerPos.equals("UTIL") ? "SELECTED" : "")%>>Utility (UTIL)</option>
                                    <option value="SP" style="font-size:10pt" <%=(playerPos.equals("SP") ? "SELECTED" : "")%>>Starting Pitchers (SP)</option>
                                    <option value="RP" style="font-size:10pt" <%=(playerPos.equals("RP") ? "SELECTED" : "")%>>Relief Pitchers (RP)</option>
                                </select>
                                <select id="playerTeam" name="playerTeam" style="font-size:10pt; width:8em; margin-left:0.5em" onchange="setPlayerList('TEAM')">
                                    <option value="ALL" style="font-size:10pt">&lt;all teams&gt;</option>
                                    <option value="ATL" style="font-size:10pt" <%=(playerTeam.equals("ATL") ? "SELECTED" : "")%>>ATL</option>
                                    <option value="ARI" style="font-size:10pt" <%=(playerTeam.equals("ARI") ? "SELECTED" : "")%>>ARI</option>
                                    <option value="BAL" style="font-size:10pt" <%=(playerTeam.equals("BAL") ? "SELECTED" : "")%>>BAL</option>
                                    <option value="BOS" style="font-size:10pt" <%=(playerTeam.equals("BOS") ? "SELECTED" : "")%>>BOS</option>
                                    <option value="CHC" style="font-size:10pt" <%=(playerTeam.equals("CHC") ? "SELECTED" : "")%>>CHC</option>
                                    <option value="CHW" style="font-size:10pt" <%=(playerTeam.equals("CHW") ? "SELECTED" : "")%>>CHW</option>
                                    <option value="CIN" style="font-size:10pt" <%=(playerTeam.equals("CIN") ? "SELECTED" : "")%>>CIN</option>
                                    <option value="CLE" style="font-size:10pt" <%=(playerTeam.equals("CLE") ? "SELECTED" : "")%>>CLE</option>
                                    <option value="COL" style="font-size:10pt" <%=(playerTeam.equals("COL") ? "SELECTED" : "")%>>COL</option>
                                    <option value="DET" style="font-size:10pt" <%=(playerTeam.equals("DET") ? "SELECTED" : "")%>>DET</option>
                                    <option value="FL" style="font-size:10pt"  <%=(playerTeam.equals("FL") ? "SELECTED" : "")%>>FL</option>
                                    <option value="HOU" style="font-size:10pt" <%=(playerTeam.equals("HOU") ? "SELECTED" : "")%>>HOU</option>
                                    <option value="KC" style="font-size:10pt"  <%=(playerTeam.equals("KC") ? "SELECTED" : "")%>>KC</option>
                                    <option value="LAA" style="font-size:10pt" <%=(playerTeam.equals("LAA") ? "SELECTED" : "")%>>LAA</option>
                                    <option value="LAD" style="font-size:10pt" <%=(playerTeam.equals("LAD") ? "SELECTED" : "")%>>LAD</option>
                                    <option value="MIL" style="font-size:10pt" <%=(playerTeam.equals("MIL") ? "SELECTED" : "")%>>MIL</option>
                                    <option value="MIN" style="font-size:10pt" <%=(playerTeam.equals("MIN") ? "SELECTED" : "")%>>MIN</option>
                                    <option value="NYM" style="font-size:10pt" <%=(playerTeam.equals("NYM") ? "SELECTED" : "")%>>NYM</option>
                                    <option value="NYY" style="font-size:10pt" <%=(playerTeam.equals("NYY") ? "SELECTED" : "")%>>NYY</option>
                                    <option value="OAK" style="font-size:10pt" <%=(playerTeam.equals("OAK") ? "SELECTED" : "")%>>OAK</option>
                                    <option value="PHI" style="font-size:10pt" <%=(playerTeam.equals("PHI") ? "SELECTED" : "")%>>PHI</option>
                                    <option value="PIT" style="font-size:10pt" <%=(playerTeam.equals("PIT") ? "SELECTED" : "")%>>PIT</option>
                                    <option value="SD" style="font-size:10pt"  <%=(playerTeam.equals("SD") ? "SELECTED" : "")%>>SD</option>
                                    <option value="SEA" style="font-size:10pt" <%=(playerTeam.equals("SEA") ? "SELECTED" : "")%>>SEA</option>
                                    <option value="SF" style="font-size:10pt"  <%=(playerTeam.equals("SF") ? "SELECTED" : "")%>>SF</option>
                                    <option value="STL" style="font-size:10pt" <%=(playerTeam.equals("STL") ? "SELECTED" : "")%>>STL</option>
                                    <option value="TB" style="font-size:10pt"  <%=(playerTeam.equals("TB") ? "SELECTED" : "")%>>TB</option>
                                    <option value="TEX" style="font-size:10pt" <%=(playerTeam.equals("TEX") ? "SELECTED" : "")%>>TEX</option>
                                    <option value="TOR" style="font-size:10pt" <%=(playerTeam.equals("TOR") ? "SELECTED" : "")%>>TOR</option>
                                    <option value="WAS" style="font-size:10pt" <%=(playerTeam.equals("WAS") ? "SELECTED" : "")%>>WAS</option>
                                </select>
                                <input type="text" id="playerName" name="playerName" value="<%=playerName%>" style="width:8em" onchange="setPlayerList('NAME')" />
                                <br/>
                                <span style="padding-left:2em">
                                    <input type="checkbox" id="hideSelected" name="hideSelected" value="Y" onclick="setPlayerList('SEL')" <%=(hideSelected.equals("Y") ? "CHECKED" : "")%>><label for="hideSelected" style="margin-left:0.5em">Hide selected</label>
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <select id="playerList" name="playerList" size="12" style="width:425px" onclick="highlightPlayer()">
                                </select>
                            </td>
                        </tr>
                    </table>
				</div>
				<div id="divRoster" class="win" style="margin-top:2em; width:450px">
                    <div>
                        <select id="rosterTeamKey" name="rosterTeamKey" style="font-size:10pt; width:20em" onchange="setTeamRoster(1)">
                            <%
                            iter = teams.iterator();
                            while( iter.hasNext() ) {
                                String teamName = (String)iter.next();
                                String sel = (teamName.equals(rosterTeam) ? "SELECTED" : "");
                                %>
                                <option value="<%=teamName%>" <%=sel%>><%=teamName%></option>
                                <%
                            }
                            %>
                        </select>
                    </div>
                    <div id="divRosterList" style="margin-top:0.5em">
                    </div>
				</div>
			</td>
		</tr>
	</table>
</div>
<div style="display:none">
    <form name="frmMakePick" action="make_selection.jsp" method="POST">
        <input type="hidden" name="currPick" value="<%=currPick%>" />
        <input type="hidden" name="currTeam" value="<%=currTeam%>" />
        <input type="hidden" name="currTeamName" value="<%=currTeamName%>" />
        <input type="hidden" name="playerKey" value="0" />
    </form>
</div>

<script language="javascript">
    var currPick = <%=currPick%>;
    var lastChat = <%=lastChat%>;
    var rosterPos = new Array("C", "1B", "2B", "3B", "SS", "OF", "OF", "OF", "UTIL", "SP", "SP", "SP", "RP", "RP", "P", "P", "BN", "BN", "BN", "BN");

    // ************************************************************************
    // PLAYER ARRAY
    // ************************************************************************
    var players = new Array();
    <%
    if( allPlayers != null ) {
        iter = allPlayers.iterator();
        int i = 0;
        String dataList = "";
        while( iter.hasNext() ) {
            DraftPlayer dp = (DraftPlayer)iter.next();
            dataList = dp.pickKey + "|" + dp.playerKey + "|" + dp.rank + "|" + dp.lname + "|" + dp.fname + "|" + dp.pos + "|" + dp.teamAbrv;
            out.print("players[" + i + "] = \"" + dataList + "\";\n");
            i++;
        }
    }
    %>

    // ************************************************************************
    // DRAFT PICKS ARRAY
    // ************************************************************************
    var picks = new Array();
    <%
    if( picks != null ) {
        iter = picks.iterator();
        int i = 0;
        String dataList = "";
        while( iter.hasNext() ) {
            DraftPick dp = (DraftPick)iter.next();
            if( dp.playerKey == 0 ) break;
            DraftPlayer plyr = dp.selectedPlayer;
            dataList = dp.pickKey + "|" + dp.pickTeam + "|" + dp.playerKey + "|" + plyr.rank + "|" + plyr.lname + "|" + plyr.fname + "|" + plyr.pos + "|" + plyr.teamAbrv + "|" + dp.pickRound;
            out.print("picks[" + i + "] = \"" + dataList + "\";\n");
            i++;
        }
    }
    %>


    // ************************************************************************
    // saveSessAttribute
    // ************************************************************************
    function saveSessAttribute(attrName, attrValue) {
        //alert("saving session attribute: name=" + attrName + "; value=" + attrValue + ";");
        var oAjax = (window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP") );
        oAjax.onreadystatechange=function() {
            if (oAjax.readyState == 4 && oAjax.status == 200) {
                return true;
            }
        }
        oAjax.open("GET", "ajax_sess_attr.jsp?attrName=" + attrName + "&attrValue=" + attrValue, true);
        oAjax.send();
    }


    // ************************************************************************
    // checkUpdate
    // ************************************************************************
    function checkUpdate() {
        var oAjax = (window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP") );
        oAjax.onreadystatechange=function() {
            if (oAjax.readyState == 4) {
                if(oAjax.status == 200) {
                    var respTxt = oAjax.responseText;
                    dataElem = respTxt.split(",");
                    var newCurrPick = parseInt( dataElem[0] );
                    var newLastChat = parseInt( dataElem[1] );
                    if(newCurrPick > currPick || newLastChat > lastChat) {
                        window.location = "draft2012.jsp";
                        return false;
                    }
                    else {
                        window.setTimeout("checkUpdate()", 5000);
                    }
                }
                else {
                    alert("Ajax returned an invalid status: " + oAjax.status);
                }
            }
        }
        var n = Math.floor(Math.random() * 1000000);
        oAjax.open("GET", "ajax_curr_pick.jsp?rnd="+n, true);
        oAjax.send();
    }


    // ************************************************************************
    // setScreenHandle
    // ************************************************************************
    function setScreenHandle(oTxt) {
        saveSessAttribute("screenName", oTxt.value);
    }

    
    // ************************************************************************
    // setTeamRoster
    // ************************************************************************
    function setTeamRoster(whatChanged) {
        oTeam = document.getElementById("rosterTeamKey");
        oList = document.getElementById("divRosterList");

        pickTeamName = oTeam.options[oTeam.selectedIndex].value;
        if( whatChanged != "" ) saveSessAttribute("rosterTeam", pickTeamName);

        var sHtml = "<table width='99%' cellpadding='1' cellspacing='0'>";
        sHtml += "<tr class='hdr'>";
        sHtml += "<td>Pos</td>";
        sHtml += "<td>Player</td>";
        sHtml += "<td style='text-align:center'>Team</td>";
        sHtml += "<td style='text-align:center'>Pick</td>";
        sHtml += "<td style='text-align:center'>Yahoo! Rank</td>";
        sHtml += "</tr>";

        var usedPlayers = "";
        var currPos = "";
        var rosterName = "";
        var rosterRank = "";
        var rosterTeam = "";
        var rosterPick = "";
        var usePlayer = false;
        var trWeight = "";
        for(var i=0; i < rosterPos.length; i++) {
            currPos = rosterPos[i];
            rosterName = "&nbsp;";
            rosterPick = "&nbsp;";
            rosterRank = "&nbsp;";
            rosterTeam = "&nbsp;";
            for(var k=0; k < picks.length; k++) {
                dataElem = picks[k].split("|");
                usePlayer = false;
                trWeight = "";
                if( dataElem[1] == pickTeamName && usedPlayers.indexOf("|" + dataElem[2] + "|") == -1 ) {
                    if( dataElem[6].toUpperCase().indexOf(currPos) > -1 )
                        usePlayer = true;
                    else if( currPos == "UTIL" && dataElem[6].toUpperCase().indexOf("SP") == -1 && dataElem[6].toUpperCase().indexOf("RP") == -1)
                        usePlayer = true;
                    else if( currPos == "P" && (dataElem[6].toUpperCase().indexOf("SP") > -1 || dataElem[6].toUpperCase().indexOf("RP") > -1) )
                        usePlayer = true;
                    if( currPos == "BN" )
                        usePlayer = true;
                }
                
                if( usePlayer ) {
                    trWeight = "bold";
                    usedPlayers += "|" + dataElem[2] + "|";
                    rosterName = dataElem[5] + " " + dataElem[4] + " (" + dataElem[6] + ")";
                    rosterPick = "Rd " + dataElem[8] + " (" + dataElem[0] + ")";
                    rosterTeam = dataElem[7];
                    rosterRank = dataElem[3];
                    break;
                }
            }
            sHtml += "<tr class='brdr' style='font-weight:" + trWeight + "; font-size:8pt'>";
            sHtml += "<td>" + currPos + "</td>";
            sHtml += "<td>" + rosterName + "</td>";
            sHtml += "<td style='text-align:center'>" + rosterTeam + "</td>";
            sHtml += "<td style='text-align:center'>" + rosterPick + "</td>";
            sHtml += "<td style='text-align:center'>" + rosterRank + "</td>";
            sHtml += "<tr>";
        }
        sHtml += "</table>";
        oList.innerHTML = sHtml;
    }

    function OLD_setTeamRoster(whatChanged) {
        oTeam = document.getElementById("rosterTeamKey");
        oList = document.getElementById("divRosterList");

        pickTeamName = oTeam.options[oTeam.selectedIndex].value;
        if( whatChanged != "" ) saveSessAttribute("rosterTeam", pickTeamName);

        var sHtml = "<table width='99%' cellpadding='1' cellspacing='0' class='league'>";
        sHtml += "<tr class='hdr'>";
        sHtml += "<td style='text-align:left'>Pick</td>";
        sHtml += "<td>Pos</td>";
        sHtml += "<td style='text-align:left'>Player</td>";
        sHtml += "<td>Team</td>";
        sHtml += "<td>Yahoo! Rank</td>";
        sHtml += "</tr>";

        for(i=0; i < picks.length; i++) {
            dataElem = picks[i].split("|");
            if( dataElem[1] == pickTeamName ) {
                sHtml += "<tr class='row2'>";
                sHtml += "<td style='text-align:left'>" + dataElem[0] + ".</td>";
                sHtml += "<td>" + dataElem[6] + "</td>";
                sHtml += "<td style='text-align:left'>" + dataElem[5] + " " + dataElem[4] + "</td>";
                sHtml += "<td>" + dataElem[7] + "</td>";
                sHtml += "<td>" + dataElem[3] + "</td>";
                sHtml += "<tr>";
            }
        }
        sHtml += "</table>";
        oList.innerHTML = sHtml;
    }

    
    // ************************************************************************
    // setPlayerList
    // ************************************************************************
    function setPlayerList(whatChanged) {
        oPos = document.getElementById("playerPos");
        oTeam = document.getElementById("playerTeam");
        oName = document.getElementById("playerName");
        oList = document.getElementById("playerList");
        bHide = (document.getElementById("hideSelected").checked);

        //clear the list
        oList.options.length = 0;

        selPos = oPos.options[oPos.selectedIndex].value.toUpperCase();
        selTeam = oTeam.options[oTeam.selectedIndex].value.toUpperCase();
        selName = oName.value.toUpperCase();

        if( whatChanged != "" ) {
            if(whatChanged == "POS") saveSessAttribute("playerPos", selPos);
            if(whatChanged == "TEAM") saveSessAttribute("playerTeam", selTeam);
            if(whatChanged == "NAME") saveSessAttribute("playerName", selName);
            if(whatChanged == "SEL") saveSessAttribute("hideSelected", (bHide ? "Y" : "N"));
        }

        for(i=0; i < players.length; i++) {
            dataElem = players[i].split("|");
            showPlayer = true;

            if( bHide && dataElem[0] != "0" )
                showPlayer = false;
            else if( selPos != "ALL" && dataElem[5].toUpperCase().indexOf(selPos) == -1 )
                showPlayer = false;
            else if( selTeam != "ALL" && dataElem[6].toUpperCase() != selTeam )
                showPlayer = false;
            else if(selName != "" && dataElem[3].toUpperCase().indexOf(selName) == -1 && dataElem[4].toUpperCase().indexOf(selName) == -1  )
                showPlayer = false;
            else {
                pValue = dataElem[1];
                pText = dataElem[2] + ".  " + dataElem[4] + " " + dataElem[3] + " (" + dataElem[6] + " - " + dataElem[5] + ")";
                oOpt = new Option(pText, pValue);
                if( dataElem[0] != "0" ) {
                    oOpt.style.color="#DD0000";
                    oOpt.style.textDecoration="line-through";
                    oOpt.style.fontWeight="bold";
                    oOpt.value = "";
                }
                oList.options[oList.options.length] = oOpt;
            }
        }
    }


    // ************************************************************************
    // highlightPlayer
    // ************************************************************************
    function highlightPlayer() {
        oList = document.getElementById("playerList");
        if( oList.options[oList.selectedIndex].value == "" ) {
            oList.selectedIndex = -1;
        }
    }


    // ************************************************************************
    // makeDraftSelection
    // ************************************************************************
    function makeDraftSelection() {
        oList = document.getElementById("playerList");
        if( oList.selectedIndex == -1 ) {
            alert("You must select a player from the player list on the right.");
            return false;
        }

        var selKey = oList.options[oList.selectedIndex].value;
        var selName  = oList.options[oList.selectedIndex].text;

        if( !confirm("Are you sure you want to draft '" + selName + "' ???") ) {
            return false;
        }

        document.frmMakePick.playerKey.value = selKey;
        document.frmMakePick.submit();
        return false;
    }


    // ************************************************************************
    // actions once the page has been loaded
    // ************************************************************************
    setPlayerList("");
    setTeamRoster("");
    <% if( currPick > 15 ) {%>window.location="#draftPick<%=(currPick-10)%>";<%}%>
    document.frmChat.msg.focus();
    window.setTimeout("checkUpdate()", 5000);
    // -->
</script>

</body>
</html>
