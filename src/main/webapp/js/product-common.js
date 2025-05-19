// 상품 페이지 공통 자바스크립트 함수
document.addEventListener("DOMContentLoaded", function () {
  // 헤더와 푸터 로드
  loadHeaderAndFooter();

  // 정렬 버튼 초기화
  initSortButtons();

  // 페이지네이션 초기화
  initPagination();

  // 상품 아이템 초기화 (클릭 이벤트)
  initProductItems();
});

// 헤더와 푸터 로드 함수
function loadHeaderAndFooter() {
  // 헤더와 푸터는 모두 직접 HTML에 포함되어 있으므로 로드하지 않음
  // 헤더 관련 이벤트 초기화만 수행
  initHeaderEvents();
}

// 헤더 이벤트 초기화
function initHeaderEvents() {
  // 검색 버튼
  const searchBtn = document.querySelector(".search-box button");
  if (searchBtn) {
    searchBtn.addEventListener("click", function () {
      const searchInput = document.querySelector(".search-box input");
      const searchTerm = searchInput.value.trim();

      if (searchTerm) {
        alert("검색어: " + searchTerm + " (검색 기능 구현 예정)");
      } else {
        alert("검색어를 입력해주세요.");
      }
    });
  }
}

// 현재 카테고리 하이라이트
function highlightCurrentCategory(category) {
  setTimeout(() => {
    const menuItems = document.querySelectorAll(".main-nav > ul > li > a");
    menuItems.forEach((item) => {
      if (item.textContent === category) {
        item.parentElement.classList.add("active");
      }
    });
  }, 100);
}

// 정렬 버튼 초기화
function initSortButtons() {
  const sortButtons = document.querySelectorAll(".product-sort button");

  sortButtons.forEach((button) => {
    button.addEventListener("click", function () {
      // 활성 클래스 제거 및 추가
      sortButtons.forEach((btn) => btn.classList.remove("active"));
      this.classList.add("active");

      // 정렬 로직
      const sortType = this.dataset.sort;
      sortProducts(sortType);
    });
  });

  // 기본 정렬 (인기순)
  document.querySelector('[data-sort="popular"]').classList.add("active");
}

// 상품 정렬 함수
function sortProducts(sortType) {
  const productGrid = document.querySelector(".product-grid");
  const products = Array.from(document.querySelectorAll(".product-item"));

  products.sort((a, b) => {
    switch (sortType) {
      case "popular": // 인기순
        return parseInt(b.dataset.popularity) - parseInt(a.dataset.popularity);

      case "newest": // 최신순
        return parseInt(b.dataset.date) - parseInt(a.dataset.date);

      case "priceAsc": // 낮은가격순
        return parseInt(a.dataset.price) - parseInt(b.dataset.price);

      case "priceDesc": // 높은가격순
        return parseInt(b.dataset.price) - parseInt(a.dataset.price);

      case "rating": // 평점순
        return parseFloat(b.dataset.rating) - parseFloat(a.dataset.rating);

      default:
        return 0;
    }
  });

  // 정렬된 상품을 다시 그리드에 추가
  productGrid.innerHTML = "";
  products.forEach((product) => {
    productGrid.appendChild(product);
  });

  // 현재 페이지 번호 저장
  const currentPage = parseInt(
    document.querySelector(".pagination-item.active button").textContent
  );

  // 페이지네이션에 따라 상품 표시/숨김
  updateProductVisibility(currentPage);
}

// 페이지네이션 초기화
function initPagination() {
  const paginationItems = document.querySelectorAll(".pagination-item");
  const prevButton = document.querySelector(".pagination-prev button");
  const nextButton = document.querySelector(".pagination-next button");

  // 페이지 버튼 클릭 이벤트
  paginationItems.forEach((item) => {
    item.addEventListener("click", function () {
      const pageNumber = parseInt(this.querySelector("button").textContent);
      changePage(pageNumber);
    });
  });

  // 이전 페이지 버튼
  if (prevButton) {
    prevButton.addEventListener("click", function () {
      const activePage = document.querySelector(".pagination-item.active");
      const pageNumber = parseInt(
        activePage.querySelector("button").textContent
      );

      if (pageNumber > 1) {
        changePage(pageNumber - 1);
      }
    });
  }

  // 다음 페이지 버튼
  if (nextButton) {
    nextButton.addEventListener("click", function () {
      const activePage = document.querySelector(".pagination-item.active");
      const pageNumber = parseInt(
        activePage.querySelector("button").textContent
      );
      const totalPages = document.querySelectorAll(".pagination-item").length;

      if (pageNumber < totalPages) {
        changePage(pageNumber + 1);
      }
    });
  }

  // 첫 페이지 활성화
  changePage(1);
}

// 페이지 변경 함수
function changePage(pageNumber) {
  // 활성 페이지 표시
  const paginationItems = document.querySelectorAll(".pagination-item");
  paginationItems.forEach((item) => {
    const itemPage = parseInt(item.querySelector("button").textContent);
    if (itemPage === pageNumber) {
      item.classList.add("active");
    } else {
      item.classList.remove("active");
    }
  });

  // 상품 표시/숨김 업데이트
  updateProductVisibility(pageNumber);
}

// 현재 페이지에 따라 상품 표시/숨김
function updateProductVisibility(pageNumber) {
  const productsPerPage = 8;
  const startIndex = (pageNumber - 1) * productsPerPage;
  const endIndex = startIndex + productsPerPage;

  const products = document.querySelectorAll(".product-item");
  products.forEach((product, index) => {
    if (index >= startIndex && index < endIndex) {
      product.style.display = "block";
    } else {
      product.style.display = "none";
    }
  });
}

// 상품 아이템 초기화
function initProductItems() {
  const productItems = document.querySelectorAll(".product-item");

  productItems.forEach((item) => {
    item.addEventListener("click", function () {
      const productId = this.dataset.id;

      // 상품 상세 페이지로 이동
      window.location.href = `product-detail.html?id=${productId}`;
    });
  });
}

// 카테고리 배너 클릭 이벤트
function initCategoryBanner() {
  const categoryBanner = document.querySelector(".category-banner");
  if (categoryBanner) {
    categoryBanner.addEventListener("click", function () {
      // 현재 페이지의 첫 번째 페이지로 이동
      const currentUrl = window.location.href.split("?")[0];
      window.location.href = currentUrl;
    });
  }
}
