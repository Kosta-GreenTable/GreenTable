// product-detail.js

document.addEventListener("DOMContentLoaded", () => {
  // DOM 요소 가져오기
  const buyNowButton = document.querySelector(".buy-now");
  const addToCartButton = document.querySelector(".add-to-cart");
  const quantityInput = document.getElementById("quantity");
  const increaseQuantityBtn = document.getElementById("increase-quantity");
  const decreaseQuantityBtn = document.getElementById("decrease-quantity");
  const totalPriceAmount = document.getElementById("total-price-amount");

  // 제품 가격
  const productPrice = 8500; // 원

  // 이미지 슬라이더 기능
  setupImageSlider();

  // 리뷰 슬라이더 기능
  setupReviewSlider();

  // 탭 기능
  setupTabs();
  // 수량 변경 및 금액 업데이트
  function updateTotalPrice() {
    const quantity = parseInt(quantityInput.value);
    const total = productPrice * quantity;
    totalPriceAmount.textContent = formatPrice(total) + "원";
  }

  increaseQuantityBtn.addEventListener("click", () => {
    let currentValue = parseInt(quantityInput.value);
    if (currentValue < 10) {
      quantityInput.value = currentValue + 1;
      updateTotalPrice();
    }
  });

  decreaseQuantityBtn.addEventListener("click", () => {
    let currentValue = parseInt(quantityInput.value);
    if (currentValue > 1) {
      quantityInput.value = currentValue - 1;
      updateTotalPrice();
    }
  });

  quantityInput.addEventListener("change", () => {
    let value = parseInt(quantityInput.value);
    if (isNaN(value) || value < 1) {
      quantityInput.value = 1;
    } else if (value > 10) {
      quantityInput.value = 10;
    }
    updateTotalPrice();
  });
  // 바로 구매하기 버튼 클릭 이벤트
  buyNowButton.addEventListener("click", () => {
    const quantity = parseInt(quantityInput.value);
    // 구매할 상품 정보를 localStorage에 저장
    const orderItem = {
      id: "bulgogi-poke-salad",
      name: "불고기 포케 샐러드 275g",
      price: productPrice,
      quantity: quantity,
      image: "https://picsum.photos/id/488/500/500",
    };

    // 바로 구매용 주문 정보 저장
    localStorage.setItem("directOrder", JSON.stringify(orderItem));

    // order.html 페이지로 이동
    window.location.href = "order.html";
  });

  // 장바구니 담기 버튼 클릭 이벤트
  addToCartButton.addEventListener("click", () => {
    const quantity = parseInt(quantityInput.value);
    // 장바구니 데이터 저장 (localStorage 사용)
    addToCart({
      id: "bulgogi-poke-salad",
      name: "불고기 포케 샐러드 275g",
      price: productPrice,
      quantity: quantity,
      image: "https://picsum.photos/id/488/500/500",
    });

    // 장바구니 페이지로 이동
    window.location.href = "cart.html";
  });

  // 장바구니에 상품 추가하는 함수
  function addToCart(product) {
    let cart = JSON.parse(localStorage.getItem("cart")) || [];

    // 이미 장바구니에 있는지 확인
    const existingProductIndex = cart.findIndex(
      (item) => item.id === product.id
    );

    if (existingProductIndex !== -1) {
      // 이미 있으면 수량만 증가
      cart[existingProductIndex].quantity += product.quantity;
    } else {
      // 없으면 새로 추가
      cart.push(product);
    }

    localStorage.setItem("cart", JSON.stringify(cart));

    // 장바구니 아이콘이나 카운터가 있다면 업데이트할 수 있음
    updateCartCounter();
  }

  // 장바구니 카운터 업데이트 (필요시)
  function updateCartCounter() {
    // 장바구니 아이콘 옆에 수량 표시 기능 구현 (옵션)
    let cart = JSON.parse(localStorage.getItem("cart")) || [];
    let totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);

    // 카운터 요소가 있다면 업데이트
    const cartCounter = document.querySelector(".cart-counter");
    if (cartCounter) {
      cartCounter.textContent = totalItems;
      cartCounter.style.display = totalItems > 0 ? "block" : "none";
    }
  }
  // 여기 불필요한 모달 관련 코드 삭제

  // 초기 금액 설정
  updateTotalPrice();
  // 빠른 장바구니 담기 버튼
  const quickAddButtons = document.querySelectorAll(".quick-add");
  quickAddButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      const card = e.target.closest(".product-card");
      const name = card.querySelector("h3").textContent;
      const price = parseInt(
        card.querySelector(".product-price").textContent.replace(/[^\d]/g, "")
      );
      const image = card.querySelector("img").src;
      const id = name.toLowerCase().replace(/\s+/g, "-");

      addToCart({
        id,
        name,
        price,
        quantity: 1,
        image,
      });

      // 장바구니 페이지로 이동
      window.location.href = "cart.html";
    });
  });
});

// 이미지 슬라이더 설정
function setupImageSlider() {
  const images = document.querySelectorAll(".product-image-container img");
  const leftNav = document.querySelector(".image-navigation.left");
  const rightNav = document.querySelector(".image-navigation.right");
  const imageCounter = document.querySelector(".image-counter");
  let currentIndex = 0;

  const updateSlider = () => {
    images.forEach((img, index) => {
      img.style.display = index === currentIndex ? "block" : "none";
    });
    imageCounter.textContent = `${currentIndex + 1} / ${images.length}`;
  };

  leftNav.addEventListener("click", () => {
    currentIndex = (currentIndex - 1 + images.length) % images.length;
    updateSlider();
  });

  rightNav.addEventListener("click", () => {
    currentIndex = (currentIndex + 1) % images.length;
    updateSlider();
  });

  // 초기 슬라이더 설정
  updateSlider();
}

// 리뷰 슬라이더 설정
function setupReviewSlider() {
  const reviewImages = document.querySelector(".review-images");
  const reviewLeftNav = document.querySelector(".review-navigation.left");
  const reviewRightNav = document.querySelector(".review-navigation.right");
  const reviewImageCount =
    document.querySelectorAll(".review-images img").length;
  const imagesPerView = 4; // 한 번에 보이는 이미지 수
  let currentReviewIndex = 0;

  const updateReviewSlider = () => {
    const maxOffset = Math.max(0, reviewImageCount - imagesPerView);
    const normalizedIndex = Math.min(currentReviewIndex, maxOffset);

    // 이미지 너비(+갭)에 따라 이동 거리 계산
    const imageWidth = 160; // 이미지 너비 + 갭
    reviewImages.style.transform = `translateX(-${
      normalizedIndex * imageWidth
    }px)`;
  };

  reviewLeftNav.addEventListener("click", () => {
    currentReviewIndex = Math.max(0, currentReviewIndex - 1);
    updateReviewSlider();
  });

  reviewRightNav.addEventListener("click", () => {
    const maxIndex = Math.max(0, reviewImageCount - imagesPerView);
    currentReviewIndex = Math.min(maxIndex, currentReviewIndex + 1);
    updateReviewSlider();
  });

  // 초기 슬라이더 설정
  updateReviewSlider();
}

// 탭 기능 설정
function setupTabs() {
  const tabs = document.querySelectorAll(".tabs button");
  const tabContents = document.querySelectorAll(".tab-content");

  tabs.forEach((tab, index) => {
    tab.addEventListener("click", () => {
      tabs.forEach((t) => t.classList.remove("active"));
      tabContents.forEach((content) => (content.style.display = "none"));

      tab.classList.add("active");
      tabContents[index].style.display = "block";
    });
  });

  // 기본 탭 활성화
  tabs[0].classList.add("active");
  tabContents[0].style.display = "block";
}

// 가격 포맷팅 함수
function formatPrice(price) {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 화면 크기에 따른 반응형 조정
window.addEventListener("resize", () => {
  setupReviewSlider();
});

// 삭제된 결제 모달 관련 코드
