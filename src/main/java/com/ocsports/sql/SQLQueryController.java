/*
 * Title         SQLQueryController.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.sql;
 */
package com.ocsports.sql;

import java.sql.ResultSet;

import com.ocsports.core.ProcessException;

public class SQLQueryController extends SQLBase {
    
    public SQLQueryController() throws ProcessException {
    }
    
    public void runQuery(String sql) throws ProcessException {
        this.executeUpdate(sql, (Object[])null);
    }
    
    public ResultSet runQueryWithResults(String sql) throws ProcessException {
        this.executeQuery(sql, (Object[])null);
        return this.rs;
    }
}
