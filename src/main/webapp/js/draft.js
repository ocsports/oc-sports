var refreshTimeoutValue = 20000;
var selectedPlayerKeys = "";
var refreshTimer;

function calcage(secs, num1, num2) {
  s = ((Math.floor(secs/num1))%num2).toString();
  if (s.length < 2)
    s = "0" + s;
  return "<b>" + s + "</b>";
}

function CountBack(elemName) {
    var displayFmt = "%%D%% Days, %%H%% Hours, %%M%% Minutes, %%S%% Seconds.";
    var dthen = new Date("03/19/2010 7:30 PM");
    var dnow = new Date();
    var ddiff = new Date(dthen-dnow);
    secs = Math.floor(ddiff.valueOf()/1000);

    if (secs < 0) {
        document.getElementById(elemName).innerHTML = "Draft Time!!!";
        return false;
    }
    displayStr = displayFmt.replace(/%%D%%/g, calcage(secs,86400,100000));
    displayStr = displayStr.replace(/%%H%%/g, calcage(secs,3600,24));
    displayStr = displayStr.replace(/%%M%%/g, calcage(secs,60,60));
    displayStr = displayStr.replace(/%%S%%/g, calcage(secs,1,60));

    document.getElementById(elemName).innerHTML = displayStr;
    return true;
}

function addRandomParam(url) {
    var n = Math.floor(Math.random() * 1000000);
    if( url.indexOf("?") > 0 ) {
        url = url.substr(0, url.indexOf("?") );
    }
    return (url + "?rnd=" + n);
}

function refreshDraftLists() {
    elem = document.getElementById('frameDraftList');
    elem.src = addRandomParam(elem.src);

    elem = document.getElementById('framePlayerList');
    elem.src = addRandomParam(elem.src);
}

function refreshDraft() {
    elem = document.getElementById('frameDraftList');
    elem.src = addRandomParam(elem.src);

    elem = document.getElementById('framePlayerList');
    elem.src = addRandomParam(elem.src);

    elem = document.getElementById('frameCurrentPick');
    elem.src = addRandomParam(elem.src);

    window.setTimeout("refreshDraft()", refreshTimeoutValue);
}

function showDraftSelection(currPick, currTeam, selectedPlayerIds) {
    window.clearTimeout(refreshTimer);
    document.frmCurrPick.pickId.value = currPick;
    document.frmCurrPick.pickTeam.value = currTeam;
    elem = document.getElementById('currPickDisp');
    elem.innerHTML = currPick + ". " + currTeam;

    selectedPlayerKeys = "," + selectedPlayerIds + ",";
    setPlayerList();

    return showDiv("divSelection", 500, 400, document.frmCurrPick.playerId);
}
