<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ page import="site.greentable.util.ImageUtil" %> <%@ page import="java.lang.System" %>
<%
    String s3BaseUrl = System.getenv("S3_BASE_URL");
    if (s3BaseUrl == null) {
        s3BaseUrl = "https://greentable-images-your-region.s3.ap-northeast-2.amazonaws.com";
    }
    pageContext.setAttribute("s3BaseUrl", s3BaseUrl);
%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>취소/환불 내역 | 마이페이지 | Green Table</title>

    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/common/styles.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/user/mypage.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/user/mycancel.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <!-- 헤더 컨테이너 -->
    <jsp:include page="../common/header.jsp" />

    <!-- 메인 컨텐츠 - 취소/환불 내역 섹션 -->
    <main class="mypage-container">
      <h1 class="page-title">취소/환불 내역</h1>
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
                <a
                  href="${pageContext.request.contextPath}/front?key=mypage&methodName=mypage"
                  >주문/배송 조회</a
                >
              </li>
              <li class="active">
                <a href="${pageContext.request.contextPath}/user/mycancel.jsp"
                  >취소/환불 내역</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/user/mypoint.jsp"
                  >적립금 내역</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/user/mycoupon.jsp"
                  >쿠폰 내역</a
                >
              </li>
              <li>
                <a
                  href="${pageContext.request.contextPath}/front?key=review&methodName=myReviews"
                  >상품 리뷰</a
                >
              </li>
              <li>
                <a
                  href="${pageContext.request.contextPath}/front?key=qna&methodName=myQnas"
                  >상품 문의</a
                >
              </li>
            </ul>
            <h3>나의 계정설정</h3>
            <ul>
              <li>
                <a href="${pageContext.request.contextPath}/user/myinfo.jsp"
                  >회원정보 수정</a
                >
              </li>
            </ul>
          </nav>
        </div>

        <!-- 마이페이지 메인 내용 -->
        <div class="mypage-main">
          <!-- 취소/환불 현황 요약 -->
          <section class="cancel-summary">
            <div class="summary-box">
              <div class="summary-item">
                <p class="item-title">취소 처리중</p>
                <p class="item-count">${cancelProcessingCount}</p>
              </div>
              <div class="summary-divider"></div>
              <div class="summary-item">
                <p class="item-title">환불 완료</p>
                <p class="item-count">${refundCompletedCount}</p>
              </div>
              <div class="summary-divider"></div>
              <div class="summary-item">
                <p class="item-title">총 취소 금액</p>
                <p class="item-count">
                  <fmt:formatNumber
                    value="${totalCancelAmount}"
                    pattern="#,###"
                  />원
                </p>
              </div>
            </div>
          </section>

          <!-- 조회 기간 필터 -->
          <section class="cancel-filter">
            <div class="filter-container">
              <div class="filter-group">
                <label>조회 기간</label>
                <div class="period-selector">
                  <button class="period-btn active" data-period="1">
                    1개월
                  </button>
                  <button class="period-btn" data-period="3">3개월</button>
                  <button class="period-btn" data-period="6">6개월</button>
                  <button class="period-btn" data-period="12">1년</button>
                </div>
              </div>
              <div class="filter-group">
                <label>처리 상태</label>
                <select name="cancelStatus" id="cancelStatus">
                  <option value="all">전체</option>
                  <option value="CANCEL_REQUESTED">취소 요청</option>
                  <option value="CANCEL_PROCESSING">취소 처리중</option>
                  <option value="CANCEL_COMPLETED">취소 완료</option>
                  <option value="REFUND_PROCESSING">환불 처리중</option>
                  <option value="REFUND_COMPLETED">환불 완료</option>
                </select>
              </div>
            </div>
          </section>

          <!-- 취소/환불 내역 목록 -->
          <section class="cancel-list-section">
            <table class="cancel-table">
              <thead>
                <tr>
                  <th>주문번호</th>
                  <th>상품정보</th>
                  <th>취소/환불 사유</th>
                  <th>취소 금액</th>
                  <th>처리 상태</th>
                  <th>신청일</th>
                </tr>
              </thead>
              <tbody>
                <c:choose>
                  <c:when test="${empty cancelList}">
                    <!-- 취소/환불 내역이 없는 경우 -->
                    <tr class="no-data">
                      <td colspan="6">취소/환불 내역이 없습니다.</td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <!-- 취소/환불 내역이 있는 경우 -->
                    <c:forEach
                      var="cancel"
                      items="${cancelList}"
                      varStatus="status"
                    >
                      <tr
                        class="cancel-item"
                        data-cancel-id="${cancel.cancelId}"
                      >
                        <td class="order-number">
                          <a
                            href="${pageContext.request.contextPath}/front?key=order&methodName=orderDetail&orderNo=${cancel.orderNo}"
                          >
                            ${cancel.orderNo}
                          </a>
                        </td>
                        <td class="product-info">
                          <div class="product-info-cell">
                            <c:choose>
                              <c:when test="${cancel.productImage.startsWith('products/')}">
                                <!-- S3 이미지 URL -->
                                <img
                                  src="${s3BaseUrl}/${cancel.productImage}"
                                  alt="${cancel.productName}"
                                  onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';"
                                />
                              </c:when>
                              <c:otherwise>
                                <!-- 기존 로컬 이미지 -->
                                <img
                                  src="${s3BaseUrl}/${cancel.productImage}"
                                  alt="${cancel.productName}"
                                  onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';"
                                />
                              </c:otherwise>
                            </c:choose>
                            <div class="product-details">
                              <span class="product-name"
                                >${cancel.productName}</span
                              >
                              <span class="product-option"
                                >수량: ${cancel.quantity}개</span
                              >
                            </div>
                          </div>
                        </td>
                        <td class="cancel-reason">${cancel.cancelReason}</td>
                        <td class="cancel-amount">
                          <fmt:formatNumber
                            value="${cancel.cancelAmount}"
                            pattern="#,###"
                          />원
                        </td>
                        <td class="cancel-status">
                          <c:choose>
                            <c:when
                              test="${cancel.status == 'CANCEL_REQUESTED'}"
                            >
                              <span class="status-badge requested"
                                >취소 요청</span
                              >
                            </c:when>
                            <c:when
                              test="${cancel.status == 'CANCEL_PROCESSING'}"
                            >
                              <span class="status-badge processing"
                                >취소 처리중</span
                              >
                            </c:when>
                            <c:when
                              test="${cancel.status == 'CANCEL_COMPLETED'}"
                            >
                              <span class="status-badge completed"
                                >취소 완료</span
                              >
                            </c:when>
                            <c:when
                              test="${cancel.status == 'REFUND_PROCESSING'}"
                            >
                              <span class="status-badge refunding"
                                >환불 처리중</span
                              >
                            </c:when>
                            <c:when
                              test="${cancel.status == 'REFUND_COMPLETED'}"
                            >
                              <span class="status-badge refunded"
                                >환불 완료</span
                              >
                            </c:when>
                          </c:choose>
                        </td>
                        <td class="cancel-date">
                          <fmt:formatDate
                            value="${cancel.createdAt}"
                            pattern="yyyy.MM.dd"
                          />
                        </td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </tbody>
            </table>

            <!-- 페이지네이션 -->
            <c:if test="${not empty pagination}">
              <div class="pagination">
                <c:if test="${pagination.hasPrev}">
                  <a
                    href="?period=${period}&status=${status}&page=${pagination.prevPage}"
                    class="page-nav"
                    >&lt;</a
                  >
                </c:if>

                <c:forEach
                  begin="${pagination.startPage}"
                  end="${pagination.endPage}"
                  var="pageNum"
                >
                  <a
                    href="?period=${period}&status=${status}&page=${pageNum}"
                    class="page-num ${pageNum == pagination.currentPage ? 'active' : ''}"
                    >${pageNum}</a
                  >
                </c:forEach>

                <c:if test="${pagination.hasNext}">
                  <a
                    href="?period=${period}&status=${status}&page=${pagination.nextPage}"
                    class="page-nav"
                    >&gt;</a
                  >
                </c:if>
              </div>
            </c:if>

            <div class="page-info">
              <c:if test="${not empty totalCount}">
                <span>총 ${totalCount}건의 취소/환불 내역</span>
              </c:if>
            </div>
          </section>

          <!-- 취소/환불 안내 -->
          <section class="info-section">
            <h3 class="info-title">취소/환불 안내</h3>
            <ul class="info-list">
              <li>
                <i class="fas fa-check"></i> 배송 전 주문 취소는 즉시 처리되며,
                결제 수단에 따라 환불 처리 기간이 상이합니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 신용카드 결제 시 취소 승인 후
                카드사 정책에 따라 환불됩니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 계좌이체/무통장입금의 경우 영업일
                기준 1-3일 내 환불계좌로 입금됩니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 배송 시작 후 취소/반품은 상품 회수
                후 환불 처리됩니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 신선식품의 특성상 단순 변심에 의한
                취소/반품은 제한될 수 있습니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 취소/환불 문의는
                고객센터(1588-1234)로 연락주시기 바랍니다.
              </li>
            </ul>
          </section>
        </div>
      </div>
    </main>

    <!-- 푸터 컨테이너 -->
    <jsp:include page="../common/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/user/mycancel.js"></script>
    <script>
      document.addEventListener("DOMContentLoaded", function () {
        // 필터링 기능
        const periodBtns = document.querySelectorAll(".period-btn");
        const cancelStatus = document.getElementById("cancelStatus");

        // 현재 설정된 필터값 반영
        const urlParams = new URLSearchParams(window.location.search);
        const currentPeriod = urlParams.get("period") || "1";
        const currentStatus = urlParams.get("status") || "all";

        // 기간 버튼 초기 상태 설정
        periodBtns.forEach((btn) => {
          if (btn.getAttribute("data-period") === currentPeriod) {
            btn.classList.add("active");
          } else {
            btn.classList.remove("active");
          }
        });

        // 상태 드롭다운 초기 상태 설정
        if (cancelStatus) {
          cancelStatus.value = currentStatus;
        }

        // 필터링 버튼 이벤트 리스너
        periodBtns.forEach((btn) => {
          btn.addEventListener("click", function () {
            periodBtns.forEach((b) => b.classList.remove("active"));
            this.classList.add("active");
            filterCancel();
          });
        });

        if (cancelStatus) {
          cancelStatus.addEventListener("change", filterCancel);
        }

        function filterCancel() {
          const period = document
            .querySelector(".period-btn.active")
            .getAttribute("data-period");
          const status = cancelStatus.value;

          location.href =
            "${pageContext.request.contextPath}/user/mycancel.jsp?period=" +
            period +
            "&status=" +
            status;
        }
      });
    </script>
  </body>
</html>
