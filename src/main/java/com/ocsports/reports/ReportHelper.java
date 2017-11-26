package com.ocsports.reports;

import java.util.HashMap;


public class ReportHelper {

    public static final String RPT_PUBLISHED_NOTICES = "com.ocsports.reports.PublishedNoticesReport";
    public static final String RPT_PUBLISHED_WINNERS = "com.ocsports.reports.PublishedWinnersReport";

    // holds all reports for download;
    // Map 'key' is the reportType; Map 'value' is the constructed report
    static HashMap savedReports = new HashMap();

    /*
     * @name getReport
     * @param reportType - the type of report being requested
     * @return BaseReport - the constructed report ready for reading
     */
    public static BaseReport getReport(String reportType) {
        BaseReport rpt = (BaseReport)ReportHelper.savedReports.get(reportType);
        if(rpt == null) {
            rpt = ReportHelper.constructNewReport(reportType);
            if(rpt == null) {
                return null;
            }
        }
        if(rpt.isStale()) {
            if(!rpt.build()) {
                ReportHelper.savedReports.remove(reportType);
                return null;
            }
        }
        ReportHelper.savedReports.put(reportType, rpt);
        // return a copy of the report; do not reference the original;
        return rpt.copy();
    }

    /*
     * @name setReportStale
     * @description - set the report stale when underlying data has changed
     * @param reportType - the type of reqport being requested
     */
    public static void setReportStale(String reportType) {
        BaseReport rpt = (BaseReport)ReportHelper.savedReports.get(reportType);
        if(rpt != null) {
            rpt.stale = false;
            ReportHelper.savedReports.put(reportType, rpt);
        }
    }

    public static BaseReport constructNewReport(String reportType) {
        BaseReport rpt = null;
        try {
            Class c = Class.forName(reportType);
            rpt = (BaseReport)c.newInstance();
        } catch(Exception e) {
            // do nothing for now
        }
        return rpt;
    }
}
