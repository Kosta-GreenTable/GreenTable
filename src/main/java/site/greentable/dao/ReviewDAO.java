package site.greentable.dao;

import site.greentable.dto.ReviewDTO;
import site.greentable.dto.ReviewImageDTO;

import java.util.List;

public interface ReviewDAO {
    int insertReview(ReviewDTO review) throws Exception;

    void insertReviewImage(ReviewImageDTO image) throws Exception;

    List<ReviewDTO> getProductReviews(int productId, int offset, int limit) throws Exception;

    List<ReviewDTO> getUserReviews(int userId) throws Exception;

    ReviewDTO getReview(int reviewId) throws Exception;

    List<ReviewImageDTO> getReviewImages(int reviewId) throws Exception;

    void updateReview(ReviewDTO review) throws Exception;

    void deleteReview(int reviewId) throws Exception;

    void deleteReviewImages(int reviewId) throws Exception;

    boolean isReviewable(int userId, int productId, int orderDetailId) throws Exception;

    int getReviewCount(int productId) throws Exception;

    /**
     * 사용자가 작성할 수 있는 리뷰 목록을 가져옵니다.
     * 
     * @param userId 사용자 ID
     * @return 작성 가능한 리뷰 목록
     * @throws Exception 데이터 조회 중 오류 발생시
     */
    List<ReviewDTO> getWritableReviews(int userId) throws Exception;

    int getLatestOrderDetailId(int userId, int productId) throws Exception;
}