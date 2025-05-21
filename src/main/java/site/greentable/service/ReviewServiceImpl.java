package site.greentable.service;

import site.greentable.dao.ReviewDAO;
import site.greentable.dao.ReviewDAOImpl;
import site.greentable.dto.ReviewDTO;
import site.greentable.dto.ReviewImageDTO;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    private ReviewDAO reviewDAO;

    public ReviewServiceImpl() {
        this.reviewDAO = new ReviewDAOImpl();
    }

    @Override
    public void writeReview(ReviewDTO review) throws Exception {
        // 리뷰 작성 가능 여부 확인
        if (!isReviewable(review.getUserId(), review.getProductId(), review.getOrderDetailId())) {
            throw new Exception("리뷰를 작성할 수 없는 상품입니다.");
        }

        // 리뷰 등록
        int reviewId = reviewDAO.insertReview(review);

        // 리뷰 이미지 등록
        if (review.getImages() != null && !review.getImages().isEmpty()) {
            for (ReviewImageDTO image : review.getImages()) {
                image.setReviewId(reviewId);
                reviewDAO.insertReviewImage(image);
            }
        }
    }

    @Override
    public List<ReviewDTO> getProductReviews(int productId, int page) throws Exception {
        int limit = 10; // 페이지당 리뷰 수
        int offset = (page - 1) * limit;

        return reviewDAO.getProductReviews(productId, offset, limit);
    }

    @Override
    public List<ReviewDTO> getUserReviews(int userId) throws Exception {
        return reviewDAO.getUserReviews(userId);
    }

    @Override
    public ReviewDTO getReview(int reviewId) throws Exception {
        ReviewDTO review = reviewDAO.getReview(reviewId);
        if (review != null) {
            review.setImages(reviewDAO.getReviewImages(reviewId));
        }
        return review;
    }

    @Override
    public void updateReview(ReviewDTO review) throws Exception {
        reviewDAO.updateReview(review);

        // 기존 이미지 삭제 및 새 이미지 등록
        if (review.isImageChanged()) {
            reviewDAO.deleteReviewImages(review.getReviewId());

            if (review.getImages() != null && !review.getImages().isEmpty()) {
                for (ReviewImageDTO image : review.getImages()) {
                    reviewDAO.insertReviewImage(image);
                }
            }
        }
    }

    @Override
    public void deleteReview(int reviewId) throws Exception {
        // 리뷰 이미지 삭제
        reviewDAO.deleteReviewImages(reviewId);

        // 리뷰 삭제
        reviewDAO.deleteReview(reviewId);
    }

    @Override
    public boolean isReviewable(int userId, int productId, int orderDetailId) throws Exception {
        
    	return true;
    	// 해당 상품을 구매했는지, 이미 리뷰를 작성했는지 확인하는 로직
        // return reviewDAO.isReviewable(userId, productId, orderDetailId);
    }

    @Override
    public List<ReviewDTO> getWritableReviews(int userId) throws Exception {
        return reviewDAO.getWritableReviews(userId);
    }

    @Override
    public int getLatestOrderDetailId(int userId, int productId) throws Exception {
        try {
            // 해당 사용자의 가장 최근 주문 상세 ID 조회
            return reviewDAO.getLatestOrderDetailId(userId, productId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // 오류 발생 시 0 반환
        }
    }
}