package site.greentable.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DbUtil {
    private static DataSource ds = null;
    private static final Object lock = new Object();

    // Docker 환경용 직접 연결 정보 (fallback)
    private static final String JDBC_URL = "jdbc:mysql://greentable_mysql:3306/greentable?serverTimezone=Asia/Seoul&useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "greentable123";

    static {
        initializeDataSource();
    }

    private static void initializeDataSource() {
        try {
            Context initContext = new InitialContext();
            // 리소스명 통일: jdbc/greentable
            ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/greentable");
            System.out.println("JNDI DataSource lookup successful");

            // 연결 테스트
            try (Connection testConn = ds.getConnection()) {
                System.out.println("JNDI DataSource connection test successful");
            }
        } catch (Exception e) {
            System.out.println("JNDI lookup failed, will use direct JDBC connection: " + e.getMessage());
            ds = null;
        }
    }

    public static Connection getConnection() throws SQLException {
        // JNDI DataSource 시도
        if (ds != null) {
            try {
                Connection conn = ds.getConnection();
                if (conn != null && !conn.isClosed()) {
                    System.out.println("JNDI connection established");
                    return conn;
                }
            } catch (SQLException e) {
                System.err.println("JNDI connection failed, falling back to direct connection: " + e.getMessage());
            }
        }

        // 직접 JDBC 연결 시도
        synchronized (lock) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                System.out.println("✅ Direct JDBC connection established");
                return conn;
            } catch (ClassNotFoundException e) {
                throw new SQLException("JDBC Driver not found", e);
            } catch (SQLException e) {
                System.err.println("❌ Direct JDBC connection failed: " + e.getMessage());
                throw e;
            }
        }
    }

    public static void dbClose(Connection con, Statement st, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            dbClose(con, st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dbClose(Connection con, Statement st) {
        try {
            if (st != null)
                st.close();
            if (con != null)
                con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}