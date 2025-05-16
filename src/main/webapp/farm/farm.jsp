<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="category" content="농가소개" />
    <title>농가소개 - Green Table</title>
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
      href="${pageContext.request.contextPath}/css/farm-styles.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
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
                  <%-- 농가 카드 시작 - 데이터베이스의 정보로 대체 --%>
                  <div class="farm-card" data-category="${farm.category}">
                    <div class="farm-img">
                      <img
                        src="${pageContext.request.contextPath}/assets/images/farms/${farm.farmImg}"
                        alt="${farm.name} 이미지"
                        onerror="this.src='https://picsum.photos/seed/farm${farm.farmId}/300/250'"
                      />
                    </div>
                    <div class="farm-info">
                      <h3 class="farm-name">${farm.name}</h3>
                      <p class="farm-desc">${farm.description}</p>
                      <p class="farm-location">
                        <i class="fas fa-map-marker-alt"></i>${farm.address}
                      </p>
                      <div class="farm-detail">
                        <p>${farm.detailDescription}</p>
                        <c:if test="${not empty farm.mainProducts}">
                          <p>주요 작물: ${farm.mainProducts}</p>
                        </c:if>
                      </div>
                    </div>
                  </div>
                  <%-- 농가 카드 끝 --%>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <%-- 데이터가 없을 경우 기본 예시 농가들 표시 --%>
                <!-- 농가 카드 1 -->
                <div class="farm-card" data-category="vege">
                  <div class="farm-img">
                    <img
                      src="https://picsum.photos/seed/farm1/300/250"
                      alt="그린 팜 농장 이미지"
                    />
                  </div>
                  <div class="farm-info">
                    <h3 class="farm-name">그린 팜</h3>
                    <p class="farm-desc">유기농 채소 전문 농장</p>
                    <p class="farm-location">
                      <i class="fas fa-map-marker-alt"></i>강원도 원주시
                    </p>
                    <div class="farm-detail">
                      <p>
                        2010년부터 친환경 농법으로 채소를 재배해온 그린 팜은
                        깨끗한 강원도의 자연환경 속에서 건강한 채소를
                        생산합니다.
                      </p>
                      <p>주요 작물: 상추, 시금치, 깻잎, 쌈채소</p>
                    </div>
                  </div>
                </div>

                <!-- 농가 카드 2 -->
                <div class="farm-card" data-category="fruit">
                  <div class="farm-img">
                    <img
                      src="https://picsum.photos/seed/farm2/300/250"
                      alt="자연 농원 농장 이미지"
                    />
                  </div>
                  <div class="farm-info">
                    <h3 class="farm-name">자연 농원</h3>
                    <p class="farm-desc">친환경 과일 농장</p>
                    <p class="farm-location">
                      <i class="fas fa-map-marker-alt"></i>경상북도 상주시
                    </p>
                    <div class="farm-detail">
                      <p>
                        3대째 이어온 과수원으로, 최소한의 농약만을 사용하여
                        안전하고 맛있는 과일을 생산합니다.
                      </p>
                      <p>주요 작물: 사과, 배, 복숭아, 자두</p>
                    </div>
                  </div>
                </div>

                <!-- 농가 카드 3 -->
                <div class="farm-card" data-category="dairy">
                  <div class="farm-img">
                    <img
                      src="https://picsum.photos/seed/farm3/300/250"
                      alt="평화 목장 이미지"
                    />
                  </div>
                  <div class="farm-info">
                    <h3 class="farm-name">평화 목장</h3>
                    <p class="farm-desc">무항생제 유기농 목장</p>
                    <p class="farm-location">
                      <i class="fas fa-map-marker-alt"></i>전라남도 순천시
                    </p>
                    <div class="farm-detail">
                      <p>
                        넓은 초원에서 자유롭게 풀을 뜯는 소들을 통해 건강한
                        우유와 유제품을 만듭니다.
                      </p>
                      <p>주요 제품: 우유, 요거트, 치즈</p>
                    </div>
                  </div>
                </div>

                <!-- 농가 카드 4 -->
                <div class="farm-card" data-category="vege">
                  <div class="farm-img">
                    <img
                      src="https://picsum.photos/seed/farm4/300/250"
                      alt="행복 농장 이미지"
                    />
                  </div>
                  <div class="farm-info">
                    <h3 class="farm-name">행복 농장</h3>
                    <p class="farm-desc">제철 채소 전문 농장</p>
                    <p class="farm-location">
                      <i class="fas fa-map-marker-alt"></i>충청남도 공주시
                    </p>
                    <div class="farm-detail">
                      <p>
                        20년 경력의 농부가 계절마다 가장 맛있는 제철 채소를
                        정성껏 재배합니다.
                      </p>
                      <p>주요 작물: 토마토, 오이, 가지, 파프리카</p>
                    </div>
                  </div>
                </div>

                <!-- 농가 카드 5 -->
                <div class="farm-card" data-category="others">
                  <div class="farm-img">
                    <img
                      src="https://picsum.photos/seed/farm5/300/250"
                      alt="들판 양봉원 이미지"
                    />
                  </div>
                  <div class="farm-info">
                    <h3 class="farm-name">들판 양봉원</h3>
                    <p class="farm-desc">친환경 꿀 전문 양봉장</p>
                    <p class="farm-location">
                      <i class="fas fa-map-marker-alt"></i>경상남도 하동군
                    </p>
                    <div class="farm-detail">
                      <p>
                        청정지역에서 자연 그대로의 벌꿀을 채취하여 건강하고
                        달콤한 꿀을 생산합니다.
                      </p>
                      <p>주요 제품: 아카시아꿀, 밤꿀, 잡화꿀, 프로폴리스</p>
                    </div>
                  </div>
                </div>

                <!-- 농가 카드 6 -->
                <div class="farm-card" data-category="fruit">
                  <div class="farm-img">
                    <img
                      src="https://picsum.photos/seed/farm6/300/250"
                      alt="햇살농원 이미지"
                    />
                  </div>
                  <div class="farm-info">
                    <h3 class="farm-name">햇살농원</h3>
                    <p class="farm-desc">유기농 딸기 전문 농장</p>
                    <p class="farm-location">
                      <i class="fas fa-map-marker-alt"></i>충청북도 청주시
                    </p>
                    <div class="farm-detail">
                      <p>
                        천연 퇴비만을 사용하여 건강하고 달콤한 딸기를 연중
                        재배합니다.
                      </p>
                      <p>주요 작물: 설향딸기, 장희딸기, 금실딸기</p>
                    </div>
                  </div>
                </div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </section>

      <!-- 품질 관리 섹션 -->
      <section class="quality-control-section">
        <div class="container">
          <h2 class="section-title">그린테이블 품질 관리</h2>
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
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <!-- 자바스크립트 -->
    <script src="${pageContext.request.contextPath}/js/farm.js"></script>
  </body>
</html>
