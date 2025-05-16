// Header와 Footer를 불러오는 함수
document.addEventListener("DOMContentLoaded", function () {
  // Header 불러오기
  const headerContainer = document.getElementById("header-container");
  if (headerContainer) {
    fetch("../html/header.html")
      .then((response) => response.text())
      .then((data) => {
        headerContainer.innerHTML = data;
        // 현재 페이지에 맞게 active 클래스 설정
        setActiveMenuClass();
      })
      .catch((error) => console.error("Header 로딩 실패:", error));
  }

  // Footer 불러오기
  const footerContainer = document.getElementById("footer-container");
  if (footerContainer) {
    fetch("../html/footer.html")
      .then((response) => response.text())
      .then((data) => {
        footerContainer.innerHTML = data;
      })
      .catch((error) => console.error("Footer 로딩 실패:", error));
  }
});

// 현재 페이지에 맞게 메뉴에 active 클래스 설정
function setActiveMenuClass() {
  // 현재 페이지 URL 가져오기
  const currentPage = window.location.pathname.split("/").pop();

  // 상단 메뉴 활성화
  const userMenuLinks = document.querySelectorAll(".user-menu a");
  userMenuLinks.forEach((link) => {
    const href = link.getAttribute("href");
    if (href && href.includes(currentPage)) {
      link.classList.add("active");
    }
  });

  // 메인 네비게이션 활성화
  const mainNavLinks = document.querySelectorAll(".main-nav ul li a");
  mainNavLinks.forEach((link) => {
    const href = link.getAttribute("href");
    if (href && href.includes(currentPage)) {
      link.classList.add("active");
    }
  });

  // 검색 기능 초기화
  initSearchFunction();
}

// 검색 기능 초기화
function initSearchFunction() {
  const searchInput = document.getElementById("search-input");
  const searchButton = document.getElementById("search-button");

  if (searchButton && searchInput) {
    searchButton.addEventListener("click", function () {
      performSearch(searchInput.value);
    });

    searchInput.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        performSearch(searchInput.value);
      }
    });
  }
}

// 검색 실행 함수
function performSearch(query) {
  if (!query || query.trim() === "") {
    alert("검색어를 입력해주세요.");
    return;
  }

  // 실제로는 검색 결과 페이지로 이동하거나 API 호출 등이 이루어질 수 있음
  // 현재는 임시로 알림 표시
  alert(`"${query}" 검색 결과는 준비 중입니다.`);
}
