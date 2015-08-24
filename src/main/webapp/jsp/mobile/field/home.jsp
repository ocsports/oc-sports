<%@page contentType="text/html"%>
<%@page import="java.util.*,java.text.*"%>
<%@ page import="com.ocsports.models.*" %>
<%
    UserModel userModel = (UserModel)session.getAttribute("UserModel");
    Collection messages = (Collection)request.getAttribute("ForumMessageModels");
    SortedMap standings = (SortedMap)request.getAttribute("standings");
    SortedMap lockStandings = (SortedMap)request.getAttribute("lockStandings");
    int survivorCount = Integer.parseInt( (String)request.getAttribute("survivorCount") );
    
    SimpleDateFormat fmt = new SimpleDateFormat();
    Iterator iter = null;
%>
<jsp:include page="/jsp/mobile/field/page_header.jsp" />

<jsp:include page="/jsp/mobile/field/page_footer.jsp" />
