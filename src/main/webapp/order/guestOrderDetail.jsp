<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="site.greentable.util.ImageUtil" %>
<%@ page import="java.lang.System" %>
<%
    String s3BaseUrl = System.getenv("S3_BASE_URL");
    if (s3BaseUrl == null) {
        s3BaseUrl = "https://greentable-images-your-region.s3.ap-northeast-2.amazonaws.com";
    }
    pageContext.setAttribute("s3BaseUrl", s3BaseUrl);
%>

<c:set var="path" value="${pageContext.request.contextPath}" />
<jsp:include page="/common/header.jsp"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>그린테이블</title>
<!-- <link rel="stylesheet" href="${path}/css/common/reset.css" /> -->
<link rel="stylesheet" href="${path}/css/order/guestOrderDetail.css">
</head>
<body>
    <c:if test="${empty order}">
        <h1 class="empty-order">주문 정보를 찾을 수 없습니다.</h1>
    </c:if>

    <c:if test="${not empty order}">
        <div class="order-wrapper hd__inner1100">
            <!-- 주문 타이틀 및 주문 정보 -->
            <div class="order-header">
              <h2 class="order-title">주문상세조회</h2>
              <div class="order-subtitle">
                <div><span class="order-subtitle2">주문번호 | ${order.merchantUid}</span></div>
                <div><span class="order-subtitle2">주문일 | <fmt:formatDate value="${order.orderAt}" pattern="yyyy-MM-dd"/></span></div>
              </div>
            </div>
          
            <!-- 상품 테이블 -->
            <div class="container">
                <table class="product-table">
                <thead>
                    <tr>
                    <th>상품 정보</th>
                    <th>수량</th>
                    <th>상품 금액</th>
                    <th>배송 상황</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${order.orderDetails}">
                        <tr>
                            <td class="product-info">
                                <c:choose>
                                  <c:when test="${order.mainImageName.startsWith('products/')}">
                                    <!-- S3 이미지 URL -->
                                    <img class="product-img" src="${s3BaseUrl}/${order.mainImageName}" alt="${detail.productName}">
                                  </c:when>
                                  <c:otherwise>
                                    <!-- 기존 로컬 이미지 -->
                                    <img class="product-img" src="${s3BaseUrl}/${order.mainImageName}" alt="${detail.productName}">
                                  </c:otherwise>
                                </c:choose>
                                <div class="product-name">${detail.productName}</div>
                            </td>
                            <td>${detail.quantity}</td>
                            <td><fmt:formatNumber value="${detail.price}" type="number"/>원</td>
                            <td class="delivery-status">
                                ${order.orderStatus}<br>
                                <button class="delivery-button">배송조회</button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- 결제박스 -->
            <c:set var="totalOrderPrice" value="0"/>
            <c:forEach var="detail" items="${order.orderDetails}">
                <c:set var="totalOrderPrice" value="${totalOrderPrice + detail.price * detail.quantity}"/>
            </c:forEach>
            <c:set var="totalDiscount" value="${totalOrderPrice - order.totalAmount}"/>

            <div class="container">
                <h2  class="sub-title">결제정보</h2>
                <div class="payment-box">
                <div class="column">
                    <div class="label">총 주문 금액</div>
                    <div class="amount"><fmt:formatNumber value="${totalOrderPrice}" type="number"/><span>원</span></div>
                    <div class="sub">
                        <div><span>총 상품금액</span><span><fmt:formatNumber value="${totalOrderPrice}" type="number"/>원</span></div>
                        <div><span>총 배송비</span><span>3,500원</span></div>
                    </div>
                </div>
                <div class="operator">-</div>
                <div class="column">
                    <div class="label">총 할인금액</div>
                    <div class="amount"><fmt:formatNumber value="${totalDiscount}" type="number"/><span>원</span></div>
                    <div class="sub">
                        <div><span>쿠폰</span><span><fmt:formatNumber value="${totalDiscount}" type="number"/>원</span></div>
                    </div>
                </div>
                <div class="operator">=</div>
                <div class="column">
                    <div class="label">결제금액</div>
                    <div class="amount highlight"><fmt:formatNumber value="${order.totalAmount}" type="number"/><span>원</span></div>
                </div>
                </div>
                <div class="notice">* 제주/도서산간 지역의 경우 추가 배송비가 발생할 수 있습니다.</div>
            </div>


            <!-- 주문자 정보 -->
            <section class="container">
                <h2 class="sub-title">구매자정보</h2>
                <table>
                    <tr><th>주문하시는 분</th><td>${order.customerName}</td></tr>
                    <tr><th>이메일주소</th><td>${order.customerEmail}</td></tr>
                    <tr><th>휴대폰번호</th><td>${order.customerPhone}</td></tr>
                </table>
            </section>
        
            <!-- 결제 정보 -->
            <section class="container">
              <h2 class="sub-title">결제정보</h2>
              <table>
                <tr><th>주문번호</th><td>${order.merchantUid}</td></tr>
                <tr><th>주문일자</th><td><fmt:formatDate value="${order.orderAt}" pattern="yyyy-MM-dd"/></td></tr>
                <tr><th>주문 처리상태</th><td>결제완료</td></tr>
              </table>
            </section>
        
            <!-- 배송지 정보 -->
            <section class="container">
              <h2 class="sub-title">배송지정보</h2>
              <table>
                <tr><th>받으시는 분</th><td>${order.recipient}</td></tr>
                <tr><th>휴대폰번호</th><td>${order.recipientPhone}</td></tr>
                <tr><th>우편번호</th><td>${order.zipCode}</td></tr>
                <tr><th>주소</th><td>${order.address} ${order.addressDetail}</td></tr>
              </table>
            </section>

            <div>
                <a href="${path}/index.jsp"><button class="home-btn">홈으로</button></a>
            </div>
        
        
        </div>
    
    
</c:if>
</body>
</html>
<jsp:include page="/common/footer.jsp"/>