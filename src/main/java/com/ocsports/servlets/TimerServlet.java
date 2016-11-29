package com.ocsports.servlets;

import com.ocsports.core.JSPPages;
import com.ocsports.models.TimerMessageModel;
import com.ocsports.timer.TimerTask;
import java.util.ArrayList;
import java.util.Timer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet class to manage all of the global tasks which will run on a 
 * scheduled basis; Servlet should be loaded at startup so the tasks are scheduled
 * at the earliest convenience; Each task may have a separate scheduled rate defined
 * in the web.xml file
 * @author paulcharlton
 */
public class TimerServlet extends ServletBase {
    public static final String MSG_TYPE_INFO = "INFO";
    public static final String MSG_TYPE_ERROR = "ERROR";
    public static final String MSG_TYPE_STARTUP = "STARTUP";
	public static ArrayList timerMessages;

    private ArrayList activeTasks;
    private Timer timer;

    /**
     * Read the scheduled tasks and their intervals from the web.xml file, written
     * as parameters to the servlet; one parameter per class; the parameter value
     * is a comma-separated array for name(String) and interval(long in seconds)
     * @param config
     * @throws ServletException 
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            activeTasks = new ArrayList();
            for(int i=0; i < 100; i++) {
                String taskClass = config.getInitParameter("taskClass"+i);
                if(taskClass == null || taskClass.equals("")) {
                    break;
                }
                activeTasks.add(taskClass);
            }
            startTimer();
        }
        catch (Exception ex) {
            log.error("Unable to init TimerServlet: " + ex.getMessage());
        }
    }

	/**
     * destroy the current servlet instance, including disabling the timer
     */
    public void destroy() {
        endTimer();
        timerMessages = null;
        activeTasks = null;
    }
    
	/**
     * called when there is no requested method on the URL line
     * @param request
     * @param response
     * @param session 
     */
	public void defaultAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		timerStatus( request, response, session );
	}

    /**
     * method invoked by an administrator to view the current timer status and
     * any messages which have been queued for reading
     * @param request
     * @param response
     * @param session 
     */
    public void timerStatus(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		request.setAttribute("scheduledTasks", activeTasks);
		request.setAttribute("messages", timerMessages);
		request.setAttribute("detailPage", JSPPages.ADMIN_TIMER );
		sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
    }

    /**
     * method invoked by an administrator to start or restart the local timer
     * @param request
     * @param response
     * @param session 
     */
    public void enableTimer(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		startTimer();
		this.timerStatus(request, response, session);
    }

    /**
     * method invoked by an administrator to stop the local timer
     * @param request
     * @param response
     * @param session 
     */
    public void disableTimer(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        endTimer();
        this.timerStatus(request, response, session);
    }

    /**
     * method invoked by an administrator to run an individual task which may or
     * may not be loaded as an active task
     * @param request
     * @param response
     * @param session 
     */
    public void runTask(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        String taskClass = request.getParameter("class");
        if(taskClass == null || taskClass.equals("")) {
            addTaskMessage(MSG_TYPE_ERROR, "class type " + taskClass + " not found");
        }
        else {
            try {
                Class c = Class.forName(taskClass);
                TimerTask task = (TimerTask)c.newInstance();

                addTaskMessage(MSG_TYPE_INFO, "manual execution of task " + taskClass);
                task.run();
            }
            catch(ClassNotFoundException cnfe) {
                addTaskMessage(MSG_TYPE_ERROR, "class type " + taskClass + " not found");
            }
            catch (IllegalAccessException iax) {
                addTaskMessage(MSG_TYPE_ERROR, "unknown error " + iax.getMessage());
            }
            catch (InstantiationException ix) {
                addTaskMessage(MSG_TYPE_ERROR, "unknown error " + ix.getMessage());
            }
        }
        this.timerStatus(request, response, session);
    }

    /**
     * adds a TimerMessage object to the static list of messages for the TimerServlet
     * @param model  the object to add to the end of the array
     */
    public static void addMessageToQueue(TimerMessageModel model) {
        if (timerMessages != null && model != null) {
            timerMessages.add(model);
        }
    }

    /**
     * add a message to the TimerServlet message queue; This will also record a log message
     * @param msgType  enumerated message type (i.e. TimerServlet.MSG_TYPE_ERROR)
     * @param msg  body of the message to be displayed
     */
    private void addTaskMessage(String msgType, String msg) {
        if (log != null) {
            log.debug(msg);
        }
        addMessageToQueue(new TimerMessageModel(msgType, this.getClass().getName(), msg));
    }

    /**
     * instantiate and schedule all timer tasks listed in web.xml file;
     */
    private void startTimer() {
		timerMessages = new ArrayList();
        try {
            if (timer != null) {
                endTimer();
            }
            timer = new Timer();
            addTaskMessage(TimerServlet.MSG_TYPE_STARTUP, "Timer started");
        }
        catch (Exception ex) {
            addTaskMessage(TimerServlet.MSG_TYPE_ERROR, ex.getMessage());
        }
            
        if(activeTasks == null || activeTasks.isEmpty()) {
            addTaskMessage(TimerServlet.MSG_TYPE_INFO, "No tasks found");
            return;
        }
        
        long delay = 0;
        String taskClass = null;
        int taskPeriod = 0;
        for(int i=0; i < activeTasks.size(); i++) {
            try {
                String[] taskElem = ((String)activeTasks.get(i)).split(",");
                taskClass = taskElem[0];
                taskPeriod = Integer.parseInt(taskElem[1]);

                Class c = Class.forName(taskClass);
                TimerTask task = (TimerTask)c.newInstance();
                timer.scheduleAtFixedRate(task, (1000l * 60l * ++delay), (1000l * 60l * taskPeriod));
                addTaskMessage(MSG_TYPE_INFO, taskClass + " scheduled to run every " + taskPeriod + " minutes");
            }
            catch(ClassNotFoundException cnfe) {
                addTaskMessage(MSG_TYPE_ERROR, "class type " + taskClass + " not found");
            }
            catch(NumberFormatException nfe) {
                addTaskMessage(MSG_TYPE_ERROR, "unable to parse minutes for period " + taskPeriod);
            }
            catch (IllegalAccessException iax) {
                addTaskMessage(MSG_TYPE_ERROR, "unknown error " + iax.getMessage());
            }
            catch (InstantiationException ix) {
                addTaskMessage(MSG_TYPE_ERROR, "unknown error " + ix.getMessage());
            }
        }
    }

    /**
     * stop the timer and any scheduled tasks
     */
    private void endTimer() {
        try {
            if(timer != null) {
                timer.cancel();
            }
            timerMessages = new ArrayList();
        }
        catch(Exception ex) {
            addTaskMessage(TimerServlet.MSG_TYPE_ERROR, "unable to stop timer object: " + ex.getMessage());
        }
        finally {
            timer = null;
            addTaskMessage(TimerServlet.MSG_TYPE_INFO, "Timer Stopped");
        }
    }
}
