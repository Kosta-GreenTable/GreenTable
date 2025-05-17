document.addEventListener("DOMContentLoaded", () => { 
    // 현재 사용자 정보
    const userId = document.body.dataset.userid || '';
    const isLoggedIn = !!userId;
    const contextPath = document.body.dataset.contextpath || '';
    
    // 숫자 포맷 함수
    function formatNumber(num) {
        return new Intl.NumberFormat().format(num);
    }

    /**
     * 비회원 장바구니 데이터 서버로 전송 및 업데이트
     */
    async function guestCartInfo() {
        const guestCart = JSON.parse(localStorage.getItem("guestCart") || "[]");

        // 비회원 장바구니가 비어있으면 빈 장바구니 화면 유지
        if (guestCart.length === 0) {
            return;
        }
        // productId를 정수형으로 변환
        const formattedCart = guestCart.map(item => ({
        productId: parseInt(item.productId),
        quantity: parseInt(item.quantity)
    }));
        try {
            const response = await fetch(`${contextPath}/ajax?key=cartRest&methodName=calculateGuestCart`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ items: formattedCart }),
            });

            if (!response.ok) throw new Error("서버 오류로 인해 데이터를 가져올 수 없습니다.");

            const result = await response.json();

            if (result.success) {
                // 비회원 장바구니 데이터 렌더링
                renderGuestCart(result.cartItems);
            
                // 가격 정보 업데이트
                updatePriceInfo({
                    totalProductPrice: result.totalProductPrice,
                    totalDiscount: result.totalDiscount, 
                    deliveryFee: result.deliveryFee,
                    totalPayPrice: result.totalPayPrice
                });
            } else {
                console.error("장바구니 데이터를 가져오는 데 실패했습니다:", result.message);
            }
        } catch (error) {
            console.error("비회원 장바구니 데이터 처리 중 오류 발생:", error);
        }
    }

    // 페이지 초기화 - 회원/비회원 장바구니 데이터 로드
    initCart();
    
    /**
     * 페이지 초기화
     */
    function initCart() {
        if (!isLoggedIn) {
            // 비회원 장바구니 데이터 로드 및 화면 표시
            guestCartInfo();
        }
        
        // 장바구니 이벤트 등록 (회원/비회원 공통)
        setupQuantityButtons();
        setupDeleteButtons();
        setupCheckboxEvents();
        setupOrderButtons();
    }

    /**
     * 비회원 장바구니 렌더링
     */
    function renderGuestCart(cartItems) {
        
        // 비회원 장바구니가 비어있으면 빈 장바구니 화면 유지
        if (!cartItems || cartItems.length === 0) {
            return;
        }

        // 비회원 장바구니가 있으면 빈 장바구니 메시지를 숨김
        const emptyContainer = document.querySelector('.empty-cart-container');
        if (emptyContainer) {
            emptyContainer.style.display = 'none';
        }

        //장바구니 생성
        let cartTable = document.querySelector('.cart-table');
        if (!cartTable) {
            const container = document.querySelector('.cart-container');
            
            // 장바구니 테이블 생성
            cartTable = document.createElement('table');
            cartTable.className = 'cart-table';
            cartTable.innerHTML = `
                <thead>
                    <tr>
                        <th><input type="checkbox" checked></th>
                        <th>상품정보</th>
                        <th>수량</th>
                        <th>구매 금액</th>
                        <th>선택</th>
                    </tr>
                </thead>
                <tbody></tbody>
            `;
    
            // 가격 정보 컨테이너 생성
            const priceContainer = document.createElement('div');
            priceContainer.className = 'price-container';
            priceContainer.innerHTML = `
                <div class="price-block">
                    <p>총 상품금액</p>
                    <p class="price-cell">0원</p>
                </div>
                <img src="https://atowertr6856.cdn-nhncommerce.com/data/skin/front/kaimen_pc_n/img/order/order_price_minus.png" alt="빼기" class="price-icon">
                <div class="price-block">
                    <p>총 할인금액</p>
                    <p class="price-cell">0원</p>
                </div>
                <img src="https://atowertr6856.cdn-nhncommerce.com/data/skin/front/kaimen_pc_n/img/order/order_price_plus.png" alt="더하기" class="price-icon">
                <div class="price-block">
                    <p>총 배송비</p>
                    <p class="price-cell">0원</p>
                </div>
                <img src="https://atowertr6856.cdn-nhncommerce.com/data/skin/front/kaimen_pc_n/img/order/order_price_total.png" alt="합계" class="price-icon">
                <div class="price-block total">
                    <p>결제금액</p>
                    <p class="price-cell">0원</p>
                </div>
            `;
            
            // 버튼 컨테이너 생성
            const buttonContainer = document.createElement('div');
            buttonContainer.className = 'cart-buttons';
            buttonContainer.innerHTML = `
                <button class="continue-btn">쇼핑 계속하기</button>
                <button class="order-selected-btn">선택 상품 주문</button>
                <button class="order-all-btn">전체 주문</button>
            `;
    
            container.appendChild(cartTable);
            container.appendChild(priceContainer);
            container.appendChild(buttonContainer);
        }

        // 비회원 장바구니가 있는 경우 테이블 채우기
        const tableBody = document.querySelector('.cart-table tbody');
        if (!tableBody) return;
        
        tableBody.innerHTML = '';
        
        cartItems.forEach(item => {
            const discountedPrice = item.price * (1 - (item.discountRate / 100));
            const totalPrice = discountedPrice * item.quantity;
            
            const row = document.createElement('tr');
            row.dataset.productid = item.productId;
            
            row.innerHTML = `
                <td><input type="checkbox" class="cart-checkbox" checked></td>
                <td class="product-info">
                    <img src="${item.imageName}" alt="상품 이미지">
                    <div>
                        <p>${item.productName}</p>
                    </div>
                </td>
                <td class="quantity-cell">
                    <button class="quantity-btn minus">-</button>
                    <input type="text" value="${item.quantity}" class="quantity-input" readonly>
                    <button class="quantity-btn plus">+</button>
                </td>
                <td class="product-price">${formatNumber(totalPrice)}원</td>
                <td>
                    <div class="action-buttons">
                        <button class="order-btn">주문</button>
                        <button class="delete-btn">삭제</button>
                    </div>
                </td>
            `;
            
            tableBody.appendChild(row);
        });
        
        // 이벤트 리스너 다시 설정
        setupQuantityButtons();
        setupDeleteButtons();
        setupCheckboxEvents();
        setupOrderButtons();
    }// 렌더링끝
    
    ////////////////////////////////////////////////////////////////
    /**
     * 수량 변경 버튼 이벤트 설정
     */
    function setupQuantityButtons() {
        document.querySelectorAll(".quantity-btn").forEach(btn => {
            btn.addEventListener("click", function() {
                const isPlus = this.classList.contains("plus");
                const row = this.closest("tr");
                const productId = parseInt(row.dataset.productid);
                
                const quantityInput = row.querySelector(".quantity-input");
                let quantity = parseInt(quantityInput.value);
                
                if (!isPlus && quantity <= 1) return; // 최소 수량 1
                
                // + 버튼이면 수량 증가, - 버튼이면 수량 감소
                quantity = isPlus ? quantity + 1 : quantity - 1;
                quantityInput.value = quantity;
                
                if (isLoggedIn) {
                    // 회원 - 서버에 수량 변경 요청
                    updateCartQuantity(userId, productId, quantity, row);
                } else {
                    // 비회원 - 로컬스토리지에 수량 변경 저장
                    updateGuestCartQuantity(productId, quantity, row);
                }
                // 체크된 항목들의 합계 업데이트
                calculateSelectedTotal();
            });
        });
    }
    
    /**
     * 회원 장바구니 수량 변경 요청
     */
    async function updateCartQuantity(userId, productId, quantity, row) {
        try {
            const response = await fetch(`${contextPath}/ajax`, {
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

            if (result.success) {
                // 서버에서 계산된 값으로 상품 가격 업데이트
                const priceCell = row.querySelector(".product-price");
                priceCell.textContent = `${formatNumber(result.itemTotal)}원`;

                // 체크박스 체크된 상품만 계산에 반영
                //calculateSelectedTotal();
                // 전체 가격 정보 업데이트
                updatePriceInfo(result);
            } else {
                alert(result.message || "수량 변경에 실패했습니다.");
                // 실패시 원래 수량으로 복원
                row.querySelector(".quantity-input").value = quantity + (isPlus ? -1 : 1);
            }
        } catch (error) {
            console.error("수량 업데이트 실패", error); 
            alert("수량 변경 중 문제가 발생했습니다.");
        }
    }
    
    /**
     * 비회원 장바구니 수량 변경
     */
    function updateGuestCartQuantity(productId, quantity, row) {
        const guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        
        // 정수형으로 타입 변환 후 비교
        const itemIndex = guestCart.findIndex(item => parseInt(item.productId) === productId);
        
        if (itemIndex !== -1) {
            guestCart[itemIndex].quantity = quantity;
            localStorage.setItem('guestCart', JSON.stringify(guestCart));
            
            guestCartInfo(); // 전체 카트 정보
        } else {
            console.error("상품을 찾을 수 없음:", productId);
        }
    }
    
    //////////////////////////////////////////////////////////////////
    /**
     * 삭제 버튼 이벤트 설정
     */
    function setupDeleteButtons() {
        document.querySelectorAll(".delete-btn").forEach(btn => {
            btn.addEventListener("click", function() {
                if(confirm("상품을 장바구니에서 삭제하시겠습니까?")) {
                    const row = this.closest("tr");
                    const productId = parseInt(row.dataset.productid);
                    
                    if (isLoggedIn) {
                        // 회원 - DB에서 삭제
                        deleteCartItem(userId, productId, row);
                    } else {
                        // 비회원 - 로컬스토리지에서 삭제
                        deleteGuestCartItem(productId, row);
                    }
                }
            });
        });
    }
    
    /**
     * 회원 장바구니 상품 삭제
     */
    async function deleteCartItem(userId, productId, row) {
        try {
            const response = await fetch(`${contextPath}/ajax`, {
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
            console.log("삭제결과: ", result);

            if(result.success) {
                row.remove();

                // 장바구니 비었는지 확인
                if (result.isEmpty) {
                    window.location.reload(); // 장바구니가 비었으면 새로고침
                } else {
                    // 체크박스 체크된 상품만 계산
                    calculateSelectedTotal();
                }
            } else {
                alert(result.message || "상품 삭제에 실패했습니다.");
            }
        } catch (error) {
            console.error("삭제 실패", error); 
            alert("상품 삭제 중 문제가 발생했습니다.");
        }
    }
    
    /**
     * 비회원 장바구니 상품 삭제
     */
    function deleteGuestCartItem(productId, row) {
        const guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        
        // 정수형으로 타입 변환 후 비교
        const updatedCart = guestCart.filter(item => parseInt(item.productId) !== productId);
        
        localStorage.setItem('guestCart', JSON.stringify(updatedCart));
        row.remove();
        
        if (updatedCart.length === 0) {
            window.location.reload(); // 장바구니가 비었으면 새로고침
        } else {
            // 서버에서 갱신된 데이터 가져오기
            guestCartInfo();
        }
    }
    
    ////////////////////////////////////////////////////////////////////
    /**
     * 체크박스 이벤트 설정
     */
    function setupCheckboxEvents() {
        // 전체 선택 체크박스
        const allCheckbox = document.querySelector("thead input[type=checkbox]");
        if (allCheckbox) {
            // 전체 선택/해제 이벤트
            allCheckbox.addEventListener("change", function() {
                const isChecked = this.checked;
                document.querySelectorAll("tbody input[type=checkbox]").forEach(checkbox => {
                    checkbox.checked = isChecked;
                });
                
                // 체크된 상품 가격 계산
                calculateSelectedTotal();
            });
            
            // 개별 체크박스 이벤트
            document.querySelectorAll("tbody input[type=checkbox]").forEach(checkbox => {
                checkbox.addEventListener('change', function() {
                    // 모든 체크박스 상태 확인 후 업데이트트
                    const allChecked = Array.from(document.querySelectorAll("tbody input[type=checkbox]"))
                        .every(cb => cb.checked);
                    
                    // 헤더 체크박스 상태 업데이트
                    allCheckbox.checked = allChecked;
                    
                    // 체크된 상품 가격 계산
                    calculateSelectedTotal();
                });
            });
        }
    }
    
    /**
     * 체크된 상품만 계산하는 함수
     */
    function calculateSelectedTotal() {
        const checkedProductIds = [...document.querySelectorAll("tbody input[type=checkbox]:checked")].map(checkbox => {
            const row = checkbox.closest("tr");
            return parseInt(row.dataset.productid);
        });
        
        if (checkedProductIds.length === 0) {
            // 선택된 상품이 없으면 모든 가격을 0으로 표시
            updatePriceInfo({
                totalProductPrice: 0,
                totalDiscount: 0,
                deliveryFee: 0,
                totalPayPrice: 0
            });
            return;
        }
        
        if (isLoggedIn) {
            // 회원 - 서버에 선택된 상품들의 가격 계산 요청
            calSelectedCart(userId, checkedProductIds);
        } else {
            // 비회원 - 서버에 선택된 상품들의 가격 계산 요청
            calSelectedGuestCart(checkedProductIds);
        }
    }
    
    /**
     * 회원 장바구니 선택된 상품 총액 계산 요청
     */
    async function calSelectedCart(userId, selectedItems) {
        try {
            const response = await fetch(`${contextPath}/ajax`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: new URLSearchParams({
                    key: "cartRest",
                    methodName: "calculateSelected",
                    userId: userId,
                    selectedItems: JSON.stringify(selectedItems)
                })
            });
            
            if (!response.ok) throw new Error("계산 실패: 서버 오류");
            
            const result = await response.json();
            console.log("선택 상품 계산 결과:", result);
            
            if (result.success) {
                updatePriceInfo(result);
            }
        } catch (error) {
            console.error("선택 상품 계산 실패", error);
        }
    }

    /**
     * 비회원 장바구니 선택된 상품 총액 계산 요청
     */
    async function calSelectedGuestCart(productIds) {
        try {
            // 로컬스토리지에서 선택된 상품 정보 추출
            const guestCart = JSON.parse(localStorage.getItem("guestCart") || "[]");
            console.log("현재 저장된 guestCart:", guestCart); 
            
            const selectedItems = guestCart
                .filter(item => productIds.includes(parseInt(item.productId)))
                .map(item => ({
                    productId: parseInt(item.productId),
                    quantity: parseInt(item.quantity)
                }));

            const response = await fetch(`${contextPath}/ajax?key=cartRest&methodName=calculateGuestCart`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ items: selectedItems })
            });
            
            if (!response.ok) throw new Error("계산 실패: 서버 오류");

            const result = await response.json();
            
            if (result.success) {
                updatePriceInfo(result);
            }
        } catch (error) {
            console.error("비회원 선택 상품 계산 실패", error);
        }
    }
    
    /**
     * 장바구니 가격 정보 업데이트 함수
     */
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
    
    //////////////////////////////////////////////////////////////////
    /**
     * 주문 버튼 이벤트
     */
    function setupOrderButtons() {
        // 개별 주문 버튼
        document.querySelectorAll(".order-btn").forEach(btn => {
            btn.addEventListener("click", function() {
                const row = this.closest("tr");
                const productId = row.dataset.productid;
                const quantity = row.querySelector(".quantity-input").value;
                
                orderProducts([{
                    productId: parseInt(productId),
                    quantity: parseInt(quantity)
                }]);
            });
        });
        
        // 선택 상품 주문 버튼
        const orderSelectedBtn = document.querySelector(".order-selected-btn");
        if (orderSelectedBtn) {
            orderSelectedBtn.addEventListener("click", function() {
                const selectedProducts = [];
                
                document.querySelectorAll("tbody input[type=checkbox]:checked").forEach(checkbox => {
                    const row = checkbox.closest("tr");
                    if (row) {
                        selectedProducts.push({
                            productId: parseInt(row.dataset.productid),
                            quantity: parseInt(row.querySelector(".quantity-input").value)
                        });
                    }
                });
                
                if (selectedProducts.length === 0) {
                    alert("선택된 상품이 없습니다.");
                    return;
                }
                
                orderProducts(selectedProducts);
            });
        }
        
        // 전체 상품 주문 버튼
        const orderAllBtn = document.querySelector(".order-all-btn");
        if (orderAllBtn) {
            orderAllBtn.addEventListener("click", function() {
                const allProducts = [];
                
                document.querySelectorAll("tbody tr").forEach(row => {
                    allProducts.push({
                        productId: parseInt(row.dataset.productid),
                        quantity: parseInt(row.querySelector(".quantity-input").value)
                    });
                });
                
                if (allProducts.length === 0) {
                    alert("장바구니에 상품이 없습니다.");
                    return;
                }
                
                orderProducts(allProducts);
            });
        }
    }
    
    /**
     * 주문 처리 함수
     */
    function orderProducts(products) {
        if (!isLoggedIn) {
            if (confirm("로그인이 필요한 서비스입니다. 로그인 페이지로 이동하시겠습니까?")) {
                localStorage.setItem("pendingOrder", JSON.stringify(products));
                window.location.href = `${contextPath}/user/login.jsp`;
            }
            return;
        }
        
        // 주문 폼 생성 및 제출
        const orderForm = document.createElement('form');
        orderForm.method = 'POST';
        orderForm.action = `${contextPath}/front?key=order&methodName=orderFromCart`;
        
        // 선택한 상품 정보 추가
        const productsInput = document.createElement('input');
        productsInput.type = 'hidden';
        productsInput.name = 'selectedProducts';
        productsInput.value = JSON.stringify(products);
        orderForm.appendChild(productsInput);
        
        document.body.appendChild(orderForm);
        orderForm.submit();
    }
    
    // 비회원 로그인 후 장바구니 이관을 위한 코드
    // 로그인 후 리다이렉트 된 페이지가 장바구니 페이지라면 이관 시도
    if (isLoggedIn && localStorage.getItem('guestCart')) {
        migrateGuestCart(userId);
    }
    
    /**
     * 비회원 장바구니 -> 회원 장바구니 이관
     */
    async function migrateGuestCart(userId) {
        const guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        if (guestCart.length === 0) return;
        
        try {
            const response = await fetch(`${contextPath}/ajax`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },
                body: new URLSearchParams({
                    key: "cartRest",
                    methodName: "migrateGuestCart",
                    userId: userId,
                    guestCart: JSON.stringify(guestCart)
                })
            });
            
            if (!response.ok) throw new Error("이관 실패: 서버 오류");
            
            const result = await response.json();
            console.log("장바구니 이관 결과:", result);
            
            if (result.success) {
                // 이관 성공 시 로컬 장바구니 비우기
                localStorage.removeItem('guestCart');
                // 페이지 새로고침하여 회원 장바구니 표시
                window.location.reload();
            } else {
                alert(result.message || "장바구니 이관에 실패했습니다.");
            }
        } catch (error) {
            console.error("장바구니 이관 실패", error);
            alert("장바구니 이관 중 오류가 발생했습니다.");
        }
    }
});