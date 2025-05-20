<%@page import="site.greentable.dto.UserDTO"%>
<%@page import="site.greentable.dto.CartDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
	Integer userId = (Integer) session.getAttribute("userId");
    UserDTO user = (UserDTO) session.getAttribute("loginUser");

	String userEmail = user != null ? user.getEmail() : "";
    String userName = user != null && user.getUserInfoDto() != null ? user.getUserInfoDto().getUserName() : "";
    String userPhone = user != null && user.getUserInfoDto() != null ? user.getUserInfoDto().getPhone() : "";

    List<CartDTO> orderItems = (List<CartDTO>) session.getAttribute("orderItems");
%>

   <jsp:include page="/common/header.jsp"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>그린테이블</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/reset.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/order/order.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!--  <script src="https://cdn.portone.io/v2/browser-sdk.js"></script> -->
<script src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
</head>
<body data-context-path="${pageContext.request.contextPath}">
<div class="order-container hd__inner1100">
    <h2 class="order-title">주문결제</h2>
    
        <form id="orderForm" method="post" action="${pageContext.request.contextPath}/front?key=order&methodName=completeOrder">
            <div class="order-container-inner">

            
            <div class="order-left">
                <!-- 주문자 정보 -->
                <section class="order-section" id="orderInfo">
                    <h2 class="order-section-title">주문자 정보</h2>
                    <div class="form-group">
                        <label for="name">이름 *</label>
                        <div class="sub-group">
                            <input type="text" id="name" name="name" placeholder="주문자 이름" value="${sessionScope.loginUser.userInfoDto.userName}" required >
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="email">이메일 *</label>
                        <div class="sub-group email-group">
                            <input type="text" name="email1" id="email1" value="${fn:split(sessionScope.loginUser.email, '@')[0]}" required><span>@</span>
                            <input type="text" name="email2" id="email2" value="${fn:split(sessionScope.loginUser.email, '@')[1]}" required>
                            <select id="emailSelect">
                                <option value="">직접입력</option>
                                <option value="gmail.com">gmail.com</option>
                                <option value="naver.com">naver.com</option>
                                <option value="daum.net">daum.net</option>
                                <option value="nate.com">nate.com</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="phone">휴대폰번호 *</label>
                        <div class="sub-group phone-group">
                            <select name="phonePrefix" required>
                                <option value="">선택</option>
                                <option value="010" <c:if test="${userPhone.startsWith('010')}">selected</c:if>>010</option>
                                <option value="011" <c:if test="${userPhone.startsWith('011')}">selected</c:if>>011</option>
                                <option value="016" <c:if test="${userPhone.startsWith('016')}">selected</c:if>>016</option>
                                <option value="017" <c:if test="${userPhone.startsWith('017')}">selected</c:if>>017</option>
                                <option value="018" <c:if test="${userPhone.startsWith('018')}">selected</c:if>>018</option>
                            </select><span>-</span>
                            <input type="text" id="phone1" name="phone1" maxlength="4" value="${userPhone != null ? userPhone.substring(3, 7) : ''}" required><span>-</span>
                            <input type="text" id="phone2" name="phone2" maxlength="4" value="${userPhone != null ? userPhone.substring(7) : ''}" required>
                        </div>                   
                    </div>

                    <c:if test="${empty userId}">
                        <div class="form-group">
                            <label for="password">주문 비밀번호 *</label>
                            <div class="sub-group">
                                <input type="password" id="password" name="password" placeholder="비밀번호" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="passwordConfirm">주문 비밀번호 확인 *</label>
                            <div class="sub-group">
                                <input type="password" id="passwordConfirm" name="passwordConfirm" placeholder="비밀번호 확인" required>
                            </div>
                        </div>
                    </c:if>
                </section>
                
                <!-- 배송지 정보 -->
                <section class="order-section">
                    <h2 class="order-section-title">배송지 정보</h2>
                    <div class="form-group">
                        <label><input type="radio" id="sameAsOrderer"> 주문자 정보와 동일</label>
                    </div>
                    <div class="form-group">
                        <label for="recipient">받는 분 *</label>
                        <div class="sub-group">
                            <input type="text" id="recipient" name="recipient" placeholder="받는 분" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="zipCode">주소 *</label>
                        <div class="sub-group address-group">
                            <input type="text" name="zipCode" id="zipCode" placeholder="우편번호" required>
                            <button type="button">주소검색</button>
                            <input type="text" id="address" name="address" placeholder="주소" required>
                            <input type="text" id="addressDetail" name="addressDetail" placeholder="상세주소" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>휴대폰번호 *</label>
                        <div class="sub-group phone-group">
                            <select name="recipientPhonePrefix" required>
                                <option value="">선택</option>
                                <option value="010">010</option>
                                <option value="011">011</option>
                                <option value="016">016</option>
                                <option value="017">017</option>
                                <option value="018">018</option>
                            </select><span>-</span>
                            <input type="text" id="recipientPhone1" name="recipientPhone1" maxlength="4" required><span>-</span>
                            <input type="text" id="recipientPhone2" name="recipientPhone2" maxlength="4" required>
                        </div>
                    </div>
                </section>

                <!-- 쿠폰/포인트 -->
                <section class="order-section">
                    <h2 class="order-section-title">할인/부가결제</h2>
                    <div class="form-group">
                        <label for="coupon">쿠폰 할인</label>
                        <div class="sub-group coupon-group">
                            <div class="coupon-input">
                                <span id="couponDiscount">0원</span>
                                <button type="button" id="applyCouponBtn" >쿠폰 적용</button>
                            </div>
                            <span>보유 쿠폰</span>
                            <span>2개</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="point">적립금</label>
                        <div class="sub-group point-group">
                            <div class="point-input">
                                <input type="text" name="point" id="point" placeholder="적립금 입력">
                                <button type="button" id="useAllPointsBtn" >전액 사용</button>
                            </div>
                            <span>적립금 잔액</span>
                            <span id="pointBalance">2,000원</span>
                        </div>
                    </div>
                </section>

                <!-- 결제수단 선택 -->
                <section class="order-section">
                <h2 class="order-section-title">결제수단 선택</h2>
                <div class="payment-method">
                    <h3>간편결제</h3>
                    <div class="quick-payment">
                        <button type="button">토스페이</button>
                        <button type="button">네이버페이</button>
                        <button type="button">PAYCO</button>
                        <button type="button">삼성페이</button>
                    </div>
                    <h3>일반결제</h3>
                    <div class="general-payment">
                        <button type="button">신용카드</button>
                        <button type="button">가상계좌</button>
                        <button type="button">계좌이체</button>
                    </div>
                </div>
                <div class="payment-info">
                    <h4>결제안내</h4>
                    <p>
                        고객이 온라인 쇼핑몰에서 상품 및 서비스를 신용카드로 진행하는 결제 서비스입니다. <br>
                        카드번호 유효기간 등의 신용정보는 안전하게 암호화되어 해당 신용카드사로 전달됩니다.
                    </p>
                </div>
                
                </section>
            </div>
          
            <div class="order-right">
                <!-- 주문 상품 정보 -->
                <div class="sticky-box">
                    <div class="order-product-title">
                        <h3>주문상품</h3>
                        <span>${orderList.size()} 개</span>
                    </div>
                    <div class="order-product-contents">
                        <c:forEach var="item" items="${orderList}">
                            <div class="product-info">
                                <div class="product-image">
                                    <img src="${item.imageName}" alt="상품 이미지">
                                </div>
                                <div class="product-description">
                                    <h4>${item.productName}</h4>
                                    <p>수량: ${item.quantity}</p>
                                    <p>가격: <fmt:formatNumber value="${item.price * item.quantity}" type="number" />원</p>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="price-info">
                        <div class="order-product-title">
                            <h3>결제정보</h3>
                        </div>
                        <div class="price-details">
                            <table>
                                <colgroup>
                                    <col style="width: 155px">
                                    <col style="width: auto">
                                </colgroup>
                                <tbody>
                                    <tr>
                                        <th>상품금액</th>
                                        <td><fmt:formatNumber value="${requestScope.totalProductPrice}" type="number" />원</td>
                                    </tr>
                                    <tr>
                                        <th>배송비</th>
                                        <td>+<fmt:formatNumber value="${requestScope.deliveryFee}" type="number" />원</td>
                                    </tr>
                                    <tr>
                                        <th>할인/부가결제</th>
                                        <td><span class="discount">-<fmt:formatNumber value="${requestScope.totalDiscount}" type="number" /></span>원</td>
                                    </tr>
                                </tbody>
                            </table>
                            <div class="total-price">
                                <h3>총 결제 금액</h3>
                                <strong><fmt:formatNumber value="${requestScope.totalPayPrice}" type="number" />원</strong>
                            </div>
                        </div>  
                    </div>
                    <div class="terms">
                        <label><input type="checkbox" id="agreeAll"> 필수 항목 전체 동의하기</label><br>
                        <label><input type="checkbox" class="agree-checkbox" required> 전자금융거래 동의 (필수)</label><br>
                        <label><input type="checkbox" class="agree-checkbox" required> 개인정보 수집/이용 동의 (필수)</label>
                    </div>
                    <div class="order-button">
                        <button type="submit" class="pay-btn" id="payBtn">결제하기</button>
                        <button class="cancel-btn">취소</button>
                    </div>
                </div>
            </div>
       
            <input type="hidden" id="merchantUid"     name="merchantUid"    value="${merchantUid}"/>
            <input type="hidden" id="impUid"          name="impUid"         />
            <input type="hidden" id="paymentMethod"   name="paymentMethod"  value="CREDIT_CARD"/>
            <input type="hidden" id="totalAmount"     name="totalAmount"    />
            <input type="hidden" id="paymentStatus"   name="paymentStatus"  />       
            
        </form>

    </div>
</div>
<script src="${pageContext.request.contextPath}/js/order/order.js"></script>
<script src="${pageContext.request.contextPath}/js/order/payment.js"></script>
</body>
</html>
<jsp:include page="/common/footer.jsp"/>