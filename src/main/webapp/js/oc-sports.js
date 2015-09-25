function get_max_num(a) {
	var max_val = 0;
	for(var i=0; i < a.length; i++) {	
		if( !isNaN(a[i]) && a[i] > max_val) max_val = a[i];
	}
	return max_val;
}

function showDiv(divName, divW, divH, oFocus) {
	var d = document;
	var db = document.body;
	var dde = document.documentElement;

	// this will avoid scrollbars appearing in IE9
	var screenPad = 5;

    var docH = get_max_num( new Array(db.scrollHeight, dde.scrollHeight, db.offsetHeight, dde.offsetHeight, db.clientHeight, dde.clientHeight, self.innerHeight) );
	var screenW = get_max_num( new Array(self.innerWidth, dde.clientWidth, db.clientWidth) );
	var screenH = get_max_num( new Array(self.innerHeight, dde.clientHeight, db.clientHeight) );
	var scrollH = get_max_num( new Array(window.pageYOffset, db.scrollTop, dde.scrollTop) );

	//set the block screen to the width of the screen and the height of the entire document
	var oDiv1 = document.getElementById("divBlockScreen");
	oDiv1.style.width = (screenW-screenPad) + 'px';
	oDiv1.width = (screenW-screenPad) + 'px';
	oDiv1.style.height = (docH-screenPad) + 'px';
	oDiv1.height = (docH-screenPad) + 'px';

	//set the popup width and height to the specified values in the function paramters; will change for each popup screen
	oDiv2 = document.getElementById(divName);
	//oDiv2.style.width = divW + 'px';
	//oDiv2.width = divW + 'px';
	//oDiv2.style.height = divH + 'px';
	//oDiv2.height = divH + 'px';

	//set the popup Left and Top centered in the screen {10px higher than center; looks nicer}
	var divL = (screenW / 2) - (divW / 2);
	var divT = (screenH / 2) - (divH / 2) + scrollH - 10;
    oDiv2.style.left = (divL < 10 ? 10 : divL) + 'px';
    //oDiv2.style.top  = (divT < 10 ? 10 : divT) + 'px';
    oDiv2.style.top = '125px';
    window.scrollTo(0, 0);

	//set both div elements visible
	oDiv1.style.visibility = 'visible';
	oDiv2.style.visibility = 'visible';

	// set the cursor to the specified field {optional}
    if( oFocus ) oFocus.focus();
    return false;
}


function OLD_showDiv_OLD(divName, divW, divH, oFocus) {
    window.scrollTo(0, 0);

	var screenX = 0;
	var screenY = 0;
	// all except Explorer
	if (self.innerHeight) {
		screenX = self.innerWidth;
		screenY = self.innerHeight;
	}
	// Explorer 6 Strict Mode
	else if (document.documentElement && document.documentElement.clientHeight) {
		screenX = document.documentElement.clientWidth;
		screenY = document.documentElement.clientHeight;
	}
	// other Explorers
	else if (document.body) {
		screenX = document.body.clientWidth;
		screenY = document.body.clientHeight;
	}

	oDiv1 = document.getElementById("divBlockScreen");
    if( !oDiv1 ) {
        alert("DIV tag #1 does not exist");
        return false;
    }
	oDiv1.style.width = screenX + 'px';
	oDiv1.style.height = screenY + 'px';
	oDiv1.width = screenX + 'px';
	oDiv1.height = screenY + 'px';
	oDiv1.style.visibility = 'visible';
    
	oDiv2 = document.getElementById(divName);
    if( !oDiv2 ) {
        alert("DIV tag #2 does not exist");
        return false;
    }
    oDiv2.style.left = ((screenX / 2) - (divW / 2)) + 'px';
    oDiv2.style.top  = ((screenY / 2) - (divH / 2) - 50) + 'px';
	oDiv2.style.visibility = 'visible';

    if( oFocus ) oFocus.focus();
	return false;
}

function hideDiv(divName) {
	oDiv1 = document.getElementById("divBlockScreen");
	oDiv1.style.visibility = 'hidden';

	oDiv2 = document.getElementById(divName);
	oDiv2.style.visibility = 'hidden';

	return false;
}

function showLogin() {
	return showDiv("divLogin", 300, 450, document.frmLogin.loginId);
}

function showAdminLogin() {
	return showDiv("divAdminLogin", 300, 450, document.frmAdminLogin.adminId);
}

function showSignup() {
	return showDiv("divSignup", 500, 650, document.frmSignup.firstName);
}

function showForum() {
	return showDiv("divAddForum", 500, 650, document.frmMsg.msg);
}

function pickTeam(selectType, gameCounter, teamId, initPage) {
    highlightColor = "#FFF380";

    if( selectType == "A" )  {
        oSelectTag = document.getElementById("tdAway" + gameCounter);
        oUnselectTag = document.getElementById("tdHome" + gameCounter);
    }
    else {
        oSelectTag = document.getElementById("tdHome" + gameCounter);
        oUnselectTag = document.getElementById("tdAway" + gameCounter);
    }
    oElem = document.frmPicks.elements["gamePick" + gameCounter];
    oImg = document.getElementById("img" + gameCounter);

    if(oElem.value == teamId) {
        oSelectTag.style.backgroundColor = "";
        oElem.value = "-1";
        oImg.src = "../images/red_dot.gif";
        oImg.title = "No team selected. Click a team name to make a selection.";
    }
    else {
        oSelectTag.style.backgroundColor = highlightColor;
        oUnselectTag.style.backgroundColor = "";
        oElem.value = teamId;
        if(initPage == "Y") {
            oImg.src = "../images/green_dot.gif";
            oImg.title = "Your selection is confirmed.";
        } else {
            oImg.src = "../images/yellow_dot.gif";
            oImg.title = "Click the 'Save My Changes' button to confirm your selection.";
        }
    }
    return false;
}

function forumMsgChanged(maxLen) {
    msgLen = (document.frmMsg.msg.value).length;
    remainLen = maxLen - msgLen;
    if(remainLen < 0) remainLen = 0;
    document.frmMsg.msgChars.value = remainLen;
    return false;
}

function serverTime(divName, time) {
	var oDiv = document.getElementById(divName);
	if( !oDiv ) return;

	var tm = parseFloat(time) + 1000;
	var oDate = new Date(tm);

	var hr = oDate.getHours();
	var mm = oDate.getMinutes();
	var ss = oDate.getSeconds();
	var am = "";
	if( ss < 10 ) ss = "0" + ss;
	if( mm < 10 ) mm = "0" + mm;

	if( hr == 0 ) {
		hr = 12;
		am = "AM";
	} else if( hr <= 11 ) {
		am = "AM";
	} else if( hr == 12 ) {
		am = "PM";
	} else {
		hr = hr - 12;
		am = "PM";
	}

	var htm = "Server Time: " + hr + ":" + mm + ":" + ss + " " + am;
	oDiv.innerHTML = htm;
		
	window.setTimeout("serverTime('" + divName + "', '" + tm + "')", 1000);
	
}
