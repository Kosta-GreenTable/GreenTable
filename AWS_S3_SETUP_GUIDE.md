# AWS S3 버킷 설정 가이드

## 1. AWS S3 버킷 생성

### 1.1 AWS 콘솔에서 S3 버킷 생성

1. AWS Management Console 로그인
2. S3 서비스 접속
3. "버킷 만들기" 클릭
4. 버킷 이름 입력 (예: `greentable-images-your-region`)
   - 전역적으로 고유한 이름이어야 함
   - 소문자, 숫자, 하이픈만 사용 가능
5. 리전 선택 (한국의 경우 `ap-northeast-2` 권장)

### 1.2 버킷 설정

- **객체 소유권**: ACL 비활성화됨 (권장)
- **퍼블릭 액세스 차단**: 모든 퍼블릭 액세스 차단 해제
- **버킷 버전 관리**: 비활성화 (선택사항)
- **암호화**: 기본 암호화 활성화 (SSE-S3)

## 2. IAM 사용자 생성 및 권한 설정

### 2.1 IAM 사용자 생성

1. AWS Management Console에서 IAM 서비스 접속
2. "사용자" → "사용자 추가"
3. 사용자 이름 입력 (예: `greentable-s3-user`)
4. 액세스 유형: "프로그래밍 방식 액세스" 선택

### 2.2 권한 정책 연결

다음 정책을 생성하여 사용자에게 연결:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject",
        "s3:DeleteObject",
        "s3:ListBucket"
      ],
      "Resource": [
        "arn:aws:s3:::your-bucket-name",
        "arn:aws:s3:::your-bucket-name/*"
      ]
    }
  ]
}
```

### 2.3 액세스 키 생성

- 사용자 생성 완료 후 액세스 키 ID와 비밀 액세스 키를 안전하게 보관

## 3. 버킷 정책 설정 (퍼블릭 읽기 허용)

S3 버킷의 "권한" 탭에서 다음 버킷 정책 추가:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::your-bucket-name/*"
    }
  ]
}
```

## 4. CORS 설정

버킷의 "권한" 탭 → "CORS(Cross-origin resource sharing)" 에서 다음 설정 추가:

```json
[
  {
    "AllowedHeaders": ["*"],
    "AllowedMethods": ["GET", "PUT", "POST", "DELETE", "HEAD"],
    "AllowedOrigins": ["*"],
    "ExposeHeaders": ["ETag"],
    "MaxAgeSeconds": 3000
  }
]
```

## 5. 환경 변수 설정

`.env` 파일에 다음 변수들을 설정:

```env
# AWS S3 Configuration
AWS_ACCESS_KEY_ID=your-access-key-id
AWS_SECRET_ACCESS_KEY=your-secret-access-key
AWS_REGION=ap-northeast-2
S3_BUCKET_NAME=your-bucket-name
```

## 6. 폴더 구조 권장사항

S3 버킷 내에서 다음과 같은 폴더 구조 사용을 권장:

```
your-bucket-name/
├── products/           # 상품 이미지
│   ├── main/          # 메인 이미지
│   └── additional/    # 추가 이미지
├── reviews/           # 리뷰 이미지
├── users/             # 사용자 프로필 이미지 (향후 확장)
└── temp/              # 임시 파일 (향후 확장)
```

## 7. 보안 고려사항

### 7.1 액세스 키 보안

- 액세스 키를 코드에 하드코딩하지 말 것
- 환경 변수나 AWS IAM 역할 사용
- 정기적으로 액세스 키 로테이션

### 7.2 버킷 보안

- 불필요한 퍼블릭 액세스 제한
- CloudTrail로 API 호출 로깅
- 버킷 정책으로 세밀한 권한 제어

### 7.3 비용 최적화

- S3 Intelligent-Tiering 고려
- 오래된 파일 자동 삭제 정책 설정
- CloudFront CDN 사용 고려 (글로벌 서비스 시)

## 8. 테스트 방법

1. 애플리케이션 시작 후 관리자 페이지에서 상품 등록
2. 이미지 업로드가 성공적으로 되는지 확인
3. S3 콘솔에서 파일이 업로드되었는지 확인
4. 사용자 페이지에서 이미지가 정상적으로 표시되는지 확인
5. 리뷰 작성 시 이미지 업로드 기능 테스트

## 9. 문제 해결

### 9.1 업로드 실패

- AWS 자격 증명 확인
- 버킷 권한 정책 확인
- 네트워크 연결 상태 확인

### 9.2 이미지 표시 안됨

- 버킷 정책의 퍼블릭 읽기 권한 확인
- CORS 설정 확인
- 이미지 URL 형식 확인

### 9.3 권한 오류

- IAM 사용자 권한 정책 확인
- 버킷 정책과 IAM 권한 충돌 확인
