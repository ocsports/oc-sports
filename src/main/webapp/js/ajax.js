function ajax_getDraftResults() {
    var oAjax;
    if (window.XMLHttpRequest) {
        oAjax = new XMLHttpRequest();
    }
    else {
        oAjax = new ActiveXObject("Microsoft.XMLHTTP");
    }
    alert("created object");

    oAjax.onreadystatechange=function() {
        if (oAjax.readyState==4 && oAjax.status==200) {
            alert("response returned - " + oAjax.responseText);
            var newCurrPick = oAjax.responseText.trim();
            if(newCurrPick != pageCurrPick) {
                pageCurrPick = newCurrPick;
                alert("updating current pick to " + newCurrPick);
                refreshDraft();
            }
        }
    }
    alert("sending object");
    oAjax.open("GET", "http://localhost:8080/draft/current_pick_ajax.asp", true);
    oAjax.send();
}
