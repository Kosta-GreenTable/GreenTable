// product-detail.js

document.addEventListener("DOMContentLoaded", () => {
  // DOM 요소 가져오기
  const buyNowButton = document.querySelector(".buy-now");
  const addToCartButton = document.querySelector(".add-to-cart");
  const quantityInput = document.getElementById("quantity");
  const increaseQuantityBtn = document.getElementById("increase-quantity");
  const decreaseQuantityBtn = document.getElementById("decrease-quantity");
  const totalPriceAmount = document.getElementById("total-price-amount");
  const stockAlert = document.querySelector(".stock-alert");
  
  // 제품 가격은 JSP에서 설정된 변수 사용 (originalPrice, discountRate, finalPrice)
  // 이미 선언되어 있는 변수: var originalPrice, var discountRate, var finalPrice

  // 이미지 슬라이더 기능
  setupImageSlider();

  // 리뷰 슬라이더 기능
  setupReviewSlider();

  // 탭 기능
  setupTabs();
  
  // 상품 정보
  const productId = document.querySelector('.add-to-cart').getAttribute('data-product-id');
  let stock = 0; // 재고 수량
  
  // 페이지 로드 시 재고 확인
  checkStock();
  
  // 수량 변경 및 금액 업데이트
  function updateTotalPrice() {
    const quantity = parseInt(quantityInput.value);
    const total = finalPrice * quantity;
    totalPriceAmount.textContent = formatPrice(total) + "원";
  }

  // 재고 부족 알림 표시 함수
  function showStockAlert() {
    stockAlert.style.display = 'block';
    stockAlert.textContent = `최대 주문 가능 수량은 ${stock}개입니다.`;
    
    // 2초 후 알림 메시지 숨기기
    setTimeout(() => {
      stockAlert.style.display = 'none';
    }, 2000);
  }

  increaseQuantityBtn.addEventListener("click", () => {
    let currentValue = parseInt(quantityInput.value);
    if (currentValue < stock) {
      quantityInput.value = currentValue + 1;
      updateTotalPrice();
    } else {
      showStockAlert();
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
    } else if (value > stock) {
      quantityInput.value = stock;
      showStockAlert();
    }
    updateTotalPrice();
  });
  
  // 바로 구매하기 버튼 클릭 이벤트
  buyNowButton.addEventListener("click", function(e) {
    // 재고 확인 후 처리
    const quantity = parseInt(quantityInput.value);
    
    if (quantity > stock) {
      e.preventDefault(); // 이벤트 기본 동작 중지
      
      // 알림 표시
      showStockAlert();
      
      // 수량 조정
      quantityInput.value = stock;
      updateTotalPrice();
      return false;
    }
    
    // 재고가 충분하면 바로 구매 페이지로 이동
    const url = `${contextPath}/front?key=order&methodName=buyNow&productId=${productId}&quantity=${quantity}`;
    window.location.href = url;
  });
  
  // 장바구니 담기 버튼 클릭 이벤트
  addToCartButton.addEventListener("click", function() {
    const quantity = parseInt(quantityInput.value);
    
    if (quantity > stock) {
      showStockAlert();
      quantityInput.value = stock;
      updateTotalPrice();
      return;
    }
    
    // AJAX 요청으로 장바구니에 상품 추가
    fetch(`${contextPath}/front?key=cart&methodName=add&productId=${productId}&quantity=${quantity}`)
      .then(response => {
        if (!response.ok) throw new Error('서버 응답 오류');
        return response.json();
      })
      .then(data => {
        if (data.success) {
          alert("장바구니에 상품이 추가되었습니다.");
          
          // 장바구니 아이콘 업데이트 (옵션)
          const cartCount = document.querySelector('.cart-count');
          if (cartCount) {
            cartCount.textContent = data.cartCount;
          }
        } else {
          if (data.needLogin) {
            if (confirm("로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")) {
              window.location.href = `${contextPath}/front?key=user&methodName=loginForm`;
            }
          } else {
            alert(data.message || "장바구니에 상품을 추가할 수 없습니다.");
          }
        }
      })
      .catch(error => {
        console.error('장바구니 추가 중 오류 발생:', error);
        alert("장바구니 추가 중 오류가 발생했습니다. 다시 시도해주세요.");
      });
  });

  // Ajax로 재고 확인
  function checkStock() {
    fetch(`${contextPath}/front?key=product&methodName=checkStock&productId=${productId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('서버 응답 오류');
        }
        return response.json();
      })
      .then(data => {
        if (data && data.success) {
          stock = data.stock;
        } else {
          console.warn('재고 정보를 가져올 수 없습니다:', data ? data.message : '알 수 없는 오류');
          // 기본값 설정 (재고 10개로 가정)
          stock = 10;
        }
        
        // 재고가 없으면 버튼 비활성화
        if (stock <= 0) {
          quantityInput.value = 0;
          quantityInput.disabled = true;
          decreaseQuantityBtn.disabled = true;
          increaseQuantityBtn.disabled = true;
          buyNowButton.disabled = true;
          addToCartButton.disabled = true;
          
          // 품절 표시
          const soldOutMsg = document.createElement('p');
          soldOutMsg.className = 'sold-out-message';
          soldOutMsg.textContent = '품절된 상품입니다.';
          quantityInput.parentElement.parentElement.appendChild(soldOutMsg);
        } else {
          console.log(`상품 재고: ${stock}개`);
        }
      })
      .catch(error => {
        console.error('재고 확인 중 오류 발생:', error);
        // 오류 발생 시 기본값 설정
        stock = 10;
      });
  }
  
  // 재고 초과 알림
  function showStockAlert() {
    alert(`재고 수량을 초과하여 구매할 수 없습니다. (현재 재고: ${stock}개)`);
  }// 바로 구매하기 버튼 클릭 이벤트
  buyNowButton.addEventListener("click", () => {
    const quantity = parseInt(quantityInput.value);
    // 구매할 상품 정보를 localStorage에 저장
    const orderItem = {
      id: buyNowButton.dataset.productId,
      name: document.querySelector(".product-info h1").textContent,
      originalPrice: originalPrice,
      discountRate: discountRate,
      price: finalPrice,
      quantity: quantity,
      image: document.querySelector(".product-image").src,
    };

    // 바로 구매용 주문 정보 저장
    localStorage.setItem("directOrder", JSON.stringify(orderItem));

    // order.html 페이지로 이동
    window.location.href = contextPath + "/order.jsp";
  });
  // 장바구니 담기 버튼 클릭 이벤트
  addToCartButton.addEventListener("click", () => {
    const quantity = parseInt(quantityInput.value);
    // 장바구니 데이터 저장 (localStorage 사용)
    addToCart({
      id: addToCartButton.dataset.productId,
      name: document.querySelector(".product-info h1").textContent,
      originalPrice: originalPrice,
      discountRate: discountRate,
      price: finalPrice,
      quantity: quantity,
      image: document.querySelector(".product-image").src,
    });

    // 장바구니 페이지로 이동
    window.location.href = contextPath + "/cart.jsp";
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
