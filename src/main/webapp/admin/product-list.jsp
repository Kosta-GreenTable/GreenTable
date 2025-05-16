<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>그린테이블 관리자 - 상품 관리</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-table-fix.css">
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
                <h1>상품 관리</h1>
                <div class="admin-user">
                    <span>관리자</span>
                    <a href="${pageContext.request.contextPath}/" class="btn-secondary"><i class="fas fa-home"></i> 사이트로 이동</a>
                    <button class="btn-danger"><i class="fas fa-sign-out-alt"></i> 로그아웃</button>
                </div>
            </header>            <div class="product-management">
                <div class="product-actions">
                    <div style="display: flex; gap: 10px;">
                        <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productInsertForm" class="btn-primary"><i class="fas fa-plus"></i> 새 상품 등록</a>
                        <a href="${pageContext.request.contextPath}/front?key=admin&methodName=resetProductId" class="btn-secondary"><i class="fas fa-sync"></i> ID 시퀀스 리셋</a>
                    </div>
                    <div class="product-filter">
                        <select id="categoryFilter">
                            <option value="all">모든 카테고리</option>
                            <option value="도시락">도시락</option>
                            <option value="샐러드">샐러드</option>
                        </select>
                        <div class="search-box">
                            <input type="text" id="productSearch" placeholder="상품 검색">
                            <button class="search-btn"><i class="fas fa-search"></i></button>
                        </div>
                    </div>                </div>
                
                <c:if test="${not empty successMessage || not empty errorMessage}">
                    <div class="alert ${not empty errorMessage ? 'alert-danger' : 'alert-success'}" style="margin-bottom: 20px; padding: 10px; border-radius: 4px; background-color: ${not empty errorMessage ? '#f8d7da' : '#d4edda'}; color: ${not empty errorMessage ? '#721c24' : '#155724'}; border: 1px solid ${not empty errorMessage ? '#f5c6cb' : '#c3e6cb'};">
                        ${not empty errorMessage ? errorMessage : successMessage}
                    </div>
                </c:if>

                <div class="product-list-container">
                    <table class="admin-table product-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>이미지</th>
                                <th>상품명</th>
                                <th>카테고리</th>
                                <th>가격</th>
                                <th>재고</th>
                                <th>할인율</th>
                                <th class="action-column">관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="product" items="${productList}">
                                <tr class="product-row" data-category="${product.category}">
                                    <td>${product.productId}</td>
                                    <td class="product-image-cell">
                                        <c:choose>
                                            <c:when test="${not empty product.mainImage}">
                                                <img src="${pageContext.request.contextPath}/assets/images/products/${product.mainImage}" alt="${product.name}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/assets/images/no-image.jpg" alt="이미지 없음">
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${product.name}</td>
                                    <td>${product.category}</td>
                                    <td><fmt:formatNumber value="${product.price}" pattern="#,###" />원</td>
                                    <td>${product.stock}개</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${product.discountRate > 0}">
                                                ${product.discountRate}%
                                            </c:when>
                                            <c:otherwise>
                                                -
                                            </c:otherwise>
                                        </c:choose>
                                    </td>                                    <td class="action-column">
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productDetail&productId=${product.productId}" class="btn-small btn-info" title="상세보기"><i class="fas fa-eye"></i></a>
                                            <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productUpdateForm&productId=${product.productId}" class="btn-small btn-secondary" title="수정"><i class="fas fa-edit"></i></a>
                                            <button class="btn-small btn-danger delete-product" data-id="${product.productId}" title="삭제"><i class="fas fa-trash-alt"></i></button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        
                            <c:if test="${empty productList}">
                                <tr>
                                    <td colspan="8" class="no-products">등록된 상품이 없습니다.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>                <!-- 페이지네이션 향상된 버전 -->
                <div class="pagination">
                    <c:if test="${not empty totalPages && totalPages > 1}">
                        <ul class="page-numbers">
                            <!-- 처음 페이지로 이동 -->
                            <c:if test="${currentPage > 1}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList&page=1" title="처음"><i class="fas fa-angle-double-left"></i></a>
                                </li>
                            </c:if>
                            
                            <!-- 이전 블록으로 이동 -->
                            <c:if test="${currentPage > pageCnt.blockcount}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList&page=${currentPage-pageCnt.blockcount}" title="이전"><i class="fas fa-angle-left"></i></a>
                                </li>
                            </c:if>
                            
                            <!-- 페이지 번호 표시 -->
                            <c:set var="startPage" value="${(((currentPage-1) / pageCnt.blockcount) * pageCnt.blockcount) + 1}" />
                            <c:set var="endPage" value="${startPage + pageCnt.blockcount - 1}" />
                            <c:if test="${endPage > totalPages}">
                                <c:set var="endPage" value="${totalPages}" />
                            </c:if>
                            
                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                <li class="${i == currentPage ? 'active' : ''}">
                                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList&page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            
                            <!-- 다음 블록으로 이동 -->
                            <c:if test="${endPage < totalPages}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList&page=${startPage + pageCnt.blockcount}" title="다음"><i class="fas fa-angle-right"></i></a>
                                </li>
                            </c:if>
                            
                            <!-- 마지막 페이지로 이동 -->
                            <c:if test="${currentPage < totalPages}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList&page=${totalPages}" title="마지막"><i class="fas fa-angle-double-right"></i></a>
                                </li>
                            </c:if>
                        </ul>
                    </c:if>
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
    <script src="${pageContext.request.contextPath}/admin/js/product-list.js"></script>
</body>
</html>