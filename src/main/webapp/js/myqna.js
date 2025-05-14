/**
 * 상품 문의 페이지 자바스크립트
 */
document.addEventListener("DOMContentLoaded", function () {
  // 샘플 데이터 - 실제로는 서버에서 받아와야 함
  const qnaData = [
    {
      id: 1,
      productImage: "https://picsum.photos/seed/product101/50/50",
      productName: "유기농 채소 건강 도시락",
      title: "배송 관련 문의드립니다.",
      date: "2025.05.01",
      status: "waiting", // 답변 대기
      content:
        "안녕하세요. 주문한 상품의 배송이 예상보다 늦어지고 있는데 언제쯤 받을 수 있을까요?",
      answer: {
        title: "배송 관련 문의에 대한 답변입니다.",
        content:
          "안녕하세요, 그린테이블입니다. 고객님의 주문은 오늘 오후에 출고될 예정입니다. 내일 중으로 배송될 예정이니 조금만 더 기다려주세요. 불편을 드려 죄송합니다.",
        date: "2025.05.02",
      },
      isPrivate: false,
    },
    {
      id: 2,
      productImage: "https://picsum.photos/seed/product102/50/50",
      productName: "프리미엄 샐러드 세트",
      title: "상품 구성 문의드립니다.",
      date: "2025.04.28",
      status: "completed", // 답변 완료
      content:
        "프리미엄 샐러드 세트에 어떤 채소들이 포함되어 있나요? 알레르기가 있어서 확인하고 싶습니다.",
      answer: {
        title: "상품 구성 문의에 대한 답변입니다.",
        content:
          "안녕하세요, 그린테이블입니다. 저희 프리미엄 샐러드 세트에는 양상추, 로메인, 시금치, 아루굴라, 방울토마토, 오이, 당근이 포함되어 있습니다. 드레싱은 올리브유 베이스의 발사믹 드레싱이 제공됩니다. 알레르기 성분이 우려되시면 주문시 요청사항에 기재해 주시면 특정 채소를 제외해드릴 수 있습니다.",
        date: "2025.04.29",
      },
      isPrivate: false,
    },
  ];

  // 페이지 로딩 시 데이터 표시 여부 설정
  const showSampleData = false; // true로 설정하면 샘플 데이터 표시

  // DOM 요소
  const qnaTable = document.querySelector(".qna-table");
  const qnaTbody = qnaTable.querySelector("tbody");
  const noData = document.querySelector(".no-data");
  const periodBtns = document.querySelectorAll(".period-btn");
  const answerStatusSelect = document.querySelector(
    'select[name="answerStatus"]'
  );
  const pageInfo = document.querySelector(".page-info");

  // 문의 작성 모달 관련 요소
  const newQuestionBtn = document.querySelector(".btn-new-question");
  const questionModal = document.getElementById("questionModal");
  const closeModalBtn = document.querySelector(".close-modal");
  const cancelBtn = document.querySelector(".cancel-btn");
  const questionText = document.getElementById("questionText");
  const textLength = document.querySelector(".text-length");
  const questionForm = document.getElementById("questionForm");

  // 샘플 데이터 표시
  if (showSampleData) {
    loadData(qnaData);
  }

  /**
   * 데이터 로드 및 표시
   * @param {Array} data - 문의 내역 데이터
   */
  function loadData(data) {
    // 기존 행 제거 (no-data 행은 제외)
    const existingRows = qnaTbody.querySelectorAll("tr:not(.no-data)");
    existingRows.forEach((row) => row.remove());

    if (data.length > 0) {
      noData.style.display = "none";

      data.forEach((item, index) => {
        // 문의 행 생성
        const qnaRow = document.createElement("tr");
        qnaRow.className = "qna-item";
        qnaRow.innerHTML = `
          <td class="qna-num">${item.id}</td>
          <td class="qna-product">
            <div class="product-info-cell">
              <img src="${item.productImage}" alt="상품이미지">
              <span>${item.productName}</span>
            </div>
          </td>
          <td class="qna-title">${item.title}</td>
          <td class="qna-date">${item.date}</td>
          <td class="qna-status ${item.status}">${
          item.status === "waiting" ? "답변 대기" : "답변 완료"
        }</td>
        `;

        // 문의 내용 행 생성
        const contentRow = document.createElement("tr");
        contentRow.className = "qna-content";
        contentRow.style.display = "none";

        let answerHTML = "";
        if (item.status === "completed") {
          answerHTML = `
            <div class="answer-content">
              <div class="answer-header">
                <span class="answer-label">A</span>
                <span class="answer-title">${item.answer.title}</span>
              </div>
              <div class="answer-body">
                <p>${item.answer.content}</p>
              </div>
              <div class="answer-footer">
                <div class="answer-date">
                  <span>답변일: ${item.answer.date}</span>
                </div>
              </div>
            </div>
          `;
        } else {
          answerHTML = `
            <div class="answer-content" style="display: none;">
              <div class="answer-header">
                <span class="answer-label">A</span>
                <span class="answer-title">답변 준비 중입니다.</span>
              </div>
              <div class="answer-body">
                <p>문의하신 내용에 대한 답변을 준비 중입니다. 빠른 시일 내에 답변 드리겠습니다.</p>
              </div>
            </div>
          `;
        }

        contentRow.innerHTML = `
          <td colspan="5">
            <div class="question-content">
              <div class="question-header">
                <span class="question-label">Q</span>
                <span class="question-title">${item.title}</span>
              </div>
              <div class="question-body">
                <p>${item.content}</p>
              </div>
              <div class="question-footer">
                <div class="question-actions">
                  <button class="btn-edit">수정</button>
                  <button class="btn-delete">삭제</button>
                </div>
              </div>
            </div>
            ${answerHTML}
          </td>
        `;

        // 문의 행 클릭 시 내용 토글
        qnaRow.addEventListener("click", function () {
          const isHidden = contentRow.style.display === "none";
          contentRow.style.display = isHidden ? "table-row" : "none";
        });

        // 문의 수정 버튼 이벤트
        const editButton = contentRow.querySelector(".btn-edit");
        if (editButton) {
          editButton.addEventListener("click", function (e) {
            e.stopPropagation();
            // TODO: 문의 수정 모달 열기
            alert("문의 수정 기능은 준비 중입니다.");
          });
        }

        // 문의 삭제 버튼 이벤트
        const deleteButton = contentRow.querySelector(".btn-delete");
        if (deleteButton) {
          deleteButton.addEventListener("click", function (e) {
            e.stopPropagation();
            // TODO: 문의 삭제 확인
            if (confirm("문의를 삭제하시겠습니까?")) {
              alert("문의가 삭제되었습니다.");
              // TODO: 문의 삭제 처리 로직
            }
          });
        }

        // tbody에 행 추가
        qnaTbody.appendChild(qnaRow);
        qnaTbody.appendChild(contentRow);
      });

      // 페이지 정보 업데이트
      pageInfo.textContent = `1-${data.length} / ${data.length}`;
    } else {
      noData.style.display = "table-row";
      pageInfo.textContent = "1-0 / 0";
    }
  }

  /**
   * 모달 열기
   */
  function openModal() {
    // 모달 초기화
    resetModal();

    // 모달 표시
    questionModal.style.display = "flex";
  }

  /**
   * 모달 초기화
   */
  function resetModal() {
    // 선택된 상품 초기화
    const productSelect = document.getElementById("productSelect");
    if (productSelect) productSelect.selectedIndex = 0;

    // 제목 초기화
    const questionTitle = document.getElementById("questionTitle");
    if (questionTitle) questionTitle.value = "";

    // 문의 내용 초기화
    if (questionText) {
      questionText.value = "";
      if (textLength) textLength.textContent = "0/500자";
    }

    // 비밀글 체크박스 초기화
    const privateQuestion = document.getElementById("privateQuestion");
    if (privateQuestion) privateQuestion.checked = false;
  }

  /**
   * 모달 닫기
   */
  function closeModal() {
    questionModal.style.display = "none";
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
        loadData(qnaData);
      }
    });
  });

  // 답변 상태 변경
  if (answerStatusSelect) {
    answerStatusSelect.addEventListener("change", function () {
      // 답변 상태에 따라 필터링
      if (showSampleData) {
        const status = this.value;
        if (status === "all") {
          loadData(qnaData);
        } else if (status === "waiting") {
          loadData(qnaData.filter((item) => item.status === "waiting"));
        } else if (status === "completed") {
          loadData(qnaData.filter((item) => item.status === "completed"));
        }
      }
    });
  }

  // 문의하기 버튼 클릭
  if (newQuestionBtn) {
    newQuestionBtn.addEventListener("click", openModal);
  }

  // 모달 닫기 버튼 클릭
  if (closeModalBtn) {
    closeModalBtn.addEventListener("click", closeModal);
  }

  // 모달 취소 버튼 클릭
  if (cancelBtn) {
    cancelBtn.addEventListener("click", closeModal);
  }

  // 모달 바깥쪽 클릭 시 모달 닫기
  window.addEventListener("click", function (event) {
    if (event.target === questionModal) {
      closeModal();
    }
  });

  // 문의 내용 글자 수 카운트
  if (questionText) {
    questionText.addEventListener("input", function () {
      const length = this.value.length;
      if (textLength) {
        textLength.textContent = `${length}/500자`;

        // 500자 초과 방지
        if (length > 500) {
          this.value = this.value.substring(0, 500);
          textLength.textContent = "500/500자";
        }
      }
    });
  }

  // 문의 폼 제출
  if (questionForm) {
    questionForm.addEventListener("submit", function (e) {
      e.preventDefault();

      // 입력값 검증
      const productSelect = document.getElementById("productSelect");
      const questionTitle = document.getElementById("questionTitle");

      if (productSelect.value === "") {
        alert("상품을 선택해주세요.");
        productSelect.focus();
        return;
      }

      if (questionTitle.value.trim() === "") {
        alert("문의 제목을 입력해주세요.");
        questionTitle.focus();
        return;
      }

      if (questionText.value.trim() === "") {
        alert("문의 내용을 입력해주세요.");
        questionText.focus();
        return;
      }

      // TODO: 문의 제출 처리 로직
      alert("문의가 등록되었습니다.");
      closeModal();

      // 페이지 새로고침 (실제로는 서버에서 데이터를 다시 가져와야 함)
      // location.reload();
    });
  }

  // 프로필 수정 버튼 클릭
  const profileEditBtn = document.querySelector(".profile-edit-btn");
  if (profileEditBtn) {
    profileEditBtn.addEventListener("click", function () {
      window.location.href = "myinfo.html";
    });
  }
});
