<%@page contentType="text/html" %>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ocsports.models.SystemNoticeModel"%>
<%@page import="com.ocsports.reports.PublishedNoticesReport"%>
<%@page import="com.ocsports.reports.PublishedWinnersReport"%>
<%@page import="com.ocsports.reports.ReportHelper"%>
<%
    Collection notices = null;
    Object rpt = ReportHelper.getReport(ReportHelper.RPT_PUBLISHED_NOTICES);
    if(rpt != null) {
        notices = (PublishedNoticesReport)rpt.getNotices();
    }
    if(notices == null) notices = new ArrayList();

    rpt = ReportHelper.getReport(ReportHelper.RPT_PUBLISHED_WINNERS);
    if(rpt != null) {
        String sysNotice = (PublishedWinnersReport)rpt.getSystemNotice();
        if(sysNotice != null && sysNotice.length() > 0) notices.add(sysNotice);
    }
%>
<h4>Recent News / League Information</h4>
<div id="msgList" style="width:100%; overflow:auto">
    <ul>
        <%if(notices.size() == 0) {%>
            <li>No messages</li>
        <%} else { %>
            <%
            Iterator iter = notices.iterator();
            while(iter.hasNext()) {
                SystemNoticeModel snm = (SystemNoticeModel)iter.next();
                %>
                <li><%=snm.getMessage()%></li>
                <%
            }
            %>
        <%}%>
    </ul>
</div>
