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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/order/order.css">
</head>
<body>
<div class="order-container hd__inner1100">
    <h2 class="order-title">주문결제</h2>
    <div class="order-container-inner">

        <div class="order-left">
            <!-- 주문자 정보 -->
            <section class="order-section" id="orderInfo">
                <h2 class="order-section-title">주문자 정보</h2>
                <form>
                <div class="form-group">
                    <label for="name">이름 *</label>
                    <div class="sub-group">
                        <input type="text" id="name" placeholder="주문자 이름">
                    </div>
                </div>
                <div class="form-group">
                    <label for="email">이메일 *</label>
                    <div class="sub-group email-group">
                        <input type="text" name="email1" id="email" required><span>@</span>
                        <input type="text" name="email2" id="email2" required>
                        <select id="emailSelect">
                            <option value="">직접입력</option>
                            <option value="naver.com">naver.com</option>
                            <option value="gmail.com">gmail.com</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="phone">휴대폰번호 *</label>
                    <div class="sub-group phone-group">
                        <select>
                            <option>선택</option>
                            <option>010</option>
                            <option>011</option>
                        </select><span>-</span>
                        <input type="text" maxlength="4"><span>-</span>
                        <input type="text" maxlength="4">
                    </div>                   
                </div>
                <div class="form-group">
                    <label for="password">주문 비밀번호 *</label>
                    <div class="sub-group">
                        <input type="password" id="password" placeholder="비밀번호" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="password-confirm">주문 비밀번호 확인 *</label>
                    <div class="sub-group">
                        <input type="password" id="password-confirm" placeholder="비밀번호 확인" required>
                    </div>
                </div>
                </form>
            </section>
            
            <!-- 배송지 정보 -->
            <section class="order-section">
                <h2 class="order-section-title">배송지 정보</h2>
                <form>
                <div class="form-group">
                    <label><input type="checkbox" id="sameAsOrderer"> 주문자 정보와 동일</label>
                </div>
                <div class="form-group">
                    <label for="recipient">받는 분 *</label>
                    <div class="sub-group">
                        <input type="text" id="recipient" placeholder="받는 분" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="zip-code">주소 *</label>
                    <div class="sub-group address-group">
                        <input type="text" name="zip-code" id="zipCode" placeholder="우편번호" required>
                        <button type="button">주소검색</button>
                        <input type="text" name="address" placeholder="주소" required>
                        <input type="text" name="address-detail" placeholder="상세주소" required>
                    </div>
                </div>
                <div class="form-group">
                    <label>휴대폰번호 *</label>
                    <div class="sub-group phone-group">
                        <select>
                            <option>선택</option>
                            <option>010</option>
                            <option>011</option>
                        </select><span>-</span>
                        <input type="text" maxlength="4"><span>-</span>
                        <input type="text" maxlength="4">
                    </div>
                </div>
                </form>
            </section>

            <!-- 쿠폰/포인트 -->
            <section class="order-section">
                <h2 class="order-section-title">할인/부가결제</h2>
                <form>
                    <div class="form-group">
                        <label for="coupon">쿠폰 할인</label>
                        <div class="sub-group coupon-group">
                            <div class="coupon-input">
                                <span>0원</span>
                                <button>쿠폰 적용</button>
                            </div>
                            <span>보유 쿠폰</span>
                            <span>2개</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="point">적립금</label>
                        <div class="sub-group point-group">
                            <div class="point-input">
                                <input type="text" id="point" placeholder="적립금 입력">
                                <button>전액 사용</button>
                            </div>
                            <span>적립금 잔액</span>
                            <span>2,000원</span>
                        </div>
                    </div>            
                </form>
            </section>

            <!-- 결제수단 선택 -->
            <section class="order-section">
            <h2 class="order-section-title">결제수단 선택</h2>
            <div class="payment-method">
                <h3>간편결제</h3>
                <div class="quick-payment">
                    <button>토스페이</button>
                    <button>네이버페이</button>
                    <button>PAYCO</button>
                    <button>삼성페이</button>
                </div>
                <h3>일반결제</h3>
                <div class="general-payment">
                    <button>신용카드</button>
                    <button>가상계좌</button>
                    <button>계좌이체</button>
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
                    <span>2개</span>
                </div>
                <div class="order-product-contents">
                    <div class="product-info">
                        <div class="product-image">
                            <a href="#">
                                <img src="https://saladpanda.co.kr/web/product/tiny/202407/d40f6e2cab42a7745c8b035de77cd15e.jpg" alt="상품 이미지">
                            </a>
                        </div>
                        <div class="product-description">
                            <h4>상품 A</h4>
                            <p>상품 설명</p>
                        </div>
                        <button class="product-delete-btn" id="deleteBtn">삭제</button>
                    </div>
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
                                    <td>15,800원</td>
                                </tr>
                                <tr>
                                    <th>배송비</th>
                                    <td>+3,500원</td>
                                </tr>
                                <tr>
                                    <th>할인/부가결제</th>
                                    <td><span class="discount">-1,800</span>원</td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="total-price">
                            <h3>총 결제 금액</h3>
                            <strong>17,500원</strong>
                        </div>
                    </div>  
                </div>
                <div class="terms">
                    <label><input type="checkbox" required> 전자금융거래 동의 (필수)</label><br>
                    <label><input type="checkbox" required> 개인정보 수집/이용 동의 (필수)</label>
                </div>
                <div class="order-button">
                    <a href="${pageContext.request.contextPath}/order/orderSuccess.jsp"><button class="pay-btn">결제하기</button></a>
                    <a href="${pageContext.request.contextPath}/order/cart.jsp"><button class="cancel-btn">취소</button></a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<jsp:include page="/common/footer.jsp"/>