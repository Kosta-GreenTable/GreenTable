package site.greentable.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import site.greentable.dto.Product;
import site.greentable.dto.ProductDetail;
import site.greentable.dto.ProductImage;
import site.greentable.util.DbUtil;

public class ProductDAOImpl implements ProductDAO {
    private Properties proFile = new Properties();
    private static final int PAGE_SIZE = 5; // 한 페이지에 표시할 상품 수

    public ProductDAOImpl() {
        try {
            // properties 파일 로딩
            InputStream is = getClass().getClassLoader().getResourceAsStream("dbQuery.properties");
            proFile.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> selectAll(int pageNo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Product> list = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("product.getList"));

            // 페이징 처리를 위한 시작 인덱스 계산
            int startIndex = (pageNo - 1) * PAGE_SIZE;
            pstmt.setInt(1, startIndex);
            pstmt.setInt(2, PAGE_SIZE);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setSubName(rs.getString("sub_name"));
                product.setPrice(rs.getInt("price"));
                product.setStock(rs.getInt("stock"));
                product.setDiscountRate(rs.getInt("discount_rate"));
                product.setCategory(rs.getString("category"));
                product.setMainImageName(rs.getString("image_name"));

                list.add(product);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return list;
    }

    @Override
    public List<Product> selectByCategory(String category, int pageNo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Product> list = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("product.getByCategory"));

            int startIndex = (pageNo - 1) * PAGE_SIZE;
            pstmt.setString(1, category);
            pstmt.setInt(2, startIndex);
            pstmt.setInt(3, PAGE_SIZE);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setSubName(rs.getString("sub_name"));
                product.setPrice(rs.getInt("price"));
                product.setDiscountRate(rs.getInt("discount_rate"));
                product.setCategory(rs.getString("category"));
                product.setMainImageName(rs.getString("image_name"));

                // 새로운 필드 설정
                product.setRating(rs.getDouble("rating"));
                product.setReviewCount(rs.getInt("review_count"));

                // 일단 인기도, 설명, 썸네일 등을 임시값으로 설정 (나중에 DB 필드 추가 필요)
                product.setPopularity(90 + (int) (Math.random() * 10));
                product.setDescription(rs.getString("sub_name"));
                product.setThumbnailUrl("products/images/" + rs.getString("image_name"));

                list.add(product);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return list;
    }

    @Override
    public Product selectProductDetail(int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Product product = null;

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("product.getDetail"));
            pstmt.setInt(1, productId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setProductId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setSubName(rs.getString("sub_name"));
                product.setPrice(rs.getInt("price"));
                product.setStock(rs.getInt("stock"));
                product.setCategory(rs.getString("category"));
                product.setDiscountRate(rs.getInt("discount_rate"));
                product.setMainImageName(rs.getString("image_name"));

                // 상품 상세정보 설정
                ProductDetail detail = new ProductDetail();
                detail.setProductId(rs.getInt("product_id"));
                detail.setDescription(rs.getString("description"));
                detail.setIngredients(rs.getString("ingredients"));
                detail.setKcal(rs.getInt("kcal"));
                detail.setAmount(rs.getInt("amount"));
                detail.setNutrition(rs.getString("nutrition"));
                detail.setCreatedDate(rs.getDate("created_date"));
                detail.setUpdatedDate(rs.getDate("updated_date"));

                product.setProductDetail(detail);

                // 이미지 목록 조회하여 설정
                List<ProductImage> images = selectProductImages(productId);
                product.setProductImages(images);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return product;
    }

    @Override
    public List<ProductImage> selectProductImages(int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ProductImage> images = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("product.getImages"));
            pstmt.setInt(1, productId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                ProductImage image = new ProductImage();
                image.setImageName(rs.getString("image_name"));
                image.setProductId(productId);

                images.add(image);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return images;
    }

    @Override
    public int insertProduct(Product product) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int productId = 0;

        try {
            conn = DbUtil.getConnection();
            // properties 파일에서 쿼리를 가져오되, 없으면 직접 SQL 쿼리 사용
            String sql = proFile.getProperty("product.insert") != null ? proFile.getProperty("product.insert")
                    : "INSERT INTO products (name, sub_name, price, stock, category, discount_rate) VALUES (?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getSubName());
            pstmt.setInt(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.setString(5, product.getCategory());
            pstmt.setInt(6, product.getDiscountRate());

            int result = pstmt.executeUpdate();

            if (result > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    productId = rs.getInt(1);
                }
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return productId;
    }

    @Override
    public int insertProductDetail(ProductDetail productDetail) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            // properties 파일에서 쿼리를 가져오되, 없으면 직접 SQL 쿼리 사용
            String sql = proFile.getProperty("product.insertDetail") != null
                    ? proFile.getProperty("product.insertDetail")
                    : "INSERT INTO product_details (product_id, description, ingredients, kcal, amount, nutrition, created_date, updated_date) "
                            +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, productDetail.getProductId());
            pstmt.setString(2, productDetail.getDescription());
            pstmt.setString(3, productDetail.getIngredients());
            pstmt.setInt(4, productDetail.getKcal());
            pstmt.setInt(5, productDetail.getAmount());
            pstmt.setString(6, productDetail.getNutrition());

            // 날짜 필드가 있는 경우 설정, 없으면 현재 시간으로
            if (productDetail.getCreatedDate() != null) {
                pstmt.setDate(7, new java.sql.Date(productDetail.getCreatedDate().getTime()));
            } else {
                pstmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));
            }

            if (productDetail.getUpdatedDate() != null) {
                pstmt.setDate(8, new java.sql.Date(productDetail.getUpdatedDate().getTime()));
            } else {
                pstmt.setDate(8, new java.sql.Date(System.currentTimeMillis()));
            }

            result = pstmt.executeUpdate();
        } finally {
            DbUtil.dbClose(conn, pstmt);
        }

        return result;
    }

    @Override
    public int insertProductImage(ProductImage productImage) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            // properties 파일에서 쿼리를 가져오되, 없으면 직접 SQL 쿼리 사용
            String sql = proFile.getProperty("product.insertImage") != null ? proFile.getProperty("product.insertImage")
                    : "INSERT INTO product_images (image_name, is_main, product_id) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, productImage.getImageName());
            pstmt.setBoolean(2, productImage.isMain());
            pstmt.setInt(3, productImage.getProductId());

            result = pstmt.executeUpdate();
        } finally {
            DbUtil.dbClose(conn, pstmt);
        }

        return result;
    }

    @Override
    public int updateProduct(Product product) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            String sql = proFile.getProperty("product.update") != null ? proFile.getProperty("product.update")
                    : "UPDATE products SET name=?, sub_name=?, price=?, stock=?, category=?, discount_rate=? WHERE product_id=?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getSubName());
            pstmt.setInt(3, product.getPrice());
            pstmt.setInt(4, product.getStock());
            pstmt.setString(5, product.getCategory());
            pstmt.setInt(6, product.getDiscountRate());
            pstmt.setInt(7, product.getProductId());

            result = pstmt.executeUpdate();

        } finally {
            DbUtil.dbClose(conn, pstmt);
        }

        return result;
    }

    @Override
    public int updateProductDetail(ProductDetail productDetail) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            String sql = proFile.getProperty("product.updateDetail") != null
                    ? proFile.getProperty("product.updateDetail")
                    : "UPDATE product_details SET description=?, ingredients=?, kcal=?, amount=?, nutrition=?, updated_date=? WHERE product_id=?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, productDetail.getDescription());
            pstmt.setString(2, productDetail.getIngredients());
            pstmt.setInt(3, productDetail.getKcal());
            pstmt.setInt(4, productDetail.getAmount());
            pstmt.setString(5, productDetail.getNutrition());
            // java.util.Date를 java.sql.Timestamp로 변환
            pstmt.setTimestamp(6, new java.sql.Timestamp(productDetail.getUpdatedDate().getTime()));
            pstmt.setInt(7, productDetail.getProductId());

            result = pstmt.executeUpdate();

        } finally {
            DbUtil.dbClose(conn, pstmt);
        }

        return result;
    }

    @Override
    public int deleteProduct(int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            String sql = proFile.getProperty("product.delete") != null ? proFile.getProperty("product.delete")
                    : "DELETE FROM products WHERE product_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            result = pstmt.executeUpdate();

            // 삭제 성공 시 더 높은 ID를 가진 제품들의 ID를 하나씩 감소시킵니다
            if (result > 0) {
                DbUtil.dbClose(null, pstmt, null);
                String updateSql = "UPDATE products SET product_id = product_id - 1 WHERE product_id > ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setInt(1, productId);

                pstmt.executeUpdate();

                // AUTO_INCREMENT 값도 조정합니다
                DbUtil.dbClose(null, pstmt, null);

                // 현재 최대 ID 찾기
                String maxIdSql = "SELECT MAX(product_id) as max_id FROM products";
                pstmt = conn.prepareStatement(maxIdSql);
                ResultSet rs = pstmt.executeQuery();

                int maxId = 0;
                if (rs.next()) {
                    maxId = rs.getInt("max_id");
                }

                rs.close();
                DbUtil.dbClose(null, pstmt, null);

                // AUTO_INCREMENT 설정
                String resetSql = "ALTER TABLE products AUTO_INCREMENT = ?";
                pstmt = conn.prepareStatement(resetSql);
                pstmt.setInt(1, maxId + 1);

                pstmt.executeUpdate();
            }

        } finally {
            DbUtil.dbClose(conn, pstmt);
        }

        return result;
    }

    @Override
    public int deleteProductImages(int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();
            String sql = proFile.getProperty("product.deleteImages") != null
                    ? proFile.getProperty("product.deleteImages")
                    : "DELETE FROM product_images WHERE product_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            result = pstmt.executeUpdate();

        } finally {
            DbUtil.dbClose(conn, pstmt);
        }
        return result;
    }

    @Override
    public int getTotalProductCount() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("product.getCount"));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return count;
    }

    @Override
    public int getCategoryProductCount(String category) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DbUtil.getConnection();
            pstmt = conn.prepareStatement(proFile.getProperty("product.getCategoryCount"));
            pstmt.setString(1, category);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return count;
    }

    @Override
    public int resetProductAutoIncrement() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int result = 0;

        try {
            conn = DbUtil.getConnection();

            // 현재 존재하는 모든 상품 ID를 가져옵니다
            List<Integer> existingIds = new ArrayList<>();
            String getAllIdsSql = "SELECT product_id FROM products ORDER BY product_id";
            pstmt = conn.prepareStatement(getAllIdsSql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                existingIds.add(rs.getInt("product_id"));
            }

            // 존재하는 ID가 없으면 AUTO_INCREMENT를 1로 설정하고 반환
            if (existingIds.isEmpty()) {
                DbUtil.dbClose(null, pstmt, rs);
                String resetSql = "ALTER TABLE products AUTO_INCREMENT = 1";
                pstmt = conn.prepareStatement(resetSql);
                return pstmt.executeUpdate();
            }

            // 가장 간단한 방법으로 AUTO_INCREMENT 값을 다음 사용 가능한 ID로 설정
            int maxId = existingIds.get(existingIds.size() - 1);

            DbUtil.dbClose(null, pstmt, rs);

            // AUTO_INCREMENT 값을 현재 최대 ID + 1로 재설정
            String resetAutoIncrementSql = "ALTER TABLE products AUTO_INCREMENT = ?";
            pstmt = conn.prepareStatement(resetAutoIncrementSql);
            pstmt.setInt(1, maxId + 1);

            result = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return result;
    }

    @Override
    public List<Product> selectBestProducts(int pageNo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Product> list = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            // 베스트 상품 조회 쿼리: '베스트' 카테고리 제품 우선, 그 다음 평점(rating)과 리뷰 수(review_count)로 정렬
            String sql = "SELECT p.*, pi.image_name, " +
                    "(SELECT COALESCE(AVG(rating), 0) FROM product_reviews WHERE product_id = p.product_id) as rating, "
                    +
                    "(SELECT COUNT(*) FROM product_reviews WHERE product_id = p.product_id) as review_count " +
                    "FROM products p " +
                    "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = 1 " +
                    "ORDER BY CASE WHEN p.category = '베스트' THEN 0 ELSE 1 END, rating DESC, review_count DESC, p.product_id DESC "
                    +
                    "LIMIT ?, ?";

            pstmt = conn.prepareStatement(sql);

            int startIndex = (pageNo - 1) * PAGE_SIZE;
            pstmt.setInt(1, startIndex);
            pstmt.setInt(2, PAGE_SIZE);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = createProductFromResultSet(rs);
                list.add(product);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return list;
    }

    @Override
    public List<Product> selectRegularProducts(int pageNo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Product> list = new ArrayList<>();

        try {
            conn = DbUtil.getConnection(); // 정기배송 상품 조회 쿼리
            String sql = "SELECT p.*, pi.image_name, " +
                    "(SELECT COALESCE(AVG(rating), 0) FROM product_reviews WHERE product_id = p.product_id) as rating, "
                    +
                    "(SELECT COUNT(*) FROM product_reviews WHERE product_id = p.product_id) as review_count " +
                    "FROM products p " +
                    "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = 1 " +
                    "WHERE p.category = '정기배송' " +
                    "ORDER BY p.product_id DESC " +
                    "LIMIT ?, ?";

            pstmt = conn.prepareStatement(sql);

            int startIndex = (pageNo - 1) * PAGE_SIZE;
            pstmt.setInt(1, startIndex);
            pstmt.setInt(2, PAGE_SIZE);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = createProductFromResultSet(rs);
                list.add(product);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return list;
    }

    @Override
    public List<Product> selectLunchboxProducts(int pageNo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Product> list = new ArrayList<>();

        try {
            conn = DbUtil.getConnection(); // 도시락 상품 조회 쿼리
            String sql = "SELECT p.*, pi.image_name, " +
                    "(SELECT COALESCE(AVG(rating), 0) FROM product_reviews WHERE product_id = p.product_id) as rating, "
                    +
                    "(SELECT COUNT(*) FROM product_reviews WHERE product_id = p.product_id) as review_count " +
                    "FROM products p " +
                    "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = 1 " +
                    "WHERE p.category = '도시락' " +
                    "ORDER BY p.product_id DESC " +
                    "LIMIT ?, ?";

            pstmt = conn.prepareStatement(sql);

            int startIndex = (pageNo - 1) * PAGE_SIZE;
            pstmt.setInt(1, startIndex);
            pstmt.setInt(2, PAGE_SIZE);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = createProductFromResultSet(rs);
                list.add(product);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return list;
    }

    @Override
    public List<Product> selectSaladProducts(int pageNo) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Product> list = new ArrayList<>();

        try {
            conn = DbUtil.getConnection(); // 샐러드 상품 조회 쿼리
            String sql = "SELECT p.*, pi.image_name, " +
                    "(SELECT COALESCE(AVG(rating), 0) FROM product_reviews WHERE product_id = p.product_id) as rating, "
                    +
                    "(SELECT COUNT(*) FROM product_reviews WHERE product_id = p.product_id) as review_count " +
                    "FROM products p " +
                    "LEFT JOIN product_images pi ON p.product_id = pi.product_id AND pi.is_main = 1 " +
                    "WHERE p.category = '샐러드' " +
                    "ORDER BY p.product_id DESC " +
                    "LIMIT ?, ?";

            pstmt = conn.prepareStatement(sql);

            int startIndex = (pageNo - 1) * PAGE_SIZE;
            pstmt.setInt(1, startIndex);
            pstmt.setInt(2, PAGE_SIZE);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = createProductFromResultSet(rs);
                list.add(product);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return list;
    }

    @Override
    public int getBestProductCount() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM products";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return count;
    }

    @Override
    public int getRegularProductCount() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM products WHERE category = 'regular'";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return count;
    }

    @Override
    public int getLunchboxProductCount() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM products WHERE category = 'lunchbox'";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return count;
    }

    @Override
    public int getSaladProductCount() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM products WHERE category = 'salad'";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } finally {
            DbUtil.dbClose(conn, pstmt, rs);
        }

        return count;
    }

    // Helper method to create a Product object from ResultSet
    private Product createProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setSubName(rs.getString("sub_name"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setCategory(rs.getString("category"));
        product.setDiscountRate(rs.getInt("discount_rate"));
        product.setMainImageName(rs.getString("image_name"));
        product.setRating(rs.getDouble("rating"));
        product.setReviewCount(rs.getInt("review_count"));
        product.setPopularity(90 + (int) (Math.random() * 10)); // 임시 인기도 값
        product.setDescription(rs.getString("sub_name"));
        product.setThumbnailUrl("products/images/" + rs.getString("image_name"));

        return product;
    }

    // ...existing code...
}