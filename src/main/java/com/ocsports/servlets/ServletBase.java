package com.ocsports.servlets;

import java.lang.reflect.Method;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import com.ocsports.core.*;
import com.ocsports.models.UserModel;
import org.apache.log4j.Logger;

/**
 * Base class for all servlets within this project;  
 * override GET and POST HTTP requests but no others (PUT, DELETE, etc...)
 * @author     Paul Charlton
 * @created    September, 2014
 */
public abstract class ServletBase extends HttpServlet {

	private Logger log = Logger.getLogger(ServletBase.class.getName());

	abstract void defaultAction(HttpServletRequest request, HttpServletResponse response, HttpSession session);

	/**
	 * DO NOT USE INSTANCE VARIABLES
	 * only one instance of this class is created and shared among all requests
	 */
	public ServletBase() {
	}

	/**
	 * @override - called once when the Servlet is first started;  this is NOT called for each request
	 * @param config
	 * @throws ServletException 
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	/**
	 * @override - default request action called just before doGet or doPost
	 * @param request
	 * @param response
	 * @throws ServletException 
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			super.service(request, response);
		} catch (java.io.IOException ioe) {
			this.handleException(request, response, null, ioe);
		}
	}

	/**
	 * @override - process HTTP GET requests
	 * @param request
	 * @param response 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		this.processRequest(request, response);
	}

	/**
	 * @override - process HTTP POST requests
	 * @param request
	 * @param response 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		this.processRequest(request, response);
	}

	/**
	 * performs the same set of actions regardless if the request was GET or POST
	 * @param request
	 * @param response 
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = null;
		try {
			session = request.getSession(true);

			// something really bad happened if any of these objects are invalid or null
			if (request == null || response == null || session == null) {
				throw new ProcessException("An unexpected error has occurred.  Http request object(s) are missing.");
			}

			String action = request.getParameter("r");
			if (action == null || action.length() == 0) {
				this.defaultAction(request, response, session);
				return;
			}

			try {
				Method method = this.getClass().getDeclaredMethod(action, new Class[]{HttpServletRequest.class, HttpServletResponse.class, HttpSession.class});
				method.invoke(this, new Object[]{request, response, session});
			}
			catch( NoSuchMethodException e) {
				this.defaultAction(request, response, session);
			}
		}
		catch (Exception e) {
			this.handleException(request, response, session, e);
		}
	}

	/**
	 * what to do if there was a problem servicing the http request
	 * @param request
	 * @param response
	 * @param session
	 * @param e 
	 */
	protected void handleException(HttpServletRequest request, HttpServletResponse response, HttpSession session, Exception e) {
		try {
			ProcessException pe = null;
			if (e instanceof ProcessException) {
				pe = (ProcessException) e;
			} else {
				pe = new ProcessException(e);
			}


			String hdr = new String(new char[80]).replaceAll("\0", "-") + "\n";

			StringBuffer buf = new StringBuffer();
			buf.append("\n" + hdr);
			buf.append("ServletBase.handleException()\n");
			buf.append(hdr);

			buf.append("UserModel values:\n");
			if (session != null && session.getAttribute("UserModel") != null) {
				UserModel um = (UserModel) session.getAttribute("UserModel");
				buf.append("userId: " + um.getUserId() + "\n");
				buf.append("name: " + um.getFullName() + "\n");
				buf.append("loginId: " + um.getLoginId() + "\n");
				buf.append("userEmail: " + um.getEmail() + "\n");
				buf.append("\n");
			}

			buf.append("HttpSession values:\n");
			if (session != null) {
				buf.append("sessionId: " + session.getId() + "\n");
				buf.append("\n");
			}

			buf.append("HttpServletRequest values:\n");
			if (request != null) {
				buf.append("query: " + request.getQueryString() + " (" + request.getMethod() + ")\n");
				buf.append("remoteAddress: " + request.getRemoteAddr() + "\n");
				buf.append("user-agent: " + request.getHeader("user-agent") + "\n");
				buf.append("\n");
			}

			buf.append("ProcessException values:\n");
			if (pe != null) {
				buf.append("exceptionTypeClass: " + pe.getExceptionTypeClass().getName() + "\n");
				buf.append("exceptionType: " + pe.getExceptionType() + "\n");
				buf.append("message: " + pe.getMessage() + "\n");
				buf.append(pe.getStackTraceAsString() + "\n");
				buf.append("\n");
			}

			// log all error info into the log file
			log.error( buf.toString() );

			request.setAttribute("exception", pe);
			this.sendToJSP(JSPPages.ERROR_PAGE, request, response, session);
		} catch (Exception e2) {
			// something really, really, really went wrong here
			log.error( e2.getMessage() );
		}
	}

	/**
	 * forward to a JSP page / JSP servlet;  does NOT go back to the client on a loop
	 * @param url
	 * @param request
	 * @param response
	 * @param session
	 * @throws ProcessException 
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
	 * send a forwarding request back to the client who will redirect back to the server;
	 * does a new request from the client
	 * @param url
	 * @param request
	 * @param response
	 * @param session
	 * @throws ProcessException 
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
	 * change the URL from a standard to a mobile url if on a mobile phone or the attribute is forced
	 * @param url
	 * @param request
	 * @param session
	 * @throws ProcessException 
	 */
	protected String checkViewType(String url, HttpServletRequest request, HttpSession session) {
		String viewType = (String) session.getAttribute( AttributeList.SESS_VIEW_TYPE );
		if (viewType == null || viewType.length() == 0) {
			viewType = getViewType(request);
			session.setAttribute(AttributeList.SESS_VIEW_TYPE, viewType);
		}
		if (viewType.equals("MOBILE") && url.indexOf("/admin/") < 0) {
			url = url.replaceAll("/std/", "/mobile/");
		}
		return url;
	}

	/**
	 * check the request headers and identify if the user-agent is a mobile device type
	 * @param request
	 * @return 
	 */
	protected String getViewType(HttpServletRequest request) {
		int i = 0;
		String[] types = null;

		// check if this header exists from the user agent
		String xwap = (String) request.getHeader("x-wap-profile");
		if (xwap != null && xwap.length() > 0) {
			return "MOBILE";
		}

		// check if the user agent will accept WAP content
		String acc = (String) request.getHeader("accept");
		if (acc != null && acc.length() > 0) {
			acc = acc.toLowerCase();
			if (acc.indexOf("/wap.") >= 0) {
				return "MOBILE";
			}
			if (acc.indexOf(".wap") >= 0) {
				return "MOBILE";
			}
		}

		// grab the user-agent description from the request
		String agent = request.getHeader("user-agent");
		agent = (agent == null ? "" : agent.toLowerCase());

		//these ARE NOT mobile device types which could return a match below
		types = new String[]{"Creative AutoUpdate", "OfficeLiveConnector", "MSIE 8.0", "OptimizedIE8", "MSN Optimized", "Swapper"};
		for (i = 0; i < types.length; i++) {
			String searchFor = types[i].toLowerCase();
			if (agent.indexOf(searchFor) >= 0) {
				return "STD";
			}
		}

		// these ARE mobile device types
		types = new String[]{"midp", "j2me", "avantg", "docomo", "novarra", "palmos", "palmsource", "240x320", "opwv", "chtml", "pda", "windows ce", "mmp/", "blackberry", "mib/", "symbian", "wireless", "nokia", "hand", "mobi", "phone", "cdm", "up.", "audio", "SIE-", "SEC-", "samsung", "HTC", "mot-", "mitsu", "sagem", "sony", "alcatel", "lg", "erics", "vx", "NEC", "philips", "mmm", "xx", "panasonic", "sharp", "wap", "sch", "rover", "pocket", "benq", "java", "pt", "pg", "vox", "amoi", "bird", "compal", "kg", "voda", "sany", "kdd", "dbt", "sendo", "sgh", "gradi", "jb", "moto"};
		for (i = 0; i < types.length; i++) {
			String searchFor = types[i].toLowerCase();
			if (agent.indexOf(searchFor) >= 0) {
				return "MOBILE";
			}
		}
		return "STD";
	}
}
