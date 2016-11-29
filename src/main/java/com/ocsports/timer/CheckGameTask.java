package com.ocsports.timer;

import com.ocsports.core.ProcessException;
import com.ocsports.models.GameModel;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;
import java.util.Collection;
import java.util.Iterator;

/**
 * Scheduled task to set default picks for users who did not make selections; look
 * for games which are already in progress but have not already been processed
 * by this same task earlier
 * @author paulcharlton
 */
public class CheckGameTask extends TimerTask {
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
            
            Collection gamesStarted = seasonSql.findGamesToSetDefaultPicks();
            if(gamesStarted == null || gamesStarted.isEmpty()) {
                return;
            }
            addTaskMessage(gamesStarted.size() + " games have started");

            Iterator iter = gamesStarted.iterator();
            while(iter.hasNext()) {
                GameModel gm = (GameModel)iter.next();
                //set default pick for users who haven't chosen a team yet
                poolSql.setGameDefaultPicks(gm);
                //update game record so we do NOT pick up on next run
                seasonSql.setGameDefaultStatus(gm.getId(), 1);
                addTaskMessage("Default picks set for game " + gm.getId());
            }
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
}
