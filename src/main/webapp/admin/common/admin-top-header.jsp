<!-- filepath: c:\Users\user\git\GreenTable\src\main\webapp\admin\common\admin-top-header.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 상단 헤더 -->
<div class="admin-top-header">
  <div class="page-title">
    <h1>${param.pageTitle}</h1>
  </div>
  <div class="admin-user-actions">
    <a href="${pageContext.request.contextPath}/" class="btn-secondary">
      <i class="fas fa-home"></i> 사이트로 이동
    </a>
    <button
      class="btn-danger"
      onclick="location.href='${pageContext.request.contextPath}/front?key=user&methodName=logout'"
    >
      <i class="fas fa-sign-out-alt"></i> 로그아웃
    </button>
  </div>
</div>
