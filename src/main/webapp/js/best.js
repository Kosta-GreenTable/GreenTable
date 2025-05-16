// 베스트 상품 페이지 전용 스크립트
document.addEventListener("DOMContentLoaded", function () {
  // 베스트 상품 관련 추가 기능이 필요한 경우 여기에 구현
  console.log("베스트 상품 페이지가 로드되었습니다.");

  // 베스트 상품 배지 추가
  addBestBadges();

  // 상품 정렬 시 초기값 설정(인기순)
  document.querySelector('[data-sort="popular"]').click();
});

// 베스트 상품 배지 추가
function addBestBadges() {
  const productItems = document.querySelectorAll(".product-item");

  // 상위 3개 상품에 베스트 배지 추가
  for (let i = 0; i < 3 && i < productItems.length; i++) {
    const badge = document.createElement("div");
    badge.classList.add("best-badge");
    badge.textContent = `BEST ${i + 1}`;

    // 배지 스타일 설정
    badge.style.position = "absolute";
    badge.style.top = "10px";
    badge.style.left = "10px";
    badge.style.backgroundColor = "#ff6b6b";
    badge.style.color = "white";
    badge.style.padding = "5px 10px";
    badge.style.borderRadius = "3px";
    badge.style.fontSize = "12px";
    badge.style.fontWeight = "bold";
    badge.style.zIndex = "1";

    // 이미지 컨테이너에 위치 상대값 설정
    const imageContainer = productItems[i].querySelector(".product-image");
    imageContainer.style.position = "relative";

    // 배지 추가
    imageContainer.appendChild(badge);
  }
}
