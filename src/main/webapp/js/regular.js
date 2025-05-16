// 정기배송 상품 페이지 전용 스크립트
document.addEventListener("DOMContentLoaded", function () {
  // 정기배송 상품 관련 추가 기능이 필요한 경우 여기에 구현
  console.log("정기배송 상품 페이지가 로드되었습니다.");

  // 정기배송 상품 배지 추가
  addSubscriptionBadges();

  // 구독 주기 선택 버튼 초기화
  initSubscriptionButtons();

  // 상품 정렬 시 초기값 설정(인기순)
  document.querySelector('[data-sort="popular"]').click();
});

// 정기배송 상품 배지 추가
function addSubscriptionBadges() {
  const productItems = document.querySelectorAll(".product-item");

  productItems.forEach((item) => {
    const badge = document.createElement("div");
    badge.classList.add("subscription-badge");
    badge.textContent = "정기배송";

    // 배지 스타일 설정
    badge.style.position = "absolute";
    badge.style.top = "10px";
    badge.style.left = "10px";
    badge.style.backgroundColor = "#4caf50";
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
  });
}

// 구독 주기 선택 버튼 초기화
function initSubscriptionButtons() {
  const productItems = document.querySelectorAll(".product-item");

  productItems.forEach((item) => {
    const subscriptionOptions = document.createElement("div");
    subscriptionOptions.classList.add("subscription-options");
    subscriptionOptions.innerHTML = `
            <div class="subscription-title">구독 주기</div>
            <div class="subscription-buttons">
                <button class="subscription-btn active" data-period="weekly">주 1회</button>
                <button class="subscription-btn" data-period="biweekly">2주 1회</button>
                <button class="subscription-btn" data-period="monthly">월 1회</button>
            </div>
        `;

    // 구독 옵션 스타일 설정
    subscriptionOptions.style.marginTop = "10px";
    subscriptionOptions.style.borderTop = "1px solid #eee";
    subscriptionOptions.style.paddingTop = "10px";

    const subscriptionTitle = subscriptionOptions.querySelector(
      ".subscription-title"
    );
    subscriptionTitle.style.fontSize = "12px";
    subscriptionTitle.style.color = "#777";
    subscriptionTitle.style.marginBottom = "5px";

    const subscriptionButtons = subscriptionOptions.querySelector(
      ".subscription-buttons"
    );
    subscriptionButtons.style.display = "flex";
    subscriptionButtons.style.gap = "5px";

    const buttons = subscriptionOptions.querySelectorAll(".subscription-btn");
    buttons.forEach((button) => {
      button.style.flex = "1";
      button.style.padding = "5px";
      button.style.fontSize = "12px";
      button.style.border = "1px solid #ddd";
      button.style.borderRadius = "3px";
      button.style.backgroundColor = "#f9f9f9";
      button.style.cursor = "pointer";

      // 버튼 클릭 이벤트
      button.addEventListener("click", function (e) {
        e.stopPropagation(); // 상품 아이템 클릭 이벤트 전파 방지

        // 활성화 클래스 제거 및 추가
        buttons.forEach((btn) => {
          btn.classList.remove("active");
          btn.style.backgroundColor = "#f9f9f9";
          btn.style.color = "#333";
        });

        this.classList.add("active");
        this.style.backgroundColor = "#4caf50";
        this.style.color = "white";
      });
    });

    // 기본 활성화 버튼 스타일 적용
    const activeButton = subscriptionOptions.querySelector(
      ".subscription-btn.active"
    );
    if (activeButton) {
      activeButton.style.backgroundColor = "#4caf50";
      activeButton.style.color = "white";
    }

    // 상품 정보에 구독 옵션 추가
    const productInfo = item.querySelector(".product-info");
    productInfo.appendChild(subscriptionOptions);
  });
}
