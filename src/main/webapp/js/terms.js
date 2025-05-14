/**
 * 이용약관 페이지 자바스크립트
 * - 전체 동의 체크박스 동작
 * - 필수 항목 체크에 따른 다음 버튼 활성화
 */
document.addEventListener("DOMContentLoaded", function () {
  const checkAll = document.getElementById("check-all");
  const termsCheckboxes = document.querySelectorAll(".terms-checkbox");
  const requiredCheckboxes = document.querySelectorAll(
    ".terms-checkbox[required]"
  );
  const nextBtn = document.getElementById("terms-next-btn");
  const termsForm = document.getElementById("terms-form");
  const cancelBtn = document.querySelector(".cancel-btn");

  // 전체 동의 체크박스 이벤트
  if (checkAll) {
    checkAll.addEventListener("change", function () {
      termsCheckboxes.forEach((checkbox) => {
        checkbox.checked = checkAll.checked;
      });
      updateNextButtonState();
    });
  }

  // 개별 체크박스 이벤트
  termsCheckboxes.forEach((checkbox) => {
    checkbox.addEventListener("change", function () {
      updateCheckAllState();
      updateNextButtonState();
    });
  });

  // 취소 버튼 클릭 이벤트
  if (cancelBtn) {
    cancelBtn.addEventListener("click", function () {
      if (confirm("약관 동의를 취소하시겠습니까? 메인 페이지로 이동합니다.")) {
        window.location.href = "index.html";
      }
    });
  }

  // 약관 내용 클릭 시 스크롤 동작 개선
  const termsTextAreas = document.querySelectorAll(".terms-text-area");
  termsTextAreas.forEach((area) => {
    area.addEventListener("click", function () {
      this.focus();
    });
  });

  /**
   * 전체 체크박스 상태 업데이트
   */
  function updateCheckAllState() {
    const allChecked = Array.from(termsCheckboxes).every(
      (checkbox) => checkbox.checked
    );
    checkAll.checked = allChecked;
  }

  /**
   * 필수 체크박스 상태에 따라 다음 버튼 활성화/비활성화
   */
  function updateNextButtonState() {
    const requiredAllChecked = Array.from(requiredCheckboxes).every(
      (checkbox) => checkbox.checked
    );
    nextBtn.disabled = !requiredAllChecked;
  }
  // 폼 제출 이벤트
  if (termsForm) {
    termsForm.addEventListener("submit", function (e) {
      e.preventDefault(); // 기본 제출 동작 방지

      const requiredAllChecked = Array.from(requiredCheckboxes).every(
        (checkbox) => checkbox.checked
      );

      if (!requiredAllChecked) {
        alert("필수 약관에 모두 동의해주세요.");
        return;
      }

      // 선택적 항목 동의 상태 저장 (실제로는 서버로 전송하거나 세션에 저장)
      const marketingChecked =
        document.getElementById("terms-marketing")?.checked || false;
      const locationChecked =
        document.getElementById("terms-location")?.checked || false;
      const smsChecked = document.getElementById("terms-sms")?.checked || false;
      const emailChecked =
        document.getElementById("terms-email")?.checked || false;

      // 로컬 스토리지에 임시 저장 (예시)
      localStorage.setItem("marketing_agreed", marketingChecked);
      localStorage.setItem("location_agreed", locationChecked);
      localStorage.setItem("sms_agreed", smsChecked);
      localStorage.setItem("email_agreed", emailChecked);

      // 회원가입 페이지로 이동
      window.location.href = "register.html";
    });
  }
});
