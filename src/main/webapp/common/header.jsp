<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 상단 메뉴 바 -->
<div class="top-menu-bar">
  <div class="user-menu">
    <a
      href="${pageContext.request.contextPath}/front?key=user&methodName=registerForm"
      >회원가입</a
    >
    <a
      href="${pageContext.request.contextPath}/front?key=user&methodName=loginForm"
      >로그인</a
    >
    <a
      href="${pageContext.request.contextPath}/front?key=cart&methodName=viewCart"
      >장바구니</a
    >
    <a
      href="${pageContext.request.contextPath}/front?key=mypage&methodName=main"
      >마이페이지</a
    >
    <a href="${pageContext.request.contextPath}/front?key=cs&methodName=main"
      >고객센터</a
    >
    <a
      href="${pageContext.request.contextPath}/admin/index.jsp"
      class="admin-link"
      >관리자</a
    >
  </div>
</div>

<style>
  .admin-link {
    background-color: #007bff;
    color: white !important;
    padding: 2px 8px;
    border-radius: 4px;
  }
  .admin-link:hover {
    background-color: #0056b3;
  }
</style>

<!-- 상단 헤더 섹션 -->
<header>
  <div class="top-header">
    <div class="logo-container">
      <a href="${pageContext.request.contextPath}/index.jsp">
        <img src="https://picsum.photos/100/40" alt="Green Table 로고" />
      </a>
      <h1>Green Table</h1>
    </div>
  </div>
</header>

<!-- 카테고리 섹션 -->
<section class="category-section">
  <div class="category-container">
    <div class="category-left">
      <!-- 기본 메인 네비게이션 -->
      <nav class="main-nav">
        <ul>
          <li>
            <a
              href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=best"
              >베스트</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=regular"
              >정기배송</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=lunchbox"
              >도시락</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=salad"
              >샐러드</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/front?key=event&methodName=list"
              >이벤트</a
            >
          </li>
          <li>
            <a
              href="${pageContext.request.contextPath}/front?key=farm&methodName=list"
              >농가 소개</a
            >
          </li>
        </ul>
      </nav>
    </div>
    <div class="category-right">
      <div class="search-box">
        <input
          type="text"
          id="search-input"
          placeholder="검색어를 입력하세요"
        />
        <button id="search-button"><i class="fas fa-search"></i></button>
      </div>
    </div>
  </div>
</section>
