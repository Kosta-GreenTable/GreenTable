/**
 * 마이페이지 자바스크립트
 * - 주문 내역 데이터 처리
 * - UI 상호작용
 */
document.addEventListener("DOMContentLoaded", function () {
  // 샘플 데이터 - 실제로는 서버에서 받아와야 함
  const sampleOrders = [
    {
      orderDate: "2025.05.08",
      orderNumber: "2025050801",
      productImage: "https://picsum.photos/seed/product101/100/100",
      productName: "유기농 채소 건강 도시락",
      productOption: "기본 구성 / 수량: 1개",
      productPrice: "9,800원",
      orderStatus: "배송완료",
    },
    {
      orderDate: "2025.05.02",
      orderNumber: "2025050201",
      productImage: "https://picsum.photos/seed/product102/100/100",
      productName: "프리미엄 샐러드 세트",
      productOption: "비건 소스 / 수량: 2개",
      productPrice: "17,000원",
      orderStatus: "배송완료",
    },
  ];

  const sampleInquiries = [
    {
      status: "답변완료",
      title: "배송 관련 문의드립니다.",
      date: "2025.05.01",
    },
    {
      status: "접수완료",
      title: "주문 취소 문의",
      date: "2025.04.28",
    },
  ];

  const sampleProducts = [
    {
      image: "https://picsum.photos/seed/product201/150/150",
      name: "유기농 채소 건강 도시락",
      price: "9,800원",
    },
    {
      image: "https://picsum.photos/seed/product202/150/150",
      name: "프리미엄 샐러드 세트",
      price: "8,500원",
    },
    {
      image: "https://picsum.photos/seed/product203/150/150",
      name: "제철 과일 바구니",
      price: "12,500원",
    },
  ];

  // 페이지 로딩 시 데이터 표시 여부 설정
  const showSampleData = false; // true로 설정하면 샘플 데이터 표시

  // DOM 요소
  const orderList = document.querySelector(".order-list");
  const noOrders = document.querySelector(".no-orders");
  const orderItems = document.querySelector(".order-item");

  const inquiryList = document.querySelector(".inquiry-list");
  const noInquiry = document.querySelector(".no-inquiry");
  const inquiryItems = document.querySelector(".inquiry-item");

  const productList = document.querySelector(".product-list");
  const noProducts = document.querySelector(".no-products");
  const productGrid = document.querySelector(".product-grid");

  // 샘플 데이터 표시
  if (showSampleData) {
    loadSampleData();
  }

  /**
   * 샘플 데이터 로드 및 화면 표시
   */
  function loadSampleData() {
    // 주문 내역 표시
    if (sampleOrders.length > 0) {
      noOrders.style.display = "none";
      orderList.innerHTML = sampleOrders
        .map((order) => createOrderItemHTML(order))
        .join("");
    }

    // 문의 내역 표시
    if (sampleInquiries.length > 0) {
      noInquiry.style.display = "none";
      inquiryList.innerHTML = sampleInquiries
        .map((inquiry) => createInquiryItemHTML(inquiry))
        .join("");
    }

    // 최근 본 상품 표시
    if (sampleProducts.length > 0) {
      noProducts.style.display = "none";
      productGrid.style.display = "flex";
      productGrid.innerHTML = sampleProducts
        .map((product) => createProductItemHTML(product))
        .join("");
    }
  }

  /**
   * 주문 항목 HTML 생성
   * @param {Object} order - 주문 정보 객체
   * @returns {string} 주문 항목 HTML
   */
  function createOrderItemHTML(order) {
    return `
      <div class="order-item">
        <div class="order-header">
          <span class="order-date">${order.orderDate}</span>
          <span class="order-number">주문번호: ${order.orderNumber}</span>
          <a href="#" class="order-detail">주문상세보기 <i class="fas fa-angle-right"></i></a>
        </div>
        <div class="order-content">
          <div class="product-image">
            <img src="${order.productImage}" alt="상품 이미지">
          </div>
          <div class="order-info">
            <p class="product-name">${order.productName}</p>
            <p class="product-option">${order.productOption}</p>
            <p class="product-price">${order.productPrice}</p>
          </div>
          <div class="order-status">
            <p class="status">${order.orderStatus}</p>
            <div class="action-buttons">
              <button class="btn-review">리뷰쓰기</button>
              <button class="btn-reorder">재주문</button>
            </div>
          </div>
        </div>
      </div>
    `;
  }

  /**
   * 문의 항목 HTML 생성
   * @param {Object} inquiry - 문의 정보 객체
   * @returns {string} 문의 항목 HTML
   */
  function createInquiryItemHTML(inquiry) {
    return `
      <div class="inquiry-item">
        <div class="inquiry-title">
          <span class="badge">${inquiry.status}</span>
          <span class="title">${inquiry.title}</span>
        </div>
        <div class="inquiry-date">${inquiry.date}</div>
        <div class="inquiry-status">
          <a href="#" class="inquiry-detail">상세보기 <i class="fas fa-angle-right"></i></a>
        </div>
      </div>
    `;
  }

  /**
   * 상품 항목 HTML 생성
   * @param {Object} product - 상품 정보 객체
   * @returns {string} 상품 항목 HTML
   */
  function createProductItemHTML(product) {
    return `
      <div class="product-card">
        <div class="product-img">
          <img src="${product.image}" alt="상품 이미지">
        </div>
        <div class="product-info">
          <h4 class="product-name">${product.name}</h4>
          <p class="product-price">${product.price}</p>
        </div>
      </div>
    `;
  }

  // 이벤트 리스너 등록

  // 프로필 수정 버튼 클릭
  const profileEditBtn = document.querySelector(".profile-edit-btn");
  if (profileEditBtn) {
    profileEditBtn.addEventListener("click", function () {
      // 회원정보 수정 페이지로 이동
      window.location.href = "#";
    });
  }

  // 더보기 버튼 클릭 - 주문내역
  const btnViewMoreOrders = document.querySelector(
    ".recent-orders .btn-view-more"
  );
  if (btnViewMoreOrders) {
    btnViewMoreOrders.addEventListener("click", function (e) {
      e.preventDefault();
      // 주문내역 전체 페이지로 이동
      window.location.href = "#";
    });
  }

  // 더보기 버튼 클릭 - 문의내역
  const btnViewMoreInquiry = document.querySelector(
    ".my-inquiry .btn-view-more"
  );
  if (btnViewMoreInquiry) {
    btnViewMoreInquiry.addEventListener("click", function (e) {
      e.preventDefault();
      // 문의내역 전체 페이지로 이동
      window.location.href = "#";
    });
  }

  // 1:1 문의하기 버튼 클릭
  const btnInquiry = document.querySelector(".btn-inquiry");
  if (btnInquiry) {
    btnInquiry.addEventListener("click", function (e) {
      e.preventDefault();
      // 문의 작성 페이지로 이동
      window.location.href = "#";
    });
  }
});
