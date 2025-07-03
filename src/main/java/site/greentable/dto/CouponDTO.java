package site.greentable.dto;

import java.sql.Timestamp;

public class CouponDTO {
    private int couponId;
    private int userId;
    private String couponName;
    private String couponType; // 할인금액, 할인률
    private int discountValue; // 할인 금액 또는 할인율
    private int minOrderAmount; // 최소 주문 금액
    private String category; // 적용 카테고리
    private String status; // AVAILABLE, USED, EXPIRED
    private Timestamp validFrom;
    private Timestamp validTo;
    private Timestamp usedAt;
    private Timestamp createdAt;

    // 사용된 주문 정보
    private String orderNo;

    public CouponDTO() {
    }

    public CouponDTO(int userId, String couponName, String couponType, int discountValue,
            int minOrderAmount, String category, Timestamp validFrom, Timestamp validTo) {
        this.userId = userId;
        this.couponName = couponName;
        this.couponType = couponType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.category = category;
        this.status = "AVAILABLE";
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    // Getters and Setters
    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public int getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(int minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    public Timestamp getValidTo() {
        return validTo;
    }

    public void setValidTo(Timestamp validTo) {
        this.validTo = validTo;
    }

    public Timestamp getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Timestamp usedAt) {
        this.usedAt = usedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    // Helper methods
    public boolean isExpired() {
        return validTo != null && validTo.before(new Timestamp(System.currentTimeMillis()));
    }

    public boolean isAvailable() {
        return "AVAILABLE".equals(status) && !isExpired();
    }

    @Override
    public String toString() {
        return "CouponDTO [couponId=" + couponId + ", userId=" + userId +
                ", couponName=" + couponName + ", couponType=" + couponType +
                ", discountValue=" + discountValue + ", minOrderAmount=" + minOrderAmount +
                ", category=" + category + ", status=" + status +
                ", validFrom=" + validFrom + ", validTo=" + validTo + "]";
    }
}
