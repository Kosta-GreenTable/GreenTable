document.addEventListener("DOMContentLoaded", function () {
  // 농가 카드 클릭 이벤트
  initFarmCardEvents();
  
  // 농가 필터링 기능
  initFilterButtons();
  
  // 애니메이션 효과
  initScrollAnimation();
  
  // 페이지 로드 완료 후 로딩 애니메이션
  document.body.classList.add("loaded");
});

// 농가 카드 확장/축소 이벤트 초기화
function initFarmCardEvents() {
  const farmCards = document.querySelectorAll(".farm-card");
  farmCards.forEach((card) => {
    card.addEventListener("click", function () {
      this.classList.toggle("expanded");
    });
  });
}

// 농가 필터 버튼 초기화
function initFilterButtons() {
  const filterButtons = document.querySelectorAll(".farm-filter-btn");
  const farmCards = document.querySelectorAll(".farm-card");
  
  filterButtons.forEach((button) => {
    button.addEventListener("click", function () {
      // 활성화된 버튼 표시
      filterButtons.forEach((btn) => btn.classList.remove("active"));
      this.classList.add("active");

      // 필터 적용
      const filterValue = this.getAttribute("data-filter");

      farmCards.forEach((card) => {
        if (filterValue === "all") {
          card.style.display = "block";
        } else if (card.getAttribute("data-category") === filterValue) {
          card.style.display = "block";
        } else {
          card.style.display = "none";
        }
      });
    });
  });
}

// 스크롤 애니메이션 초기화
function initScrollAnimation() {
  const handleScroll = function() {
    const fadeElements = document.querySelectorAll(".fade-in");
    fadeElements.forEach((element) => {
      const elementPosition = element.getBoundingClientRect().top;
      const screenPosition = window.innerHeight / 1.3;

      if (elementPosition < screenPosition) {
        element.classList.add("visible");
      }
    });
  };
  
  // 초기 로드 시 한번 실행
  handleScroll();
  
  // 스크롤 이벤트 리스너 등록
  window.addEventListener("scroll", handleScroll);
}

// 농가 슬라이더 초기화 (메인 페이지용)
function initFarmSlider() {
  const slider = document.querySelector('.farm-slider .farm-container');
  const leftArrow = document.querySelector('.farm-slider .arrow-left');
  const rightArrow = document.querySelector('.farm-slider .arrow-right');
  
  if (!slider || !leftArrow || !rightArrow) return;
  
  let position = 0;
  const cardWidth = 300; // 카드 너비 + 간격
  const visibleCards = Math.floor(slider.clientWidth / cardWidth);
  const totalCards = slider.querySelectorAll('.farm-card').length;
  const maxPosition = Math.max(0, totalCards - visibleCards);
  
  // 화살표 버튼 활성화/비활성화 설정
  function updateArrows() {
    leftArrow.style.opacity = position <= 0 ? "0.5" : "1";
    rightArrow.style.opacity = position >= maxPosition ? "0.5" : "1";
  }
  
  // 초기 화살표 상태 설정
  updateArrows();
  
  // 왼쪽 이동 버튼
  leftArrow.addEventListener('click', () => {
    if (position > 0) {
      position--;
      slider.style.transform = `translateX(-${position * cardWidth}px)`;
      updateArrows();
    }
  });
  
  // 오른쪽 이동 버튼
  rightArrow.addEventListener('click', () => {
    if (position < maxPosition) {
      position++;
      slider.style.transform = `translateX(-${position * cardWidth}px)`;
      updateArrows();
    }
  });
}
