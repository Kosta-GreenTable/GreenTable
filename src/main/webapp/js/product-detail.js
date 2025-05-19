// 전역 함수 정의
function setupImageSlider() {
  const images = document.querySelectorAll(".product-image-container img");
  const leftNav = document.querySelector(".image-navigation.left");
  const rightNav = document.querySelector(".image-navigation.right");
  const imageCounter = document.querySelector(".image-counter");
  
  if (!images.length || !leftNav || !rightNav || !imageCounter) {
    console.warn("이미지 슬라이더 요소를 찾을 수 없습니다");
    return;
  }
  
  console.log(`이미지 슬라이더 초기화: ${images.length}개 이미지`);
  
  let currentIndex = 0;
  const totalImages = images.length;

  // 이미지 표시 업데이트
  const updateSlider = () => {
    images.forEach((img, index) => {
      img.style.display = index === currentIndex ? "block" : "none";
    });
    imageCounter.textContent = `${currentIndex + 1} / ${totalImages}`;
    console.log(`이미지 변경: ${currentIndex + 1} / ${totalImages}`);
  };

  // 이전 이미지 버튼
  leftNav.addEventListener("click", () => {
    currentIndex = (currentIndex - 1 + totalImages) % totalImages;
    updateSlider();
  });

  // 다음 이미지 버튼
  rightNav.addEventListener("click", () => {
    currentIndex = (currentIndex + 1) % totalImages;
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
  
  if (!reviewImages || !reviewLeftNav || !reviewRightNav) {
    console.warn("리뷰 슬라이더 요소를 찾을 수 없습니다");
    return;
  }
  
  const reviewImageCount = document.querySelectorAll(".review-images img").length;
  if (reviewImageCount <= 1) {
    console.log("리뷰 이미지가 1개 이하이므로 슬라이더를 초기화하지 않습니다");
    return;
  }
  
  console.log(`리뷰 슬라이더 초기화: ${reviewImageCount}개 이미지`);
  
  const imagesPerView = window.innerWidth < 768 ? 2 : 4; // 모바일에서는 2개, 데스크탑에서는 4개
  let currentReviewIndex = 0;

  // 리뷰 슬라이더 업데이트
  const updateReviewSlider = () => {
    const maxOffset = Math.max(0, reviewImageCount - imagesPerView);
    const normalizedIndex = Math.min(currentReviewIndex, maxOffset);

    // 이미지 너비(+갭)에 따라 이동 거리 계산
    const imageWidth = 160; // 이미지 너비 + 갭
    reviewImages.style.transform = `translateX(-${normalizedIndex * imageWidth}px)`;
    console.log(`리뷰 슬라이더 위치 업데이트: ${normalizedIndex} / ${maxOffset}`);
  };

  // 이전 리뷰 이미지 버튼
  reviewLeftNav.addEventListener("click", () => {
    currentReviewIndex = Math.max(0, currentReviewIndex - 1);
    updateReviewSlider();
  });

  // 다음 리뷰 이미지 버튼
  reviewRightNav.addEventListener("click", () => {
    const maxIndex = Math.max(0, reviewImageCount - imagesPerView);
    currentReviewIndex = Math.min(maxIndex, currentReviewIndex + 1);
    updateReviewSlider();
  });

  // 초기 리뷰 슬라이더 설정
  updateReviewSlider();
}

// 탭 기능 설정
function setupTabs() {
  const tabs = document.querySelectorAll(".tabs button");
  const tabContents = document.querySelectorAll(".tab-content");
  
  if (!tabs.length || !tabContents.length) {
    console.warn("탭 요소를 찾을 수 없습니다");
    return;
  }
  
  console.log(`탭 초기화: ${tabs.length}개 탭, ${tabContents.length}개 컨텐츠`);

  tabs.forEach((tab, index) => {
    tab.addEventListener("click", () => {
      console.log(`${index + 1}번째 탭 클릭됨`);
      
      // 모든 탭 및 콘텐츠 비활성화
      tabs.forEach(t => t.classList.remove("active"));
      tabContents.forEach(c => c.style.display = "none");
      
      // 클릭한 탭 및 해당 콘텐츠 활성화
      tab.classList.add("active");
      
      if (index < tabContents.length) {
        tabContents[index].style.display = "block";
      } else {
        console.error(`탭 인덱스(${index})에 해당하는 컨텐츠가 없습니다`);
      }
    });
  });
  
  // 첫 번째 탭이 기본적으로 활성화되도록 설정
  if (tabs.length > 0 && tabContents.length > 0) {
    tabs[0].classList.add("active");
    tabContents[0].style.display = "block";
  }
}

// 가격 포맷팅 함수
function formatPrice(price) {
  return Math.round(price).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 메인 이벤트 리스너
document.addEventListener("DOMContentLoaded", () => {
  console.log("상품 상세 페이지 초기화 중...");
  
  // DOM 요소 가져오기
  const buyNowButton = document.querySelector(".buy-now");
  const addToCartButton = document.querySelector(".add-to-cart");
  const quantityInput = document.getElementById("quantity");
  const increaseQuantityBtn = document.getElementById("increase-quantity");
  const decreaseQuantityBtn = document.getElementById("decrease-quantity");
  const totalPriceAmount = document.getElementById("total-price-amount");
  const stockAlert = document.querySelector(".stock-alert");
  
  // 디버깅을 위한 요소 확인
  console.log("DOM 요소 초기화:", {
    buyNowButton: !!buyNowButton,
    addToCartButton: !!addToCartButton,
    quantityInput: !!quantityInput,
    increaseQuantityBtn: !!increaseQuantityBtn,
    decreaseQuantityBtn: !!decreaseQuantityBtn,
    totalPriceAmount: !!totalPriceAmount,
    stockAlert: !!stockAlert
  });
  
  // 제품 가격은 JSP에서 설정된 변수 사용 
  console.log("상품 정보 초기화:", {
    originalPrice,
    discountRate,
    finalPrice,
    maxStock
  });
  
  // 이미지 슬라이더 초기화
  setupImageSlider();
  
  // 리뷰 슬라이더 초기화 (요소가 존재하는 경우만)
  const reviewSlider = document.querySelector('.review-slider-container');
  if (reviewSlider) {
    setupReviewSlider();
  }
  
  // 탭 기능 초기화
  setupTabs();
  
  // 상품 정보
  //const productId = addToCartBtn.getAttribute('data-product-id');
  const productId = document.querySelector('input[name="productId"]')?.value;
  if (!productId) {
    console.error("상품 ID를 찾을 수 없습니다");
  }
  
  // 페이지 로드 시 재고 확인
  checkStock();
  
  // 실시간 재고 확인 (30초마다)
  setInterval(checkStock, 30000);
  
  // 수량 변경 및 금액 업데이트
  function updateTotalPrice() {
    if (!quantityInput || !totalPriceAmount) {
      console.error("수량 입력 필드 또는 총 가격 요소를 찾을 수 없습니다");
      return;
    }
    
    const quantity = parseInt(quantityInput.value) || 1;
    const total = finalPrice * quantity;
    
    console.log(`수량 변경: ${quantity}개, 총 금액: ${formatPrice(total)}원`);
    totalPriceAmount.textContent = formatPrice(total) + "원";
  }

  // 재고 부족 알림 표시 함수
  function showStockAlert() {
    if (!stockAlert) return;
    
    stockAlert.style.display = 'block';
    stockAlert.textContent = `최대 주문 가능 수량은 ${maxStock}개입니다.`;
    
    console.log(`재고 부족 알림: 최대 ${maxStock}개까지 구매 가능`);
    
    // 3초 후 알림 메시지 숨기기
    setTimeout(() => {
      stockAlert.style.display = 'none';
    }, 3000);
  }

  // 수량 증가 버튼 클릭
  if (increaseQuantityBtn) {
    increaseQuantityBtn.addEventListener("click", () => {
      if (!quantityInput) return;
      
      let currentValue = parseInt(quantityInput.value) || 1;
      if (currentValue < maxStock) {
        quantityInput.value = currentValue + 1;
        updateTotalPrice();
      } else {
        showStockAlert();
      }
    });
  }

  // 수량 감소 버튼 클릭
  if (decreaseQuantityBtn) {
    decreaseQuantityBtn.addEventListener("click", () => {
      if (!quantityInput) return;
      
      let currentValue = parseInt(quantityInput.value) || 1;
      if (currentValue > 1) {
        quantityInput.value = currentValue - 1;
        updateTotalPrice();
      }
    });
  }

  // 수량 직접 입력 시
  if (quantityInput) {
    quantityInput.addEventListener("change", () => {
      let value = parseInt(quantityInput.value) || 1;
      
      if (value < 1) {
        quantityInput.value = 1;
        value = 1;
      } else if (value > maxStock) {
        quantityInput.value = maxStock;
        value = maxStock;
        showStockAlert();
      }
      
      console.log(`수량 직접 입력: ${value}개`);
      updateTotalPrice();
    });
  }
  
  // 바로 구매하기 버튼 클릭 이벤트
  if (buyNowButton) {
    buyNowButton.addEventListener("click", function(e) {
      if (!quantityInput || !productId) return;
      
      // 재고 확인 후 처리
      const quantity = parseInt(quantityInput.value) || 1;
      
      if (quantity > maxStock) {
        e.preventDefault(); // 이벤트 기본 동작 중지
        
        // 알림 표시
        showStockAlert();
        
        // 수량 조정
        quantityInput.value = maxStock;
        updateTotalPrice();
        return false;
      }
      
      console.log(`바로 구매: 상품 ID ${productId}, 수량 ${quantity}개`);
      
      // 재고가 충분하면 바로 구매 페이지로 이동
      const url = `${contextPath}/front?key=order&methodName=buyNow&productId=${productId}&quantity=${quantity}`;
      window.location.href = url;
    });
  }
  
  // 장바구니 담기 버튼 클릭 이벤트
  if (addToCartButton) {
    addToCartButton.addEventListener("click", function() {
      if (!quantityInput || !productId) return;
      
      const quantity = parseInt(quantityInput.value) || 1;
      
      if (quantity > maxStock) {
        showStockAlert();
        quantityInput.value = maxStock;
        updateTotalPrice();
        return;
      }
      
      console.log(`장바구니 담기: 상품 ID ${productId}, 수량 ${quantity}개`);
      
	  if (!userId || userId === "0") {
	        // 비회원일 경우 로컬스토리지에 저장
	        addCartToLocalStorage(productId, quantity);

	      } else {
	        // 회원이면 서버로 전송
	        addCartToServer(userId, productId, quantity);
	      }
		  
    });
  }
  
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

  // Ajax로 재고 확인 및 업데이트
  function checkStock() {
    if (!productId) return;
    
    console.log(`재고 확인 중: 상품 ID ${productId}`);
    
    fetch(`${contextPath}/front?key=product&methodName=checkStock&productId=${productId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('서버 응답 오류');
        }
        return response.json();
      })
      .then(data => {
        console.log('재고 확인 응답:', data);
        
        if (data && data.success) {
          // 재고 업데이트
          const previousStock = maxStock;
          maxStock = data.stock;
          
          console.log(`재고 업데이트: ${previousStock}개 → ${maxStock}개`);
          
          // 재고 표시 업데이트
          const stockDisplay = document.getElementById('stock-quantity');
          if (stockDisplay) {
            stockDisplay.textContent = maxStock;
          }
          
          // 수량 입력 필드 최대값 업데이트
          if (quantityInput) {
            quantityInput.setAttribute('max', maxStock);
            
            // 현재 선택된 수량이 재고보다 많으면 조정
            const currentQuantity = parseInt(quantityInput.value) || 1;
            if (currentQuantity > maxStock) {
              const newQuantity = maxStock > 0 ? maxStock : 0;
              console.log(`수량 자동 조정: ${currentQuantity}개 → ${newQuantity}개 (재고 부족)`);
              
              quantityInput.value = newQuantity;
              updateTotalPrice();
            }
          }
          
          // 재고가 없으면 버튼 비활성화
          if (maxStock <= 0) {
            handleSoldOut();
          }
        } else {
          console.warn('재고 정보를 가져올 수 없습니다:', data ? data.message : '알 수 없는 오류');
        }
      })
      .catch(error => {
        console.error('재고 확인 중 오류 발생:', error);
      });
  }
  
  // 품절 처리 함수
  function handleSoldOut() {
    console.log('품절 처리');
    
    if (quantityInput) {
      quantityInput.value = 0;
      quantityInput.disabled = true;
    }
    
    if (decreaseQuantityBtn) decreaseQuantityBtn.disabled = true;
    if (increaseQuantityBtn) increaseQuantityBtn.disabled = true;
    
    if (buyNowButton) buyNowButton.disabled = true;
    if (addToCartButton) addToCartButton.disabled = true;
    
    // 품절 메시지 표시
    if (!document.querySelector('.sold-out-message')) {
      const soldOutMsg = document.createElement('p');
      soldOutMsg.className = 'sold-out-message';
      soldOutMsg.textContent = '품절된 상품입니다.';
      
      const buttonContainer = document.querySelector('.button-container');
      if (buttonContainer) {
        buttonContainer.insertAdjacentElement('beforebegin', soldOutMsg);
      } else if (quantityInput && quantityInput.parentElement) {
        quantityInput.parentElement.parentElement.appendChild(soldOutMsg);
      }
    }
  }
  
  // 빠른 장바구니 담기 버튼
  const quickAddButtons = document.querySelectorAll(".quick-add");
  quickAddButtons.forEach((button) => {
    button.addEventListener("click", (e) => {
      e.preventDefault();
      e.stopPropagation(); // 부모 요소의 클릭 이벤트 방지 (상품 상세로 이동하는 것 방지)
      
      const productId = button.getAttribute('data-product-id');
      if (!productId) return;
      
      console.log(`빠른 장바구니: 상품 ID ${productId}, 수량 1개`);
      
      // AJAX 요청으로 장바구니에 상품 추가
      fetch(`${contextPath}/front?key=cart&methodName=add`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `productId=${productId}&quantity=1`
      })
      .then(response => {
        if (!response.ok) throw new Error('서버 응답 오류');
        return response.json();
      })
      .then(data => {
        console.log('빠른 장바구니 응답:', data);
        
        if (data.success) {
          // 버튼 텍스트 및 스타일 변경으로 직관적인 피드백 제공
          const originalText = button.innerHTML;
          button.innerHTML = '<i class="fas fa-check"></i> 담기 완료';
          button.style.backgroundColor = '#00c471';
          button.style.color = 'white';
          
          // 3초 후 원래 상태로 복원
          setTimeout(() => {
            button.innerHTML = originalText;
            button.style.backgroundColor = '';
            button.style.color = '';
          }, 2000);
          
          // 장바구니 아이콘 업데이트 (옵션)
          const cartCount = document.querySelector('.cart-count');
          if (cartCount) {
            cartCount.textContent = data.cartCount || parseInt(cartCount.textContent || '0') + 1;
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
  });

  // 초기 금액 설정
  updateTotalPrice();
  
  console.log("상품 상세 페이지 초기화 완료");
});

// 화면 크기에 따른 반응형 조정
window.addEventListener("resize", () => {
  const reviewSlider = document.querySelector('.review-slider-container');
  if (reviewSlider) {
    setupReviewSlider();
  }
});