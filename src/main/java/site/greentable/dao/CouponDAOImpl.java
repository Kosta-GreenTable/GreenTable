package site.greentable.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import site.greentable.dto.CouponDTO;
import site.greentable.util.DbUtil;

public class CouponDAOImpl implements CouponDAO {
    private Properties pro;

    public CouponDAOImpl() {
        pro = new Properties();
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
            if (input != null) {
                pro.load(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CouponDTO> getAvailableCouponsByUserId(int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CouponDTO> couponList = new ArrayList<>();

        String sql = "SELECT ic.issued_coupon_id, c.coupon_id, c.coupon_name, c.discount_rate, " +
                "c.min_order_price, c.coupon_grade, c.expiration_date, ic.issued_date, " +
                "ic.is_used, ic.used_date " +
                "FROM issued_coupons ic " +
                "JOIN coupons c ON ic.coupon_id = c.coupon_id " +
                "WHERE ic.user_id = ? AND ic.is_used = 0 AND c.expiration_date > NOW() " +
                "ORDER BY c.expiration_date ASC";

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                CouponDTO coupon = new CouponDTO();
                coupon.setCouponId(rs.getInt("coupon_id"));
                coupon.setUserId(userId);
                coupon.setCouponName(rs.getString("coupon_name"));
                coupon.setCouponType("PERCENT"); // 기존 DB에서는 discount_rate가 퍼센트
                coupon.setDiscountValue(rs.getInt("discount_rate"));
                coupon.setMinOrderAmount(rs.getInt("min_order_price"));
                coupon.setCategory(rs.getString("coupon_grade"));
                coupon.setStatus("AVAILABLE");
                coupon.setValidFrom(rs.getTimestamp("issued_date"));
                coupon.setValidTo(rs.getTimestamp("expiration_date"));
                coupon.setUsedAt(rs.getTimestamp("used_date"));
                coupon.setCreatedAt(rs.getTimestamp("issued_date"));

                couponList.add(coupon);
            }
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }

        return couponList;
    }

    @Override
    public List<CouponDTO> getUsedCouponsByUserId(int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CouponDTO> couponList = new ArrayList<>();

        String sql = "SELECT ic.issued_coupon_id, c.coupon_id, c.coupon_name, c.discount_rate, " +
                "c.min_order_price, c.coupon_grade, c.expiration_date, ic.issued_date, " +
                "ic.is_used, ic.used_date " +
                "FROM issued_coupons ic " +
                "JOIN coupons c ON ic.coupon_id = c.coupon_id " +
                "WHERE ic.user_id = ? AND ic.is_used = 1 " +
                "ORDER BY ic.used_date DESC";

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                CouponDTO coupon = new CouponDTO();
                coupon.setCouponId(rs.getInt("coupon_id"));
                coupon.setUserId(userId);
                coupon.setCouponName(rs.getString("coupon_name"));
                coupon.setCouponType("PERCENT");
                coupon.setDiscountValue(rs.getInt("discount_rate"));
                coupon.setMinOrderAmount(rs.getInt("min_order_price"));
                coupon.setCategory(rs.getString("coupon_grade"));
                coupon.setStatus("USED");
                coupon.setValidFrom(rs.getTimestamp("issued_date"));
                coupon.setValidTo(rs.getTimestamp("expiration_date"));
                coupon.setUsedAt(rs.getTimestamp("used_date"));
                coupon.setCreatedAt(rs.getTimestamp("issued_date"));

                couponList.add(coupon);
            }
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }

        return couponList;
    }

    @Override
    public List<CouponDTO> getExpiredCouponsByUserId(int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CouponDTO> couponList = new ArrayList<>();

        String sql = "SELECT ic.issued_coupon_id, c.coupon_id, c.coupon_name, c.discount_rate, " +
                "c.min_order_price, c.coupon_grade, c.expiration_date, ic.issued_date, " +
                "ic.is_used, ic.used_date " +
                "FROM issued_coupons ic " +
                "JOIN coupons c ON ic.coupon_id = c.coupon_id " +
                "WHERE ic.user_id = ? AND ic.is_used = 0 AND c.expiration_date <= NOW() " +
                "ORDER BY c.expiration_date DESC";

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                CouponDTO coupon = new CouponDTO();
                coupon.setCouponId(rs.getInt("coupon_id"));
                coupon.setUserId(userId);
                coupon.setCouponName(rs.getString("coupon_name"));
                coupon.setCouponType("PERCENT");
                coupon.setDiscountValue(rs.getInt("discount_rate"));
                coupon.setMinOrderAmount(rs.getInt("min_order_price"));
                coupon.setCategory(rs.getString("coupon_grade"));
                coupon.setStatus("EXPIRED");
                coupon.setValidFrom(rs.getTimestamp("issued_date"));
                coupon.setValidTo(rs.getTimestamp("expiration_date"));
                coupon.setUsedAt(rs.getTimestamp("used_date"));
                coupon.setCreatedAt(rs.getTimestamp("issued_date"));

                couponList.add(coupon);
            }
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }

        return couponList;
    }

    @Override
    public boolean useCoupon(int issuedCouponId, String orderNo) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        String sql = "UPDATE issued_coupons SET is_used = 1, used_date = NOW() " +
                "WHERE issued_coupon_id = ? AND is_used = 0";

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, issuedCouponId);

            int result = ps.executeUpdate();
            return result > 0;
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public boolean issueCouponToUser(int userId, int couponId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO issued_coupons (user_id, coupon_id, issued_date, is_used) " +
                "VALUES (?, ?, NOW(), 0)";

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, couponId);

            int result = ps.executeUpdate();
            return result > 0;
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public int getCouponIdByCode(String couponCode) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // 쿠폰 코드 기능이 없다면 쿠폰 이름으로 검색
        String sql = "SELECT coupon_id FROM coupons WHERE coupon_name = ?";

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, couponCode);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("coupon_id");
            }
            return 0;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }
}
