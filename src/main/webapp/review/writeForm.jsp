<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="site.greentable.util.ImageUtil" %>
<%@ page import="java.lang.System" %>
<%
    String s3BaseUrl = System.getenv("S3_BASE_URL");
    if (s3BaseUrl == null) {
        s3BaseUrl = "https://greentable-images-your-region.s3.ap-northeast-2.amazonaws.com";
    }
    pageContext.setAttribute("s3BaseUrl", s3BaseUrl);
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>리뷰 작성 | Green Table</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review-form.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />

    <main class="container">
        <section class="review-form-section">
            <h1 class="page-title">리뷰 작성</h1>
            
            <div class="product-info">
                <div class="product-image">
                    <c:choose>
                        <c:when test="${not empty product.mainImage}">
                            <img src="${s3BaseUrl}/${product.mainImage}" alt="${product.name}"
                                 onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">
                        </c:when>
                        <c:otherwise>
                            <img src="${s3BaseUrl}/products/no-image.jpg" alt="상품 이미지">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="product-details">
                    <h3 class="product-name">
                        <c:choose>
                            <c:when test="${not empty product.name}">${product.name}</c:when>
                            <c:otherwise>상품 ID: ${productId}</c:otherwise>
                        </c:choose>
                    </h3>
                </div>
            </div>

            <form id="reviewForm" method="post" action="${pageContext.request.contextPath}/front?key=review&methodName=writeReview" enctype="multipart/form-data">
                <input type="hidden" name="productId" value="${productId}">
                <input type="hidden" name="orderDetailId" value="${orderDetailId}">

                <div class="form-group">
                    <label>평점</label>
                    <div class="star-rating">
                        <i class="far fa-star" data-rating="1"></i>
                        <i class="far fa-star" data-rating="2"></i>
                        <i class="far fa-star" data-rating="3"></i>
                        <i class="far fa-star" data-rating="4"></i>
                        <i class="far fa-star" data-rating="5"></i>
                        <span class="rating-value">0점</span>
                    </div>
                    <input type="hidden" id="rating" name="rating" value="0">
                </div>

                <div class="form-group">
                    <label for="reviewContent">리뷰 내용</label>
                    <textarea id="reviewContent" name="content" rows="5" placeholder="상품에 대한 평가를 자유롭게 작성해주세요." required></textarea>
                    <p class="text-length">0/1000자</p>
                </div>

                <div class="form-group">
                    <label>포토 리뷰 (선택)</label>
                    <div class="photo-upload-area">
                        <div class="photo-upload-btn">
                            <i class="fas fa-plus"></i>
                            <input type="file" id="photoUpload" name="photos" accept="image/*" multiple>
                        </div>
                        <div class="photo-upload-preview"></div>
                    </div>
                    <p class="photo-upload-info">
                        * 최대 5장까지 업로드 가능합니다. (JPG, PNG 파일, 각 5MB 이하)
                    </p>
                </div>

                <div class="form-group review-benefits">
                    <h3>리뷰 적립금 안내</h3>
                    <ul>
                        <li><i class="fas fa-check"></i> 텍스트 리뷰 작성 시 500원 적립</li>
                        <li><i class="fas fa-check"></i> 포토 리뷰 작성 시 1,000원 적립</li>
                        <li><i class="fas fa-info-circle"></i> 적립금은 리뷰 작성 후 3일 이내에 지급됩니다.</li>
                    </ul>
                </div>

                <div class="form-actions">
                    <button type="button" class="cancel-btn" onclick="history.back()">취소</button>
                    <button type="submit" class="submit-btn">등록하기</button>
                </div>
            </form>
        </section>
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // 별점 기능
            const stars = document.querySelectorAll('.star-rating i');
            const ratingValue = document.querySelector('.rating-value');
            const ratingInput = document.getElementById('rating');
            
            stars.forEach(star => {
                star.addEventListener('click', function() {
                    const rating = this.getAttribute('data-rating');
                    ratingInput.value = rating;
                    ratingValue.textContent = rating + '점';
                    
                    // 별점 UI 업데이트
                    stars.forEach(s => {
                        if (s.getAttribute('data-rating') <= rating) {
                            s.className = 'fas fa-star';
                        } else {
                            s.className = 'far fa-star';
                        }
                    });
                });
            });
            
            // 글자수 카운트 기능
            const reviewContent = document.getElementById('reviewContent');
            const textLength = document.querySelector('.text-length');
            
            reviewContent.addEventListener('input', function() {
                const length = this.value.length;
                textLength.textContent = length + '/1000자';
                
                if (length > 1000) {
                    textLength.style.color = 'red';
                } else {
                    textLength.style.color = '';
                }
            });
            
            // 이미지 미리보기 기능
            const photoUpload = document.getElementById('photoUpload');
            const photoPreview = document.querySelector('.photo-upload-preview');
            
            photoUpload.addEventListener('change', function() {
                const files = this.files;
                
                if (files.length > 5) {
                    alert('이미지는 최대 5장까지 업로드 가능합니다.');
                    this.value = '';
                    return;
                }
                
                photoPreview.innerHTML = '';
                
                for (let i = 0; i < files.length; i++) {
                    if (files[i].size > 5 * 1024 * 1024) {
                        alert('이미지 크기는 5MB 이하만 가능합니다.');
                        this.value = '';
                        photoPreview.innerHTML = '';
                        return;
                    }
                    
                    const reader = new FileReader();
                    reader.onload = function(e) {
                        const div = document.createElement('div');
                        div.className = 'photo-preview-item';
                        div.innerHTML = `
                            <img src="${e.target.result}" alt="미리보기 이미지">
                            <button type="button" class="remove-photo">&times;</button>
                        `;
                        photoPreview.appendChild(div);
                        
                        // 이미지 삭제 버튼 기능
                        div.querySelector('.remove-photo').addEventListener('click', function() {
                            div.remove();
                            if (photoPreview.children.length === 0) {
                                photoUpload.value = '';
                            }
                        });
                    }
                    reader.readAsDataURL(files[i]);
                }
            });
            
            // 폼 제출 전 유효성 검사
            const reviewForm = document.getElementById('reviewForm');
            reviewForm.addEventListener('submit', function(e) {
                const rating = ratingInput.value;
                const content = reviewContent.value.trim();
                
                if (rating === '0') {
                    e.preventDefault();
                    alert('별점을 선택해주세요.');
                    return;
                }
                
                if (content.length < 10) {
                    e.preventDefault();
                    alert('리뷰 내용은 최소 10자 이상 입력해주세요.');
                    return;
                }
                
                if (content.length > 1000) {
                    e.preventDefault();
                    alert('리뷰 내용은 최대 1000자까지 입력 가능합니다.');
                    return;
                }
            });
        });
    </script>
</body>
</html>