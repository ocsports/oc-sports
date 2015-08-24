/*
 * Title         CheckSeriesTask.java
 * Created       September 26, 2006
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.timer;
 */
package com.ocsports.timer;

import java.util.*;
import org.apache.log4j.Logger;

import com.ocsports.core.*;
import com.ocsports.models.*;
import com.ocsports.sql.*;
import com.ocsports.servlets.TimerServlet;

public class CheckSeriesTask extends TimerTask implements ITimerTask {
    private static final  String CLASS_NAME = CheckSeriesTask.class.getName();
    private static final  Logger log = Logger.getLogger( CLASS_NAME );

    private String        msg = null;

    public void run() {
        this.run(false);
    }
    
    public void run(boolean ignoreTimes) {
        try {
            log.debug("STARTING");

            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date());
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            cal = null;

            //Check the time, only run this task if between 4:00PM and 10:00PM
            if(ignoreTimes || (currentHour >= 16 && currentHour <= 22)) {
                this.getCurrentSeries();
                TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, "Task completed."));
            }
            log.debug("COMPLETED");
        }
        catch(ProcessException pe) {
            log.debug(pe.getStackTraceAsString());
            TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_ERROR, CLASS_NAME, "Task failed: " + pe.getExceptionTypeClass().getName() + "-" + pe.getMessage()));
        }
        catch(Throwable t) {
            log.debug("Throwable caught: " + t.getMessage());
            TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_ERROR, CLASS_NAME, "Task failed: Throwable caught: " + t.getMessage()));
        }
    }
    
    private void getCurrentSeries() throws ProcessException {
        SeasonSQLController seasonSQL = null;
        PoolSQLController poolSQL = null;
        try {
            seasonSQL = new SeasonSQLController();
            poolSQL = new PoolSQLController();

            Collection seasons = seasonSQL.getSeasons(0, true);
            if(seasons != null && !seasons.isEmpty()) {
                Iterator iter = seasons.iterator();
                int seriesId = -1;
                while(iter.hasNext()) {
                    SeasonModel sm = (SeasonModel)iter.next();
                    seriesId = seasonSQL.getCurrentSeries(sm.getId());
                    SeasonSeriesModel ssm = seasonSQL.getSeasonSeriesModel(seriesId);
                    if(!ssm.isUserCleanup()) {
                        this.checkUserSeries(seriesId);
                    }
                }
            }
        }
        catch(Exception e) {
            if(e instanceof ProcessException) 
                throw (ProcessException)e;
            else
                throw new ProcessException(e);
        }
        finally {
            if(seasonSQL != null) seasonSQL.closeConnection();
            if(poolSQL != null) poolSQL.closeConnection();
        }
    }
    
    private void checkUserSeries(int seriesId) throws ProcessException {
        SeasonSQLController seasonSQL = null;
        PoolSQLController poolSQL = null;
        try {
            seasonSQL = new SeasonSQLController();
            poolSQL = new PoolSQLController();

            //check if all games have started for the series
            boolean allGamesStarted = seasonSQL.allSeriesGamesStarted(seriesId);
            if(allGamesStarted) {
                msg = "Cleaning User Series; seriesId=" + seriesId;
                log.debug(msg);
                TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, msg));
                poolSQL.cleanupUserSeries(seriesId);
                //set the status so we don't perform on this series any longer
                seasonSQL.setSeriesCleanupStatus(seriesId, 1);
            }
        }
        catch(Exception e) {
            if(e instanceof ProcessException) 
                throw (ProcessException)e;
            else
                throw new ProcessException(e);
        }
        finally {
            if(seasonSQL != null) seasonSQL.closeConnection();
            if(poolSQL != null) poolSQL.closeConnection();
        }
    }
}
