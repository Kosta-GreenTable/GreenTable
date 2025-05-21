<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>상품 리뷰 | Green Table</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user/myreview.css" />
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
      <h1 class="page-title">상품 리뷰</h1>

      <div class="mypage-content">


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
              <button class="tab-btn active">작성 가능한 리뷰 (${writableReviewsCount})</button>
              <button class="tab-btn">작성한 리뷰 (${writtenReviewsCount})</button>
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
                            <p class="order-date">주문일자: ${review.orderDate}</p>
                            <h4 class="product-name">${review.productName}</h4>
                            <p class="product-option">${review.productOption}</p>
                            <div class="review-deadline">
                              <span class="deadline-text"
                                >리뷰 작성 기한: <b>${review.deadline}</b>까지</span
                              >
                            </div>
                          </div>
                        </div>
                        <div class="review-action">
                          <button class="btn-write-review" onclick="location.href='${pageContext.request.contextPath}/front?key=review&methodName=writeForm&productId=${review.productId}&orderDetailId=${review.orderDetailId}'">리뷰 작성</button>
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
                            <img
                              src="${review.productImage}"
                              alt="${review.productName}"
                            />
                          </div>
                          <div class="product-info">
                            <p class="review-date">작성일자: ${review.createdAt}</p>
                            <h4 class="product-name">${review.productName}</h4>
                            <p class="product-option">${review.productOption}</p>
                            <div class="rating">
                              <span class="rating-text">평점:</span>
                              <span class="stars">
                                <c:forEach begin="1" end="5" var="i">
                                  <c:choose>
                                    <c:when test="${i <= review.rating}">
                                      <i class="fas fa-star"></i>
                                    </c:when>
                                    <c:when test="${i > review.rating && i-0.5 <= review.rating}">
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
                                    src="${pageContext.request.contextPath}/upload/review/${image.realName}"
                                    alt="리뷰사진"
                                  />
                                </div>
                              </c:forEach>
                            </div>
                          </c:if>
                          <div class="review-actions">
                            <button class="btn-edit-review" onclick="location.href='${pageContext.request.contextPath}/front?key=review&methodName=updateForm&reviewId=${review.reviewId}'">수정</button>
                            <button class="btn-delete-review" onclick="if(confirm('정말 삭제하시겠습니까?')) location.href='${pageContext.request.contextPath}/front?key=review&methodName=deleteReview&reviewId=${review.reviewId}'">삭제</button>
                          </div>
                        </div>
                      </div>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </div>
            </div>

            <!-- 페이지네이션 -->
            <div class="pagination">
              <span class="page-info">${page}-${size} / ${total}</span>
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

    <!-- 리뷰 작성 모달 -->
    <div class="modal-background" id="reviewModal" style="display: none">
      <div class="modal-content">
        <div class="modal-header">
          <h3>리뷰 작성</h3>
          <button class="close-modal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="review-product-info">
            <div class="product-image">
              <img
                src=""
                alt="상품 이미지"
                id="modal-product-image"
              />
            </div>
            <div class="product-details">
              <h4 class="product-name" id="modal-product-name"></h4>
              <p class="product-option" id="modal-product-option"></p>
            </div>
          </div>
          <form id="reviewForm" method="post" action="${pageContext.request.contextPath}/front?key=review&methodName=writeReview" enctype="multipart/form-data">
            <input type="hidden" id="productId" name="productId" value="">
            <input type="hidden" id="orderDetailId" name="orderDetailId" value="">
            <div class="form-group">
              <label>평점</label>
              <div class="star-rating">
                <i class="far fa-star" data-rating="1"></i>
                <i class="far fa-star" data-rating="2"></i>
                <i class="far fa-star" data-rating="3"></i>
                <i class="far fa-star" data-rating="4"></i>
                <i class="far fa-star" data-rating="5"></i>
                <span class="rating-value">0점</span>
              </div>
              <input type="hidden" id="rating" name="rating" value="0">
            </div>
            <div class="form-group">
              <label for="reviewText">리뷰 내용</label>
              <textarea
                id="reviewText"
                name="content"
                placeholder="상품에 대한 솔직한 리뷰를 작성해주세요."
                rows="5"
                required
              ></textarea>
              <p class="text-length">0/1000자</p>
            </div>
            <div class="form-group">
              <label>포토 리뷰 (선택)</label>
              <div class="photo-upload-area">
                <div class="photo-upload-btn">
                  <i class="fas fa-plus"></i>
                  <input
                    type="file"
                    id="photoUpload"
                    name="reviewImages"
                    accept="image/*"
                    multiple
                  />
                </div>
                <div class="photo-upload-preview">
                  <!-- 업로드된 이미지 미리보기가 여기에 표시됩니다 -->
                </div>
              </div>
              <p class="photo-upload-info">
                * 최대 5장까지 업로드 가능합니다. (JPG, PNG 파일, 각 5MB 이하)
              </p>
            </div>
            <div class="form-group form-actions">
              <button type="button" class="cancel-btn">취소</button>
              <button type="submit" class="submit-btn">등록하기</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script src="${pageContext.request.contextPath}/js/user/myreview.js"></script>
  </body>
</html>