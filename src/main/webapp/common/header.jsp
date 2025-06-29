<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c"%> <%@ page session="true"%> <%site.greentable.dto.UserDTO loginUser =
(site.greentable.dto.UserDTO) session.getAttribute("loginUser"); String email =
null; if (loginUser != null) { email = loginUser.getEmail(); } %>

<script>
  window.contextPath = "<%= request.getContextPath() %>";
</script>

<title>그린테이블</title>
<link
  rel="stylesheet"
  href="${pageContext.request.contextPath}/css/common/header.css"
/>
<link
  rel="stylesheet"
  href="${pageContext.request.contextPath}/css/search.css"
/>
<link
  rel="stylesheet"
  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
/>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<header>
  <div class="header-container">
    <!-- 상단 메뉴 바 -->
    <div class="top-menu-container">
      <ul class="user-menu">
        <% if (email != null) { %>
        <li><span><%= email %>님 환영합니다!</span></li>
        <li>
          <a
            href="${pageContext.request.contextPath}/front?key=user&methodName=logout"
            >로그아웃</a
          >
        </li>
        <% } else { %>
        <li>
          <a href="${pageContext.request.contextPath}/user/terms.jsp"
            >회원가입</a
          >
        </li>
        <li>
          <a
            href="${pageContext.request.contextPath}/front?key=user&methodName=login"
            >로그인</a
          >
        </li>
        <% } %>

        <!-- 아래 메뉴는 로그인 여부와 상관없이 항상 보임 -->

        <li>
          <a
            href="${pageContext.request.contextPath}/front?key=cart&methodName=selectCartByUserId"
            >장바구니</a
          >
        </li>
        <li>
          <a
            href="${pageContext.request.contextPath}/front?key=mypage&methodName=mypage"
            >마이페이지</a
          >
        </li>
        <li>
          <a
            href="${pageContext.request.contextPath}/front?key=cs&methodName=main"
            >고객센터</a
          >
        </li>
      </ul>
    </div>

    <!-- 헤더 로고 -->
    <div class="logo-container">
      <a href="${pageContext.request.contextPath}/index.jsp">
        <img
          id="logo"
          src="${pageContext.request.contextPath}/image/logo_3.png"
          alt="Green Table 로고"
        />
      </a>
      <!-- <h1>Green Table</h1> -->
    </div>

    <!-- 카테고리 메뉴 바 -->
    <section class="category-section">
      <div class="category-container">
        <!-- 모바일 메뉴 토글 버튼 -->
        <button class="mobile-menu-toggle" id="mobile-menu-toggle">
          <span></span>
          <span></span>
          <span></span>
        </button>

        <nav class="category-nav" id="category-nav">
          <ul>
            <li class="cate">
              <a
                href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=best"
                >베스트</a
              >
            </li>
            <li class="cate">
              <a
                href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=regular"
                >정기배송</a
              >
            </li>
            <li class="cate">
              <a
                href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=lunchbox"
                >도시락</a
              >
            </li>
            <li class="cate">
              <a
                href="${pageContext.request.contextPath}/front?key=product&methodName=category&category=salad"
                >샐러드</a
              >
            </li>
            <li class="cate">
              <a
                href="${pageContext.request.contextPath}/front?key=event&methodName=list"
                >이벤트</a
              >
            </li>
            <li class="cate">
              <a
                href="${pageContext.request.contextPath}/front?key=farm&methodName=list"
                >농가 소개</a
              >
            </li>
          </ul>
        </nav>
        <!-- 헤더 검색 영역 -->
        <div class="search-container">
          <div class="search-box">
            <input
              type="text"
              id="search-input"
              placeholder="검색어를 입력하세요"
            />
            <button class="search-btn" id="search-button">
              <i class="fas fa-search"></i>
            </button>
          </div>
        </div>
      </div>
    </section>
  </div>
</header>
<script>
  document.addEventListener("DOMContentLoaded", function () {
    const cat = document.querySelector(".category-section");
    const origOffset = cat.getBoundingClientRect().top + window.pageYOffset;
    // placeholder로 레이아웃 밀림 방지
    const placeholder = document.createElement("div");
    placeholder.style.height = cat.offsetHeight + "px";

    window.addEventListener("scroll", function () {
      if (window.pageYOffset > origOffset) {
        if (!cat.classList.contains("sticky")) {
          cat.classList.add("sticky");
          cat.parentNode.insertBefore(placeholder, cat.nextSibling);
        }
      } else {
        if (cat.classList.contains("sticky")) {
          cat.classList.remove("sticky");
          placeholder.remove();
        }
      }
    });
  });

  // 모바일 메뉴 토글 기능
  document.addEventListener("DOMContentLoaded", function () {
    const mobileMenuToggle = document.getElementById("mobile-menu-toggle");
    const categoryNav = document.getElementById("category-nav");

    if (mobileMenuToggle && categoryNav) {
      mobileMenuToggle.addEventListener("click", function () {
        categoryNav.classList.toggle("active");
        document.body.classList.toggle("menu-open");

        // 토글 버튼 애니메이션
        this.classList.toggle("active");
      });

      // 메뉴 아이템 클릭 시 메뉴 닫기
      const menuItems = categoryNav.querySelectorAll("a");
      menuItems.forEach((item) => {
        item.addEventListener("click", function () {
          categoryNav.classList.remove("active");
          document.body.classList.remove("menu-open");
          mobileMenuToggle.classList.remove("active");
        });
      });
    }
  });
</script>
<script src="${pageContext.request.contextPath}/js/search.js"></script>
