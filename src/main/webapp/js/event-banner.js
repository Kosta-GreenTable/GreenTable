document.addEventListener("DOMContentLoaded", function () {
  // event.js에서 이벤트 데이터 가져오기
  let ongoingEvents = [];

  // 현재 날짜 정보
  const today = new Date();
  const todayStr = today.toISOString().split("T")[0];

  // 날짜 포맷 함수
  function formatDate(dateString) {
    const date = new Date(dateString);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${month}월 ${day}일`;
  }

  // 이벤트 데이터 가져오기
  function fetchEventData() {
    // import event.js에서 진행 중인 이벤트 가져오기
    // 여기서는 event.js의 이벤트 데이터를 직접 가져옵니다
    ongoingEvents = [
      {
        id: 1,
        title: "식단 정기배송 15% 할인 이벤트",
        image: "https://picsum.photos/seed/event1/1200/300",
        description: "5월 한 달간 정기배송 신청 시 15% 할인 혜택을 드립니다.",
        startDate: "2025-05-01",
        endDate: "2025-05-31",
      },
      {
        id: 2,
        title: "친구 초대 포인트 더블 적립",
        image: "https://picsum.photos/seed/event2/1200/300",
        description:
          "친구 초대 시 기존 5,000포인트에서 10,000포인트로 더블 적립!",
        startDate: "2025-05-05",
        endDate: "2025-05-20",
      },
      {
        id: 3,
        title: "신규 회원 첫 주문 무료배송",
        image: "https://picsum.photos/seed/event3/1200/300",
        description:
          "신규 회원 가입 후 첫 주문 시 배송비 무료 혜택을 드립니다.",
        startDate: "2025-05-01",
        endDate: "2025-06-30",
      },
      {
        id: 4,
        title: "특가 샐러드 기획전",
        image: "https://picsum.photos/seed/event4/1200/300",
        description: "인기 샐러드 5종 한정 수량 특가 판매!",
        startDate: "2025-05-10",
        endDate: "2025-05-25",
      },
    ];

    // 진행중인 이벤트만 필터링 (실제로는 event.js에서 필터링해야 함)
    ongoingEvents = ongoingEvents.filter(
      (event) => event.startDate <= todayStr && event.endDate >= todayStr
    );

    return ongoingEvents;
  }

  // 배너 슬라이더에 이벤트 데이터 적용
  function updateBannerWithEvents() {
    // 이벤트 데이터 가져오기
    const events = fetchEventData();

    const bannerSlider = document.querySelector(".banner-slider");
    if (!bannerSlider) return; // 이벤트가 없으면 기본 배너 표시
    if (events.length === 0) {
      const slide = document.createElement("div");
      slide.className = "banner-slide active";
      slide.innerHTML = `
        <img src="https://picsum.photos/seed/default/1200/300" alt="그린 테이블 배너">
        <div class="banner-overlay">
          <div class="banner-text">
            <h3>그린 테이블과 건강한 식단을 만나보세요</h3>
            <p>신선한 식재료로 만든 건강한 식단을 매일 집에서 만나보세요.</p>
            <button class="banner-more-btn">시작하기 <i class="fas fa-arrow-right"></i></button>
          </div>
        </div>
      `;
      bannerSlider.appendChild(slide);

      // 페이지 표시 업데이트
      const totalPagesSpan = document.querySelector(".total-pages");
      const currentPageSpan = document.querySelector(".current-page");
      if (totalPagesSpan) totalPagesSpan.textContent = "1";
      if (currentPageSpan) currentPageSpan.textContent = "1";
      return;
    }

    // 기존 배너 내용 지우기
    bannerSlider.innerHTML = "";

    // 이벤트로 배너 채우기
    events.forEach((event, index) => {
      const slide = document.createElement("div");
      slide.className = `banner-slide ${index === 0 ? "active" : ""}`;
      slide.dataset.id = event.id;

      // 이벤트 상세 페이지로 이동하는 링크
      const link = document.createElement("a");
      link.href = "event.html";
      link.onclick = (e) => {
        e.preventDefault();
        // 로컬 스토리지에 클릭한 이벤트 ID 저장
        localStorage.setItem("selectedEventId", event.id);
        localStorage.setItem("eventTab", "ongoing");
        window.location.href = "event.html";
      };

      // 배너 이미지 생성
      const img = document.createElement("img");
      img.src = event.image;
      img.alt = event.title;

      // 이벤트 상태 확인
      const isActive = event.endDate >= todayStr && event.startDate <= todayStr;
      const statusText = isActive
        ? "진행중"
        : event.startDate > todayStr
        ? "진행예정"
        : "종료됨";
      const statusClass = isActive
        ? "active"
        : event.startDate > todayStr
        ? "upcoming"
        : "ended"; // 배너 텍스트 오버레이 추가
      const overlay = document.createElement("div");
      overlay.className = "banner-overlay";
      overlay.innerHTML = `
        <div class="banner-text">
          <span class="banner-status ${statusClass}">${statusText}</span>
          <h3>${event.title}</h3>
          <p>${event.description}</p>
          <span class="banner-date">${formatDate(
            event.startDate
          )} ~ ${formatDate(event.endDate)}</span>
          <button class="banner-more-btn">자세히 보기 <i class="fas fa-arrow-right"></i></button>
        </div>
      `;

      link.appendChild(img);
      link.appendChild(overlay);
      slide.appendChild(link);
      bannerSlider.appendChild(slide);
    });

    // 배너 페이지 숫자 업데이트
    const totalPagesSpan = document.querySelector(".total-pages");
    if (totalPagesSpan) {
      totalPagesSpan.textContent = events.length;
    }
  }

  // 현재 배너 인덱스
  let currentSlideIndex = 0;

  // 배너 이동 함수
  function showSlide(index) {
    const slides = document.querySelectorAll(".banner-slide");
    if (!slides.length) return;

    // 인덱스 범위 확인
    if (index >= slides.length) {
      currentSlideIndex = 0;
    } else if (index < 0) {
      currentSlideIndex = slides.length - 1;
    } else {
      currentSlideIndex = index;
    }

    // 모든 슬라이드 비활성화
    slides.forEach((slide) => slide.classList.remove("active"));

    // 현재 슬라이드만 활성화
    slides[currentSlideIndex].classList.add("active");

    // 현재 페이지 표시 업데이트
    const currentPageElem = document.querySelector(".current-page");
    if (currentPageElem) {
      currentPageElem.textContent = currentSlideIndex + 1;
    }
  }

  // 화살표 버튼 이벤트 추가
  function setupNavigation() {
    const leftArrow = document.querySelector(".arrow-left");
    const rightArrow = document.querySelector(".arrow-right");

    if (leftArrow) {
      leftArrow.addEventListener("click", () => {
        showSlide(currentSlideIndex - 1);
      });
    }

    if (rightArrow) {
      rightArrow.addEventListener("click", () => {
        showSlide(currentSlideIndex + 1);
      });
    }
  }

  // 배너 업데이트 실행
  updateBannerWithEvents();

  // 네비게이션 설정
  setupNavigation();
});
