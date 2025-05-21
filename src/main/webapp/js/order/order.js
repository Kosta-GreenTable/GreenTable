document.addEventListener("DOMContentLoaded", () => { 
   // 주소 검색 관련 요소
   const findAddressBtn = document.querySelector(".find-address-btn");
      const addressModal = document.getElementById("address-modal");
      const closeAddressBtn = addressModal?.querySelector(".close-btn");
  
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
	  
	  //주소 찾기
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
	  	          const address1Element = document.getElementById("address");
	  	          
	  	          if (zipCodeElement) zipCodeElement.value = data.zonecode;
	  	          if (address1Element) address1Element.value = addr;

	  	          // 상세주소 입력창으로 포커스 이동
	  	          const address2Element = document.getElementById("addressDetail");
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



