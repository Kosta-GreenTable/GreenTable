<%@page import="site.greentable.dto.CartDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<jsp:include page="/common/header.jsp"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>그린테이블</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/reset.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/order/cart.css">
</head>
<body data-contextpath="${pageContext.request.contextPath}" data-userid="${sessionScope.userId}">
  <div class="cart-container hd__inner1100">
    <div class="cart-title">
    	<h1>장바구니</h1>
    </div>
    
    <c:choose>
      <c:when test="${empty cartList}">
      	<div  class="empty-cart-container">
          <div class="empty-cart">
            <p>장바구니에 담긴 상품이 없습니다.</p>
          </div>
      		<a href="${pageContext.request.contextPath}/index.jsp"><button class="home-btn">홈으로</button></a>
      	</div>
      </c:when>
      
      <c:otherwise>
      <table class="cart-table">
      <thead>
        <tr>
          <th><input type="checkbox"></th>
          <th>상품정보</th>
          <th>수량</th>
          <th>구매 금액</th>
          <th>선택</th>
        </tr>
      </thead>
      <tbody>
      	<c:forEach var="cart" items="${cartList}">
      		<tr data-userid="${cart.userId}" data-product-id="${cart.productId}" >
		  		<td><input type="checkbox"></td>
		  		<td class="product-info">
					<img src="${cart.imageName}" alt="상품 이미지">
					<div>
			  			<p>${cart.productName}</p>
					</div>
		  		</td>
		  		<td class="quantity-cell">
					<button class="quantity-btn minus">-</button>
					<input type="text" value="${cart.quantity}" class="quantity-input" readonly>
					<button class="quantity-btn plus">+</button>
		  		</td>
		  		<td class="product-price"><fmt:formatNumber value="${cart.price * cart.quantity}" />원</td>
		  		<td>
		            <div class="action-buttons">
		              <button class="order-btn">주문</button>
		              <button class="delete-btn">삭제</button>
		            </div>
		  		</td>
      		</tr>      	
      	</c:forEach>
            </tbody>
    	</table>
    	<div class="price-container">
        <div class="price-block">
          <p>총 상품금액</p>
          <p class="price-cell"><fmt:formatNumber value="${totalProductPrice}" />원</p>
        </div>
        <img src="https://atowertr6856.cdn-nhncommerce.com/data/skin/front/kaimen_pc_n/img/order/order_price_minus.png" alt="빼기" class="price-icon">
        <div class="price-block">
          <p>총 할인금액</p>
          <p class="price-cell"><fmt:formatNumber value="${totalDiscount}" />원</p>
        </div>
        <img src="https://atowertr6856.cdn-nhncommerce.com/data/skin/front/kaimen_pc_n/img/order/order_price_plus.png" alt="더하기" class="price-icon">
        <div class="price-block">
          <p>총 배송비</p>
          <p class="price-cell"><fmt:formatNumber value="${deliveryFee}" />원</p>
        </div>
        <img src="https://atowertr6856.cdn-nhncommerce.com/data/skin/front/kaimen_pc_n/img/order/order_price_total.png" alt="합계" class="price-icon">
        <div class="price-block total">
          <p>결제금액</p>
          <p class="price-cell"><fmt:formatNumber value="${totalPayPrice}" />원</p>
        </div>
      </div>
      <form id="orderForm" method="post" action="${pageContext.request.contextPath}/ajax?key=orderRest&methodName=processOrder" >
	      <input type="hidden" name="productIds" id="productIds" >
		    <input type="hidden" name="quantity" id="quantity">
			      
	      <div class="cart-buttons">
	        <button type="button" class="continue-btn">쇼핑 계속하기</button>
	        <button type="button" class="order-selected-btn">선택 상품 주문</button>
	        <button type="button" class="order-all-btn">전체 주문</button>
	      </div>
      </form>
     
      </c:otherwise>
    </c:choose>
      
        
    
  </div>
  <script src="${pageContext.request.contextPath}/js/order/cart.js"></script>
</body>
</html>
<jsp:include page="/common/footer.jsp"/>