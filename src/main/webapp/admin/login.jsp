<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>그린테이블 관리자 로그인</title>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/admin/css/admin-login.css"
    />
  </head>
  <body>
    <div class="login-container">
      <div class="login-header">
        <h1><i class="fas fa-shield-alt"></i> 관리자 로그인</h1>
        <p>그린테이블 관리자 시스템</p>
      </div>

      <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
      </c:if>

      <form action="${pageContext.request.contextPath}/front" method="post">
        <input type="hidden" name="key" value="admin" />
        <input type="hidden" name="methodName" value="login" />

        <div class="form-group">
          <label for="email">이메일</label>
          <input
            type="email"
            id="email"
            name="email"
            required
            placeholder="관리자 이메일을 입력하세요"
          />
        </div>

        <div class="form-group">
          <label for="password">비밀번호</label>
          <input
            type="password"
            id="password"
            name="password"
            required
            placeholder="비밀번호를 입력하세요"
          />
        </div>

        <button type="submit" class="login-btn">
          <i class="fas fa-sign-in-alt"></i> 로그인
        </button>
      </form>

      <div class="back-link">
        <a href="${pageContext.request.contextPath}/index.jsp">
          <i class="fas fa-arrow-left"></i> 메인 페이지로 돌아가기
        </a>
      </div>
    </div>
  </body>
</html>
