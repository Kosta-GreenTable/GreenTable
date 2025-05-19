document.addEventListener("DOMContentLoaded", function () {
  // 헤더 고정 기능
  initFixedHeader();

  // 배너 슬라이더 기능
  initBannerSlider();

  // 로그인 상태 체크 및 메뉴 변경
  checkLoginStatus();

  // 상품 클릭 이벤트
  initProductClicks();

  // 검색 기능
  initSearchFunction();

  // 농가 슬라이더 기능
  initFarmSlider();
});

// 헤더 고정 기능
function initFixedHeader() {
  const header = document.querySelector(".main-header");
  const headerOffsetTop = header.offsetTop;

  window.addEventListener("scroll", function () {
    if (window.pageYOffset > headerOffsetTop) {
      header.classList.add("fixed-header");
      document.body.style.paddingTop = header.offsetHeight + "px";
    } else {
      header.classList.remove("fixed-header");
      document.body.style.paddingTop = 0;
    }
  });
}

// 배너 슬라이더 초기화
function initBannerSlider() {
  const banner = document.querySelector(".main-banner");
  if (!banner) return;

  // 배너 변수들
  const bannerSlides = banner.querySelectorAll(".banner-slide");
  const progressBarFill = banner.querySelector(".progress-bar-fill");
  const prevBtn = banner.querySelector(".arrow-left");
  const nextBtn = banner.querySelector(".arrow-right");
  const currentPageSpan = banner.querySelector(".current-page");
  const totalPagesSpan = banner.querySelector(".total-pages");

  let currentSlide = 0;
  const totalSlides = bannerSlides.length;
  let slideInterval;
  const autoSlideDelay = 5000; // 5초 마다 슬라이드
  let progressBarAnimation = null;

  // 페이지 숫자 업데이트
  if (totalPagesSpan) {
    totalPagesSpan.textContent = totalSlides;
  }

  // 진행 바 초기화
  function startProgressBar() {
    // 실행 중인 애니메이션을 취소
    if (progressBarAnimation) {
      cancelAnimationFrame(progressBarAnimation);
    }

    if (progressBarFill) {
      // 진행 상태 초기화
      progressBarFill.style.width = "0%";
      progressBarFill.style.transition = "none";

      // 강제로 reflow 발생시켜 transition이 즉시 적용되게 함
      progressBarFill.offsetHeight;

      // 진행 바 애니메이션 - CSS transition 대신 JavaScript 애니메이션 사용
      progressBarFill.style.transition = `width ${autoSlideDelay}ms linear`;
      progressBarFill.style.width = "100%";
    }
  }

  // 특정 슬라이드를 보여주는 함수
  function showSlide(index) {
    // 모든 슬라이드에서 active 클래스 제거
    bannerSlides.forEach((slide) => slide.classList.remove("active"));

    // 현재 슬라이드에 active 클래스 추가
    currentSlide = (index + totalSlides) % totalSlides;
    bannerSlides[currentSlide].classList.add("active");

    // 현재 페이지 숫자 업데이트
    if (currentPageSpan) {
      currentPageSpan.textContent = currentSlide + 1;
    }

    // 진행 바 초기화 및 시작
    resetAndStartProgressBar();
  }

  // 다음 슬라이드 함수
  function nextSlide() {
    showSlide(currentSlide + 1);
  }

  // 이전 슬라이드 함수
  function prevSlide() {
    showSlide(currentSlide - 1);
  }

  // 진행 바 초기화 및 타이머 시작
  function resetAndStartProgressBar() {
    clearInterval(slideInterval);
    startProgressBar();
    slideInterval = setInterval(nextSlide, autoSlideDelay);
  }

  // 자동 슬라이더 초기화
  resetAndStartProgressBar();
  // 네비게이션 버튼에 대한 이벤트 리스너
  if (prevBtn) {
    prevBtn.addEventListener("click", function (e) {
      e.preventDefault();
      prevSlide();
    });
  }

  if (nextBtn) {
    nextBtn.addEventListener("click", function (e) {
      e.preventDefault();
      nextSlide();
    });
  }

  // 사용자가 슬라이더와 상호작용할 때 자동재생 일시정지
  banner.addEventListener("mouseenter", function () {
    clearInterval(slideInterval);
    if (progressBarFill) {
      const currentWidth = getComputedStyle(progressBarFill).width;
      progressBarFill.style.transition = "none";
      progressBarFill.style.width = currentWidth;
    }
  });

  // 사용자가 슬라이더에서 마우스를 떼면 자동재생 재개
  banner.addEventListener("mouseleave", function () {
    resetAndStartProgressBar();
  });

  // 슬라이더가 화면에 보이는 경우에만 애니메이션 동작
  const observer = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        resetAndStartProgressBar();
      } else {
        clearInterval(slideInterval);
      }
    },
    { threshold: 0.3 }
  );

  observer.observe(banner);

  // 초기 슬라이드 설정
  showSlide(0);

  // 페이지 가시성 변경 감지 (탭 전환 등)
  document.addEventListener("visibilitychange", function () {
    if (document.hidden) {
      clearInterval(slideInterval);
    } else {
      resetAndStartProgressBar();
    }
  });
}

// 농가 슬라이더 초기화
function initFarmSlider() {
  const farmContainer = document.querySelector(".farm-container");
  const farmCards = document.querySelectorAll(".farm-card");
  const arrowLeft = document.querySelector(".farm-slider .arrow-left");
  const arrowRight = document.querySelector(".farm-slider .arrow-right");

  if (!farmContainer || !arrowLeft || !arrowRight) return;

  let currentPosition = 0;
  const cardWidth = farmCards.length > 0 ? farmCards[0].offsetWidth + 20 : 300; // card width + gap
  const visibleCards = Math.floor(farmContainer.offsetWidth / cardWidth);
  const maxPosition = Math.max(0, farmCards.length - visibleCards);

  // 버튼 활성화/비활성화 관리
  function updateArrows() {
    arrowLeft.style.opacity = currentPosition === 0 ? "0.3" : "1";
    arrowRight.style.opacity = currentPosition >= maxPosition ? "0.3" : "1";
  }

  // 슬라이더 이동
  function moveSlider(direction) {
    if (direction === "left" && currentPosition > 0) {
      currentPosition--;
    } else if (direction === "right" && currentPosition < maxPosition) {
      currentPosition++;
    }

    farmContainer.style.transform = `translateX(-${
      currentPosition * cardWidth
    }px)`;
    updateArrows();
  }

  // 이벤트 리스너 설정
  arrowLeft.addEventListener("click", () => moveSlider("left"));
  arrowRight.addEventListener("click", () => moveSlider("right"));

  // 화면 크기 변화에 대응
  window.addEventListener("resize", () => {
    const newVisibleCards = Math.floor(farmContainer.offsetWidth / cardWidth);
    const newMaxPosition = Math.max(0, farmCards.length - newVisibleCards);

    if (currentPosition > newMaxPosition) {
      currentPosition = newMaxPosition;
      farmContainer.style.transform = `translateX(-${
        currentPosition * cardWidth
      }px)`;
    }

    updateArrows();
  });

  // 초기 화살표 상태 설정
  updateArrows();

  // 농가 카드 클릭 이벤트 - 메인 페이지에서는 농가소개 페이지로 이동
  farmCards.forEach((card) => {
    card.addEventListener("click", () => {
      window.location.href = "farm.html";
    });
  });
}

// 로그인 상태 확인 및 메뉴 변경
function checkLoginStatus() {
  // 로컬 스토리지에서 로그인 상태 확인 (실제로는 서버 세션을 확인)
  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
  const userMenu = document.querySelector(".user-menu");

  if (isLoggedIn && userMenu) {
    // 로그인 상태일 때 회원가입 메뉴 숨기고 로그아웃 메뉴 표시
    const menuItems = userMenu.querySelectorAll("a");
    menuItems.forEach((item) => {
      if (item.textContent === "회원가입") {
        item.style.display = "none";
      }
      if (item.textContent === "로그인") {
        item.textContent = "로그아웃";
        item.href = "#logout";
        item.addEventListener("click", function (e) {
          e.preventDefault();
          // 로그아웃 처리
          localStorage.setItem("isLoggedIn", "false");
          window.location.reload();
        });
      }
    });
  }
}

// 상품 클릭 이벤트
function initProductClicks() {
  const productCards = document.querySelectorAll(
    ".product-card, .large-product, .product-item"
  );

  productCards.forEach((card) => {
    // 상품 카드 클릭 시 상세 페이지로 이동
    card.addEventListener("click", function (e) {
      // 장바구니나 찜하기 버튼 클릭은 별도 처리
      if (e.target.closest(".product-actions")) {
        return;
      }
      const productName = this.querySelector(".product-name").textContent;
      window.location.href = `#product-detail/${encodeURIComponent(
        productName
      )}`;
    });
  });

  // "더 보기" 버튼 클릭 시 해당 카테고리 전체 페이지로 이동
  const subtitles = document.querySelectorAll(".subtitle");
  subtitles.forEach((subtitle) => {
    const chevronIcon = subtitle.querySelector(".fa-chevron-right");
    if (chevronIcon) {
      chevronIcon.parentElement.style.cursor = "pointer";
      chevronIcon.parentElement.addEventListener("click", function () {
        const sectionTitle =
          this.closest("section").querySelector(".section-title")
            ?.textContent || this.textContent.split("그린테이블")[0].trim();
        window.location.href = `#${sectionTitle}`;
      });
    }
  });
}

// 장바구니와 찜하기 기능
function initCartWishButtons() {
  // 장바구니 버튼 기능
  const cartButtons = document.querySelectorAll(".cart-btn");
  cartButtons.forEach((button) => {
    button.addEventListener("click", function (e) {
      e.stopPropagation(); // 이벤트 버블링 방지
      const productCard =
        this.closest(".product-card") || this.closest(".large-product");
      const productName =
        productCard.querySelector(".product-name").textContent;

      // 장바구니 데이터를 로컬 스토리지에 저장 (실제로는 서버에 전송)
      let cartItems = JSON.parse(localStorage.getItem("cartItems") || "[]");
      cartItems.push({
        name: productName,
        price: productCard.querySelector(".product-price").textContent,
        image: productCard.querySelector("img").src,
      });
      localStorage.setItem("cartItems", JSON.stringify(cartItems));

      // 사용자에게 피드백 제공
      alert(`${productName}이(가) 장바구니에 추가되었습니다.`);

      // 장바구니 아이콘 애니메이션
      const icon = this.querySelector("i");
      icon.style.transform = "scale(1.5)";
      setTimeout(() => {
        icon.style.transform = "scale(1)";
      }, 300);
    });
  });

  // 찜하기 버튼 기능
  const wishButtons = document.querySelectorAll(".like-btn");
  wishButtons.forEach((button) => {
    button.addEventListener("click", function (e) {
      e.stopPropagation(); // 이벤트 버블링 방지
      const icon = this.querySelector("i");
      const productCard =
        this.closest(".product-card") || this.closest(".large-product");
      const productName =
        productCard.querySelector(".product-name").textContent;

      let wishItems = JSON.parse(localStorage.getItem("wishItems") || "[]");

      // 찜 상태 토글
      if (icon.classList.contains("far")) {
        // 찜 추가
        icon.classList.remove("far");
        icon.classList.add("fas");
        icon.style.color = "#ff6b6b";

        wishItems.push({
          name: productName,
          price: productCard.querySelector(".product-price").textContent,
          image: productCard.querySelector("img").src,
        });

        localStorage.setItem("wishItems", JSON.stringify(wishItems));
      } else {
        // 찜 취소
        icon.classList.remove("fas");
        icon.classList.add("far");
        icon.style.color = "";

        const newWishItems = wishItems.filter(
          (item) => item.name !== productName
        );
        localStorage.setItem("wishItems", JSON.stringify(newWishItems));
      }
    });
  });
}

// 검색 기능
function initSearchFunction() {
  const searchBoxes = document.querySelectorAll(".search-box");

  searchBoxes.forEach((searchBox) => {
    const searchInput = searchBox.querySelector("input");
    const searchButton = searchBox.querySelector("button");

    if (!searchInput || !searchButton) return;

    searchButton.addEventListener("click", function () {
      performSearch(searchInput);
    });

    searchInput.addEventListener("keypress", function (e) {
      if (e.key === "Enter") {
        e.preventDefault();
        performSearch(searchInput);
      }
    });
  });

  function performSearch(input) {
    const searchTerm = input.value.trim();
    if (searchTerm) {
      // 알림으로 검색 결과 표시 (실제 구현 시에는 검색 결과 페이지로 이동)
      alert(`"${searchTerm}" 검색 결과는 준비 중입니다.`);
    } else {
      alert("검색어를 입력해주세요.");
    }
  }
}
