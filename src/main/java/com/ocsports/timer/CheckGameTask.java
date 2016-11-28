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

    private SeasonSQLController seasonSQL;
    private PoolSQLController poolSQL;

    public void run(boolean ignoreTimes) {
        run();
    }

    public void run() {
        try {
            seasonSQL = new SeasonSQLController();
            poolSQL = new PoolSQLController();
            
            Collection gamesStarted = seasonSQL.findGamesToSetDefaultPicks();
            if(gamesStarted == null || gamesStarted.isEmpty()) {
                return;
            }
            addTaskMessage(gamesStarted.size() + " games have started...");

            Iterator iter = gamesStarted.iterator();
            while(iter.hasNext()) {
                GameModel gm = (GameModel)iter.next();
                //set default pick for users who haven't chosen a team yet
                poolSQL.setGameDefaultPicks(gm);
                //update game so we do NOT pick up on next run
                seasonSQL.setGameDefaultStatus(gm.getId(), 1);
                addTaskMessage("Default picks set for game " + gm.getId());
            }
            addTaskMessage(CLASS_NAME + " task completed.");
        }
        catch(ProcessException ex) {
            addTaskMessage(CLASS_NAME + " task failed: " + ex.getExceptionTypeClass().getName() + "-" + ex.getMessage());
        }
        catch(Throwable t) {
            addTaskMessage(CLASS_NAME + " task failed: Throwable caught: " + t.getMessage());
        }
        finally {
            seasonSQL = null;
            poolSQL = null;
        }
    }

    private void addTaskMessage(String msg) {
        log.debug(msg);
        TimerServlet.timerMessages.add(new TimerMessageModel(TimerServlet.TIMER_INFO, CLASS_NAME, msg));
    }
}
