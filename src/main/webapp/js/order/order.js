document.addEventListener("DOMContentLoaded", () => { 
    // 이메일 자동완성
    document.getElementById('emailSelect').addEventListener('change', function() {
        document.getElementById('email2').value = this.value;
    });

    // 배송지 설정 주문자 정보와 동일 체크박스
    document.getElementById('sameAsOrderer').addEventListener('change', function() {
        if (this.checked) {
          document.getElementById('recipient').value = document.getElementById('name').value;
          document.getElementById('recipientPhone1').value = document.getElementById('phone1').value;
          document.getElementById('recipientPhone2').value = document.getElementById('phone2').value;
        }
      });

    // 전체 동의 체크박스
    const agreeAll = document.getElementById("agreeAll");
    const agreeCheckboxes = document.querySelectorAll(".agree-checkbox");
  
    agreeAll.addEventListener("change", () => {
      agreeCheckboxes.forEach((checkbox) => {
        checkbox.checked = agreeAll.checked;
      });
    });
  
    agreeCheckboxes.forEach((checkbox) => {
      checkbox.addEventListener("change", () => {
        if (!checkbox.checked) {
          agreeAll.checked = false;
        } else if (Array.from(agreeCheckboxes).every((cb) => cb.checked)) {
          agreeAll.checked = true;
        }
      });
    });

});



