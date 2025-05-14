<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="category-best" content="베스트 상품" />
    <meta name="category-regular" content="정기배송" />
    <meta name="category-lunchbox" content="도시락" />
    <meta name="category-salad" content="샐러드" />
    <meta name="category-farm" content="농가소개" />
    <title>Green Table - 건강한 식탁을 위한 선택</title>    <link rel="stylesheet" href="${path }/css/styles.css" />
    <link rel="stylesheet" href="${path}/css/menu-fix.css" />
    <link rel="stylesheet" href="${path}/css/product-common.css" />
    <link rel="stylesheet" href="${path}/css/category-sections.css" />
    <link rel="stylesheet" href="${path}/css/event.css" />
    <link rel="stylesheet" href="${path}/css/event-banner.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <!-- 헤더 컨테이너: header.html이 로드됩니다 -->
    <div id="header-container"><jsp:include page="/common/header.html"/></div>
    <!-- 메인 배너 섹션 -->
    <section class="main-banner">
      <div class="banner-container">
        <button class="arrow arrow-left">
          <i class="fas fa-chevron-left"></i>
        </button>
        <div class="banner-content">
          <h2 class="visually-hidden">이벤트 배너</h2>
          <!-- 배너 내용 (event-banner.js에서 동적으로 채워집니다) -->
          <div class="banner-slider">
            <!-- 이벤트 배너는 event-banner.js에 의해 동적으로 생성됩니다 -->
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
    <!-- 푸터 컨테이너: footer.html이 로드됩니다 -->
    <div id="footer-container"><jsp:include page="/common/footer.html"/></div>
    <!-- 자바스크립트 -->    <script src="${path }/js/include.js"></script>
    <script src="${path}/js/category-loader.js"></script>
    <script src="${path}/js/event-banner.js"></script>
    <script src="${path}/js/script.js"></script>
  </body>
</html>
