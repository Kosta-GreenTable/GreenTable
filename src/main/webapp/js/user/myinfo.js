/**
 * 회원정보 수정 페이지 자바스크립트
 */
document.addEventListener("DOMContentLoaded", function () {

  console.log("myinfo.js loaded");

  // DOM 요소
  const memberInfoForm = document.getElementById("memberInfoForm");
  console.log("memberInfoForm:", memberInfoForm); // 제대로 form이 로드되었는지 확인
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
  const btnSave = document.querySelector(".btn-save");
  console.log("btnSave:", btnSave);

  // contextPath가 제대로 정의되었는지 확인
 const contextPath = document.body.getAttribute("data-context-path");

 
 console.log("contextPath:", contextPath); // contextPath 확인

  

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

  
  // 회원정보 수정 폼 제출 처리
 function handleMemberInfoSubmit(e) {
    console.log("✅ handleMemberInfoSubmit 실행됨");
    if (e) e.preventDefault();

    const userId = parseInt(document.getElementById("userId").value);
    const email = document.getElementById("userEmail").value;
    const userName = document.getElementById("userName").value;
    const password = document.getElementById("userPassword").value;
    const passwordConfirm = document.getElementById("userPasswordConfirm").value;
    const phone = `${document.getElementById("mobile-first").value}-${document.getElementById("mobile-middle").value}-${document.getElementById("mobile-last").value}`;

    const zipCode = parseInt(document.getElementById("zipCode").value, 10) || 0;
    const address1 = document.getElementById("address1").value;
    const address2 = document.getElementById("address2").value;

    // 비밀번호 확인
    if (password && password !== passwordConfirm) {
      alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
      return;
    }

    const jsonData = {
      email,
     password,
     userId,
  userInfoDto: {
    userName,
    phone,
    zipCode,
    address: address1,
    detailAddress: address2
  }
    };

    // fetch로 서버에 요청
    fetch("/GreenTable/front?key=ajaxUser&methodName=updateUser", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(jsonData),
    })
      .then((res) => res.json())
      .then((data) => {
        alert(data.message || "처리 완료");
        if (data.success) {
          window.location.href = "mypage.jsp";
        }
      })
      .catch((err) => {
        console.error("에러:", err);
        alert("회원정보 수정 실패");
      });
  }

  // btnSave 클릭 시 handleMemberInfoSubmit 실행
  if (btnSave) {
    btnSave.addEventListener("click", function (e) {
      console.log("저장 버튼 클릭됨");
      handleMemberInfoSubmit(e); // 폼 제출 대신 직접 함수 호출
    });
  } else {
    console.error("btnSave is null. 저장 버튼을 찾을 수 없습니다.");
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

  const withdrawReason = document.getElementById("withdrawReason");
  const withdrawReasonDetail = document.getElementById("withdrawReasonDetail").value;

  if (withdrawReason.value === "") {
    alert("탈퇴 사유를 선택해주세요.");
    withdrawReason.focus();
    return;
  }

 fetch(contextPath + "/front?key=ajaxUser&methodName=withdrawUser", {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify({
        password: withdrawPassword.value,
        reason: withdrawReason.value,
        reasonDetail: document.getElementById("withdrawReasonDetail").value
    }),
})
.then((res) => res.json())
.then((data) => {
    console.log("서버 응답:", data);

    if (data.status === "success") {
        alert(data.message || "회원 탈퇴가 완료되었습니다. 그동안 그린테이블을 이용해주셔서 감사합니다.");
        window.location.href = contextPath + "/register.jsp";
    } else if (data.status === "wrong_password") {
        alert("비밀번호가 틀렸습니다.");
    } else {
        alert(data.message || "탈퇴 처리 중 오류가 발생했습니다.");
        window.location.href = contextPath + "/index.jsp";
    }
})
.catch((err) => {
    console.error("탈퇴 요청 중 에러:", err);
    alert("서버와의 통신 중 문제가 발생했습니다.");
});
}

  // 이벤트 리스너 등록

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
