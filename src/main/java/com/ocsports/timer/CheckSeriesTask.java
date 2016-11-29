package com.ocsports.timer;

import com.ocsports.core.ProcessException;
import com.ocsports.core.SportTypes;
import com.ocsports.models.SeasonModel;
import com.ocsports.models.SeasonSeriesModel;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;
import java.util.Collection;
import java.util.Iterator;

/**
 * Scheduled task for adding lock and survivor picks for users who made no selections
 * for the current series which just concluded; also conduct any other general cleanup 
 * for the current series.
 * @author paulcharlton
 */
public class CheckSeriesTask extends TimerTask {
    private SeasonSQLController seasonSql;
    private PoolSQLController poolSql;

    /**
     * execute the task and do not let exceptions leak as they will destroy the timer object
     */
    public void run() {
        try {
            initTask();
            seasonSql = new SeasonSQLController();
            poolSql = new PoolSQLController();

            this.getCurrentSeries();
            timerTaskCompleted();
        }
        catch(ProcessException pe) {
            timerTaskFailed(pe);
        }
        catch(Throwable t) {
            timerTaskFailed(t);
        }
        finally {
            seasonSql = null;
            poolSql = null;
        }
    }
    
    /**
     * get the current series for each active season and check the flag if cleanup
     * has already been done; if not, execute the cleanup
     * @throws ProcessException 
     */
    private void getCurrentSeries() throws ProcessException {
        Collection seasons = seasonSql.getSeasons(SportTypes.TYPE_NFL_FOOTBALL, true);

        if(seasons == null || seasons.isEmpty()) {
            return;
        }

        Iterator iter = seasons.iterator();
        while(iter.hasNext()) {
            SeasonModel sm = (SeasonModel)iter.next();
            int seriesId = seasonSql.getCurrentSeries(sm.getId());
            SeasonSeriesModel ssm = seasonSql.getSeasonSeriesModel(seriesId);
            if(!ssm.isUserCleanup()) {
                boolean allGamesStarted = seasonSql.allSeriesGamesStarted(seriesId);
                if(allGamesStarted) {
                    addTaskMessage("Final cleanup for season series " + seriesId);
                    poolSql.cleanupUserSeries(seriesId);
                    //set the status so we don't perform on this series any longer
                    seasonSql.setSeriesCleanupStatus(seriesId, 1);
                }
            }
        }
    }
}
