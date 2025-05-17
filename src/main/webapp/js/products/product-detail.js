document.addEventListener("DOMContentLoaded", () => {
  // DOM 요소 가져오기
  const buyNowButton = document.querySelector(".buy-now");
  const addToCartBtn = document.querySelector(".add-to-cart");
  const quantityInput = document.getElementById("quantity");
  const increaseQuantityBtn = document.getElementById("increase-quantity");
  const decreaseQuantityBtn = document.getElementById("decrease-quantity");
  const totalPriceAmount = document.getElementById("total-price-amount");
  
  // 제품 가격은 JSP에서 설정된 변수 사용 (originalPrice, discountRate, finalPrice)
  // 이미 선언되어 있는 변수: var originalPrice, var discountRate, var finalPrice

  // 이미지 슬라이더 기능
  setupImageSlider();

  // 리뷰 슬라이더 기능
  //setupReviewSlider();

  // 탭 기능
  //setupTabs();
  
  // 상품 정보
  const productId = addToCartBtn.getAttribute('data-product-id');
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
    alert(`재고 수량을 초과하여 구매할 수 없습니다. (현재 재고: ${stock}개)`);
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
  addToCartBtn.addEventListener("click", function() {
    const quantity = parseInt(quantityInput.value);
    
    if (quantity > stock) {
      showStockAlert();
      quantityInput.value = stock;
      updateTotalPrice();
      return;
    }

    if (!userId || userId === "0") {
      // 비회원일 경우 로컬스토리지에 저장
      addCartToLocalStorage(productId, quantity);

    } else {
      // 회원이면 서버로 전송
      addCartToServer(userId, productId, quantity);
    }
  });

   //비회원 장바구니 추가
   const addCartToLocalStorage = (productId, quantity) => {
    const guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
    const existing = guestCart.find(item => item.productId === productId);
    
    if (existing) {
      existing.quantity += quantity;
    } else {
      guestCart.push({ productId, quantity });
    }
    localStorage.setItem('guestCart', JSON.stringify(guestCart));
    alert('장바구니에 담겼습니다.');
  }

  //회원 장바구니 추가
  const addCartToServer = async (userId, productId, quantity) => {
    try {
      const response = await fetch("/front?key=cart&methodName=insertCart", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({ userId, productId, quantity }),
      });
  
      if (!response.ok) throw new Error("서버 오류");
  
      const result = await response.json(); // 필요 시 JSON으로 바꿀 수도 있음
      if (result.success) {
        alert("장바구니에 담겼습니다.");
      } else {
        alert(result.message || "장바구니 담기에 실패했습니다.");
      }
    } catch (err) {
      console.error("장바구니 추가 실패:", err);
      alert("장바구니 담기에 실패했습니다.");
    }
  }


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
          addToCartBtn.disabled = true;
          
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
  
  // 바로 구매하기 버튼 클릭 이벤트
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

  // 초기 금액 설정
  updateTotalPrice();

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
// function setupReviewSlider() {
//   const reviewImages = document.querySelector(".review-images");
//   const reviewLeftNav = document.querySelector(".review-navigation.left");
//   const reviewRightNav = document.querySelector(".review-navigation.right");
//   const reviewImageCount =
//     document.querySelectorAll(".review-images img").length;
//   const imagesPerView = 4; // 한 번에 보이는 이미지 수
//   let currentReviewIndex = 0;

//   const updateReviewSlider = () => {
//     const maxOffset = Math.max(0, reviewImageCount - imagesPerView);
//     const normalizedIndex = Math.min(currentReviewIndex, maxOffset);

//     // 이미지 너비(+갭)에 따라 이동 거리 계산
//     const imageWidth = 160; // 이미지 너비 + 갭
//     reviewImages.style.transform = `translateX(-${
//       normalizedIndex * imageWidth
//     }px)`;
//   };

//   reviewLeftNav.addEventListener("click", () => {
//     currentReviewIndex = Math.max(0, currentReviewIndex - 1);
//     updateReviewSlider();
//   });

//   reviewRightNav.addEventListener("click", () => {
//     const maxIndex = Math.max(0, reviewImageCount - imagesPerView);
//     currentReviewIndex = Math.min(maxIndex, currentReviewIndex + 1);
//     updateReviewSlider();
//   });

//   // 초기 슬라이더 설정
//   updateReviewSlider();
// }

// // 탭 기능 설정
// function setupTabs() {
//   const tabs = document.querySelectorAll(".tabs button");
//   const tabContents = document.querySelectorAll(".tab-content");

//   tabs.forEach((tab, index) => {
//     tab.addEventListener("click", () => {
//       tabs.forEach((t) => t.classList.remove("active"));
//       tabContents.forEach((content) => (content.style.display = "none"));

//       tab.classList.add("active");
//       tabContents[index].style.display = "block";
//     });
//   });

//   // 기본 탭 활성화
//   tabs[0].classList.add("active");
//   tabContents[0].style.display = "block";
// }

// 가격 포맷팅 함수
function formatPrice(price) {
  return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
});