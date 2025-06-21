<!-- filepath: c:\Users\user\git\GreenTable\src\main\webapp\admin\common\admin-sidebar.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 사이드 메뉴 -->
<aside class="admin-sidebar">
  <div class="admin-logo">
    <h2>그린테이블 관리자</h2>
  </div>
  <nav class="admin-nav">
    <ul>
      <li class="${param.currentPage == 'dashboard' ? 'active' : ''}">
        <a href="${pageContext.request.contextPath}/admin/index.jsp"
          ><i class="fas fa-home"></i> <span>대시보드</span></a
        >
      </li>
      <li class="${param.currentPage == 'products' ? 'active' : ''}">
        <a
          href="${pageContext.request.contextPath}/front?key=admin&methodName=productList"
          ><i class="fas fa-box"></i> <span>상품 관리</span></a
        >
      </li>
      <li class="${param.currentPage == 'users' ? 'active' : ''}">
        <a
          href="${pageContext.request.contextPath}/front?key=admin&methodName=userList"
          ><i class="fas fa-users"></i> <span>회원 관리</span></a
        >
      </li>
      <li class="${param.currentPage == 'orders' ? 'active' : ''}">
        <a
          href="${pageContext.request.contextPath}/front?key=admin&methodName=orderList"
          ><i class="fas fa-shopping-cart"></i> <span>주문 관리</span></a
        >
      </li>
      <li class="${param.currentPage == 'farms' ? 'active' : ''}">
        <a
          href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList"
          ><i class="fas fa-tractor"></i> <span>농가 관리</span></a
        >
      </li>
      <li class="${param.currentPage == 'statistics' ? 'active' : ''}">
        <a
          href="${pageContext.request.contextPath}/front?key=admin&methodName=statistics"
          ><i class="fas fa-chart-line"></i> <span>통계</span></a
        >
      </li>
    </ul>
  </nav>
</aside>
