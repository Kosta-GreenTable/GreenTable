// 진행 중인 이벤트 데이터를 전역 변수로 노출
window.ongoingEvents = [
  {
    id: 1,
    title: "식단 정기배송 15% 할인 이벤트",
    image: "https://picsum.photos/seed/event1/530/190",
    description: "5월 한 달간 정기배송 신청 시 15% 할인 혜택을 드립니다.",
    startDate: "2025-05-01",
    endDate: "2025-05-31",
  },
  {
    id: 2,
    title: "친구 초대 포인트 더블 적립",
    image: "https://picsum.photos/seed/event11/530/190",
    description:
      "친구 초대 시 기존 5,000포인트에서 10,000포인트로 더블 적립!",
    startDate: "2025-05-05",
    endDate: "2025-05-20",
  },
  {
    id: 3,
    title: "신규 회원 첫 주문 무료배송",
    image: "https://picsum.photos/seed/event3/530/190",
    description: "신규 회원 가입 후 첫 주문 시 배송비 무료 혜택을 드립니다.",
    startDate: "2025-05-01",
    endDate: "2025-06-30",
  },
  {
    id: 4,
    title: "특가 샐러드 기획전",
    image: "https://picsum.photos/seed/event4/530/190",
    description: "인기 샐러드 5종 한정 수량 특가 판매!",
    startDate: "2025-05-10",
    endDate: "2025-05-25",
  },
  {
    id: 5,
    title: "도시락 2+1 프로모션",
    image: "https://picsum.photos/seed/event5/530/190",
    description: "인기 도시락 2개 구매 시 1개 무료 증정!",
    startDate: "2025-05-15",
    endDate: "2025-05-22",
  },
  {
    id: 6,
    title: "인스타그램 인증 이벤트",
    image: "https://picsum.photos/seed/event6/530/190",
    description:
      "그린테이블 제품과 함께 인스타그램 인증 시 5,000포인트 적립!",
    startDate: "2025-05-01",
    endDate: "2025-05-31",
  },
  {
    id: 7,
    title: "제철 과일 증정 이벤트",
    image: "https://picsum.photos/seed/event7/530/190",
    description: "3만원 이상 구매 시 제철 과일 세트를 드립니다.",
    startDate: "2025-05-12",
    endDate: "2025-05-26",
  },
  {
    id: 8,
    title: "결제 수단별 추가 할인",
    image: "https://picsum.photos/seed/event8/530/190",
    description: "카드사별 추가 할인 혜택을 확인하세요!",
    startDate: "2025-05-01",
    endDate: "2025-05-31",
  },
  {
    id: 9,
    title: "리뷰 작성 더블 포인트",
    image: "https://picsum.photos/seed/event9/530/190",
    description: "포토 리뷰 작성 시 포인트 2배 적립!",
    startDate: "2025-05-05",
    endDate: "2025-05-25",
  },
  {
    id: 10,
    title: "프리미엄 식단 체험단 모집",
    image: "https://picsum.photos/seed/event10/530/190",
    description: "새롭게 출시된 프리미엄 식단 체험단을 모집합니다.",
    startDate: "2025-05-10",
    endDate: "2025-05-20",
  },
];

document.addEventListener("DOMContentLoaded", () => {
  // 로컬 스토리지에서 선택된 이벤트 ID와 탭 가져오기
  const selectedEventId = parseInt(localStorage.getItem("selectedEventId"));
  const selectedTab = localStorage.getItem("eventTab");

  // 로컬 스토리지 데이터 사용 후 삭제
  localStorage.removeItem("selectedEventId");
  localStorage.removeItem("eventTab");

  // 현재 날짜 표시
  const currentDateElem = document.getElementById("currentDate");
  if (currentDateElem) {
    const now = new Date();
    const dateOptions = {
      year: "numeric",
      month: "long",
      day: "numeric",
      weekday: "long",
    };
    currentDateElem.textContent = `오늘 날짜: ${now.toLocaleDateString(
      "ko-KR",
      dateOptions
    )}`;
  } // 현재 날짜 - 실제 날짜를 기반으로 진행중/종료된 이벤트 구분을 위해
  const today = new Date();
  const todayStr = today.toISOString().split("T")[0]; // YYYY-MM-DD 형식

  // 진행 중인 이벤트 데이터 (실제 이벤트 데이터)
  const ongoingEvents = window.ongoingEvents; // 전역 변수 참조

  // 종료된 이벤트 데이터
  const endedEvents = [
    {
      id: 101,
      title: "봄맞이 건강식단 기획전",
      image: "https://picsum.photos/seed/pastevent1/530/190",
      description: "봄을 맞아 준비한 신선한 식재료로 만든 건강식단 기획전",
      startDate: "2025-03-01",
      endDate: "2025-04-30",
    },
    {
      id: 102,
      title: "어린이날 특별 도시락 이벤트",
      image: "https://picsum.photos/seed/pastevent2/530/190",
      description: "어린이날 맞이 어린이 영양식단 특별 할인",
      startDate: "2025-04-25",
      endDate: "2025-05-05",
    },
    {
      id: 103,
      title: "첫 구매 고객 50% 할인",
      image: "https://picsum.photos/seed/pastevent3/530/190",
      description: "첫 구매 고객님을 위한 파격 할인 혜택!",
      startDate: "2025-04-01",
      endDate: "2025-04-15",
    },
    {
      id: 104,
      title: "SNS 공유 이벤트",
      image: "https://picsum.photos/seed/pastevent4/530/190",
      description: "그린테이블 SNS 공유하고 상품권 받자!",
      startDate: "2025-04-10",
      endDate: "2025-04-20",
    },
    {
      id: 105,
      title: "봄나물 샐러드 할인전",
      image: "https://picsum.photos/seed/pastevent5/530/190",
      description: "제철 봄나물로 만든 샐러드 특별가",
      startDate: "2025-03-15",
      endDate: "2025-04-10",
    },
    {
      id: 106,
      title: "리뷰왕 이벤트",
      image: "https://picsum.photos/seed/pastevent6/530/190",
      description: "베스트 리뷰어에게 상품권을 드립니다!",
      startDate: "2025-03-01",
      endDate: "2025-03-31",
    },
    {
      id: 107,
      title: "회원가입 특별 혜택",
      image: "https://picsum.photos/seed/pastevent7/530/190",
      description: "신규 회원가입 시 즉시 사용 가능한 5,000원 쿠폰 증정!",
      startDate: "2025-02-15",
      endDate: "2025-03-15",
    },
    {
      id: 108,
      title: "겨울 보양식 기획전",
      image: "https://picsum.photos/seed/pastevent8/530/190",
      description: "추운 겨울을 이겨내는 영양 가득 보양식 기획전",
      startDate: "2025-01-01",
      endDate: "2025-02-28",
    },
    {
      id: 109,
      title: "설날 명절 선물 이벤트",
      image: "https://picsum.photos/seed/pastevent9/530/190",
      description: "설날 명절 선물용 건강식품 세트 특별 판매",
      startDate: "2025-01-20",
      endDate: "2025-02-10",
    },
    {
      id: 110,
      title: "새해 맞이 건강 식단 챌린지",
      image: "https://picsum.photos/seed/pastevent10/530/190",
      description: "새해 결심을 지키는 4주 건강 식단 챌린지",
      startDate: "2025-01-01",
      endDate: "2025-01-31",
    },
  ];

  const eventsPerPage = 8;
  let currentTab = "ongoing";
  let currentPage = 1;
  let modalOpen = false;

  const eventGrid = document.getElementById("eventGrid");
  const pagination = document.getElementById("pagination");
  const tabs = document.querySelectorAll(".tab");

  // 이벤트 모달 생성
  const modalContainer = document.createElement("div");
  modalContainer.className = "event-modal-container";
  modalContainer.style.display = "none";
  document.body.appendChild(modalContainer);

  function renderEvents() {
    // 정렬된 이벤트 목록 가져오기
    let events = currentTab === "ongoing" ? ongoingEvents : endedEvents;

    // 진행중 이벤트는 종료일 기준 오름차순(가까운 순), 종료된 이벤트는 종료일 기준 내림차순(최근 종료순)
    events = [...events].sort((a, b) => {
      if (currentTab === "ongoing") {
        // 종료 임박 이벤트를 먼저
        return new Date(a.endDate) - new Date(b.endDate);
      } else {
        // 최근에 종료된 이벤트를 먼저
        return new Date(b.endDate) - new Date(a.endDate);
      }
    });

    const start = (currentPage - 1) * eventsPerPage;
    const end = start + eventsPerPage;
    const visibleEvents = events.slice(start, end);

    // 날짜 포맷 함수
    function formatDate(dateString) {
      const date = new Date(dateString);
      const month = date.getMonth() + 1;
      const day = date.getDate();
      return `${month}월 ${day}일`;
    }

    eventGrid.innerHTML = visibleEvents
      .map((event) => {
        // 이벤트 상태 확인 (오늘 날짜 기준)
        const isActive = event.endDate >= todayStr;
        const statusClass = isActive ? "event-active" : "event-ended";

        // 종료 임박 이벤트 확인 (3일 이내 종료)
        const endDate = new Date(event.endDate);
        const diffTime = Math.abs(endDate - today);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        const isEnding =
          isActive && event.startDate <= todayStr && diffDays <= 3;

        // 상태 표시 HTML
        const eventStatus = isActive
          ? event.startDate > todayStr
            ? '<span class="event-coming-soon">진행 예정</span>'
            : isEnding
            ? '<span class="event-ending-soon">종료 임박</span>'
            : '<span class="event-ongoing">진행 중</span>'
          : '<span class="event-ended-tag">종료됨</span>';

        return `
          <div class="event-card ${statusClass}" data-id="${event.id}">
            <img src="${event.image}" alt="${event.title}" />
            <div class="event-info">
              <h3>${event.title}</h3>
              <p class="event-period">${formatDate(
                event.startDate
              )} ~ ${formatDate(event.endDate)}</p>
              ${eventStatus}
            </div>
          </div>
        `;
      })
      .join("");

    renderPagination(events.length);
  }

  function renderPagination(totalEvents) {
    const totalPages = Math.ceil(totalEvents / eventsPerPage);
    pagination.innerHTML = `
      <button class="page-btn prev" ${currentPage === 1 ? "disabled" : ""}>
        <i class="fas fa-chevron-left"></i>
      </button>
      ${Array.from(
        { length: totalPages },
        (_, i) => `
        <button class="page-btn ${
          currentPage === i + 1 ? "active" : ""
        }" data-page="${i + 1}">
          ${i + 1}
        </button>
      `
      ).join("")}
      <button class="page-btn next" ${
        currentPage === totalPages ? "disabled" : ""
      }>
        <i class="fas fa-chevron-right"></i>
      </button>
    `;
    document.querySelectorAll(".page-btn").forEach((button) => {
      button.addEventListener("click", () => {
        if (button.classList.contains("prev")) {
          currentPage--;
        } else if (button.classList.contains("next")) {
          currentPage++;
        } else {
          currentPage = parseInt(button.dataset.page, 10);
        }
        renderAndAttachListeners();
      });
    });
  }

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      tabs.forEach((t) => t.classList.remove("active"));
      tab.classList.add("active");
      currentTab = tab.dataset.tab;
      currentPage = 1;
      renderEvents();
    });
  });

  // 이벤트 카드 클릭 이벤트 처리
  function attachEventCardListeners() {
    document.querySelectorAll(".event-card").forEach((card) => {
      card.addEventListener("click", () => {
        const eventId = parseInt(card.dataset.id);
        const events = currentTab === "ongoing" ? ongoingEvents : endedEvents;
        const event = events.find((e) => e.id === eventId);

        if (event) {
          showEventModal(event);
        }
      });
    });
  }
  // 날짜 포맷 함수
  function formatDateLong(dateString) {
    const options = { year: "numeric", month: "long", day: "numeric" };
    return new Date(dateString).toLocaleDateString("ko-KR", options);
  }

  // 이벤트 모달 표시
  function showEventModal(event) {
    // 이벤트 상태 확인
    const isActive = event.endDate >= todayStr;
    const isUpcoming = event.startDate > todayStr;

    // 이벤트 상태에 따른 메시지와 스타일
    let statusHTML = "";
    if (!isActive) {
      statusHTML = '<div class="event-status ended">종료된 이벤트입니다</div>';
    } else if (isUpcoming) {
      statusHTML =
        '<div class="event-status upcoming">곧 시작될 이벤트입니다</div>';
    } else {
      statusHTML =
        '<div class="event-status active">현재 진행 중인 이벤트입니다</div>';
    }

    // 남은 기간 계산
    let remainingDaysHTML = "";
    if (isActive && !isUpcoming) {
      const endDate = new Date(event.endDate);
      const today = new Date();
      const diffTime = Math.abs(endDate - today);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      remainingDaysHTML = `<div class="remaining-days">종료까지 <strong>${diffDays}일</strong> 남았습니다</div>`;
    }

    const modalContent = `
      <div class="event-modal">
        <div class="modal-header">
          <h2>${event.title}</h2>
          <button class="close-modal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="modal-image-container">
            <img src="${event.image.replace("530/190", "800/300")}" alt="${
      event.title
    }" class="modal-image" />
            ${statusHTML}
          </div>
          
          <div class="modal-details">
            <div class="event-info-box">
              <div class="event-period-box">
                <strong>이벤트 기간</strong>
                <p>${formatDateLong(event.startDate)} ~ ${formatDateLong(
      event.endDate
    )}</p>
                ${remainingDaysHTML}
              </div>
              <div class="event-description-box">
                <strong>이벤트 설명</strong>
                <p>${event.description}</p>
              </div>
            </div>
            
            <div class="event-content">
              <h3>이벤트 상세 내용</h3>
              <p>이 이벤트는 Green Table의 고객분들을 위해 준비한 특별한 혜택입니다. 아래 내용을 확인하시고 많은 참여 부탁드립니다.</p>
              <ul>
                <li>이벤트 참여 방법: 해당 상품 구매 후 자동 적용</li>
                <li>혜택 지급 일정: 이벤트 종료 후 7일 이내</li>
                <li>문의처: Green Table 고객센터 (1588-0000)</li>
              </ul>
            </div>
            
            <div class="modal-actions">
              <button class="action-btn primary-btn">이벤트 참여하기</button>
              <button class="action-btn share-btn">공유하기</button>
            </div>
          </div>
        </div>
      </div>
    `;
    modalContainer.innerHTML = modalContent;
    modalContainer.style.display = "flex";
    modalOpen = true;

    // 닫기 버튼
    document.querySelector(".close-modal").addEventListener("click", () => {
      modalContainer.style.display = "none";
      modalOpen = false;
    });

    // 이벤트 참여 버튼
    const primaryBtn = document.querySelector(".primary-btn");
    if (primaryBtn) {
      primaryBtn.addEventListener("click", () => {
        const isActive =
          event.endDate >= todayStr && event.startDate <= todayStr;
        if (isActive) {
          alert("이벤트에 참여해 주셔서 감사합니다!");
        } else if (event.startDate > todayStr) {
          alert(
            "아직 시작되지 않은 이벤트입니다. " +
              formatDateLong(event.startDate) +
              "부터 참여할 수 있습니다."
          );
        } else {
          alert("이미 종료된 이벤트입니다.");
        }
      });
    }

    // 공유하기 버튼
    const shareBtn = document.querySelector(".share-btn");
    if (shareBtn) {
      shareBtn.addEventListener("click", () => {
        alert("이벤트 공유 기능은 현재 준비 중입니다.");
      });
    }

    // 모달 외부 클릭 시 닫기
    modalContainer.addEventListener("click", (e) => {
      if (e.target === modalContainer) {
        modalContainer.style.display = "none";
        modalOpen = false;
      }
    });

    // ESC 키로 모달 닫기
    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape" && modalOpen) {
        modalContainer.style.display = "none";
        modalOpen = false;
      }
    });
  }
  // 이벤트 렌더링 후 리스너 연결
  function renderAndAttachListeners() {
    renderEvents();
    attachEventCardListeners();

    // 선택된 이벤트가 있으면 해당 이벤트 모달 보여주기
    if (selectedEventId) {
      // 선택된 탭으로 변경
      if (selectedTab) {
        currentTab = selectedTab;
        tabs.forEach((tab) => {
          if (tab.dataset.tab === selectedTab) {
            tabs.forEach((t) => t.classList.remove("active"));
            tab.classList.add("active");
          }
        });
        renderEvents();
        attachEventCardListeners();
      }

      // 이벤트 찾기
      const events = currentTab === "ongoing" ? ongoingEvents : endedEvents;
      const event = events.find((e) => e.id === selectedEventId);

      if (event) {
        // 약간의 지연 후 모달 표시 (페이지 로딩 후)
        setTimeout(() => {
          showEventModal(event);
        }, 500);
      }
    }
  }

  renderAndAttachListeners();
  // 페이지 변경 후에도 리스너 다시 연결
  document.addEventListener("click", (e) => {
    if (
      e.target.classList.contains("page-btn") ||
      e.target.closest(".page-btn")
    ) {
      setTimeout(() => {
        attachEventCardListeners();
      }, 100);
    }
  });

  // 이벤트 검색 기능
  const searchInput = document.getElementById("eventSearchInput");
  const searchButton = document.getElementById("eventSearchButton");

  if (searchInput && searchButton) {
    // 검색 버튼 클릭
    searchButton.addEventListener("click", () => {
      searchEvents();
    });

    // Enter 키 입력
    searchInput.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        searchEvents();
      }
    });

    function searchEvents() {
      const searchTerm = searchInput.value.toLowerCase().trim();

      // 검색어가 없으면 모든 이벤트 표시
      if (!searchTerm) {
        renderAndAttachListeners();
        return;
      }

      // 현재 탭의 이벤트 목록 필터링
      const events = currentTab === "ongoing" ? ongoingEvents : endedEvents;

      // 제목이나 설명에 검색어 포함된 이벤트 필터링
      const filteredEvents = events.filter(
        (event) =>
          event.title.toLowerCase().includes(searchTerm) ||
          event.description.toLowerCase().includes(searchTerm)
      );

      // 검색 결과가 없는 경우
      if (filteredEvents.length === 0) {
        eventGrid.innerHTML = `
          <div class="no-events-message">
            <i class="fas fa-search"></i>
            <p>"${searchTerm}" 검색 결과가 없습니다</p>
            <p class="no-events-sub">다른 검색어로 다시 시도해보세요</p>
          </div>
        `;
        pagination.innerHTML = "";
      } else {
        const start = 0;
        const end = filteredEvents.length;
        const visibleEvents = filteredEvents.slice(start, end);

        // 검색 결과 렌더링
        eventGrid.innerHTML = visibleEvents
          .map((event) => {
            const isActive = event.endDate >= todayStr;
            const statusClass = isActive ? "event-active" : "event-ended";

            // 종료 임박 이벤트 확인 (3일 이내 종료)
            const endDate = new Date(event.endDate);
            const diffTime = Math.abs(endDate - today);
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
            const isEnding =
              isActive && event.startDate <= todayStr && diffDays <= 3;

            // 상태 표시 HTML
            const eventStatus = isActive
              ? event.startDate > todayStr
                ? '<span class="event-coming-soon">진행 예정</span>'
                : isEnding
                ? '<span class="event-ending-soon">종료 임박</span>'
                : '<span class="event-ongoing">진행 중</span>'
              : '<span class="event-ended-tag">종료됨</span>';

            // 검색어 하이라이트
            const highlightedTitle = event.title.replace(
              new RegExp(searchTerm, "gi"),
              (match) => `<mark>${match}</mark>`
            );

            return `
              <div class="event-card ${statusClass}" data-id="${event.id}">
                <img src="${event.image}" alt="${event.title}" />
                <div class="event-info">
                  <h3>${highlightedTitle}</h3>
                  <p class="event-period">${formatDate(
                    event.startDate
                  )} ~ ${formatDate(event.endDate)}</p>
                  ${eventStatus}
                </div>
              </div>
            `;
          })
          .join("");

        pagination.innerHTML = `
          <div class="search-result-info">
            검색 결과: ${filteredEvents.length}개의 이벤트
            <button class="reset-search-btn">검색 초기화</button>
          </div>
        `;

        // 검색 초기화 버튼
        document
          .querySelector(".reset-search-btn")
          .addEventListener("click", () => {
            searchInput.value = "";
            renderAndAttachListeners();
          });

        // 이벤트 카드 리스너 연결
        attachEventCardListeners();
      }
    }
  }
});