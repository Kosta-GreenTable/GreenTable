package site.greentable.dto;

import java.sql.Timestamp;
import java.util.List;

public class ReviewDTO {
    private int reviewId;
    private int productId;
    private int userId;
    private int orderDetailId;
    private int rating;
    private String content;
    private Timestamp createdAt;
    private List<ReviewImageDTO> images;
    private boolean imageChanged;

    // 유저 이름 표시를 위한 필드
    private String userName;
    // 상품 정보 표시를 위한 필드
    private String productName;
    private String productImage;

    // 기본 생성자
    public ReviewDTO() {
    }

    // 게터/세터 메서드
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<ReviewImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ReviewImageDTO> images) {
        this.images = images;
    }

    public boolean isImageChanged() {
        return imageChanged;
    }

    public void setImageChanged(boolean imageChanged) {
        this.imageChanged = imageChanged;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getReviewImagesCount() {
        return (images == null) ? 0 : images.size();
    }

    @Override
    public String toString() {
        return "ReviewDTO [reviewId=" + reviewId + ", productId=" + productId + ", userId=" + userId
                + ", orderDetailId="
                + orderDetailId + ", rating=" + rating + ", content=" + content + ", createdAt=" + createdAt
                + ", userName=" + userName + ", productName=" + productName + "]";
    }
}