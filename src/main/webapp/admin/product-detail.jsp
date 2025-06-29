<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>그린테이블 관리자 - 상품 상세 정보</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
</head>
<body>
    <div class="admin-container">
        <!-- 사이드 메뉴 -->
        <jsp:include page="common/admin-sidebar.jsp" />

        <!-- 메인 내용 -->
        <main class="admin-content">
            <!-- 상단 헤더 포함 -->
            <jsp:include page="common/admin-top-header.jsp">
                <jsp:param name="pageTitle" value="상품 상세 정보" />
            </jsp:include>

            <div class="product-detail-container">
                <div class="form-navigation">
                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList" class="back-link">
                        <i class="fas fa-arrow-left"></i> 상품 목록으로 돌아가기
                    </a>
                </div>

                <div class="product-detail-actions">
                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productUpdateForm&productId=${product.productId}" class="btn-secondary"><i class="fas fa-edit"></i> 상품 수정</a>
                    <button id="deleteProductBtn" class="btn-danger" data-id="${product.productId}"><i class="fas fa-trash-alt"></i> 상품 삭제</button>
                </div>

                <div class="product-detail-content">
                    <div class="product-images-section">
                        <h3>상품 이미지</h3>
                        <div class="product-main-image">
                            <c:choose>
                                <c:when test="${not empty productImages}">
                                    <c:forEach var="image" items="${productImages}">
                                        <c:if test="${image.main}">
                                            <div class="main-image-container">
                                                <h4>대표 이미지</h4>
                                                <img src="${pageContext.request.contextPath}/assets/images/products/${image.imageName}" alt="${product.name} 대표 이미지">
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="main-image-container">
                                        <h4>대표 이미지</h4>
                                        <img src="${pageContext.request.contextPath}/assets/images/no-image.jpg" alt="이미지 없음">
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="product-additional-images">
                            <h4>추가 이미지</h4>
                            <div class="additional-images-container">
                                <c:set var="hasAdditionalImages" value="false" />
                                <c:forEach var="image" items="${productImages}">
                                    <c:if test="${not image.main}">
                                        <c:set var="hasAdditionalImages" value="true" />
                                        <div class="additional-image">
                                            <img src="${pageContext.request.contextPath}/assets/images/products/${image.imageName}" alt="${product.name} 추가 이미지">
                                        </div>
                                    </c:if>
                                </c:forEach>
                                
                                <c:if test="${not hasAdditionalImages}">
                                    <p class="no-images-message">추가 이미지가 없습니다.</p>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="product-info-section">
                        <h3>기본 정보</h3>
                        <table class="detail-table">
                            <tr>
                                <th>상품 ID</th>
                                <td>${product.productId}</td>
                            </tr>
                            <tr>
                                <th>상품명</th>
                                <td>${product.name}</td>
                            </tr>
                            <tr>
                                <th>부제목</th>
                                <td>${product.subName}</td>
                            </tr>
                            <tr>
                                <th>카테고리</th>
                                <td>${product.category}</td>
                            </tr>
                            <tr>
                                <th>가격</th>
                                <td><fmt:formatNumber value="${product.price}" pattern="#,###" />원</td>
                            </tr>
                            <tr>
                                <th>재고</th>
                                <td>${product.stock}개</td>
                            </tr>
                            <tr>
                                <th>할인율</th>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.discountRate > 0}">
                                            ${product.discountRate}%
                                        </c:when>
                                        <c:otherwise>
                                            -
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div class="product-detail-section">
                        <h3>상세 정보</h3>
                        <table class="detail-table">
                            <tr>
                                <th>상품 설명</th>
                                <td>${productDetail.description}</td>
                            </tr>
                            <tr>
                                <th>원재료</th>
                                <td>${productDetail.ingredients}</td>
                            </tr>
                            <tr>
                                <th>칼로리</th>
                                <td>${productDetail.kcal} kcal</td>
                            </tr>
                            <tr>
                                <th>중량</th>
                                <td>${productDetail.amount} g</td>
                            </tr>
                            <tr>
                                <th>보관 방법</th>
                                <td>${productDetail.nutrition}</td>
                            </tr>
                            <tr>
                                <th>등록일</th>
                                <td>${productDetail.createdDate}</td>
                            </tr>
                            <tr>
                                <th>수정일</th>
                                <td>${productDetail.updatedDate}</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- 상품 삭제 확인 모달 -->
    <div id="deleteModal" class="modal">
        <div class="modal-content">
            <h3>상품 삭제</h3>
            <p>정말 이 상품을 삭제하시겠습니까?</p>
            <p>이 작업은 되돌릴 수 없습니다.</p>
            <div class="modal-buttons">
                <a id="confirmDelete" href="${pageContext.request.contextPath}/front?key=admin&methodName=productDelete&productId=${product.productId}" class="btn-danger">삭제</a>
                <button id="cancelDelete" class="btn-secondary">취소</button>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/admin/js/admin-script.js"></script>
    <script src="${pageContext.request.contextPath}/admin/js/product-detail.js"></script>
</body>
</html>