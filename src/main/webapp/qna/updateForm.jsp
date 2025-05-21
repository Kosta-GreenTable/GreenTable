<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>상품 문의 수정 | Green Table</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/styles.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/qna-form.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />

    <main class="container">
      <section class="qna-form-section">
        <h1 class="page-title">상품 문의 수정</h1>

        <div class="product-info">
          <div class="product-image">
            <img src="${product.mainImage}" alt="${product.name}" />
          </div>
          <div class="product-details">
            <h3 class="product-name">${product.name}</h3>
          </div>
        </div>

        <form
          id="qnaForm"
          method="post"
          action="${pageContext.request.contextPath}/front?key=qna&methodName=updateQna"
        >
          <input type="hidden" name="qnaId" value="${qna.qnaId}" />
          <input type="hidden" name="productId" value="${qna.productId}" />

          <div class="form-group">
            <label for="qnaTitle">제목</label>
            <input
              type="text"
              id="qnaTitle"
              name="title"
              value="${qna.title}"
              required
            />
          </div>
          <div class="form-group">
            <label for="qnaContent">문의 내용</label>
            <textarea id="qnaContent" name="content" rows="5" required>
${qna.content}</textarea
            >
            <p class="text-length">${qna.content.length()}/500자</p>
          </div>

          <div class="form-group qna-notice">
            <h3>문의 수정 안내</h3>
            <ul>
              <li>
                <i class="fas fa-exclamation-triangle"></i> 이미 답변이 등록된
                문의는 수정이 불가능합니다.
              </li>
              <li>
                <i class="fas fa-info-circle"></i> 상품에 대한 문의사항을
                작성해주세요.
              </li>
              <li>
                <i class="fas fa-info-circle"></i> 주문/배송/취소/교환/반품 관련
                문의는 '고객센터 > 1:1 문의'를 이용해주세요.
              </li>
              <li>
                <i class="fas fa-info-circle"></i> 문의 내용에 개인정보(주소,
                전화번호, 이메일 등)가 포함되지 않도록 유의해 주세요.
              </li>
            </ul>
          </div>

          <div class="form-actions">
            <button type="button" class="cancel-btn" onclick="history.back()">
              취소
            </button>
            <button type="submit" class="submit-btn">수정완료</button>
          </div>
        </form>
      </section>
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/qna-form.js"></script>
  </body>
</html>
