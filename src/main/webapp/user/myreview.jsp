<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ page import="site.greentable.util.ImageUtil" %> <%@ page import="java.lang.System" %>
<%
    String s3BaseUrl = System.getenv("S3_BASE_URL");
    if (s3BaseUrl == null) {
        s3BaseUrl = "https://greentable-images-your-region.s3.ap-northeast-2.amazonaws.com";
    }
    pageContext.setAttribute("s3BaseUrl", s3BaseUrl);
%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>상품 리뷰 | Green Table</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/styles.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/user/mypage.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/user/myreview.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />
    <!-- 메인 컨텐츠 - 상품 리뷰 섹션 -->
    <main class="mypage-container">
      <%-- 로그인 체크 --%>
      <c:if test="${empty sessionScope.loginUser}">
        <jsp:forward page="../auth-required.jsp" />
      </c:if>
      <c:set var="path" value="${pageContext.request.contextPath}" />

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
            <h3>나의 쇼핑정보</h3>
            <ul>
              <li>
                <a href="${path}/front?key=mypage&methodName=mypage"
                  >주문/배송 조회</a
                >
              </li>
              <li><a href="${path}/user/mycancel.jsp">취소/환불 내역</a></li>
              <li><a href="${path}/user/mypoint.jsp">적립금 내역</a></li>
              <li><a href="${path}/user/mycoupon.jsp">쿠폰 내역</a></li>
              <li class="active">
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
          </nav>
        </div>

        <!-- 상품 리뷰 메인 내용 -->
        <div class="mypage-main">
          <!-- 상품 리뷰 현황 섹션 -->
          <section class="review-summary">
            <div class="summary-box">
              <div class="summary-item">
                <p class="item-title">작성 가능 리뷰</p>
                <p class="item-count">${writableReviewsCount}</p>
              </div>
              <div class="summary-divider"></div>
              <div class="summary-item">
                <p class="item-title">작성 완료 리뷰</p>
                <p class="item-count">${writtenReviewsCount}</p>
              </div>
              <div class="summary-divider"></div>
              <div class="summary-item">
                <p class="item-title">포토 리뷰</p>
                <p class="item-count">${photoReviewsCount}</p>
              </div>
            </div>
          </section>

          <!-- 상품 리뷰 탭 섹션 -->
          <section class="review-tab-section">
            <div class="tab-container">
              <button class="tab-btn active">
                작성 가능한 리뷰 (${writableReviewsCount})
              </button>
              <button class="tab-btn">
                작성한 리뷰 (${writtenReviewsCount})
              </button>
            </div>

            <!-- 작성 가능한 리뷰 탭 내용 -->
            <div class="tab-content active">
              <div class="section-header">
                <h3>작성 가능한 리뷰</h3>
                <div class="section-info">
                  구매확정 후 30일 이내에 리뷰를 작성할 수 있습니다.
                </div>
              </div>

              <!-- 작성 가능한 리뷰 목록 -->
              <div class="review-list">
                <c:choose>
                  <c:when test="${empty writableReviews}">
                    <!-- 작성 가능한 리뷰가 없을 경우 -->
                    <div class="no-review">
                      <p>작성 가능한 리뷰가 없습니다.</p>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <!-- 작성 가능한 리뷰가 있을 경우 -->
                    <c:forEach var="review" items="${writableReviews}">
                      <div class="review-item writable">
                        <div class="review-product">
                          <div class="product-image">
                            <img
                              src="${review.productImage}"
                              alt="${review.productName}"
                            />
                          </div>
                          <div class="product-info">
                            <p class="order-date">
                              주문일자: ${review.orderDate}
                            </p>
                            <h4 class="product-name">${review.productName}</h4>
                            <div class="review-deadline">
                              <span class="deadline-text"
                                >리뷰 작성 기한:
                                <b>${review.deadline}</b>까지</span
                              >
                            </div>
                          </div>
                        </div>
                        <div class="review-action">
                          <button
                            class="btn-write-review"
                            onclick="location.href='${pageContext.request.contextPath}/front?key=review&methodName=writeForm&productId=${review.productId}&orderDetailId=${review.orderDetailId}'"
                          >
                            리뷰 작성
                          </button>
                        </div>
                      </div>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>

            <!-- 작성한 리뷰 탭 내용 -->
            <div class="tab-content">
              <div class="section-header">
                <h3>작성한 리뷰</h3>
              </div>

              <!-- 작성한 리뷰 목록 -->
              <div class="review-list">
                <c:choose>
                  <c:when test="${empty writtenReviews}">
                    <!-- 작성한 리뷰가 없을 경우 -->
                    <div class="no-review">
                      <p>작성한 리뷰가 없습니다.</p>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <!-- 작성한 리뷰가 있을 경우 -->
                    <c:forEach var="review" items="${writtenReviews}">
                      <div class="review-item written">
                        <div class="review-product">
                          <div class="product-image">
                            <c:choose>
                              <c:when test="${not empty review.productImage}">
                                <img
                                  src="${review.productImage}"
                                  alt="${review.productName}"
                                />
                              </c:when>
                              <c:otherwise>
                                <img
                                  src="${s3BaseUrl}/products/no-image.jpg"
                                  alt="${review.productName}"
                                />
                              </c:otherwise>
                            </c:choose>
                          </div>
                          <div class="product-info">
                            <p class="review-date">
                              작성일자:
                              <fmt:formatDate
                                value="${review.createdAt}"
                                pattern="yyyy-MM-dd"
                              />
                            </p>
                            <h4 class="product-name">${review.productName}</h4>
                            <div class="rating">
                              <span class="rating-text">평점:</span>
                              <span class="stars">
                                <c:forEach begin="1" end="5" var="i">
                                  <c:choose>
                                    <c:when test="${i <= review.rating}">
                                      <i class="fas fa-star"></i>
                                    </c:when>
                                    <c:when
                                      test="${i > review.rating && i-0.5 <= review.rating}"
                                    >
                                      <i class="fas fa-star-half-alt"></i>
                                    </c:when>
                                    <c:otherwise>
                                      <i class="far fa-star"></i>
                                    </c:otherwise>
                                  </c:choose>
                                </c:forEach>
                              </span>
                              <span class="rating-value">${review.rating}</span>
                            </div>
                          </div>
                        </div>
                        <div class="review-content">
                          <div class="review-text">
                            <p>${review.content}</p>
                          </div>
                          <c:if test="${not empty review.images}">
                            <div class="review-photos">
                              <c:forEach var="image" items="${review.images}">
                                <div class="photo-item">
                                  <img
                                    src="${s3BaseUrl}/${image.realName}"
                                     onerror="this.onerror=null; this.src='${s3BaseUrl}/reviews/no-image.jpg';"
                                    alt="리뷰사진"
                                  />
                                </div>
                              </c:forEach>
                            </div>
                          </c:if>
                          <div class="review-actions">
                            <button
                              class="btn-edit-review"
                              onclick="location.href='${pageContext.request.contextPath}/front?key=review&methodName=updateForm&reviewId=${review.reviewId}'"
                            >
                              수정
                            </button>
                            <button
                              class="btn-delete-review"
                              onclick="if(confirm('정말 삭제하시겠습니까?')) location.href='${pageContext.request.contextPath}/front?key=review&methodName=deleteReview&reviewId=${review.reviewId}'"
                            >
                              삭제
                            </button>
                          </div>
                        </div>
                      </div>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>

            <!-- 페이지네이션 -->
            <c:if test="${not empty pagination}">
              <div class="pagination">
                <c:if test="${pagination.hasPrev}">
                  <a
                    href="${pageContext.request.contextPath}/front?key=review&methodName=myReviews&page=${pagination.prevPage}"
                    class="page-nav"
                    >&lt;</a
                  >
                </c:if>

                <c:forEach
                  begin="${pagination.startPage}"
                  end="${pagination.endPage}"
                  var="pageNum"
                >
                  <a
                    href="${pageContext.request.contextPath}/front?key=review&methodName=myReviews&page=${pageNum}"
                    class="page-num ${pageNum == pagination.currentPage ? 'active' : ''}"
                    >${pageNum}</a
                  >
                </c:forEach>

                <c:if test="${pagination.hasNext}">
                  <a
                    href="${pageContext.request.contextPath}/front?key=review&methodName=myReviews&page=${pagination.nextPage}"
                    class="page-nav"
                    >&gt;</a
                  >
                </c:if>
              </div>
            </c:if>
            <div class="page-info">
              <c:if test="${not empty totalCount}">
                <span>총 ${totalCount}개의 리뷰</span>
              </c:if>
            </div>
          </section>

          <!-- 리뷰 작성 혜택 안내 -->
          <section class="info-section">
            <h3 class="info-title">리뷰 작성 혜택 안내</h3>
            <ul class="info-list">
              <li>
                <i class="fas fa-check"></i> 일반 리뷰 작성 시 100원의 적립금을
                드립니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 포토 리뷰 작성 시 300원의 적립금을
                추가로 드립니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 상품과 무관한 내용의 리뷰는 적립금
                지급 대상에서 제외될 수 있습니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 작성한 리뷰는 수정 및 삭제가
                가능하나, 지급된 적립금은 회수될 수 있습니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 리뷰 작성은 구매확정 후 30일 이내에
                가능합니다.
              </li>
            </ul>
          </section>
        </div>
      </div>
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <script>
      document.addEventListener("DOMContentLoaded", function () {
        // 탭 전환 기능
        const tabBtns = document.querySelectorAll(".tab-btn");
        const tabContents = document.querySelectorAll(".tab-content");

        tabBtns.forEach((btn, index) => {
          btn.addEventListener("click", function () {
            // 모든 탭 버튼과 내용에서 active 클래스 제거
            tabBtns.forEach((b) => b.classList.remove("active"));
            tabContents.forEach((c) => c.classList.remove("active"));

            // 클릭된 탭 버튼과 해당 내용에 active 클래스 추가
            this.classList.add("active");
            tabContents[index].classList.add("active");
          });
        });
      });
    </script>
  </body>
</html>
