# GreenTable - AWS S3 이미지 업로드 적용

이 프로젝트는 기존의 로컬 파일 업로드 방식에서 AWS S3 클라우드 스토리지로 전환되었습니다.

## 주요 변경사항

### 1. AWS S3 업로드 적용

- **문제 해결**: Tomcat의 멀티파트 파일 개수 제한(`FileCountLimitExceededException`) 해결
- **클라우드 이미지 저장**: 확장성과 성능 향상
- **하위 호환성**: 기존 로컬 이미지와 S3 이미지 모두 지원

### 2. 기술적 구현

- `S3Util.java`: AWS S3 업로드/삭제 유틸리티 클래스
- `AdminController`: S3 업로드 방식으로 전면 리팩토링
- JSP 페이지들: S3 이미지 URL 자동 인식 및 표시

### 3. 지원하는 기능

- ✅ 상품 등록 시 이미지 S3 업로드
- ✅ 상품 수정 시 기존 S3 이미지 삭제 후 새 이미지 업로드
- ✅ 상품 삭제 시 S3 이미지 자동 삭제
- ✅ 리뷰 작성/수정 시 이미지 S3 업로드
- ✅ 리뷰 삭제 시 S3 이미지 자동 삭제
- ✅ 모든 페이지에서 S3 이미지 자동 표시
- ✅ 코드 단순화 (로컬/S3 분기 로직 제거)
- ✅ ImageUtil 클래스 S3 전용으로 최적화

## 배포 방법

### 1. AWS S3 버킷 설정

```bash
# S3 버킷 생성 (AWS CLI 사용)
aws s3 mb s3://greentable-images --region ap-northeast-2

# 버킷 정책 설정 (공개 읽기 허용)
aws s3api put-bucket-policy --bucket greentable-images --policy '{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::greentable-images/*"
    }
  ]
}'
```

### 2. 환경변수 설정

`.env` 파일을 생성하고 다음 내용을 입력:

```bash
# AWS 자격증명
AWS_ACCESS_KEY_ID=your_aws_access_key_id
AWS_SECRET_ACCESS_KEY=your_aws_secret_access_key
AWS_DEFAULT_REGION=ap-northeast-2
S3_BUCKET_NAME=greentable-images

# 기존 이메일 설정
MAIL_USERNAME=kosta295mail@gmail.com
MAIL_PASSWORD=vnjimaiikpaqojsn
MAIL_SMTP_HOST=smtp.gmail.com
MAIL_SMTP_PORT=587
```

### 3. Docker Compose 실행

```bash
# 환경변수 파일과 함께 실행
docker-compose --env-file .env up -d

# 또는 직접 환경변수 설정하여 실행
AWS_ACCESS_KEY_ID=xxx AWS_SECRET_ACCESS_KEY=yyy S3_BUCKET_NAME=greentable-images docker-compose up -d
```

## 이미지 처리 방식

### S3 전용 이미지 관리

- **상품 이미지**: `products/UUID.확장자` 형태로 S3에 저장
- **리뷰 이미지**: `reviews/UUID.확장자` 형태로 S3에 저장
- **기본 이미지**: `products/no-image.jpg`, `reviews/no-image.jpg` S3에 저장

### JSP에서 이미지 표시 (S3 전용)

```jsp
<!-- 상품 이미지 -->
<img src="${s3BaseUrl}/${product.mainImageName}"
     alt="${product.name}"
     onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">

<!-- 리뷰 이미지 -->
<img src="${s3BaseUrl}/${review.imageName}"
     alt="리뷰 이미지"
     onerror="this.onerror=null; this.src='${s3BaseUrl}/reviews/no-image.jpg';">
```

### ImageUtil 유틸리티 클래스 (단순화)

```java
// S3 전용 URL 생성
String imageUrl = ImageUtil.getProductImageUrl(imageName);
String reviewUrl = ImageUtil.getReviewImageUrl(imageName);
```

## 트러블슈팅

### 1. S3 업로드 실패

- AWS 자격증명 확인
- S3 버킷 권한 확인
- 네트워크 연결 상태 확인

### 2. 이미지 표시 안됨

- S3 버킷 공개 읽기 권한 확인
- CORS 설정 확인 (필요시)
- 브라우저 개발자 도구에서 404/403 오류 확인

### 3. 기존 데이터 마이그레이션

기존 로컬 이미지들은 그대로 유지되며, 새로 업로드되는 이미지만 S3로 저장됩니다.
필요시 기존 이미지들을 S3로 마이그레이션하는 스크립트를 별도로 작성할 수 있습니다.

## 성능 및 비용 최적화

### CDN 연동 (옵션)

향후 CloudFront CDN과 연동하여 이미지 로딩 속도를 더욱 향상시킬 수 있습니다.

### 이미지 압축 (옵션)

업로드 시 이미지 압축 기능을 추가하여 저장공간과 전송량을 최적화할 수 있습니다.

---

이제 상품 등록 시 발생했던 `FileCountLimitExceededException` 오류가 해결되며,
클라우드 환경에 적합한 확장 가능한 이미지 저장 시스템이 구축되었습니다.
