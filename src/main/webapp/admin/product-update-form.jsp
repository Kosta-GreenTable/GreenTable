<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <!-- 사이드 메뉴 -->
        <aside class="admin-sidebar">
            <div class="admin-logo">
                <h2>그린테이블 관리자</h2>
            </div>
            <nav class="admin-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin/index.jsp"><i class="fas fa-home"></i> 대시보드</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList"><i class="fas fa-box"></i> 상품 관리</a></li>
                    <li><a href="#"><i class="fas fa-users"></i> 회원 관리</a></li>
                    <li><a href="#"><i class="fas fa-shopping-cart"></i> 주문 관리</a></li>
                    <li><a href="#"><i class="fas fa-chart-line"></i> 통계</a></li>
                    <li><a href="#"><i class="fas fa-cog"></i> 설정</a></li>
                </ul>
            </nav>
        </aside>

        <!-- 메인 내용 -->
        <main class="admin-content">
            <header class="admin-header">
                <h1>상품 수정</h1>
                <div class="admin-user">
                    <span>관리자</span>
                    <a href="${pageContext.request.contextPath}/" class="btn-secondary"><i class="fas fa-home"></i> 사이트로 이동</a>
                    <button class="btn-danger"><i class="fas fa-sign-out-alt"></i> 로그아웃</button>
                </div>
            </header>

            <div class="product-form-container">
                <div class="form-navigation">
                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productDetail&productId=${product.productId}" class="back-link">
                        <i class="fas fa-arrow-left"></i> 상품 상세 정보로 돌아가기
                    </a>
                </div>

                <form action="${pageContext.request.contextPath}/front?key=admin&methodName=productUpdate" method="post" enctype="multipart/form-data" class="product-form" id="productForm">
                    <!-- 상품 ID (hidden) -->
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
                                    <c:when test="${not empty productImages}">
                                        <div class="image-grid">
                                            <c:forEach var="image" items="${productImages}">
                                                <div class="current-image-item">
                                                    <img src="${pageContext.request.contextPath}/assets/images/products/${image.imageName}" alt="${product.name}">
                                                    <div class="image-info">
                                                        ${image.main ? '<span class="main-badge">대표 이미지</span>' : ''}
                                                    </div>
                                                </div>
                                            </c:forEach>
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
                                    <img src="${pageContext.request.contextPath}/assets/images/no-image.jpg" alt="대표 이미지 미리보기">
                                </div>
                                <input type="file" id="productImage0" name="productImage0" accept="image/*">
                                <label for="productImage0" class="image-upload-btn">이미지 선택</label>
                                <p class="help-text">권장 크기: 500x500px, 최대 5MB</p>
                            </div>
                            
                            <div class="additional-images">
                                <h4>새 추가 이미지</h4>
                                <div class="image-upload-grid">
                                    <div class="image-item">
                                        <div class="image-preview" id="additionalImagePreview1">
                                            <img src="${pageContext.request.contextPath}/assets/images/no-image.jpg" alt="추가 이미지 1 미리보기">
                                        </div>
                                        <input type="file" id="productImage1" name="productImage1" accept="image/*">
                                        <label for="productImage1" class="image-upload-btn">이미지 선택</label>
                                    </div>
                                    <div class="image-item">
                                        <div class="image-preview" id="additionalImagePreview2">
                                            <img src="${pageContext.request.contextPath}/assets/images/no-image.jpg" alt="추가 이미지 2 미리보기">
                                        </div>
                                        <input type="file" id="productImage2" name="productImage2" accept="image/*">
                                        <label for="productImage2" class="image-upload-btn">이미지 선택</label>
                                    </div>
                                    <div class="image-item">
                                        <div class="image-preview" id="additionalImagePreview3">
                                            <img src="${pageContext.request.contextPath}/assets/images/no-image.jpg" alt="추가 이미지 3 미리보기">
                                        </div>
                                        <input type="file" id="productImage3" name="productImage3" accept="image/*">
                                        <label for="productImage3" class="image-upload-btn">이미지 선택</label>
                                    </div>
                                </div>
                                <p class="help-text">각 이미지 권장 크기: 500x500px, 최대 5MB</p>
                            </div>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn-primary">상품 수정</button>
                        <button type="button" id="cancelBtn" class="btn-secondary" data-return-url="${pageContext.request.contextPath}/front?key=admin&methodName=productDetail&productId=${product.productId}">취소</button>
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
                            img.src = '${pageContext.request.contextPath}/assets/images/no-image.jpg';
                        });
                    }
                });
            }
        });
    </script>
</body>
</html>