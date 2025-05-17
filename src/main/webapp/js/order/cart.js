document.addEventListener("DOMContentLoaded", () => { 
    // 현재 사용자 정보
    const userId = document.body.dataset.userid || '';
    const isLoggedIn = !!userId;
    const contextPath = document.body.dataset.contextpath || '';
    
    // 숫자 포맷 함수
    function formatNumber(num) {
        return new Intl.NumberFormat().format(num);
    }
    
    // 페이지 초기화 - 회원/비회원 장바구니 데이터 로드
    initCart();
    
    /**
     * 페이지 초기화
     */
    function initCart() {
        if (!isLoggedIn) {
            // 비회원 장바구니 데이터 로드 및 화면 표시
            renderGuestCart();
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
    function renderGuestCart() {
        const guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        
        if (guestCart.length === 0) {
            // 비회원 장바구니가 비어있는 경우 처리
            return; // 기본 빈 장바구니 화면 유지
        }

        // 비회원 장바구니가 있으면 빈 장바구니 메시지를 숨김
        const emptyContainer = document.querySelector('.empty-cart-container');
        if (emptyContainer) {
            emptyContainer.style.display = 'none';
        }
// 장바구니 테이블이 없으면 생성
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
    
    // 요소들을 컨테이너에 추가
    container.appendChild(cartTable);
    container.appendChild(priceContainer);
    container.appendChild(buttonContainer);
}

        /////////////////////////////////////////////////
        // 비회원 장바구니가 있는 경우 테이블 채우기
        const tableBody = document.querySelector('.cart-table tbody');
        if (!tableBody) return;
        
        tableBody.innerHTML = '';
        
        guestCart.forEach(item => {
            const discountedPrice = item.price * (1 - (item.discountRate / 100));
            const totalPrice = discountedPrice * item.quantity;
            
            const row = document.createElement('tr');
            row.dataset.productid = item.productId;
            row.dataset.price = item.price;
            row.dataset.discountrate = item.discountRate;
            
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
        
        // 가격 정보 업데이트
        updateGuestCartPriceInfo();
    }
    
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
                    // 회원: 서버에 수량 변경 요청
                    updateCartQuantity(userId, productId, quantity, row);
                } else {
                    // 비회원: 로컬스토리지에 수량 변경 저장
                    updateGuestCartQuantity(productId, quantity, row);
                }
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
            console.log("수량업데이트: ", result);

            if (result.success) {
                // 서버에서 계산된 값으로 상품 가격 업데이트
                const priceCell = row.querySelector(".product-price");
                priceCell.textContent = `${formatNumber(result.itemTotal)}원`;

                // 체크박스 체크된 상품만 계산에 반영
                calculateSelectedTotal();
            } else {
                alert(result.message || "수량 변경에 실패했습니다.");
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
        const item = guestCart.find(item => item.productId === productId);
        
        if (item) {
            item.quantity = quantity;
            localStorage.setItem('guestCart', JSON.stringify(guestCart));
            
            // 화면에 표시된 가격 업데이트
            const price = parseInt(row.dataset.price);
            const discountRate = parseInt(row.dataset.discountrate) || 0;
            const discountedPrice = price * (1 - (discountRate / 100));
            const totalPrice = discountedPrice * quantity;
            
            const priceCell = row.querySelector(".product-price");
            priceCell.textContent = `${formatNumber(totalPrice)}원`;
            
            // 장바구니 가격 정보 업데이트
            updateGuestCartPriceInfo();
        }
    }
    
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
                        // 회원: 서버에 삭제 요청
                        deleteCartItem(userId, productId, row);
                    } else {
                        // 비회원: 로컬스토리지에서 삭제
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
        const updatedCart = guestCart.filter(item => item.productId !== productId);
        
        localStorage.setItem('guestCart', JSON.stringify(updatedCart));
        row.remove();
        
        if (updatedCart.length === 0) {
            window.location.reload(); // 장바구니가 비었으면 새로고침
        } else {
            // 장바구니 가격 정보 업데이트
            updateGuestCartPriceInfo();
        }
    }
    
    /**
     * 비회원 장바구니 가격 정보 계산 및 업데이트
     */
    function updateGuestCartPriceInfo() {
        const guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        
        // 체크된 상품만 계산에 포함
        const checkedItems = [];
        document.querySelectorAll('.cart-table tbody tr').forEach(row => {
            const checkbox = row.querySelector('.cart-checkbox');
            if (checkbox && checkbox.checked) {
                const productId = parseInt(row.dataset.productid);
                const item = guestCart.find(item => item.productId === productId);
                if (item) {
                    checkedItems.push(item);
                }
            }
        });
        
        let totalProductPrice = 0; // 총 상품금액
        let totalDiscount = 0;     // 총 할인금액
        
        checkedItems.forEach(item => {
            const itemTotal = item.price * item.quantity;
            totalProductPrice += itemTotal;
            
            if (item.discountRate > 0) {
                const discountAmount = Math.floor(itemTotal * (item.discountRate / 100));
                totalDiscount += discountAmount;
            }
        });
        
        // 배송비 계산 (5만원 이상 무료, 기본 3500원)
        const deliveryFee = totalProductPrice >= 50000 ? 0 : 3500;
        
        // 결제 금액 (총상품금액 - 총할인금액 + 배송비)
        const totalPayPrice = totalProductPrice - totalDiscount + deliveryFee;
        
        // 화면에 가격 정보 업데이트
        const priceBlocks = document.querySelectorAll(".price-cell");
        if(priceBlocks.length >= 4) {
            priceBlocks[0].textContent = `${formatNumber(totalProductPrice)}원`;
            priceBlocks[1].textContent = `${formatNumber(totalDiscount)}원`;
            priceBlocks[2].textContent = `${formatNumber(deliveryFee)}원`;
            priceBlocks[3].textContent = `${formatNumber(totalPayPrice)}원`;
        }
    }
    
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
                    // 모든 체크박스 상태 확인
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
        if (isLoggedIn) {
            // 회원: 체크된 상품 ID 목록 생성하여 서버에 전송
            const checkedProductIds = [];
            document.querySelectorAll("tbody input[type=checkbox]:checked").forEach(checkbox => {
                const row = checkbox.closest('tr');
                if (row) {
                    checkedProductIds.push(parseInt(row.dataset.productid));
                }
            });
            
            // 서버에 체크된 상품 계산 요청
            fetchSelectedTotal(userId, checkedProductIds);
        } else {
            // 비회원: 로컬스토리지 데이터로 계산
            updateGuestCartPriceInfo();
        }
    }
    
    /**
     * 회원 장바구니 선택된 상품 총액 계산 요청
     */
    async function fetchSelectedTotal(userId, productIds) {
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
                    productIds: JSON.stringify(productIds)
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
    
    // 쇼핑 계속하기 버튼 클릭 이벤트
    const continueBtn = document.querySelector(".continue-btn");
    if (continueBtn) {
        continueBtn.addEventListener("click", function() {
            window.location.href = `${contextPath}/`;
        });
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