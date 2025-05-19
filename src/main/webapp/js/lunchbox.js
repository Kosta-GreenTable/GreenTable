// 도시락 상품 페이지 전용 스크립트
document.addEventListener("DOMContentLoaded", function () {
  // 도시락 상품 관련 추가 기능이 필요한 경우 여기에 구현
  console.log("도시락 상품 페이지가 로드되었습니다.");

  // 도시락 상품 배지 추가
  addLunchboxBadges();

  // 칼로리 정보 추가
  addCalorieInfo();

  // 상품 정렬 시 초기값 설정(인기순)
  document.querySelector('[data-sort="popular"]').click();
});

// 도시락 상품 배지 추가
function addLunchboxBadges() {
  const productItems = document.querySelectorAll(".product-item");

  productItems.forEach((item) => {
    // 칼로리가 낮은 상품에는 '저칼로리' 배지 추가
    if (item.querySelector(".product-name").textContent.includes("저칼로리")) {
      const badge = document.createElement("div");
      badge.classList.add("calorie-badge");
      badge.textContent = "저칼로리";

      // 배지 스타일 설정
      badge.style.position = "absolute";
      badge.style.top = "10px";
      badge.style.left = "10px";
      badge.style.backgroundColor = "#42a5f5";
      badge.style.color = "white";
      badge.style.padding = "5px 10px";
      badge.style.borderRadius = "3px";
      badge.style.fontSize = "12px";
      badge.style.fontWeight = "bold";
      badge.style.zIndex = "1";

      // 이미지 컨테이너에 위치 상대값 설정
      const imageContainer = item.querySelector(".product-image");
      imageContainer.style.position = "relative";

      // 배지 추가
      imageContainer.appendChild(badge);
    }
  });
}

// 칼로리 정보 추가
function addCalorieInfo() {
  const productItems = document.querySelectorAll(".product-item");

  // 도시락 상품별 칼로리 정보 (임의의 값)
  const calorieData = {
    "단백질 도시락": "450kcal",
    "한끼 영양 도시락": "520kcal",
    "채소 가득 도시락": "380kcal",
    "닭가슴살 도시락": "420kcal",
    "저칼로리 도시락": "300kcal",
    "일품 한식 도시락": "550kcal",
    "어린이 영양 도시락": "480kcal",
    "아침 식사 도시락": "350kcal",
  };

  productItems.forEach((item) => {
    const productName = item.querySelector(".product-name").textContent;

    // 해당 상품의 칼로리 정보가 있는 경우
    if (calorieData[productName]) {
      const calorieInfo = document.createElement("div");
      calorieInfo.classList.add("calorie-info");

      // 칼로리 정보 스타일 설정
      calorieInfo.style.display = "inline-block";
      calorieInfo.style.marginTop = "5px";
      calorieInfo.style.padding = "3px 8px";
      calorieInfo.style.backgroundColor = "#f1f8e9";
      calorieInfo.style.borderRadius = "3px";
      calorieInfo.style.fontSize = "12px";
      calorieInfo.style.color = "#689f38";

      // 칼로리 값에 따라 색상 변경
      const calorieValue = parseInt(calorieData[productName]);
      if (calorieValue < 400) {
        calorieInfo.style.backgroundColor = "#e8f5e9";
        calorieInfo.style.color = "#2e7d32";
      } else if (calorieValue > 500) {
        calorieInfo.style.backgroundColor = "#fbe9e7";
        calorieInfo.style.color = "#d84315";
      }

      calorieInfo.textContent = calorieData[productName];

      // 상품 설명 뒤에 칼로리 정보 추가
      const productDesc = item.querySelector(".product-desc");
      productDesc.parentNode.insertBefore(calorieInfo, productDesc.nextSibling);
    }
  });
}
