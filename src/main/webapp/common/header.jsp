<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>

<%site.greentable.dto.UserDTO loginUser = (site.greentable.dto.UserDTO) session.getAttribute("loginUser");
            String email = null;
            if (loginUser != null) {
                email = loginUser.getEmail();
            } %>
            
<title>그린테이블</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/header.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
<header>
	<div class="header-container">
		<!-- 상단 메뉴 바 -->
		<div class="top-menu-container">
			<ul class="user-menu">
				<% if (email != null) { %>
				<li><span><%= email %>님 환영합니다!</span></li>
				<li><a
					href="${pageContext.request.contextPath}/front?key=user&methodName=logout">로그아웃</a></li>
				<% } else { %>
				<li><a
					href="${pageContext.request.contextPath}/front?key=user&methodName=register">회원가입</a></li>
				<li><a
					href="${pageContext.request.contextPath}/front?key=user&methodName=login">로그인</a></li>
				<% } %>

				<!-- 아래 메뉴는 로그인 여부와 상관없이 항상 보임 -->

				<li><a href="${pageContext.request.contextPath}/order/cart.jsp">장바구니</a></li>
				<li><a
					href="${pageContext.request.contextPath}/front?key=mypage&methodName=mypage">마이페이지</a></li>
				<li><a
					href="${pageContext.request.contextPath}/front?key=cs&methodName=main">고객센터</a></li>
				<li><a
					href="${pageContext.request.contextPath}/admin/index.jsp"
					class="admin-link">관리자</a></li>
			</ul>
		</div>


		<!-- 헤더 로고 -->
		<div class="logo-container">
			<a href="${pageContext.request.contextPath}/index.jsp"> <img
				src="https://picsum.photos/100/40" alt="Green Table 로고" />
			</a>
			<h1>Green Table</h1>
		</div>

		<!-- 카테고리 메뉴 바 -->
		<section class="category-section">
			<div class="category-container">
				<nav class="category-nav">
					<ul>
						<li class="cate"><a
							href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=best">베스트</a>
						</li>
						<li class="cate"><a
							href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=regular">정기배송</a>
						</li>
						<li class="cate"><a
							href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=lunchbox">도시락</a>
						</li>
						<li class="cate"><a
							href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=salad">샐러드</a>
						</li>
						<li class="cate"><a
							href="${pageContext.request.contextPath}/front?key=event&methodName=list">이벤트</a>
						</li>
						<li class="cate"><a
							href="${pageContext.request.contextPath}">농가소개</a>
						</li>
					</ul>
				</nav>
				<!-- 헤더 검색 영역 -->
				<div class="search-container">
					<div class="search-box">
						<input type="text" id="search-input" placeholder="검색어를 입력하세요" />
						<button class="search-btn" id="search-button">
							<i class="fas fa-search"></i>
						</button>
					</div>
				</div>
			</div>
		</section>
	</div>
</header>