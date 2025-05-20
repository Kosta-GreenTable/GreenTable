<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>그린테이블 - 고객센터</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/cs/cs.css" />
  </head>
  <body>
    <!-- 헤더 포함 -->
    <jsp:include page="../common/header.jsp" />

    <!-- 고객센터 메인 컨텐츠 -->
    <div class="service-container">
      <!-- 사이드바 -->
      <aside class="sidebar">
        <h2>고객센터</h2>
        <ul class="menu">
          <li class="menu-item active" data-content="notice">공지사항</li>
          <li class="menu-item" data-content="faq">FAQ</li>
        </ul>
        <div class="contact-info">
          <h3>고객상담센터</h3>
          <p class="contact">1588-1234</p>
          <p>월~금: 오전 9시 ~ 오후 6시</p>
          <p>토요일/일요일/공휴일은 휴무입니다.</p>
        </div>
      </aside>

      <!-- 콘텐츠 영역 -->
      <div class="content">
        <!-- 공지사항 -->
        <section id="notice" class="content-section active">
          <div class="content-header">
            <h2>공지사항</h2>
          </div>

          <table class="content-table">
            <thead>
              <tr>
                <th>번호</th>
                <th>제목</th>
                <th>날짜</th>
                <th>작성자</th>
                <th>조회</th>
              </tr>
            </thead>
            <tbody>
              <!-- JavaScript로 동적 데이터 추가 -->
            </tbody>
          </table>
          <div class="pagination">
            <!-- JavaScript로 동적 페이지네이션 추가 -->
          </div>

          <!-- 공지사항 상세 보기 영역 -->
          <div id="notice-detail" class="content-detail">
            <h3 id="notice-title"></h3>
            <div class="detail-info">
              <span id="notice-date"></span>
              <span id="notice-writer"></span>
              <span id="notice-views"></span>
            </div>
            <div id="notice-content" class="detail-content"></div>
            <button class="back-button" onclick="hideNoticeDetail()">
              목록으로 돌아가기
            </button>
          </div>
        </section>

        <!-- FAQ -->
        <section id="faq" class="content-section">
          <div class="content-header">
            <h2>FAQ</h2>
          </div>

          <table class="content-table">
            <thead>
              <tr>
                <th>번호</th>
                <th>분류</th>
                <th>질문</th>
              </tr>
            </thead>
            <tbody>
              <!-- JavaScript로 동적 데이터 추가 -->
            </tbody>
          </table>
          <div class="pagination">
            <!-- JavaScript로 동적 페이지네이션 추가 -->
          </div>

          <!-- FAQ 상세 보기 영역 -->
          <div id="faq-detail" class="content-detail">
            <div class="faq-question">
              <span id="faq-category"></span>
              <h3 id="faq-title"></h3>
            </div>
            <div id="faq-content" class="detail-content"></div>
            <button class="back-button" onclick="hideFaqDetail()">
              목록으로 돌아가기
            </button>
          </div>
        </section>
      </div>
    </div>

    <!-- 푸터 포함 -->
    <jsp:include page="../common/footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/js/include.js"></script>
    <script src="${pageContext.request.contextPath}/cs/cs.js"></script>
  </body>
</html>