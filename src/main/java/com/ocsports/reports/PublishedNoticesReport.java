package com.ocsports.reports;

import java.util.Collection;
import com.ocsports.sql.UserSQLController;


public class PublishedNoticesReport extends BaseReport {

    private Collection notices = null;

    public PublishedNoticesReport() {
        super(); // set/use report defaults
    }

    public Collection getNotices() {
        return this.notices;
    }

    public boolean build() {
        UserSQLController userSql = null;
        try {
            userSql = new UserSQLController();
            this.notices = userSql.getSystemNotices(-1, true);
        }
        catch(Exception e) {
            return buildFailed();
        }
        finally {
            if (userSql != null) {
                userSql.closeConnection();
                userSql = null;
            }
        }
        return buildSuccessful();
    }

    public BaseReport copy() {
        PublishedNoticesReport rpt = new PublishedNoticesReport();
        rpt.notices = this.notices;
        return rpt;
    }
}
