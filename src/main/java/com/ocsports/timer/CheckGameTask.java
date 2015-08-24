/*
 * Title         CheckGameTask.java
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

public class CheckGameTask extends TimerTask implements ITimerTask {
    private static final  String CLASS_NAME = CheckGameTask.class.getName();
    private static final  Logger log = Logger.getLogger( CLASS_NAME );

    private String        msg = null;

    public void run() {
        this.run(false);
    }

    public void run(boolean ignoreTimes) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new java.util.Date());
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            cal = null;

            //Check the time, only run this task if between 10:00AM and 8:00PM
            if(ignoreTimes || (currentHour >= 10 && currentHour <= 20)) {
                this.setGameDefaultPicks();
                TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, "Task completed."));
            }
        }
        catch(Exception e) {
			ProcessException pe = new ProcessException(e);
			String msg = pe.getExceptionTypeClass() + ": " + pe.getMessage();
            log.error( msg );
            TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_ERROR, CLASS_NAME, msg));
        }
    }

    private void setGameDefaultPicks() throws ProcessException {
        SeasonSQLController seasonSQL = null;
        PoolSQLController poolSQL = null;
        try {
            seasonSQL = new SeasonSQLController();
            poolSQL = new PoolSQLController();

            Collection games = seasonSQL.findGamesToSetDefaultPicks();
            if(games != null && !games.isEmpty()) {
                Iterator iter = games.iterator();
                while(iter.hasNext()) {
                    GameModel gm = (GameModel)iter.next();
                    //set default pick for users who haven't chosen a team yet
                    poolSQL.setGameDefaultPicks(gm);
                    //update game so we do NOT pick up on next run
                    seasonSQL.setGameDefaultStatus(gm.getId(), 1);
                    msg = "Default picks set for game " + gm.getId();
                    log.debug(msg);
                    TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, msg));
                }
            }
        }
        catch(ProcessException pe) {
            throw pe;
        }
        finally {
            if(seasonSQL != null) seasonSQL.closeConnection();
            if(poolSQL != null) poolSQL.closeConnection();
        }
    }
}
