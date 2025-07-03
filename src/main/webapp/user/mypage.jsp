<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>마이페이지 | Green Table</title>

    <link rel="stylesheet" href="${path}/css/common/styles.css" />
    <link rel="stylesheet" href="${path}/css/user/mypage.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>    <jsp:include page="/common/header.jsp" />

    <!-- 로그인 체크 -->
    <c:if test="${empty sessionScope.loginUser}">
        <jsp:forward page="auth-required.jsp" />
    </c:if>

    <main class="mypage-container">
      <h1 class="page-title">마이페이지</h1>

      <div class="mypage-content">
        <!-- 사이드바 메뉴 -->
        <div class="mypage-sidebar">
          <div class="user-profile">
            <div class="profile-image">
              <i class="fas fa-user-circle"></i>
            </div>
            <div class="user-info">
              <p class="user-name">
                <c:out value="${sessionScope.loginUser.userInfoDto.userName}" />
                님
              </p>

            </div>
          </div>

          <nav class="sidebar-menu">
            <h3>나의 쇼핑정보</h3>            <ul>
              <li class="active"><a href="${path}/front?key=mypage&methodName=mypage">주문/배송 조회</a></li>
              <li><a href="${path}/user/mycancel.jsp">취소/환불 내역</a></li>
              <li><a href="${path}/user/mypoint.jsp">적립금 내역</a></li>
              <li><a href="${path}/user/mycoupon.jsp">쿠폰 내역</a></li>
              <li>
                <a href="${path}/front?key=review&methodName=myReviews"
                  >상품 리뷰</a
                >
              </li>
              <li>
                <a href="${path}/front?key=qna&methodName=myQnas">상품 문의</a>
              </li>
            </ul>
			<h3>나의 계정설정</h3>
			
			<ul>
			
			<li><a href="${path}/user/myinfo.jsp">회원정보 수정</a></li>
			
			</ul>

						<!--  <button class="profile-edit-btn">회원정보수정</button> -->
			</nav>
		</div>

        <!-- 메인 콘텐츠 -->
        <div class="mypage-main">
          <section class="member-info">
            <div class="grade-info">
              <div class="grade-title">
                <h3>
                  <c:out
                    value="${sessionScope.loginUser.userInfoDto.userName}"
                  />
                  님
                </h3>
              </div>
              <div class="grade-detail">
                <p>회원등급 :</p>
                <p class="grade-name">
                  <c:out
                    value="${sessionScope.loginUser.userInfoDto.userGrade}"
                  />
                </p>
                <span class="grade-value"
                  >포인트:
                  <c:out
                    value="${sessionScope.loginUser.userInfoDto.point}"
                  />원
                </span>
                <span class="grade-count">쿠폰 개수: 2</span>
              </div>
            </div>
          </section>


		<section class="recent-orders">
			<div class="section-header">
				<h3>최근 주문내역</h3>
				<p class="sub-text">주문 건수: <c:out value="${fn:length(orderList)}" />건</p>
			</div>
			<c:if test="${empty orderList }">
				<div class="order-list">
					<div class="no-orders">
						<p>최근 6개월간 주문 내역이 없습니다.</p>
					</div>
				</div>
			</c:if>
			
			<div class="order-list-container">
			  <table class="order-table">
			    <thead>
			      <tr>
			        <th>주문일</th>
			        <th>상품정보</th>
			        <th>결제금액</th>
			        <th>배송상태</th>
			        <th>주문번호</th>
			      </tr>
			    </thead>
			    <tbody>
				<c:forEach var="order" items="${orderList}">
			      <tr>
			        <td class="order-date">
						<fmt:formatDate value="${order.orderAt}" pattern="yyyy. MM. dd" />
					</td>
			        <td class="product-info">
			          <div class="product-box">
			            <c:choose>
			              <c:when test="${order.mainImageName.startsWith('products/')}">
			                <!-- S3 이미지 URL -->
			                <img src="${s3BaseUrl}/${order.mainImageName}" alt="상품이미지"
			                     onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">
			              </c:when>
			              <c:when test="${not empty order.mainImageName}">
			                <!-- 기존 로컬 이미지 -->
			                <img src="${path}/save/${order.mainImageName}" alt="상품이미지"
			                     onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';">
			              </c:when>
			              <c:otherwise>
			                <!-- 기본 이미지 -->
			                <img src="${s3BaseUrl}/products/no-image.jpg" alt="상품이미지">
			              </c:otherwise>
			            </c:choose>
			            <div class="product-details">
			              <p class="product-name">
							<c:choose>
								<c:when test="${not empty order.orderDetails}">
								<c:set var="firstProduct" value="${order.orderDetails[0]}" />
								<c:out value="${firstProduct.productName}" /> 
								<c:if test="${order.orderDetails.size() > 1}">
									외 ${order.orderDetails.size() - 1}개
								</c:if>
								</c:when>
								<c:otherwise>
								상품 정보 없음
								</c:otherwise>
							</c:choose>
			            </div>
			          </div>
			        </td>
			        <td class="order-price"><fmt:formatNumber value="${order.totalAmount}" />원</td>
			        <td class="order-status">${order.orderStatus}</td>
			        <td class="order-id">${order.merchantUid}</td>
			      </tr>
			</c:forEach>
			 </tbody>
			  </table>
			</div>
			
					<div class="view-more">
				<a href="${path}/front?key=mypage&methodName=mypage" class="btn-view-more">더보기 <i
					class="fas fa-angle-right"></i></a>
			</div>
			
		</section>
			</div>
		</div>
	</main>

	<jsp:include page="/common/footer.jsp" />
	<script src="${path}/js/user/include.js"></script>
	<script src="${path}/js/user/script.js"></script>
	<script src="${path}/js/user/mypage.js"></script>
</body>
</html>
