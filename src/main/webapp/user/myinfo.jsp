<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
<script
	src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="${path }/js/user/register.js"></script>
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
						<p class="user-name">${sessionScope.loginUser.userInfoDto.userName}님</p>
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
					<form id="memberInfoForm" class="member-info-form"
						action="${pageContext.request.contextPath}/front" method="post">
						<input type="hidden" name="key" value="ajaxUser"> <input
							type="hidden" name="methodName" value="updateUser">
						<!-- 기본 정보 -->
						<div class="form-section">
							<h4 class="form-section-title">기본 정보</h4>
							<div class="form-group">
								<label for="userId">이메일</label>
								<div class="input-with-text">
									<input type="text" id="userId" name="userId"
										value="${user.email}" readonly /> <span class="input-guide">이메일은
										변경이 불가능합니다.</span>
								</div>
							</div>
							<div class="form-group">
								<label for="userName">이름</label> <input type="text"
									id="userName" name="userName"
									value="${user.userInfoDto.userName}" readonly />
							</div>
							<div class="form-group">
								<label for="userPassword">비밀번호</label> <input type="password"
									id="userPassword" name="password" placeholder="변경할 비밀번호 입력" />
							</div>
							<div class="form-group">
								<label for="userPasswordConfirm">비밀번호 확인</label> <input
									type="password" id="userPasswordConfirm" name="passwordConfirm"
									placeholder="변경할 비밀번호 입력 확인" />
							</div>
							<div class="form-group">
								<label for="mobile">휴대전화<span class="required">*</span></label>
								<div class="phone-group">
									<select id="mobile-first" required>
										<option value="010">010</option>
										<option value="011">011</option>
										<option value="016">016</option>
										<option value="017">017</option>
										<option value="018">018</option>
										<option value="019">019</option>
									</select> <span class="phone-dash">-</span> <input type="text"
										id="mobile-middle" maxlength="4" placeholder="XXXX" required />
									<span class="phone-dash">-</span> <input type="text"
										id="mobile-last" maxlength="4" placeholder="XXXX" required />
								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="address">주소<span class="required">*</span></label>
							<div class="address-group">
								<div class="input-with-button">
									<input type="text" id="zipCode" name="zipCode"
										placeholder="우편번호" readonly required />
									<button type="button" class="find-address-btn">주소찾기</button>
								</div>
								<input type="text" id="address1" name="address1"
									placeholder="기본주소" readonly required /> <input type="text"
									id="address2" name="address2" placeholder="상세주소를 입력하세요"
									required />
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
