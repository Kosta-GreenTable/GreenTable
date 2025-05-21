

document.addEventListener("DOMContentLoaded", function() {
	// 탭 전환 기능
	const tabBtns = document.querySelectorAll(".tab-btn");
	const couponTabs = document.querySelectorAll(".coupon-tab");

	tabBtns.forEach((btn) => {
		btn.addEventListener("click", function() {
			// 모든 탭 비활성화
			tabBtns.forEach((b) => b.classList.remove("active"));
			couponTabs.forEach((tab) => (tab.style.display = "none"));

			// 선택한 탭 활성화
			this.classList.add("active");
			const tabId = this.getAttribute("data-tab");
			document.getElementById(tabId + "-coupons").style.display = "block";
		});
	});

	// 쿠폰 등록 버튼 이벤트
	const registerBtn = document.querySelector(".register-btn");
	const couponInput = document.querySelector(".coupon-input");

	if (registerBtn) {
		registerBtn.addEventListener("click", function() {
			const couponCode = couponInput.value.trim();
			if (!couponCode) {
				alert("쿠폰 번호를 입력해주세요.");
				couponInput.focus();
				return;
			}

			// 실제 구현에서는 서버로 쿠폰 코드 확인 요청
			alert("입력하신 쿠폰 번호를 확인 중입니다.");

			// 임시 응답 (실제로는 서버 응답에 따라 처리)
			setTimeout(() => {
				alert("유효하지 않은 쿠폰 번호입니다.");
				couponInput.value = "";
				couponInput.focus();
			}, 500);
		});
	}

	// 샘플 데이터 표시 여부 설정
	const showSampleData = false; // true로 설정하면 샘플 데이터 표시

	if (showSampleData) {
		document.querySelector(".no-coupons").style.display = "none";
		document.querySelector(".coupon-grid").style.display = "grid";
		document.querySelector(".count-number").innerHTML =
			"2 <span>개</span>";
		document.querySelector('[data-tab="available"]').innerHTML =
			"사용 가능 쿠폰 (2)";
	}
});
/**
* 
*/