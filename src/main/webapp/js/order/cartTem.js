document.addEventListener("DOMContentLoaded", () => { 
    // 현재 사용자 정보
    const userId = document.body.dataset.userid || '0';
    const isLoggedIn = !!userId;
    const contextPath = document.body.dataset.contextpath || '';

    // 숫자 포맷 함수
    function formatNumber(num) {
        return new Intl.NumberFormat().format(num);
    }
    
    // 장바구니 상품 수량 변경 버튼
    document.querySelectorAll(".quantity-btn").forEach(btn => {
        btn.addEventListener("click", function() {
            const isPlus = this.classList.contains("plus");
            const row = this.closest("tr");
            const userId = row.dataset.userid;
            const productId = row.dataset.productid;
             
            console.log("Button clicked - userId:", userId, "productId:", productId);
            const quantityinput = row.querySelector(".quantity-input");
            let quantity = parseInt(quantityinput.value);

            if (!isPlus && quantity <= 1) return; // 최소 수량 1
            
            // + 버튼이면 수량 증가, - 버튼이면 수량 감소
            quantity = isPlus ? quantity + 1 : quantity - 1;
            quantityinput.value = quantity;

            // // 수량이 변경되면 가격도 업데이트
            // const unitPrice = getUnitPrice(row);
            const priceCell = row.querySelector(".product-price");
            priceCell.textContent = `${formatNumber(price * quantity)}원`;
            
            // 서버에 수량 변경 요청
            updateCartQuantity(userId, productId, quantity, row);
        });
    });

    // 상품 수량 변경 ajax 요청
    async function updateCartQuantity(userId, productId, quantity, row) {
        try{
            const response = await fetch("/GreenTable/ajax", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: new URLSearchParams({
                    key: "cartRest",
                    methodName: "updateQuantity",
                    userId: userId,
                    productId: productId,
                    quantity: quantity
                })
            });
        
            if (!response.ok) throw new Error("수정 실패 : 서버 오류");

            const result = await response.json();
            console.log("수량업데이트: ",result);

            if (result.success) {
                // 서버에서 계산된 값으로 상품 가격 업데이트
                const priceCell = row.querySelector(".product-price");
                priceCell.textContent = `${formatNumber(result.itemTotal)}원`;

                updatePriceInfo(result);
        } else {
            alert(result.message || "수량 변경에 실패했습니다.");
        }
    } catch (error) {
            console.error("수량 업데이트 실패", error); 
            alert("수량 변경 중 문제가 발생했습니다.");
        }
    }

    // 장바구니 가격 정보 업데이트 함수
    function updatePriceInfo(data) {
        const priceBlocks = document.querySelectorAll(".price-cell");
        if(priceBlocks.length >= 4) {
            // 총 상품금액 업데이트
            priceBlocks[0].textContent = `${formatNumber(data.totalProductPrice)}원`;
            // 총 할인금액 업데이트
            priceBlocks[1].textContent = `${formatNumber(data.totalDiscount)}원`;
            // 배송비 업데이트
            priceBlocks[2].textContent = `${formatNumber(data.deliveryFee)}원`;
            // 결제금액 업데이트
            priceBlocks[3].textContent = `${formatNumber(data.totalPayPrice)}원`;
        }
    }


/////////////////////////////////////////////////////////////////////////////////
// 상품 삭제
async function deleteCartItem(userId, productId, row) {
    try{
        const response = await fetch("/GreenTable/ajax", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({
                key: "cartRest",
                methodName: "deleteCart",
                userId: userId,
                productId: productId
            })
        });
    
        if (!response.ok) throw new Error("삭제 실패 : 서버 오류");

        const result = await response.json();
        console.log("삭제결과: ",result);

        if(result.success) {
            row.remove();

            // 장바구니 비었는지 확인
            if (result.isEmpty) {
                window.location.reload(); // 장바구니가 비었으면 새로고침
            } else {
                // 하단 가격 정보 업데이트
                updatePriceInfo(result);
            }
        } else {
            alert(result.message || "상품 삭제에 실패했습니다.");
        }      
    } catch (error) {
        console.error("삭제 실패", error); 
        alert("상품 삭제 중 문제가 발생했습니다.");
    }
    
}


    // 삭제 버튼 이벤트 리스너
    document.querySelectorAll(".delete-btn").forEach(btn => {
        btn.addEventListener("click", function() {
            if(confirm("상품을 장바구니에서 삭제하시겠습니까?")) {
                const row = this.closest("tr");
                const userId = row.dataset.userid;
                const productId = row.dataset.productid;
                
               deleteCartItem(userId, productId, row);
            }
        });
    });
    

    
    // 전체 체크박스 이벤트
    const allCheckbox = document.querySelector("thead input[type=checkbox]");
    if(allCheckbox) {
        allCheckbox.addEventListener("change", function() {
            const isChecked = this.checked;
            document.querySelectorAll("tbody input[type=checkbox]").forEach(checkbox => {
                checkbox.checked = isChecked;
            });
        });
    }
});



// //장바구니 등록 (기존 코드)
// function addToCart(productId) {
//     const isLogin = <%= session.getAttribute("userId") != null %>;

//     if (isLogin) {
//         // 로그인 상태: 서버로 바로 전송
//         fetch("/cart/insert", {
//             method: "POST",
//             headers: { "Content-Type": "application/json" },
//             body: JSON.stringify({
//                 productId: productId,
//                 quantity: 1
//             })
//         }).then(res => alert("장바구니에 담겼습니다!"));
//     } else {
//         // 비로그인 상태: localStorage에 저장
//         let guestCart = JSON.parse(localStorage.getItem("guestCart") || "[]");

//         const existing = guestCart.find(item => item.productId === productId);
//         if (existing) {
//             existing.quantity += 1;
//         } else {
//             guestCart.push({ productId: productId, quantity: 1 });
//         }

//         localStorage.setItem("guestCart", JSON.stringify(guestCart));
//         alert("비회원 장바구니에 담겼습니다!");
//     }
// }

////////////////////////////////////////////////////////////////
// 비회원 장바구니 관리
const LOCAL_CART_KEY = 'guestCart';  //장바구니 localStorage 키

// 장바구니 불러오기
function loadGuestCart() {
  const data = localStorage.getItem(LOCAL_CART_KEY);
  return data ? JSON.parse(data) : [];
}

// 장바구니 저장하기
function saveGuestCart(cart) {
  localStorage.setItem(LOCAL_CART_KEY, JSON.stringify(cart));
}

// 장바구니에 상품 추가 (같은 상품이면 수량만 증가)
function addToGuestCart(product) {
  const cart = loadGuestCart();
  const existing = cart.find(item => item.productId === product.productId);

  if (existing) {
    existing.quantity += product.quantity;
  } else {
    cart.push(product);
  }

  saveGuestCart(cart);
}

// // 수량 변경
function updateGuestCartQuantity(productId, newQuantity) {
  const cart = loadGuestCart();
  const item = cart.find(item => item.productId === productId);

  if (item) {
    item.quantity = newQuantity;
    saveGuestCart(cart);
  }
}

// // 상품 삭제
function deleteGuestCartItem(productId) {
  const cart = loadGuestCart();
  const updatedCart = cart.filter(item => item.productId !== productId);
  saveGuestCart(updatedCart);
}
////////////////////////////////사용
// 상품 추가 예시
addToGuestCart({
    productId: 3,
    productName: "테스트상품",
    imageName: "/images/test.jpg",
    price: 20000,
    quantity: 1,
    discountRate: 5
  });
  
//   // 수량 증가 예시
//  updateGuestCartQuantity(3, 5);
  
//   // 삭제 예시
//   deleteGuestCartItem(3);
  
//   // 불러오기 후 HTML에 출력
  const cartItems = loadGuestCart();
  console.log(cartItems); // 장바구니 출력

document.getElementById('cartForm').addEventListener('submit', function(e) {
    e.preventDefault();
  
    const userId = document.getElementById('userId').value;
    const productId = parseInt(document.getElementById('productId').value);
    const quantity = parseInt(document.getElementById('quantity').value);
  
    if (!userId) {
      // 비회원일 경우 로컬스토리지에 저장
      const cart = JSON.parse(localStorage.getItem('guestCart') || '[]');
  
      const existing = cart.find(item => item.productId === productId);
      if (existing) {
        existing.quantity += quantity;
      } else {
        cart.push({ productId, quantity });
      }
  
      localStorage.setItem('guestCart', JSON.stringify(cart));
      alert('비회원 장바구니에 담겼습니다.');
    } else {
      // 회원이면 서버로 전송
      this.submit(); // 원래 form 제출
    }
  });