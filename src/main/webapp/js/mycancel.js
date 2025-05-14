/**
 * 취소/반품 내역 페이지 자바스크립트
 */
document.addEventListener("DOMContentLoaded", function () {
  // 샘플 데이터 - 실제로는 서버에서 받아와야 함
  const cancelReturnData = [
    {
      type: "cancel", // 취소인지 반품인지
      status: "취소완료",
      date: "2025.05.01",
      orderNumber: "2025050101",
      productImage: "https://picsum.photos/seed/product101/100/100",
      productName: "유기농 채소 건강 도시락",
      productOption: "기본 구성 / 수량: 1개",
      productPrice: "9,800원",
      reason: "단순변심",
      refundAmount: "9,800원",
      refundDate: "2025.05.02",
    },
    {
      type: "return", // 취소인지 반품인지
      status: "반품완료",
      date: "2025.04.15",
      orderNumber: "2025041501",
      productImage: "https://picsum.photos/seed/product102/100/100",
      productName: "프리미엄 샐러드 세트",
      productOption: "비건 소스 / 수량: 2개",
      productPrice: "17,000원",
      reason: "상품 품질 불만",
      refundAmount: "17,000원",
      refundDate: "2025.04.20",
    },
  ];

  // 페이지 로딩 시 데이터 표시 여부 설정
  const showSampleData = false; // true로 설정하면 샘플 데이터 표시

  // DOM 요소
  const historyList = document.querySelector(".cancel-return-list");
  const noData = document.querySelector(".no-data");
  const periodBtns = document.querySelectorAll(".period-btn");
  const tabBtns = document.querySelectorAll(".tab-btn");
  const sortSelect = document.querySelector('select[name="sortOrder"]');
  const pageInfo = document.querySelector(".page-info");

  // 샘플 데이터 표시
  if (showSampleData) {
    loadData(cancelReturnData);
  }

  /**
   * 데이터 로드 및 표시
   * @param {Array} data - 취소/반품 내역 데이터
   */
  function loadData(data) {
    if (data.length > 0) {
      noData.style.display = "none";
      historyList.innerHTML = "";

      data.forEach((item) => {
        const historyItem = document.createElement("div");
        historyItem.className = "history-item";
        historyItem.innerHTML = `
          <div class="history-header">
            <div class="status-badge ${item.type}">${item.status}</div>
            <span class="date">${item.date}</span>
            <span class="order-number">주문번호: ${item.orderNumber}</span>
          </div>
          <div class="history-content">
            <div class="product-image">
              <img src="${item.productImage}" alt="상품이미지">
            </div>
            <div class="product-info">
              <h4 class="product-name">${item.productName}</h4>
              <p class="product-option">${item.productOption}</p>
              <p class="product-price">${item.productPrice}</p>
            </div>
            <div class="history-reason">
              <p class="reason-title">${
                item.type === "cancel" ? "취소사유" : "반품사유"
              }</p>
              <p class="reason-text">${item.reason}</p>
            </div>
            <div class="history-status">
              <p class="status-text">${item.status}</p>
              <p class="refund-info">환불금액: ${item.refundAmount}</p>
              <p class="refund-date">환불일자: ${item.refundDate}</p>
            </div>
          </div>
        `;
        historyList.appendChild(historyItem);
      });

      // 페이지 정보 업데이트
      pageInfo.textContent = `1-${data.length} / ${data.length}`;
    } else {
      noData.style.display = "block";
      pageInfo.textContent = "1-0 / 0";
    }
  }

  // 이벤트 리스너 등록

  // 기간 버튼 클릭
  periodBtns.forEach((btn) => {
    btn.addEventListener("click", function () {
      periodBtns.forEach((b) => b.classList.remove("active"));
      this.classList.add("active");

      // TODO: 선택된 기간에 따라 데이터를 필터링하는 로직 구현
      // 예시 목적으로 샘플 데이터를 표시
      if (showSampleData) {
        loadData(cancelReturnData);
      }
    });
  });

  // 탭 버튼 클릭 (전체/취소/반품)
  tabBtns.forEach((btn) => {
    btn.addEventListener("click", function () {
      tabBtns.forEach((b) => b.classList.remove("active"));
      this.classList.add("active");

      // 선택된 탭에 따라 데이터 필터링
      const tabType = this.textContent.toLowerCase();
      if (showSampleData) {
        if (tabType === "전체") {
          loadData(cancelReturnData);
        } else if (tabType === "취소") {
          loadData(cancelReturnData.filter((item) => item.type === "cancel"));
        } else if (tabType === "반품") {
          loadData(cancelReturnData.filter((item) => item.type === "return"));
        }
      }
    });
  });

  // 정렬 방식 변경
  sortSelect?.addEventListener("change", function () {
    if (showSampleData) {
      const sortedData = [...cancelReturnData];
      if (this.value === "latest") {
        // 최신순 정렬
        sortedData.sort((a, b) => new Date(b.date) - new Date(a.date));
      } else {
        // 오래된순 정렬
        sortedData.sort((a, b) => new Date(a.date) - new Date(b.date));
      }
      loadData(sortedData);
    }
  });

  // 프로필 수정 버튼 클릭
  const profileEditBtn = document.querySelector(".profile-edit-btn");
  if (profileEditBtn) {
    profileEditBtn.addEventListener("click", function () {
      window.location.href = "myinfo.html";
    });
  }
});
