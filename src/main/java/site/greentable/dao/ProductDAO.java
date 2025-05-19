package site.greentable.dao;

import java.sql.SQLException;
import java.util.List;
import site.greentable.dto.Product;
import site.greentable.dto.ProductDetail;
import site.greentable.dto.ProductImage;

public interface ProductDAO {
    /**
     * 모든 상품 목록 조회 (페이징)
     */
    List<Product> selectAll(int pageNo) throws SQLException;

    /**
     * 카테고리별 상품 목록 조회 (페이징)
     */
    List<Product> selectByCategory(String category, int pageNo) throws SQLException;

    /**
     * 베스트 상품 목록 조회 (페이징)
     */
    List<Product> selectBestProducts(int pageNo) throws SQLException;

    /**
     * 정기배송 상품 목록 조회 (페이징)
     */
    List<Product> selectRegularProducts(int pageNo) throws SQLException;

    /**
     * 도시락 상품 목록 조회 (페이징)
     */
    List<Product> selectLunchboxProducts(int pageNo) throws SQLException;

    /**
     * 샐러드 상품 목록 조회 (페이징)
     */
    List<Product> selectSaladProducts(int pageNo) throws SQLException;

    /**
     * 상품 상세 정보 조회
     */
    Product selectProductDetail(int productId) throws SQLException;

    /**
     * 상품 이미지 조회
     */
    List<ProductImage> selectProductImages(int productId) throws SQLException;

    /**
     * AUTO_INCREMENT 값 리셋
     */
    int resetProductAutoIncrement() throws SQLException;

    /**
     * 상품 등록
     */
    int insertProduct(Product product) throws SQLException;

    /**
     * 상품 상세 정보 등록
     */
    int insertProductDetail(ProductDetail productDetail) throws SQLException;

    /**
     * 상품 이미지 등록
     */
    int insertProductImage(ProductImage productImage) throws SQLException;

    /**
     * 상품 정보 수정
     */
    int updateProduct(Product product) throws SQLException;

    /**
     * 상품 상세 정보 수정
     */
    int updateProductDetail(ProductDetail productDetail) throws SQLException;

    /**
     * 상품 삭제
     */
    int deleteProduct(int productId) throws SQLException;

    /**
     * 상품 이미지 삭제
     */
    int deleteProductImages(int productId) throws SQLException;

    /**
     * 상품 총 개수 조회
     */
    int getTotalProductCount() throws SQLException;

    /**
     * 카테고리별 상품 개수 조회
     */
    int getCategoryProductCount(String category) throws SQLException;

    /**
     * 베스트 상품 개수 조회
     */
    int getBestProductCount() throws SQLException;

    /**
     * 정기배송 상품 개수 조회
     */
    int getRegularProductCount() throws SQLException;

    /**
     * 도시락 상품 개수 조회
     */
    int getLunchboxProductCount() throws SQLException;

    /**
     * 샐러드 상품 개수 조회
     */
    int getSaladProductCount() throws SQLException;

    /**
     * 추천 상품 목록 조회
     * 
     * @param productId 현재 조회 중인 상품 ID
     * @param limit     조회할 추천 상품 개수
     * @return 추천 상품 목록
     */
    List<Product> selectRecommendedProducts(int productId, int limit) throws SQLException;
}