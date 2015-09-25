/*
 * Title         SQLBase.java
 * Created       April 1, 2004
 * Author        Paul Charlton
 * Modified      7/30/2009 - moved to package com.ocsports.sql;
 */
package com.ocsports.sql;

import com.ocsports.core.ProcessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public abstract class SQLBase {

    private static final Logger log = Logger.getLogger(SQLBase.class.getName());

//    private final String DRIVER = "com.mysql.jdbc.Driver";
//    private final String HOST = "localhost";
//    private final String DB = "comocspo_pool";
//    private final String USER = "comocspo_admin";
//    private final String PWD = "AAA---Password123";
    private java.sql.Connection conn;
    private java.sql.PreparedStatement stmt;
    protected java.sql.ResultSet rs;

    private static DataSource datasource = null;

    public SQLBase() {
        log.info("ENTER SQLBase constructor");
        loadConnectionPool();
        log.info("EXIT SQLBase constructor");
    }

    private void loadConnectionPool() {
        try {
            InitialContext initialContext = new InitialContext();
            if (initialContext == null) {
                String message = "There was no InitialContext in DBBroker. We're about to have some problems.";
                throw new Exception(message);
            }
            log.info("initialContext is valid");

            datasource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/ocsportsDB");

            if (datasource == null) {
                String message = "Could not find our DataSource in DBBroker. We're about to have problems.";
                throw new Exception(message);
            }
            log.info("datasource is valid");
        } catch (Exception e) {
            log.error("*** " + e.getMessage());
        }
    }

    public void openConnection() throws ProcessException {
        try {
            if (datasource == null) {
                loadConnectionPool();
            }
            if (conn == null || conn.isClosed()) {
                conn = datasource.getConnection();
//                Class.forName(DRIVER).newInstance();
//
//                log.info("opening sql connection");
//                String url = "jdbc:mysql://" + HOST + "/" + DB + "?user=" + USER + "&password=" + PWD;
//                conn = DriverManager.getConnection(url);
//                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//                log.info("sql connection open");

                // MySQL does not support Transactions
                /*
                 conn.setAutoCommit(false);
                 if(conn.getAutoCommit() == false) {
                 throw new ProcessException("Unable to set auto commit to false");
                 }
                 */
            }
//        } catch (java.lang.ClassNotFoundException cnfe) {
//            log.error(cnfe.getMessage());
//            throw new ProcessException(cnfe);
//        } catch (java.lang.InstantiationException ie) {
//            log.error(ie.getMessage());
//            throw new ProcessException(ie);
//        } catch (java.lang.IllegalAccessException iae) {
//            log.error(iae.getMessage());
//            throw new ProcessException(iae);
        } catch (java.sql.SQLException sqle) {
            log.error(sqle.getMessage());
            throw new ProcessException(sqle);
        }
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed() && !conn.getAutoCommit()) {
                conn.commit();
            }

            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                stmt.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        } catch (SQLException sqle) {
        } finally {
            rs = null;
            stmt = null;
            conn = null;
        }
    }

    public void executeUpdate(String sql, Object[] params) throws ProcessException {
        try {
            this.openConnection();
            this.resetStatement();
            stmt = conn.prepareStatement(sql);
            this.addParameters(params);
            //System.out.println("executeQuery=" + sql + "");
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public ResultSet executeQuery(String sql, Object[] params) throws ProcessException {
        try {
            this.openConnection();
            this.resetStatement();
            stmt = conn.prepareStatement(sql);
            this.addParameters(params);
            //System.out.println("executeQueryWithResults=" + sql + "");
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public static synchronized int getNextKey(SQLBase sqlBase, String tblName, String colName) throws ProcessException {
        String query = "UPDATE key_tbl SET val = val + 1 WHERE tbl = ? and col = ?";
        String query2 = "SELECT val FROM key_tbl WHERE tbl = ? and col = ?";
        String query3 = "SELECT MAX(" + colName + ") FROM " + tblName;
        String query4 = "INSERT INTO key_tbl( tbl, col, val ) VALUES(?,?,?)";

        int nextKey = -1;

        ResultSet rs = null;
        try {
            sqlBase.executeUpdate(query, new Object[]{tblName, colName});

            rs = sqlBase.executeQuery(query2, new Object[]{tblName, colName});

            if (rs == null || !rs.next()) {
                // not found in key_tbl; get max + 1
                rs = sqlBase.executeQuery(query3, null);
                if (rs == null || !rs.next()) {
                    nextKey = 1;
                } else {
                    nextKey = rs.getInt(1) + 1;
                }
                sqlBase.executeUpdate(query4, new Object[]{tblName, colName, new Integer(nextKey)});
            } else {
                //already updated val + 1 in query 1;
                nextKey = rs.getInt(1);
            }
        } catch (java.sql.SQLException sqle) {
            throw new ProcessException(sqle);
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            rs = null;
        }

        return nextKey;
    }

    private void resetStatement() throws ProcessException {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            } finally {
                rs = null;
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
            } finally {
                stmt = null;
            }
        }
    }

    private void addParameters(Object[] params) throws ProcessException {
        try {
            if (params != null && params.length > 0) {
                for (int i = 0, len = params.length; i < len; i++) {
                    if (params[i] == null) {
                        stmt.setString(i + 1, "");
                    } else if (params[i] instanceof java.lang.String) {
                        String s = (params[i] == null ? "" : (String) params[i]);
                        stmt.setString(i + 1, s);
                    } else if (params[i] instanceof java.lang.Integer) {
                        int n = (params[i] == null ? -1 : ((Integer) params[i]).intValue());
                        stmt.setInt(i + 1, n);
                    } else if (params[i] instanceof java.lang.Long) {
                        long l = (params[i] == null ? -1 : ((Long) params[i]).longValue());
                        stmt.setLong(i + 1, l);
                    } else if (params[i] instanceof java.lang.Float) {
                        float f = (params[i] == null ? -1 : ((Float) params[i]).floatValue());
                        stmt.setFloat(i + 1, f);
                    } else if (params[i] instanceof java.util.Date) {
                        java.sql.Timestamp t = null;
                        if (params[i] != null) {
                            t = new java.sql.Timestamp(((java.util.Date) params[i]).getTime());
                        }
                        stmt.setTimestamp(i + 1, t);
                    } else if (params[i] instanceof java.sql.Timestamp) {
                        stmt.setTimestamp(i + 1, (java.sql.Timestamp) params[i]);
                    } else if (params[i] instanceof java.sql.Date) {
                        stmt.setDate(i + 1, (java.sql.Date) params[i]);
                    } else if (params[i] instanceof java.lang.Boolean) {
                        stmt.setBoolean(i + 1, ((java.lang.Boolean) params[i]).booleanValue());
                    }
                }
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public void commit() throws ProcessException {
        try {
            if (!conn.getAutoCommit()) {
                conn.commit();
            }
        } catch (SQLException sqle) {
            try {
                conn.rollback();
            } catch (SQLException sqle2) {
            }
            throw new ProcessException(sqle);
        }
    }

    public void rollback() throws ProcessException {
        try {
            if (!conn.getAutoCommit()) {
                conn.rollback();
            }
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }
}
