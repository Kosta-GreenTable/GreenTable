<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>적립금 내역 | 마이페이지 | Green Table</title>
    <link rel="stylesheet" href="${path}/css/common/styles.css" />
    <link rel="stylesheet" href="${path}/css/user/mypage.css" />
    <link rel="stylesheet" href="${path}/css/user/mypoint.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <jsp:include page="/common/header.jsp" />

    <!-- 로그인 체크 -->
    <c:if test="${empty sessionScope.loginUser}">
      <jsp:forward page="auth-required.jsp" />
    </c:if>

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
              <p class="user-name">
                ${sessionScope.loginUser.userInfoDto.userName}님
              </p>
            </div>
          </div>

          <nav class="sidebar-menu">
            <h3>나의 쇼핑정보</h3>
            <ul>
              <li>
                <a href="${path}/front?key=mypage&methodName=mypage"
                  >주문/배송 조회</a
                >
              </li>
              <li><a href="${path}/user/mycancel.jsp">취소/환불 내역</a></li>
              <li class="active">
                <a href="${path}/user/mypoint.jsp">적립금 내역</a>
              </li>
              <li><a href="${path}/user/mycoupon.jsp">쿠폰 내역</a></li>
              <li>
                <a href="${path}/front?key=review&methodName=myReviews"
                  >상품 리뷰</a
                >
              </li>
              <li>
                <a href="${path}/front?key=qna&methodName=myQnas">상품 문의</a>
              </li>
            </ul>
            <h3>나의 계정설정</h3>
            <ul>
              <li><a href="${path}/user/myinfo.jsp">회원정보 수정</a></li>
            </ul>
          </nav>
        </div>

        <!-- 마이페이지 메인 내용 -->
        <div class="mypage-main">
          <!-- 적립금 요약 -->
          <section class="point-summary">
            <div class="point-title">사용 가능한 적립금</div>
            <div class="point-amount">
              <fmt:formatNumber
                value="${sessionScope.loginUser.userInfoDto.point}"
              />
              <span>원</span>
            </div>
          </section>

          <!-- 적립금 내역 표 -->
          <section class="point-history-section">
            <c:choose>
              <c:when test="${empty pointHistory}">
                <div class="no-history">
                  <p>적립금 내역이 없습니다.</p>
                </div>
              </c:when>
              <c:otherwise>
                <table class="point-history">
                  <thead>
                    <tr>
                      <th>날짜</th>
                      <th>내용</th>
                      <th>적립금</th>
                      <th>잔액</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach var="history" items="${pointHistory}">
                      <tr>
                        <td>
                          <fmt:formatDate
                            value="${history.createdAt}"
                            pattern="yyyy.MM.dd"
                          />
                        </td>
                        <td class="detail">${history.reason}</td>
                        <td
                          class="amount ${history.pointType eq '적립' ? 'plus' : 'minus'}"
                        >
                          <c:choose>
                            <c:when test="${history.pointType eq '적립'}">
                              +<fmt:formatNumber
                                value="${history.pointChange}"
                              />원
                            </c:when>
                            <c:otherwise>
                              -<fmt:formatNumber
                                value="${history.pointChange}"
                              />원
                            </c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <fmt:formatNumber value="${history.balanceAfter}" />원
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </c:otherwise>
            </c:choose>
          </section>

          <!-- 적립금 안내 -->
          <section class="point-info">
            <div class="section-header">
              <h3>적립금 안내</h3>
            </div>
            <div class="info-box">
              <p>• 회원 가입 시 2,000원의 적립금이 지급됩니다.</p>
              <p>• 주문 시 적립금 사용 가능 (일부 상품 제외)</p>
              <p>• 구매금액의 1%가 기본 적립됩니다.</p>
              <p>• 상품 리뷰 작성 시 300원의 적립금이 추가됩니다.</p>
              <p>• 적립금은 지급일로부터 1년간 유효합니다.</p>
              <p>• 최소 적립금 사용 금액은 1,000원 이상입니다.</p>
              <p>• 자세한 내용은 고객센터로 문의해주세요.</p>
            </div>
          </section>
        </div>
      </div>
    </main>

    <jsp:include page="/common/footer.jsp" />
  </body>
</html>
