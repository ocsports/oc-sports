/*
 * Title         AdminServlet.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.servlets;
 */
package com.ocsports.servlets;

import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.servlet.http.*;
import com.ocsports.core.*;
import com.ocsports.models.*;
import com.ocsports.sql.*;

public class AdminServlet extends ServletBase {
    public static final String LOGIN_SESSION_ATTR = "sessAdminLogin";
    public static final String DETAIL_PAGE_ATTR = "detailPage";

    private final String ADMIN_PWD = "ChickenOrTheEgg";

    public AdminServlet() {
		super();
    }

	public void defaultAction(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			beginLogin(request, response, session);
		}
		catch( ProcessException pe) {
			handleException(request, response, session, pe);
		}
	}

    public void beginLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        String attr = (String)session.getAttribute( LOGIN_SESSION_ATTR );
        if(attr != null && attr.length() > 0 ) {
            this.seasons(request, response, session);
            return;
        }
        sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
    }
    
	public void login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
		session.removeAttribute( LOGIN_SESSION_ATTR );
        ArrayList errors = new ArrayList();

		String pwd = request.getParameter("adminPwd");

        if( pwd == null || !pwd.equals(ADMIN_PWD) ) {
            errors.add("Invalid password. Access Denied!!!");
            request.setAttribute( "loginErrors", errors );
            sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
        }
        else {
            session.setAttribute( LOGIN_SESSION_ATTR, "Y" );
            this.sendRedirect( "goAdmin?r=seasons", request, response, session );
        }
    }
    
    public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        session.removeAttribute(LOGIN_SESSION_ATTR);
        sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
    }

    // ***** SEASONS - SERIES - GAMES ***** //

    public void seasons(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController sqlCtrlr = null;
        try {
            sqlCtrlr = new SeasonSQLController();

            Collection seasons = sqlCtrlr.getSeasons( SportTypes.TYPE_NFL_FOOTBALL, false );
            request.setAttribute( "SeasonModels", seasons );

            int seasonId = -1;
            int seriesId = -1;
            String season = request.getParameter( "seasonId" );
            String series = request.getParameter( "seriesId" );
            if( season == null && series == null ) {
                Iterator iter = seasons.iterator();
                while( iter.hasNext() ) {
                    SeasonModel sm = (SeasonModel)iter.next();
                    if( sm.isActive() ) {
                        seasonId = sm.getId();
                        break;
                    }
                }
                if(seasonId > 0) seriesId = sqlCtrlr.getCurrentSeries( seasonId );
            } 
            else {
                if( season != null && season.length() > 0 ) seasonId = Integer.parseInt( season );
                if( series != null && series.length() > 0 ) seriesId = Integer.parseInt( series );
            }

            if( seasonId > 0 ) {
                Collection seriesModels = sqlCtrlr.getSeriesBySeason(seasonId);
                request.setAttribute("SeriesModels", seriesModels);
            }

            if( seriesId > 0 ) {
                Collection gameModels = sqlCtrlr.getGamesBySeries( seriesId );
                request.setAttribute("GameModels", gameModels);
            }

            HashMap teamMap = (HashMap)session.getAttribute( "teamMap" );
            if( teamMap == null ) {
                teamMap = sqlCtrlr.getTeamMap( SportTypes.TYPE_NFL_FOOTBALL );
                session.setAttribute( "teamMap", teamMap );
            }

            request.setAttribute( "seasonId", String.valueOf(seasonId) );
            request.setAttribute( "seriesId", String.valueOf(seriesId) );
            request.setAttribute( DETAIL_PAGE_ATTR, JSPPages.ADMIN_SEASONS );
            this.sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
        }
        catch(ProcessException pe) {
            throw pe;
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(sqlCtrlr != null) sqlCtrlr.closeConnection();
        }
    }
    
    public void saveSeason(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController seasonCtrlr = null;
        try {
            seasonCtrlr = new SeasonSQLController();
            
            int seasonId = Integer.parseInt( request.getParameter("updateSeasonId") );
            int sportType = Integer.parseInt( request.getParameter("sportType") );
            String name = request.getParameter("name");
            String prefix = request.getParameter("prefix");
            boolean active = ( request.getParameter("act") != null );
            
            ArrayList errors = new ArrayList();
            if(sportType <= 0) errors.add( "Sport type is required" );
            if(name == null || name.length() == 0) errors.add("Name is required");
            if(prefix == null || prefix.length() == 0) errors.add("Series prefix is required");
            
            if( errors.isEmpty() ) {
                seasonCtrlr.updateSeason(seasonId, sportType, name, prefix, active);
            }
            
            this.seasons(request, response, session);
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(seasonCtrlr != null) seasonCtrlr.closeConnection();
        }
    }

    public void saveSeries(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController seasonCtrlr = null;
        try {
            seasonCtrlr = new SeasonSQLController();
            SimpleDateFormat fmt = new SimpleDateFormat();
            fmt.applyPattern("MM/dd/yyyy HH:mm");

            int seasonId = Integer.parseInt( request.getParameter("updateSeasonId") );
            int seriesId = Integer.parseInt( request.getParameter("updateSeriesId") );
            long startDate = fmt.parse( request.getParameter("startDate") + " 00:00" ).getTime();
            long endDate = fmt.parse( request.getParameter("endDate") + " 23:59" ).getTime();
            boolean published = ( request.getParameter("pub") != null );
            boolean cleanup = ( request.getParameter("cleanup") != null );
            boolean reminder = ( request.getParameter("remind") != null );

            seasonCtrlr.updateSeasonSeries(seriesId, startDate, endDate, published, cleanup, reminder);
            this.seasons(request, response, session);
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(seasonCtrlr != null) seasonCtrlr.closeConnection();
        }
    }
    
    public void saveGame(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController sqlCtrlr = null;
        PoolSQLController poolCtrlr = null;
        try {
            sqlCtrlr = new SeasonSQLController();
            poolCtrlr = new PoolSQLController();

            SimpleDateFormat fmt = new SimpleDateFormat();
            fmt.applyPattern("MM/dd/yyyy h:mm a");

            GameModel gm = new GameModel();
            gm.setId( Integer.parseInt(request.getParameter("gameId")) );
            gm.setSeriesId( Integer.parseInt(request.getParameter("seriesId")) );
            gm.setStartDate( fmt.parse(request.getParameter("start")).getTime() );
            gm.setAwayTeamId( Integer.parseInt(request.getParameter("awayTeam")) );
            gm.setHomeTeamId( Integer.parseInt(request.getParameter("homeTeam")) );
            gm.setSpread( Float.parseFloat(request.getParameter("spread")) );
            gm.setAwayScore( Integer.parseInt(request.getParameter("awayScore")) );
            gm.setHomeScore( Integer.parseInt(request.getParameter("homeScore")) );
            String post = request.getParameter("posted");
            gm.setPosted( (post != null && post.equals("Y")) );
            //gm.setNotes( request.getParameter("notes") );

            sqlCtrlr.updateGame( gm );
            if( gm.isPosted() ) poolCtrlr.postGame( gm );

            this.seasons(request, response, session);
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(sqlCtrlr != null) sqlCtrlr.closeConnection();
            if(poolCtrlr != null) poolCtrlr.closeConnection();
        }
    }

    // ***** PLAYERS ***** //

    public void players(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        SeasonSQLController seasonCtrlr = null;
        try {
            seasonCtrlr = new SeasonSQLController();
            userCtrlr = new UserSQLController();

            int orderBy = UserSQLController.USERS_ORDER_BY_NAME;
            int leagueId = -1;

            String league = request.getParameter( "leagueId" );
            String order = request.getParameter( "orderBy" );
            if( order != null && order.length() > 0 ) orderBy = Integer.parseInt( order );
            if( league != null && league.length() > 0 ) leagueId = Integer.parseInt( league );

            if( leagueId <= 0 ) leagueId = userCtrlr.getActiveLeagueId();
            request.setAttribute( "orderBy", String.valueOf(orderBy) );
            request.setAttribute( "leagueId", String.valueOf(leagueId) );

            Collection userModels = userCtrlr.getUsersByLeague( leagueId, orderBy );
            request.setAttribute( "UserModels", userModels );

            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.ADMIN_PLAYERS);
            this.sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userCtrlr != null) userCtrlr.closeConnection();
            if(seasonCtrlr != null) seasonCtrlr.closeConnection();
        }
    }

    public void savePlayer(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        try {
            userCtrlr = new UserSQLController();

            int userId = Integer.parseInt( request.getParameter("userId") );
            UserModel um = userCtrlr.getUserModel( userId );

            um.setFirstName( request.getParameter("firstName") );
            um.setLastName( request.getParameter("lastName") );
            um.setAddress( request.getParameter("addr") );
            um.setEmail( request.getParameter("email") );
            um.setEmail2( request.getParameter("email2") );
            um.setLoginId( request.getParameter("loginId") );
            um.setLoginDisabled( (request.getParameter("disabld") != null) );
            userCtrlr.updateProfile( um );

            this.sendRedirect( "goAdmin?r=players", request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userCtrlr != null) userCtrlr.closeConnection();
        }
    }

    public void savePlayerPayment(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        try {
            userCtrlr = new UserSQLController();

            int userId = Integer.parseInt( request.getParameter("userId") );
            int payStatus = Integer.parseInt( request.getParameter("payStatus") );

            UserModel um = userCtrlr.getUserModel( userId );
            um.setPaid( (payStatus == 1) );
            userCtrlr.updateProfile( um );

            this.sendRedirect( "goAdmin?r=players", request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userCtrlr != null) userCtrlr.closeConnection();
        }
    }

    public void deletePlayer(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        try {
            userCtrlr = new UserSQLController();

            int userId = Integer.parseInt( request.getParameter("userId") );
            userCtrlr.deleteUser(userId);

            this.sendRedirect( "goAdmin?r=players", request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userCtrlr != null) userCtrlr.closeConnection();
        }
    }

    // ***** SYSTEM NOTICES ***** //
    
    public void systemNotices(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userSQL = null;
        try {
            userSQL = new UserSQLController();

            Collection notices = userSQL.getSystemNotices(0, false);
            request.setAttribute("SystemNoticeModels", notices);

            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.ADMIN_NOTICES);
            this.sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userSQL != null) userSQL.closeConnection();
        }
    }

    public void saveSystemNotice(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        try {
            userCtrlr = new UserSQLController();
            
            int noticeId = Integer.parseInt( request.getParameter("noticeId") );
            String msg = request.getParameter( "msg" );
            String pub = request.getParameter("pub");
            boolean publish = ( pub != null && pub.length() > 0 );

            if( noticeId > 0 )
                userCtrlr.updateSystemNotice(noticeId, msg, publish);
            else
                userCtrlr.createSystemNotice(msg, publish);
                
            this.systemNotices(request, response, session);
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userCtrlr != null) userCtrlr.closeConnection();
        }
    }
    
    public void deleteSystemNotice(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        try {
            userCtrlr = new UserSQLController();

            int noticeId = Integer.parseInt( request.getParameter("noticeId") );
            userCtrlr.deleteSystemNotice(noticeId);

            this.systemNotices(request, response, session);
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userCtrlr != null) userCtrlr.closeConnection();
        }
    }
    
    // ***** EMAIL PLAYERS ***** //
    
    public void email(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        try {
            userCtrlr = new UserSQLController();

            int leagueId = -1;
            String league = (String)request.getParameter("leagueId");
            if( league != null && league.length() > 0 ) leagueId = Integer.parseInt( league );
            if( leagueId <= 0 ) leagueId = userCtrlr.getActiveLeagueId();
            request.setAttribute( "leagueId", String.valueOf(leagueId) );

            Collection users = userCtrlr.getUsersByLeague( leagueId, UserSQLController.USERS_ORDER_BY_NAME );
            if( users != null ) request.setAttribute( "UserModels", users );

            request.setAttribute( DETAIL_PAGE_ATTR, JSPPages.ADMIN_EMAIL );
            this.sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if( userCtrlr != null ) userCtrlr.closeConnection();
        }
    }

    public void sendEmail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        try {
            userCtrlr = new UserSQLController();
            
            int leagueId = Integer.parseInt( request.getParameter("leagueId") );
            String contentType = request.getParameter("contentType");
            String subject = request.getParameter("subject");
            String msg = request.getParameter("msg");
            String[] userList = request.getParameterValues("userIds");
            if( contentType == null || contentType.length() == 0 ) contentType = MyEmailer.CONTENT_HTML;

            if(subject != null && msg != null && userList != null) {
                ArrayList emailAddrs = new ArrayList();
                for(int i=0, len=userList.length; i < len; i++) {
                    int userId = Integer.parseInt( userList[i] );
                    if( userId == -99 ) {
                        Collection userModels = userCtrlr.getUsersByLeague( leagueId, UserSQLController.USERS_ORDER_BY_EMAIL );
                        Iterator iter = userModels.iterator();
                        while( iter.hasNext() ) {
                            UserModel um = (UserModel)iter.next();
                            if( um.getEmail() != null && um.getEmail().length() > 0 ) {
                                emailAddrs.add( um.getEmail() );
                            }
                            if( um.getEmail2() != null && um.getEmail2().length() > 0 ) {
                                emailAddrs.add( um.getEmail2() );
                            }
                        }
                        break;
                    }
                    else {
                        UserModel um = userCtrlr.getUserModel( userId );
                        if( um.getEmail() != null && um.getEmail().length() > 0 ) {
                            emailAddrs.add( um.getEmail() );
                        }
                        if( um.getEmail2() != null && um.getEmail2().length() > 0 ) {
                            emailAddrs.add( um.getEmail2() );
                        }
                    }
                }
                String[] bccRecips = (String[])emailAddrs.toArray( new String[emailAddrs.size()] );
                MyEmailer.sendEmailMsg( null, null, bccRecips, subject, msg, contentType );
            }

            this.sendRedirect( "goAdmin?r=email", request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userCtrlr != null) userCtrlr.closeConnection();
        }
    }

    // ***** TEAMS - DIVISIONS - CONFERENCES ***** //
    
    public void teams(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController seasonCtrlr = null;
        try {
            seasonCtrlr = new SeasonSQLController();
            
            Collection divisions = seasonCtrlr.getTeamDivisionList( SportTypes.TYPE_NFL_FOOTBALL, -1);
            request.setAttribute( "TeamDivisionModels", divisions );

            Collection teamModels = seasonCtrlr.getTeamList( SportTypes.TYPE_NFL_FOOTBALL, SeasonSQLController.TEAMS_ORDER_BY_CITY );
            request.setAttribute( "TeamModels", teamModels );

            request.setAttribute( DETAIL_PAGE_ATTR, JSPPages.ADMIN_TEAMS );
            this.sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(seasonCtrlr != null) seasonCtrlr.closeConnection();
        }
    }
    
    public void saveTeam(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController seasonCtrlr = null;
        try {
            seasonCtrlr = new SeasonSQLController();
            
            int teamId = Integer.parseInt( request.getParameter("teamId") );
            TeamModel tm = seasonCtrlr.getTeamModel( teamId );
            
            tm.setAbrv( request.getParameter("abrv") );
            tm.setName( request.getParameter("nm") );
            tm.setCity( request.getParameter("city") );
            tm.setDivision( Integer.parseInt( request.getParameter("div") ) );
            tm.setDome( (request.getParameter("dome") != null) );
            tm.setTurf( (request.getParameter("turf") != null) );
            tm.setWeatherURL( request.getParameter("weather") );
            seasonCtrlr.updateTeam( tm );

            this.sendRedirect( "goAdmin?r=teams", request, response, session );
        }
        catch(ProcessException pe) {
            throw pe;
        }
        finally {
            if(seasonCtrlr != null) seasonCtrlr.closeConnection();
        }
    }

    // ***** FORUM MESSAGES - SMACK TALK ***** //
    
    public void forums(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userCtrlr = null;
        ForumSQLController forumCtrlr = null;
        try {
            userCtrlr = new UserSQLController();
            forumCtrlr = new ForumSQLController();
            
            int leagueId = -1;
            int msgCount = 20;
            String league = request.getParameter( "leagueId" );
            String cnt = request.getParameter( "msgCount" );
            if( league != null && league.length() > 0 ) leagueId = Integer.parseInt( league );
            if( cnt != null && cnt.length() > 0 ) msgCount = Integer.parseInt( cnt );
            
            if( leagueId <= 0 ) leagueId = userCtrlr.getActiveLeagueId();
            Collection messages = forumCtrlr.getLatestMessages(20, leagueId);

            request.setAttribute( "leagueId", String.valueOf(leagueId) );
            request.setAttribute( "msgCount", String.valueOf(msgCount) );
            request.setAttribute( "ForumMessageModels", messages );

            request.setAttribute( DETAIL_PAGE_ATTR, JSPPages.ADMIN_FORUMS );
            this.sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if( userCtrlr != null ) userCtrlr.closeConnection();
            if( forumCtrlr != null ) forumCtrlr.closeConnection();
        }
    }
    
    public void saveForumMsg(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        ForumSQLController forumCtrlr = null;
        try {
            forumCtrlr = new ForumSQLController();

            int leagueId = Integer.parseInt( request.getParameter("leagueId") );

            int msgId = Integer.parseInt( request.getParameter("msgId") );
            String msg = request.getParameter( "msg" );
            if( msg.length() > 2000 ) {
                msg = msg.substring(0, 1999);
            }
            forumCtrlr.updateMessage(msgId, msg);

            this.sendRedirect( "goAdmin?r=forums", request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if( forumCtrlr != null ) forumCtrlr.closeConnection();
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void saveGames(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController sqlCtrlr = null;
        PoolSQLController poolCtrlr = null;
        try {
            sqlCtrlr = new SeasonSQLController();
            poolCtrlr = new PoolSQLController();

            int seasonId = Integer.parseInt( request.getParameter("seasonId") );
            int seriesId = Integer.parseInt( request.getParameter("seriesId") );

            SimpleDateFormat fmt = new SimpleDateFormat();
            fmt.applyPattern("MM/dd/yyyy HH:mm");

            int i = 0;
            while( true ) {
                String key = request.getParameter("game"+i);
                if(key == null || key.equals("")) break;
                
                GameModel gm = new GameModel();
                gm.setId( Integer.parseInt(key) );
                gm.setSeriesId( seriesId );
                gm.setStartDate(fmt.parse(request.getParameter("start"+i)).getTime());
                gm.setAwayTeamId(Integer.parseInt(request.getParameter("awayTeam"+i)));
                gm.setHomeTeamId(Integer.parseInt(request.getParameter("homeTeam"+i)));
                gm.setSpread(Float.parseFloat(request.getParameter("spread"+i)));
                gm.setAwayScore(Integer.parseInt(request.getParameter("awayScore"+i)));
                gm.setHomeScore(Integer.parseInt(request.getParameter("homeScore"+i)));
                String post = request.getParameter("posted"+i);
                gm.setPosted((post != null && post.equals("Y")));
                gm.setNotes(request.getParameter("notes"+i));

                sqlCtrlr.updateGame(gm);
                if(gm.isPosted()) poolCtrlr.postGame(gm);
                i++;
            }

            //check if we added a new game
            String startX = request.getParameter("startX");
            if(startX != null && !startX.equals("")) {
                long startDateX = fmt.parse(startX).getTime();
                int awayTeamX = Integer.parseInt(request.getParameter("awayTeamX"));
                int homeTeamX = Integer.parseInt(request.getParameter("homeTeamX"));
                String sSpread = request.getParameter("spreadX");
                if(awayTeamX > 0 && homeTeamX > 0 && sSpread != null && !sSpread.equals("")) {
                    float spread = Float.parseFloat(sSpread);
                    GameModel gm = new GameModel();
                    gm.setSeriesId(seriesId);
                    gm.setStartDate(startDateX);
                    gm.setHomeTeamId(homeTeamX);
                    gm.setAwayTeamId(awayTeamX);
                    gm.setSpread(spread);
                    gm.setNotes("");
                    sqlCtrlr.createGame(gm);
                }
            }

            this.sendRedirect("goAdmin?r=viewSeason&seasonId=" + seasonId + "&seriesId=" + seriesId, request, response, session);
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(sqlCtrlr != null) sqlCtrlr.closeConnection();
            if(poolCtrlr != null) poolCtrlr.closeConnection();
        }
    }

    public void deleteGame(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        SeasonSQLController sqlCtrlr = null;
        try {
            sqlCtrlr = new SeasonSQLController();

            int seasonId = Integer.parseInt( request.getParameter("seasonId") );
            int seriesId = Integer.parseInt( request.getParameter("seriesId") );
            int gameId = Integer.parseInt( request.getParameter("gameId") );

            sqlCtrlr.deleteGame(gameId);
            this.sendRedirect("goAdmin?r=viewSeason&seasonId=" + seasonId + "&seriesId=" + seriesId, request, response, session);
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(sqlCtrlr != null) sqlCtrlr.closeConnection();
        }
    }
    
   
    // *******************************************
    //  AUDIT LOGINS WINDOW
    // *******************************************
    public void auditLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        UserSQLController userSQL = null;
        try {
            userSQL = new UserSQLController();

            String league = request.getParameter("leagueId");
            int leagueId = (league == null || league.equals("") ? -1 : Integer.parseInt(league));

            String user = request.getParameter("userId");
            int userId = (user == null || user.equals("") ? -1 : Integer.parseInt(user));

            String changeLeague = request.getParameter("changeLeague");
            if(changeLeague != null && changeLeague.equals("Y")) userId = -1;

            request.setAttribute("leagueId", String.valueOf(leagueId));
            request.setAttribute("userId", String.valueOf(userId));

            java.util.Date timestamp = null;
            String sTimestamp = request.getParameter("timestamp");
            if(sTimestamp != null && !sTimestamp.equals("")) {
                SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                timestamp = fmt.parse(sTimestamp);
                request.setAttribute("timestamp", fmt.format(timestamp));
            }

            Collection leagues = userSQL.getLeagues();
            request.setAttribute("LeagueModels", leagues);

            if(leagueId > 0) {
                Collection users = userSQL.getUsersByLeague(leagueId, UserSQLController.USERS_ORDER_BY_NAME);
                request.setAttribute("UserModels", users);
            }

            //get the last 50 logins for everyone, or all logins for a particular user or date
            Collection logins = userSQL.getAuditLoginModels(userId, timestamp, (userId > 0 || timestamp != null ? 0 : 50));
            request.setAttribute("AuditLoginModels", logins);

            request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.ADMIN_AUDIT_LOGINS);
            this.sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
        }
        catch(Exception e) {
            throw new ProcessException(e);
        }
        finally {
            if(userSQL != null) userSQL.closeConnection();
        }
    }
    
    // *******************************************
    //  SQL QUERY WINDOW
    // *******************************************
    public void executeSQL(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        ArrayList messages = new ArrayList();
        SQLQueryController querySQL = null;
        try {
            String script = request.getParameter("query");
            if(script == null || script.trim().equals("")) {
                request.setAttribute(DETAIL_PAGE_ATTR,  JSPPages.SQL_QUERY_PAGE);
                this.sendToJSP(JSPPages.ADMIN_MAIN, request, response, session);
                return;
            }

            querySQL = new SQLQueryController();
            script = script.trim();
            request.setAttribute("query", script);

            //parse the script into individual commands
            ArrayList arrCmds = new ArrayList();
            while( script.indexOf(";") > 0 ) {
                int n = script.indexOf(";");
                arrCmds.add( script.substring(0, n).trim() );
                if(n == script.length())
                    script = "";
                else
                    script = script.substring(n+1).trim();
            }
            //any text remaining after last semicolon? must be the last command
            if(script.length() > 0) {
                arrCmds.add(script.trim());
            }

            //run each command found in the script
            //only run one query with results;
            if(!arrCmds.isEmpty()) {
                Iterator iter = arrCmds.iterator();
                while(iter.hasNext()) {
                    String cmd = (String)iter.next();
                    String testCmd = cmd.toUpperCase();

                    try {
                        if(testCmd.indexOf("INSERT") == 0 || testCmd.indexOf("UPDATE") == 0 || testCmd.indexOf("DELETE") == 0) {
                            querySQL.runQuery(cmd);
                            messages.add("SQL Command Executed Successfully");
                        }
                        else {
                            this.parseSQLResults( querySQL.runQueryWithResults(cmd), request );
                            break;
                        }
                    }
                    catch(Exception e2) {
                        messages.add("SQL Command Failed: " + e2.getMessage());
                    }
                }
            }
        }
        catch(Exception e) {
            messages.add(e.getClass().getName() + ": " + e.getMessage());
            try {
                if(querySQL != null) querySQL.rollback();
            }
            catch(Exception e2) {
                messages.add("Unable to Rollback Transaction");
            }
        }
        finally {
            if(querySQL != null) querySQL.closeConnection();
        }

        request.setAttribute("messages", messages);
        request.setAttribute(DETAIL_PAGE_ATTR,  JSPPages.SQL_QUERY_PAGE);
        this.sendToJSP(JSPPages.ADMIN_MAIN, request, response, session);
    }

    private void parseSQLResults(ResultSet rs, HttpServletRequest request) throws java.sql.SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();

        for( int c=1; c <= colCount; c++) {
            String header = md.getColumnLabel(c);
            request.setAttribute("header_col" + c, header);
        }

        int rowCount = 0;
        Object obj = null;

        rs.beforeFirst();
        while(rs.next()) {
            for( int i=1, len = colCount; i <= colCount; i++) {
                try {
                    obj = rs.getObject(i);
                }
                catch(Exception e) {
                    obj = null;
                }
                request.setAttribute("data_row" + rowCount + "_col" + i, (obj == null ? "" : obj.toString()) );
            }
            rowCount++;
        }

        request.setAttribute("rowCount", String.valueOf(rowCount));
        request.setAttribute("colCount", String.valueOf(colCount));
        request.setAttribute("hasResults", "Y");
    }

    // *******************************************
    //  SYSTEM NOTICES WINDOW
    // *******************************************
    
    // *******************************************
    //  HTML WINDOW
    // *******************************************
    public void viewHtml(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ProcessException {
        String url = request.getParameter("url");
        if(url != null && url.length() > 0) {
            String source = "";
            source = MyHTTPClient.getURL(url);
            request.setAttribute("source", source);
            request.setAttribute("url", url);
        }
        request.setAttribute(DETAIL_PAGE_ATTR, JSPPages.ADMIN_HTML);
        this.sendToJSP( JSPPages.ADMIN_MAIN, request, response, session );
    }
}
