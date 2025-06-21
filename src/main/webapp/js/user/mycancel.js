/**
 * 취소/환불 내역 페이지 JavaScript
 * - 취소/환불 데이터 처리
 * - 필터링 및 정렬 기능
 * - UI 상호작용 처리
 */

document.addEventListener("DOMContentLoaded", function () {
  console.log("취소/환불 내역 페이지 로드 완료");

  // 필터링 버튼 이벤트 처리
  initFilterButtons();

  // 상태별 필터 이벤트 처리
  initStatusFilter();

  // 검색 기능 초기화
  initSearch();

  // 상세보기 모달 초기화
  initModal();

  // 취소 요청 버튼 이벤트 처리
  initCancelRequestButtons();
});

/**
 * 기간 필터 버튼 초기화
 */
function initFilterButtons() {
  const filterButtons = document.querySelectorAll(".filter-btn");

  filterButtons.forEach((button) => {
    button.addEventListener("click", function () {
      // 모든 버튼에서 active 클래스 제거
      filterButtons.forEach((btn) => btn.classList.remove("active"));
      
      // 클릭된 버튼에 active 클래스 추가
      this.classList.add("active");

      // 필터링 적용
      const period = this.getAttribute("data-period");
      applyPeriodFilter(period);
    });
  });
}

/**
 * 상태별 필터 초기화
 */
function initStatusFilter() {
  const statusSelect = document.getElementById("statusFilter");
  
  if (statusSelect) {
    statusSelect.addEventListener("change", function () {
      const status = this.value;
      applyStatusFilter(status);
    });
  }
}

/**
 * 검색 기능 초기화
 */
function initSearch() {
  const searchInput = document.getElementById("searchInput");
  const searchButton = document.querySelector(".search-btn");

  if (searchButton) {
    searchButton.addEventListener("click", function () {
      const keyword = searchInput.value.trim();
      applySearch(keyword);
    });
  }

  if (searchInput) {
    searchInput.addEventListener("keypress", function (e) {
      if (e.key === "Enter") {
        const keyword = this.value.trim();
        applySearch(keyword);
      }
    });
  }
}

/**
 * 상세보기 모달 초기화
 */
function initModal() {
  const detailButtons = document.querySelectorAll(".btn-detail");
  const modal = document.getElementById("cancelDetailModal");
  const closeModal = document.querySelector(".close-modal");

  detailButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const orderId = this.getAttribute("data-order-id");
      showCancelDetail(orderId);
    });
  });

  if (closeModal) {
    closeModal.addEventListener("click", function () {
      modal.style.display = "none";
    });
  }

  // 모달 외부 클릭 시 닫기
  window.addEventListener("click", function (event) {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  });
}

/**
 * 취소 요청 버튼 초기화
 */
function initCancelRequestButtons() {
  const cancelButtons = document.querySelectorAll(".btn-cancel-request");

  cancelButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const orderId = this.getAttribute("data-order-id");
      const orderNumber = this.getAttribute("data-order-number");
      
      if (confirm(`주문번호 ${orderNumber}의 취소를 요청하시겠습니까?`)) {
        requestCancel(orderId);
      }
    });
  });
}

/**
 * 기간별 필터 적용
 */
function applyPeriodFilter(period) {
  // 현재 URL 파라미터 업데이트
  const urlParams = new URLSearchParams(window.location.search);
  urlParams.set("period", period);
  
  // 페이지 새로고침 또는 AJAX 요청
  const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
  window.location.href = newUrl;
}

/**
 * 상태별 필터 적용
 */
function applyStatusFilter(status) {
  const urlParams = new URLSearchParams(window.location.search);
  urlParams.set("status", status);
  
  const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
  window.location.href = newUrl;
}

/**
 * 검색 필터 적용
 */
function applySearch(keyword) {
  const urlParams = new URLSearchParams(window.location.search);
  
  if (keyword) {
    urlParams.set("search", keyword);
  } else {
    urlParams.delete("search");
  }
  
  const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
  window.location.href = newUrl;
}

/**
 * 취소/환불 상세 정보 표시
 */
function showCancelDetail(orderId) {
  // AJAX로 상세 정보 조회
  fetch(`${contextPath}/front?key=mypage&methodName=getCancelDetail&orderId=${orderId}`)
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        displayCancelDetail(data.detail);
      } else {
        alert("상세 정보를 불러올 수 없습니다.");
      }
    })
    .catch(error => {
      console.error("Error:", error);
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    });
}

/**
 * 취소/환불 상세 정보 화면에 표시
 */
function displayCancelDetail(detail) {
  const modal = document.getElementById("cancelDetailModal");
  
  // 모달 내용 업데이트
  document.getElementById("modalOrderNumber").textContent = detail.orderNumber;
  document.getElementById("modalOrderDate").textContent = detail.orderDate;
  document.getElementById("modalCancelDate").textContent = detail.cancelDate;
  document.getElementById("modalCancelReason").textContent = detail.cancelReason;
  document.getElementById("modalRefundAmount").textContent = detail.refundAmount;
  document.getElementById("modalRefundMethod").textContent = detail.refundMethod;
  document.getElementById("modalProcessStatus").textContent = detail.processStatus;
  
  // 상품 목록 업데이트
  const productList = document.getElementById("modalProductList");
  productList.innerHTML = "";
  
  detail.products.forEach(product => {
    const productItem = document.createElement("div");
    productItem.className = "product-item";
    productItem.innerHTML = `
      <img src="${product.image}" alt="${product.name}" class="product-image">
      <div class="product-info">
        <h4>${product.name}</h4>
        <p>수량: ${product.quantity}개</p>
        <p>가격: ${product.price}원</p>
      </div>
    `;
    productList.appendChild(productItem);
  });
  
  modal.style.display = "block";
}

/**
 * 취소 요청 처리
 */
function requestCancel(orderId) {
  // AJAX로 취소 요청 전송
  fetch(`${contextPath}/front?key=order&methodName=requestCancel`, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: `orderId=${orderId}`
  })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        alert("취소 요청이 완료되었습니다.");
        location.reload(); // 페이지 새로고침
      } else {
        alert(data.message || "취소 요청에 실패했습니다.");
      }
    })
    .catch(error => {
      console.error("Error:", error);
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    });
}

/**
 * 환불 처리 상태 업데이트
 */
function updateRefundStatus(orderId, status) {
  fetch(`${contextPath}/front?key=admin&methodName=updateRefundStatus`, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: `orderId=${orderId}&status=${status}`
  })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        alert("환불 상태가 업데이트되었습니다.");
        location.reload();
      } else {
        alert(data.message || "상태 업데이트에 실패했습니다.");
      }
    })
    .catch(error => {
      console.error("Error:", error);
      alert("오류가 발생했습니다.");
    });
}

/**
 * 날짜 형식 변환 (YYYY-MM-DD to YYYY.MM.DD)
 */
function formatDate(dateString) {
  if (!dateString) return "";
  return dateString.replace(/-/g, ".");
}

/**
 * 금액 형식 변환 (1000 to 1,000원)
 */
function formatPrice(price) {
  if (!price) return "0원";
  return parseInt(price).toLocaleString() + "원";
}

/**
 * 상태에 따른 배지 클래스 반환
 */
function getStatusBadgeClass(status) {
  switch (status) {
    case "CANCEL_REQUESTED":
      return "status-requested";
    case "CANCEL_APPROVED":
      return "status-approved";
    case "REFUND_PROCESSING":
      return "status-processing";
    case "REFUND_COMPLETED":
      return "status-completed";
    case "CANCEL_REJECTED":
      return "status-rejected";
    default:
      return "status-default";
  }
}

/**
 * 페이지 로딩 시 필터 상태 복원
 */
function restoreFilterState() {
  const urlParams = new URLSearchParams(window.location.search);
  
  // 기간 필터 복원
  const period = urlParams.get("period") || "3";
  const periodButton = document.querySelector(`[data-period="${period}"]`);
  if (periodButton) {
    document.querySelectorAll(".filter-btn").forEach(btn => btn.classList.remove("active"));
    periodButton.classList.add("active");
  }
  
  // 상태 필터 복원
  const status = urlParams.get("status") || "all";
  const statusSelect = document.getElementById("statusFilter");
  if (statusSelect) {
    statusSelect.value = status;
  }
  
  // 검색어 복원
  const search = urlParams.get("search");
  const searchInput = document.getElementById("searchInput");
  if (searchInput && search) {
    searchInput.value = search;
  }
}

// 페이지 로드 완료 후 필터 상태 복원
document.addEventListener("DOMContentLoaded", function () {
  restoreFilterState();
});
