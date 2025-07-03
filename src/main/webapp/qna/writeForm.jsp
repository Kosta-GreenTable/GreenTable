<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="site.greentable.util.ImageUtil" %>
<%@ page import="java.lang.System" %>
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
    <title>상품 문의 작성 | Green Table</title>
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
        <h1 class="page-title">상품 문의 작성</h1>
        <div class="product-info" id="productInfo">
          <div class="product-image">
            <c:choose>
              <c:when test="${not empty product.mainImage}">
                <img
                  src="${s3BaseUrl}/${product.mainImage}"
                  onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';"
                  alt="${product.name}"
                />
              </c:when>
              <c:otherwise>
                <img
                  src="${s3BaseUrl}/products/no-image.jpg"
                  alt="상품 이미지"
                />
              </c:otherwise>
            </c:choose>
          </div>
          <div class="product-details">
            <h3 class="product-name">
              <c:choose>
                <c:when test="${not empty product.name}">
                  ${product.name}
                </c:when>
                <c:otherwise> 상품 ID: ${param.productId} </c:otherwise>
              </c:choose>
            </h3>
          </div>
        </div>

        <form
          id="qnaForm"
          method="post"
          action="${pageContext.request.contextPath}/front?key=qna&methodName=writeQna"
        >
          <input type="hidden" name="productId" value="${param.productId}" />

          <div class="form-group">
            <label for="qnaTitle">제목</label>
            <input
              type="text"
              id="qnaTitle"
              name="title"
              placeholder="문의 제목을 입력해주세요."
              required
            />
          </div>
          <div class="form-group">
            <label for="qnaContent">문의 내용</label>
            <textarea
              id="qnaContent"
              name="content"
              rows="5"
              placeholder="문의 내용을 상세히 입력해주세요."
              required
            ></textarea>
            <p class="text-length">0/500자</p>
          </div>

          <div class="form-group qna-notice">
            <h3>문의 작성 안내</h3>
            <ul>
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
              <li>
                <i class="fas fa-info-circle"></i> 문의 답변은 영업일 기준 1~2일
                내에 등록됩니다.
              </li>
            </ul>
          </div>

          <div class="form-actions">
            <button type="button" class="cancel-btn" onclick="history.back()">
              취소
            </button>
            <button type="submit" class="submit-btn">등록하기</button>
          </div>
        </form>
      </section>
    </main>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <script>
      document.addEventListener("DOMContentLoaded", function () {
        // 글자수 카운트 기능
        const qnaContent = document.getElementById("qnaContent");
        const textLength = document.querySelector(".text-length");

        qnaContent.addEventListener("input", function () {
          const length = this.value.length;
          textLength.textContent = length + "/500자";

          if (length > 500) {
            textLength.style.color = "red";
          } else {
            textLength.style.color = "";
          }
        });

        // 폼 제출 전 유효성 검사
        const qnaForm = document.getElementById("qnaForm");
        qnaForm.addEventListener("submit", function (e) {
          const title = document.getElementById("qnaTitle").value.trim();
          const content = qnaContent.value.trim();

          if (title.length < 2) {
            e.preventDefault();
            alert("제목은 최소 2자 이상 입력해주세요.");
            return;
          }

          if (content.length < 10) {
            e.preventDefault();
            alert("내용은 최소 10자 이상 입력해주세요.");
            return;
          }

          if (content.length > 500) {
            e.preventDefault();
            alert("내용은 최대 500자까지 입력 가능합니다.");
            return;
          }
        });
      });
    </script>
  </body>
</html>
