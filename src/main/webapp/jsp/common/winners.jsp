<%@ page contentType="text/html" %>
<%@ page import="com.ocsports.core.PropertiesHelper" %>
<%@ page import="com.ocsports.core.DateHelper" %>
<%@ page import="java.util.Map" %>
<%
    Map winnersList = PropertiesHelper.getPoolWinnersList();
    Map lockWinners = PropertiesHelper.getLockWinnersList();
    Map survivorWinners = PropertiesHelper.getSurvivorWinnersList();
%>
<h2>Past Champions</h2>

<span class="windowmsg">
    Here is a list of our past champions.
    If you are honored on our wall of champions, please send in a picture or image to represent yourself.
</span>

<div id="divMessages" style="width:98%; margin-top:2em; padding:2px; border:0px solid #26370A">
    <%
    int currYear = DateHelper.getCurrentYear();
	for (int i = 0; i < 20; i++) {
		String yr = String.valueOf(currYear - i);
        String pw = winnersList.get(yr);
        String lw = lockWinners.get(yr);
        String sw = survivorWinners.get(yr);
        if(pw.length() > 0 || sw.length() > 0 || lw.length() > 0) {
            %>
            <table cellspacing="1" cellpadding="15" border="1" class="winner">
                <tr colspan="2">
                    <td>
                        <h2><%=yr%> Winners:</h2>
                    </td>
                </tr>
                <tr>
                    <td>
                        <img src="<%=request.getContextPath()%>/images/oc-sports/silhouette.jpg" />
                    </td>
                    <td>
                        <h4>Overall Champion:</h4>
                        <strong><%=pw%></strong>
                    </td>
                </tr>
                <tr>
                    <td>
                        <img src="<%=request.getContextPath()%>/images/oc-sports/silhouette.jpg" />
                    </td>
                    <td>
                        <h4>Locks Champion:</h4>
                        <strong><%=lw%></strong>
                    </td>
                </tr>
                <tr>
                    <td>
                        <img src="<%=request.getContextPath()%>/images/oc-sports/silhouette.jpg" />
                    </td>
                    <td>
                        <h4>Survivor Champion:</h4>
                        <strong><%=sw%></strong>
                    </td>
                </tr>
            </table>
            <%
        }
    }
    %>
</div>
