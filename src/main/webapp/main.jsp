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

<%-- contextPath 변수 설정 (이미지 해결방법 적용) --%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <script>
      // 컨텍스트 경로를 JavaScript 변수로 설정
      var contextPath = "${path}";
      // window 객체에도 할당하여 전역 접근 가능하게 함
      window.contextPath = contextPath;
      
      // 이미지 해결방법에서 제안한 함수 추가
      function totalBtn() {
        window.location.href = "${path}/commute/empCommute?empId=${empId}";
      }
    </script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-store, no-cache, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="theme-color" content="#00c471" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <meta name="category-best" content="베스트 상품" />
    <meta name="category-regular" content="정기배송" />
    <meta name="category-lunchbox" content="도시락" />
    <meta name="category-salad" content="샐러드" />
    <meta name="category-farm" content="농가소개" />
    <title>
      <c:choose>
        <c:when test="${not empty category}">그린테이블 - ${category}</c:when>
        <c:otherwise>그린테이블 - 건강한 식탁을 위한 선택</c:otherwise>
      </c:choose>
    </title>
    
    <!-- CSS 파일들 - path 변수 사용 -->
    <link rel="stylesheet" href="${path}/css/styles.css" />
    <link rel="stylesheet" href="${path}/css/common/styles.css" />
    <link rel="stylesheet" href="${path}/css/menu-fix.css" />
    <link rel="stylesheet" href="${path}/css/product-common.css" />
    <link rel="stylesheet" href="${path}/css/category-sections.css" />
    <link rel="stylesheet" href="${path}/css/event.css" />
    <link rel="stylesheet" href="${path}/css/event-banner.css" />
    <link rel="stylesheet" href="${path}/css/farm-styles.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="common/header.jsp" />

    <c:choose>
        <%-- 상품 목록 페이지인 경우 (productList 속성이 존재하는 경우) --%>
        <c:when test="${not empty productList}">
            <!-- 상품 목록 컨테이너 -->
            <main class="product-list-container">
                <section class="product-category">
                    <h2>
                        <c:choose>
                            <c:when test="${category eq '도시락'}">도시락</c:when>
                            <c:when test="${category eq '샐러드'}">샐러드</c:when>
                            <c:otherwise>전체 상품</c:otherwise>
                        </c:choose>
                    </h2>
                    
                    <div class="category-links">                        
                        <a href="${path}/front?key=product&methodName=list" class="${empty category ? 'active' : ''}">전체</a>
                        <a href="${path}/front?key=product&methodName=category&category=lunchbox" class="${category eq 'lunchbox' ? 'active' : ''}">도시락</a>
                        <a href="${path}/front?key=product&methodName=category&category=salad" class="${category eq 'salad' ? 'active' : ''}">샐러드</a>
                        <a href="${path}/front?key=product&methodName=category&category=best" class="${category eq 'best' ? 'active' : ''}">베스트</a>
                        <a href="${path}/front?key=product&methodName=category&category=regular" class="${category eq 'regular' ? 'active' : ''}">정기배송</a>
                        <a href="${path}/front?key=farm&methodName=list" class="${category eq 'farm' ? 'active' : ''}">농가소개</a>
                    </div>
                </section>
                
                <section class="product-grid">
                    <c:forEach items="${productList}" var="product">
                        <div class="product-card">
                            <a href="${path}/front?key=product&methodName=detail&productId=${product.productId}">
                                <div class="product-image">
                                    <!-- S3 전용 이미지 URL -->
                                    <img src="${s3BaseUrl}/${product.mainImageName}" alt="${product.name}"
                                         onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">
                                </div>
                                <div class="product-info">
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
                </section>
                
                <section class="pagination">
                    <c:if test="${totalPages > 1}">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i eq pageNo}">
                                    <span class="current">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${empty category}">
                                            <a href="${path}/front?key=product&methodName=list&pageNo=${i}">${i}</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${path}/front?key=product&methodName=category&category=${category}&pageNo=${i}">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:if>
                </section>
            </main>
        </c:when>
        
        <%-- 메인 페이지인 경우 --%>
        <c:otherwise>
            <!-- 메인 배너 섹션 -->
          <section class="main-banner">
            <div class="banner-container">
              <button class="arrow arrow-left">
                <i class="fas fa-chevron-left"></i>
              </button>
              <div class="banner-content">
                <h2 class="visually-hidden">이벤트 배너</h2>
                <div class="banner-slider">
                  <!-- 이벤트 배너는 direct-event-loader.js에 의해 동적으로 생성됩니다 -->
                </div>
              </div>
              <button class="arrow arrow-right">
                <i class="fas fa-chevron-right"></i>
              </button>
            </div>
            <div class="banner-pagination">
              <div class="page-numbers">
                <span class="current-page">1</span> /
                <span class="total-pages">4</span>
              </div>
            </div>
          </section>
            
            <!-- 메인 콘텐츠 컨테이너 -->
            <main class="main-content">
              <!-- 각 카테고리 컨테이너 - JS로 콘텐츠 로드 -->
              <div id="best-container"></div>
              <div id="regular-container"></div>
              <div id="lunchbox-container"></div>
              <div id="salad-container"></div>
              <div id="farm-container"></div>
            </main>
        </c:otherwise>

    </c:choose>

    <!-- 푸터 인클루드 -->
    <jsp:include page="common/footer.jsp" />

    <!-- 자바스크립트 -->
    <c:choose>
      <c:when test="${not empty productList}">
        <!-- 상품 목록 페이지용 스크립트 -->
        <script src="${path}/js/script.js"></script>
      </c:when>
      <c:otherwise>
        <!-- 메인 페이지용 스크립트 -->
        <script src="${path}/js/category-loader.js"></script>
        <script src="${path}/event/event.js"></script>
        <script src="${path}/js/direct-event-loader.js"></script>
        <script src="${path}/js/script.js"></script>
      </c:otherwise>
    </c:choose>
  </body>
</html>