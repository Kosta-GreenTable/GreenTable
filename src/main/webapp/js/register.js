/**
 * 회원가입 페이지 자바스크립트
 * - 폼 유효성 검사
 * - 이메일 인증 기능
 * - 주소 검색 모달
 * - 휴대폰 번호 자동 포맷팅
 * - 생년월일 날짜 옵션 설정
 */
document.addEventListener("DOMContentLoaded", function () {
  // 이메일 인증 버튼 이벤트
  const verifyEmailBtn = document.querySelector(".verify-email-btn");
  const emailInput = document.getElementById("email");
  const verificationCodeGroup = document.getElementById(
    "verification-code-group"
  );
  const verificationCodeInput = document.getElementById("verification-code");
  const verifyCodeBtn = document.querySelector(".verify-code-btn");
  const timerSpan = document.getElementById("timer");
  const emailVerifyModal = document.getElementById("email-verify-modal");

  // 비밀번호 유효성 검사
  const passwordInput = document.getElementById("password");
  const passwordConfirmInput = document.getElementById("password-confirm");

  // 주소 검색 관련 요소
  const findAddressBtn = document.querySelector(".find-address-btn");
  const addressModal = document.getElementById("address-modal");
  const closeAddressBtn = addressModal.querySelector(".close-btn");
  const searchAddressBtn = document.getElementById("search-address-btn");
  const searchZipcodeInput = document.getElementById("search-zipcode");
  const addressResultsList = document.getElementById("address-results");
  const zipcodeInput = document.getElementById("zipcode");
  const address1Input = document.getElementById("address1");

  // 휴대폰 번호 입력 필드
  const mobileMiddleInput = document.getElementById("mobile-middle");
  const mobileLastInput = document.getElementById("mobile-last");
  const telMiddleInput = document.getElementById("tel-middle");
  const telLastInput = document.getElementById("tel-last");

  // 생년월일 관련 요소
  const birthYearInput = document.getElementById("birth-year");
  const birthMonthSelect = document.getElementById("birth-month");
  const birthDaySelect = document.getElementById("birth-day");

  // 회원가입 폼 및 버튼
  const registerForm = document.getElementById("register-form");
  const submitBtn = document.querySelector(".submit-btn");
  const cancelBtn = document.querySelector(".cancel-btn");
  const registerSuccessModal = document.getElementById(
    "register-success-modal"
  );
  const goLoginBtn = document.getElementById("go-login-btn");
  const goMainBtn = document.getElementById("go-main-btn");

  // 모든 모달 닫기 버튼에 이벤트 연결
  document.querySelectorAll(".close-btn").forEach((btn) => {
    btn.addEventListener("click", function () {
      const modal = this.closest(".modal");
      closeModal(modal);
    });
  });

  // 이메일 인증 버튼 클릭 이벤트
  if (verifyEmailBtn) {
    verifyEmailBtn.addEventListener("click", function () {
      const email = emailInput.value.trim();
      if (!validateEmail(email)) {
        alert("올바른 이메일 주소를 입력해주세요.");
        emailInput.focus();
        return;
      }

      // 이메일 인증 코드 발송 로직 (실제로는 서버와 통신)
      verifyEmailBtn.disabled = true;
      verifyEmailBtn.textContent = "인증번호 발송됨";

      // 인증번호 입력란 표시
      verificationCodeGroup.classList.remove("hidden");

      // 5분 타이머 시작
      startTimer(5 * 60);
    });
  }

  // 인증번호 확인 버튼 클릭 이벤트
  if (verifyCodeBtn) {
    verifyCodeBtn.addEventListener("click", function () {
      const code = verificationCodeInput.value.trim();
      if (!code) {
        alert("인증번호를 입력해주세요.");
        verificationCodeInput.focus();
        return;
      }

      // 인증번호 확인 로직 (실제로는 서버와 통신)
      // 예시로 '123456'을 유효한 인증번호로 가정
      if (code === "123456") {
        // 인증 성공 처리
        emailVerifyModal.classList.add("active");

        // 이메일 입력란 비활성화
        emailInput.readOnly = true;
        verifyEmailBtn.disabled = true;
        verificationCodeGroup.classList.add("hidden");
      } else {
        alert("인증번호가 일치하지 않습니다.");
      }
    });
  }

  // 이메일 인증 완료 모달 확인 버튼
  const modalConfirmBtn = emailVerifyModal?.querySelector(".modal-confirm-btn");
  if (modalConfirmBtn) {
    modalConfirmBtn.addEventListener("click", function () {
      closeModal(emailVerifyModal);

      // 인증 완료 상태로 변경
      verifyEmailBtn.textContent = "인증완료";
      verifyEmailBtn.className = "verify-email-btn verified";
    });
  }

  // 주소찾기 버튼 클릭 이벤트
  if (findAddressBtn) {
    findAddressBtn.addEventListener("click", function () {
      openModal(addressModal);
    });
  }

  // 주소 검색 버튼 클릭 이벤트
  if (searchAddressBtn) {
    searchAddressBtn.addEventListener("click", function () {
      const searchQuery = searchZipcodeInput.value.trim();
      if (!searchQuery) {
        alert("검색어를 입력해주세요.");
        searchZipcodeInput.focus();
        return;
      }

      // 주소 검색 결과 표시 (실제로는 API와 통신)
      searchAddress(searchQuery);
    });
  }

  // 주소 검색 모달 닫기 버튼
  if (closeAddressBtn) {
    closeAddressBtn.addEventListener("click", function () {
      closeModal(addressModal);
    });
  }

  // 생년월일 관련 설정
  if (birthMonthSelect && birthDaySelect) {
    // 년도 입력 제한 (숫자만)
    if (birthYearInput) {
      birthYearInput.addEventListener("input", function () {
        this.value = this.value.replace(/[^0-9]/g, "");

        // 4자리 이상 입력 방지
        if (this.value.length > 4) {
          this.value = this.value.slice(0, 4);
        }
      });
    }

    // 월 변경 시 해당 월의 일수에 맞게 일 옵션 변경
    birthMonthSelect.addEventListener("change", function () {
      updateDays();
    });
  }

  // 휴대폰 번호 입력 제한 (숫자만)
  [mobileMiddleInput, mobileLastInput, telMiddleInput, telLastInput].forEach(
    (input) => {
      if (input) {
        input.addEventListener("input", function () {
          this.value = this.value.replace(/[^0-9]/g, "");

          // 자동 포커스 이동
          if (this.value.length >= this.maxLength) {
            const nextInput = this.nextElementSibling?.nextElementSibling;
            if (nextInput && nextInput.tagName === "INPUT") {
              nextInput.focus();
            }
          }
        });
      }
    }
  );

  // 회원가입 폼 제출 이벤트
  if (registerForm) {
    registerForm.addEventListener("submit", function (e) {
      e.preventDefault();

      // 폼 유효성 검사
      if (validateForm()) {
        // 성공 모달 표시
        openModal(registerSuccessModal);
      }
    });
  }

  // 취소 버튼 클릭 이벤트
  if (cancelBtn) {
    cancelBtn.addEventListener("click", function () {
      if (
        confirm("회원가입을 취소하시겠습니까? 입력한 정보는 저장되지 않습니다.")
      ) {
        window.location.href = "index.html";
      }
    });
  }

  // 로그인하기 버튼 클릭 이벤트
  if (goLoginBtn) {
    goLoginBtn.addEventListener("click", function () {
      window.location.href = "login.html";
    });
  }

  // 메인으로 가기 버튼 클릭 이벤트
  if (goMainBtn) {
    goMainBtn.addEventListener("click", function () {
      window.location.href = "index.html";
    });
  }

  // 유틸리티 함수 모음

  // 이메일 유효성 검사
  function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
  }

  // 비밀번호 유효성 검사
  function validatePassword(password) {
    // 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합, 10~16자리
    const hasLower = /[a-z]/.test(password);
    const hasUpper = /[A-Z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    const isValidLength = password.length >= 10 && password.length <= 16;

    const typeCount = [hasLower, hasUpper, hasNumber, hasSpecial].filter(
      Boolean
    ).length;

    return typeCount >= 2 && isValidLength;
  }

  // 폼 전체 유효성 검사
  function validateForm() {
    // 이메일 검증
    const email = emailInput.value.trim();
    if (!validateEmail(email)) {
      alert("올바른 이메일 형식으로 입력해주세요.");
      emailInput.focus();
      return false;
    }

    if (!verifyEmailBtn.classList.contains("verified")) {
      alert("이메일 인증을 완료해주세요.");
      verifyEmailBtn.focus();
      return false;
    }

    // 비밀번호 검증
    const password = passwordInput.value;
    if (!validatePassword(password)) {
      alert(
        "비밀번호는 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합하여 10~16자리로 입력해주세요."
      );
      passwordInput.focus();
      return false;
    }

    // 비밀번호 확인
    const passwordConfirm = passwordConfirmInput.value;
    if (password !== passwordConfirm) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      passwordConfirmInput.focus();
      return false;
    }

    // 이름 검증
    const name = document.getElementById("name").value.trim();
    if (!name) {
      alert("이름을 입력해주세요.");
      document.getElementById("name").focus();
      return false;
    }

    // 주소 검증
    if (
      !zipcodeInput.value ||
      !address1Input.value ||
      !document.getElementById("address2").value
    ) {
      alert("주소를 모두 입력해주세요.");
      findAddressBtn.focus();
      return false;
    }

    // 휴대폰 번호 검증
    if (!mobileMiddleInput.value || !mobileLastInput.value) {
      alert("휴대폰 번호를 입력해주세요.");
      mobileMiddleInput.focus();
      return false;
    }

    // 생년월일 검증
    if (
      !birthYearInput.value ||
      birthMonthSelect.value === "" ||
      birthDaySelect.value === ""
    ) {
      alert("생년월일을 모두 선택해주세요.");
      birthYearInput.focus();
      return false;
    }

    // 유효한 생년월일 검사
    const year = parseInt(birthYearInput.value);
    const currentYear = new Date().getFullYear();
    if (year < 1900 || year > currentYear) {
      alert("유효한 생년월일을 입력해주세요.");
      birthYearInput.focus();
      return false;
    }

    return true;
  }

  // 타이머 시작 함수
  function startTimer(duration) {
    let timer = duration;
    let minutes, seconds;

    const timerInterval = setInterval(function () {
      minutes = parseInt(timer / 60, 10);
      seconds = parseInt(timer % 60, 10);

      minutes = minutes < 10 ? "0" + minutes : minutes;
      seconds = seconds < 10 ? "0" + seconds : seconds;

      timerSpan.textContent = minutes + ":" + seconds;

      if (--timer < 0) {
        clearInterval(timerInterval);
        timerSpan.textContent = "00:00";
        verifyCodeBtn.disabled = true;
        alert("인증 시간이 만료되었습니다. 다시 시도해주세요.");
      }
    }, 1000);
  }

  // 날짜 옵션 업데이트 함수
  function updateDays() {
    const selectedMonth = birthMonthSelect.value;
    const selectedYear = birthYearInput.value;

    if (!selectedMonth) return;

    const daysInMonth = new Date(
      selectedYear || new Date().getFullYear(),
      parseInt(selectedMonth),
      0
    ).getDate();

    birthDaySelect.innerHTML = '<option value="" disabled selected>일</option>';

    for (let i = 1; i <= daysInMonth; i++) {
      const option = document.createElement("option");
      option.value = i < 10 ? "0" + i : i;
      option.textContent = i;
      birthDaySelect.appendChild(option);
    }
  }

  // 주소 검색 함수 (실제로는 API 연동 필요)
  function searchAddress(query) {
    // 예시 주소 데이터 (실제로는 API에서 받아옴)
    const mockResults = [
      { zipcode: "06134", address: "서울특별시 강남구 테헤란로 427" },
      { zipcode: "06133", address: "서울특별시 강남구 테헤란로 415" },
      { zipcode: "06130", address: "서울특별시 강남구 테헤란로 401" },
      { zipcode: "06142", address: "서울특별시 강남구 테헤란로 432" },
    ];

    // 검색 결과 표시
    addressResultsList.innerHTML = "";

    if (mockResults.length === 0) {
      const noResult = document.createElement("p");
      noResult.className = "no-result";
      noResult.textContent = "검색 결과가 없습니다.";
      addressResultsList.appendChild(noResult);
    } else {
      mockResults.forEach((item) => {
        const li = document.createElement("li");
        li.innerHTML = `
          <div class="address-item">
            <div class="zipcode">[${item.zipcode}]</div>
            <div class="address-text">${item.address}</div>
          </div>
        `;

        // 주소 선택 이벤트
        li.addEventListener("click", function () {
          zipcodeInput.value = item.zipcode;
          address1Input.value = item.address;
          closeModal(addressModal);
          document.getElementById("address2").focus();
        });

        addressResultsList.appendChild(li);
      });
    }
  }

  // 모달 열기 함수
  function openModal(modal) {
    if (modal) {
      modal.classList.add("active");
    }
  }

  // 모달 닫기 함수
  function closeModal(modal) {
    if (modal) {
      modal.classList.remove("active");
    }
  }
});
