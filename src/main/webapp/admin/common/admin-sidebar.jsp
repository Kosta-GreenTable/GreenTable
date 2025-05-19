<!-- filepath: c:\Users\user\git\GreenTable\src\main\webapp\admin\common\admin-sidebar.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 사이드 메뉴 -->
<aside class="admin-sidebar">
    <div class="admin-logo">
        <h2>그린테이블 관리자</h2>
    </div>
    <nav class="admin-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/index.jsp"><i class="fas fa-home"></i> 대시보드</a></li>
            <li><a href="${pageContext.request.contextPath}/front?key=admin&methodName=productList"><i class="fas fa-box"></i> 상품 관리</a></li>
            <li><a href="#"><i class="fas fa-users"></i> 회원 관리</a></li>
            <li><a href="#"><i class="fas fa-shopping-cart"></i> 주문 관리</a></li>
            <li><a href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList"><i class="fas fa-tractor"></i> 농가 관리</a></li>
            <li><a href="#"><i class="fas fa-chart-line"></i> 통계</a></li>
            <!-- 설정 메뉴 삭제 -->
        </ul>
    </nav>
</aside>