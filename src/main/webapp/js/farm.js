document.addEventListener("DOMContentLoaded", function () {
  // 농가 카드 클릭 이벤트
  const farmCards = document.querySelectorAll(".farm-card");
  farmCards.forEach((card) => {
    card.addEventListener("click", function () {
      this.classList.toggle("expanded");
    });
  });

  // 농가 필터링 기능
  const filterButtons = document.querySelectorAll(".farm-filter-btn");
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

  // 애니메이션 효과
  window.addEventListener("scroll", function () {
    const fadeElements = document.querySelectorAll(".fade-in");
    fadeElements.forEach((element) => {
      const elementPosition = element.getBoundingClientRect().top;
      const screenPosition = window.innerHeight / 1.3;

      if (elementPosition < screenPosition) {
        element.classList.add("visible");
      }
    });
  });

  // 페이지 로드 완료 후 로딩 애니메이션
  document.body.classList.add("loaded");
});
