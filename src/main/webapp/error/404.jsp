<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>404 - 페이지를 찾을 수 없습니다</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
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
        <div class="error-code">404</div>
        <div class="error-message">페이지를 찾을 수 없습니다</div>
        <div class="error-description">
            <p>요청하신 페이지가 존재하지 않거나, 잘못된 경로로 접근하셨습니다.</p>
            <p>URL을 다시 확인하시거나, 아래 버튼을 클릭하여 홈페이지로 이동하세요.</p>
        </div>
        <a href="${pageContext.request.contextPath}/" class="home-button">홈페이지로 이동</a>
    </div>
</body>
</html>