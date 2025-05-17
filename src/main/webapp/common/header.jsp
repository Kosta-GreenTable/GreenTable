<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    

<title>그린테이블</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/reset.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/header.css" />
<link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
<header>
    <div class="header-container">
        <!-- 상단 메뉴 바 -->
        <div class="top-menu-container">
            <ul class="user-menu">
                <li><a href="#">
                    <span>회원가입</span>
                </a></li>
                <li><a href="#">
                    <span>로그인</span>
                </a></li>
                <li><a href="#">
                    <span>장바구니</span>
                </a></li>
                <li><a href="#">
                    <span>마이페이지</span>
                </a></li>
                <li><a href="#">
                    <span>고객센터</span>
                </a></li>
            </ul>
        </div>

        
        <!-- 헤더 로고 -->
        <div class="logo-container">
            <a href="index.jsp">
                <!-- <img src="https://saladpanda.co.kr/web/upload/_awesome_skin/layout/logo2.png" alt="Green Table 로고"/> -->
                <img src="${pageContext.request.contextPath}/images/logo_2.png" alt="Green Table 로고"/>
            </a>
            <h1>Green Table</h1>
        </div>
            
        <!-- 카테고리 메뉴 바 -->
        <div class="category-container">
            <nav class="category-nav">
                <ul>
                    <li class="cate"><a href="#">베스트</a></li>
                    <li class="cate"><a href="#">정기배송</a></li>
                    <li class="cate"><a href="#">도시락</a></li>
                    <li class="cate"><a href="#">샐러드</a></li>
                    <li class="cate"><a href="#">이벤트</a></li>
                    <li class="cate"><a href="#">농가소개</a></li>
                </ul>
            </nav>
            <!-- 헤더 검색 영역 -->
            <div class="header-search-container">
                <div class="search-box">
                    <input type="text" placeholder="검색어를 입력하세요" />
                    <button class="btn-search">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
            </div>
        </div>

    </div>
</header>
