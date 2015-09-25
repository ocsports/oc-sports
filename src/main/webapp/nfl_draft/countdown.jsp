<%@page contentType="text/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
	<title>Fantasy Baseball 2012</title>
    <link rel="stylesheet" href="../css/draft.css" type="text/css"></link>
</head>
<body>
<center>

<div id="divHeader" style="width:1000px; margin:0; padding:0 1em 1em 1em; background:#153215 url('<%=request.getContextPath()%>/images/draft/baseball_2010_bg.jpg') no-repeat center top">
    <table cellspacing="0" cellpadding="3" style="width:100%">
        <tr style="vertical-align:top">
            <td style="text-align:left; font-size:18pt; color:#EEEEEE">2012 Fantasy Baseball Draft</td>
            <td style="text-align:right; font-size:12pt; color:#EEEEEE"><span style="font-size:28pt; font-weight:bold">Lefty's</span><br/>Haunted Mansion 5</td>
        </tr>
        <tr style="vertical-align:bottom">
            <td style="text-align:left"><img src="<%=request.getContextPath()%>/images/draft/baseball-beer-2.jpg" width="180" height="125" style="margin-left:1em"/></td>
            <td style="text-align:right"><img src="<%=request.getContextPath()%>/images/draft/hauntedmansion.jpg" width="125" height="125" style="margin-right:1em"/></td>
        </tr>
    </table>
</div>

<div id="divBody" style="color:#5E7830; font-weight:bold; width:100%; margin:1em 0 0 0">
    <center>
        <h2 style="color:#FFFFFF">Welcome to the 2012 MLB Fantasy Baseball Draft!</h2>
        <h3 style="margin-top:3em">The draft begins in <div id="divCountdown" style="font-size:22pt; color:white; font-weight:bold">&nbsp;</div></h3>
        <form style="margin-top:2em">
            <input type="button" id="btnStart" class="btnGray" value=">> START >>" title="click to enter the draft page" onclick="clickDraftButton(this)" />
        </form>
    </center>
    <div id="draftLoc" style="font-size:14pt; margin-top:3em">
        <u>Draft Location</u>
        <br/>Dave N' Busters
        <br/>Block of Orange Mall
    </div>
</div>

<script language="javascript">
<!--
    var draftDate = new Date("March 23, 2012 18:00:00")

    function setCountdown() {
        var todayDate = new Date();
        diffMillis = draftDate - todayDate;
        if(diffMillis > 0) {
            diffDays = Math.floor(diffMillis/(60*60*1000*24)*1);
            diffHours = Math.floor((diffMillis%(60*60*1000*24))/(60*60*1000)*1);
            diffMin = Math.floor(((diffMillis%(60*60*1000*24))%(60*60*1000))/(60*1000)*1);
            diffSec = Math.floor((((diffMillis%(60*60*1000*24))%(60*60*1000))%(60*1000))/1000*1);

            oDiv = document.getElementById("divCountdown");
            oDiv.innerHTML = diffDays + " days, " + diffHours + " hours, " + diffMin + " minutes, " + diffSec + " seconds...";
            window.setTimeout("setCountdown()", 1000);
        }
        else {
            oDiv = document.getElementById("divCountdown");
            oDiv.innerHTML = "Draft Time! please wait one moment..."
            window.setTimeout("startDraft()", 5000);
        }
    }

    function clickDraftButton(oBtn) {
        if( oBtn.className == "btnGray" ) {
            if( !confirm("The draft has not started. Enter anyways?") ) return false;
        }
        oBtn.className = "btnYellow";
        window.location = "draft2012.jsp";
        return false;
    }

    function startDraft() {
        oBtn = document.getElementById("btnStart");
        oBtn.className = "btnYellow";
    }

    window.setTimeout("setCountdown()", 300);
// -->
</script>

</center>
</body>
</html>
