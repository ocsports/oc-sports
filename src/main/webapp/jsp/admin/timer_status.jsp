<%@ page contentType="text/html" %>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.models.TimerMessageModel" %>
<%@ page import="com.ocsports.servlets.TimerServlet" %>
<%@ page import="com.ocsports.timer.*" %>
<%
    ArrayList messages = (ArrayList)request.getAttribute("messages");
    Collection tasks = (Collection)request.getAttribute("scheduledTasks");

    String runURL = response.encodeURL("goTimer?r=runTask");
    String enableURL = response.encodeURL("goTimer?r=enableTimer");
    String disableURL = response.encodeURL("goTimer?r=disableTimer");
%>

<h2>
	Timer Messages
	<a href="<%=enableURL%>" title="Start Timer" style="padding-left: 0.5em; font-size: 10pt; font-weight: bold">Start Timer</a>
	<a href="<%=disableURL%>" title="Stop Timer" style="padding-left: 0.5em; font-size: 10pt; font-weight: bold">Stop Timer</a>
	<a href="" title="Run a Task" style="padding-left: 0.5em; font-size: 10pt; font-weight: bold" onclick="return runTimerTask()">Run a Task</a>
</h2>
<div id="divRunTask" class="msg">
	<form name="frmTask" action="<%=runURL%>" style="padding: 10px" method="POST">
		<h4>Select a task to run</h4>
		<select name="class" style="width: 30em" onChange="document.frmTask.submit()">
			<option value=''>&nbsp;&nbsp;&nbsp;</option>
			<%
			if(tasks != null && !tasks.isEmpty()) {
				Iterator iter = tasks.iterator();
				while(iter.hasNext()) {
					String taskClass = (String)iter.next();
					%>
					<option><%=taskClass.substring(0, taskClass.indexOf(","))%></option>
					<%
				}
			}
			%>
		</select>
		<br/><a href="" title="Cancel" onclick="return hideRunTask()">Cancel</a>
	</form>
</div>

<div style="overflow:auto; height:500; width:99%; border-style:inset; border-width:0;">
    <table cellspacing="0" cellpadding="5" border="0" class="trtable" style="padding:0; font-size:10pt; width:100%">
        <tr class="hdr">
            <td>Time</td>
            <td>Subject</td>
            <td>Task</td>
            <td width='100%'>Message</td>
        </tr>
<%
    if(messages != null && !messages.isEmpty()) {
        String trClass = "";
        String trStyle = "";
        int counter = 0;
		SimpleDateFormat fmt = new SimpleDateFormat("h:mm a");
		TimerMessageModel tmm = null;

        for(int i = messages.size(); i > 0; i--) {
			if( i > 50 ) break;
            tmm = (TimerMessageModel)messages.get(i-1);
			trClass = (trClass.equals("row1") ? "row2" : "row1");
            trStyle = (tmm.getSubject().equals("ERROR") ? "color:red;" : "");
%>
            <tr class="<%=trClass%>" style="<%=trStyle%>">
                <td style="white-space: nowrap"><%=fmt.format(tmm.getTimestamp())%></td>
                <td style="white-space: nowrap"><%=tmm.getSubject()%></td>
                <td style="white-space: nowrap"><%=tmm.getTaskName()%>&nbsp;</td>
                <td><%=tmm.getMessage()%>&nbsp;</td>
            </tr>
<%
        }
    }
%>
    </table>
</div>
