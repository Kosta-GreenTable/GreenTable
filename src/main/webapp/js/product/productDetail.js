document.addEventListener('DOMContentLoaded', () => {
  document.querySelector('.add-to-cart').addEventListener("click", (e) => {
    e.preventDefault();
  
    //const userId = sessionStorage.getItem("userId");
    const userId = document.getElementById('userId')?.value;
    const productId = parseInt(e.target.dataset.productId);
    const quantity = parseInt(document.getElementById('quantity').value);
  
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
    alert('장바구니에 담겼습니다.(비회원)');
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
      alert("장바구니에 담겼습니다.");
      return result;
    } catch (err) {
      console.error("장바구니 추가 실패:", err);
      alert("장바구니 담기에 실패했습니다.");
    }
  }

});