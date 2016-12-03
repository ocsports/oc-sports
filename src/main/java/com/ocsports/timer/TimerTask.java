package com.ocsports.timer;

import com.ocsports.models.TimerMessageModel;
import com.ocsports.servlets.TimerServlet;
import org.apache.log4j.Logger;

public abstract class TimerTask extends java.util.TimerTask {

    protected final Logger log = Logger.getLogger(getClass().getName());

    /**
     * add a general information message to the timer message queue
     *
     * @param msg the message body to add
     */
    protected void addTaskMessage(String msg) {
        addTaskMessage(TimerServlet.MSG_TYPE_INFO, msg);
    }

    /**
     * add a information message to the timer message queue; with specific type
     *
     * @param msgType the type of message (i.e. TimerServlet.MSG_TYPE_INFO)
     * @param msg the message body to add
     */
    protected void addTaskMessage(String msgType, String msg) {
        if (log != null) {
            String className = getClass().getName();
            className = className.substring(className.lastIndexOf(".") + 1);
            if (msgType.equals(TimerServlet.MSG_TYPE_ERROR)) {
                log.error(className + " - " + msg);
            } else {
                log.info(className + " - " + msg);
            }
        }
        TimerServlet.addMessageToQueue(new TimerMessageModel(msgType, getClass().getName(), msg));
    }

    /**
     * add a general information message indicating the scheduled task has
     * completed successfully
     */
    protected void timerTaskCompleted() {
        addTaskMessage(TimerServlet.MSG_TYPE_INFO, "Task completed");
    }

    /**
     * add a error message indicating the scheduled task failed to complete
     *
     * @param t the exception object which caused the scheduled task to fail
     */
    protected void timerTaskFailed(Throwable t) {
        addTaskMessage(TimerServlet.MSG_TYPE_ERROR, "Task failed: " + t.getMessage());
    }
}
