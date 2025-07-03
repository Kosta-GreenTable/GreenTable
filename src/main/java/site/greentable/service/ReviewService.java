package site.greentable.service;

import site.greentable.dto.ReviewDTO;
import site.greentable.dto.ReviewImageDTO;

import java.util.List;

public interface ReviewService {
    void writeReview(ReviewDTO review) throws Exception;

    List<ReviewDTO> getProductReviews(int productId, int page) throws Exception;

    List<ReviewDTO> getUserReviews(int userId) throws Exception;

    ReviewDTO getReview(int reviewId) throws Exception;

    void updateReview(ReviewDTO review) throws Exception;

    void deleteReview(int reviewId) throws Exception;

    boolean isReviewable(int userId, int productId, int orderDetailId) throws Exception;

    /**
     * 사용자가 작성할 수 있는 리뷰 목록을 가져옵니다.
     * 구매 완료된 상품 중 아직 리뷰를 작성하지 않은 상품 목록을 반환합니다.
     * 
     * @param userId 사용자 ID
     * @return 작성 가능한 리뷰 목록
     * @throws Exception 데이터 조회 중 오류 발생시
     */
    List<ReviewDTO> getWritableReviews(int userId) throws Exception;

    /**
     * 사용자가 해당 상품에 대해 가장 최근에 구매한 주문 상세 ID를 반환
     * 
     * @param userId    사용자 ID
     * @param productId 상품 ID
     * @return 주문 상세 ID (없으면 0 반환)
     */
    int getLatestOrderDetailId(int userId, int productId) throws Exception;

    /**
     * 리뷰에 연결된 이미지 목록을 가져옵니다.
     * 
     * @param reviewId 리뷰 ID
     * @return 리뷰 이미지 목록
     * @throws Exception 데이터 조회 중 오류 발생시
     */
    List<ReviewImageDTO> getReviewImages(int reviewId) throws Exception;
}