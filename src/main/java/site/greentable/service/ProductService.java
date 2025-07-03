package site.greentable.service;

import java.sql.SQLException;
import java.util.List;

import site.greentable.dto.Product;
import site.greentable.dto.ProductDetail;
import site.greentable.dto.ProductImage;

public interface ProductService {
    /**
     * 모든 상품 목록 조회 (페이징)
     */
    List<Product> getAllProducts(int pageNo) throws SQLException;

    /**
     * 카테고리별 상품 목록 조회 (페이징)
     */
    List<Product> getProductsByCategory(String category, int pageNo) throws SQLException;

    /**
     * 베스트 상품 목록 조회 (페이징)
     */
    List<Product> getBestProducts(int pageNo) throws SQLException;

    /**
     * 정기배송 상품 목록 조회 (페이징)
     */
    List<Product> getRegularProducts(int pageNo) throws SQLException;

    /**
     * 도시락 상품 목록 조회 (페이징)
     */
    List<Product> getLunchboxProducts(int pageNo) throws SQLException;

    /**
     * 샐러드 상품 목록 조회 (페이징)
     */
    List<Product> getSaladProducts(int pageNo) throws SQLException;

    /**
     * 상품 상세 정보 조회
     */
    Product getProductDetail(int productId) throws SQLException;

    /**
     * 상품 상세 정보 조회 (ProductDetail만)
     */
    ProductDetail getProductDetailInfo(int productId) throws SQLException;

    /**
     * 상품 이미지 조회
     */
    List<ProductImage> getProductImages(int productId) throws SQLException;

    /**
     * 새 상품 등록 (상품 정보, 상세 정보, 이미지 포함)
     */
    int registerProduct(Product product, ProductDetail detail, List<ProductImage> images) throws SQLException;

    /**
     * 상품 정보 수정
     */
    int updateProduct(Product product, ProductDetail detail) throws SQLException;

    /**
     * 상품 정보 수정 (이미지 포함)
     */
    int updateProduct(Product product, ProductDetail detail, List<ProductImage> images, boolean updateImages)
            throws SQLException;

    /**
     * 상품 삭제
     */
    int deleteProduct(int productId) throws SQLException;

    /**
     * 총 상품 페이지 수 계산
     */
    int getTotalPages() throws SQLException;

    /**
     * 카테고리별 총 상품 페이지 수 계산
     */
    int getCategoryTotalPages(String category) throws SQLException;

    /**
     * 베스트 상품 총 페이지 수 계산
     */
    int getBestTotalPages() throws SQLException;

    /**
     * 정기배송 상품 총 페이지 수 계산
     */
    int getRegularTotalPages() throws SQLException;

    /**
     * 도시락 상품 총 페이지 수 계산
     */
    int getLunchboxTotalPages() throws SQLException;

    /**
     * 샐러드 상품 총 페이지 수 계산
     */
    int getSaladTotalPages() throws SQLException;

    /**
     * 상품 테이블의 AUTO_INCREMENT 값 리셋
     */
    int resetProductIdSequence() throws SQLException;

    /**
     * 전체 상품 개수 조회
     */
    int getTotalProductCount() throws SQLException;

    /**
     * 현재 상품과 동일한 카테고리의 다른 상품 중 몇 개를 추천 상품으로 가져옵니다.
     * 
     * @param productId 현재 상품의 ID
     * @param limit     가져올 추천 상품 수
     * @return 추천 상품 목록
     */
    List<Product> getRecommendedProducts(int productId, int limit) throws SQLException;

    /**
     * 검색어로 상품 검색 (페이징)
     * 
     * @param query  검색어
     * @param pageNo 페이지 번호
     * @return 검색 결과 상품 목록
     */
    List<Product> searchProducts(String query, int pageNo) throws SQLException;

    /**
     * 검색 결과의 총 페이지 수 계산
     * 
     * @param query 검색어
     * @return 총 페이지 수
     */
    int getSearchTotalPages(String query) throws SQLException;

    /**
     * 상품 목록을 최신순으로 정렬
     * 
     * @param products 정렬할 상품 목록
     * @return 정렬된 상품 목록
     */
    List<Product> sortProductsByNewest(List<Product> products);

    /**
     * 상품 목록을 가격 오름차순으로 정렬
     * 
     * @param products 정렬할 상품 목록
     * @return 정렬된 상품 목록
     */
    List<Product> sortProductsByPriceAsc(List<Product> products);

    /**
     * 상품 목록을 가격 내림차순으로 정렬
     * 
     * @param products 정렬할 상품 목록
     * @return 정렬된 상품 목록
     */
    List<Product> sortProductsByPriceDesc(List<Product> products);

    /**
     * 상품 목록을 할인율 높은순으로 정렬
     * 
     * @param products 정렬할 상품 목록
     * @return 정렬된 상품 목록
     */
    List<Product> sortProductsByDiscount(List<Product> products);
}