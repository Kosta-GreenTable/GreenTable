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
    
    // 컨텍스트 경로를 사용하여 URL 구성
    const contextPath = window.contextPath || "";
    return fetch(`${contextPath}/front?key=product&methodName=category&category=${categoryName}&pageNo=1`)
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
        } 
        
        // HTML 생성 (배너 제외)
        let sectionHTML = `
          <section class="category-section-preview" id="${categoryName}-preview">
            <h2 class="section-title">${title}</h2>
            <div class="product-grid">
              ${productsHtml}
            </div>
            <div class="view-more-container">
              <a href="${contextPath}/front?key=product&methodName=category&category=${categoryName}" class="view-more-btn">더 보기</a>
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
  }  
  
  // 이미지 로드 상태 체크 함수 (기존 코드에 추가)
  function checkFarmImagesLoaded() {
    console.log('[농가 이미지 체크] 이미지 로드 상태 확인 시작');
    const farmImages = document.querySelectorAll('.farm-img img');
    
    farmImages.forEach((img, index) => {
      console.log(`[농가 이미지 #${index}] 경로: ${img.src}`);
      
      img.addEventListener('load', function() {
        console.log(`[농가 이미지 #${index}] 이미지 로드 성공: ${img.src}`);
      });
      
      // 오류 이벤트 리스너 추가
      img.addEventListener('error', function() {
        console.error(`[농가 이미지 #${index}] 이미지 로드 실패: ${img.src}`);
        // 대체 이미지 설정
        this.onerror = null; // 무한 루프 방지
        this.src = `https://picsum.photos/seed/farm${index}/300/250`;
      });
    });
  }
  
  // 농가 소개 섹션 로드 함수
  function loadFarmSection(containerId) {
    const container = document.getElementById(containerId);
    if (!container) return Promise.reject(`${containerId} 요소를 찾을 수 없습니다.`);
    
    const contextPath = window.contextPath || "";
    
    // 로딩 상태 표시
    container.innerHTML = `<div class="loading-indicator">
      <div class="spinner"></div>
      <p>농가 정보를 불러오는 중...</p>
    </div>`;
    
    // 요청 URL 구성 - 카테고리 로드 방식과 동일하게 변경
    const requestUrl = `${contextPath}/front?key=farm&methodName=category&category=farm&pageNo=1`;
    console.log(`[농가 데이터] 요청 URL: ${requestUrl}`);
    
    return fetch(requestUrl)
      .then(response => {
        console.log(`[농가 데이터] 응답 상태: ${response.status} ${response.statusText}`);
        if (!response.ok) {
          throw new Error(`응답 오류: ${response.status} ${response.statusText}`);
        }
        return response.text(); // JSON 대신 HTML을 받음
      })
      .then(html => {
        console.log(`[농가 데이터] HTML 응답 받음 (길이: ${html.length})`);
        container.innerHTML = html;
        
        // 이미지 로드 상태 확인
        setTimeout(checkFarmImagesLoaded, 100);
        
        // 슬라이더 초기화
        setTimeout(initFarmSlider, 300);
        
        return 'farm-success';
      })
      .catch(error => {
        console.error('[농가 데이터] 로드 오류:', error);
        container.innerHTML = `
          <div class="farm-error-container">
            <div class="error-message">
              <p>농가 정보를 불러오는데 실패했습니다</p>
              <p class="error-details">${error.message}</p>
            </div>
            <button class="retry-button" onclick="retryLoadFarm('${containerId}')">
              <i class="fas fa-sync-alt"></i> 다시 시도
            </button>
          </div>
        `;
        return 'farm-error';
      });
  }

  // farm-section이 로드된 후 이미지 패스 디버깅
  function logFarmImagesPath() {
    const farmImages = document.querySelectorAll('.farm-img img');
    console.log(`[농가 이미지] 총 ${farmImages.length}개 이미지 찾음`);
    
    farmImages.forEach((img, index) => {
      console.log(`[농가 이미지 #${index}] src: ${img.src}, alt: ${img.alt}`);
      // 이미지 로드 이벤트 리스너 추가
      img.addEventListener('error', function() {
        console.error(`[농가 이미지 #${index}] 이미지 로드 실패: ${img.src}`);
        // 기본 이미지 설정 (이미 onerror에 설정되어 있지만 명시적으로 설정)
        this.src = `${window.contextPath || ''}/images/farm/default-farm.jpg`;
      });
      
      // 이미지가 이미 로드된 경우를 대비하여 현재 완료 상태 확인
      if (img.complete) {
        if (img.naturalHeight === 0) {
          console.warn(`[농가 이미지 #${index}] 이미지가 로드되었지만 내용이 없음: ${img.src}`);
        } else {
          console.log(`[농가 이미지 #${index}] 이미 로드됨: ${img.naturalWidth}x${img.naturalHeight}`);
        }
      }
    });
  }

  // 재시도 함수
  window.retryLoadFarm = function(containerId) {
    console.log('[농가 데이터] 재시도');
    loadFarmSection(containerId);
  };

  // 농가 슬라이더 초기화 함수
  function initFarmSlider() {
    const container = document.querySelector('.farm-container');
    const leftArrow = document.querySelector('.farm-slider .arrow-left');
    const rightArrow = document.querySelector('.farm-slider .arrow-right');
    
    if (!container || !leftArrow || !rightArrow) {
      console.warn('[농가 슬라이더] 슬라이더 요소를 찾을 수 없습니다.');
      return;
    }
    
    let position = 0;
    const itemWidth = 280; // 카드 하나당 대략적인 너비 + 간격
    const farmCards = container.querySelectorAll('.farm-card');
    const maxPosition = Math.max(0, farmCards.length - 4) * itemWidth;
    
    // 초기 상태 설정
    container.style.transition = 'transform 0.3s ease';
    
    // 화살표 버튼 이벤트 리스너
    leftArrow.addEventListener('click', function() {
      position = Math.max(0, position - itemWidth);
      container.style.transform = `translateX(-${position}px)`;
    });
    
    rightArrow.addEventListener('click', function() {
      position = Math.min(maxPosition, position + itemWidth);
      container.style.transform = `translateX(-${position}px)`;
    });
    
    console.log('[농가 슬라이더] 초기화 완료');
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

  // 카테고리 로딩 부분에 농가 섹션도 추가
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
    // 모든 제품 카테고리 로드 후 농가 섹션 로드
    logLoadingStatus("farm", "로딩 시작");
    return loadFarmSection("farm-container");
  })
  .then(() => {
    console.log("[카테고리 로더] 모든 섹션 로딩 완료");
  })
  .catch((error) => {
    console.error("[카테고리 로더] 초기화 중 오류:", error);
  });
    
  // 전역 스코프에 함수 노출
  window.initFarmSlider = initFarmSlider;
});