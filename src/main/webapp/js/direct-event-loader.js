document.addEventListener("DOMContentLoaded", function() {
    console.log('direct-event-loader.js 실행 시작');
    
    // 전역 이벤트 데이터 확인
    if (!window.ongoingEvents || !window.ongoingEvents.length) {
      console.error('이벤트 데이터를 찾을 수 없습니다. window.ongoingEvents:', window.ongoingEvents);
      return;
    }
    
    console.log(`이벤트 데이터 확인: ${window.ongoingEvents.length}개 항목 발견`);
    
    // DOM 요소 확인 및 로깅
    const bannerSlider = document.querySelector('.banner-slider');
    const currentPageEl = document.querySelector('.current-page');
    const totalPagesEl = document.querySelector('.total-pages');
    const leftArrow = document.querySelector('.arrow-left');
    const rightArrow = document.querySelector('.arrow-right');
    
    console.log('DOM 요소 확인:', {
      bannerSlider: !!bannerSlider,
      currentPageEl: !!currentPageEl,
      totalPagesEl: !!totalPagesEl,
      leftArrow: !!leftArrow,
      rightArrow: !!rightArrow
    });
    
    if (!bannerSlider || !currentPageEl || !totalPagesEl || !leftArrow || !rightArrow) {
      console.error('배너 요소를 찾을 수 없습니다.');
      return;
    }
    
    // 배너에 표시할 이벤트 (최대 4개)
    const bannerEvents = window.ongoingEvents.slice(0, 4);
    console.log(`배너에 표시할 이벤트: ${bannerEvents.length}개`);
    
    let currentIndex = 0;
    totalPagesEl.textContent = bannerEvents.length;
    
    // 터치 이벤트를 위한 변수들
    let touchStartX = 0;
    let touchEndX = 0;
    
    // 배너 자동 슬라이드 타이머
    let autoSlideTimer;
    
    // 배너 초기 렌더링
    function renderBanner() {
      console.log('배너 렌더링 시작...');
      
      bannerSlider.innerHTML = bannerEvents.map((event, index) => `
        <a href="${contextPath}/event/event.jsp" class="banner-slide ${index === 0 ? 'active' : ''}" data-index="${index}">
          <img src="${event.image.replace('530/190', '1200/400')}" alt="${event.title}" />
          <div class="banner-info">
            <h3>${event.title}</h3>
            <p>${event.description}</p>
            <span class="learn-more">자세히 보기 <i class="fas fa-chevron-right"></i></span>
          </div>
        </a>
      `).join('');
      
      console.log('배너 HTML 생성 완료');
      updateBanner();
    }
    
    // 배너 상태 업데이트
    function updateBanner() {
      // 모든 배너를 숨기고 현재 배너만 표시
      const slides = bannerSlider.querySelectorAll('.banner-slide');
      console.log(`배너 업데이트: ${slides.length}개 슬라이드 중 ${currentIndex + 1}번째 활성화`);
      
      slides.forEach((slide, i) => {
        if (i === currentIndex) {
          slide.classList.add('active');
        } else {
          slide.classList.remove('active');
        }
      });
      
      // 페이지 번호 업데이트
      currentPageEl.textContent = currentIndex + 1;
    }
    
    // 배너 이전/다음 표시
    function showPrevBanner() {
      console.log('이전 배너 표시');
      currentIndex = (currentIndex - 1 + bannerEvents.length) % bannerEvents.length;
      updateBanner();
    }
    
    function showNextBanner() {
      console.log('다음 배너 표시');
      currentIndex = (currentIndex + 1) % bannerEvents.length;
      updateBanner();
    }
    
    // 이벤트 핸들러 등록
    function setupEventHandlers() {
      leftArrow.addEventListener('click', showPrevBanner);
      rightArrow.addEventListener('click', showNextBanner);
      
      // 터치 이벤트 처리
      bannerSlider.addEventListener('touchstart', handleTouchStart, { passive: true });
      bannerSlider.addEventListener('touchend', handleTouchEnd, { passive: true });
      
      // 마우스 이벤트 처리 (데스크톱)
      bannerSlider.addEventListener('mouseenter', pauseAutoSlide);
      bannerSlider.addEventListener('mouseleave', startAutoSlide);
      
      // 자동 슬라이드 시작
      startAutoSlide();
    }
    
    // 터치 이벤트 핸들러
    function handleTouchStart(e) {
      touchStartX = e.changedTouches[0].screenX;
      pauseAutoSlide(); // 터치 시작 시 자동 슬라이드 일시 중지
    }
    
    function handleTouchEnd(e) {
      touchEndX = e.changedTouches[0].screenX;
      handleSwipe();
      startAutoSlide(); // 터치 종료 시 자동 슬라이드 재개
    }
    
    // 스와이프 처리
    function handleSwipe() {
      const SWIPE_THRESHOLD = 50; // 스와이프로 인식할 최소 거리
      
      if (touchStartX - touchEndX > SWIPE_THRESHOLD) {
        // 왼쪽으로 스와이프: 다음 배너
        showNextBanner();
      } else if (touchEndX - touchStartX > SWIPE_THRESHOLD) {
        // 오른쪽으로 스와이프: 이전 배너
        showPrevBanner();
      }
    }
    
    // 자동 슬라이드 시작
    function startAutoSlide() {
      // 기존 타이머 제거
      clearInterval(autoSlideTimer);
      // 새 타이머 설정 (5초마다 다음 배너)
      autoSlideTimer = setInterval(showNextBanner, 5000);
    }
    
    // 자동 슬라이드 일시 중지
    function pauseAutoSlide() {
      clearInterval(autoSlideTimer);
    }
    
    // 최초 렌더링
    console.log('배너 초기 렌더링 시작');
    renderBanner();
    setupEventHandlers();
    console.log('direct-event-loader.js 실행 완료');
});