/*
 * Title         TimerServlet.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.servlets;
 */
package com.ocsports.servlets;

import com.ocsports.core.JSPPages;
import com.ocsports.models.TimerMessageModel;
import com.ocsports.timer.ITimerTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;


public class TimerServlet extends ServletBase {
    public static final String ALIAS  = "goTimer";
    public static final String TIMER_INFO = "INFO";
    public static final String TIMER_ERROR = "ERROR";
	public static List timerMessages = new ArrayList();

    private String CLASS_NAME = TimerServlet.class.getName();
    private Logger log = Logger.getLogger( CLASS_NAME );
    private List taskList = null;
    private String msg = null;
    private Timer timer = null;

    public TimerServlet() {
		super();
    }

	// @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        String cnt = config.getInitParameter("taskCount");
        int taskCount = (cnt == null || cnt.equals("") ? 0 : Integer.parseInt(cnt));

        taskList = new ArrayList();
        for(int i=0; i < taskCount; i++) {
            String taskClass = config.getInitParameter("taskClass"+i);
            if(taskClass != null && !taskClass.equals("")) {
                taskList.add(taskClass);
            }
        }
        this.startTimer();
    }

	// @Override
    public void destroy() {
        this.endTimer();
    }
    
	// @Override
	public void defaultAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		timerStatus( request, response, session );
	}

    public void timerStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		request.setAttribute("scheduledTasks", taskList);
		request.setAttribute("messages", timerMessages);
		request.setAttribute("detailPage", JSPPages.ADMIN_TIMER );
		sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
    }

    public void enableTimer(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		if(timer == null) startTimer();
		this.timerStatus(request, response, session);
    }

    public void disableTimer(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        if(timer != null) endTimer();
        this.timerStatus(request, response, session);
    }

    public void runTask(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String taskClass = request.getParameter("class");
        if(taskClass != null && taskClass.length() > 0) {
            try {
                Class c = Class.forName(taskClass);
                timerMessages.add(new TimerMessageModel(TIMER_INFO, taskClass, "running task manually..."));
                ITimerTask task = (ITimerTask)c.newInstance();
                task.run(true);
            }
            catch(Exception e) {
                msg = e.getClass().getName() + " - " +  e.getMessage();
                log.error(msg);
                timerMessages.add(new TimerMessageModel(TIMER_ERROR, taskClass, msg));
            }
        }
        this.timerStatus(request, response, session);
    }

    private void startTimer() {
		timerMessages = new ArrayList();
        try {
            if(taskList != null && !taskList.isEmpty()) {
                timer = new Timer();
                msg = "Timer started";
                log.info(msg);
                timerMessages.add(new TimerMessageModel("STARTUP", CLASS_NAME, msg));

                long delay = 0;
				for( int i=0; i < taskList.size(); i++ ) {
                    String schedTask = (String)taskList.get(i);
                    String[] aTask = schedTask.split(",");
                    String taskClass = aTask[0];
                    int taskPeriod = Integer.parseInt( aTask[1] );
                    try {
                        Class c = Class.forName(taskClass);
                        java.util.TimerTask task = (java.util.TimerTask)c.newInstance();

                        timer.scheduleAtFixedRate( task, (1000l * 60l * ++delay), (1000l * 60l * taskPeriod) );
                        timerMessages.add(new TimerMessageModel(TIMER_INFO, taskClass, "run every " + taskPeriod + " minutes"));
                    }
                    catch(Exception e) {
                        msg = e.getClass().getName() + " - " +  e.getMessage();
                        log.error(msg);
                        timerMessages.add(new TimerMessageModel(TIMER_ERROR, taskClass, msg));
                    }
                }
            }
        }
        catch(Exception e) {
            msg = e.getClass().getName() + " - " +  e.getMessage();
            log.error(msg);
            timerMessages.add(new TimerMessageModel(TIMER_ERROR, CLASS_NAME, msg));
        }
    }
    
    private void endTimer() {
		if(timer == null) return;
		timer.cancel();
		msg = "Timer stopped";
		log.info(msg);
		timerMessages.add(new TimerMessageModel("SHUTDOWN", CLASS_NAME, msg));
		timer = null;
    }
    
}
