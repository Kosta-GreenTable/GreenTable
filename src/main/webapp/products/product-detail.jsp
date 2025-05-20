<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>그린테이블 - ${product.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/styles.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
	  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/products/product-detail.css" />    
    <script>
      // 컨텍스트 경로를 JavaScript 변수로 설정
      var contextPath = "${pageContext.request.contextPath}";
      
      var userId = "${sessionScope.userId != null ? sessionScope.userId : '0'}";
      
      // 가격 정보를 JavaScript 변수로 설정
      var originalPrice = <c:out value="${product.price}"/>; // 원래 가격
      var discountRate = <c:out value="${product.discountRate > 0 ? product.discountRate : 0}"/>; // 할인율
      var finalPrice = <c:out value="${product.discountRate > 0 ? product.price * (100 - product.discountRate) / 100 : product.price}"/>; // 최종 가격 (할인 적용)
      
      // 재고 정보
      var maxStock = <c:out value="${product.stock}"/>;
    </script>
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />

    <!-- 상품 이미지 및 정보 섹션 -->
    <section class="product-info-container">
      <div class="product-image-container">
        <button class="image-navigation left">&lt;</button>
        <c:choose>
          <c:when test="${not empty productImages}">
            <c:forEach var="image" items="${productImages}" varStatus="status">
              <img
                class="product-image ${status.index == 0 ? '' : 'hidden'}"
                src="${pageContext.request.contextPath}/assets/images/products/${image.imageName}"
                alt="${product.name} 이미지 ${status.index + 1}"
                ${status.index > 0 ? 'style="display: none"' : ''}
                onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.jpg';"
              />
            </c:forEach>
          </c:when>
          <c:otherwise>
            <img
              class="product-image"
              src="${pageContext.request.contextPath}/assets/images/no-image.jpg"
              alt="${product.name}"
            />
          </c:otherwise>
        </c:choose>
        <button class="image-navigation right">&gt;</button>
        <div class="image-counter">1 / <c:out value="${productImages.size() > 0 ? productImages.size() : 1}" /></div>
      </div>
      
      <div class="product-info">
        <h1>${product.name}</h1>
        <div class="price">
          <c:choose>
            <c:when test="${product.discountRate > 0}">
              <div class="original-price-line">
                <span class="original-price" style="text-decoration: line-through; color: #888;"><fmt:formatNumber value="${product.price}" pattern="#,###" />원</span>
              </div>
              <div class="discount-price-line">
                <span class="discount-rate">${product.discountRate}%</span>
                <span class="final-price"><fmt:formatNumber value="${product.price * (100 - product.discountRate) / 100}" pattern="#,###" />원</span>
              </div>
            </c:when>
            <c:otherwise>
              <span class="final-price"><fmt:formatNumber value="${product.price}" pattern="#,###" />원</span>
            </c:otherwise>
          </c:choose>
        </div>
        <div class="details">
          <table>
            <tr>
              <th>배송정보</th>
              <td>주문 시 오늘 출고 (평일 12:00 이전 주문 기준)</td>
            </tr>
            <tr>
              <th>배송비</th>
              <td>3,500원 (50,000원 이상 무료 배송)</td>
            </tr>
            <tr>
              <th>중량</th>
              <td>${productDetail.amount}g</td>
            </tr>
            <tr>
              <th>칼로리</th>
              <td>${productDetail.kcal}Kcal</td>
            </tr>
            <tr>
              <th>재고</th>
              <td><span id="stock-quantity">${product.stock}</span>개</td>
            </tr>
          </table>
        </div>
        
        <div class="quantity-selector">
          <span>수량</span>
          <div class="quantity-controls">
            <button id="decrease-quantity">-</button>
            <input type="number" id="quantity" value="1" min="1" max="${product.stock}" />
            <button id="increase-quantity">+</button>
          </div>
          <div class="stock-alert"></div>
        </div>
        
        <!-- 재고 0인 경우 품절 메시지 표시 -->
        <c:if test="${product.stock <= 0}">
          <div class="sold-out-message">품절된 상품입니다.</div>
        </c:if>
        
        <div class="total-price">
          <span>총 상품 금액</span>
          <span id="total-price-amount">
            <c:choose>
              <c:when test="${product.discountRate > 0}">
                <fmt:formatNumber value="${product.price * (100 - product.discountRate) / 100}" pattern="#,###" />원
              </c:when>
              <c:otherwise>
                <fmt:formatNumber value="${product.price}" pattern="#,###" />원
              </c:otherwise>
            </c:choose>
          </span>
        </div>
        
        <div class="button-container">
          <button class="add-to-cart" data-product-id="${product.productId}" ${product.stock <= 0 ? 'disabled' : ''}>장바구니 담기</button>
          <button class="buy-now" data-product-id="${product.productId}" ${product.stock <= 0 ? 'disabled' : ''}>바로 구매하기</button>
        </div>
        
        <!-- 상품 ID 숨김 필드 (JS에서 사용) -->
        <input type="hidden" name="productId" value="${product.productId}">
        <input type="hidden" name="productPrice" value="${product.discountRate > 0 ? product.price * (100 - product.discountRate) / 100 : product.price}">
      </div>
    </section>
    
    <!-- 리뷰 섹션 -->
    <section class="review-section">
      <h2 class="review-title">고객 리뷰 (${product.reviewCount})</h2>
      <c:if test="${not empty reviewImages}">
        <div class="review-slider-container">
          <button class="review-navigation left">&lt;</button>
          <div class="review-images">
            <c:forEach var="image" items="${reviewImages}">
              <img src="${pageContext.request.contextPath}/assets/images/reviews/${image.imageName}" alt="리뷰 이미지" 
                   onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.jpg';" />
            </c:forEach>
          </div>
          <button class="review-navigation right">&gt;</button>
        </div>
      </c:if>
    </section>

    <!-- 상세 설명 섹션 -->
    <section class="detail-section">
      <div class="tabs">
        <button class="active">상품정보</button>
        <button>상품리뷰</button>
        <button>상품문의</button>
        <button>배송/환불안내</button>
      </div>
      
      <div class="tab-content">
        <h3>${product.name}</h3>
        <p>${productDetail.description}</p>
        <div class="product-details-image">
          <c:if test="${not empty productImages && productImages.size() > 0}">
            <c:forEach var="image" items="${productImages}">
              <div class="detail-image">
                <img src="${pageContext.request.contextPath}/assets/images/products/${image.imageName}" 
                     alt="상품 상세 이미지" 
                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.jpg';" />
              </div>
            </c:forEach>
          </c:if>
        </div>
        <div class="product-features">
          <h4>제품 특징</h4>
          <ul>
            <c:if test="${not empty productDetail.ingredients}">
              <li>${productDetail.ingredients}</li>
            </c:if>
          </ul>
        </div>
        <div class="nutrition-info">
          <h4>영양 정보</h4>
          <table>
            <tr>
              <th>열량</th>
              <td>${productDetail.kcal}Kcal</td>
            </tr>
            <tr>
              <th>영양성분</th>
              <td>${productDetail.nutrition}</td>
            </tr>
          </table>
        </div>
      </div>
      
      <div class="tab-content" style="display: none">
        <div class="review-list">
          <c:forEach var="review" items="${reviews}">
            <div class="review-item">
              <div class="review-header">
                <span class="reviewer-name">${review.userName}</span>
                <div class="rating">
                  <c:forEach begin="1" end="5" var="i">
                    <c:choose>
                      <c:when test="${i <= review.rating}">★</c:when>
                      <c:otherwise>☆</c:otherwise>
                    </c:choose>
                  </c:forEach>
                </div>
                <span class="review-date">${review.createDate}</span>
              </div>
              <div class="review-content">
                <p>${review.content}</p>
                <c:if test="${not empty review.imageName}">
                  <div class="review-image">
                    <img src="${pageContext.request.contextPath}/assets/images/reviews/${review.imageName}" 
                         alt="리뷰 이미지" 
                         onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.jpg';" />
                  </div>
                </c:if>
              </div>
            </div>
          </c:forEach>
          
          <c:if test="${empty reviews}">
            <p class="no-reviews">아직 등록된 리뷰가 없습니다.</p>
          </c:if>
          <div class="write-review">
            <button onclick="location.href='${pageContext.request.contextPath}/front?key=review&methodName=writeForm&productId=${product.productId}'">리뷰 작성하기</button>
          </div>
        </div>
      </div>
      
      <div class="tab-content" style="display: none">
        <div class="qna-list">
          <c:forEach var="qna" items="${qnaList}">
            <div class="qna-item">
              <div class="question">
                <span class="question-label">Q</span>
                <div class="question-content">
                  <div class="question-header">
                    <span class="questioner-name">${qna.userName}</span>
                    <span class="question-date">${qna.questionDate}</span>
                  </div>
                  <p>${qna.question}</p>
                </div>
              </div>
              <c:if test="${not empty qna.answer}">
                <div class="answer">
                  <span class="answer-label">A</span>
                  <div class="answer-content">
                    <div class="answer-header">
                      <span class="answerer-name">그린테이블</span>
                      <span class="answer-date">${qna.answerDate}</span>
                    </div>
                    <p>${qna.answer}</p>
                  </div>
                </div>
              </c:if>
            </div>
          </c:forEach>
          
          <c:if test="${empty qnaList}">
            <p class="no-qna">아직 등록된 문의가 없습니다.</p>
          </c:if>
          <div class="write-question">
            <button onclick="location.href='${pageContext.request.contextPath}/front?key=qna&methodName=writeForm&productId=${product.productId}'">문의하기</button>
          </div>
        </div>
      </div>
      
      <div class="tab-content" style="display: none">
        <div class="delivery-info">
          <h3>배송 안내</h3>
          <ul>
            <li>배송 방법: 택배</li>
            <li>배송 지역: 전국 (일부 도서산간 지역 제외)</li>
            <li>배송 비용: 3,000원 (40,000원 이상 구매 시 무료배송)</li>
            <li>배송 기간: 주문 완료 후 1~2일 이내 배송 (주말, 공휴일 제외)</li>
            <li>새벽 배송: 수도권 지역 새벽 배송 가능 (전날 오후 6시까지 주문 시)</li>
          </ul>
        </div>
        <div class="refund-info">
          <h3>교환 및 반품 안내</h3>
          <ul>
            <li>신선식품의 특성상 단순 변심에 의한 교환/반품은 불가합니다.</li>
            <li>제품 하자, 오배송의 경우 수령 후 24시간 이내 고객센터로 연락 주시면 교환/환불 가능합니다.</li>
            <li>식품 관련 문제 발생 시 사진과 함께 고객센터로 문의해 주세요.</li>
          </ul>
        </div>
      </div>
    </section>

    <!-- 추천 상품 섹션 -->
    <section class="recommended-products">
      <h2>함께 구매하면 좋은 상품</h2>
      <div class="product-list">
        <c:forEach var="recommended" items="${recommendedProducts}">
          <div class="product-card" onclick="location.href='${pageContext.request.contextPath}/front?key=product&methodName=detail&productId=${recommended.productId}'">
            <img src="${pageContext.request.contextPath}/assets/images/products/${recommended.mainImageName}" 
                alt="${recommended.name}" 
                onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/images/no-image.jpg';" />
            <h3>${recommended.name}</h3>
            
            <c:choose>
              <c:when test="${recommended.discountRate > 0}">
                <p class="product-price">
                  <span class="original-price"><fmt:formatNumber value="${recommended.price}" pattern="#,###" />원</span>
                  <span class="discount-rate">${recommended.discountRate}%</span>
                  <span class="final-price"><fmt:formatNumber value="${recommended.price * (100 - recommended.discountRate) / 100}" pattern="#,###" />원</span>
                </p>
              </c:when>
              <c:otherwise>
                <p class="product-price"><fmt:formatNumber value="${recommended.price}" pattern="#,###" />원</p>
              </c:otherwise>
            </c:choose>
            
            <button class="quick-add" data-product-id="${recommended.productId}" onclick="event.stopPropagation();">
              <i class="fas fa-shopping-cart"></i> 장바구니 담기
            </button>
          </div>
        </c:forEach>
        
        <!-- 추천 상품이 없는 경우 -->
        <c:if test="${empty recommendedProducts}">
          <div style="grid-column: 1 / -1; text-align: center; padding: 30px; color: #999;">
            <i class="fas fa-info-circle"></i> 추천 상품이 없습니다.
          </div>
        </c:if>
      </div>
    </section>
    
    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />
    <!-- 자바스크립트 -->
    <script src="${pageContext.request.contextPath}/js/product-detail.js"></script>
  </body>
</html>