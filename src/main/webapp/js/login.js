// 로그인 페이지 스크립트
document.addEventListener("DOMContentLoaded", function () {
  // 탭 전환 기능
  const tabBtns = document.querySelectorAll(".tab-btn");
  const tabContents = document.querySelectorAll(".tab-content");

  tabBtns.forEach((btn) => {
    btn.addEventListener("click", function () {
      // 모든 탭 버튼에서 active 클래스 제거
      tabBtns.forEach((b) => b.classList.remove("active"));
      // 클릭한 탭 버튼에 active 클래스 추가
      this.classList.add("active");

      // 모든 탭 컨텐츠 숨기기
      tabContents.forEach((content) => content.classList.remove("active"));
      // 클릭한 탭에 해당하는 컨텐츠 표시
      const tabId = this.getAttribute("data-tab");
      document.getElementById(`${tabId}-tab`).classList.add("active");
    });
  });

  // 아이디 저장 기능 (쿠키 사용)
  const memberIdField = document.getElementById("member-id");
  const saveIdCheckbox = document.getElementById("save-id");

  // 저장된 아이디가 있으면 불러오기
  const savedId = getCookie("savedMemberId");
  if (savedId) {
    memberIdField.value = savedId;
    saveIdCheckbox.checked = true;
  }

  // 로그인 폼 제출 시 아이디 저장
  const memberLoginForm = document.getElementById("member-login-form");
  memberLoginForm.addEventListener("submit", function (e) {
    e.preventDefault(); // 실제 제출은 방지 (백엔드 구현 전이므로)

    const memberId = memberIdField.value;
    const saveId = saveIdCheckbox.checked;

    if (saveId) {
      // 30일간 쿠키 저장
      setCookie("savedMemberId", memberId, 30);
    } else {
      // 쿠키 삭제
      deleteCookie("savedMemberId");
    }

    // 로그인 성공 가정하고 메인 페이지로 이동
    showMessage("로그인 성공! 메인 페이지로 이동합니다.");
    setTimeout(() => {
      window.location.href = "index.html";
    }, 1500);
  });

  // 비회원 주문조회 폼
  const nonMemberForm = document.getElementById("non-member-login-form");
  nonMemberForm.addEventListener("submit", function (e) {
    e.preventDefault(); // 실제 제출은 방지 (백엔드 구현 전이므로)

    // 주문조회 성공 가정하고 메시지 표시
    showMessage("주문 정보를 확인했습니다. 주문 상세 페이지로 이동합니다.");
    setTimeout(() => {
      // 실제로는 주문 상세 페이지로 이동할 것
      window.location.href = "index.html";
    }, 1500);
  });

  // 아이디/비밀번호 찾기 모달
  const findIdPwLink = document.getElementById("find-id-pw");
  const findModal = document.getElementById("find-modal");
  const closeBtn = document.querySelector(".close-btn");

  findIdPwLink.addEventListener("click", function (e) {
    e.preventDefault();
    findModal.classList.add("active");
  });

  closeBtn.addEventListener("click", function () {
    findModal.classList.remove("active");
  });

  // 모달 바깥 클릭 시 닫기
  window.addEventListener("click", function (e) {
    if (e.target === findModal) {
      findModal.classList.remove("active");
    }
  });

  // 모달 내 탭 전환
  const modalTabs = document.querySelectorAll(".modal-tab");
  const modalContents = document.querySelectorAll(".modal-tab-content");

  modalTabs.forEach((tab) => {
    tab.addEventListener("click", function () {
      // 모든 탭에서 active 클래스 제거
      modalTabs.forEach((t) => t.classList.remove("active"));
      // 클릭한 탭에 active 클래스 추가
      this.classList.add("active");

      // 모든 컨텐츠 숨기기
      modalContents.forEach((c) => c.classList.remove("active"));
      // 클릭한 탭에 해당하는 컨텐츠 표시
      const contentId = this.getAttribute("data-tab") + "-content";
      document.getElementById(contentId).classList.add("active");
    });
  });

  // 아이디 찾기 폼
  const findIdForm = document.getElementById("find-id-form");
  findIdForm.addEventListener("submit", function (e) {
    e.preventDefault(); // 실제 제출은 방지 (백엔드 구현 전이므로)

    // 아이디 찾기 성공 가정하고 메시지 표시
    showMessage("회원님의 이메일은 user@example.com 입니다.");
    setTimeout(() => {
      findModal.classList.remove("active");
    }, 1500);
  });

  // 비밀번호 찾기 폼
  const findPwForm = document.getElementById("find-pw-form");
  findPwForm.addEventListener("submit", function (e) {
    e.preventDefault(); // 실제 제출은 방지 (백엔드 구현 전이므로)

    // 임시 비밀번호 발급 가정하고 메시지 표시
    showMessage("임시 비밀번호가 이메일로 발송되었습니다.");
    setTimeout(() => {
      findModal.classList.remove("active");
    }, 1500);
  });

  // 인증번호 받기 버튼
  const verifyBtns = document.querySelectorAll(".verify-btn");
  verifyBtns.forEach((btn) => {
    if (btn.textContent === "인증번호 받기") {
      btn.addEventListener("click", function () {
        showMessage("인증번호가 이메일로 발송되었습니다.");

        // 인증 시간 카운트다운 표시 (시뮬레이션)
        const timeElement =
          this.closest(".input-group").querySelector(".verify-time");
        if (timeElement) {
          timeElement.style.display = "block";
          simulateCountdown(timeElement);
        }
      });
    } else if (btn.textContent === "인증확인") {
      btn.addEventListener("click", function () {
        showMessage("인증이 완료되었습니다.");
      });
    }
  });

  // 회원가입 링크
  const registerLink = document.getElementById("register");
  registerLink.addEventListener("click", function (e) {
    e.preventDefault();
    showMessage("회원가입 페이지로 이동합니다.");
    setTimeout(() => {
      // 실제로는 회원가입 페이지로 이동할 것
      window.location.href = "index.html";
    }, 1500);
  });

  // 소셜 로그인 버튼
  const socialBtns = document.querySelectorAll(".social-btn");
  socialBtns.forEach((btn) => {
    btn.addEventListener("click", function () {
      const provider = this.classList.contains("kakao") ? "카카오" : "구글";
      showMessage(`${provider} 로그인 창을 엽니다.`);

      // 소셜 로그인 팝업 시뮬레이션
      setTimeout(() => {
        window.open("about:blank", "소셜로그인", "width=500,height=600");
      }, 500);
    });
  });

  // 유틸리티 함수

  // 쿠키 설정 함수
  function setCookie(name, value, days) {
    let expires = "";
    if (days) {
      const date = new Date();
      date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
      expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
  }

  // 쿠키 가져오기 함수
  function getCookie(name) {
    const nameEQ = name + "=";
    const ca = document.cookie.split(";");
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) === " ") c = c.substring(1, c.length);
      if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
  }

  // 쿠키 삭제 함수
  function deleteCookie(name) {
    document.cookie = name + "=; Max-Age=-99999999; path=/";
  }

  // 메시지 표시 함수
  function showMessage(message) {
    // 이미 표시된 메시지가 있으면 제거
    const existingMessage = document.querySelector(".message-toast");
    if (existingMessage) {
      document.body.removeChild(existingMessage);
    }

    // 새 메시지 생성
    const toast = document.createElement("div");
    toast.className = "message-toast";
    toast.textContent = message;

    // 스타일 설정
    toast.style.position = "fixed";
    toast.style.top = "20px";
    toast.style.left = "50%";
    toast.style.transform = "translateX(-50%)";
    toast.style.backgroundColor = "rgba(0, 196, 113, 0.9)";
    toast.style.color = "white";
    toast.style.padding = "12px 20px";
    toast.style.borderRadius = "5px";
    toast.style.zIndex = "9999";
    toast.style.boxShadow = "0 2px 10px rgba(0, 0, 0, 0.2)";

    document.body.appendChild(toast);

    // 3초 후 자동 제거
    setTimeout(() => {
      if (document.body.contains(toast)) {
        document.body.removeChild(toast);
      }
    }, 3000);
  }

  // 카운트다운 시뮬레이션 함수
  function simulateCountdown(element) {
    let minutes = 5;
    let seconds = 0;

    const interval = setInterval(() => {
      if (seconds === 0) {
        if (minutes === 0) {
          clearInterval(interval);
          element.textContent = "인증 시간이 만료되었습니다.";
          return;
        }
        minutes--;
        seconds = 59;
      } else {
        seconds--;
      }

      // 시간 형식 맞춤 (05:00 형식)
      const formattedMinutes = minutes.toString().padStart(2, "0");
      const formattedSeconds = seconds.toString().padStart(2, "0");
      element.textContent = `남은시간 ${formattedMinutes}:${formattedSeconds}`;
    }, 1000);
  }
});
