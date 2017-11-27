<%@page contentType="text/html" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ocsports.models.SystemNoticeModel"%>
<%@page import="com.ocsports.reports.PublishedNoticesReport"%>
<%@page import="com.ocsports.reports.PublishedWinnersReport"%>
<%@page import="com.ocsports.reports.ReportHelper"%>
<%
    ArrayList noticeList = new ArrayList();

    // first, gather any published system notices and add to the list
    Object rpt = ReportHelper.getReport(ReportHelper.RPT_PUBLISHED_NOTICES);
    if(rpt != null) {
        Collection sysNotices = (PublishedNoticesReport)rpt.getNotices();
        if(sysNotices != null && sysNotices.size() > 0) {
            Iterator sysIter = sysNotices.iterator();
            while(sysIter.hasNext()) {
                SystemNoticeModel snm = (SystemNoticeModel)sysIter.next();
                noticeList.add(ssm.getMessage());
            }
    }

    // see if there is a weekly winners notice, and add that to the list
    rpt = ReportHelper.getReport(ReportHelper.RPT_PUBLISHED_WINNERS);
    if(rpt != null) {
        String notice = (PublishedWinnersReport)rpt.getSystemNotice();
        if(notice != null && notice.length() > 0) noticeList.add(notice);
    }
%>
<h4>Recent News / League Information</h4>
<div id="msgList" style="width:100%; overflow:auto">
    <ul>
        <%if(noticeList.size() == 0) {%>
            <li>No messages</li>
        <%} else { %>
            <%
            Iterator iter = noticeList.iterator();
            while(iter.hasNext()) {
                %>
                <li><%=(String)iter.next()%></li>
                <%
            }
            %>
        <%}%>
    </ul>
</div>
