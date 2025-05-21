/**
 * 상품 리뷰 페이지 자바스크립트
 */
document.addEventListener("DOMContentLoaded", function () {
  // 샘플 데이터 - 실제로는 서버에서 받아와야 함
  const writableReviews = [
    {
      orderDate: "2025.05.01",
      productImage: "https://picsum.photos/seed/product101/100/100",
      productName: "유기농 채소 건강 도시락",
      productOption: "기본 구성 / 수량: 1개",
      deadline: "2025.06.01",
    },
  ];

  const writtenReviews = [
    {
      reviewDate: "2025.04.15",
      productImage: "https://picsum.photos/seed/product102/100/100",
      productName: "프리미엄 샐러드 세트",
      productOption: "비건 소스 / 수량: 2개",
      rating: 4.5,
      content:
        "신선한 재료로 만들어져서 너무 맛있었어요! 다음에도 구매할 예정입니다. 배송도 빠르고 포장도 꼼꼼하게 되어있어 좋았습니다.",
      photos: [
        "https://picsum.photos/seed/review1/100/100",
        "https://picsum.photos/seed/review2/100/100",
      ],
    },
  ];

  // 페이지 로딩 시 데이터 표시 여부 설정
  const showSampleData = false; // true로 설정하면 샘플 데이터 표시

  // DOM 요소
  const tabBtns = document.querySelectorAll(".tab-btn");
  const tabContents = document.querySelectorAll(".tab-content");
  const writableList = document.querySelector(
    ".tab-content.active .review-list"
  );
  const writtenList = document.querySelector(
    ".tab-content:not(.active) .review-list"
  );
  const noWritableReview = document.querySelector(
    ".tab-content.active .no-review"
  );
  const noWrittenReview = document.querySelector(
    ".tab-content:not(.active) .no-review"
  );
  const summaryItems = document.querySelectorAll(".summary-item .item-count");

  // 모달 관련 요소
  const modal = document.getElementById("reviewModal");
  const closeModalBtn = document.querySelector(".close-modal");
  const cancelBtn = document.querySelector(".cancel-btn");
  const starRatings = document.querySelectorAll(".star-rating .fa-star");
  const ratingValue = document.querySelector(".rating-value");
  const reviewText = document.getElementById("reviewText");
  const textLength = document.querySelector(".text-length");
  const photoUpload = document.getElementById("photoUpload");
  const photoPreview = document.querySelector(".photo-upload-preview");
  const reviewForm = document.getElementById("reviewForm");

  // 샘플 데이터 표시
  if (showSampleData) {
    loadData();
  }

  /**
   * 데이터 로드 및 표시
   */
  function loadData() {
    updateSummary();
    updateWritableReviews();
    updateWrittenReviews();
  }

  /**
   * 리뷰 요약 정보 업데이트
   */
  function updateSummary() {
    // 작성 가능 리뷰 개수
    summaryItems[0].textContent = writableReviews.length;

    // 작성 완료 리뷰 개수
    summaryItems[1].textContent = writtenReviews.length;

    // 포토 리뷰 개수
    const photoReviewsCount = writtenReviews.filter(
      (review) => review.photos && review.photos.length > 0
    ).length;
    summaryItems[2].textContent = photoReviewsCount;

    // 탭 버튼 텍스트 업데이트
    tabBtns[0].textContent = `작성 가능한 리뷰 (${writableReviews.length})`;
    tabBtns[1].textContent = `작성한 리뷰 (${writtenReviews.length})`;
  }

  /**
   * 작성 가능한 리뷰 목록 업데이트
   */
  function updateWritableReviews() {
    if (writableReviews.length > 0) {
      noWritableReview.style.display = "none";
      writableList.innerHTML = "";

      writableReviews.forEach((review) => {
        const reviewItem = document.createElement("div");
        reviewItem.className = "review-item writable";
        reviewItem.innerHTML = `
          <div class="review-product">
            <div class="product-image">
              <img src="${review.productImage}" alt="상품이미지">
            </div>
            <div class="product-info">
              <p class="order-date">주문일자: ${review.orderDate}</p>
              <h4 class="product-name">${review.productName}</h4>
              <p class="product-option">${review.productOption}</p>
              <div class="review-deadline">
                <span class="deadline-text">리뷰 작성 기한: <b>${review.deadline}</b>까지</span>
              </div>
            </div>
          </div>
          <div class="review-action">
            <button class="btn-write-review">리뷰 작성</button>
          </div>
        `;

        // 리뷰 작성 버튼 이벤트 리스너 등록
        const writeButton = reviewItem.querySelector(".btn-write-review");
        writeButton.addEventListener("click", function () {
          openReviewModal(review);
        });

        writableList.appendChild(reviewItem);
      });
    } else {
      noWritableReview.style.display = "block";
    }
  }

  /**
   * 작성한 리뷰 목록 업데이트
   */
  function updateWrittenReviews() {
    if (writtenReviews.length > 0) {
      noWrittenReview.style.display = "none";
      writtenList.innerHTML = "";

      writtenReviews.forEach((review) => {
        const reviewItem = document.createElement("div");
        reviewItem.className = "review-item written";

        // 별점 HTML 생성
        let starsHTML = "";
        for (let i = 1; i <= 5; i++) {
          if (i <= Math.floor(review.rating)) {
            starsHTML += '<i class="fas fa-star"></i>';
          } else if (i - 0.5 <= review.rating) {
            starsHTML += '<i class="fas fa-star-half-alt"></i>';
          } else {
            starsHTML += '<i class="far fa-star"></i>';
          }
        }

        // 사진 HTML 생성
        let photosHTML = "";
        if (review.photos && review.photos.length > 0) {
          review.photos.forEach((photo) => {
            photosHTML += `
              <div class="photo-item">
                <img src="${photo}" alt="리뷰사진">
              </div>
            `;
          });
        }

        reviewItem.innerHTML = `
          <div class="review-product">
            <div class="product-image">
              <img src="${review.productImage}" alt="상품이미지">
            </div>
            <div class="product-info">
              <p class="review-date">작성일자: ${review.reviewDate}</p>
              <h4 class="product-name">${review.productName}</h4>
              <p class="product-option">${review.productOption}</p>
              <div class="rating">
                <span class="rating-text">평점:</span>
                <span class="stars">${starsHTML}</span>
                <span class="rating-value">${review.rating}</span>
              </div>
            </div>
          </div>
          <div class="review-content">
            <div class="review-text">
              <p>${review.content}</p>
            </div>
            <div class="review-photos">
              ${photosHTML}
            </div>
            <div class="review-actions">
              <button class="btn-edit-review">수정</button>
              <button class="btn-delete-review">삭제</button>
            </div>
          </div>
        `;

        // 리뷰 수정 버튼 이벤트 리스너 등록
        const editButton = reviewItem.querySelector(".btn-edit-review");
        editButton.addEventListener("click", function () {
          // TODO: 리뷰 수정 모달 열기
          alert("리뷰 수정 기능은 준비 중입니다.");
        });

        // 리뷰 삭제 버튼 이벤트 리스너 등록
        const deleteButton = reviewItem.querySelector(".btn-delete-review");
        deleteButton.addEventListener("click", function () {
          // TODO: 리뷰 삭제 확인 모달 열기
          if (confirm("리뷰를 삭제하시겠습니까?")) {
            alert("리뷰가 삭제되었습니다.");
            // TODO: 리뷰 삭제 처리 로직
          }
        });

        writtenList.appendChild(reviewItem);
      });
    } else {
      noWrittenReview.style.display = "block";
    }
  }

  /**
   * 리뷰 작성 모달 열기
   * @param {Object} review - 리뷰 정보 객체
   */
  function openReviewModal(review) {
    // 모달에 상품 정보 설정
    const productImage = modal.querySelector(".product-image img");
    const productName = modal.querySelector(".product-name");
    const productOption = modal.querySelector(".product-option");

    productImage.src = review.productImage;
    productName.textContent = review.productName;
    productOption.textContent = review.productOption;

    // 모달 초기화
    resetModal();

    // 모달 표시
    modal.style.display = "flex";
  }

  /**
   * 모달 초기화
   */
  function resetModal() {
    // 별점 초기화
    starRatings.forEach((star) => {
      star.className = "far fa-star";
    });
    ratingValue.textContent = "0점";

    // 텍스트 초기화
    reviewText.value = "";
    textLength.textContent = "0/1000자";

    // 사진 초기화
    photoPreview.innerHTML = "";
  }

  /**
   * 모달 닫기
   */
  function closeModal() {
    modal.style.display = "none";
  }

  // 이벤트 리스너 등록

  // 탭 버튼 클릭
  tabBtns.forEach((btn, index) => {
    btn.addEventListener("click", function () {
      tabBtns.forEach((b) => b.classList.remove("active"));
      tabContents.forEach((c) => c.classList.remove("active"));

      this.classList.add("active");
      tabContents[index].classList.add("active");
    });
  });

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
    if (event.target === modal) {
      closeModal();
    }
  });

  // 별점 선택
  if (starRatings) {
    starRatings.forEach((star) => {
      star.addEventListener("click", function () {
        const rating = parseInt(this.getAttribute("data-rating"));

        // 별점 시각적 업데이트
        starRatings.forEach((s, i) => {
          s.className = i < rating ? "fas fa-star" : "far fa-star";
        });

        // 평점 텍스트 업데이트
        ratingValue.textContent = `${rating}점`;
      });

      // 별점 호버 효과
      star.addEventListener("mouseenter", function () {
        const rating = parseInt(this.getAttribute("data-rating"));
        starRatings.forEach((s, i) => {
          if (i < rating) s.classList.add("hover");
        });
      });

      star.addEventListener("mouseleave", function () {
        starRatings.forEach((s) => s.classList.remove("hover"));
      });
    });
  }

  // 리뷰 텍스트 글자 수 카운트
  if (reviewText) {
    reviewText.addEventListener("input", function () {
      const length = this.value.length;
      textLength.textContent = `${length}/1000자`;

      // 1000자 초과 방지
      if (length > 1000) {
        this.value = this.value.substring(0, 1000);
        textLength.textContent = "1000/1000자";
      }
    });
  }

  // 사진 업로드 및 미리보기
  if (photoUpload) {
    photoUpload.addEventListener("change", function () {
      const files = Array.from(this.files);

      // 최대 5장으로 제한
      if (files.length + photoPreview.children.length > 5) {
        alert("최대 5장까지만 업로드할 수 있습니다.");
        return;
      }

      files.forEach((file) => {
        // 파일 크기 체크 (5MB)
        if (file.size > 5 * 1024 * 1024) {
          alert("파일 크기는 5MB 이하여야 합니다.");
          return;
        }

        // 이미지 타입 체크
        if (!file.type.match("image/jpeg") && !file.type.match("image/png")) {
          alert("JPG 또는 PNG 파일만 업로드할 수 있습니다.");
          return;
        }

        const reader = new FileReader();

        reader.onload = function (e) {
          const photoContainer = document.createElement("div");
          photoContainer.className = "photo-preview-item";

          photoContainer.innerHTML = `
            <img src="${e.target.result}" alt="미리보기">
            <button class="remove-photo" type="button">&times;</button>
          `;

          // 사진 삭제 버튼 이벤트
          photoContainer
            .querySelector(".remove-photo")
            .addEventListener("click", function () {
              photoPreview.removeChild(photoContainer);
            });

          photoPreview.appendChild(photoContainer);
        };

        reader.readAsDataURL(file);
      });
    });
  }

  // 리뷰 폼 제출
  if (reviewForm) {
    reviewForm.addEventListener("submit", function (e) {
      e.preventDefault();

      // 별점 체크
      const selectedStars = document.querySelectorAll(".star-rating .fas");
      if (selectedStars.length === 0) {
        alert("별점을 선택해주세요.");
        return;
      }

      // 리뷰 내용 체크
      if (reviewText.value.trim() === "") {
        alert("리뷰 내용을 입력해주세요.");
        reviewText.focus();
        return;
      }

      // TODO: 리뷰 제출 처리 로직
      alert("리뷰가 등록되었습니다.");
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
