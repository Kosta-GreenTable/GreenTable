<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>405 - 허용되지 않는 메소드</title>
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
      <div class="error-code">405</div>
      <div class="error-message">허용되지 않는 메소드</div>
      <div class="error-description">
        <p>이 페이지에서 요청된 메소드는 허용되지 않습니다.</p>
        <p>GET, POST 등 올바른 접근 방식으로 다시 시도해주세요.</p>
        <p>${error.message}</p>
      </div>
      <a href="${pageContext.request.contextPath}/" class="home-button"
        >홈페이지로 이동</a
      >
    </div>
  </body>
</html>
