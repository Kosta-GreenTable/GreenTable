package site.greentable.dto;

import java.sql.Timestamp;

public class PointHistoryDTO {
    private int pointHistoryId;
    private int userId;
    private String pointType; // EARN, USE
    private int pointAmount;
    private String description;
    private Timestamp createdAt;
    private int balanceAfter; // 포인트 적용 후 잔액

    // 관련 주문 정보 (포인트 적립/사용 시)
    private String orderNo;

    public PointHistoryDTO() {
    }

    public PointHistoryDTO(int userId, String pointType, int pointAmount, String description, int balanceAfter) {
        this.userId = userId;
        this.pointType = pointType;
        this.pointAmount = pointAmount;
        this.description = description;
        this.balanceAfter = balanceAfter;
    }

    public PointHistoryDTO(int userId, String pointType, int pointAmount, String description,
            int balanceAfter, String orderNo) {
        this.userId = userId;
        this.pointType = pointType;
        this.pointAmount = pointAmount;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.orderNo = orderNo;
    }

    // Getters and Setters
    public int getPointHistoryId() {
        return pointHistoryId;
    }

    public void setPointHistoryId(int pointHistoryId) {
        this.pointHistoryId = pointHistoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public int getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(int pointAmount) {
        this.pointAmount = pointAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "PointHistoryDTO [pointHistoryId=" + pointHistoryId + ", userId=" + userId +
                ", pointType=" + pointType + ", pointAmount=" + pointAmount +
                ", description=" + description + ", createdAt=" + createdAt +
                ", balanceAfter=" + balanceAfter + ", orderNo=" + orderNo + "]";
    }
}
