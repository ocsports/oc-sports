// -- SEASON LIST -- //
function changeSeason(seasonId) {
    if(seasonId == document.frmList.seasonId.value) {
        document.frmList.seasonId.value = "";
        document.frmList.seriesId.value = "";
    } else {
        document.frmList.seasonId.value = seasonId;
    }
    document.frmList.submit();
    return false;
}

function changeSeries(seriesId) {
    if(seriesId == document.frmList.seriesId.value) {
        document.frmList.seriesId.value = "";
    } else {
        document.frmList.seriesId.value = seriesId;
    }
    document.frmList.submit();
    return false;
}

function showSeason(sId, sName, sType, sPrefix, sActive) {
    document.frmSeason.updateSeasonId.value = sId;
    document.frmSeason.name.value = sName;
    document.frmSeason.sportType.selectedIndex = 0;
    document.frmSeason.prefix.value = sPrefix;
    document.frmSeason.act.checked = (sActive == "1");
    return showDiv("divSeason", 400, 500);
}

function showSeries(sId, seasonId, startDate, endDate, sPub, sCleanup, sReminder) {
    document.frmSeries.updateSeriesId.value = sId;
    document.frmSeries.updateSeasonId.value = seasonId;
    document.frmSeries.startDate.value = startDate;
    document.frmSeries.endDate.value = endDate;
    document.frmSeries.pub.checked = (sPub == "1");
    document.frmSeries.cleanup.checked = (sCleanup == "1");
    document.frmSeries.remind.checked = (sReminder == "1");
    return showDiv("divSeries", 450, 500);
}

function showGame(sId, startDate, awayTeamId, homeTeamId, spread, awayScore, homeScore, posted) {
    document.frmGame.gameId.value = sId;
    document.frmGame.start.value = startDate;
    for(var i=0; i < document.frmGame.awayTeam.length; i++) {
        if(document.frmGame.awayTeam.options[i].value == awayTeamId) {
            document.frmGame.awayTeam.options[i].selected = true;
            break;
        }
    }
    for(var i=0; i < document.frmGame.homeTeam.length; i++) {
        if(document.frmGame.homeTeam.options[i].value == homeTeamId) {
            document.frmGame.homeTeam.options[i].selected = true;
            break;
        }
    }
    document.frmGame.spread.value = spread;
    document.frmGame.awayScore.value = awayScore;
    document.frmGame.homeScore.value = homeScore;
    document.frmGame.posted.checked = (posted == "1");
    return showDiv("divGame", 450, 600);
}


// -- PLAYERS LIST -- //
function sortPlayerList(orderBy) {
    document.frmPlayersList.orderBy.value = orderBy;
    document.frmPlayersList.submit();
    return false;
}

function showPlayerDetail(userId, firstName, lastName, addrId, email1, email2, loginId, disabld) {
    document.frmPlayer.userId.value = userId;
    document.frmPlayer.firstName.value = firstName;
    document.frmPlayer.lastName.value = lastName;

    sAddr = aAddrs[addrId];
    sAddr = sAddr.replace("||", "\n");
    sAddr = sAddr.replace("|", "\n");
    sAddr = sAddr.replace("|", "");
    document.frmPlayer.addr.value = sAddr;

    document.frmPlayer.email.value = email1;
    document.frmPlayer.email2.value = email2;
    document.frmPlayer.loginId.value = loginId;
    document.frmPlayer.disabld.checked = (disabld == "Y");
    return showDiv("divPlayer", 450, 550);
}

function showPlayerPayStatus(userId, userName, currStatus) {
    document.frmPayment.userId.value = userId;
    document.frmPayment.nm.value = userName;
    if(currStatus == "1") {
        document.frmPayment.payStatus[1].checked = true;
    } else {
        document.frmPayment.payStatus[0].checked = true;
    }
    document.frmPayment.em.checked = true;
    return showDiv("divPayment", 450, 500);
}

function deletePlayer() {
    sName = document.frmPlayer.firstName.value + " " + document.frmPlayer.lastName.value;
    if( confirm("Delete player '" + sName + "' ?") ) {
        hideDiv("divPlayer");
        document.frmPlayer.action = sDeletePlayerAction;
        document.frmPlayer.submit();
    }
    return false;
}


// -- SYSTEM NOTICES -- //
function showSysNotice(id, msgId, pub) {
    document.frmSysNotice.noticeId.value = id;
    document.frmSysNotice.msg.value = aMsgs[msgId];
    document.frmSysNotice.pub.checked = (pub == "1");
    return showDiv("divSysNotice", 450, 500);
}

function deleteSysNotice() {
    if( confirm("Delete this notice permanently?") ) {
        hideDiv("divSysNotice");
        document.frmSysNotice.action = deleteNoticeURL;
        document.frmSysNotice.submit();
    }
    return false;
}

function createSysNotice() {
    document.frmSysNotice.noticeId.value = -1;
    document.frmSysNotice.msg.value = "";
    document.frmSysNotice.pub.checked = false;
    return showDiv("divSysNotice", 450, 500);
}


// -- TEAMS -- //
function showTeamDetail(teamId, abrv, city, nm, divId, weather, dome, turf) {
    document.frmTeam.teamId.value = teamId;
    document.frmTeam.abrv.value = abrv;
    document.frmTeam.city.value = city;
    document.frmTeam.nm.value = nm;
    for(var i=0; i < document.frmTeam.div.length; i++) {
        if(document.frmTeam.div.options[i].value == divId) {
            document.frmTeam.div.options[i].selected = true;
            break;
        }
    }
    document.frmTeam.weather.value = weather;
    document.frmTeam.dome.checked = (dome == "Y");
    document.frmTeam.turf.checked = (turf == "Y");
    return showDiv("divTeam", 450, 500);
}


// -- TIMERS -- //
function runTimerTask() {
	showDiv("divRunTask", 450, 500);
	return false;
}

function hideRunTask() {
	hideDiv("divRunTask");
	return false;
}


// -- FORUMS -- //
function showForumMsg(msgId, msgText) {
    document.frmForumMsg.msgId.value = msgId;
    document.frmForumMsg.msg.value = msgText;
    return showDiv("divForumMsg", 450, 500);
}
