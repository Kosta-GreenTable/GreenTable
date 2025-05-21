package site.greentable.dao;

import site.greentable.dto.ReviewDTO;
import site.greentable.dto.ReviewImageDTO;
import site.greentable.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;

public class ReviewDAOImpl implements ReviewDAO {
    private Properties proFile;

    public ReviewDAOImpl() {
        try {
            proFile = new Properties();

            // 1. 클래스패스에서 직접 로드 시도
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");

            // 2. 위 방법으로 로드 실패시 파일 시스템에서 로드 시도
            if (inputStream == null) {
                inputStream = new FileInputStream("resources/dbQuery.properties");
            }

            proFile.load(inputStream);
            inputStream.close();

        } catch (Exception e) {
            System.err.println("Properties 파일 로드 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int insertReview(ReviewDTO review) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int reviewId = 0;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.insert"), Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, review.getRating());
            ps.setString(2, review.getContent());
            ps.setInt(3, review.getProductId());
            ps.setInt(4, review.getOrderDetailId());
            ps.setInt(5, review.getUserId());

            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                reviewId = rs.getInt(1);
            }

            return reviewId;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public void insertReviewImage(ReviewImageDTO image) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.insertImage"));

            ps.setString(1, image.getRealName());
            ps.setString(2, image.getOriginalName());
            ps.setBoolean(3, image.isMain());
            ps.setInt(4, image.getReviewId());

            ps.executeUpdate();
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public List<ReviewDTO> getProductReviews(int productId, int offset, int limit) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReviewDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.getByProduct"));

            ps.setInt(1, productId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);

            rs = ps.executeQuery();

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setReviewId(rs.getInt("review_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getInt("rating"));
                review.setContent(rs.getString("content"));
                review.setCreatedAt(rs.getTimestamp("created_at"));
                review.setOrderDetailId(rs.getInt("order_detail_id"));
                review.setUserName(rs.getString("user_name"));

                // 이미지 정보 설정
                review.setImages(getReviewImages(review.getReviewId()));

                list.add(review);
            }

            return list;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public List<ReviewDTO> getUserReviews(int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReviewDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.getByUser"));

            ps.setInt(1, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setReviewId(rs.getInt("review_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getInt("rating"));
                review.setContent(rs.getString("content"));
                review.setCreatedAt(rs.getTimestamp("created_at"));
                review.setOrderDetailId(rs.getInt("order_detail_id"));
                review.setProductName(rs.getString("product_name"));
                review.setProductImage(rs.getString("product_image"));

                // 이미지 정보 설정
                review.setImages(getReviewImages(review.getReviewId()));

                list.add(review);
            }

            return list;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public ReviewDTO getReview(int reviewId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ReviewDTO review = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.getById"));

            ps.setInt(1, reviewId);

            rs = ps.executeQuery();

            if (rs.next()) {
                review = new ReviewDTO();
                review.setReviewId(rs.getInt("review_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setUserId(rs.getInt("user_id"));
                review.setRating(rs.getInt("rating"));
                review.setContent(rs.getString("content"));
                review.setCreatedAt(rs.getTimestamp("created_at"));
                review.setOrderDetailId(rs.getInt("order_detail_id"));
            }

            return review;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public List<ReviewImageDTO> getReviewImages(int reviewId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReviewImageDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.getImages"));

            ps.setInt(1, reviewId);

            rs = ps.executeQuery();

            while (rs.next()) {
                ReviewImageDTO image = new ReviewImageDTO();
                image.setReviewImageId(rs.getInt("review_image_id"));
                image.setReviewId(rs.getInt("review_id"));
                image.setRealName(rs.getString("real_name"));
                image.setOriginalName(rs.getString("original_name"));
                image.setMain(rs.getBoolean("is_main"));

                list.add(image);
            }

            return list;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public void updateReview(ReviewDTO review) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.update"));

            ps.setInt(1, review.getRating());
            ps.setString(2, review.getContent());
            ps.setInt(3, review.getReviewId());

            ps.executeUpdate();
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public void deleteReview(int reviewId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.delete"));

            ps.setInt(1, reviewId);

            ps.executeUpdate();
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public void deleteReviewImages(int reviewId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.deleteImages"));

            ps.setInt(1, reviewId);

            ps.executeUpdate();
        } finally {
            DbUtil.dbClose(con, ps);
        }
    }

    @Override
    public boolean isReviewable(int userId, int productId, int orderDetailId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.isReviewable"));

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, orderDetailId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public int getReviewCount(int productId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.count"));

            ps.setInt(1, productId);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return 0;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public List<ReviewDTO> getWritableReviews(int userId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ReviewDTO> list = new ArrayList<>();

        try {
            con = DbUtil.getConnection();
            ps = con.prepareStatement(proFile.getProperty("review.getWritableReviews"));

            ps.setInt(1, userId);

            rs = ps.executeQuery();

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setOrderDetailId(rs.getInt("order_detail_id"));
                review.setProductId(rs.getInt("product_id"));
                review.setProductName(rs.getString("product_name"));
                review.setProductImage(rs.getString("product_image"));

                // 제품 옵션 정보
                String productOption = rs.getString("product_option");
                if (productOption != null) {
                    review.setContent(productOption); // 임시로 content 필드에 저장
                }

                // 기타 필요한 정보
                review.setCreatedAt(rs.getTimestamp("order_date"));

                list.add(review);
            }

            return list;
        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }

    @Override
    public int getLatestOrderDetailId(int userId, int productId) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderDetailId = 0;

        try {
            con = DbUtil.getConnection();

            // SQL 쿼리에서 order_date를 order_at으로 변경
            String sql = "SELECT od.order_detail_id FROM order_details od " +
                         "JOIN orders o ON od.order_id = o.order_id " +
                         "WHERE o.user_id = ? AND od.product_id = ? AND o.order_status = '배송완료' " + // 'DELIVERED'를 '배송완료'로 변경
                         "AND NOT EXISTS (SELECT 1 FROM product_reviews r WHERE r.order_detail_id = od.order_detail_id) " +
                         "ORDER BY o.order_at DESC LIMIT 1";  // order_date를 order_at으로 변경

            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                orderDetailId = rs.getInt("order_detail_id");
            }

            return orderDetailId;

        } finally {
            DbUtil.dbClose(con, ps, rs);
        }
    }
}