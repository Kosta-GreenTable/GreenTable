// 주문 페이지 스크립트
document.addEventListener("DOMContentLoaded", function () {
  // 주문 상품 정보를 세션 스토리지에서 로드
  let orderItems = [];
  try {
    const storedItems = sessionStorage.getItem("orderItems");
    if (storedItems) {
      orderItems = JSON.parse(storedItems);
    }
  } catch (e) {
    console.error("Error loading order items:", e);
  }

  // 상품이 없으면 장바구니 페이지로 리디렉션
  if (orderItems.length === 0) {
    // alert('주문할 상품이 없습니다.');
    // window.location.href = 'cart.html';

    // 테스트용 더미 데이터
    orderItems = [
      {
        id: 1,
        name: "그릭 샐러드",
        image: "https://picsum.photos/seed/salad1/200/200",
        price: 6900,
        quantity: 1,
        options: {
          size: "small",
          topping: "none",
          dressing: "olive",
        },
      },
      {
        id: 2,
        name: "카프레제 샐러드",
        image: "https://picsum.photos/seed/salad2/200/200",
        price: 8900,
        quantity: 1,
        options: {
          size: "small",
          topping: "chicken",
          dressing: "balsamic",
        },
      },
    ];
  }

  // 금액 포맷 함수
  function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
  }

  // 주문 상품 렌더링
  function renderOrderItems() {
    const orderItemContainer = document.getElementById("order-items");
    const orderItemCount = document.getElementById("order-item-count");

    // 상품 개수 표시
    orderItemCount.textContent = `${orderItems.length}개`;

    // 주문 상품 렌더링
    let itemsHTML = "";
    orderItems.forEach((item) => {
      // 옵션 텍스트 생성
      let optionsText = [];

      // 사이즈 옵션
      if (item.options.size === "medium") {
        optionsText.push("Medium (+1,000원)");
      } else if (item.options.size === "large") {
        optionsText.push("Large (+2,000원)");
      } else {
        optionsText.push("Small");
      }

      // 토핑 옵션
      if (item.options.topping === "chicken") {
        optionsText.push("닭가슴살 (+2,500원)");
      } else if (item.options.topping === "bacon") {
        optionsText.push("베이컨 (+1,500원)");
      } else if (item.options.topping === "avocado") {
        optionsText.push("아보카도 (+2,000원)");
      }

      // 드레싱 옵션
      if (item.options.dressing === "olive") {
        optionsText.push("올리브 오일 드레싱");
      } else if (item.options.dressing === "balsamic") {
        optionsText.push("발사믹 드레싱");
      } else if (item.options.dressing === "yogurt") {
        optionsText.push("요거트 드레싱");
      } else if (item.options.dressing === "caesar") {
        optionsText.push("시저 드레싱");
      }

      itemsHTML += `
                <div class="product-item">
                    <div class="product-image">
                        <img src="${item.image}" alt="${item.name}">
                    </div>
                    <div class="product-details">
                        <div class="product-name">${item.name}</div>
                        <div class="product-options">${optionsText.join(
                          " / "
                        )}</div>
                        <div class="product-price">${formatPrice(
                          item.price
                        )}</div>
                        <div class="product-quantity">수량: ${
                          item.quantity
                        }개</div>
                    </div>
                </div>
            `;
    });

    orderItemContainer.innerHTML = itemsHTML;

    // 가격 정보 업데이트
    updatePriceInfo();
  }

  // 가격 정보 업데이트
  function updatePriceInfo() {
    let totalProductPrice = 0;
    let shippingCost = 3000;
    let couponDiscount = 0;
    let pointDiscount = 0;

    // 상품 총액 계산
    orderItems.forEach((item) => {
      totalProductPrice += item.price * item.quantity;
    });

    // 화면에 표시
    document.getElementById("total-product-price").textContent =
      formatPrice(totalProductPrice);
    document.getElementById("shipping-cost").textContent =
      formatPrice(shippingCost);
    document.getElementById("coupon-discount").textContent =
      "-" + formatPrice(couponDiscount);
    document.getElementById("point-discount").textContent =
      "-" + formatPrice(pointDiscount);

    // 최종 가격 계산
    const finalPrice =
      totalProductPrice + shippingCost - couponDiscount - pointDiscount;
    document.getElementById("final-price").textContent =
      formatPrice(finalPrice);

    // 세션 스토리지에 결제 정보 저장
    sessionStorage.setItem(
      "paymentInfo",
      JSON.stringify({
        totalProductPrice,
        shippingCost,
        couponDiscount,
        pointDiscount,
        finalPrice,
      })
    );
  }

  // 주문자 정보와 동일 체크박스 이벤트
  const sameAsOrdererCheckbox = document.getElementById("sameAsOrderer");
  if (sameAsOrdererCheckbox) {
    sameAsOrdererCheckbox.addEventListener("change", function () {
      if (this.checked) {
        // 주문자 정보 가져오기
        const name = document.getElementById("name").value;
        const phonePrefix = document.getElementById("phone-prefix").value;
        const phoneMiddle = document.getElementById("phone-middle").value;
        const phoneLast = document.getElementById("phone-last").value;

        // 배송지 정보에 적용
        document.getElementById("recipient").value = name;
        document.getElementById("recipient-phone-prefix").value = phonePrefix;
        document.getElementById("recipient-phone-middle").value = phoneMiddle;
        document.getElementById("recipient-phone-last").value = phoneLast;
      } else {
        // 체크 해제 시 배송지 정보 초기화
        document.getElementById("recipient").value = "";
        document.getElementById("recipient-phone-middle").value = "";
        document.getElementById("recipient-phone-last").value = "";
      }
    });
  }

  // 이메일 도메인 선택 이벤트
  const emailDomainSelect = document.getElementById("email-domain");
  if (emailDomainSelect) {
    emailDomainSelect.addEventListener("change", function () {
      const emailDomain = document.querySelector('input[name="email2"]');
      if (this.value) {
        emailDomain.value = this.value;
        emailDomain.readOnly = true;
      } else {
        emailDomain.value = "";
        emailDomain.readOnly = false;
        emailDomain.focus();
      }
    });
  }

  // 배송 메모 직접 입력 옵션
  const deliveryMemoSelect = document.getElementById("delivery-memo");
  const customMemoInput = document.getElementById("custom-memo");

  if (deliveryMemoSelect && customMemoInput) {
    deliveryMemoSelect.addEventListener("change", function () {
      if (this.value === "custom") {
        customMemoInput.style.display = "block";
      } else {
        customMemoInput.style.display = "none";
      }
    });
  }

  // 쿠폰 모달
  const couponModal = document.getElementById("coupon-modal");
  const showCouponsButton = document.getElementById("show-coupons");
  const couponCancelButton = document.getElementById("coupon-cancel");
  const couponConfirmButton = document.getElementById("coupon-confirm");
  const closeModalBtn = document.querySelector("#coupon-modal .close-modal");

  if (showCouponsButton) {
    showCouponsButton.addEventListener("click", function () {
      couponModal.style.display = "block";
    });
  }

  if (couponCancelButton) {
    couponCancelButton.addEventListener("click", function () {
      couponModal.style.display = "none";
    });
  }

  if (closeModalBtn) {
    closeModalBtn.addEventListener("click", function () {
      couponModal.style.display = "none";
    });
  }

  // 쿠폰 적용하기
  if (couponConfirmButton) {
    couponConfirmButton.addEventListener("click", function () {
      const selectedCoupons = document.querySelectorAll(
        ".coupon-checkbox:checked"
      );
      let couponDiscount = 0;

      // 쿠폰 할인 계산
      selectedCoupons.forEach((coupon) => {
        const couponId = coupon.id;
        if (couponId === "coupon-1") {
          // 10% 할인 쿠폰 (최대 5,000원)
          let totalProductPrice = 0;
          orderItems.forEach((item) => {
            totalProductPrice += item.price * item.quantity;
          });

          couponDiscount += Math.min(totalProductPrice * 0.1, 5000);
        } else if (couponId === "coupon-2") {
          // 2,000원 할인 쿠폰
          couponDiscount += 2000;
        }
      });

      // 쿠폰 할인 적용
      document.getElementById("coupon-discount").textContent =
        "-" + formatPrice(couponDiscount);

      // 최종 가격 업데이트
      updateFinalPrice();

      // 모달 닫기
      couponModal.style.display = "none";
    });
  }

  // 포인트 전액 사용 버튼
  const useAllPointsButton = document.getElementById("use-all-points");
  if (useAllPointsButton) {
    useAllPointsButton.addEventListener("click", function () {
      const pointInput = document.getElementById("point");
      pointInput.value = "5000"; // 보유 포인트 5,000 적용

      // 포인트 적용
      document.getElementById("point-discount").textContent =
        "-" + formatPrice(5000);

      // 최종 가격 업데이트
      updateFinalPrice();
    });
  }

  // 포인트 입력 이벤트
  const pointInput = document.getElementById("point");
  if (pointInput) {
    pointInput.addEventListener("change", function () {
      let points = parseInt(this.value);
      if (isNaN(points) || points < 0) {
        points = 0;
        this.value = "";
      } else if (points > 5000) {
        // 최대 5,000 포인트
        points = 5000;
        this.value = "5000";
      }

      // 포인트 적용
      document.getElementById("point-discount").textContent =
        "-" + formatPrice(points);

      // 최종 가격 업데이트
      updateFinalPrice();
    });
  }

  // 결제 수단 선택 이벤트
  const paymentOptions = document.querySelectorAll(".payment-option");
  if (paymentOptions) {
    paymentOptions.forEach((option) => {
      option.addEventListener("click", function () {
        // 기존 선택 해제
        paymentOptions.forEach((opt) => opt.classList.remove("selected"));
        // 현재 선택된 결제 수단 표시
        this.classList.add("selected");

        // 선택된 결제 수단 저장
        const paymentMethod = this.getAttribute("data-method");
        sessionStorage.setItem("paymentMethod", paymentMethod);
      });
    });
  }

  // 최종 가격 업데이트 함수
  function updateFinalPrice() {
    let totalProductPrice = 0;
    orderItems.forEach((item) => {
      totalProductPrice += item.price * item.quantity;
    });

    const shippingCost = 3000;
    const couponDiscountText =
      document.getElementById("coupon-discount").textContent;
    const couponDiscount =
      parseInt(couponDiscountText.replace(/[^0-9]/g, "")) || 0;
    const pointDiscountText =
      document.getElementById("point-discount").textContent;
    const pointDiscount =
      parseInt(pointDiscountText.replace(/[^0-9]/g, "")) || 0;

    const finalPrice =
      totalProductPrice + shippingCost - couponDiscount - pointDiscount;
    document.getElementById("final-price").textContent =
      formatPrice(finalPrice);

    // 결제 정보 저장
    sessionStorage.setItem(
      "paymentInfo",
      JSON.stringify({
        totalProductPrice,
        shippingCost,
        couponDiscount,
        pointDiscount,
        finalPrice,
        orderDate: new Date().toISOString(),
      })
    );
  }

  // 결제하기 버튼 이벤트
  const payButton = document.getElementById("pay-button");
  if (payButton) {
    payButton.addEventListener("click", function () {
      // 필수 입력값 검사
      const name = document.getElementById("name").value;
      const email = document.getElementById("email").value;
      const phoneMiddle = document.getElementById("phone-middle").value;
      const phoneLast = document.getElementById("phone-last").value;
      const password = document.getElementById("password").value;
      const passwordConfirm = document.getElementById("password-confirm").value;
      const recipient = document.getElementById("recipient").value;
      const zipCode = document.getElementById("zip-code").value;
      const address1 = document.getElementById("address1").value;
      const address2 = document.getElementById("address2").value;

      // 결제 수단 선택 여부
      const selectedPayment = document.querySelector(
        ".payment-option.selected"
      );

      // 약관 동의 여부
      const termsAgree = document.getElementById("terms-agree").checked;
      const privacyAgree = document.getElementById("privacy-agree").checked;

      // 유효성 검사
      if (!name) {
        alert("주문자 이름을 입력해주세요.");
        return;
      }
      if (!email) {
        alert("이메일을 입력해주세요.");
        return;
      }
      if (!phoneMiddle || !phoneLast) {
        alert("휴대폰번호를 입력해주세요.");
        return;
      }
      if (!password) {
        alert("주문 비밀번호를 입력해주세요.");
        return;
      }
      if (password !== passwordConfirm) {
        alert("주문 비밀번호가 일치하지 않습니다.");
        return;
      }
      if (!recipient) {
        alert("받는 분 이름을 입력해주세요.");
        return;
      }
      if (!zipCode || !address1 || !address2) {
        alert("주소를 입력해주세요.");
        return;
      }
      if (!selectedPayment) {
        alert("결제수단을 선택해주세요.");
        return;
      }
      if (!termsAgree || !privacyAgree) {
        alert("필수 약관에 동의해주세요.");
        return;
      }

      // 모든 유효성 검사 통과 시
      // 주문 번호 생성 - format: YYYYMMDDXXXX (연월일+4자리 번호)
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, "0");
      const day = String(now.getDate()).padStart(2, "0");
      const randomNum = Math.floor(Math.random() * 10000)
        .toString()
        .padStart(4, "0");
      const orderNumber = `${year}${month}${day}${randomNum}`;

      // 주문 번호 저장
      sessionStorage.setItem("orderNumber", orderNumber);

      // 주문 완료 페이지로 이동
      window.location.href = "orderSuccess.html";
    });
  }

  // 취소 버튼 이벤트
  const cancelButton = document.getElementById("cancel-button");
  if (cancelButton) {
    cancelButton.addEventListener("click", function () {
      if (confirm("주문을 취소하시겠습니까?")) {
        window.location.href = "cart.html";
      }
    });
  }

  // 우편번호 검색 버튼 이벤트
  const findAddressButton = document.getElementById("find-address");
  if (findAddressButton) {
    findAddressButton.addEventListener("click", function () {
      // 테스트용 주소 데이터 설정
      document.getElementById("zip-code").value = "12345";
      document.getElementById("address1").value = "서울시 강남구 테헤란로 123";
      document.getElementById("address2").focus();
      alert("실제 환경에서는 Daum 우편번호 검색 API를 통해 주소를 검색합니다.");
    });
  }

  // 초기 렌더링
  renderOrderItems();
});
