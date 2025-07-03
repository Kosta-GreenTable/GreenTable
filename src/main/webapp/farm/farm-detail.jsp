<!-- filepath: c:\Users\user\git\GreenTable\src\main\webapp\farm\farm-detail.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ page import="site.greentable.util.ImageUtil" %> <%@ page import="java.lang.System" %>
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
    <meta name="category" content="농가상세" />
    <title>${farm.name} - Green Table 농가소개</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/styles.css"
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

    <style>
      /* 지도 관련 스타일 */
      .farm-map {
        width: 100%;
        height: 400px;
        border: 1px solid #ddd;
        border-radius: 8px;
        background-color: #f8f9fa;
        position: relative;
        overflow: hidden;
      }

      .map-loading {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
        flex-direction: column;
        color: #666;
      }

      .map-error {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
        background-color: #f8f9fa;
        border-radius: 8px;
      }

      .map-links {
        margin-top: 15px;
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
      }

      .map-btn {
        display: inline-flex;
        align-items: center;
        gap: 5px;
        padding: 10px 20px;
        text-decoration: none;
        border-radius: 5px;
        font-size: 14px;
        transition: all 0.2s ease;
      }

      .map-btn:hover {
        transform: translateY(-1px);
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
      }

      .kakao-map-btn {
        background: #ffeb00;
        color: #000;
      }

      .directions-btn {
        background: #00c471;
        color: white;
      }

      .google-map-btn {
        background: #4285f4;
        color: white;
      }

      .copy-btn {
        background: #6c757d;
        color: white;
      }
    </style>
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />

    <!-- 농가상세 메인 컨텐츠 -->
    <main class="farm-page">
      <!-- 농가 상세 페이지 배너 -->
      <div class="farm-banner">
        <img
          src="${s3BaseUrl}/farms/${farm.farmImg}"
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
                src="${s3BaseUrl}/farms/${farm.farmImg}"
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
                      <!-- S3 전용 이미지 URL -->
                      <img
                        src="${s3BaseUrl}/${product.mainImageName}"
                        alt="${product.name}"
                        onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';"
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

          <!-- 농장 위치 정보 표시 -->
          <c:if test="${not empty farm.latitude and not empty farm.longitude}">
            <div style="margin-bottom: 10px; color: #666; font-size: 14px">
              <i class="fas fa-crosshairs"></i>
              위도: ${farm.latitude}, 경도: ${farm.longitude}
            </div>
          </c:if>

          <!-- 지도 영역 -->
          <div
            class="farm-map"
            id="farmMap"
            data-lat="${not empty farm.latitude ? farm.latitude : '35.1595'}"
            data-lng="${not empty farm.longitude ? farm.longitude : '129.1600'}"
            data-name="${farm.name}"
            data-address="${farm.address}"
          >
            <!-- 로딩 화면 -->
            <div class="map-loading" id="mapLoading">
              <i
                class="fas fa-map-marked-alt"
                style="font-size: 32px; margin-bottom: 15px; color: #00c471"
              ></i>
              <p style="margin: 0; font-size: 16px">
                지도를 불러오는 중입니다...
              </p>
              <div style="margin-top: 10px">
                <div
                  style="
                    width: 40px;
                    height: 4px;
                    background: #e9ecef;
                    border-radius: 2px;
                    overflow: hidden;
                  "
                >
                  <div
                    style="
                      width: 100%;
                      height: 100%;
                      background: #00c471;
                      animation: loading 1.5s infinite;
                    "
                  ></div>
                </div>
              </div>
            </div>
          </div>

          <!-- 지도 링크 버튼들 -->
          <div class="map-links">
            <a
              href="javascript:void(0);"
              onclick="openKakaoMap()"
              class="map-btn kakao-map-btn"
            >
              <i class="fas fa-external-link-alt"></i> 카카오맵에서 보기
            </a>
            <a
              href="javascript:void(0);"
              onclick="openDirections()"
              class="map-btn directions-btn"
            >
              <i class="fas fa-route"></i> 길찾기
            </a>
            <a
              href="javascript:void(0);"
              onclick="openGoogleMap()"
              class="map-btn google-map-btn"
            >
              <i class="fab fa-google"></i> 구글맵에서 보기
            </a>
            <a
              href="javascript:void(0);"
              onclick="copyAddress()"
              class="map-btn copy-btn"
            >
              <i class="fas fa-copy"></i> 주소 복사
            </a>
          </div>
        </div>
      </section>
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <!-- 로딩 애니메이션 CSS -->
    <style>
      @keyframes loading {
        0% {
          transform: translateX(-100%);
        }
        100% {
          transform: translateX(100%);
        }
      }
    </style>

    <!-- 카카오맵 API 스크립트 -->
    <script
      type="text/javascript"
      src="//dapi.kakao.com/v2/maps/sdk.js?appkey=97d32c8286b9ab7d32830cdc50d43210&libraries=services"
    ></script>

    <!-- 자바스크립트 -->
    <script>
      // 전역 변수 선언
      let map;
      let marker;
      let infowindow;
      let currentLat;
      let currentLng;
      let currentFarmName;
      let currentAddress;
      let mapInitialized = false;

      // 페이지 로드 시 데이터 초기화
      document.addEventListener("DOMContentLoaded", function () {
        console.log("DOM 로드 완료");
        initializeFarmData();

        // 카카오맵 API 로드 확인 후 지도 초기화
        if (typeof kakao !== "undefined" && kakao.maps) {
          console.log("카카오맵 API가 이미 로드되어 있습니다.");
          initializeMap();
        } else {
          console.log("카카오맵 API 로드 대기 중...");
          setTimeout(checkKakaoMapAPI, 500);
        }
      });

      // 농장 데이터 초기화
      function initializeFarmData() {
        const mapContainer = document.getElementById("farmMap");
        if (mapContainer) {
          currentLat =
            parseFloat(mapContainer.getAttribute("data-lat")) || 35.1595;
          currentLng =
            parseFloat(mapContainer.getAttribute("data-lng")) || 129.16;
          currentFarmName = mapContainer.getAttribute("data-name") || "농장";
          currentAddress = mapContainer.getAttribute("data-address") || "";

          console.log(`농장 정보 초기화: ${currentFarmName}`);
          console.log(`위치: ${currentLat}, ${currentLng}`);
          console.log(`주소: ${currentAddress}`);
        }
      }

      // 카카오맵 API 로드 확인
      function checkKakaoMapAPI(attempts = 0) {
        if (typeof kakao !== "undefined" && kakao.maps) {
          console.log("카카오맵 API 로드 완료");
          initializeMap();
        } else if (attempts < 20) {
          console.log(`카카오맵 API 로드 대기 중... (${attempts + 1}/20)`);
          setTimeout(() => checkKakaoMapAPI(attempts + 1), 500);
        } else {
          console.error("카카오맵 API 로드 실패");
          showMapError("카카오맵 API를 불러올 수 없습니다.");
        }
      }

      // 지도 초기화
      function initializeMap() {
        if (mapInitialized) return;

        console.log("지도 초기화 시작");
        const mapContainer = document.getElementById("farmMap");

        if (!mapContainer) {
          console.error("지도 컨테이너를 찾을 수 없습니다.");
          return;
        }

        try {
          // 지도 옵션 설정
          const mapOption = {
            center: new kakao.maps.LatLng(currentLat, currentLng),
            level: 3,
          };

          // 지도 생성
          map = new kakao.maps.Map(mapContainer, mapOption);
          console.log("지도 생성 완료");

          // 마커 생성
          const markerPosition = new kakao.maps.LatLng(currentLat, currentLng);
          marker = new kakao.maps.Marker({
            position: markerPosition,
            title: currentFarmName,
          });

          // 마커를 지도에 표시
          marker.setMap(map);
          console.log("마커 생성 완료");

          // 인포윈도우 생성
          const infoContent = `
            <div style="padding:15px;font-size:14px;font-family:'Malgun Gothic',sans-serif;min-width:200px;">
              <strong style="color:#00c471;font-size:16px;display:block;margin-bottom:5px;">${currentFarmName}</strong>
              <span style="color:#666;font-size:12px;line-height:1.4;">${currentAddress}</span>
            </div>
          `;

          infowindow = new kakao.maps.InfoWindow({
            content: infoContent,
          });

          // 마커 클릭 이벤트
          kakao.maps.event.addListener(marker, "click", function () {
            if (infowindow.getMap()) {
              infowindow.close();
            } else {
              infowindow.open(map, marker);
            }
          });

          // 마커 호버 이벤트
          kakao.maps.event.addListener(marker, "mouseover", function () {
            infowindow.open(map, marker);
          });

          // 지도 컨트롤 추가
          addMapControls();

          // 주소로 좌표 검색 (기본값인 경우)
          if (isDefaultLocation() && currentAddress) {
            searchAddressByGeocoder(currentAddress);
          }

          mapInitialized = true;
          console.log("지도 초기화 완료");
        } catch (error) {
          console.error("지도 초기화 중 오류:", error);
          showMapError("지도를 생성하는데 실패했습니다.");
        }
      }

      // 지도 컨트롤 추가
      function addMapControls() {
        try {
          // 지도타입 컨트롤
          const mapTypeControl = new kakao.maps.MapTypeControl();
          map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

          // 줌 컨트롤
          const zoomControl = new kakao.maps.ZoomControl();
          map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

          console.log("지도 컨트롤 추가 완료");
        } catch (error) {
          console.warn("지도 컨트롤 추가 실패:", error);
        }
      }

      // 기본 위치인지 확인
      function isDefaultLocation() {
        return currentLat === 35.1595 && currentLng === 129.16;
      }

      // 주소로 좌표 검색
      function searchAddressByGeocoder(address) {
        if (!address || typeof kakao.maps.services === "undefined") {
          console.warn("주소 검색 서비스를 사용할 수 없습니다.");
          return;
        }

        console.log(`주소 검색 시작: ${address}`);

        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.addressSearch(address, function (result, status) {
          if (status === kakao.maps.services.Status.OK) {
            const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

            // 지도 중심과 마커 위치 변경
            map.setCenter(coords);
            marker.setPosition(coords);

            // 전역 변수 업데이트
            currentLat = parseFloat(result[0].y);
            currentLng = parseFloat(result[0].x);

            console.log(
              `주소 검색 성공: ${address} -> ${currentLat}, ${currentLng}`
            );
          } else {
            console.warn(`주소 검색 실패: ${address}`);
          }
        });
      }

      // 지도 에러 표시
      function showMapError(message) {
        const mapContainer = document.getElementById("farmMap");
        if (mapContainer) {
          mapContainer.innerHTML = `
            <div class="map-error">
              <div style="text-align: center; color: #666; padding: 40px;">
                <i class="fas fa-exclamation-triangle" style="font-size: 32px; margin-bottom: 15px; color: #ffc107;"></i>
                <p style="margin: 0 0 15px 0; font-size: 16px;">${message}</p>
                <button onclick="retryMapInit()" style="padding:10px 20px;background:#00c471;color:white;border:none;border-radius:5px;cursor:pointer;">
                  다시 시도
                </button>
              </div>
            </div>
          `;
        }
      }

      // 지도 초기화 재시도
      function retryMapInit() {
        console.log("지도 초기화 재시도");
        mapInitialized = false;

        const mapContainer = document.getElementById("farmMap");
        mapContainer.innerHTML = `
          <div class="map-loading">
            <i class="fas fa-map-marked-alt" style="font-size: 32px; margin-bottom: 15px; color: #00c471;"></i>
            <p style="margin: 0; font-size: 16px;">지도를 불러오는 중입니다...</p>
          </div>
        `;

        setTimeout(initializeMap, 1000);
      }

      // 카카오맵에서 보기
      function openKakaoMap() {
        try {
          let url;
          if (currentAddress) {
            const searchQuery = encodeURIComponent(currentAddress);
            url = `https://map.kakao.com/?q=${searchQuery}`;
          } else if (currentLat && currentLng) {
            const encodedName = encodeURIComponent(currentFarmName);
            url = `https://map.kakao.com/link/map/${encodedName},${currentLat},${currentLng}`;
          } else {
            const searchQuery = encodeURIComponent(currentFarmName);
            url = `https://map.kakao.com/?q=${searchQuery}`;
          }
          window.open(url, "_blank");
        } catch (error) {
          console.error("카카오맵 열기 오류:", error);
          alert("카카오맵을 열 수 없습니다.");
        }
      }

      // 길찾기
      function openDirections() {
        try {
          let url;
          if (currentLat && currentLng) {
            const encodedName = encodeURIComponent(currentFarmName);
            url = `https://map.kakao.com/link/to/${encodedName},${currentLat},${currentLng}`;
          } else {
            const searchQuery = encodeURIComponent(
              currentAddress || currentFarmName
            );
            url = `https://map.kakao.com/?q=${searchQuery}`;
          }
          window.open(url, "_blank");
        } catch (error) {
          console.error("길찾기 열기 오류:", error);
          alert("길찾기를 열 수 없습니다.");
        }
      }

      // 구글맵에서 보기
      function openGoogleMap() {
        try {
          let url;
          if (currentAddress) {
            const searchQuery = encodeURIComponent(currentAddress);
            url = `https://www.google.com/maps/search/${searchQuery}`;
          } else if (currentLat && currentLng) {
            url = `https://www.google.com/maps/@${currentLat},${currentLng},15z`;
          } else {
            const searchQuery = encodeURIComponent(currentFarmName);
            url = `https://www.google.com/maps/search/${searchQuery}`;
          }
          window.open(url, "_blank");
        } catch (error) {
          console.error("구글맵 열기 오류:", error);
          alert("구글맵을 열 수 없습니다.");
        }
      }

      // 주소 복사
      function copyAddress() {
        const textToCopy = currentAddress || currentFarmName;

        if (!textToCopy) {
          alert("복사할 주소가 없습니다.");
          return;
        }

        // 클립보드 API 사용
        if (navigator.clipboard && window.isSecureContext) {
          navigator.clipboard
            .writeText(textToCopy)
            .then(function () {
              alert(`주소가 복사되었습니다:\n${textToCopy}`);
            })
            .catch(function (err) {
              console.error("클립보드 복사 실패:", err);
              fallbackCopyText(textToCopy);
            });
        } else {
          fallbackCopyText(textToCopy);
        }
      }

      // 클립보드 복사 대체 방법
      function fallbackCopyText(text) {
        const textArea = document.createElement("textarea");
        textArea.value = text;
        textArea.style.position = "fixed";
        textArea.style.top = "-1000px";
        textArea.style.left = "-1000px";
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();

        try {
          const successful = document.execCommand("copy");
          if (successful) {
            alert(`주소가 복사되었습니다:\n${text}`);
          } else {
            prompt("아래 주소를 복사해주세요:", text);
          }
        } catch (err) {
          console.error("복사 실패:", err);
          prompt("아래 주소를 복사해주세요:", text);
        }

        document.body.removeChild(textArea);
      }
    </script>
  </body>
</html>
