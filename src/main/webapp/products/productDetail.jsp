<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>상품 상세 샘플</h1>

<%-- <form action="${pageContext.request.contextPath}/front?key=cart&methodName=insertCart" method="post">
    <input type="hidden" name="userId" value="${sessionScope.userId}">
    <input type="hidden" name="productId" value="${product.productId}">
    <input type="number" name="quantity" value="1" min="1">
    <button type="submit">장바구니 담기</button>
</form> --%>
<%-- <form action="${pageContext.request.contextPath}/front?key=cart&methodName=insertCart" method="post" id="cartForm">
    <input type="hidden" id="userId" name="userId" value="1">
    <input type="hidden" id="productId" name="productId" value="1">
    <input type="number" id="quantity" name="quantity" value="1" min="1">
    <button type="submit">장바구니 담기</button>
</form> --%>

<!-- 상품상세 연결하면 진짜 쓸 폼 -->
<%-- <form id="cartForm">
    <!-- 회원일 경우 userId 설정, 비회원은 0 또는 빈 문자열 -->
    <input type="hidden" id="userId" name="userId" value="${sessionScope.userId != null ? sessionScope.userId : '0'}">
    <input type="hidden" id="productId" name="productId" value="${product.productId}">
    <input type="hidden" id="productName" name="productName" value="${product.name}">
    <input type="hidden" id="price" name="price" value="${product.price}">
    <input type="hidden" id="imageName" name="imageName" value="${product.imageName}">
    <input type="hidden" id="discountRate" name="discountRate" value="${product.discountRate}">
    
    <label for="quantity">수량:</label>
    <input type="number" id="quantity" name="quantity" value="1" min="1">
    <button type="submit">장바구니 담기</button>
</form> --%>

<form action="${pageContext.request.contextPath}/front?key=cart&methodName=insertCart" method="post" id="cartForm">
    <!-- 회원일 경우 userId 설정, 비회원은 0 또는 빈 문자열 -->
    <input type="hidden" id="userId" name="userId" value="1">
    <input type="hidden" id="productId" name="productId" value="2">
    <input type="hidden" id="productName" name="productName" value="닭가슴살 도시락">
    <input type="hidden" id="price" name="price" value="8500">
    <input type="hidden" id="imageName" name="imageName" value="https://saladpanda.co.kr/web/product/medium/202502/4db7a75405a01f9677165e715e1c3877.jpg">
    <input type="hidden" id="discountRate" name="discountRate" value="5">
    
    <label for="quantity">수량:</label>
    <input type="number" id="quantity" name="quantity" value="1" min="1">
    <button type="submit">장바구니 담기</button>
</form>

<script src="${pageContext.request.contextPath}/js/product/productDetail.js"></script>
</body>
</html>