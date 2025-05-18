<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="category" content="베스트" />
    <title>베스트 상품 | Green Table</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/styles.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/product-common.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/best.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
    <script>
      // 컨텍스트 경로를 JavaScript 변수로 설정
      var contextPath = "${pageContext.request.contextPath}";
    </script>
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />

    <!-- 메인 콘텐츠 -->
    <main class="product-page">
      <!-- 1. 카테고리 배너 -->
      <div class="category-banner">
        <img
          src="${pageContext.request.contextPath}/products/images/best-banner.jpg"
          alt="베스트 상품"
        />
      </div>

      <!-- 2. 상품 목록 헤더 -->
      <div class="product-header">
        <!-- 수정 후 -->
        <div class="product-count">
          총
          <strong>${not empty productList ? productList.size() : 0}</strong>개
          상품
        </div>
        <div class="product-sort">
          <button data-sort="popular">인기순</button>
          <button data-sort="newest">최신순</button>
          <button data-sort="priceAsc">낮은가격순</button>
          <button data-sort="priceDesc">높은가격순</button>
          <button data-sort="rating">리뷰평점순</button>
        </div>
      </div>

      <!-- 상품 목록 -->
      <div class="product-grid">
        <c:forEach var="product" items="${productList}">
          <!-- 상품 아이템 -->
          <div
            class="product-item"
            data-id="${product.productId}"
            data-price="${product.price}"
            data-popularity="${product.popularity}"
            data-date="${product.createDate}"
            data-rating="${product.rating}"
          >
            <a
              href="${pageContext.request.contextPath}/front?key=product&methodName=detail&productId=${product.productId}"
              class="product-link"
            >
              <div class="product-image">
                <c:choose>
                  <c:when test="${not empty product.mainImageName}">
                    <img
                      src="${pageContext.request.contextPath}/assets/images/products/${product.mainImageName}"
                      alt="${product.name}"
                    />
                  </c:when>
                  <c:otherwise>
                    <img
                      src="${pageContext.request.contextPath}/assets/images/no-image.jpg"
                      alt="${product.name}"
                    />
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="product-info">
                <h3 class="product-name">${product.name}</h3>
                <p class="product-price">
                  <c:choose>
                    <c:when test="${product.discountRate > 0}">
                      <div class="original-price-line">
                        <span class="original-price">
                          <fmt:formatNumber
                            value="${product.price}"
                            pattern="#,###"
                          />원
                        </span>
                      </div>
                      <div class="discount-price-line">
                        <span class="discount-rate"
                          >${product.discountRate}%</span
                        >
                        <span class="final-price">
                          <fmt:formatNumber
                            value="${product.price * (100 - product.discountRate) / 100}"
                            pattern="#,###"
                          />원
                        </span>
                      </div>
                    </c:when>
                    <c:otherwise>
                      <fmt:formatNumber
                        value="${product.price}"
                        pattern="#,###"
                      />원
                    </c:otherwise>
                  </c:choose>
                </p>
                <p class="product-desc">${product.subName}</p>
                <div class="product-rating">
                  <span class="stars" data-rating="${product.rating}"></span>
                  <span class="count">(${product.reviewCount})</span>
                </div>
              </div>
            </a>
          </div>
        </c:forEach>

        <!-- 상품이 없을 경우 메시지 표시 -->
        <c:if test="${empty productList}">
          <div class="no-products-message">
            <p>현재 표시할 상품이 없습니다.</p>
          </div>
        </c:if>
      </div>

      <!-- 3. 페이지네이션 -->
      <div class="pagination">
        <div class="pagination-prev">
          <c:if test="${pageNo > 1}">
            <a
              href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=best&pageNo=${pageNo - 1}"
            >
              <button><i class="fas fa-chevron-left"></i></button>
            </a>
          </c:if>
          <c:if test="${pageNo <= 1}">
            <button disabled><i class="fas fa-chevron-left"></i></button>
          </c:if>
        </div>
        <ul class="pagination-list">
          <c:forEach begin="1" end="${totalPages}" var="i">
            <li class="pagination-item ${i == pageNo ? 'active' : ''}">
              <a
                href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=best&pageNo=${i}"
              >
                <button>${i}</button>
              </a>
            </li>
          </c:forEach>
        </ul>
        <div class="pagination-next">
          <c:if test="${pageNo < totalPages}">
            <a
              href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=best&pageNo=${pageNo + 1}"
            >
              <button><i class="fas fa-chevron-right"></i></button>
            </a>
          </c:if>
          <c:if test="${pageNo >= totalPages}">
            <button disabled><i class="fas fa-chevron-right"></i></button>
          </c:if>
        </div>
      </div>
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <!-- 자바스크립트 -->
    <script src="${pageContext.request.contextPath}/js/product-common.js"></script>
    <script src="${pageContext.request.contextPath}/js/best.js"></script>
  </body>
</html>
