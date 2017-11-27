package com.ocsports.reports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.ocsports.core.PropertiesHelper;
import com.ocsports.core.SportTypes;
import com.ocsports.models.LeagueSeriesXrefModel;
import com.ocsports.models.SeasonModel;
import com.ocsports.models.SeasonSeriesModel;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;


/**
 * This report class will automatically build a system notice based on
 * the winners and high scores for each series in the active season, for
 * the default league. This will avoid an administrator logging in to check
 * the winners and manually update the correct system notice each week.
 */
public class PublishedWinnersReport extends BaseReport {

    private String systemNotice = "";

    public PublishedWinnersReport() {
        super(); // set/use report defaults
    }

    public String getSystemNotice() {
        return this.systemNotice;
    }

    public boolean build() {
        SeasonSQLController seasonSql = null;
        PoolSQLController poolSql = null;
        try {
            seasonSql = new SeasonSQLController();
            poolSql = new PoolSQLController();

            ArrayList seasons = (ArrayList)seasonSql.getSeasons(SportTypes.TYPE_NFL_FOOTBALL, true);
            if(seasons == null || seasons.size() == 0) {
                return buildSuccessful();
            }
            int activeSeasonId = ((SeasonModel)seasons.get(0)).getId();
            int defaultLeagueId = PropertiesHelper.getDefaultLeagueId();

            Collection seriesModels = seasonSql.getSeriesBySeason(activeSeasonId);
            Collection leagueSeriesModels = poolSql.getLeagueSeriesBySeason(defaultLeagueId, activeSeasonId);

            buildNotice(leagueSeriesModels, seriesModels);
        }
        catch(Exception e) {
            return buildFailed();
        }
        finally {
            if (poolSql != null) {
                poolSql.closeConnection();
                poolSql = null;
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
                seasonSql = null;
            }
        }
        return buildSuccessful();
    }

    public void buildNotice(Collection leagueSeriesModels, Collection seriesModels) {
        // reset the message before we rebuild
        this.systemNotice = "";

        if(leagueSeriesModels == null || leagueSeriesModels.size() == 0) {
            return;
        }

        String msg = "";
        Iterator iter = leagueSeriesModels.iterator();
        while(iter.hasNext()) {
            LeagueSeriesXrefModel lsx = (LeagueSeriesXrefModel)iter.next();
            int seq = this.getSeriesSequence(seriesModels, lsx.getSeriesId());
            String week = "Week " + String.valueOf(seq) + " Winners - " +
                      "<strong>" + lsx.getLeagueWinners() + "</strong>" +
                      " (" + lsx.getLeagueHighScore() + " Wins)";

            // later series go on top of the list; <br> tag between
            if(msg.length() > 0) msg = "<br>" + msg;
            msg = week + msg;
        }
        // put the final touches on the winners list
        this.systemNotice = "<span style='color:#3355FF'>" + msg + "</span>";
    }

    private int getSeriesSequence(Collection seriesModels, int seriesId) {
        if(seriesModels == null || seriesModels.size() == 0) return 0;

        Iterator iter = seriesModels.iterator();
        while(iter.hasNext()) {
            SeasonSeriesModel ssm = (SeasonSeriesModel)iter.next();
            if(ssm.getId() == seriesId) return ssm.getSequence();
        }
        return 0;
    }

    public BaseReport copy() {
        PublishedWinnersReport rpt = new PublishedWinnersReport();
        rpt.systemNotice = this.systemNotice;
        return rpt;
    }
}
