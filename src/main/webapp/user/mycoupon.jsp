<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>쿠폰 내역 | 마이페이지 | Green Table</title>
    
    <link rel="stylesheet" href="${path }/css/common/styles.css" />
    <link rel="stylesheet" href="${path }/css/user/mypage.css" />
    <link rel="stylesheet" href="${path }/css/user/mycoupon.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
  </head>
  <body>
    <!-- 헤더 컨테이너 -->
    <jsp:include page="/common/header.jsp" />
    <div>
    <script src="${path }/js/user/script.js"></script>
    <script src="${path }/js/user/mypage.js"></script>
    </div>
      
    
    <!-- 메인 컨텐츠 - 쿠폰 내역 섹션 -->
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
              <p class="user-name">${sessionScope.loginUser.userInfoDto.userName}님</p>
              
            </div>
          </div>

          <nav class="sidebar-menu">
            <h3>나의 쇼핑정보</h3>
            <ul>
              <li><a href="mypage.jsp">주문/배송 조회</a></li>
              <li><a href="mycancel.jsp">취소/반품 내역</a></li>
              <li><a href="mypoint.jsp">적립금 내역</a></li>
              <li class="active"><a href="mycoupon.jsp">쿠폰 내역</a></li>
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
          <!-- 쿠폰 요약 정보 -->
          <section class="coupon-summary">
            <div class="coupon-count">
              <p class="count-title">사용 가능한 쿠폰</p>
              <p class="count-number">2 <span>개</span></p>
            </div>
            <div class="coupon-register">
              <input type="text" class="coupon-input" placeholder="쿠폰 번호를 입력해주세요" />
              <button class="register-btn">쿠폰 등록</button>
            </div>
          </section>
          <!-- 쿠폰 탭 -->
          <section class="coupon-tabs">
            <button class="tab-btn active" data-tab="available">사용 가능 쿠폰 (2)</button>
            <button class="tab-btn" data-tab="used">사용 완료 쿠폰 (0)</button>
            <button class="tab-btn" data-tab="expired">기간 만료 쿠폰 (0)</button>
          </section>
          <!-- 쿠폰 목록 -->
          <section class="coupon-container">
            <!-- 사용 가능 쿠폰 -->
            <div id="available-coupons" class="coupon-tab active">
              <div class="coupon-grid">
                <div class="coupon-card">
                  <div class="coupon-header">
                    <span class="coupon-type">신규 가입 쿠폰</span>
                    <span class="coupon-status status-available">사용 가능</span>
                  </div>
                  <div class="coupon-body">
                    <h3 class="coupon-name">신규 회원 가입 축하 쿠폰</h3>
                    <p class="coupon-discount">10,000원</p>
                    <ul class="coupon-info">
                      <li><span>사용 기간</span><span>~2025.06.08까지</span></li>
                      <li><span>최소 주문 금액</span><span>50,000원 이상</span></li>
                      <li><span>적용 카테고리</span><span>전체 상품</span></li>
                    </ul>
                  </div>
                  <div class="coupon-footer">
                    <a href="#">상품 보러가기 <i class="fas fa-angle-right"></i></a>
                  </div>
                </div>
                <div class="coupon-card">
                  <div class="coupon-header">
                    <span class="coupon-type">이벤트 쿠폰</span>
                    <span class="coupon-status status-available">사용 가능</span>
                  </div>
                  <div class="coupon-body">
                    <h3 class="coupon-name">봄맞이 특별 할인 쿠폰</h3>
                    <p class="coupon-discount">15% 할인</p>
                    <ul class="coupon-info">
                      <li><span>사용 기간</span><span>~2025.05.31까지</span></li>
                      <li><span>최소 주문 금액</span><span>30,000원 이상</span></li>
                      <li><span>적용 카테고리</span><span>샐러드, 도시락</span></li>
                    </ul>
                  </div>
                  <div class="coupon-footer">
                    <a href="#">상품 보러가기 <i class="fas fa-angle-right"></i></a>
                  </div>
                </div>
              </div>
            </div>
            <!-- 사용 완료 쿠폰 탭 -->
            <div id="used-coupons" class="coupon-tab" style="display: none">
              <div class="coupon-grid">
                <div class="coupon-card">
                  <div class="coupon-header">
                    <span class="coupon-type">생일 쿠폰</span>
                    <span class="coupon-status status-used">사용 완료</span>
                  </div>
                  <div class="coupon-body">
                    <h3 class="coupon-name">생일 축하 쿠폰</h3>
                    <p class="coupon-discount">5,000원</p>
                    <ul class="coupon-info">
                      <li><span>사용 기간</span><span>~2025.04.30까지</span></li>
                      <li><span>최소 주문 금액</span><span>20,000원 이상</span></li>
                      <li><span>적용 카테고리</span><span>전체 상품</span></li>
                    </ul>
                  </div>
                  <div class="coupon-footer">
                    <a href="#">상품 보러가기 <i class="fas fa-angle-right"></i></a>
                  </div>
                </div>
              </div>
            </div>
            <!-- 기간 만료 쿠폰 탭 -->
            <div id="expired-coupons" class="coupon-tab" style="display: none">
              <div class="coupon-grid">
                <div class="coupon-card">
                  <div class="coupon-header">
                    <span class="coupon-type">이벤트 쿠폰</span>
                    <span class="coupon-status status-expired">기간 만료</span>
                  </div>
                  <div class="coupon-body">
                    <h3 class="coupon-name">여름맞이 할인 쿠폰</h3>
                    <p class="coupon-discount">10% 할인</p>
                    <ul class="coupon-info">
                      <li><span>사용 기간</span><span>~2025.03.31까지</span></li>
                      <li><span>최소 주문 금액</span><span>10,000원 이상</span></li>
                      <li><span>적용 카테고리</span><span>샐러드</span></li>
                    </ul>
                  </div>
                  <div class="coupon-footer">
                    <a href="#">상품 보러가기 <i class="fas fa-angle-right"></i></a>
                  </div>
                </div>
              </div>
            </div>
          </section>
          <!-- 쿠폰 안내 -->
          <section class="coupon-info-section">
            <div class="section-header">
              <h3>쿠폰 이용 안내</h3>
            </div>
            <div class="info-box">
              <p>• 쿠폰은 주문 시 적용 가능합니다. (일부 상품 제외)</p>
              <p>• 쿠폰은 최소 주문 금액과 적용 카테고리를 확인해주세요.</p>
              <p>• 쿠폰은 발급 받으신 후 사용 기간 내 사용 가능합니다.</p>
              <p>• 일부 쿠폰은 중복 사용이 불가할 수 있습니다.</p>
              <p>• 쿠폰 사용 후 주문 취소 시 쿠폰은 반환되지 않을 수 있습니다.</p>
              <p>• 쿠폰 코드 등록 시 대소문자를 구분하여 입력해주세요.</p>
              <p>• 자세한 내용은 고객센터로 문의해주세요.</p>
            </div>
          </section>
        </div>
      </div>
    </main>
    <!-- 푸터 컨테이너 -->
    <jsp:include page="/common/footer.jsp" />
    <div>
      <!-- ...existing code... -->
    </div>
   
  </body>
</html>