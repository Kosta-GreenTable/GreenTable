/**
 * 회원가입 페이지 자바스크립트
 * - 폼 유효성 검사
 * - 이메일 인증 기능
 * - 주소 검색 모달
 * - 휴대폰 번호 자동 포맷팅
 * - 생년월일 날짜 옵션 설정
 */

// contextPath 변수 정의 - 여러 방법으로 시도
// 1. meta 태그에서 불러오기
const metaContextPath = document.querySelector('meta[name="context-path"]')?.getAttribute('content');
// 2. 스크립트 태그의 src 속성에서 경로 추출 
const scriptSrc = document.currentScript?.src || '';
let extractedPath = '';
if (scriptSrc.includes('/js/')) {
  extractedPath = scriptSrc.split('/js/')[0];
}
// 최종 contextPath 결정 (우선순위: meta > 스크립트 경로 > 빈 문자열)
const contextPath = metaContextPath || extractedPath || '';

// 디버깅용 로그
console.log("현재 사용 중인 contextPath:", contextPath);

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

  // 비밀번호 필드에 autocomplete 속성 추가 (보안 경고 해결)
  if (passwordInput) passwordInput.setAttribute("autocomplete", "new-password");
  if (passwordConfirmInput) passwordConfirmInput.setAttribute("autocomplete", "new-password");

  // 주소 검색 관련 요소
  const findAddressBtn = document.querySelector(".find-address-btn");
  const addressModal = document.getElementById("address-modal");
  const closeAddressBtn = addressModal?.querySelector(".close-btn");
  const searchAddressBtn = document.getElementById("search-address-btn");
  const searchZipcodeInput = document.getElementById("search-zipcode");
  const addressResultsList = document.getElementById("address-results");
  const zipcodeInput = document.getElementById("zipcode");
  const address1Input = document.getElementById("address1");

  // 휴대폰 번호 입력 필드
  const mobileFirstInput = document.getElementById('mobile-first');
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

  if (verifyEmailBtn) {
    verifyEmailBtn.addEventListener("click", async function () {
      const email = emailInput.value.trim();
      if (!validateEmail(email)) {
        alert("올바른 이메일 주소를 입력해주세요.");
        emailInput.focus();
        return;
      }

      // 이메일 인증 코드 발송 로직 (서버와 통신)
      try {
        const url = `${contextPath}/ajax?key=user&methodName=verifyEmail&email=${email}`;
        console.log("요청 URL:", url);
        
        const response = await fetch(url, {
          method: "GET",
        });

        // 응답이 JSON이 아닐 수 있으므로 안전하게 처리
        let result = null;
        const contentType = response.headers.get("content-type");
        
        if (contentType && contentType.includes("application/json")) {
          result = await response.json();
        } else {
          const text = await response.text();
          console.log("JSON이 아닌 응답:", text);
          throw new Error("서버에서 올바른 응답을 받지 못했습니다");
        }

        if (!response.ok) {
          throw new Error(result?.errorMsg || `서버 오류: ${response.status}`);
        }
        
        alert("인증번호가 전송되었습니다");

        verifyEmailBtn.disabled = true;
        verifyEmailBtn.textContent = "인증번호 발송됨";

        // 인증번호 입력란 표시
        verificationCodeGroup?.classList.remove("hidden");

        // 5분 타이머 시작
        startTimer(5 * 60);
      } catch (e) {
        alert(`이메일 인증 오류: ${e.message}`);
        console.error("이메일 인증 오류:", e.message);
      }
    });
  }

  // 인증번호 확인 버튼 클릭 이벤트
  if (verifyCodeBtn) {
    verifyCodeBtn.addEventListener("click", async function () {
      const code = verificationCodeInput.value.trim();
      if (!code) {
        alert("인증번호를 입력해주세요.");
        verificationCodeInput.focus();
        return;
      }

      // 인증번호 확인 로직
      try {
        const url = `${contextPath}/ajax?key=user&methodName=verifyEmailOk&code=${code}`;
        console.log("요청 URL:", url);
        
        const response = await fetch(url);
        
        // 응답이 JSON이 아닐 수 있으므로 안전하게 처리
        let result = null;
        const contentType = response.headers.get("content-type");
        
        if (contentType && contentType.includes("application/json")) {
          result = await response.json();
        } else {
          const text = await response.text();
          console.log("JSON이 아닌 응답:", text);
          throw new Error("서버에서 올바른 응답을 받지 못했습니다");
        }
        
        if (!response.ok) {
          throw new Error(result?.errorMsg || `서버 오류: ${response.status}`);
        }
        
        // 인증 성공 처리
        if (emailVerifyModal) {
          emailVerifyModal.classList.add("active");
        }

        // 이메일 입력란 비활성화
        emailInput.readOnly = true;
        verifyEmailBtn.disabled = true;
        verificationCodeGroup?.classList.add("hidden");
      } catch (e) {
        alert(`인증 오류: ${e.message}`);
        return;
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
      execDaumPostcode();
    });
  }
  
  // 다음 우편번호 서비스 연동 함수
  function execDaumPostcode() {
    if (typeof daum !== 'undefined' && daum.Postcode) {
      new daum.Postcode({
        oncomplete: function (data) {
          // 도로명 주소가 있으면 그걸, 아니면 지번주소 사용
          const addr = data.roadAddress || data.jibunAddress;

          // 주소를 입력창에 채워 넣기
          const zipCodeElement = document.getElementById("zipCode");
          const address1Element = document.getElementById("address1");
          
          if (zipCodeElement) zipCodeElement.value = data.zonecode;
          if (address1Element) address1Element.value = addr;

          // 상세주소 입력창으로 포커스 이동
          const address2Element = document.getElementById("address2");
          if (address2Element) address2Element.focus();
        }
      }).open();
    } else {
      console.error("Daum Postcode 라이브러리를 찾을 수 없습니다.");
      alert("주소 검색 서비스를 불러올 수 없습니다. 페이지를 새로고침하거나 나중에 다시 시도해주세요.");
    }
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

    if (validateForm()) {
      console.log("AJAX 호출 시작");
      const url = `${contextPath}/ajax?key=user&methodName=register`;
      console.log("요청 URL:", url);

      const data = {
        email: document.getElementById("email").value.trim(),
        password: document.getElementById("password").value,
        userName: document.getElementById("name").value.trim(),
        phone: document.getElementById("phone").value,
        zipCode: document.getElementById("zipCode")?.value.trim() || "",
        address: document.getElementById("address1")?.value.trim() || "",
        detailAddress: document.getElementById("address2")?.value.trim() || ""
      };

      fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
      })
      .then(res => {
        if (!res.ok) {
          throw new Error(`서버 오류: ${res.status}`);
        }
        return res.json();  // JSON 바로 파싱
      })
      .then(data => {
        console.log("서버 응답 JSON:", data);
        if (data.success) {
          openModal(registerSuccessModal);
        } else {
          alert("회원가입 실패: " + (data.message || "알 수 없는 오류"));
        }
      })
      .catch(err => {
        console.error("오류 발생:", err);
        alert(`회원가입 중 오류: ${err.message}`);
      });
    }
  });
}


  // 취소 버튼 클릭 이벤트
  if (cancelBtn) {
    cancelBtn.addEventListener("click", function () {
      if (
        confirm("회원가입을 취소하시겠습니까? 입력한 정보는 저장되지 않습니다.")
      ) {
        window.location.href = contextPath + "/index.jsp";
      }
    });
  }

  // 로그인하기 버튼 클릭 이벤트
  if (goLoginBtn) {
    goLoginBtn.addEventListener("click", function () {
      window.location.href = contextPath + "/user/login.jsp";
    });
  }

  // 메인으로 가기 버튼 클릭 이벤트
  if (goMainBtn) {
    goMainBtn.addEventListener("click", function () {
      window.location.href = contextPath + "/index.jsp";
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

    // 휴대폰 번호 검증
    if (!mobileMiddleInput.value || !mobileLastInput.value) {
      alert("휴대폰 번호를 입력해주세요.");
      mobileMiddleInput.focus();
      return false;
    } else {
      const fullPhone = mobileFirstInput.value + mobileMiddleInput.value + mobileLastInput.value;
      const phoneInput = document.getElementById('phone');
      if (phoneInput) {
        phoneInput.value = fullPhone;
      }
    }

    return true;
  }
    
  // 타이머 시작 함수
  function startTimer(duration) {
    let timer = duration;
    let minutes, seconds;
    
    if (!timerSpan) return;

    const timerInterval = setInterval(function () {
      minutes = parseInt(timer / 60, 10);
      seconds = parseInt(timer % 60, 10);

      minutes = minutes < 10 ? "0" + minutes : minutes;
      seconds = seconds < 10 ? "0" + seconds : seconds;

      timerSpan.textContent = minutes + ":" + seconds;

      if (--timer < 0) {
        clearInterval(timerInterval);
        timerSpan.textContent = "00:00";
        if (verifyCodeBtn) verifyCodeBtn.disabled = true;
        alert("인증 시간이 만료되었습니다. 다시 시도해주세요.");
      }
    }, 1000);
  }

  // 날짜 옵션 업데이트 함수
  function updateDays() {
    if (!birthMonthSelect || !birthDaySelect) return;
    
    const selectedMonth = birthMonthSelect.value;
    const selectedYear = birthYearInput ? birthYearInput.value : null;

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