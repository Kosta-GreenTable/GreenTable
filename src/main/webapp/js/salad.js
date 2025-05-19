// 샐러드 상품 페이지 전용 스크립트
document.addEventListener("DOMContentLoaded", function () {
  // 샐러드 상품 관련 추가 기능이 필요한 경우 여기에 구현
  console.log("샐러드 상품 페이지가 로드되었습니다.");

  // 드레싱 옵션 추가
  addDressingOptions();

  // 칼로리 정보 추가
  addNutritionInfo();

  // 상품 정렬 시 초기값 설정(인기순)
  document.querySelector('[data-sort="popular"]').click();
});

// 드레싱 옵션 추가
function addDressingOptions() {
  const productItems = document.querySelectorAll(".product-item");

  // 샐러드 드레싱 정보 (임의의 값)
  const dressingData = {
    "오브랜치드 샐러드": ["프렌치", "요거트", "발사믹"],
    "바질페퍼 샐러드": ["이탈리안", "시저", "발사믹"],
    "참깨두부 샐러드": ["참깨", "오리엔탈", "레몬"],
    "매실간장 샐러드": ["간장", "매실", "유자"],
    "비엔나 페스타 샐러드": ["시저", "허니머스타드", "마요네즈"],
    "단백질가득 샐러드": ["핫칠리", "시저", "오리엔탈"],
    "에그그릴 샐러드": ["마요네즈", "허니머스타드", "시저"],
    "자몽파인 샐러드": ["유자", "발사믹", "오렌지"],
  };

  productItems.forEach((item) => {
    const productName = item.querySelector(".product-name").textContent;

    // 해당 샐러드의 드레싱 옵션이 있는 경우
    if (dressingData[productName]) {
      const dressingOptions = document.createElement("div");
      dressingOptions.classList.add("dressing-options");

      // 드레싱 옵션 스타일 설정
      dressingOptions.style.marginTop = "10px";
      dressingOptions.style.borderTop = "1px solid #eee";
      dressingOptions.style.paddingTop = "10px";

      // 드레싱 제목 추가
      const dressingTitle = document.createElement("div");
      dressingTitle.classList.add("dressing-title");
      dressingTitle.textContent = "드레싱 옵션";
      dressingTitle.style.fontSize = "12px";
      dressingTitle.style.color = "#777";
      dressingTitle.style.marginBottom = "5px";

      // 드레싱 버튼 컨테이너
      const dressingButtonsContainer = document.createElement("div");
      dressingButtonsContainer.classList.add("dressing-buttons");
      dressingButtonsContainer.style.display = "flex";
      dressingButtonsContainer.style.gap = "5px";
      dressingButtonsContainer.style.flexWrap = "wrap";

      // 드레싱 버튼 생성
      dressingData[productName].forEach((dressing, index) => {
        const button = document.createElement("button");
        button.classList.add("dressing-btn");
        button.textContent = dressing;
        button.dataset.dressing = dressing;

        // 첫 번째 드레싱을 기본값으로 활성화
        if (index === 0) {
          button.classList.add("active");
        }

        // 버튼 스타일 설정
        button.style.flex = "1";
        button.style.minWidth = "80px";
        button.style.padding = "5px";
        button.style.fontSize = "12px";
        button.style.border = "1px solid #ddd";
        button.style.borderRadius = "3px";
        button.style.backgroundColor = index === 0 ? "#00c471" : "#f9f9f9";
        button.style.color = index === 0 ? "white" : "#333";
        button.style.cursor = "pointer";

        // 버튼 클릭 이벤트
        button.addEventListener("click", function (e) {
          e.stopPropagation(); // 상품 아이템 클릭 이벤트 전파 방지

          // 활성화 클래스 제거 및 추가
          const buttons =
            dressingButtonsContainer.querySelectorAll(".dressing-btn");
          buttons.forEach((btn) => {
            btn.classList.remove("active");
            btn.style.backgroundColor = "#f9f9f9";
            btn.style.color = "#333";
          });

          this.classList.add("active");
          this.style.backgroundColor = "#00c471";
          this.style.color = "white";
        });

        // 버튼 추가
        dressingButtonsContainer.appendChild(button);
      });

      // 드레싱 옵션 컨테이너에 제목과 버튼 추가
      dressingOptions.appendChild(dressingTitle);
      dressingOptions.appendChild(dressingButtonsContainer);

      // 상품 정보에 드레싱 옵션 추가
      const productInfo = item.querySelector(".product-info");
      productInfo.appendChild(dressingOptions);
    }
  });
}

// 영양 정보 추가
function addNutritionInfo() {
  const productItems = document.querySelectorAll(".product-item");

  // 샐러드 영양 정보 (임의의 값)
  const nutritionData = {
    "오브랜치드 샐러드": { cal: 320, protein: 18, fat: 12 },
    "바질페퍼 샐러드": { cal: 280, protein: 15, fat: 10 },
    "참깨두부 샐러드": { cal: 310, protein: 22, fat: 14 },
    "매실간장 샐러드": { cal: 260, protein: 16, fat: 8 },
    "비엔나 페스타 샐러드": { cal: 350, protein: 20, fat: 15 },
    "단백질가득 샐러드": { cal: 330, protein: 28, fat: 12 },
    "에그그릴 샐러드": { cal: 290, protein: 19, fat: 12 },
    "자몽파인 샐러드": { cal: 240, protein: 8, fat: 6 },
  };

  productItems.forEach((item) => {
    const productName = item.querySelector(".product-name").textContent;

    // 해당 샐러드의 영양 정보가 있는 경우
    if (nutritionData[productName]) {
      const nutritionInfo = document.createElement("div");
      nutritionInfo.classList.add("nutrition-info");

      const { cal, protein, fat } = nutritionData[productName];

      // 영양 정보 내용 설정
      nutritionInfo.innerHTML = `
                <span class="nutrition-item">
                    <span class="nutrition-value">${cal}</span>
                    <span class="nutrition-unit">kcal</span>
                </span>
                <span class="nutrition-divider">|</span>
                <span class="nutrition-item">
                    <span class="nutrition-value">${protein}g</span>
                    <span class="nutrition-label">단백질</span>
                </span>
                <span class="nutrition-divider">|</span>
                <span class="nutrition-item">
                    <span class="nutrition-value">${fat}g</span>
                    <span class="nutrition-label">지방</span>
                </span>
            `;

      // 영양 정보 스타일 설정
      nutritionInfo.style.display = "flex";
      nutritionInfo.style.alignItems = "center";
      nutritionInfo.style.marginTop = "8px";
      nutritionInfo.style.padding = "5px 8px";
      nutritionInfo.style.backgroundColor = "#f1f8e9";
      nutritionInfo.style.borderRadius = "3px";
      nutritionInfo.style.fontSize = "11px";
      nutritionInfo.style.color = "#689f38";

      // 칼로리 값에 따라 색상 변경
      if (cal < 280) {
        nutritionInfo.style.backgroundColor = "#e8f5e9";
        nutritionInfo.style.color = "#2e7d32";
      } else if (cal > 320) {
        nutritionInfo.style.backgroundColor = "#fbe9e7";
        nutritionInfo.style.color = "#d84315";
      }

      // 영양 정보 아이템 스타일
      const nutritionItems = nutritionInfo.querySelectorAll(".nutrition-item");
      nutritionItems.forEach((item) => {
        item.style.display = "flex";
        item.style.flexDirection = "column";
        item.style.alignItems = "center";
        item.style.margin = "0 5px";
      });

      // 영양 정보 값 스타일
      const nutritionValues =
        nutritionInfo.querySelectorAll(".nutrition-value");
      nutritionValues.forEach((value) => {
        value.style.fontWeight = "bold";
        value.style.fontSize = "13px";
      });

      // 영양 정보 라벨 스타일
      const nutritionLabels = nutritionInfo.querySelectorAll(
        ".nutrition-label, .nutrition-unit"
      );
      nutritionLabels.forEach((label) => {
        label.style.fontSize = "10px";
        label.style.opacity = "0.8";
      });

      // 영양 정보 구분선 스타일
      const nutritionDividers =
        nutritionInfo.querySelectorAll(".nutrition-divider");
      nutritionDividers.forEach((divider) => {
        divider.style.margin = "0 5px";
        divider.style.opacity = "0.5";
        divider.style.fontSize = "10px";
      });

      // 상품 평점 앞에 영양 정보 추가
      const productRating = item.querySelector(".product-rating");
      productRating.parentNode.insertBefore(nutritionInfo, productRating);
    }
  });
}
