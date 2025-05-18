<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<!-- 컨텍스트 경로: ${pageContext.request.contextPath} -->
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <script>
      // 컨텍스트 경로를 JavaScript 변수로 설정
      var contextPath = "${pageContext.request.contextPath}";
      // window 객체에도 할당하여 전역 접근 가능하게 함
      window.contextPath = contextPath;
    </script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta
      http-equiv="Cache-Control"
      content="no-store, no-cache, must-revalidate"
    />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="category-best" content="베스트 상품" />
    <meta name="category-regular" content="정기배송" />
    <meta name="category-lunchbox" content="도시락" />
    <meta name="category-salad" content="샐러드" />
    <meta name="category-farm" content="농가소개" />
    <title>
      <c:choose> <c:when test="${not empty category}">그린테이블 -
      ${category}</c:when> <c:otherwise>그린테이블 - 건강한 식탁을 위한
      선택</c:otherwise> </c:choose>
    </title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/styles.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/menu-fix.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/product-common.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/category-sections.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/event.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/event-banner.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/farm-styles.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
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
            <!-- 상품 카테고리 내용 -->
          </section>

          <section class="product-grid">
            <!-- 상품 그리드 내용 -->
          </section>

          <section class="pagination">
            <!-- 페이지네이션 내용 -->
          </section>
        </main>
      </c:when>

      <%-- 메인 페이지인 경우 --%>
      <c:otherwise>
        <!-- 메인 배너 섹션 -->
        <section class="main-banner">
          <!-- 메인 배너 내용 -->
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
        <script src="${pageContext.request.contextPath}/js/script.js"></script>
      </c:when>
      <c:otherwise>
        <!-- 메인 페이지용 스크립트 -->
        <script
          src="${pageContext.request.contextPath}/js/category-loader.js"
        ></script>
        <script
          src="${pageContext.request.contextPath}/js/event-banner.js"
        ></script>
        <script src="${pageContext.request.contextPath}/js/script.js"></script>
      </c:otherwise>
    </c:choose>
  </body>
</html>
