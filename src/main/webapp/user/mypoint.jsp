<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>적립금 내역 | 마이페이지 | Green Table</title>
    <link rel="stylesheet" href="${path }/css/common/styles.css" />
    <link rel="stylesheet" href="${path }/css/user/mypage.css" />
    <link rel="stylesheet" href="${path }/css/user/mypoint.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
   
  </head>
  <body>
    <!-- 헤더 컨테이너 -->
    <jsp:include page="/common/header.jsp" />

    <!-- 메인 컨텐츠 - 적립금 내역 섹션 -->
    <main class="mypage-container">
      <h1 class="page-title">적립금 내역</h1>

      <div class="mypage-content">
        <!-- 사이드바 메뉴 -->
        <div class="mypage-sidebar">
          <div class="user-profile">
            <div class="profile-image">
              <i class="fas fa-user-circle"></i>
            </div>
            <div class="user-info">
              <p class="user-name">${sessionScope.loginUser.userInfoDto.userName}님</p>
              
            </div>
          </div>

          <nav class="sidebar-menu">
            <h3>나의 쇼핑정보</h3>
            <ul>
              <li><a href="mypage.jsp">주문/배송 조회</a></li>
              <li><a href="mycancel.jsp">취소/반품 내역</a></li>
              <li class="active"><a href="mypoint.jsp">적립금 내역</a></li>
              <li><a href="mycoupon.jsp">쿠폰 내역</a></li>
              <li><a href="myreview.jsp">상품 리뷰</a></li>
              <li><a href="myqna.jsp">상품 문의</a></li>
            </ul>
            <h3>나의 계정설정</h3>
            <ul>
              <li><a href="myinfo.jsp">회원정보 수정</a></li>
            </ul>
          </nav>
        </div>

        <!-- 마이페이지 메인 내용 -->
        <div class="mypage-main">
          <!-- 적립금 요약 -->
          <section class="point-summary">
            <div class="point-title">사용 가능한 적립금</div>
            <div class="point-amount">2000 <span>원</span></div>
          </section>

          <!-- 적립금 필터 -->
          <section class="point-filter">
            <div class="filter-period">
              <button class="period-btn active">1개월</button>
              <button class="period-btn">3개월</button>
              <button class="period-btn">6개월</button>
              <button class="period-btn">1년</button>
            </div>
            <div class="filter-date">
              <input type="date" class="date-input" id="start-date" />
              <span>~</span>
              <input type="date" class="date-input" id="end-date" />
              <button class="search-btn">조회</button>
            </div>
          </section>

          <!-- 적립금 내역 표 -->
          <section class="point-history-section">
            <!-- 내역이 없을 때 -->
<!--            <div class="no-history"> -->
<!--                <p>적립금 내역이 없습니다.</p> -->
<!--              </div> -->

            <!-- 내역이 있을 때 (초기에는 display: none 상태) -->
            <table class="point-history" style="display: active">
              <thead>
                <tr>
                  <th>날짜</th>
                  <th>내용</th>
                  <th>적립금</th>
                  <th>유효기간</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>2025.05.21</td>
                  <td class="detail">회원 가입 적립</td>
                  <td class="amount plus">+2000원</td>
                  <td>2025.05.21</td>
                </tr>
 <!--                 <tr>
                  <td>2025.05.01</td>
                  <td class="detail">리뷰 작성 적립</td>
                  <td class="amount plus">+300원</td>
                  <td>2026.05.01</td>
                </tr>
                <tr>
                  <td>2025.04.20</td>
                  <td class="detail">적립금 사용</td>
                  <td class="amount minus">-1,000원</td>
                  <td>-</td>
                </tr>
                <tr>
                  <td>2025.04.15</td>
                  <td class="detail">회원가입 적립</td>
                  <td class="amount plus">+2,000원</td>
                  <td>2026.04.15</td>
                </tr> -->
              </tbody>
            </table>

            <!-- 페이지네이션 -->
            <div class="point-pagination" style="display: none">
              <a href="#" class="page-btn arrow"
                ><i class="fas fa-angle-double-left"></i
              ></a>
              <a href="#" class="page-btn arrow"
                ><i class="fas fa-angle-left"></i
              ></a>
              <a href="#" class="page-btn active">1</a>
              <a href="#" class="page-btn">2</a>
              <a href="#" class="page-btn">3</a>
              <a href="#" class="page-btn">4</a>
              <a href="#" class="page-btn">5</a>
              <a href="#" class="page-btn arrow"
                ><i class="fas fa-angle-right"></i
              ></a>
              <a href="#" class="page-btn arrow"
                ><i class="fas fa-angle-double-right"></i
              ></a>
            </div>
          </section>

          <!-- 적립금 안내 -->
          <section class="point-info">
            <div class="section-header">
              <h3>적립금 안내</h3>
            </div>
            <div
              class="info-box"
              style="
                padding: 20px;
                background-color: #f9f9f9;
                border-radius: 5px;
                font-size: 14px;
                line-height: 1.6;
                color: #666;
              "
            >
              <p>• 주문 시 적립금 사용 가능 (일부 상품 제외)</p>
              <p>• 적립금은 구매금액의 1%가 기본 적립됩니다.</p>
              <p>• 상품 리뷰 작성 시 300원의 적립금이 추가됩니다.</p>
              <p>• 적립금은 지급일로부터 1년간 유효합니다.</p>
              <p>• 최소 적립금 사용 금액은 1,000원 이상입니다.</p>
              <p>
                • 각종 할인 및 이벤트, 행사로 인한 특별 적립금이 추가될 수
                있습니다.
              </p>
              <p>• 자세한 내용은 고객센터로 문의해주세요.</p>
            </div>
          </section>
        </div>
      </div>
    </main>

    <!-- 푸터 컨테이너 -->
    <jsp:include page="/common/footer.jsp" />

	<script src="${path }/js/user/script.js"></script>
	<script src="${path }/js/user/mypage.js"></script>
	<script src="${path }/js/user/mypoint.js"></script>
    
  </body>
</html>
