// 주문 완료 페이지 스크립트
document.addEventListener("DOMContentLoaded", function () {
  // 세션 스토리지에서 주문 데이터 로딩
  const orderNumber =
    sessionStorage.getItem("orderNumber") || "20230512AB123456";
  const orderItems = JSON.parse(sessionStorage.getItem("orderItems") || "[]");
  const paymentInfo = JSON.parse(sessionStorage.getItem("paymentInfo") || "{}");
  const paymentMethod = sessionStorage.getItem("paymentMethod") || "card";

  // 금액 포맷 함수
  function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
  }

  // 주문 번호 표시
  document.getElementById("order-id").textContent = orderNumber;

  // 결제 수단 표시
  let paymentMethodText = "신용카드";

  switch (paymentMethod) {
    case "card":
      paymentMethodText = "신용카드";
      break;
    case "virtual":
      paymentMethodText = "가상계좌";
      break;
    case "bank":
      paymentMethodText = "계좌이체";
      break;
    case "toss":
      paymentMethodText = "토스페이";
      break;
    case "naver":
      paymentMethodText = "네이버페이";
      break;
    case "payco":
      paymentMethodText = "페이코";
      break;
    case "samsung":
      paymentMethodText = "삼성페이";
      break;
    default:
      paymentMethodText = "신용카드";
  }

  document.getElementById("payment-method").textContent = paymentMethodText;

  // 주문 일자 표시
  const orderDate = paymentInfo.orderDate
    ? new Date(paymentInfo.orderDate)
    : new Date();

  const year = orderDate.getFullYear();
  const month = String(orderDate.getMonth() + 1).padStart(2, "0");
  const day = String(orderDate.getDate()).padStart(2, "0");
  const hours = String(orderDate.getHours()).padStart(2, "0");
  const minutes = String(orderDate.getMinutes()).padStart(2, "0");

  document.getElementById(
    "order-date"
  ).textContent = `${year}-${month}-${day} ${hours}:${minutes}`;

  // 가격 정보 표시
  let totalPrice = 0;

  // 상품별 가격 계산
  if (orderItems.length > 0) {
    orderItems.forEach((item) => {
      totalPrice += item.price * item.quantity;
    });
  } else {
    // 테스트용 더미 데이터를 사용하는 경우
    totalPrice = paymentInfo.totalProductPrice || 23700;
  }

  const shippingCost = paymentInfo.shippingCost || 3000;
  const discountAmount =
    (paymentInfo.couponDiscount || 0) + (paymentInfo.pointDiscount || 0) ||
    1800;
  const finalPrice = totalPrice + shippingCost - discountAmount;

  document.getElementById("product-price").textContent =
    formatPrice(totalPrice);
  document.getElementById("shipping-cost").textContent =
    formatPrice(shippingCost);
  document.getElementById("discount-amount").textContent =
    "-" + formatPrice(discountAmount);
  document.getElementById("final-price").textContent = formatPrice(finalPrice);

  // 세션 스토리지 데이터 정리 (실제 운영시에는 유지가 필요할 수 있음)
  // 주문 완료 후에도 확인 가능하도록 주문 정보는 유지하고,
  // 주문과 관련된 임시 데이터만 삭제하는 것이 좋습니다.
  // sessionStorage.removeItem('orderItems');
  // sessionStorage.removeItem('paymentInfo');
  // sessionStorage.removeItem('paymentMethod');
});
