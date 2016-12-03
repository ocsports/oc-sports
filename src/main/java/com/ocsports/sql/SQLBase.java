package com.ocsports.sql;

import com.ocsports.core.ProcessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public abstract class SQLBase {

    private static final int CONN_COUNT_LOG_THRESHOLD = 4;

    private static DataSource datasource = null;
    private static int connCount = 0;

    protected java.sql.ResultSet rs;

    private final Logger log = Logger.getLogger(this.getClass().getName());
    private java.sql.Connection conn;
    private java.sql.PreparedStatement stmt;

    public SQLBase() {
    }

    private boolean getDataSource() {
        if (datasource != null) {
            return true;
        }

        try {
            InitialContext initialContext = new InitialContext();
            if (initialContext == null) {
                throw new Exception("There was no InitialContext in DBBroker. We're about to have problems.");
            }
            datasource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/ocsportsDB");
            if (datasource == null) {
                throw new Exception("Could not find our DataSource in DBBroker. We're about to have problems.");
            }
            return true;
        } catch (Exception e) {
            log.error("*** " + e.getMessage());
            return false;
        }
    }

    public void openConnection() throws ProcessException {
        try {
            if (!getDataSource()) {
                throw new ProcessException("failed to open SQL connection");
            }
            if (conn == null || conn.isClosed()) {
                conn = datasource.getConnection();
                if (++connCount >= CONN_COUNT_LOG_THRESHOLD) {
                    log.info("current open connections(++): " + connCount);
                }
            }
            // always reset the statement and results when calling openConnection;
            resetStatement();
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        }
    }

    public void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                if (!conn.isClosed() && !conn.getAutoCommit()) {
                    conn.commit();
                }
                conn.close();
                if (--connCount >= CONN_COUNT_LOG_THRESHOLD) {
                    log.info("current open connections(--): " + connCount);
                }
            }
        } catch (SQLException sqle) {
            log.error("failed to close SQL connection properly: " + sqle.getMessage());
        } finally {
            rs = null;
            stmt = null;
            conn = null;
        }
    }

    public void executeUpdate(String sql, Object[] params) throws ProcessException {
        try {
            openConnection();
            stmt = conn.prepareStatement(sql);
            addParameters(params);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        } finally {
            closeConnection();
        }
    }

    public ResultSet executeQuery(String sql, Object[] params) throws ProcessException {
        try {
            openConnection();
            stmt = conn.prepareStatement(sql);
            addParameters(params);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException sqle) {
            throw new ProcessException(sqle);
        } finally {
            // cannot close connection here; close will also close resultset and statement
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
        } catch (SQLException sqle) {
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
