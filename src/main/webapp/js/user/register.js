/**
 * 회원가입 페이지 자바스크립트
 * - 폼 유효성 검사
 * - 이메일 인증 기능
 * - 주소 검색 모달
 * - 휴대폰 번호 자동 포맷팅
 */

document.addEventListener("DOMContentLoaded", function () {
  console.log("회원가입 페이지 스크립트 로드됨");
  
  // DOM 요소들 참조
  const emailInput = document.getElementById("email");
  const duplicateCheckBtn = document.getElementById("duplicate-check-btn");
  const emailVerificationGroup = document.getElementById("email-verification-group");
  const sendVerificationBtn = document.getElementById("send-verification-btn");
  const verificationCodeGroup = document.getElementById("verification-code-group");
  const verificationCodeInput = document.getElementById("verification-code");
  const verifyCodeBtn = document.querySelector(".verify-code-btn");
  const timerSpan = document.getElementById("timer");
  const passwordInput = document.getElementById("password");
  const passwordConfirmInput = document.getElementById("password-confirm");
  const registerForm = document.getElementById("register-form");
  const findAddressBtn = document.querySelector(".find-address-btn");

  console.log("DOM 요소 확인:");
  console.log("- emailInput:", emailInput);
  console.log("- duplicateCheckBtn:", duplicateCheckBtn);
  console.log("- contextPath:", typeof contextPath !== 'undefined' ? contextPath : 'contextPath가 정의되지 않음');

  // 상태 관리 변수
  let isDuplicateChecked = false;
  let isEmailVerified = false;
  let verificationTimer = null;

  // 유틸리티 함수들
  function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  function showMessage(id, message) {
    const element = document.getElementById(id);
    if (element) {
      element.textContent = message;
      element.classList.remove("hidden");
    }
  }

  function hideMessage(id) {
    const element = document.getElementById(id);
    if (element) {
      element.classList.add("hidden");
    }
  }

  function startTimer() {
    let timeLeft = 300; // 5분
    verificationTimer = setInterval(() => {
      const minutes = Math.floor(timeLeft / 60);
      const seconds = timeLeft % 60;
      if (timerSpan) {
        timerSpan.textContent = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
      }
      
      if (timeLeft <= 0) {
        clearTimer();
        alert("인증시간이 만료되었습니다. 다시 인증번호를 요청해주세요.");
        if (sendVerificationBtn) {
          sendVerificationBtn.disabled = false;
          sendVerificationBtn.textContent = "재발송";
        }
      }
      timeLeft--;
    }, 1000);
  }

  function clearTimer() {
    if (verificationTimer) {
      clearInterval(verificationTimer);
      verificationTimer = null;
    }
  }

  // 1. 이메일 중복확인 기능
  if (duplicateCheckBtn && emailInput) {
    console.log("중복확인 버튼 이벤트 리스너 등록");
    
    duplicateCheckBtn.addEventListener("click", function() {
      console.log("중복확인 버튼 클릭됨!");
      
      const email = emailInput.value.trim();
      console.log("입력된 이메일:", email);
      
      if (!email) {
        alert("이메일을 입력해주세요.");
        emailInput.focus();
        return;
      }

      if (!isValidEmail(email)) {
        alert("올바른 이메일 형식을 입력해주세요.");
        emailInput.focus();
        return;
      }

      console.log("AJAX 요청 시작 - 중복확인");
      
      // AJAX로 중복확인 요청
      fetch(`${contextPath}/ajax?key=user&methodName=checkDuplicate`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `email=${encodeURIComponent(email)}`
      })
      .then(response => {
        console.log("중복확인 응답:", response);
        return response.json();
      })
      .then(data => {
        console.log("중복확인 응답 데이터:", data);
        if (data.available) {
          alert("사용 가능한 이메일입니다.");
          showMessage("duplicate-success", "✓ 사용 가능한 이메일입니다.");
          hideMessage("duplicate-error");
          isDuplicateChecked = true;
          duplicateCheckBtn.disabled = true;
          duplicateCheckBtn.textContent = "확인완료";
          if (emailVerificationGroup) {
            emailVerificationGroup.classList.remove("hidden");
          }
          if (sendVerificationBtn) {
            sendVerificationBtn.disabled = false;
          }
        } else {
          alert("이미 사용중인 이메일입니다.");
          showMessage("duplicate-error", "✗ " + (data.message || "이미 사용중인 이메일입니다."));
          hideMessage("duplicate-success");
          isDuplicateChecked = false;
        }
      })
      .catch(error => {
        console.error('중복확인 오류:', error);
        alert("중복확인 중 오류가 발생했습니다.");
        showMessage("duplicate-error", "✗ 중복확인 중 오류가 발생했습니다.");
      });
    });
  } else {
    console.error("중복확인 버튼 또는 이메일 입력 필드를 찾을 수 없습니다!");
  }

  // 2. 이메일 인증번호 발송
  if (sendVerificationBtn) {
    sendVerificationBtn.addEventListener("click", function() {
      console.log("인증번호 발송 버튼 클릭됨");
      
      if (!isDuplicateChecked) {
        alert("먼저 이메일 중복확인을 완료해주세요.");
        return;
      }

      const email = emailInput.value.trim();
      
      console.log("AJAX 요청 시작 - 인증번호 발송");
      
      fetch(`${contextPath}/ajax?key=user&methodName=sendVerification`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `email=${encodeURIComponent(email)}`
      })
      .then(response => response.json())
      .then(data => {
        console.log("인증번호 발송 응답:", data);
        if (data.success) {
          if (verificationCodeGroup) {
            verificationCodeGroup.classList.remove("hidden");
          }
          sendVerificationBtn.disabled = true;
          sendVerificationBtn.textContent = "발송완료";
          startTimer();
          alert("인증번호가 발송되었습니다. 이메일을 확인해주세요.");
        } else {
          alert("인증번호 발송에 실패했습니다: " + (data.message || ""));
        }
      })
      .catch(error => {
        console.error('인증번호 발송 오류:', error);
        alert("인증번호 발송 중 오류가 발생했습니다.");
      });
    });
  }

  // 3. 인증번호 확인
  if (verifyCodeBtn) {
    verifyCodeBtn.addEventListener("click", function() {
      console.log("인증번호 확인 버튼 클릭됨");
      
      const code = verificationCodeInput.value.trim();
      const email = emailInput.value.trim();

      if (!code) {
        alert("인증번호를 입력해주세요.");
        showMessage("verification-error", "✗ 인증번호를 입력해주세요.");
        return;
      }

      console.log("AJAX 요청 시작 - 인증번호 확인");

      fetch(`${contextPath}/ajax?key=user&methodName=verifyCode`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `email=${encodeURIComponent(email)}&code=${encodeURIComponent(code)}`
      })
      .then(response => response.json())
      .then(data => {
        console.log("인증번호 확인 응답:", data);
        if (data.verified) {
          alert("이메일 인증이 완료되었습니다.");
          showMessage("verification-success", "✓ 이메일 인증이 완료되었습니다.");
          hideMessage("verification-error");
          isEmailVerified = true;
          verifyCodeBtn.disabled = true;
          verifyCodeBtn.textContent = "인증완료";
          clearTimer();
        } else {
          alert("인증번호가 일치하지 않습니다.");
          showMessage("verification-error", "✗ " + (data.message || "인증번호가 일치하지 않습니다."));
          hideMessage("verification-success");
        }
      })
      .catch(error => {
        console.error('인증확인 오류:', error);
        alert("인증확인 중 오류가 발생했습니다.");
        showMessage("verification-error", "✗ 인증확인 중 오류가 발생했습니다.");
      });
    });
  }

  // 4. 이메일 입력 변경 시 초기화
  if (emailInput) {
    emailInput.addEventListener("input", function() {
      isDuplicateChecked = false;
      isEmailVerified = false;
      if (duplicateCheckBtn) {
        duplicateCheckBtn.disabled = false;
        duplicateCheckBtn.textContent = "중복확인";
      }
      if (sendVerificationBtn) {
        sendVerificationBtn.disabled = true;
        sendVerificationBtn.textContent = "인증번호 발송";
      }
      if (emailVerificationGroup) {
        emailVerificationGroup.classList.add("hidden");
      }
      if (verificationCodeGroup) {
        verificationCodeGroup.classList.add("hidden");
      }
      hideMessage("duplicate-success");
      hideMessage("duplicate-error");
      hideMessage("verification-success");
      hideMessage("verification-error");
      clearTimer();
    });
  }

  // 5. 주소찾기 기능 (다음 Postcode API)
  if (findAddressBtn) {
    findAddressBtn.addEventListener("click", function() {
      console.log("주소찾기 버튼 클릭됨");
      execDaumPostcode();
    });
  }

  function execDaumPostcode() {
    if (typeof daum !== 'undefined' && daum.Postcode) {
      new daum.Postcode({
        oncomplete: function(data) {
          console.log("주소 선택됨:", data);
          
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

  // 6. 비밀번호 유효성 검사
  function validatePassword(password) {
    // 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합, 10~16자리
    const hasLower = /[a-z]/.test(password);
    const hasUpper = /[A-Z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    const isValidLength = password.length >= 10 && password.length <= 16;

    const typeCount = [hasLower, hasUpper, hasNumber, hasSpecial].filter(Boolean).length;
    return typeCount >= 2 && isValidLength;
  }

  // 7. 폼 전체 유효성 검사
  function validateForm() {
    console.log("폼 유효성 검사 시작");
    
    // 이메일 검증
    const email = emailInput.value.trim();
    if (!isValidEmail(email)) {
      alert("올바른 이메일 형식으로 입력해주세요.");
      emailInput.focus();
      return false;
    }

    if (!isDuplicateChecked) {
      alert("이메일 중복확인을 완료해주세요.");
      emailInput.focus();
      return false;
    }

    if (!isEmailVerified) {
      alert("이메일 인증을 완료해주세요.");
      if (verificationCodeInput) verificationCodeInput.focus();
      return false;
    }

    // 비밀번호 검증
    const password = passwordInput ? passwordInput.value : "";
    if (!validatePassword(password)) {
      alert("비밀번호는 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합하여 10~16자리로 입력해주세요.");
      if (passwordInput) passwordInput.focus();
      return false;
    }

    // 비밀번호 확인
    const passwordConfirm = passwordConfirmInput ? passwordConfirmInput.value : "";
    if (password !== passwordConfirm) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      if (passwordConfirmInput) passwordConfirmInput.focus();
      return false;
    }

    // 이름 검증
    const nameInput = document.getElementById("name");
    const name = nameInput ? nameInput.value.trim() : "";
    if (!name) {
      alert("이름을 입력해주세요.");
      if (nameInput) nameInput.focus();
      return false;
    }

    console.log("폼 유효성 검사 통과");
    return true;
  }

  // 8. 회원가입 폼 제출
  if (registerForm) {
    registerForm.addEventListener("submit", function(e) {
      console.log("회원가입 폼 제출 이벤트 발생");
      e.preventDefault();

      if (validateForm()) {
        console.log("폼 검증 통과 - 회원가입 진행");
        
        // 휴대폰 번호 조합
        const mobileFirst = document.getElementById("mobile-first")?.value || "010";
        const mobileMiddle = document.getElementById("mobile-middle")?.value || "";
        const mobileLast = document.getElementById("mobile-last")?.value || "";
        const fullPhone = mobileFirst + mobileMiddle + mobileLast;

        const formData = {
          email: emailInput.value.trim(),
          password: passwordInput.value,
          userName: document.getElementById("name").value.trim(),
          phone: fullPhone,
          zipCode: document.getElementById("zipCode")?.value.trim() || "",
          address: document.getElementById("address1")?.value.trim() || "",
          detailAddress: document.getElementById("address2")?.value.trim() || ""
        };

        console.log("전송할 데이터:", formData);

        fetch(`${contextPath}/ajax?key=user&methodName=register`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(formData)
        })
        .then(response => response.json())
        .then(data => {
          console.log("회원가입 응답:", data);
          if (data.success) {
            alert("회원가입이 완료되었습니다!");
            window.location.href = contextPath + "/user/login.jsp";
          } else {
            alert("회원가입 실패: " + (data.message || "알 수 없는 오류"));
          }
        })
        .catch(error => {
          console.error("회원가입 오류:", error);
          alert("회원가입 중 오류가 발생했습니다: " + error.message);
        });
      }
    });
  }

  // 9. 휴대폰 번호 입력 제한 (숫자만)
  const phoneInputs = ["mobile-middle", "mobile-last"];
  phoneInputs.forEach(id => {
    const input = document.getElementById(id);
    if (input) {
      input.addEventListener("input", function() {
        this.value = this.value.replace(/[^0-9]/g, "");
      });
    }
  });

  console.log("회원가입 페이지 스크립트 초기화 완료");
});