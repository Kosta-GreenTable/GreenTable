package site.greentable.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import site.greentable.dto.PointHistoryDTO;
import site.greentable.util.DbUtil;

public class PointDAOImpl implements PointDAO {
    private Properties pro;

    public PointDAOImpl() {
        try {
            pro = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
            pro.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PointHistoryDTO> getPointHistoryByUserId(int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PointHistoryDTO> pointHistoryList = new ArrayList<>();
        String sql = pro.getProperty("point.getHistoryByUserId");

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                PointHistoryDTO pointHistory = new PointHistoryDTO();
                pointHistory.setPointHistoryId(rs.getInt("point_history_id"));
                pointHistory.setUserId(rs.getInt("user_id"));
                pointHistory.setPointType(rs.getString("point_type"));
                pointHistory.setPointAmount(rs.getInt("point_amount"));
                pointHistory.setDescription(rs.getString("description"));
                pointHistory.setCreatedAt(rs.getTimestamp("created_at"));
                pointHistory.setBalanceAfter(rs.getInt("balance_after"));
                pointHistory.setOrderNo(rs.getString("order_no"));

                pointHistoryList.add(pointHistory);
            }
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }

        return pointHistoryList;
    }

    @Override
    public boolean insertPointHistory(PointHistoryDTO pointHistory) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO point_history (user_id, point_type, point_amount, description, " +
                "balance_after, order_no) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, pointHistory.getUserId());
            ps.setString(2, pointHistory.getPointType());
            ps.setInt(3, pointHistory.getPointAmount());
            ps.setString(4, pointHistory.getDescription());
            ps.setInt(5, pointHistory.getBalanceAfter());
            ps.setString(6, pointHistory.getOrderNo());

            int result = ps.executeUpdate();
            return result > 0;
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public List<PointHistoryDTO> getPointHistoryByOrderNo(String orderNo) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PointHistoryDTO> pointHistoryList = new ArrayList<>();

        String sql = "SELECT point_history_id, user_id, point_type, point_amount, description, " +
                "created_at, balance_after, order_no " +
                "FROM point_history WHERE order_no = ? ORDER BY created_at DESC";

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, orderNo);
            rs = ps.executeQuery();

            while (rs.next()) {
                PointHistoryDTO pointHistory = new PointHistoryDTO();
                pointHistory.setPointHistoryId(rs.getInt("point_history_id"));
                pointHistory.setUserId(rs.getInt("user_id"));
                pointHistory.setPointType(rs.getString("point_type"));
                pointHistory.setPointAmount(rs.getInt("point_amount"));
                pointHistory.setDescription(rs.getString("description"));
                pointHistory.setCreatedAt(rs.getTimestamp("created_at"));
                pointHistory.setBalanceAfter(rs.getInt("balance_after"));
                pointHistory.setOrderNo(rs.getString("order_no"));

                pointHistoryList.add(pointHistory);
            }
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }

        return pointHistoryList;
    }
}
