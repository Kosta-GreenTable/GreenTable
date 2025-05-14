// 장바구니 관리 스크립트
document.addEventListener("DOMContentLoaded", function () {
  // 장바구니 아이템 데이터 (실제로는 API나 localStorage에서 가져옵니다)
  const cartItems = [
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
    {
      id: 3,
      name: "시저 샐러드",
      image: "https://picsum.photos/seed/salad3/200/200",
      price: 7900,
      quantity: 1,
      options: {
        size: "medium",
        topping: "none",
        dressing: "caesar",
      },
    },
  ];

  // 금액 포맷 함수
  function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
  }

  // 장바구니 아이템 렌더링
  function renderCartItems() {
    const cartItemsElement = document.getElementById("cart-items");

    if (cartItems.length === 0) {
      cartItemsElement.innerHTML = `
        <tr>
          <td colspan="5">
            <div class="empty-cart">
              <i class="fas fa-shopping-cart"></i>
              <p>장바구니에 상품이 없습니다.</p>
              <button onclick="location.href='index.html'">쇼핑 시작하기</button>
            </div>
          </td>
        </tr>
      `;
      return;
    }

    let cartHtml = "";

    cartItems.forEach((item) => {
      cartHtml += `
        <tr data-id="${item.id}">
          <td><input type="checkbox" class="item-checkbox" checked></td>
          <td class="product-info">
            <img src="${item.image}" alt="${item.name}">
            <div>
              <p>${item.name}</p>
              <button class="option-btn" data-id="${item.id}">옵션 변경</button>
            </div>
          </td>
          <td>
            <div class="quantity-cell">
              <button class="quantity-btn minus" data-id="${item.id}">-</button>
              <input type="text" value="${
                item.quantity
              }" class="quantity-input" data-id="${item.id}">
              <button class="quantity-btn plus" data-id="${item.id}">+</button>
            </div>
          </td>
          <td class="item-price">${formatPrice(item.price * item.quantity)}</td>
          <td>
            <div class="action-buttons">
              <button class="order-btn" data-id="${item.id}">주문</button>
              <button class="wishlist-btn" data-id="${
                item.id
              }">관심상품</button>
              <button class="delete-btn" data-id="${item.id}">삭제</button>
            </div>
          </td>
        </tr>
      `;
    });

    cartItemsElement.innerHTML = cartHtml;

    // 이벤트 리스너 추가
    addEventListeners();
    updateTotalPrice();
  }

  // 이벤트 리스너 추가
  function addEventListeners() {
    // 수량 증가 버튼
    document.querySelectorAll(".quantity-btn.plus").forEach((button) => {
      button.addEventListener("click", function () {
        const id = parseInt(this.getAttribute("data-id"));
        const item = cartItems.find((item) => item.id === id);
        if (item) {
          item.quantity++;
          document.querySelector(`.quantity-input[data-id="${id}"]`).value =
            item.quantity;
          updateItemPrice(id);
          updateTotalPrice();
        }
      });
    });

    // 수량 감소 버튼
    document.querySelectorAll(".quantity-btn.minus").forEach((button) => {
      button.addEventListener("click", function () {
        const id = parseInt(this.getAttribute("data-id"));
        const item = cartItems.find((item) => item.id === id);
        if (item && item.quantity > 1) {
          item.quantity--;
          document.querySelector(`.quantity-input[data-id="${id}"]`).value =
            item.quantity;
          updateItemPrice(id);
          updateTotalPrice();
        }
      });
    });

    // 수량 직접 입력
    document.querySelectorAll(".quantity-input").forEach((input) => {
      input.addEventListener("change", function () {
        const id = parseInt(this.getAttribute("data-id"));
        const item = cartItems.find((item) => item.id === id);
        if (item) {
          let quantity = parseInt(this.value);
          if (isNaN(quantity) || quantity < 1) {
            quantity = 1;
            this.value = 1;
          }
          item.quantity = quantity;
          updateItemPrice(id);
          updateTotalPrice();
        }
      });
    });

    // 삭제 버튼
    document.querySelectorAll(".delete-btn").forEach((button) => {
      button.addEventListener("click", function () {
        const id = parseInt(this.getAttribute("data-id"));
        const index = cartItems.findIndex((item) => item.id === id);
        if (index !== -1) {
          if (confirm("정말로 이 상품을 삭제하시겠습니까?")) {
            cartItems.splice(index, 1);
            renderCartItems();
          }
        }
      });
    });

    // 옵션 변경 버튼
    document.querySelectorAll(".option-btn").forEach((button) => {
      button.addEventListener("click", function () {
        const id = parseInt(this.getAttribute("data-id"));
        const item = cartItems.find((item) => item.id === id);
        if (item) {
          openOptionModal(item);
        }
      });
    });

    // 체크박스 전체 선택
    const selectAllCheckbox = document.getElementById("select-all");
    if (selectAllCheckbox) {
      selectAllCheckbox.addEventListener("change", function () {
        const isChecked = this.checked;
        document.querySelectorAll(".item-checkbox").forEach((checkbox) => {
          checkbox.checked = isChecked;
        });
        updateTotalPrice();
      });
    }

    // 개별 체크박스
    document.querySelectorAll(".item-checkbox").forEach((checkbox) => {
      checkbox.addEventListener("change", function () {
        updateTotalPrice();
        // 모든 체크박스 선택 여부 확인해서 전체선택 체크박스 상태 업데이트
        const allCheckboxes = document.querySelectorAll(".item-checkbox");
        const allChecked = Array.from(allCheckboxes).every(
          (checkbox) => checkbox.checked
        );
        document.getElementById("select-all").checked = allChecked;
      });
    });

    // 주문 버튼
    document.querySelectorAll(".order-btn").forEach((button) => {
      button.addEventListener("click", function () {
        const id = parseInt(this.getAttribute("data-id"));
        const selectedItems = cartItems.filter((item) => item.id === id);
        proceedToOrder(selectedItems);
      });
    });
  }

  // 아이템 가격 업데이트
  function updateItemPrice(id) {
    const item = cartItems.find((item) => item.id === id);
    if (item) {
      const priceElement = document.querySelector(
        `tr[data-id="${id}"] .item-price`
      );
      priceElement.textContent = formatPrice(item.price * item.quantity);
    }
  }

  // 총 가격 업데이트
  function updateTotalPrice() {
    let totalPrice = 0;
    let totalDiscount = 1800; // 임의의 할인금액
    const shippingCost = 3000;

    // 체크된 상품만 계산
    document.querySelectorAll(".item-checkbox:checked").forEach((checkbox) => {
      const id = parseInt(checkbox.closest("tr").getAttribute("data-id"));
      const item = cartItems.find((item) => item.id === id);
      if (item) {
        totalPrice += item.price * item.quantity;
      }
    });

    document.getElementById("total-price").textContent =
      formatPrice(totalPrice);
    document.getElementById("total-discount").textContent =
      formatPrice(totalDiscount);
    document.getElementById("shipping-cost").textContent =
      formatPrice(shippingCost);
    document.getElementById("final-price").textContent = formatPrice(
      totalPrice - totalDiscount + shippingCost
    );
  }

  // 옵션 모달 열기
  function openOptionModal(item) {
    const modal = document.getElementById("option-modal");
    const sizeSelect = document.getElementById("option-size");
    const toppingSelect = document.getElementById("option-topping");
    const dressingSelect = document.getElementById("option-dressing");

    // 현재 상품의 옵션 선택
    sizeSelect.value = item.options.size || "small";
    toppingSelect.value = item.options.topping || "none";
    dressingSelect.value = item.options.dressing || "olive";

    // 모달 표시
    modal.style.display = "block";

    // 확인 버튼
    document.getElementById("option-confirm").onclick = function () {
      // 옵션 업데이트
      item.options.size = sizeSelect.value;
      item.options.topping = toppingSelect.value;
      item.options.dressing = dressingSelect.value;

      // 가격 조정 (사이즈, 토핑에 따라)
      let basePrice = item.price;

      // 사이즈에 따른 가격 조정 - 기존 사이즈 가격을 빼고
      if (item.options.size === "medium") {
        basePrice += 1000;
      } else if (item.options.size === "large") {
        basePrice += 2000;
      }

      // 토핑에 따른 가격 조정
      if (item.options.topping === "chicken") {
        basePrice += 2500;
      } else if (item.options.topping === "bacon") {
        basePrice += 1500;
      } else if (item.options.topping === "avocado") {
        basePrice += 2000;
      }

      // 가격 업데이트
      item.price = basePrice;
      updateItemPrice(item.id);
      updateTotalPrice();

      // 모달 닫기
      modal.style.display = "none";
    };

    // 취소 버튼 및 X 버튼
    document.getElementById("option-cancel").onclick = function () {
      modal.style.display = "none";
    };
    document.querySelector(".close-modal").onclick = function () {
      modal.style.display = "none";
    };

    // 모달 외부 클릭 시 닫기
    window.onclick = function (event) {
      if (event.target === modal) {
        modal.style.display = "none";
      }
    };
  }

  // 선택 상품 주문
  document
    .getElementById("order-selected")
    .addEventListener("click", function () {
      const selectedItems = [];
      document
        .querySelectorAll(".item-checkbox:checked")
        .forEach((checkbox) => {
          const id = parseInt(checkbox.closest("tr").getAttribute("data-id"));
          const item = cartItems.find((item) => item.id === id);
          if (item) {
            selectedItems.push(item);
          }
        });

      if (selectedItems.length === 0) {
        alert("주문할 상품을 선택해주세요.");
        return;
      }

      proceedToOrder(selectedItems);
    });

  // 전체 상품 주문
  document.getElementById("order-all").addEventListener("click", function () {
    if (cartItems.length === 0) {
      alert("장바구니에 상품이 없습니다.");
      return;
    }

    proceedToOrder(cartItems);
  });

  // 주문 처리 함수
  function proceedToOrder(items) {
    // 선택된 상품 정보를 세션 스토리지에 저장
    sessionStorage.setItem("orderItems", JSON.stringify(items));

    // 주문 페이지로 이동
    window.location.href = "order.html";
  }

  // 초기 렌더링
  renderCartItems();
});
