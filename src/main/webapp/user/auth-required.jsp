<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>그린테이블 - 접근 권한 필요</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/common/styles.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/user/auth-error.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <div class="auth-error-container">
      <div class="auth-error-card">
        <div class="error-icon">
          <i class="fas fa-lock"></i>
        </div>
        <h1 class="error-title">로그인이 필요합니다</h1>
        <p class="error-message">
          마이페이지에 접근하려면 먼저 로그인을 해주세요.<br />
          로그인 후 이용하실 수 있습니다.
        </p>

        <div class="error-actions">
          <a
            href="${pageContext.request.contextPath}/front?key=user&methodName=login"
            class="login-btn"
          >
            <i class="fas fa-sign-in-alt"></i>
            로그인하기
          </a>
          <a href="${pageContext.request.contextPath}/" class="home-btn">
            <i class="fas fa-home"></i>
            홈으로 가기
          </a>
        </div>

        <div class="additional-info">
          <p>계정이 없으신가요?</p>
          <a
            href="${pageContext.request.contextPath}/front?key=user&methodName=register"
            class="register-link"
          >
            회원가입하기
          </a>
        </div>
      </div>
    </div>
  </body>
</html>
