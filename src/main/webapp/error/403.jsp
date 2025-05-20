<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>403 - 접근 금지</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/styles.css"
    />
    <style>
      .error-container {
        text-align: center;
        padding: 50px 20px;
        max-width: 600px;
        margin: 0 auto;
      }
      .error-code {
        font-size: 72px;
        font-weight: bold;
        color: #e74c3c;
        margin-bottom: 20px;
      }
      .error-message {
        font-size: 24px;
        margin-bottom: 30px;
      }
      .error-description {
        margin-bottom: 30px;
        color: #555;
      }
      .home-button {
        display: inline-block;
        padding: 10px 20px;
        background-color: #3498db;
        color: #fff;
        text-decoration: none;
        border-radius: 5px;
        font-weight: bold;
      }
    </style>
  </head>
  <body>
    <div class="error-container">
      <div class="error-code">403</div>
      <div class="error-message">접근 금지</div>
      <div class="error-description">
        <p>해당 페이지에 접근할 권한이 없습니다.</p>
        <p>필요한 권한이 없거나 접근이 제한된 리소스입니다.</p>
        <p>${error.message}</p>
      </div>
      <a href="${pageContext.request.contextPath}/" class="home-button"
        >홈페이지로 이동</a
      >
    </div>
  </body>
</html>
