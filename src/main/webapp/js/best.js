// 즉시 실행 함수로 페이지 로드 즉시 모든 BEST 배지 제거
(function() {
  // 페이지 내 모든 BEST 배지 요소를 찾아 제거 (헤더, 네비게이션 등에서만)
  function removeBestBadges() {
    // 헤더, 네비게이션, 문서 최상단 등에 있는 배지만 제거
    const headerBestBadges = document.querySelectorAll('header .best-badge, nav .best-badge, body > .best-badge, .navigation .best-badge');
    headerBestBadges.forEach(badge => {
      badge.remove();
    });
  }
  
  // 페이지 로드 시 실행
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', removeBestBadges);
  } else {
    removeBestBadges();
  }
  
  // 페이지 완전 로드 후 다시 실행 (지연 로드되는 요소 처리)
  window.addEventListener('load', removeBestBadges);
})();

document.addEventListener('DOMContentLoaded', function() {
  // 상품 컨테이너와 상품들
  const productGrid = document.querySelector('.product-grid');
  const productItems = document.querySelectorAll('.product-item');
  
  // BEST 배지 추가 함수 - 모든 제품에 인기도 순으로 BEST 순번 배지 추가
  function addBestBadges() {
    // 인기도(popularity) 기준으로 상품 정렬
    const sortedProducts = Array.from(productItems).sort((a, b) => {
      return parseFloat(b.getAttribute('data-popularity')) - parseFloat(a.getAttribute('data-popularity'));
    });
    
    // 모든 상품에 BEST 순번 배지 추가
    for (let i = 0; i < sortedProducts.length; i++) {
      const product = sortedProducts[i];
      
      // 순위 설정 (1부터 시작)
      const bestRank = i + 1;
      product.setAttribute('data-best-rank', bestRank);
      
      // 기존 배지가 있으면 제거
      const existingBadge = product.querySelector('.best-badge');
      if (existingBadge) {
        existingBadge.remove();
      }
      
      // 새 배지 생성 및 추가
      const badge = document.createElement('div');
      badge.className = 'best-badge';
      badge.textContent = `BEST ${bestRank}`;
      
      // 상품 링크 내부의 첫 번째 위치에 배지 삽입
      const productLink = product.querySelector('.product-link');
      if (productLink) {
        productLink.insertBefore(badge, productLink.firstChild);
      }
      
      // DOM에 제품 추가 (인기도 순)
      productGrid.appendChild(product);
    }
    
    // 추가 작업: 배지 생성 후 다시 한번 더 헤더의 배지 제거
    setTimeout(function() {
      const headerBestBadges = document.querySelectorAll('header .best-badge, nav .best-badge, body > .best-badge');
      headerBestBadges.forEach(badge => badge.remove());
    }, 100);
  }
  
  // 초기 로드 시 BEST 배지 추가 및 인기도 순으로 정렬
  addBestBadges();
  
  // 정렬 버튼 섹션 제거 (더 이상 정렬 기능이 필요 없으므로)
  const productSortSection = document.querySelector('.product-sort');
  if (productSortSection) {
    productSortSection.style.display = 'none'; // 또는 부모 요소에서 제거
  }
  
  // DOM 변화 감시하여 배지가 다시 추가되는 경우 제거
  const observer = new MutationObserver(function(mutations) {
    mutations.forEach(function(mutation) {
      if (mutation.addedNodes.length > 0) {
        const headerBestBadges = document.querySelectorAll('header .best-badge, nav .best-badge, body > .best-badge');
        headerBestBadges.forEach(badge => badge.remove());
      }
    });
  });
  
  // 헤더와 네비게이션 감시
  const header = document.querySelector('header');
  const nav = document.querySelector('nav');
  
  if (header) {
    observer.observe(header, { childList: true, subtree: true });
  }
  
  if (nav) {
    observer.observe(nav, { childList: true, subtree: true });
  }
});