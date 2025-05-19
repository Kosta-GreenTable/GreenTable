/**
 * 회원정보 수정 페이지 자바스크립트
 */
document.addEventListener("DOMContentLoaded", function () {
  // DOM 요소
  const memberInfoForm = document.getElementById("memberInfoForm");
  const btnWithdraw = document.querySelector(".btn-withdraw");
  const withdrawModal = document.getElementById("withdrawModal");
  const closeModal = document.querySelector(".close-modal");
  const cancelBtns = document.querySelectorAll(".btn-cancel");
  const btnWithdrawConfirm = document.querySelector(".btn-withdraw-confirm");
  const withdrawAgree = document.getElementById("withdrawAgree");
  const withdrawPassword = document.getElementById("withdrawPassword");
  const passwordInput = document.getElementById("userPassword");
  const passwordConfirmInput = document.getElementById("userPasswordConfirm");
  const btnAddressSearch = document.querySelector(".btn-address-search");
  const btnVerifyEmail = document.querySelector(".btn-verify");

  /**
   * 비밀번호 유효성 검사
   * @param {string} password - 검사할 비밀번호
   * @returns {boolean} 유효성 여부
   */
  function isValidPassword(password) {
    // 8~20자의 영문 대소문자, 숫자, 특수문자 조합
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
    return passwordRegex.test(password);
  }

  /**
   * 이메일 유효성 검사
   * @param {string} email - 검사할 이메일
   * @returns {boolean} 유효성 여부
   */
  function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  /**
   * 전화번호 유효성 검사
   * @param {string} phone - 검사할 전화번호
   * @returns {boolean} 유효성 여부
   */
  function isValidPhone(phone) {
    // 010-1234-5678 또는 01012345678 형식
    const phoneRegex = /^(01[016789])-?([0-9]{3,4})-?([0-9]{4})$/;
    return phoneRegex.test(phone);
  }

  /**
   * 회원정보 수정 폼 제출 처리
   * @param {Event} e - 폼 제출 이벤트
   */
  function handleMemberInfoSubmit(e) {
    e.preventDefault();

    // 비밀번호 검증
    const password = passwordInput.value;
    const passwordConfirm = passwordConfirmInput.value;

    if (password) {
      if (!isValidPassword(password)) {
        alert(
          "비밀번호는 8~20자의 영문 대소문자, 숫자, 특수문자를 조합하여 입력해주세요."
        );
        passwordInput.focus();
        return;
      }

      if (password !== passwordConfirm) {
        alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        passwordConfirmInput.focus();
        return;
      }
    }

    // 이메일 검증
    const email = document.getElementById("userEmail").value;
    if (!isValidEmail(email)) {
      alert("유효한 이메일 주소를 입력해주세요.");
      document.getElementById("userEmail").focus();
      return;
    }

    // 전화번호 검증
    const phone = document.getElementById("userPhone").value;
    if (!isValidPhone(phone)) {
      alert("유효한 전화번호를 입력해주세요.");
      document.getElementById("userPhone").focus();
      return;
    }

    // TODO: 서버에 회원정보 수정 요청 로직
    alert("회원정보가 성공적으로 수정되었습니다.");

    // 수정 성공 시 마이페이지로 이동
    // window.location.href = 'mypage.html';
  }

  /**
   * 회원 탈퇴 모달 열기
   */
  function openWithdrawModal() {
    withdrawModal.style.display = "flex";
  }

  /**
   * 회원 탈퇴 모달 닫기
   */
  function closeWithdrawModal() {
    withdrawModal.style.display = "none";
    withdrawAgree.checked = false;
    withdrawPassword.value = "";
    document.getElementById("withdrawReason").selectedIndex = 0;
    document.getElementById("withdrawReasonDetail").value = "";
  }

  /**
   * 회원 탈퇴 처리
   */
  function handleWithdraw() {
    // 동의 체크 여부 확인
    if (!withdrawAgree.checked) {
      alert("회원 탈퇴 동의에 체크해주세요.");
      return;
    }

    // 비밀번호 입력 여부 확인
    if (!withdrawPassword.value.trim()) {
      alert("비밀번호를 입력해주세요.");
      withdrawPassword.focus();
      return;
    }

    // 탈퇴 사유 선택 여부 확인
    const withdrawReason = document.getElementById("withdrawReason");
    if (withdrawReason.value === "") {
      alert("탈퇴 사유를 선택해주세요.");
      withdrawReason.focus();
      return;
    }

    // TODO: 서버에 회원 탈퇴 요청 로직

    // 탈퇴 성공 시
    alert(
      "회원 탈퇴가 완료되었습니다. 그동안 그린테이블을 이용해주셔서 감사합니다."
    );

    // 로그인 페이지로 이동
    window.location.href = "index.html";
  }

  // 이벤트 리스너 등록

  // 회원정보 수정 폼 제출
  if (memberInfoForm) {
    memberInfoForm.addEventListener("submit", handleMemberInfoSubmit);
  }

  // 회원 탈퇴 버튼 클릭
  if (btnWithdraw) {
    btnWithdraw.addEventListener("click", openWithdrawModal);
  }

  // 모달 닫기 버튼 클릭
  if (closeModal) {
    closeModal.addEventListener("click", closeWithdrawModal);
  }

  // 취소 버튼 클릭
  cancelBtns.forEach((btn) => {
    btn.addEventListener("click", function () {
      if (this.closest(".modal-content")) {
        closeWithdrawModal();
      } else {
        // 회원정보 수정 폼 취소 시 마이페이지로 이동
        window.location.href = "mypage.html";
      }
    });
  });

  // 회원 탈퇴 확인 버튼 클릭
  if (btnWithdrawConfirm) {
    btnWithdrawConfirm.addEventListener("click", handleWithdraw);
  }

  // 모달 바깥 클릭 시 모달 닫기
  window.addEventListener("click", function (event) {
    if (event.target === withdrawModal) {
      closeWithdrawModal();
    }
  });

  // 주소 검색 버튼 클릭
  if (btnAddressSearch) {
    btnAddressSearch.addEventListener("click", function () {
      // TODO: 주소 검색 API 연동 로직
      alert("주소 검색 기능은 준비 중입니다.");
    });
  }

  // 이메일 중복확인 버튼 클릭
  if (btnVerifyEmail) {
    btnVerifyEmail.addEventListener("click", function () {
      const email = document.getElementById("userEmail").value;

      if (!isValidEmail(email)) {
        alert("유효한 이메일 주소를 입력해주세요.");
        document.getElementById("userEmail").focus();
        return;
      }

      // TODO: 이메일 중복확인 로직
      alert("사용 가능한 이메일입니다.");
    });
  }

  // 휴대폰 인증 버튼 클릭
  const btnVerifyPhone = document.querySelectorAll(".btn-verify")[1];
  if (btnVerifyPhone) {
    btnVerifyPhone.addEventListener("click", function () {
      const phone = document.getElementById("userPhone").value;

      if (!isValidPhone(phone)) {
        alert("유효한 전화번호를 입력해주세요.");
        document.getElementById("userPhone").focus();
        return;
      }

      // TODO: 휴대폰 인증 로직
      alert("인증번호가 발송되었습니다.");
    });
  }
});
