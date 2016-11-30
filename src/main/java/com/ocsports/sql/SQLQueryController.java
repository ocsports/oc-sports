package com.ocsports.sql;

import com.ocsports.core.ProcessException;
import java.sql.ResultSet;

public class SQLQueryController extends SQLBase {

    public void runQuery(String sql) throws ProcessException {
        executeUpdate(sql, (Object[]) null);
    }

    public ResultSet runQueryWithResults(String sql) throws ProcessException {
        executeQuery(sql, (Object[]) null);
        return rs;
    }
}
