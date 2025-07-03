<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>그린테이블 관리자 - 상품 수정</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
</head>
<body>
    <div class="admin-container">
        <!-- 사이드바 포함 -->
        <jsp:include page="common/admin-sidebar.jsp" />

        <!-- 메인 내용 -->
        <main class="admin-content">
            <!-- 상단 헤더 포함 -->
            <jsp:include page="common/admin-top-header.jsp">
                <jsp:param name="pageTitle" value="상품 수정" />
            </jsp:include>

            <div class="product-form-container">
                <div class="form-navigation">
                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList" class="back-link">
                        <i class="fas fa-arrow-left"></i> 상품 목록으로 돌아가기
                    </a>
                </div>

                <form action="${pageContext.request.contextPath}/front?key=admin&methodName=productUpdate" method="post" enctype="multipart/form-data" class="product-form" id="productForm">
                    <!-- 상품 번호 (hidden) -->
                    <input type="hidden" name="productId" value="${product.productId}">
                    
                    <div class="form-section">
                        <h3>기본 정보</h3>
                        <div class="form-group">
                            <label for="name">상품명 <span class="required">*</span></label>
                            <input type="text" id="name" name="name" value="${product.name}" required>
                        </div>
                        <div class="form-group">
                            <label for="subName">부제목</label>
                            <input type="text" id="subName" name="subName" value="${product.subName}">
                        </div>
                        <div class="form-group">
                            <label for="category">카테고리 <span class="required">*</span></label>
                            <select id="category" name="category" required>
                                <option value="">카테고리 선택</option>
                                <option value="도시락" ${product.category == '도시락' ? 'selected' : ''}>도시락</option>
                                <option value="샐러드" ${product.category == '샐러드' ? 'selected' : ''}>샐러드</option>
                                <option value="정기배송" ${product.category == '정기배송' ? 'selected' : ''}>정기배송</option>
                                <option value="베스트" ${product.category == '베스트' ? 'selected' : ''}>베스트</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="price">가격 (원) <span class="required">*</span></label>
                            <input type="number" id="price" name="price" min="0" value="${product.price}" required>
                        </div>
                        <div class="form-group">
                            <label for="stock">재고 수량 <span class="required">*</span></label>
                            <input type="number" id="stock" name="stock" min="0" value="${product.stock}" required>
                        </div>
                        <div class="form-group">
                            <label for="discountRate">할인율 (%)</label>
                            <input type="number" id="discountRate" name="discountRate" min="0" max="100" value="${product.discountRate}">
                        </div>
                    </div>
                    
                    <div class="form-section">
                        <h3>상품 상세 정보</h3>
                        <div class="form-group">
                            <label for="description">상품 설명</label>
                            <textarea id="description" name="description" rows="5">${productDetail.description}</textarea>
                        </div>
                        <div class="form-group">
                            <label for="ingredients">원재료</label>
                            <input type="text" id="ingredients" name="ingredients" value="${productDetail.ingredients}">
                        </div>
                        <div class="form-group">
                            <label for="kcal">칼로리 (kcal)</label>
                            <input type="number" id="kcal" name="kcal" min="0" value="${productDetail.kcal}">
                        </div>
                        <div class="form-group">
                            <label for="amount">중량 (g)</label>
                            <input type="number" id="amount" name="amount" min="0" value="${productDetail.amount}">
                        </div>
                        <div class="form-group">
                            <label for="nutrition">보관 방법</label>
                            <select id="nutrition" name="nutrition">
                                <option value="">보관 방법 선택</option>
                                <option value="냉장" ${productDetail.nutrition == '냉장' ? 'selected' : ''}>냉장</option>
                                <option value="냉동" ${productDetail.nutrition == '냉동' ? 'selected' : ''}>냉동</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-section">
                        <h3>상품 이미지</h3>
                        
                        <div class="form-group">
                            <label>현재 이미지</label>
                            <div class="current-images">
                                <c:choose>
                                    <c:when test="${not empty productImages and productImages.size() > 0}">
                                        <div class="image-grid">
                                            <c:forEach var="image" items="${productImages}" varStatus="status">
                                                <div class="current-image-item">
                                                    <img src="${s3BaseUrl}/${image.imageName}" alt="${product.name} - 이미지 ${status.index + 1}"
                                                         onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">
                                                    <div class="image-info">
                                                        <c:choose>
                                                            <c:when test="${image.isMain}">
                                                                <span class="main-badge">대표 이미지</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="additional-badge">추가 이미지 ${status.index}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:when test="${not empty product.mainImageName and product.mainImageName != 'no-image.jpg'}">
                                        <!-- 기존 단일 이미지 표시 (하위 호환성) -->
                                        <div class="image-grid">
                                            <div class="current-image-item">
                                                <img src="${s3BaseUrl}/${product.mainImageName}" alt="${product.name}"
                                                     onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">
                                                <div class="image-info">
                                                    <span class="main-badge">대표 이미지</span>
                                                </div>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="no-images-message">등록된 이미지가 없습니다.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <div class="image-update-options">
                                <label class="checkbox-label">
                                    <input type="checkbox" id="updateImagesCheck" name="updateImages" value="true">
                                    이미지를 새로 업로드하시겠습니까?
                                </label>
                                <p class="help-text">체크하면 기존 이미지가 모두 삭제되고 새 이미지로 대체됩니다.</p>
                            </div>
                        </div>
                        
                        <div id="imageUploadSection" class="image-upload-container" style="display: none;">
                            <div class="main-image-upload">
                                <h4>새 대표 이미지</h4>
                                <div class="image-preview" id="mainImagePreview">
                                    <img src="${s3BaseUrl}/products/no-image.jpg" alt="대표 이미지 미리보기">
                                </div>
                                <!-- 이름 변경 -->
                                <input type="file" id="mainImage" name="mainImage" accept="image/*">
                                <label for="mainImage" class="image-upload-btn">이미지 선택</label>
                                <p class="help-text">권장 크기: 500x500px, 최대 5MB</p>
                            </div>
                            
                            <div class="additional-images">
                                <h4>새 추가 이미지</h4>
                                <div class="image-upload-grid">
                                    <div class="image-item">
                                        <div class="image-preview" id="additionalImagePreview1">
                                            <img src="${s3BaseUrl}/products/no-image.jpg" alt="추가 이미지 1 미리보기">
                                        </div>
                                        <!-- 이름 변경 -->
                                        <input type="file" id="image1" name="image1" accept="image/*">
                                        <label for="image1" class="image-upload-btn">이미지 선택</label>
                                    </div>
                                    <div class="image-item">
                                        <div class="image-preview" id="additionalImagePreview2">
                                            <img src="${s3BaseUrl}/products/no-image.jpg" alt="추가 이미지 2 미리보기">
                                        </div>
                                        <!-- 이름 변경 -->
                                        <input type="file" id="image2" name="image2" accept="image/*">
                                        <label for="image2" class="image-upload-btn">이미지 선택</label>
                                    </div>
                                    <div class="image-item">
                                        <div class="image-preview" id="additionalImagePreview3">
                                            <img src="${s3BaseUrl}/products/no-image.jpg" alt="추가 이미지 3 미리보기">
                                        </div>
                                        <!-- 이름 변경 -->
                                        <input type="file" id="image3" name="image3" accept="image/*">
                                        <label for="image3" class="image-upload-btn">이미지 선택</label>
                                    </div>
                                </div>
                                <p class="help-text">각 이미지 권장 크기: 500x500px, 최대 5MB</p>
                            </div>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-primary">상품 수정</button>
                        <button type="button" id="cancelBtn" class="btn-secondary" data-return-url="${pageContext.request.contextPath}/front?key=admin&methodName=productList">취소</button>
                    </div>
                </form>
            </div>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/admin/js/admin-script.js"></script>
    <script src="${pageContext.request.contextPath}/admin/js/product-form.js"></script>
    <script>
        // 이미지 업로드 섹션 토글
        document.addEventListener('DOMContentLoaded', function() {
            const updateImagesCheck = document.getElementById('updateImagesCheck');
            const imageUploadSection = document.getElementById('imageUploadSection');
            
            if (updateImagesCheck && imageUploadSection) {
                updateImagesCheck.addEventListener('change', function() {
                    imageUploadSection.style.display = this.checked ? 'flex' : 'none';
                    
                    // 체크 해제 시 파일 입력 초기화
                    if (!this.checked) {
                        const fileInputs = imageUploadSection.querySelectorAll('input[type="file"]');
                        fileInputs.forEach(input => input.value = '');
                        
                        // 이미지 미리보기 초기화
                        const previewImages = imageUploadSection.querySelectorAll('.image-preview img');
                        previewImages.forEach(img => {
                            img.src = '${s3BaseUrl}/products/no-image.jpg';
                        });
                    }
                });
            }
        });
    </script>
</body>
</html>
