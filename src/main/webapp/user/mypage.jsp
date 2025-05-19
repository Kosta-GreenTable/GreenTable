<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>마이페이지 | Green Table</title>

<link rel="stylesheet" href="${path}/css/common/styles.css" />
<link rel="stylesheet" href="${path}/css/user/mypage.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
</head>
<body>
	<jsp:include page="/common/header.jsp" />

	<main class="mypage-container">
		<h1 class="page-title">마이페이지</h1>

		<div class="mypage-content">
			<!-- 사이드바 메뉴 -->
			<div class="mypage-sidebar">
				<div class="user-profile">
					<div class="profile-image">
						<i class="fas fa-user-circle"></i>
					</div>
					<div class="user-info">
						<p class="user-name">
							<c:out value="${sessionScope.loginUser.userInfoDto.userName}" />
							님
						</p>



						<button class="profile-edit-btn">회원정보수정</button>
					</div>
				</div>

				<nav class="sidebar-menu">
					<h3>나의 쇼핑정보</h3>
					<ul>
						<li class="active"><a href="mypage.jsp">주문/배송 조회</a></li>
						<li><a href="mycancel.jsp">취소/환불 내역</a></li>
						<li><a href="mypoint.jsp">적립금 내역</a></li>
						<li><a href="mycoupon.jsp">쿠폰 내역</a></li>
						<li><a href="myreview.jsp">상품 리뷰</a></li>
						<li><a href="myqna.jsp">상품 문의</a></li>
					</ul>

					<h3>나의 계정설정</h3>
					<ul>
						<li><a href="${path}/user/myinfo.jsp">회원정보 수정</a></li>
					</ul>
				</nav>
			</div>

			<!-- 메인 콘텐츠 -->
			<div class="mypage-main">
				<section class="member-info">
					<div class="grade-info">
						<div class="grade-title">
							<h3>
								<c:out value="${sessionScope.loginUser.userInfoDto.userName}" />
								님
							</h3>
						
						</div>
						<div class="grade-detail">
							<p>회원등급 : </p>
							<p class="grade-name">
								<c:out value="${sessionScope.loginUser.userInfoDto.userGrade}" />
							</p>
							<span class="grade-value">포인트: <c:out
									value="${sessionScope.loginUser.userInfoDto.point}" />원
							</span> <span class="grade-count">쿠폰 개수: 6</span>
						</div>
					</div>

					<div class="member-status">
						<div class="status-item">
							<h4>쇼핑정보</h4>
							<ul>
								<li>적립금/혜택<span>0</span></li>
								<li>쿠폰<span>0</span></li>
								<li>결제내역 조회<span>0</span></li>
								<li>내 리뷰/문의<span>0</span></li>
							</ul>
						</div>
						<div class="status-item">
							<h4>주문/배송조회</h4>
							<ul>
								<li>배송준비중<span>0</span></li>
								<li>배송중<span>0</span></li>
								<li>배송완료<span>0</span></li>
							</ul>
						</div>
						<div class="status-item">
							<h4>취소/환불 내역</h4>
							<ul>
								<li>취소<span>0</span></li>
								<li>환불<span>0</span></li>
							</ul>
						</div>
					</div>
				</section>

				<section class="recent-orders">
					<div class="section-header">
						<h3>최근 주문내역</h3>
						<p class="sub-text">최근 내역이 없습니다.</p>
					</div>

					<div class="order-list">
						<div class="no-orders">
							<p>최근 6개월간 주문 내역이 없습니다.</p>
						</div>
					</div>

					<div class="view-more">
						<a href="#" class="btn-view-more">더보기 <i
							class="fas fa-angle-right"></i></a>
					</div>
				</section>

				<section class="recent-viewed">
					<div class="section-header">
						<h3>최근 본 상품</h3>
					</div>
					<div class="product-list">
						<div class="no-products">
							<p>최근 본 상품이 없습니다.</p>
						</div>
					</div>
				</section>
			</div>
		</div>
	</main>

	<jsp:include page="/common/footer.jsp" />
	<script src="../js/include.js"></script>
	<script src="../js/script.js"></script>
	<script src="../js/mypage.js"></script>
</body>
</html>
