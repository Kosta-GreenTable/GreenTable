<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   <jsp:include page="/common/header.jsp"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>그린테이블</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/reset.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/order/orderSuccess.css">
</head>
<body>
<div class="complete-container hd__inner1100">
    <div class="complete-container-inner">
        <div class="complete-message">
            <h1>주문이 완료되었습니다!</h1>
            <p>고객님의 주문이 성공적으로 접수되었습니다.</p>
            <p>주문 번호: <strong>#202505120001</strong></p>
            <p>주문 내역은 마이페이지에서 확인하실 수 있습니다.</p>
        </div>
        <div class="button-container">
            <a href="${pageContext.request.contextPath}/index.jsp" class="btn">홈으로</a>
            <a href="#" class="btn">주문 내역 확인</a>
        </div>
    </div>
</div>
</body>
</html>
<jsp:include page="/common/footer.jsp"/>