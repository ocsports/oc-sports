<%@ page contentType="text/html" %>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="com.ocsports.core.AttributeList"%>
<%@ page import="com.ocsports.models.SeasonSeriesModel" %>
<%@ page import="com.ocsports.views.PickStatsView" %>
<%
    Collection seriesModels = (Collection)request.getAttribute("SeasonSeriesModels");
    PickStatsView picksModel = (PickStatsView)request.getAttribute("PickStatsModel");
	int sortKey = Integer.parseInt( (String)session.getAttribute(AttributeList.SESS_PICK_STATS_SORT) );


    String viewURL = response.encodeURL("goStats?r=byTeamPicks");
%>

<div style="margin:1em">
    <span class="smalllabel">* Click a column header to sort the teams.  Last updated at <%=picksModel.lastUpdated()%></span>
    <table cellspacing="0" cellpadding="3" border="0" class="league" style="padding:0">
        <tr class="hdr">
			<%
				String teamColHdr = (sortKey == PickStatsView.SORT_BY_TEAM ? "[Team]" : "Team");
				String totalColHdr = (sortKey == PickStatsView.SORT_BY_TOTAL ? "[Total]" : "Total");
			%>
            <td style="text-align:left"><a href="<%=viewURL%>&s=<%=PickStatsView.SORT_BY_TEAM%>"><%=teamColHdr%></a></td>
            <td style="width:2em"><a href="<%=viewURL%>&s=<%=PickStatsView.SORT_BY_TOTAL%>"><%=totalColHdr%></a></td>
            <%
            if( seriesModels != null && !seriesModels.isEmpty() ) {
                Iterator iter = seriesModels.iterator();
                while( iter.hasNext() ) {
                    SeasonSeriesModel ssm = (SeasonSeriesModel)iter.next();
					String colHdr = String.valueOf( ssm.getSequence() );
					if( sortKey == ssm.getId() ) {
						colHdr = "[" + colHdr + "]";
					}
                    %>
                    <td style="width:2em"><a href="<%=viewURL%>&s=<%=ssm.getId()%>"><%=colHdr%></a></td>
                    <%
                }
            }
            %>
        </tr>
        <%
        if( picksModel != null ) {
			Map picksMap = picksModel.getPicksByTeam();
            String trClass = "";
            Iterator iter = picksMap.keySet().iterator();
            while( iter.hasNext() ) {
                String key = (String)iter.next();
                Map teamPicks = (Map)picksMap.get(key);
				String teamName = (String)teamPicks.get("teamName");
                trClass = (trClass.equals("row1") ? "row2" : "row1");
				String totalPicks = (String)teamPicks.get("0");
                %>
                <tr class="<%=trClass%>">
                    <td style="font-size:10pt; text-align:left; white-space:nowrap"><%=teamName%></td>
                    <td><%=totalPicks%></td>
                    <%
					Iterator iter2 = seriesModels.iterator();
					while( iter2.hasNext() ) {
						SeasonSeriesModel ssm = (SeasonSeriesModel)iter2.next();
						String pickCount = (String)teamPicks.get( String.valueOf(ssm.getId()) );
						if( pickCount == null ) pickCount = "--";
						%>
						<td><%=pickCount%></td>
						<%
                    }
                    %>
                </tr>
                <%
            }
        }
        %>
    </table>
</div>
