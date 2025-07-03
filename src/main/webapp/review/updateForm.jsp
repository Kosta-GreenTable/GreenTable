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
    <title>리뷰 수정 | Green Table</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review-form.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />

    <main class="container">
        <section class="review-form-section">
            <h1 class="page-title">리뷰 수정</h1>
            
            <div class="product-info">
                <div class="product-image">
                    <img src="${product.mainImage}" alt="${product.name}">
                </div>
                <div class="product-details">
                    <h3 class="product-name">${product.name}</h3>
                </div>
            </div>

            <form id="reviewForm" method="post" action="${pageContext.request.contextPath}/front?key=review&methodName=updateReview" enctype="multipart/form-data">
                <input type="hidden" name="reviewId" value="${review.reviewId}">
                <input type="hidden" name="productId" value="${review.productId}">

                <div class="form-group">
                    <label>평점</label>
                    <div class="star-rating">
                        <c:forEach begin="1" end="5" var="i">
                            <i class="${i <= review.rating ? 'fas' : 'far'} fa-star" data-rating="${i}"></i>
                        </c:forEach>
                        <span class="rating-value">${review.rating}점</span>
                    </div>
                    <input type="hidden" id="rating" name="rating" value="${review.rating}">
                </div>

                <div class="form-group">
                    <label for="reviewText">리뷰 내용</label>
                    <textarea id="reviewText" name="content" rows="5" required>${review.content}</textarea>
                    <p class="text-length">${review.content.length()}/1000자</p>
                </div>

                <div class="form-group">
                    <label>포토 리뷰 (선택)</label>
                    <div class="photo-upload-area">
                        <div class="photo-upload-btn">
                            <i class="fas fa-plus"></i>
                            <input type="file" id="photoUpload" name="photos" accept="image/*" multiple>
                        </div>
                        <div class="photo-upload-preview">
                            <c:if test="${not empty review.images}">
                                <c:forEach var="image" items="${review.images}">
                                    <div class="photo-preview-item">
                                        <img src="${s3BaseUrl}/${image.realName}" alt="리뷰 이미지"
                                             onerror="this.onerror=null; this.src='${s3BaseUrl}/reviews/no-image.jpg';">
                                        <button type="button" class="remove-photo" data-image-id="${image.reviewImageId}">&times;</button>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </div>
                    </div>
                    <p class="photo-upload-info">
                        * 최대 5장까지 업로드 가능합니다. (JPG, PNG 파일, 각 5MB 이하)
                    </p>
                    <input type="hidden" name="imageChanged" id="imageChanged" value="false">
                </div>

                <div class="form-group review-benefits">
                    <h3>리뷰 수정 안내</h3>
                    <ul>
                        <li><i class="fas fa-exclamation-triangle"></i> 리뷰를 수정할 경우, 기존에 지급된 포인트는 유지됩니다.</li>
                        <li><i class="fas fa-exclamation-triangle"></i> 리뷰 이미지를 삭제하거나 포토 리뷰에서 일반 리뷰로 변경할 경우, 기존에 지급된 포토 리뷰 포인트는 회수될 수 있습니다.</li>
                    </ul>
                </div>

                <div class="form-actions">
                    <button type="button" class="cancel-btn" onclick="history.back()">취소</button>
                    <button type="submit" class="submit-btn">수정완료</button>
                </div>
            </form>
        </section>
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/review-form.js"></script>
</body>
</html>