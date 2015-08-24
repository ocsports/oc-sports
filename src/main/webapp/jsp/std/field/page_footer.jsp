<%@page contentType="text/html"%>
			</td>
		</tr>
	</table>
	<div style="margin-left:1em; margin-top:1em; border-top:1px solid #DDDDDD">
        <a href="<%=request.getContextPath()%>/servlet/goUser?r=switchView" style="font-size:7pt; padding-left:1em">view mobile version</a>
	</div>
	<script language="javascript">serverTime('divServerTime', <%=new java.util.Date().getTime()%>)</script>
</body>
</html>
