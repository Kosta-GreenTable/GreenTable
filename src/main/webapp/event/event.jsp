<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Green Table - 이벤트</title>
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>" />
    <link rel="stylesheet" href="<c:url value='/css/menu-fix.css'/>" />
    <link rel="stylesheet" href="<c:url value='/event/event.css'/>" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <!-- 헤더 포함 -->
    <jsp:include page="../common/header.jsp" />
    
    <!-- 이벤트 페이지 컨테이너 -->
    <div class="event-container">
      <div class="breadcrumb">
        <ul>
          <li><a href="<c:url value='/'/>">홈</a></li>
          <li class="active">이벤트</li>
        </ul>
      </div>
      <div class="event-header">
        <h1 class="event-title">이벤트</h1>
        <p class="event-subtitle">그린 테이블의 다양한 혜택을 만나보세요!</p>
        <p class="event-date" id="currentDate"></p>
      </div>
      <div class="event-filter-container">
        <div class="event-tabs">
          <button class="tab active" data-tab="ongoing">진행중인 이벤트</button>
          <button class="tab" data-tab="ended">종료된 이벤트</button>
        </div>

        <div class="event-search">
          <input
            type="text"
            id="eventSearchInput"
            placeholder="이벤트 검색..."
          />
          <button id="eventSearchButton"><i class="fas fa-search"></i></button>
        </div>
      </div>

      <div id="eventGrid" class="event-grid">
        <!-- 이벤트 카드가 여기에 동적으로 로드됩니다 -->
      </div>

      <div id="pagination" class="pagination">
        <!-- 페이지네이션이 여기에 동적으로 로드됩니다 -->
      </div>
    </div>

    <!-- 푸터 포함 -->
    <jsp:include page="../common/footer.jsp" />
    
    <!-- 자바스크립트 -->
    <script src="<c:url value='/event/event.js'/>"></script>
  </body>
</html>