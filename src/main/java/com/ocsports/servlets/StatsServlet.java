/*
 * Title         StatsServlet.java
 * Created       April 11, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.sql;
 */
package com.ocsports.servlets;

import com.ocsports.views.PickStatsView;
import java.util.Collection;

import com.ocsports.core.*;
import com.ocsports.sql.*;
import com.ocsports.models.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StatsServlet extends ServletBase {
    public static final String DETAIL_PAGE_ATTR = "detailPage";
    public static final String MENU_ITEM_ATTR   = "menuItem";
	
	public static PickStatsView savedPickStats;

    public StatsServlet() {
		super();
    }

	public void defaultAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			byTeam( request, response, session );
		}
		catch( ProcessException pe) {
			handleException(request, response, session, pe);
		}
	}

    public void byTeam(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        StatsSQLController sqlCtrlr = null;
        try {
            sqlCtrlr = new StatsSQLController();

            LeagueModel lm = (LeagueModel)session.getAttribute("LeagueModel");
            int seasonId = lm.getSeasonId();

            TeamStatsModel[] teamStats = sqlCtrlr.getTeamStats(seasonId);
            request.setAttribute("TeamStatsModels", teamStats);

            request.setAttribute(MENU_ITEM_ATTR, "Team");
            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.STATS_BY_TEAM);
            sendToJSP( JSPPages.STATS, request, response, session );
        }
        catch(ProcessException pe) {
            throw pe;
        }
        finally {
            if(sqlCtrlr != null) sqlCtrlr.closeConnection();
        }
    }

    public void byTeamGroup(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        StatsSQLController sqlCtrlr = null;
        SeasonSQLController seasonSQL = null;
        try {
            sqlCtrlr = new StatsSQLController();
            seasonSQL = new SeasonSQLController();

            String strSeries = request.getParameter("seriesId");
            int seriesId = (strSeries == null || strSeries.equals("") ? -1 : Integer.parseInt(strSeries));
            request.setAttribute("seriesId", String.valueOf(seriesId));

            LeagueModel lm = (LeagueModel)session.getAttribute("LeagueModel");
            int seasonId = lm.getSeasonId();

            SeasonModel sm = seasonSQL.getSeasonModel(seasonId);
            request.setAttribute("SeasonModel", sm);

            java.util.Collection series = seasonSQL.getSeriesBySeason(seasonId);
            request.setAttribute("SeasonSeriesModels", series);

            TeamStatsModel[] teamStats = sqlCtrlr.getTeamGroupStats(seasonId, seriesId);
            request.setAttribute("TeamStatsModels", teamStats);

            request.setAttribute(MENU_ITEM_ATTR, "TeamGroup");
            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.STATS_BY_GROUP);
            sendToJSP( JSPPages.STATS, request, response, session );
        }
        catch(ProcessException pe) {
            throw pe;
        }
        finally {
            if(sqlCtrlr != null) sqlCtrlr.closeConnection();
            if(seasonSQL != null) seasonSQL.closeConnection();
        }
    }

    public void byTeamPicks(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		StatsSQLController sqlCtrlr = null;
        SeasonSQLController seasonCtrlr = null;
        try {
            sqlCtrlr = new StatsSQLController();
            seasonCtrlr = new SeasonSQLController();

			// get the sort key out of the session first
			String sessSortKey = (String)session.getAttribute(AttributeList.SESS_PICK_STATS_SORT);
			int sortKey = (sessSortKey == null ? PickStatsView.SORT_BY_TEAM : Integer.parseInt(sessSortKey) );

			// check if we are changing the sort; if so, apply to the session
			String paramSortKey = request.getParameter("s");
			if( paramSortKey != null && paramSortKey.length() > 0 ) {
				sortKey = Integer.parseInt(paramSortKey);
			}
			session.setAttribute(AttributeList.SESS_PICK_STATS_SORT, String.valueOf(sortKey));
			
			LeagueModel lm = (LeagueModel)session.getAttribute("LeagueModel");
			int seasonId = lm.getSeasonId();

            Collection seriesModels = seasonCtrlr.getSeriesBySeason(seasonId);

			if( savedPickStats == null || savedPickStats.isStale() ) {
				savedPickStats = sqlCtrlr.getPicksStats(seasonId);
			}
			PickStatsView model = (PickStatsView)savedPickStats.clone();
			model.sortTeams( sortKey );

            request.setAttribute("SeasonSeriesModels", seriesModels);
			request.setAttribute("PickStatsModel", model);
            request.setAttribute(MENU_ITEM_ATTR, "Picks");
            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.STATS_BY_PICKS);
            sendToJSP( JSPPages.STATS, request, response, session );
        }
        catch(ProcessException pe) {
            handleException(request, response, session, pe);
        }
        finally {
            if(sqlCtrlr != null) sqlCtrlr.closeConnection();
            if(seasonCtrlr != null) seasonCtrlr.closeConnection();
        }
    }
}
