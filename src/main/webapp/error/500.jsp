<!-- filepath: c:\Users\user\git\GreenTable\src\main\webapp\error\500.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>내부 서버 오류</title>
    <style>
      body {
        font-family: "Arial", sans-serif;
        text-align: center;
        padding: 50px;
      }
      .error-container {
        max-width: 800px;
        margin: 0 auto;
        border: 1px solid #ddd;
        padding: 30px;
        border-radius: 5px;
        background-color: #f9f9f9;
      }
      h1 {
        color: #e74c3c;
      }
    </style>
  </head>
  <body>
    <div class="error-container">
      <h1>500 - 내부 서버 오류</h1>
      <p>서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.</p>
      <p><a href="${pageContext.request.contextPath}/">홈으로 돌아가기</a></p>

      <% if (request.getAttribute("javax.servlet.error.message") != null) { %>
      <div
        style="margin-top: 30px; text-align: left; font-size: 12px; color: #777"
      >
        <p>
          오류 메시지: <%= request.getAttribute("javax.servlet.error.message")
          %>
        </p>
      </div>
      <% } %>
    </div>
  </body>
</html>
