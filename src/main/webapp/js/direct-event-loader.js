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
    
    // 이벤트 핸들러
    leftArrow.addEventListener('click', (e) => {
      e.preventDefault();
      console.log('왼쪽 화살표 클릭됨');
      currentIndex = (currentIndex - 1 + bannerEvents.length) % bannerEvents.length;
      updateBanner();
    });
    
    rightArrow.addEventListener('click', (e) => {
      e.preventDefault();
      console.log('오른쪽 화살표 클릭됨');
      currentIndex = (currentIndex + 1) % bannerEvents.length;
      updateBanner();
    });
    
    // 자동 슬라이드 기능
    let slideInterval = setInterval(() => {
      currentIndex = (currentIndex + 1) % bannerEvents.length;
      updateBanner();
    }, 5000);
    
    // 배너에 마우스를 올리면 자동 슬라이드 정지
    bannerSlider.addEventListener('mouseenter', () => {
      console.log('배너에 마우스 올림 - 자동 슬라이드 정지');
      clearInterval(slideInterval);
    });
    
    // 배너에서 마우스가 벗어나면 자동 슬라이드 재개
    bannerSlider.addEventListener('mouseleave', () => {
      console.log('배너에서 마우스 벗어남 - 자동 슬라이드 재개');
      clearInterval(slideInterval); // 기존 인터벌 제거 (중복 방지)
      slideInterval = setInterval(() => {
        currentIndex = (currentIndex + 1) % bannerEvents.length;
        updateBanner();
      }, 5000);
    });
    
    // 최초 렌더링
    console.log('배너 초기 렌더링 시작');
    renderBanner();
    console.log('direct-event-loader.js 실행 완료');
});