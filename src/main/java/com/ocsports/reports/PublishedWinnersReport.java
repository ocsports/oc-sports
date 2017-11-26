package com.ocsports.reports;

import java.util.ArrayList;
import java.util.Collection;
import com.ocsports.core.PropertiesHelper;
import com.ocsports.core.SportTypes;
import com.ocsports.models.SeasonModel;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;


public class PublishedWinnersReport extends BaseReport {

    Collection seasonWinners = null;

    public PublishedWinnersReport() {
        super(); // set/use report defaults
    }

    public String getSystemNotice() {
        return null;
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

            seasonWinners = poolSql.getLeagueSeriesBySeason(defaultLeagueId, activeSeasonId);
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

    public BaseReport copy() {
        PublishedWinnersReport rpt = new PublishedWinnersReport();
        rpt.seasonWinners = this.seasonWinners;
        return rpt;
    }
}
