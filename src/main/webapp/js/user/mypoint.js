document.addEventListener("DOMContentLoaded", function () {
        // 기간 버튼 이벤트
        const periodBtns = document.querySelectorAll(".period-btn");
        periodBtns.forEach((btn) => {
          btn.addEventListener("click", function () {
            periodBtns.forEach((b) => b.classList.remove("active"));
            this.classList.add("active");

            // 날짜 설정 로직
            const today = new Date();
            const endDate = document.getElementById("end-date");
            const startDate = document.getElementById("start-date");

            // 오늘 날짜 설정
            const endDateStr = formatDate(today);
            endDate.value = endDateStr;

            // 시작 날짜 계산
            let startDateObj = new Date(today);
            if (this.textContent === "1개월") {
              startDateObj.setMonth(today.getMonth() - 1);
            } else if (this.textContent === "3개월") {
              startDateObj.setMonth(today.getMonth() - 3);
            } else if (this.textContent === "6개월") {
              startDateObj.setMonth(today.getMonth() - 6);
            } else if (this.textContent === "1년") {
              startDateObj.setFullYear(today.getFullYear() - 1);
            }

            startDate.value = formatDate(startDateObj);
          });
        });

        // 초기 날짜 설정 - 1개월
        const today = new Date();
        const oneMonthAgo = new Date(today);
        oneMonthAgo.setMonth(today.getMonth() - 1);

        document.getElementById("end-date").value = formatDate(today);
        document.getElementById("start-date").value = formatDate(oneMonthAgo);

        // 날짜 포맷 함수 (YYYY-MM-DD)
        function formatDate(date) {
          const year = date.getFullYear();
          const month = String(date.getMonth() + 1).padStart(2, "0");
          const day = String(date.getDate()).padStart(2, "0");
          return `${year}-${month}-${day}`;
        }

        // 조회 버튼 이벤트
        const searchBtn = document.querySelector(".search-btn");
        if (searchBtn) {
          searchBtn.addEventListener("click", function () {
            // 실제 구현에서는 서버에 조회 요청
            alert("적립금 내역을 조회합니다.");
          });
        }

        // 샘플 데이터 표시 여부 설정
        const showSampleData = false; // true로 설정하면 샘플 데이터 표시

        if (showSampleData) {
          document.querySelector(".no-history").style.display = "none";
          document.querySelector(".point-history").style.display = "table";
          document.querySelector(".point-pagination").style.display = "flex";
          document.querySelector(".point-amount").innerHTML =
            "1,800 <span>원</span>";
        }
      });