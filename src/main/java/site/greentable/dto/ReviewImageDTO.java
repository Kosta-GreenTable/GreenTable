package site.greentable.dto;

public class ReviewImageDTO {
    private int reviewImageId;
    private int reviewId;
    private String realName;
    private String originalName;
    private boolean isMain;

    // 기본 생성자
    public ReviewImageDTO() {
    }

    // 매개변수가 있는 생성자
    public ReviewImageDTO(String realName, String originalName, boolean isMain) {
        this.realName = realName;
        this.originalName = originalName;
        this.isMain = isMain;
    }

    // getter, setter 메서드
    public int getReviewImageId() {
        return reviewImageId;
    }

    public void setReviewImageId(int reviewImageId) {
        this.reviewImageId = reviewImageId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean isMain) {
        this.isMain = isMain;
    }

    @Override
    public String toString() {
        return "ReviewImageDTO [reviewImageId=" + reviewImageId + ", reviewId=" + reviewId + ", realName=" + realName
                + ", originalName=" + originalName + ", isMain=" + isMain + "]";
    }
}