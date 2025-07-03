<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="category" content="농가소개" />
    <title>농가소개 - Green Table</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/common/styles.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/menu-fix.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/farm-styles.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/farm-detail-styles.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />    <style>
      /* 인라인 스타일은 최소화하고 외부 CSS 파일로 관리 */
      .container {
        max-width: 1400px;
        margin: 0 auto;
        padding: 0 20px;
      }

      .farm-page {
        margin-top: 40px;
        margin-bottom: 50px;
      }

      .farm-card {
        height: 100%;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1);
        transition: transform 0.3s ease, box-shadow 0.3s ease;
        background-color: #ffffff;
      }

      .farm-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
      }

      .farm-card a {
        display: flex;
        flex-direction: column;
        height: 100%;
        text-decoration: none;
        color: inherit;
      }

      .farm-img img {
        width: 100%;
        height: 200px;
        object-fit: cover;
        transition: transform 0.3s ease;
      }

      .farm-card:hover .farm-img img {
        transform: scale(1.05);
      }

      .farm-card .farm-info {
        flex-grow: 1;
        display: flex;
        flex-direction: column;
        padding: 15px;
      }

      .farm-card .farm-name {
        font-size: 18px;
        margin-bottom: 8px;
        color: #333;
      }

      .farm-card .farm-desc {
        color: #666;
        margin-bottom: 10px;
        font-size: 14px;
      }

      .farm-card .farm-location {
        color: #888;
        font-size: 13px;
        margin-bottom: 10px;
        display: flex;
        align-items: center;
      }

      .farm-card .farm-location i {
        color: #00c471;
        margin-right: 5px;
      }

      .farm-card .farm-detail {
        margin-top: auto;
        padding-top: 10px;
        border-top: 1px solid #eee;
        font-size: 13px;
        color: #777;
      }

      .no-farms-message {
        grid-column: 1/-1;
        text-align: center;
        padding: 40px;
        background-color: #f9f9f9;
        border-radius: 10px;
        color: #666;
      }

      .no-farms-message p:first-child {
        font-size: 18px;
        margin-bottom: 10px;
      }

      /* 품질 관리 섹션 스타일 수정 */
      .quality-control-section {
        padding: 60px 0;
        background-color: #f8f9fa;
      }

      .quality-control-section .container {
        max-width: 1200px;
        margin: 0 auto;
      }

      .quality-control-section .section-title {
        text-align: center;
        margin-bottom: 30px;
      }

      .quality-steps {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
        gap: 30px;
        justify-items: center;
      }

      .quality-step {
        width: 100%;
        background-color: #fff;
        border-radius: 10px;
        padding: 25px 20px;
        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
        text-align: center;
        transition: transform 0.3s ease, box-shadow 0.3s ease;
      }

      .quality-step:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
      }
    </style>
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />

    <!-- 농가소개 메인 컨텐츠 -->
    <main class="farm-page">
      <!-- 농가 소개 페이지 배너 -->
      <div class="farm-banner">
        <img
          src="https://picsum.photos/seed/farm-banner/1200/300"
          alt="Green Table 협약 농가들"
        />
        <div class="farm-banner-overlay">
          <h1>함께하는 농가들</h1>
          <p>신선한 식재료의 시작, 그린테이블과 함께하는 농가를 소개합니다</p>
        </div>
      </div>

      <!-- 그린테이블 소개 -->
      <section class="farm-intro-section">
        <div class="container">
          <h2 class="section-title">그린테이블의 생산자 철학</h2>
          <div class="farm-intro-content">
            <div class="farm-intro-text">
              <p>
                그린테이블은 신선하고 안전한 식재료를 고객님의 식탁까지 전달하기
                위해 전국의 우수 농가들과 협약을 맺고 있습니다.
              </p>
              <p>
                건강한 먹거리의 시작은 좋은 생산자로부터 시작됩니다.
                그린테이블은 친환경 농법, 지속 가능한 농업에 투자하는 농가들과
                함께 성장하고 있습니다.
              </p>
              <p>
                엄격한 품질 관리와 정직한 거래를 통해 생산자와 소비자가 모두
                만족할 수 있는 식문화를 만들어갑니다.
              </p>
            </div>
            <div class="farm-intro-image">
              <img
                src="https://picsum.photos/seed/farm-intro/400/300"
                alt="그린테이블 농가 협약"
              />
            </div>
          </div>
        </div>
      </section>

      <!-- 농가 목록 -->
      <section class="farm-list-section">
        <div class="container">
          <h2 class="section-title">협약 농가 소개</h2>
          <p class="farm-list-desc">
            전국 각지의 우수한 농가들이 그린테이블과 함께합니다. 신선한 식재료로
            건강한 식탁을 책임지는 농가들을 만나보세요.
          </p>

          <div class="farm-filter">
            <button class="farm-filter-btn active" data-filter="all">
              전체
            </button>
            <button class="farm-filter-btn" data-filter="vege">채소</button>
            <button class="farm-filter-btn" data-filter="fruit">과일</button>
            <button class="farm-filter-btn" data-filter="dairy">
              축산/유제품
            </button>
            <button class="farm-filter-btn" data-filter="others">기타</button>
          </div>

          <div class="farm-grid">
            <c:choose>
              <c:when test="${not empty farmList}">
                <c:forEach var="farm" items="${farmList}">
                  <%-- 농가 카드 시작 - 데이터베이스의 정보로 표시 --%>
                  <div class="farm-card" data-category="${farm.category}">
                    <a
                      href="${pageContext.request.contextPath}/front?key=farm&methodName=detail&farmId=${farm.farmId}"
                      class="farm-card-link"
                    >
                      <div class="farm-img">
                        <img
                          src="${s3BaseUrl}/farms/${farm.farmImg}"
                          alt="${farm.name} 이미지"
                          onerror="this.onerror=null; this.src='https://picsum.photos/seed/farm${farm.farmId}/300/250';"
                        />
                      </div>
                      <div class="farm-info">
                        <h3 class="farm-name">${farm.name}</h3>
                        <p class="farm-desc">${farm.description}</p>
                        <p class="farm-location">
                          <i class="fas fa-map-marker-alt"></i> ${farm.address}
                        </p>
                        <div class="farm-detail">
                          <p>${farm.detailDescription}</p>
                          <c:if test="${not empty farm.mainProducts}">
                            <p>주요 작물: ${farm.mainProducts}</p>
                          </c:if>
                        </div>
                      </div>
                    </a>
                  </div>
                  <%-- 농가 카드 끝 --%>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <div class="no-farms-message">
                  <p>등록된 농가가 없습니다.</p>
                  <p>곧 다양한 협력 농가들을 소개해 드릴 예정입니다.</p>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </section>

      <!-- 품질 관리 섹션 수정 -->
      <section class="quality-control-section">
        <div class="container">
          <h2 class="section-title">그린테이블 품질 관리</h2>
          <p class="quality-intro">
            그린테이블은 철저한 품질 관리 시스템을 통해 최상의 제품만을 고객님께 전달합니다.
          </p>
          <div class="quality-steps">
            <div class="quality-step">
              <div class="step-icon">
                <i class="fas fa-seedling"></i>
              </div>
              <h3>우수 농가 선정</h3>
              <p>친환경 농법과 지속가능한 농업을 실천하는 농가를 엄선합니다.</p>
            </div>
            <div class="quality-step">
              <div class="step-icon">
                <i class="fas fa-microscope"></i>
              </div>
              <h3>철저한 품질 검사</h3>
              <p>농약 잔류 검사와 안전성 테스트를 정기적으로 진행합니다.</p>
            </div>
            <div class="quality-step">
              <div class="step-icon">
                <i class="fas fa-truck"></i>
              </div>
              <h3>신선도 유지 배송</h3>
              <p>콜드체인 시스템으로 신선한 상태 그대로 배송합니다.</p>
            </div>
            <div class="quality-step">
              <div class="step-icon">
                <i class="fas fa-sync-alt"></i>
              </div>
              <h3>지속적인 관리</h3>
              <p>정기적인 현장 방문과 품질 모니터링을 실시합니다.</p>
            </div>
          </div>
        </div>
      </section>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <!-- 자바스크립트 -->
    <script src="${pageContext.request.contextPath}/js/farm.js"></script>
    <script>
      // 농가 필터링 기능
      document.addEventListener("DOMContentLoaded", function () {
        const filterButtons = document.querySelectorAll(".farm-filter-btn");
        const farmCards = document.querySelectorAll(".farm-card");

        filterButtons.forEach((button) => {
          button.addEventListener("click", function () {
            // 버튼 활성화 상태 전환
            filterButtons.forEach((btn) => btn.classList.remove("active"));
            this.classList.add("active");

            const filter = this.getAttribute("data-filter");

            // 농가 카드 필터링
            farmCards.forEach((card) => {
              if (
                filter === "all" ||
                card.getAttribute("data-category") === filter
              ) {
                card.style.display = "";
                setTimeout(() => {
                  card.style.opacity = "1";
                  card.style.transform = "translateY(0)";
                }, 10);
              } else {
                card.style.opacity = "0";
                card.style.transform = "translateY(20px)";
                setTimeout(() => {
                  card.style.display = "none";
                }, 300);
              }
            });
          });
        });
      });
    </script>
  </body>
</html>
