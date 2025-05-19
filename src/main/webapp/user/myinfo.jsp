<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.loginUser}" />


<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>회원정보 수정 | Green Table</title>
  <link rel="stylesheet" href="${path}/css/common/styles.css" />
  <link rel="stylesheet" href="${path}/css/user/mypage.css" />
  <link rel="stylesheet" href="${path}/css/user/myinfo.css" />
  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"/>
</head>
<body>
	<jsp:include page="/common/header.jsp" />

  <!-- 메인 컨텐츠 -->
  <main class="mypage-container">
    <h1 class="page-title">회원정보 수정</h1>

    <div class="mypage-content">
      <!-- 사이드바 -->
      <div class="mypage-sidebar">
        <div class="user-profile">
          <div class="profile-image">
            <i class="fas fa-user-circle"></i>
          </div>
          <div class="user-info">
            <p class="user-name">${user.name}님</p>
            <button class="profile-edit-btn">회원정보수정</button>
          </div>
        </div>
        <nav class="sidebar-menu">
          <h3>나의 쇼핑정보</h3>
          <ul>
            <li><a href="mypage.jsp">주문/배송 조회</a></li>
            <li><a href="mycancel.jsp">취소/반품 내역</a></li>
            <li><a href="mypoint.jsp">적립금 내역</a></li>
            <li><a href="mycoupon.jsp">쿠폰 내역</a></li>
            <li><a href="myreview.jsp">상품 리뷰</a></li>
            <li><a href="myqna.jsp">상품 문의</a></li>
          </ul>

          <h3>나의 계정설정</h3>
          <ul>
            <li class="active"><a href="myinfo.jsp">회원정보 수정</a></li>
          </ul>
        </nav>
      </div>
      
<%-- 또는 --%>
<c:if test="${empty sessionScope.loginUser}">
  <p>세션에 로그인 정보가 없습니다.</p>
</c:if>

      <!-- 회원정보 수정 메인 폼 -->
      <div class="mypage-main">
        <section class="member-info-section">
          <div class="section-header">
            <h3>회원정보 수정</h3>
            <p>연락처 등의 정보를 정확하게 입력해주세요.</p>
          </div>
          <form id="memberInfoForm" class="member-info-form" action="updateUserInfo.do" method="post">
            <!-- 기본 정보 -->
            <div class="form-section">
              <h4 class="form-section-title">기본 정보</h4>
              <div class="form-group">
                <label for="userId">이메일</label>
                <div class="input-with-text">
                  <input type="text" id="userId" name="userId" value="${user.email}" readonly />
                  <span class="input-guide">이메일은 변경이 불가능합니다.</span>
                </div>
              </div>
              <div class="form-group">
                <label for="userName">이름</label>
                <input type="text" id="userName" name="userName" value="${user.userInfoDto.userName}" readonly />
              </div>
              <div class="form-group">
                <label for="userPassword">비밀번호</label>
                <input type="password" id="userPassword" name="password" placeholder="변경할 비밀번호 입력" />
              </div>
              <div class="form-group">
                <label for="userPasswordConfirm">비밀번호 확인</label>
                <input type="password" id="userPasswordConfirm" name="passwordConfirm" />
              </div>
              <div class="form-group">
                <label for="userPhone">휴대폰 번호</label>
                <input type="tel" id="userPhone" name="phone" value="${user.phone}" required />
              </div>
            </div>

            <!-- 배송지 정보 -->
            <div class="form-section">
              <h4 class="form-section-title">배송지 정보</h4>
              <div class="form-group">
                <label for="postalCode">우편번호</label>
                <input type="text" id="zipCode" name="zipCode" placeholder="우편번호" readonly required />
                <button type="button" class="find-address-btn">주소찾기</button>
              </div>
              <div class="form-group">
                <label for="address1">기본 주소</label>
                <input type="text" id="address1" name="address1" placeholder="기본주소" readonly required />
              </div>
              <div class="form-group">
                <label for="address2">상세 주소</label>
                <input type="text" id="address2" name="address2" placeholder="상세주소를 입력하세요" required />
              </div>
            </div>

            <!-- 마케팅 수신 동의 -->
            <div class="form-section">
              <h4 class="form-section-title">마케팅 정보 수신 동의</h4>
              <div class="checkbox-group">
                <label><input type="checkbox" name="marketingEmail" ${user.marketingEmail ? "checked" : ""} /> 이메일 수신 동의</label>
                <label><input type="checkbox" name="marketingSMS" ${user.marketingSMS ? "checked" : ""} /> SMS 수신 동의</label>
              </div>
            </div>

            <!-- 버튼 -->
            <div class="form-actions">
              <div class="action-left">
                <button type="button" class="btn-withdraw">회원 탈퇴</button>
              </div>
              <div class="action-right">
                <button type="reset" class="btn-cancel">취소</button>
                <button type="submit" class="btn-save">저장</button>
              </div>
            </div>
          </form>
        </section>
      </div>
    </div>
  </main>
</body>
</html>
