<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">    <title>검색 결과: ${query} | Green Table</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/search-results.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />
    
    <!-- 메인 콘텐츠 -->
    <main class="product-list-container">        <section class="product-category">
            <h2>검색 결과: "${query}"</h2>
            <p class="search-result-count">총 ${productList.size()}개의 상품이 검색되었습니다.</p>
        </section>
        
        <c:if test="${not empty productList}">
            <section class="sort-options">
                <div class="sort-container">
                    <span>정렬:</span>
                    <select id="sort-select" class="sort-select">
                        <option value="relevance" ${sortOption eq 'relevance' ? 'selected' : ''}>관련도순</option>
                        <option value="newest" ${sortOption eq 'newest' ? 'selected' : ''}>최신순</option>
                        <option value="price-asc" ${sortOption eq 'price-asc' ? 'selected' : ''}>가격 낮은순</option>
                        <option value="price-desc" ${sortOption eq 'price-desc' ? 'selected' : ''}>가격 높은순</option>
                        <option value="discount" ${sortOption eq 'discount' ? 'selected' : ''}>할인율순</option>
                    </select>
                </div>
            </section>
        </c:if>
        
        <section class="product-grid">
            <c:forEach items="${productList}" var="product">                <div class="product-card">
                    <a href="${pageContext.request.contextPath}/front?key=product&methodName=detail&productId=${product.productId}">
                        <div class="product-image">
                            <!-- S3 전용 이미지 URL -->
                            <img src="${s3BaseUrl}/${product.mainImageName}" alt="${product.name}"
                                 onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">
                            <c:if test="${product.discountRate > 10}">
                                <div class="product-tag">특가</div>
                            </c:if>
                            <c:if test="${product.category eq '베스트'}">
                                <div class="product-tag" style="background-color: rgba(244, 67, 54, 0.9);">BEST</div>
                            </c:if>
                        </div>
                        <div class="product-info">
                            <div class="product-category-label">${product.category}</div>
                            <h3>${product.name}</h3>
                            <p class="product-subname">${product.subName}</p>
                            <div class="product-price">
                                <c:if test="${product.discountRate > 0}">
                                    <span class="original-price"><fmt:formatNumber value="${product.price}" pattern="#,###"/>원</span>
                                    <span class="discount-rate">${product.discountRate}%</span>
                                </c:if>
                                <span class="final-price"><fmt:formatNumber value="${product.discountedPrice}" pattern="#,###"/>원</span>
                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
              <!-- 검색 결과가 없을 경우 -->
            <c:if test="${empty productList}">
                <div class="no-search-results">
                    <p>"${query}"에 대한 검색 결과가 없습니다.</p>
                    <p>다른 검색어를 입력하거나 철자와 띄어쓰기를 확인해보세요.</p>
                </div>
                
                <!-- 추천 상품 섹션 -->
                <c:if test="${not empty recommendedProducts}">
                    <div class="recommended-products-section">
                        <h3>이런 상품은 어떠세요?</h3>
                        <div class="recommended-products">
                            <c:forEach items="${recommendedProducts}" var="product" begin="0" end="3">
                                <div class="product-card">
                                    <a href="${pageContext.request.contextPath}/front?key=product&methodName=detail&productId=${product.productId}">
                                        <div class="product-image">
                                            <!-- S3 전용 이미지 URL -->
                                            <img src="${s3BaseUrl}/${product.mainImageName}" alt="${product.name}"
                                                 onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">
                                        </div>
                                        <div class="product-info">
                                            <div class="product-category-label">${product.category}</div>
                                            <h3>${product.name}</h3>
                                            <div class="product-price">
                                                <c:if test="${product.discountRate > 0}">
                                                    <span class="original-price"><fmt:formatNumber value="${product.price}" pattern="#,###"/>원</span>
                                                    <span class="discount-rate">${product.discountRate}%</span>
                                                </c:if>
                                                <span class="final-price"><fmt:formatNumber value="${product.discountedPrice}" pattern="#,###"/>원</span>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </c:if>
        </section>
        
        <!-- 페이지네이션 -->
        <section class="pagination">
            <c:if test="${totalPages > 1}">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <c:when test="${i eq pageNo}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/front?key=product&methodName=search&query=${query}&pageNo=${i}&sort=${param.sort}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
        </section>
    </main>
      <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />
      <!-- 자바스크립트 -->
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script src="${pageContext.request.contextPath}/js/search-results.js"></script>
</body>
</html>
