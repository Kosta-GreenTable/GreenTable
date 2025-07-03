package site.greentable.util;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * AWS S3 파일 업로드/삭제 유틸리티 클래스
 */
public class S3Util {
    private static final String BUCKET_NAME = System.getProperty("S3_BUCKET_NAME", 
                                                System.getenv("S3_BUCKET_NAME") != null ? 
                                                System.getenv("S3_BUCKET_NAME") : "greentable-images-your-region");
    private static final String REGION = System.getProperty("AWS_REGION", 
                                         System.getenv("AWS_REGION") != null ? 
                                         System.getenv("AWS_REGION") : "ap-northeast-2");
    private static final String S3_BASE_URL = System.getenv("S3_BASE_URL") != null ? 
                                               System.getenv("S3_BASE_URL") : 
                                               "https://greentable-images-your-region.s3.ap-northeast-2.amazonaws.com";
    
    private static AmazonS3 s3Client;
    
    static {
        try {
            // S3 클라이언트 초기화
            s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(REGION != null ? REGION : "ap-northeast-2")
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
            
            System.out.println("AWS S3 클라이언트 초기화 완료");
            System.out.println("버킷명: " + BUCKET_NAME);
            System.out.println("리전: " + (REGION != null ? REGION : "ap-northeast-2"));
        } catch (Exception e) {
            System.err.println("AWS S3 클라이언트 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Part 객체를 S3에 업로드
     * 
     * @param part 업로드할 파일 Part
     * @param folder S3 내 폴더 경로 (예: "products/")
     * @return S3 객체 키 (업로드된 파일의 전체 경로)
     * @throws IOException 업로드 실패 시
     */
    public static String uploadFile(Part part, String folder) throws IOException {
        if (s3Client == null) {
            throw new IOException("S3 클라이언트가 초기화되지 않았습니다.");
        }
        
        if (BUCKET_NAME == null || BUCKET_NAME.trim().isEmpty()) {
            throw new IOException("S3 버킷명이 설정되지 않았습니다.");
        }
        
        try {
            // 원본 파일명에서 확장자 추출
            String originalFileName = getOriginalFileName(part);
            String fileExtension = getFileExtension(originalFileName);
            
            // 고유한 파일명 생성 (UUID + 확장자)
            String fileName = UUID.randomUUID().toString() + fileExtension;
            String s3Key = folder + fileName;
            
            // 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(part.getSize());
            metadata.setContentType(part.getContentType());
            
            // S3에 업로드
            try (InputStream inputStream = part.getInputStream()) {
                PutObjectRequest putRequest = new PutObjectRequest(BUCKET_NAME, s3Key, inputStream, metadata);
                s3Client.putObject(putRequest);
            }
            
            System.out.println("S3 업로드 성공: " + s3Key);
            return s3Key;
            
        } catch (Exception e) {
            System.err.println("S3 업로드 실패: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("파일 업로드에 실패했습니다: " + e.getMessage());
        }
    }
    
    /**
     * S3에서 파일 삭제
     * 
     * @param s3Key 삭제할 S3 객체 키
     * @return 삭제 성공 여부
     */
    public static boolean deleteFile(String s3Key) {
        if (s3Client == null || BUCKET_NAME == null || s3Key == null) {
            return false;
        }
        
        try {
            DeleteObjectRequest deleteRequest = new DeleteObjectRequest(BUCKET_NAME, s3Key);
            s3Client.deleteObject(deleteRequest);
            
            System.out.println("S3 파일 삭제 성공: " + s3Key);
            return true;
            
        } catch (Exception e) {
            System.err.println("S3 파일 삭제 실패: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * S3 객체의 공개 URL 생성
     * 
     * @param s3Key S3 객체 키
     * @return 공개 액세스 가능한 URL
     */
    public static String getPublicUrl(String s3Key) {
        if (BUCKET_NAME == null || s3Key == null) {
            return null;
        }
        
        String region = REGION != null ? REGION : "ap-northeast-2";
        return String.format("https://%s.s3.%s.amazonaws.com/%s", BUCKET_NAME, region, s3Key);
    }
    
    /**
     * S3 이미지 전체 URL 생성 (환경변수 사용)
     */
    public static String getImageUrl(String s3Key) {
        if (s3Key == null || s3Key.trim().isEmpty()) {
            return S3_BASE_URL + "/products/no-image.jpg";
        }
        
        // S3_BASE_URL 환경변수가 있으면 사용
        if (S3_BASE_URL != null && !S3_BASE_URL.isEmpty()) {
            return S3_BASE_URL + "/" + s3Key;
        }
        
        // 환경변수가 없으면 버킷명과 리전으로 생성
        String bucket = BUCKET_NAME != null ? BUCKET_NAME : "greentable-images-your-region";
        String region = REGION != null ? REGION : "ap-northeast-2";
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, s3Key);
    }
    
    /**
     * Part에서 원본 파일명 추출
     */
    private static String getOriginalFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            for (String token : contentDisposition.split(";")) {
                if (token.trim().startsWith("filename")) {
                    String fileName = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                    return fileName;
                }
            }
        }
        return "unknown";
    }
    
    /**
     * 파일명에서 확장자 추출
     */
    private static String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return ".jpg"; // 기본 확장자
    }
}
