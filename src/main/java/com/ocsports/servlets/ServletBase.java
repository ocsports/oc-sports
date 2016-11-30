package com.ocsports.servlets;

import com.ocsports.core.AttributeList;
import com.ocsports.core.JSPPages;
import com.ocsports.core.ProcessException;
import com.ocsports.models.UserModel;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Base class for all servlets within this project; override GET and POST HTTP
 * requests but no others (PUT, DELETE, etc...)
 *
 * @author Paul Charlton
 */
public abstract class ServletBase extends HttpServlet {

    protected final Logger log = Logger.getLogger(this.getClass().getName());

    abstract void defaultAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException;

    /**
     * @override - process HTTP GET requests
     * @param request
     * @param response
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * @override - process HTTP POST requests
     * @param request
     * @param response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * performs the same set of actions regardless if the request was GET or
     * POST
     *
     * @param request
     * @param response
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = null;
        try {
            session = request.getSession(true);

            // something really bad happened if any of these objects are invalid or null
            if (request == null || response == null || session == null) {
                throw new ProcessException("Http request object(s) are missing.");
            }

            String action = request.getParameter("r");
            if (action == null || action.length() == 0) {
                defaultAction(request, response, session);
                return;
            }

            try {
                Method method = this.getClass().getDeclaredMethod(action, new Class[]{HttpServletRequest.class, HttpServletResponse.class, HttpSession.class});
                method.invoke(this, new Object[]{request, response, session});
            } catch (NoSuchMethodException e) {
                defaultAction(request, response, session);
            } catch (InvocationTargetException ex) {
                defaultAction(request, response, session);
            }
        } catch (ProcessException pe) {
            handleException(request, response, session, pe);
        } catch (IllegalAccessException e) {
            handleException(request, response, session, e);
        } catch (IllegalArgumentException e) {
            handleException(request, response, session, e);
        } catch (SecurityException e) {
            handleException(request, response, session, e);
        }
    }

    /**
     * what to do if there was a problem servicing the http request
     *
     * @param request
     * @param response
     * @param session
     * @param e
     */
    protected void handleException(HttpServletRequest request, HttpServletResponse response, HttpSession session, Exception e) {
        try {
            ProcessException pe;
            if (e == null) {
                pe = new ProcessException("null exception passed into handleException() method");
            } else if (e instanceof ProcessException) {
                pe = (ProcessException) e;
            } else {
                pe = new ProcessException(e);
            }

            String delim = new String(new char[80]).replaceAll("\0", "-") + "\n";

            StringBuffer buf = new StringBuffer();
            buf.append(delim);
            buf.append("Exception\n");
            buf.append("  exceptionTypeClass: ").append(pe.getExceptionTypeClass().getName()).append("\n");
            buf.append("  exceptionType: ").append(pe.getExceptionType()).append("\n");
            buf.append("  message: ").append(pe.getMessage()).append("\n");
            buf.append("  - UserModel\n");
            if (session != null && session.getAttribute("UserModel") != null) {
                UserModel um = (UserModel) session.getAttribute("UserModel");
                buf.append("    userId: ").append(um.getUserId()).append("\n");
                buf.append("    name: ").append(um.getFullName()).append("\n");
                buf.append("    loginId: ").append(um.getLoginId()).append("\n");
                buf.append("    userEmail: ").append(um.getEmail()).append("\n");
            }
            buf.append("  - HttpSession\n");
            if (session != null) {
                buf.append("    sessionId: ").append(session.getId()).append("\n");
            }
            buf.append("  - HttpServletRequest\n");
            if (request != null) {
                buf.append("    query: ").append(request.getQueryString()).append("\n");
                buf.append("    method: ").append(request.getMethod()).append(")\n");
                buf.append("    remoteAddress: ").append(request.getRemoteAddr()).append("\n");
                buf.append("    user-agent: ").append(request.getHeader("user-agent")).append("\n");
            }
            buf.append(pe.getStackTraceAsString()).append("\n");

            buf.append(delim);
            log.error(buf.toString());

            if (request != null) {
                request.setAttribute("exception", pe);
                this.sendToJSP(JSPPages.ERROR_PAGE, request, response, session);
            }
        } catch (Exception e2) {
            // something really, really, really went wrong here
            log.error(e2.getMessage());
        }
    }

    /**
     * forward to a JSP page / JSP servlet; does NOT go back to the client on a
     * loop
     *
     * @param url
     * @param request
     * @param response
     * @param session
     */
    protected void sendToJSP(String url, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            ServletContext context = this.getServletContext();
            url = checkViewType(url, request, session);
            RequestDispatcher dispatcher = context.getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } catch (ServletException se) {
            handleException(request, response, session, se);
        } catch (IOException ioe) {
            handleException(request, response, session, ioe);
        }
    }

    /**
     * send a forwarding request back to the client who will redirect back to
     * the server; does a new request from the client
     *
     * @param url
     * @param request
     * @param response
     * @param session
     */
    protected void sendRedirect(String url, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            url = checkViewType(url, request, session);
            response.sendRedirect(response.encodeRedirectURL(url));
        } catch (IOException ioe) {
            handleException(request, response, session, ioe);
        }
    }

    /**
     * change the URL from a standard to a mobile url if on a mobile phone or
     * the attribute is forced
     *
     * @param url
     * @param request
     * @param session
     * @return the updated url if altered based on the user-agent
     */
    protected String checkViewType(String url, HttpServletRequest request, HttpSession session) {
        String viewType = (String) session.getAttribute(AttributeList.SESS_VIEW_TYPE);
        if (viewType == null || viewType.length() == 0) {
            viewType = getViewType(request);
            session.setAttribute(AttributeList.SESS_VIEW_TYPE, viewType);
        }
        if (viewType.equals("MOBILE") && url.indexOf("/admin/") == -1) {
            url = url.replaceAll("/std/", "/mobile/");
        }
        return url;
    }

    /**
     * check the request headers and identify if the user-agent is a mobile
     * device type
     *
     * @param request
     * @return
     */
    protected String getViewType(HttpServletRequest request) {
        // check if this header exists from the user agent
        String xwap = (String) request.getHeader("x-wap-profile");
        if (xwap != null && xwap.length() > 0) {
            return "MOBILE";
        }

        // check if the user agent will accept WAP content
        String acc = (String) request.getHeader("accept");
        if (acc != null && acc.length() > 0) {
            acc = acc.toLowerCase();
            if (acc.indexOf("/wap.") > -1) {
                return "MOBILE";
            }
            if (acc.indexOf(".wap") > -1) {
                return "MOBILE";
            }
        }

        // grab the user-agent description from the request
        String agent = request.getHeader("user-agent");
        agent = (agent == null ? "" : agent.toLowerCase());

        //these ARE NOT mobile device types which could return a match below
        String[] types = new String[]{"Creative AutoUpdate", "OfficeLiveConnector", "MSIE 8.0", "OptimizedIE8", "MSN Optimized", "Swapper"};
        for (int i = 0; i < types.length; i++) {
            String searchFor = types[i].toLowerCase();
            if (agent.indexOf(searchFor) > -1) {
                return "STD";
            }
        }

        // these ARE mobile device types
        types = new String[]{"midp", "j2me", "avantg", "docomo", "novarra", "palmos", "palmsource", "240x320", "opwv", "chtml", "pda", "windows ce", "mmp/", "blackberry", "mib/", "symbian", "wireless", "nokia", "hand", "mobi", "phone", "cdm", "up.", "audio", "SIE-", "SEC-", "samsung", "HTC", "mot-", "mitsu", "sagem", "sony", "alcatel", "lg", "erics", "vx", "NEC", "philips", "mmm", "xx", "panasonic", "sharp", "wap", "sch", "rover", "pocket", "benq", "java", "pt", "pg", "vox", "amoi", "bird", "compal", "kg", "voda", "sany", "kdd", "dbt", "sendo", "sgh", "gradi", "jb", "moto"};
        for (int i = 0; i < types.length; i++) {
            String searchFor = types[i].toLowerCase();
            if (agent.indexOf(searchFor) > -1) {
                return "MOBILE";
            }
        }
        return "STD";
    }
}
