<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="category" content="농가상세" />
    <title>${farm.name} - Green Table 농가소개</title>
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
      href="${pageContext.request.contextPath}/css/farm-detail-styles.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />

    <!-- 농가상세 메인 컨텐츠 -->
    <main class="farm-page">
      <!-- 농가 상세 페이지 배너 -->
      <div class="farm-banner">
        <img
          src="${pageContext.request.contextPath}/assets/images/farms/${farm.farmImg}"
          alt="${farm.name}"
          onerror="this.src='https://picsum.photos/seed/farm${farm.farmId}/1200/300'"
        />
        <div class="farm-banner-overlay">
          <h1>${farm.name}</h1>
          <p>${farm.description}</p>
        </div>
      </div>

      <!-- 농가 상세 소개 -->
      <section class="farm-intro-section">
        <div class="container">
          <h2 class="section-title">농장 소개</h2>
          <div class="farm-intro-content">
            <div class="farm-intro-text">
              <!-- 존재하는 필드만 사용하도록 수정 -->
              <p>${farm.description}</p>
              <c:if test="${not empty farm.detailDescription}">
                <p>${farm.detailDescription}</p>
              </c:if>
              <c:if test="${not empty farm.mainProducts}">
                <p><strong>주요 작물:</strong> ${farm.mainProducts}</p>
              </c:if>
            </div>
            <div class="farm-intro-image">
              <img
                src="${pageContext.request.contextPath}/assets/images/farms/detail/${farm.farmImg}"
                alt="${farm.name} 상세 이미지"
                onerror="this.src='https://picsum.photos/seed/detail${farm.farmId}/400/300'"
              />
            </div>
          </div>
        </div>
      </section>

      <!-- 농가 생산 제품 목록 -->
      <c:if test="${not empty farmProducts}">
        <section class="farm-product-section">
          <div class="container">
            <h2 class="section-title">${farm.name}의 생산 제품</h2>
            <p class="farm-list-desc">
              ${farm.name}에서 생산하는 신선한 제품을 만나보세요.
            </p>

            <div class="product-grid">
              <c:forEach var="product" items="${farmProducts}">
                <div class="product-card">
                  <a
                    href="${pageContext.request.contextPath}/front?key=product&methodName=detail&productId=${product.productId}"
                  >
                    <div class="product-image">
                      <img
                        src="${pageContext.request.contextPath}/assets/images/products/${product.mainImageName}"
                        alt="${product.name}"
                        onerror="this.src='https://picsum.photos/seed/product${product.productId}/300/180'"
                      />
                    </div>
                    <div class="product-info">
                      <h3>${product.name}</h3>
                      <p class="product-subname">${product.subName}</p>
                      <div class="product-price">
                        <c:if test="${product.discountRate > 0}">
                          <span class="original-price"
                            ><fmt:formatNumber
                              value="${product.price}"
                              pattern="#,###"
                            />원</span
                          >
                          <span class="discount-rate"
                            >${product.discountRate}%</span
                          >
                        </c:if>
                        <span class="final-price"
                          ><fmt:formatNumber
                            value="${product.discountedPrice}"
                            pattern="#,###"
                          />원</span
                        >
                      </div>
                    </div>
                  </a>
                </div>
              </c:forEach>
            </div>
          </div>
        </section>
      </c:if>

      <!-- 농장 위치 정보 -->
      <section class="farm-location-section">
        <div class="container">
          <h2 class="section-title">농장 위치</h2>
          <p class="farm-location-address">
            <i class="fas fa-map-marker-alt"></i> ${farm.address}
          </p>
          <div
            class="farm-map"
            id="farmMap"
            data-lat="${farm.latitude}"
            data-lng="${farm.longitude}"
            data-name="${farm.name}"
          >
            <!-- 지도가 표시될 영역 -->
          </div>
        </div>
      </section>
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <!-- 카카오맵 API 스크립트 -->
    <script
      type="text/javascript"
      src="//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_MAP_KEY"
    ></script>

    <!-- 자바스크립트 -->
    <script>
      document.addEventListener("DOMContentLoaded", function () {
        // 농장 위치 지도 표시
        const mapContainer = document.getElementById("farmMap");
        if (mapContainer) {
          const lat =
            parseFloat(mapContainer.getAttribute("data-lat")) || 33.450701;
          const lng =
            parseFloat(mapContainer.getAttribute("data-lng")) || 126.570667;
          const farmName = mapContainer.getAttribute("data-name") || "농장";

          try {
            const mapOption = {
              center: new kakao.maps.LatLng(lat, lng),
              level: 3,
            };
            const map = new kakao.maps.Map(mapContainer, mapOption);

            // 마커 표시
            const markerPosition = new kakao.maps.LatLng(lat, lng);
            const marker = new kakao.maps.Marker({
              position: markerPosition,
            });
            marker.setMap(map);

            // 인포윈도우 표시
            const infowindow = new kakao.maps.InfoWindow({
              content: `<div style="padding:5px;font-size:12px;">${farmName}</div>`,
            });
            infowindow.open(map, marker);
          } catch (e) {
            console.error("지도 로딩 실패:", e);
            mapContainer.innerHTML =
              '<p class="map-error">지도를 불러오는데 실패했습니다.</p>';
          }
        }
      });
    </script>
  </body>
</html>
