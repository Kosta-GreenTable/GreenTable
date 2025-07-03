package site.greentable.util;

/**
 * 이미지 URL 생성 유틸리티 (S3 전용)
 */
public class ImageUtil {
    private static final String S3_BASE_URL = System.getenv("S3_BASE_URL") != null ?
                                                System.getenv("S3_BASE_URL") :
                                                "https://greentable-images-your-region.s3.ap-northeast-2.amazonaws.com";
    
    /**
     * S3 상품 이미지 URL 생성
     */
    public static String getProductImageUrl(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            return null; // 기본 이미지 대신 null 반환
        }
        // 경로 구분자 처리
        String baseUrl = S3_BASE_URL.endsWith("/") ? S3_BASE_URL : S3_BASE_URL + "/";
        return baseUrl + imageName;
    }
    
    /**
     * S3 리뷰 이미지 URL 생성
     */
    public static String getReviewImageUrl(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            return null; // 기본 이미지 대신 null 반환
        }
        // 경로 구분자 처리
        String baseUrl = S3_BASE_URL.endsWith("/") ? S3_BASE_URL : S3_BASE_URL + "/";
        return baseUrl + imageName;
    }
    
    /**
     * 하위 호환성을 위한 메서드 (S3 URL만 반환)
     */
    public static String getImageUrl(String imageName) {
        return getProductImageUrl(imageName);
    }
    
    /**
     * 하위 호환성을 위한 메서드 (S3 URL만 반환)
     */
    public static String processImageUrl(String imageName) {
        return getProductImageUrl(imageName);
    }
    
    /**
     * 하위 호환성을 위한 메서드 (S3 URL만 반환)
     */
    public static String getImagePath(String imageName) {
        return getProductImageUrl(imageName);
    }
    
    /**
     * 상품 이미지 처리 (S3 전용)
     */
    public static String processProductImage(String imageName) {
        return getProductImageUrl(imageName);
    }
    
    /**
     * 리뷰 이미지 처리 (S3 전용)
     */
    public static String processReviewImage(String imageName) {
        return getReviewImageUrl(imageName);
    }
}