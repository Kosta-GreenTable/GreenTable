document.addEventListener("DOMContentLoaded", () => {
  const menuItems = document.querySelectorAll(".menu-item");
  const contentSections = document.querySelectorAll(".content-section");

  // 공지사항 데이터 예시 - 상세 내용 추가
  const noticeData = [
    {
      번호: 1,
      제목: "그린테이블 신규 서비스 오픈 안내",
      날짜: "2025-05-10",
      작성자: "관리자",
      조회: 542,
      내용: "안녕하세요, 그린테이블입니다.<br><br>저희 그린테이블이 새로운 서비스를 오픈했습니다. 새로운 서비스를 통해 더 신선한 식재료와 더 다양한 레시피를 만나보실 수 있습니다.<br><br>1. 신규 정기 배송 서비스<br>- 주 1회, 주 2회, 주 3회 원하는 날짜에 신선한 식재료 배송<br>- 맞춤형 식단 구성 가능<br><br>2. 그린테이블 앱 출시<br>- iOS, Android 모두 지원<br>- 언제 어디서나 주문 가능<br>- 실시간 배송 조회 기능<br><br>많은 이용 부탁드립니다.<br><br>그린테이블 드림",
    },
    {
      번호: 2,
      제목: "5월 연휴 배송 안내",
      날짜: "2025-05-01",
      작성자: "관리자",
      조회: 423,
      내용: "안녕하세요, 그린테이블입니다.<br><br>5월 연휴 기간 동안의 배송 일정을 안내드립니다.<br><br>- 5월 5일(월): 어린이날 휴무, 배송 없음<br>- 5월 6일(화): 대체공휴일, 일부 지역 배송 가능<br>- 5월 7일(수): 정상 배송<br><br>연휴 기간 주문은 가능하나 배송이 다소 지연될 수 있으니 양해 부탁드립니다.<br><br>편안한 연휴 보내시기 바랍니다.<br><br>그린테이블 드림",
    },
    {
      번호: 3,
      제목: "그린테이블 앱 출시 안내",
      날짜: "2025-04-15",
      작성자: "관리자",
      조회: 367,
      내용: "안녕하세요, 그린테이블입니다.<br><br>그린테이블 모바일 앱이 드디어 출시되었습니다!<br><br>앱 주요 기능:<br>- 간편한 주문 및 결제<br>- 정기 구독 서비스 신청 및 관리<br>- 실시간 배송 조회<br>- 레시피 검색 및 저장<br>- 영양 정보 확인<br>- 푸시 알림 서비스<br><br>앱 스토어 또는 구글 플레이에서 '그린테이블'을 검색하여 다운로드 받으실 수 있습니다.<br><br>앱 설치 및 첫 주문 시 10% 할인 쿠폰을 드립니다.<br><br>많은 이용 부탁드립니다.<br><br>그린테이블 드림",
    },
    {
      번호: 4,
      제목: "정기 구독 서비스 업데이트 안내",
      날짜: "2025-04-05",
      작성자: "관리자",
      조회: 289,
      내용: "안녕하세요, 그린테이블입니다.<br><br>정기 구독 서비스가 업데이트되어 안내드립니다.<br><br>변경사항:<br>1. 구독 주기 선택의 다양화 (주 1회 ~ 월 1회)<br>2. 배송일 변경 기능 추가<br>3. 식단 구성 커스터마이징 강화<br>4. 구독자 전용 특별 할인 혜택 추가<br><br>해당 변경사항은 2025년 4월 10일부터 적용됩니다.<br>기존 정기 구독 고객분들은 마이페이지에서 새로운 기능을 이용해보실 수 있습니다.<br><br>더 나은 서비스로 보답하겠습니다.<br><br>그린테이블 드림",
    },
    {
      번호: 5,
      제목: "신선식품 품질 관리 정책 안내",
      날짜: "2025-03-22",
      작성자: "관리자",
      조회: 325,
      내용: "안녕하세요, 그린테이블입니다.<br><br>저희 그린테이블의 신선식품 품질 관리 정책에 대해 안내드립니다.<br><br>1. 식재료 선별 과정<br>- 계약 농가에서 직접 수확한 신선한 재료만 사용<br>- 매일 아침 전문가의 품질 검수 진행<br><br>2. 보관 및 배송<br>- 신선도 유지를 위한 콜드체인 시스템 운영<br>- 최적의 온도 유지를 위한 특수 포장재 사용<br><br>3. 품질 보증 정책<br>- 신선도 미달 상품 100% 교환 및 환불<br>- 고객 만족도 조사를 통한 지속적인 품질 개선<br><br>고객님께 최상의 식재료를 제공하기 위해 최선을 다하겠습니다.<br><br>그린테이블 드림",
    },
    {
      번호: 6,
      제목: "그린테이블 친환경 포장재 도입 안내",
      날짜: "2025-03-15",
      작성자: "관리자",
      조회: 412,
      내용: "안녕하세요, 그린테이블입니다.<br><br>환경을 생각하는 그린테이블의 친환경 포장재 도입 소식을 알려드립니다.<br><br>1. 생분해성 포장재 사용<br>- 옥수수 전분 기반의 100% 생분해성 포장재 도입<br>- 6개월 이내 자연 분해되는 친환경 소재<br><br>2. 재활용 보냉재<br>- 물로 만든 친환경 보냉재 사용<br>- 반품 시 회수하여 재사용하는 시스템 구축<br><br>3. 종이 포장재<br>- 비닐 테이프 대신 종이 테이프 사용<br>- FSC 인증 받은 지속가능한 산림에서 생산된 종이만 사용<br><br>그린테이블은 앞으로도 환경을 생각하는 다양한 노력을 계속하겠습니다.<br><br>그린테이블 드림",
    },
    {
      번호: 7,
      제목: "회원 등급 제도 개편 안내",
      날짜: "2025-03-01",
      작성자: "관리자",
      조회: 387,
      내용: "안녕하세요, 그린테이블입니다.<br><br>더 나은 혜택을 제공하기 위해 회원 등급 제도가 개편되었습니다.<br><br>신규 회원 등급:<br>1. 그린 (일반회원)<br>- 신규 가입 시 3,000원 적립금 지급<br>- 구매금액의 1% 적립<br><br>2. 실버 (누적 구매 50만원 이상)<br>- 구매금액의 3% 적립<br>- 월 1회 무료배송 쿠폰 제공<br><br>3. 골드 (누적 구매 100만원 이상)<br>- 구매금액의 5% 적립<br>- 월 2회 무료배송 쿠폰 제공<br>- 신제품 우선 구매 기회<br><br>4. 플래티넘 (VIP, 누적 구매 300만원 이상)<br>- 구매금액의 7% 적립<br>- 무제한 무료배송<br>- 전용 고객센터 이용 가능<br>- 시즌 선물 증정<br><br>새로운 등급 제도는 2025년 3월 15일부터 적용됩니다.<br><br>그린테이블 드림",
    },
    {
      번호: 8,
      제목: "2025년 봄 신메뉴 출시 안내",
      날짜: "2025-02-15",
      작성자: "관리자",
      조회: 452,
      내용: "안녕하세요, 그린테이블입니다.<br><br>2025년 봄을 맞아 새로운 메뉴가 출시되었습니다.<br><br>1. 샐러드 부문<br>- 봄나물 샐러드: 제철 봄나물과 구운 닭가슴살의 조화<br>- 딸기 아보카도 샐러드: 상큼한 딸기와 고소한 아보카도의 만남<br><br>2. 런치박스 부문<br>- 봄나물 비빔밥: 신선한 봄나물을 아낌없이 담은 비빔밥<br>- 봄맞이 도시락: 제철 채소와 단백질을 균형있게 구성<br><br>3. 정기배송 부문<br>- 디톡스 2주 프로그램: 가벼운 봄을 위한 저칼로리 식단<br>- 면역력 강화 4주 프로그램: 환절기 건강을 위한 영양 식단<br><br>신메뉴는 웹사이트 및 앱에서 주문 가능합니다.<br><br>그린테이블 드림",
    },
    {
      번호: 9,
      제목: "설 연휴 고객센터 운영 안내",
      날짜: "2025-02-01",
      작성자: "관리자",
      조회: 276,
      내용: "안녕하세요, 그린테이블입니다.<br><br>설 연휴 기간 고객센터 운영 시간을 안내드립니다.<br><br>- 2월 8일(금): 오전 9시 ~ 오후 2시<br>- 2월 9일(토) ~ 2월 12일(화): 휴무<br>- 2월 13일(수): 정상 운영 (오전 9시 ~ 오후 6시)<br><br>연휴 기간 문의사항은 홈페이지 1:1 문의 게시판을 이용해 주시면 순차적으로 답변 드리겠습니다.<br><br>풍성하고 행복한 설 명절 되시기 바랍니다.<br><br>그린테이블 드림",
    },
    {
      번호: 10,
      제목: "그린테이블 신년 이벤트 안내",
      날짜: "2025-01-05",
      작성자: "관리자",
      조회: 531,
      내용: "안녕하세요, 그린테이블입니다.<br><br>2025년 새해를 맞아 신년 이벤트를 진행합니다.<br><br>1. 신년 맞이 할인<br>- 전 제품 15% 할인 (코드: HAPPY2025)<br>- 기간: 1월 5일 ~ 1월 15일<br><br>2. 새해 복주머니 이벤트<br>- 3만원 이상 구매 시 랜덤 쿠폰 증정<br>- 최대 5만원 할인 쿠폰 당첨 가능<br><br>3. 정기 구독 혜택<br>- 신규 정기 구독 시 첫 배송 30% 할인<br>- 정기 구독 고객 대상 사은품 증정<br><br>이벤트 참여는 웹사이트 및 앱에서 가능합니다.<br>많은 참여 부탁드립니다.<br><br>그린테이블 드림",
    },
  ];

  // FAQ 데이터 예시 - 상세 내용 추가
  const faqData = [
    {
      번호: 1,
      분류: "배송",
      질문: "배송은 얼마나 걸리나요?",
      답변: "주문 후 배송까지는 일반적으로 다음과 같이 소요됩니다:<br><br>- 수도권 지역: 주문 다음날 배송<br>- 지방 도시: 1-2일 내 배송<br>- 도서산간 지역: 2-3일 내 배송<br><br>주문 시 선택하신 배송 예정일에 맞춰 배송됩니다. 새벽 배송의 경우 오전 7시 이전에 배송이 완료됩니다.",
    },
    {
      번호: 2,
      분류: "결제",
      질문: "어떤 결제 방법을 지원하나요?",
      답변: "그린테이블에서는 다음과 같은 결제 방법을 지원하고 있습니다:<br><br>- 신용카드 / 체크카드<br>- 실시간 계좌이체<br>- 가상계좌 (무통장 입금)<br>- 카카오페이, 네이버페이, 페이코 등 간편결제<br>- 휴대폰 소액결제<br><br>정기 구독의 경우 신용카드 자동결제만 가능합니다.",
    },
    {
      번호: 3,
      분류: "제품",
      질문: "식재료는 어디에서 공급받나요?",
      답변: "그린테이블은 신선한 식재료 공급을 위해 다음과 같은 원칙을 지키고 있습니다:<br><br>1. 친환경 계약 농가와 직거래<br>- 전국 각지의 우수 농가와 계약을 통해 직접 공급<br>- 정기적인 농가 방문 및 품질 관리<br><br>2. 유기농 및 친환경 인증 제품 우선 사용<br>- 가능한 모든 제품에 대해 친환경, 유기농 인증 받은 제품 사용<br><br>3. 로컬 푸드 우선주의<br>- 제철 식재료 및 지역 특산물 활용<br>- 신선도 유지를 위한 콜드체인 시스템 운영<br><br>4. 식재료 이력 관리 시스템<br>- 모든 식재료의 원산지 및 생산 과정 추적 관리<br>- 웹사이트에서 QR코드를 통한 이력 확인 가능",
    },
    {
      번호: 4,
      분류: "배송",
      질문: "배송 시간을 선택할 수 있나요?",
      답변: "네, 그린테이블에서는 다음과 같은 배송 시간 옵션을 제공하고 있습니다:<br><br>1. 새벽 배송 (오전 7시 이전)<br>- 수도권 및 주요 도시 가능<br>- 전날 오후 6시까지 주문 시 적용<br><br>2. 일반 배송 (오전 9시 ~ 오후 6시)<br>- 전국 가능<br>- 당일 오전 11시까지 주문 시 당일 출고<br><br>3. 저녁 배송 (오후 6시 ~ 오후 9시)<br>- 수도권 지역만 가능<br>- 당일 오후 2시까지 주문 시 적용<br><br>배송 시간대 지정은 주문 시 배송 정보 입력 페이지에서 선택하실 수 있습니다.",
    },
    {
      번호: 5,
      분류: "회원",
      질문: "회원 탈퇴는 어떻게 하나요?",
      답변: "회원 탈퇴는 다음 절차를 통해 진행할 수 있습니다:<br><br>1. 웹사이트 로그인<br>2. 마이페이지 접속<br>3. 하단의 '회원정보 관리' 클릭<br>4. '회원 탈퇴' 버튼 클릭<br>5. 비밀번호 확인 후 탈퇴 사유 선택<br>6. 탈퇴 완료<br><br>유의사항:<br>- 탈퇴 시 보유하신 적립금, 쿠폰 등은 모두 소멸됩니다.<br>- 주문 완료 후 배송 중인 상품이 있는 경우 배송 완료 후 탈퇴 가능합니다.<br>- 탈퇴 후 7일간 재가입이 제한됩니다.<br>- 작성한 게시글은 탈퇴 후에도 유지됩니다.",
    },
    {
      번호: 6,
      분류: "결제",
      질문: "환불 정책은 어떻게 되나요?",
      답변: "그린테이블의 환불 정책은 다음과 같습니다:<br><br>1. 배송 전 주문 취소<br>- 결제 완료 후 배송 시작 전: 전액 환불<br>- 카드 결제: 취소 승인<br>- 계좌이체/무통장: 영업일 기준 1-3일 내 환불<br><br>2. 배송 후 교환/환불<br>- 상품 하자, 오배송의 경우: 전액 환불 (배송비 포함)<br>- 단순 변심: 상품 가격 환불 (배송비 고객 부담)<br><br>3. 신선식품 품질 문제<br>- 수령 후 24시간 이내 신고 시 전액 환불 또는 재배송<br>- 사진 등 증빙자료 필요<br><br>4. 정기구독 해지<br>- 다음 결제 예정일 3일 전까지 해지 가능<br>- 이미 결제된 회차는 배송 시작 전까지만 취소 가능<br><br>환불 신청은 마이페이지 > 주문내역에서 가능합니다.",
    },
    {
      번호: 7,
      분류: "제품",
      질문: "상품의 유통기한은 어떻게 확인하나요?",
      답변: "상품의 유통기한은 다음과 같은 방법으로 확인하실 수 있습니다:<br><br>1. 상품 패키지<br>- 모든 제품에 유통기한 또는 제조일자 표기<br>- 냉장/냉동 보관 방법 및 기한 별도 표기<br><br>2. 신선식품 관리 카드<br>- 모든 배송 박스에 포함<br>- 품목별 보관 방법 및 권장 소비 기한 안내<br><br>3. 온라인 확인<br>- 주문내역에서 배송된 상품의 제조일자 및 유통기한 확인 가능<br>- 상품별 QR코드 스캔 시 상세 정보 확인 가능<br><br>그린테이블은 최대한 신선한 상품을 배송하기 위해 유통기한이 넉넉한 제품만 배송하고 있습니다.",
    },
    {
      번호: 8,
      분류: "회원",
      질문: "비밀번호를 분실했어요",
      답변: "비밀번호를 분실하셨다면 다음 절차를 통해 재설정하실 수 있습니다:<br><br>1. 로그인 페이지에서 '비밀번호 찾기' 클릭<br>2. 가입 시 등록한 이메일 주소 또는 휴대폰 번호 입력<br>3. 본인 인증 (이메일 인증번호 또는 휴대폰 인증번호)<br>4. 새로운 비밀번호 설정<br><br>이메일 또는 휴대폰 번호에 접근할 수 없는 경우:<br>- 고객센터 (1588-1234)로 문의<br>- 본인 확인 후 임시 비밀번호 발급 가능<br><br>보안을 위해 주기적인 비밀번호 변경을 권장드립니다.",
    },
    {
      번호: 9,
      분류: "배송",
      질문: "배송 상태를 확인하려면 어떻게 해야 하나요?",
      답변: "배송 상태는 다음과 같은 방법으로 확인하실 수 있습니다:<br><br>1. 웹사이트/앱 확인<br>- 로그인 후 마이페이지 > 주문/배송 조회<br>- 실시간 배송 상태 및 위치 추적 가능<br><br>2. 알림 서비스<br>- 문자 메시지: 주요 배송 단계별 SMS 발송<br>- 이메일: 상세 배송 정보 제공<br>- 앱 푸시 알림: 실시간 배송 상태 변경 시 알림<br><br>3. 배송 조회 번호 활용<br>- 배송사 웹사이트에서 송장번호로 조회<br>- 주문 완료 이메일이나 마이페이지에서 송장번호 확인 가능<br><br>배송 관련 문의나 특이사항은 고객센터(1588-1234)로 연락주시면 신속하게 도와드리겠습니다.",
    },
    {
      번호: 10,
      분류: "기타",
      질문: "그린테이블은 어떤 회사인가요?",
      답변: "그린테이블은 건강한 식생활과 지속가능한 푸드 시스템을 지향하는 푸드테크 기업입니다.<br><br>1. 회사 소개<br>- 2020년 설립<br>- 친환경 식재료와 건강한 식단 제공<br>- 국내 50개 이상의 계약 농가와 협력<br><br>2. 핵심 가치<br>- 신선함: 최고 품질의 식재료만 사용<br>- 지속가능성: 친환경 농법과 패키징<br>- 건강: 영양사와 셰프의 균형 잡힌 식단 설계<br>- 편의성: 바쁜 현대인을 위한 시간 절약<br><br>3. 사회적 책임<br>- 농가 직거래를 통한 공정 거래<br>- 탄소 배출 감소를 위한 친환경 패키징<br>- 식품 낭비 방지를 위한 정확한 양 계산<br>- 취약 계층 대상 건강 식단 기부 프로그램 운영<br><br>그린테이블은 앞으로도 더 건강하고 지속가능한 식생활 문화를 만들기 위해 노력하겠습니다.",
    },
  ];

  // 페이지네이션 및 데이터 렌더링 함수
  function renderTable(sectionId, data, page = 1, rowsPerPage = 10) {
    const tableBody = document.querySelector(
      `#${sectionId} .content-table tbody`
    );
    const pagination = document.querySelector(`#${sectionId} .pagination`);
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    const visibleData = data.slice(start, end);

    // 클릭 이벤트를 포함한 테이블 행 생성
    tableBody.innerHTML = "";
    visibleData.forEach((row) => {
      const tr = document.createElement("tr");
      tr.dataset.id = row.번호; // 데이터 ID 저장

      Object.entries(row).forEach(([key, value]) => {
        // 내용과 답변은 테이블에 표시하지 않음
        if (key !== "내용" && key !== "답변") {
          const td = document.createElement("td");
          td.textContent = value;
          tr.appendChild(td);
        }
      });

      // 행 클릭 이벤트 추가
      tr.addEventListener("click", () => {
        if (sectionId === "notice") {
          showNoticeDetail(row);
        } else if (sectionId === "faq") {
          showFaqDetail(row);
        }
      });

      tableBody.appendChild(tr);
    });

    const totalPages = Math.ceil(data.length / rowsPerPage);
    pagination.innerHTML = Array.from(
      { length: totalPages },
      (_, i) =>
        `<button class="${i + 1 === page ? "active" : ""}" data-page="${
          i + 1
        }">${i + 1}</button>`
    ).join("");

    pagination.querySelectorAll("button").forEach((button) => {
      button.addEventListener("click", () => {
        renderTable(sectionId, data, parseInt(button.dataset.page, 10));

        // 상세 내용 숨기기
        document.getElementById("notice-detail").style.display = "none";
        document.getElementById("faq-detail").style.display = "none";
      });
    });
  }

  // 공지사항 상세 보기 함수
  function showNoticeDetail(notice) {
    const detailSection = document.getElementById("notice-detail");

    // 상세 내용 채우기
    document.getElementById("notice-title").textContent = notice.제목;
    document.getElementById(
      "notice-date"
    ).textContent = `작성일: ${notice.날짜}`;
    document.getElementById(
      "notice-writer"
    ).textContent = `작성자: ${notice.작성자}`;
    document.getElementById(
      "notice-views"
    ).textContent = `조회수: ${notice.조회}`;
    document.getElementById("notice-content").innerHTML = notice.내용;

    // 테이블 숨기기
    document.querySelector("#notice .content-table").style.display = "none";
    document.querySelector("#notice .pagination").style.display = "none";

    // 상세 내용 표시
    detailSection.style.display = "block";
  }

  // FAQ 상세 보기 함수
  function showFaqDetail(faq) {
    const detailSection = document.getElementById("faq-detail");

    // 상세 내용 채우기
    document.getElementById("faq-category").textContent = `[${faq.분류}]`;
    document.getElementById("faq-title").textContent = faq.질문;
    document.getElementById("faq-content").innerHTML = faq.답변;

    // 테이블 숨기기
    document.querySelector("#faq .content-table").style.display = "none";
    document.querySelector("#faq .pagination").style.display = "none";

    // 상세 내용 표시
    detailSection.style.display = "block";
  }

  // 공지사항 목록으로 돌아가기
  window.hideNoticeDetail = function () {
    document.getElementById("notice-detail").style.display = "none";
    document.querySelector("#notice .content-table").style.display = "table";
    document.querySelector("#notice .pagination").style.display = "flex";
  };

  // FAQ 목록으로 돌아가기
  window.hideFaqDetail = function () {
    document.getElementById("faq-detail").style.display = "none";
    document.querySelector("#faq .content-table").style.display = "table";
    document.querySelector("#faq .pagination").style.display = "flex";
  };

  // 메뉴 클릭 이벤트
  menuItems.forEach((item) => {
    item.addEventListener("click", () => {
      menuItems.forEach((menu) => menu.classList.remove("active"));
      item.classList.add("active");

      contentSections.forEach((section) => section.classList.remove("active"));
      document.getElementById(item.dataset.content).classList.add("active");

      // 상세 내용 숨기기
      document.getElementById("notice-detail").style.display = "none";
      document.getElementById("faq-detail").style.display = "none";

      // 테이블 표시
      document.querySelector("#notice .content-table").style.display = "table";
      document.querySelector("#notice .pagination").style.display = "flex";
      document.querySelector("#faq .content-table").style.display = "table";
      document.querySelector("#faq .pagination").style.display = "flex";

      if (item.dataset.content === "notice") {
        renderTable("notice", noticeData);
      } else if (item.dataset.content === "faq") {
        renderTable("faq", faqData);
      }
    });
  });

  // 초기 렌더링
  renderTable("notice", noticeData);
});
