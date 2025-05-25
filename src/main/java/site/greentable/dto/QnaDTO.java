package site.greentable.dto;

import java.sql.Timestamp;

public class QnaDTO {
    private int qnaId;
    private int productId;
    private int userId;
    private String title;
    private String content;
    private String answer;
    private String isAnswered;
    private Timestamp createdAt;
    private Timestamp answeredAt;

    // 유저 이름 표시를 위한 필드
    private String userName;
    // 상품 정보 표시를 위한 필드
    private String productName;
    private String productImage;

    // 기본 생성자
    public QnaDTO() {
    }

    // getter, setter 메서드
    public int getQnaId() {
        return qnaId;
    }

    public void setQnaId(int qnaId) {
        this.qnaId = qnaId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(String isAnswered) {
        this.isAnswered = isAnswered;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Timestamp answeredAt) {
        this.answeredAt = answeredAt;
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

    @Override
    public String toString() {
        return "QnaDTO [qnaId=" + qnaId + ", productId=" + productId + ", userId=" + userId + ", title=" + title
                + ", isAnswered=" + isAnswered + ", createdAt=" + createdAt + ", userName=" + userName
                + ", productName="
                + productName + "]";
    }
}