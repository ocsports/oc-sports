package com.ocsports.servlets;

import com.ocsports.core.AttributeList;
import com.ocsports.core.JSPPages;
import com.ocsports.core.MyEmailer;
import com.ocsports.core.ProcessException;
import com.ocsports.core.PropertiesHelper;
import com.ocsports.core.SportTypes;
import com.ocsports.models.LeagueModel;
import com.ocsports.models.UserModel;
import com.ocsports.sql.ForumSQLController;
import com.ocsports.sql.PoolSQLController;
import com.ocsports.sql.SeasonSQLController;
import com.ocsports.sql.UserSQLController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserServlet extends ServletBase {

    public void defaultAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        beginLogin(request, response, session);
    }

    public void beginLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        try {
            UserModel um = (UserModel) session.getAttribute("UserModel");
            if (um != null && um.getUserId() > 0) {
                home(request, response, session);
                return;
            }

            String viewType = (String) session.getAttribute(AttributeList.SESS_VIEW_TYPE);
            if (viewType == null || viewType.length() == 0) {
                viewType = getViewType(request);
                session.setAttribute(AttributeList.SESS_VIEW_TYPE, viewType);
            }

            sendToJSP(JSPPages.HOME_PAGE, request, response, session);
        } catch (Exception e) {
            throw new ProcessException(e);
        }
    }

    public void switchView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        try {
            String viewType = (String) session.getAttribute(AttributeList.SESS_VIEW_TYPE);
            if (viewType == null || viewType.length() == 0) {
                viewType = getViewType(request);
                session.setAttribute(AttributeList.SESS_VIEW_TYPE, viewType);
            }
            session.setAttribute(AttributeList.SESS_VIEW_TYPE, (viewType.equals("MOBILE") ? "STD" : "MOBILE"));

            this.sendRedirect("goUser", request, response, session);
        } catch (Exception e) {
            throw new ProcessException(e);
        }
    }

    public void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController sqlCtrlr = null;
        PoolSQLController poolCtrlr = null;
        try {
            String loginError = "";
            HashMap errors = new HashMap();

            String loginId = request.getParameter("loginId");
            String loginPwd = request.getParameter("loginPwd");

            if (loginId == null || loginId.length() == 0) {
                loginError = "Login Id is required";
            } else if (loginPwd == null || loginPwd.length() == 0) {
                loginError = "Password is required";
            } else {
                sqlCtrlr = new UserSQLController();
                poolCtrlr = new PoolSQLController();

                int userId = sqlCtrlr.login(loginId, loginPwd);
                sqlCtrlr.createAuditLogin(loginId, loginPwd, userId);

                if (userId <= 0) {
                    loginError = "Invalid login / password";
                } else {
                    UserModel uModel = sqlCtrlr.getUserModel(userId);
                    session.setAttribute("userId", String.valueOf(userId));
                    session.setAttribute("UserModel", uModel);
                    if (uModel.isLoginDisabled()) {
                        loginError = "Account has been disabled<br/>Contact site administrator";
                    } else if (uModel.getLeagueId() <= 0) {
                        this.joinLeague(request, response, session);
                        return;
                    } else {
                        LeagueModel lModel = sqlCtrlr.getLeagueModel(uModel.getLeagueId());
                        session.setAttribute("LeagueModel", lModel);
                        this.sendRedirect("goUser?r=home", request, response, session);
                        return;
                    }
                }
            }
            request.setAttribute("loginId", loginId);
            request.setAttribute("loginError", loginError);
            sendToJSP(JSPPages.HOME_PAGE, request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
            if (poolCtrlr != null) {
                poolCtrlr.closeConnection();
            }
        }
    }

    public void home(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        this.viewMainMenu(request, response, session);
    }

    public void viewMainMenu(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserModel um = (UserModel) session.getAttribute("UserModel");

        UserSQLController sqlCtrlr = null;
        ForumSQLController forumSql = null;
        PoolSQLController poolSql = null;
        try {
            sqlCtrlr = new UserSQLController();
            forumSql = new ForumSQLController();
            poolSql = new PoolSQLController();

            Collection forumMsgs = forumSql.getLatestMessages(5, um.getLeagueId());
            request.setAttribute("ForumMessageModels", forumMsgs);

            SortedMap standings = poolSql.getStandings(um.getLeagueId(), -1);
            request.setAttribute("standings", standings);

            SortedMap lockStandings = poolSql.getLockStandings(um.getLeagueId(), -1);
            request.setAttribute("lockStandings", lockStandings);

            int survivors = sqlCtrlr.getSurvivorCount(um.getLeagueId());
            request.setAttribute("survivorCount", String.valueOf(survivors));

            sendToJSP(JSPPages.GAME_HOME, request, response, session);
        } catch (Exception e) {
            throw new ProcessException(e);
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
            if (forumSql != null) {
                forumSql.closeConnection();
            }
            if (poolSql != null) {
                poolSql.closeConnection();
            }
        }
    }

    public void viewProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController sqlCtrlr = null;
        try {
            sqlCtrlr = new SeasonSQLController();
            HashMap teams = sqlCtrlr.getTeamMap(SportTypes.TYPE_NFL_FOOTBALL);
            if (teams != null) {
                request.setAttribute("teamMap", teams);
            }

            UserModel u = (UserModel) session.getAttribute("UserModel");
            request.setAttribute("UserModel", u);

            sendToJSP(JSPPages.USER_PROFILE, request, response, session);
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
        }
    }

    public void saveProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userSql = null;
        SeasonSQLController seasonSql = null;
        try {
            userSql = new UserSQLController();
            seasonSql = new SeasonSQLController();

            int userId = Integer.parseInt(request.getParameter("userId"));
            int leagueId = Integer.parseInt(request.getParameter("leagueId"));
            int defaultPick = Integer.parseInt(request.getParameter("defaultPick"));
            int favoriteTeamId = 0;
            String firstName = request.getParameter("firstName");
            String middleName = request.getParameter("middleName");
            String lastName = request.getParameter("lastName");
            String address = request.getParameter("addr");
            String email = request.getParameter("email");
            String email2 = request.getParameter("email2");
            String loginId = request.getParameter("loginId");
            String loginPwd = request.getParameter("loginPwd");
            String loginPwd2 = request.getParameter("loginPwd2");
            String favoriteTeam = request.getParameter("favoriteTeam");
            String paid = request.getParameter("paid");
            String colorTheme = request.getParameter("colorTheme");
            boolean isPaid = (paid != null && paid.equals("Y"));
            boolean disabled = false;
            boolean emailPicks = false;
            boolean sendWarning = false;

            if (!favoriteTeam.equals("")) {
                String[] s = favoriteTeam.split("\\^");
                favoriteTeamId = Integer.parseInt(s[0]);
                favoriteTeam = s[1];
            }
            UserModel um = new UserModel(userId, firstName, middleName, lastName, address, email, email2, loginId, loginPwd, leagueId, defaultPick, emailPicks, sendWarning, isPaid, favoriteTeamId, favoriteTeam, colorTheme, disabled);

            ArrayList errors = new ArrayList();
            if (firstName == null || firstName.equals("")) {
                errors.add("First name is required");
            }
            if (lastName == null || lastName.equals("")) {
                errors.add("Last name is required");
            }
            if (address != null && address.length() > 255) {
                errors.add("Address exceeds 255 character maximum");
            }
            if (email == null || email.equals("")) {
                errors.add("Email address is required");
            }
            if (loginId == null || loginId.equals("")) {
                errors.add("Login Id is required");
            }
            if (userSql.loginExists(userId, loginId)) {
                errors.add("Login Id is taken by another player");
            }
            if (loginPwd == null || loginPwd.equals("")) {
                errors.add("Password is required");
            }
            if (loginPwd2 == null || loginPwd2.equals("")) {
                errors.add("Passwords do not match");
            }
            if (loginPwd != null && loginPwd2 != null && !loginPwd.equals(loginPwd2)) {
                errors.add("Passwords do not match");
            }

            if (errors.isEmpty()) {
                if (userId > 0) {
                    userSql.updateProfile(um);
                } else {
                    userId = userSql.createProfile(um);
                }
                um = userSql.getUserModel(userId);
                session.setAttribute("userId", String.valueOf(um.getUserId()));
                session.setAttribute("UserModel", um);
                if (um.getLeagueId() <= 0) {
                    this.joinLeague(request, response, session);
                } else {
                    this.sendRedirect("goUser?r=viewMainMenu", request, response, session);
                }
            } else {
                HashMap teams = seasonSql.getTeamMap(SportTypes.TYPE_NFL_FOOTBALL);
                if (teams != null) {
                    request.setAttribute("teamMap", teams);
                }

                um.setLoginPwd("");
                request.setAttribute("UserModel", um);
                request.setAttribute("profileErrors", errors);
                if (um.getUserId() <= 0) {
                    String viewType = (String) session.getAttribute(AttributeList.SESS_VIEW_TYPE);
                    if (viewType != null && viewType.equals("MOBILE")) {
                        sendToJSP(JSPPages.SIGNUP, request, response, session);
                    } else {
                        sendToJSP(JSPPages.HOME_PAGE, request, response, session);
                    }
                } else {
                    sendToJSP(JSPPages.USER_PROFILE, request, response, session);
                }
            }
        } catch (ProcessException pe) {
            throw pe;
        } finally {
            if (userSql != null) {
                userSql.closeConnection();
            }
            if (seasonSql != null) {
                seasonSql.closeConnection();
            }
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        session.removeAttribute("UserModel");
        session.removeAttribute("userId");
        session.removeAttribute("LeagueModel");
        this.sendRedirect("goUser?r=beginLogin", request, response, session);
    }

    public void joinLeague(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController sqlCtrlr = null;
        try {
            sqlCtrlr = new UserSQLController();

            int leagueId = PropertiesHelper.getDefaultLeagueId();
            LeagueModel lm = sqlCtrlr.getLeagueModel(leagueId);

            UserModel um = (UserModel) session.getAttribute("UserModel");
            um.setLeagueId(leagueId);
            // add the new user to the default league in the database
            sqlCtrlr.setLeague(um.getUserId(), leagueId);

            session.setAttribute("UserModel", um);
            session.setAttribute("LeagueModel", lm);
            this.sendRedirect("goUser?r=home", request, response, session);
        } catch (Exception e) {
            throw new ProcessException(e);
        } finally {
            if (sqlCtrlr != null) {
                sqlCtrlr.closeConnection();
            }
        }
    }

    public void forgotPwd(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        String email = request.getParameter("email");
        String emailMsg = "";

        if (email == null || email.equals("")) {
            emailMsg = "Email address is required";
        } else {
            UserSQLController sqlCtrlr = null;
            try {
                sqlCtrlr = new UserSQLController();
                Collection userModels = sqlCtrlr.findUsersByEmail(email);

                if (userModels == null || userModels.isEmpty()) {
                    emailMsg = "Email address does not match any registered player";
                } else {
                    //send email to person
                    Iterator iter = userModels.iterator();
                    while (iter.hasNext()) {
                        UserModel um = (UserModel) iter.next();
                        if (MyEmailer.sendForgotPwd_Plain(um)) {
                            emailMsg = "Your login information has been sent";
                        } else {
                            emailMsg = "Email could not be sent. Invalid email address";
                        }
                    }
                }
            } catch (Exception e) {
                throw new ProcessException(e);
            } finally {
                if (sqlCtrlr != null) {
                    sqlCtrlr.closeConnection();
                }
            }
        }
        request.setAttribute("email", email);
        request.setAttribute("emailMsg", emailMsg);
        sendToJSP(JSPPages.FORGOT_PASSWORD, request, response, session);
    }
}
