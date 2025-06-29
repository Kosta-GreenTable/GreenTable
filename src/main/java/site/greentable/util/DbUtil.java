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
    static DataSource ds = null;
    
 // JNDI 이름
    private static final String JNDI_NAME = "java:/comp/env/jdbc/mySql";
    
    // 직접 JDBC 접속 정보 (테스트용)
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/greentable?serverTimezone=UTC&useSSL=false";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "admin";

    static {
        try {
            Context initContext = new InitialContext();
            ds = (DataSource)initContext.lookup("java:/comp/env/jdbc/mySql");
        } catch (Exception e) {
            // 톰캣 JNDI 환경이 아니면 예외가 발생할 수 있는데, 무시하고 ds = null로 둠
        	System.out.println("JNDI lookup failed, will use direct JDBC connection");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (ds != null) {
            // JNDI 환경에서 DataSource 사용
            return ds.getConnection();
        } else {
            // JNDI 환경이 아니면 직접 JDBC 연결 (테스트용)
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("JDBC Driver not found", e);
            }
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
        }
    }

    public static void dbClose(Connection con, Statement st, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            dbClose(con, st);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dbClose(Connection con, Statement st) {
        try {
            if (st != null) st.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("DB 연결 성공!");
            } else {
                System.out.println("DB 연결 실패!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}