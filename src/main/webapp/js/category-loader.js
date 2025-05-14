// 메인 페이지에서 각 카테고리별 콘텐츠를 로드하는 기능
document.addEventListener("DOMContentLoaded", function () {
  // 카테고리 섹션 로드 함수
  function loadCategorySection(categoryName, containerId) {
    const container = document.getElementById(containerId);
    if (!container)
      return Promise.reject(`${containerId} 요소를 찾을 수 없습니다.`);

    // 로딩 상태 표시
    container.innerHTML = `<div class="loading-indicator">
      <div class="spinner"></div>
      <p>${categoryName} 콘텐츠를 불러오는 중...</p>
    </div>`;
    return fetch(`../html/${categoryName}.html`)
      .then((response) => {
        if (!response.ok) {
          throw new Error(
            `${categoryName} 페이지를 불러올 수 없습니다. (${response.status})`
          );
        }
        return response.text();
      })
      .then((html) => {
        // HTML 문서 파싱
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, "text/html");

        // 타이틀 가져오기
        let title = categoryName;
        const metaCategory = doc.querySelector("meta[name='category']");
        if (metaCategory) {
          title = metaCategory.getAttribute("content") || categoryName;
        }

        // 카테고리별 타이틀 한글화
        const categoryTitles = {
          best: "베스트",
          regular: "정기배송",
          lunchbox: "도시락",
          salad: "샐러드",
        };

        title = categoryTitles[categoryName] || title;

        // 배너 이미지 가져오기
        const banner = doc.querySelector(".category-banner img");
        const bannerSrc =
          banner?.getAttribute("src") ||
          `https://picsum.photos/seed/${categoryName}-banner/1200/200`;
        const bannerAlt = banner?.getAttribute("alt") || `${title} 상품`;

        // 상품 아이템 가져오기 (최대 4개)
        const productItems = doc.querySelectorAll(".product-item");
        const productItemsArray = Array.from(productItems).slice(0, 4);

        // 상품이 없을 경우 대체 메시지
        let productsHtml = "";
        if (productItemsArray.length > 0) {
          productsHtml = productItemsArray
            .map((item) => item.outerHTML)
            .join("");
        } else {
          productsHtml = `<div class="no-products-message">상품 준비 중입니다.</div>`;
        } // HTML 생성 (배너 제외)
        let sectionHTML = `
          <section class="category-section-preview" id="${categoryName}-preview">
            <h2 class="section-title">${title}</h2>
            <div class="product-grid">
              ${productsHtml}
            </div>
            <div class="view-more-container">
              <a href="${categoryName}.html" class="view-more-btn">더 보기</a>
            </div>
          </section>
        `;

        // 컨테이너에 HTML 삽입
        container.innerHTML = sectionHTML;

        // 상품 이벤트 리스너 추가
        if (typeof initProductClicks === "function") {
          initProductClicks();
        }

        return categoryName; // 성공적으로 로드되었음을 알림
      })
      .catch((error) => {
        console.error(`${categoryName} 섹션 로딩 실패:`, error);
        container.innerHTML = `<div class="error-message">${categoryName} 콘텐츠를 로드하는 중 오류가 발생했습니다.</div>`;
      });
  } // 농가 소개 섹션 로드 함수
  function loadFarmSection(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;

    // 농가 섹션 HTML 추가
    container.innerHTML = `
      <section class="farm-intro">
        <h2 class="section-title">농가 소개</h2>        <div class="farm-intro-header">
          <div class="farm-count">현재 협약 중인 농가 <strong>10+</strong> 곳</div>
          <a href="farm.html" class="view-more-btn">농가 더보기</a>
        </div>
        <div class="farm-slider">
          <button class="arrow arrow-left">
            <i class="fas fa-chevron-left"></i>
          </button>
          <div class="farm-container" style="display: flex; gap: 20px; transition: transform 0.3s ease; overflow: hidden;">            <div class="farm-card">
              <div class="farm-img">
                <img src="https://picsum.photos/seed/farm1/300/250" alt="농가 사진" />
              </div>              <div class="farm-info">
                <h3 class="farm-name">그린 팜</h3>
                <p class="farm-desc">유기농 채소 전문 농장</p>
                <p class="farm-location"><i class="fas fa-map-marker-alt"></i>강원도 원주시</p>
              </div>
            </div>
            <div class="farm-card">
              <div class="farm-img">
                <img src="https://picsum.photos/seed/farm2/300/250" alt="농가 사진" />
              </div>
              <div class="farm-info">
                <h3 class="farm-name">자연 농원</h3>
                <p class="farm-desc">친환경 과일 농장</p>
                <p class="farm-location"><i class="fas fa-map-marker-alt"></i>경상북도 상주시</p>
              </div>
            </div>
            <div class="farm-card">
              <div class="farm-img">
                <img src="https://picsum.photos/seed/farm3/300/250" alt="농가 사진" />
              </div>
              <div class="farm-info">
                <h3 class="farm-name">평화 목장</h3>
                <p class="farm-desc">무항생제 유기농 목장</p>
                <p class="farm-location"><i class="fas fa-map-marker-alt"></i>전라남도 순천시</p>
              </div>
            </div>
            <div class="farm-card">
              <div class="farm-img">
                <img src="https://picsum.photos/seed/farm4/300/250" alt="농가 사진" />
              </div>
              <div class="farm-info">
                <h3 class="farm-name">행복 농장</h3>
                <p class="farm-desc">제철 채소 전문 농장</p>
                <p class="farm-location"><i class="fas fa-map-marker-alt"></i>충청남도 공주시</p>
              </div>
            </div>
          </div>
          <button class="arrow arrow-right">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </section>
    `; // 농가 슬라이더 초기화 - DOM이 완전히 렌더링된 후 실행
    setTimeout(() => {
      if (typeof initFarmSlider === "function") {
        try {
          initFarmSlider();
        } catch (error) {
          console.error("농가 슬라이더 초기화 오류:", error);
        }
      }
    }, 300);

    return "farm"; // 성공적으로 로드되었음을 알림
  }

  // 콘솔에 로딩 상태 메시지 표시
  function logLoadingStatus(category, status) {
    console.log(`[카테고리 로더] ${category} ${status}`);
  }

  // 각 카테고리 섹션 순차적으로 로드
  const categories = [
    { name: "best", id: "best-container" },
    { name: "regular", id: "regular-container" },
    { name: "lunchbox", id: "lunchbox-container" },
    { name: "salad", id: "salad-container" },
  ];

  // 카테고리 로딩 순서대로 처리
  categories
    .reduce((promise, category) => {
      return promise
        .then(() => {
          logLoadingStatus(category.name, "로딩 시작");
          return loadCategorySection(category.name, category.id);
        })
        .then(() => {
          logLoadingStatus(category.name, "로딩 완료");
        })
        .catch((error) => {
          logLoadingStatus(category.name, `로딩 실패: ${error}`);
        });
    }, Promise.resolve())
    .then(() => {
      // 모든 카테고리 로딩 후 농가 섹션 로드
      logLoadingStatus("farm", "로딩 시작");
      return loadFarmSection("farm-container");
    })
    .then(() => {
      logLoadingStatus("farm", "로딩 완료");
      console.log("[카테고리 로더] 모든 섹션 로딩 완료");
    })
    .catch((error) => {
      logLoadingStatus("farm", `로딩 실패: ${error}`);
    });
});
