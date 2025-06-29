<%@ page contentType="text/html; charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>쿠폰 내역 | 마이페이지 | Green Table</title>
    <link rel="stylesheet" href="${path}/css/common/styles.css" />
    <link rel="stylesheet" href="${path}/css/user/mypage.css" />
    <link rel="stylesheet" href="${path}/css/user/mycoupon.css" />
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
      <h1 class="page-title">쿠폰 내역</h1>
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
              <li><a href="${path}/user/mypoint.jsp">적립금 내역</a></li>
              <li class="active">
                <a href="${path}/user/mycoupon.jsp">쿠폰 내역</a>
              </li>
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
          <!-- 쿠폰 요약 정보 -->
          <section class="coupon-summary">
            <div class="coupon-count">
              <p class="count-title">사용 가능한 쿠폰</p>
              <p class="count-number">
                ${availableCouponsCount} <span>개</span>
              </p>
            </div>
            <div class="coupon-register">
              <input
                type="text"
                class="coupon-input"
                placeholder="쿠폰 번호를 입력해주세요"
              />
              <button class="register-btn" onclick="registerCoupon()">
                쿠폰 등록
              </button>
            </div>
          </section>

          <!-- 쿠폰 탭 -->
          <section class="coupon-tabs">
            <button class="tab-btn active" data-tab="available">
              사용 가능 쿠폰 (${availableCouponsCount})
            </button>
            <button class="tab-btn" data-tab="used">
              사용 완료 쿠폰 (${usedCouponsCount})
            </button>
            <button class="tab-btn" data-tab="expired">
              기간 만료 쿠폰 (${expiredCouponsCount})
            </button>
          </section>

          <!-- 쿠폰 목록 -->
          <section class="coupon-container">
            <!-- 사용 가능 쿠폰 -->
            <div id="available-coupons" class="coupon-tab active">
              <c:choose>
                <c:when test="${not empty availableCoupons}">
                  <div class="coupon-grid">
                    <c:forEach var="coupon" items="${availableCoupons}">
                      <div class="coupon-card">
                        <div class="coupon-header">
                          <span class="coupon-type">${coupon.couponType}</span>
                          <span class="coupon-status status-available"
                            >사용 가능</span
                          >
                        </div>
                        <div class="coupon-body">
                          <h3 class="coupon-name">${coupon.couponName}</h3>
                          <p class="coupon-discount">
                            <c:choose>
                              <c:when test="${coupon.couponType eq '할인금액'}">
                                <fmt:formatNumber
                                  value="${coupon.discountValue}"
                                />원
                              </c:when>
                              <c:otherwise>
                                ${coupon.discountValue}% 할인
                              </c:otherwise>
                            </c:choose>
                          </p>
                          <ul class="coupon-info">
                            <li>
                              <span>사용 기간</span>
                              <span
                                >~<fmt:formatDate
                                  value="${coupon.validTo}"
                                  pattern="yyyy.MM.dd"
                                />까지</span
                              >
                            </li>
                            <li>
                              <span>최소 주문 금액</span>
                              <span
                                ><fmt:formatNumber
                                  value="${coupon.minOrderAmount}"
                                />원 이상</span
                              >
                            </li>
                            <li>
                              <span>적용 카테고리</span>
                              <span>${coupon.category}</span>
                            </li>
                          </ul>
                        </div>
                        <div class="coupon-footer">
                          <a href="${path}/front?key=product&methodName=list">
                            상품 보러가기 <i class="fas fa-angle-right"></i>
                          </a>
                        </div>
                      </div>
                    </c:forEach>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="no-coupons">
                    <p>사용 가능한 쿠폰이 없습니다.</p>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>

            <!-- 사용 완료 쿠폰 -->
            <div id="used-coupons" class="coupon-tab" style="display: none">
              <c:choose>
                <c:when test="${not empty usedCoupons}">
                  <div class="coupon-grid">
                    <c:forEach var="coupon" items="${usedCoupons}">
                      <div class="coupon-card">
                        <div class="coupon-header">
                          <span class="coupon-type">${coupon.couponType}</span>
                          <span class="coupon-status status-used"
                            >사용 완료</span
                          >
                        </div>
                        <div class="coupon-body">
                          <h3 class="coupon-name">${coupon.couponName}</h3>
                          <p class="coupon-discount">
                            <c:choose>
                              <c:when test="${coupon.couponType eq '할인금액'}">
                                <fmt:formatNumber
                                  value="${coupon.discountValue}"
                                />원
                              </c:when>
                              <c:otherwise>
                                ${coupon.discountValue}% 할인
                              </c:otherwise>
                            </c:choose>
                          </p>
                          <ul class="coupon-info">
                            <li>
                              <span>사용일</span>
                              <span
                                ><fmt:formatDate
                                  value="${coupon.usedAt}"
                                  pattern="yyyy.MM.dd"
                              /></span>
                            </li>
                            <li>
                              <span>최소 주문 금액</span>
                              <span
                                ><fmt:formatNumber
                                  value="${coupon.minOrderAmount}"
                                />원 이상</span
                              >
                            </li>
                          </ul>
                        </div>
                      </div>
                    </c:forEach>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="no-coupons">
                    <p>사용 완료된 쿠폰이 없습니다.</p>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>

            <!-- 기간 만료 쿠폰 -->
            <div id="expired-coupons" class="coupon-tab" style="display: none">
              <c:choose>
                <c:when test="${not empty expiredCoupons}">
                  <div class="coupon-grid">
                    <c:forEach var="coupon" items="${expiredCoupons}">
                      <div class="coupon-card">
                        <div class="coupon-header">
                          <span class="coupon-type">${coupon.couponType}</span>
                          <span class="coupon-status status-expired"
                            >기간 만료</span
                          >
                        </div>
                        <div class="coupon-body">
                          <h3 class="coupon-name">${coupon.couponName}</h3>
                          <p class="coupon-discount">
                            <c:choose>
                              <c:when test="${coupon.couponType eq '할인금액'}">
                                <fmt:formatNumber
                                  value="${coupon.discountValue}"
                                />원
                              </c:when>
                              <c:otherwise>
                                ${coupon.discountValue}% 할인
                              </c:otherwise>
                            </c:choose>
                          </p>
                          <ul class="coupon-info">
                            <li>
                              <span>만료일</span>
                              <span
                                ><fmt:formatDate
                                  value="${coupon.validTo}"
                                  pattern="yyyy.MM.dd"
                              /></span>
                            </li>
                            <li>
                              <span>최소 주문 금액</span>
                              <span
                                ><fmt:formatNumber
                                  value="${coupon.minOrderAmount}"
                                />원 이상</span
                              >
                            </li>
                          </ul>
                        </div>
                      </div>
                    </c:forEach>
                  </div>
                </c:when>
                <c:otherwise>
                  <div class="no-coupons">
                    <p>기간 만료된 쿠폰이 없습니다.</p>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
          </section>

          <!-- 쿠폰 안내 -->
          <section class="coupon-info-section">
            <div class="section-header">
              <h3>쿠폰 이용 안내</h3>
            </div>
            <div class="info-box">
              <p>• 회원 가입 시 신규 가입 쿠폰이 지급됩니다.</p>
              <p>• 쿠폰은 주문 시 적용 가능합니다. (일부 상품 제외)</p>
              <p>• 쿠폰은 최소 주문 금액과 적용 카테고리를 확인해주세요.</p>
              <p>• 쿠폰은 발급 받으신 후 사용 기간 내 사용 가능합니다.</p>
              <p>• 일부 쿠폰은 중복 사용이 불가할 수 있습니다.</p>
              <p>
                • 쿠폰 사용 후 주문 취소 시 쿠폰은 반환되지 않을 수 있습니다.
              </p>
              <p>• 쿠폰 코드 등록 시 대소문자를 구분하여 입력해주세요.</p>
              <p>• 자세한 내용은 고객센터로 문의해주세요.</p>
            </div>
          </section>
        </div>
      </div>
    </main>

    <jsp:include page="/common/footer.jsp" />

    <script>
      function registerCoupon() {
        const couponCode = document.querySelector(".coupon-input").value.trim();
        if (!couponCode) {
          alert("쿠폰 번호를 입력해주세요.");
          return;
        }

        // TODO: 쿠폰 등록 API 호출
        alert("쿠폰 등록 기능은 개발 중입니다.");
      }

      // 탭 전환 기능
      document.querySelectorAll(".tab-btn").forEach((btn) => {
        btn.addEventListener("click", function () {
          const tab = this.dataset.tab;

          // 탭 버튼 활성화
          document
            .querySelectorAll(".tab-btn")
            .forEach((b) => b.classList.remove("active"));
          this.classList.add("active");

          // 탭 콘텐츠 전환
          document
            .querySelectorAll(".coupon-tab")
            .forEach((t) => (t.style.display = "none"));
          document.getElementById(tab + "-coupons").style.display = "block";
        });
      });
    </script>
  </body>
</html>
