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
    <title>상품 문의 | Green Table</title>
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
      href="${pageContext.request.contextPath}/css/user/myqna.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
  </head>
  <body>
    <!-- 헤더 인클루드 -->
    <jsp:include page="../common/header.jsp" />
    <!-- 메인 컨텐츠 - 상품 문의 섹션 -->
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
              <li>
                <a href="${path}/front?key=review&methodName=myReviews"
                  >상품 리뷰</a
                >
              </li>
              <li class="active">
                <a href="${path}/front?key=qna&methodName=myQnas">상품 문의</a>
              </li>
            </ul>
            <h3>나의 계정설정</h3>
            <ul>
              <li><a href="${path}/user/myinfo.jsp">회원정보 수정</a></li>
            </ul>
          </nav>
        </div>

        <!-- 상품 문의 메인 내용 -->
        <div class="mypage-main">
          <!-- 문의 필터링 옵션 -->
          <section class="qna-filter">
            <div class="filter-container">
              <div class="filter-group">
                <label>조회 기간</label>
                <div class="period-selector">
                  <button class="period-btn active" data-period="1">
                    1개월
                  </button>
                  <button class="period-btn" data-period="3">3개월</button>
                  <button class="period-btn" data-period="6">6개월</button>
                  <button class="period-btn" data-period="12">1년</button>
                </div>
              </div>
              <div class="filter-group">
                <label>답변 상태</label>
                <select name="answerStatus" id="answerStatus">
                  <option value="all">전체</option>
                  <option value="N">답변 대기</option>
                  <option value="Y">답변 완료</option>
                </select>
              </div>
            </div>
          </section>

          <!-- 문의 목록 -->
          <section class="qna-list-section">
            <table class="qna-table">
              <thead>
                <tr>
                  <th>번호</th>
                  <th>상품정보</th>
                  <th>제목</th>
                  <th>작성일</th>
                  <th>상태</th>
                </tr>
              </thead>
              <tbody>
                <c:choose>
                  <c:when test="${empty qnaList}">
                    <!-- 문의 내역이 없는 경우 -->
                    <tr class="no-data">
                      <td colspan="5">문의 내역이 없습니다.</td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <!-- 문의 내역이 있는 경우 -->
                    <c:forEach var="qna" items="${qnaList}" varStatus="status">
                      <tr class="qna-item" data-qna-id="${qna.qnaId}">
                        <td class="qna-num">${status.count}</td>
                        <td class="qna-product">
                          <div class="product-info-cell">
                            <c:choose>
                              <c:when test="${qna.productImage.startsWith('products/')}">
                                <!-- S3 이미지 URL -->
                                <img
                                  src="${s3BaseUrl}/${qna.productImage}"
                                  alt="${qna.productName}"
                                  onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';"
                                />
                              </c:when>
                              <c:otherwise>
                                <!-- 기존 로컬 이미지 -->
                                <img
                                  src="${s3BaseUrl}/${qna.productImage}"
                                  alt="${qna.productName}"
                                  onerror="this.onerror=null; this.src='${s3BaseUrl}/products/no-image.jpg';"
                                />
                              </c:otherwise>
                            </c:choose>
                            <span>${qna.productName}</span>
                          </div>
                        </td>
                        <td class="qna-title">${qna.title}</td>
                        <td class="qna-date">
                          <fmt:formatDate
                            value="${qna.createdAt}"
                            pattern="yyyy.MM.dd"
                          />
                        </td>
                        <td
                          class="qna-status ${qna.isAnswered eq 'Y' ? 'completed' : 'waiting'}"
                        >
                          ${qna.isAnswered eq 'Y' ? '답변 완료' : '답변 대기'}
                        </td>
                      </tr>
                      <tr class="qna-content" style="display: none">
                        <td colspan="5">
                          <div class="question-content">
                            <div class="question-header">
                              <span class="question-label">Q</span>
                              <span class="question-title">${qna.title}</span>
                            </div>
                            <div class="question-body">
                              <p>${qna.content}</p>
                            </div>
                            <div class="question-footer">
                              <div class="question-actions">
                                <c:if test="${qna.isAnswered ne 'Y'}">
                                  <button
                                    class="btn-edit"
                                    onclick="location.href='${pageContext.request.contextPath}/front?key=qna&methodName=updateForm&qnaId=${qna.qnaId}'"
                                  >
                                    수정
                                  </button>
                                  <button
                                    class="btn-delete"
                                    onclick="if(confirm('정말 삭제하시겠습니까?')) location.href='${pageContext.request.contextPath}/front?key=qna&methodName=deleteQna&qnaId=${qna.qnaId}&productId=${qna.productId}'"
                                  >
                                    삭제
                                  </button>
                                </c:if>
                                <c:if test="${qna.isAnswered eq 'Y'}">
                                  <span class="no-edit-msg"
                                    >답변이 등록된 문의는 수정/삭제할 수
                                    없습니다.</span
                                  >
                                </c:if>
                              </div>
                            </div>
                          </div>
                          <c:if test="${qna.isAnswered eq 'Y'}">
                            <div class="answer-content">
                              <div class="answer-header">
                                <span class="answer-label">A</span>
                                <span class="answer-title"
                                  >${qna.title}에 대한 답변입니다.</span
                                >
                              </div>
                              <div class="answer-body">
                                <p>${qna.answer}</p>
                              </div>
                              <div class="answer-footer">
                                <div class="answer-date">
                                  <span
                                    >답변일:
                                    <fmt:formatDate
                                      value="${qna.answeredAt}"
                                      pattern="yyyy.MM.dd"
                                  /></span>
                                </div>
                              </div>
                            </div>
                          </c:if>
                        </td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </tbody>
            </table>

            <!-- 페이지네이션 -->
            <div class="pagination">
              <span class="page-info">${page}-${size} / ${total}</span>
            </div>
          </section>

          <!-- 문의 안내사항 -->
          <section class="info-section">
            <h3 class="info-title">문의 안내</h3>
            <ul class="info-list">
              <li>
                <i class="fas fa-check"></i> 상품에 대한 문의는 '상품
                문의하기'를 통해 남겨주세요.
              </li>
              <li>
                <i class="fas fa-check"></i> 주문/배송/취소/교환/반품 관련
                문의는 '고객센터 > 1:1 문의'로 남겨주세요.
              </li>
              <li>
                <i class="fas fa-check"></i> 문의 내용 작성 시 개인정보(주소,
                전화번호, 이메일 등)가 포함되지 않도록 유의해 주세요.
              </li>
              <li>
                <i class="fas fa-check"></i> 상품 및 상품 문의와 관계없는 글,
                양도, 광고성, 욕설, 비방, 도배 등의 글은 예고 없이 삭제될 수
                있습니다.
              </li>
              <li>
                <i class="fas fa-check"></i> 답변은 영업일 기준 2~3일 내에
                등록됩니다.
              </li>
            </ul>
          </section>
        </div>
      </div>
    </main>

    <!-- 상품 문의 작성 모달 -->
    <div class="modal-background" id="questionModal" style="display: none">
      <div class="modal-content">
        <div class="modal-header">
          <h3>상품 문의하기</h3>
          <button class="close-modal" onclick="closeModal()">&times;</button>
        </div>
        <div class="modal-body">
          <form
            id="questionForm"
            method="post"
            action="${pageContext.request.contextPath}/front?key=qna&methodName=writeQna"
          >
            <div class="form-group">
              <label for="productSelect"
                >상품 선택<span class="required">*</span></label
              >
              <select id="productSelect" name="productId" required>
                <option value="">상품을 선택해주세요.</option>
                <c:forEach var="product" items="${productList}">
                  <option value="${product.productId}">
                    ${product.productName}
                  </option>
                </c:forEach>
              </select>
            </div>
            <div class="form-group">
              <label for="questionTitle"
                >제목<span class="required">*</span></label
              >
              <input
                type="text"
                id="questionTitle"
                name="title"
                placeholder="문의 제목을 입력해주세요."
                maxlength="50"
                required
              />
            </div>
            <div class="form-group">
              <label for="questionText"
                >문의 내용<span class="required">*</span></label
              >
              <textarea
                id="questionText"
                name="content"
                placeholder="문의하실 내용을 입력해주세요. (최대 500자)"
                rows="5"
                maxlength="500"
                required
              ></textarea>
              <p class="text-length">0/500자</p>
            </div>
            <div class="form-group">
              <div class="checkbox-container">
                <input
                  type="checkbox"
                  id="privateQuestion"
                  name="isPrivate"
                  value="Y"
                />
                <label for="privateQuestion">비밀글로 문의하기</label>
                <p class="help-text">
                  비밀글은 고객님과 관리자만 확인할 수 있습니다.
                </p>
              </div>
            </div>
            <div class="form-group form-actions">
              <button type="button" class="cancel-btn" onclick="closeModal()">
                취소
              </button>
              <button type="submit" class="submit-btn">등록하기</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 푸터 인클루드 -->
    <jsp:include page="../common/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script src="${pageContext.request.contextPath}/js/user/myqna.js"></script>
    <script>
      // QnA 항목 클릭 시 내용 표시/숨김 토글
      document.addEventListener("DOMContentLoaded", function () {
        // QnA 클릭 이벤트 설정
        const qnaItems = document.querySelectorAll(".qna-item");

        qnaItems.forEach((item) => {
          item.addEventListener("click", function () {
            // 현재 열려있는 다른 QnA 닫기
            const openContent = document.querySelector(
              '.qna-content[style="display: table-row;"]'
            );
            if (openContent && openContent !== this.nextElementSibling) {
              openContent.style.display = "none";
            }

            // 현재 클릭한 QnA 열기/닫기
            const content = this.nextElementSibling;
            if (
              content.style.display === "none" ||
              content.style.display === ""
            ) {
              content.style.display = "table-row";
            } else {
              content.style.display = "none";
            }
          });
        });

        // 문의 내역이 없는지 확인하는 로직 수정
        const noDataRow = document.querySelector(".no-data");
        const qnaListItems = document.querySelectorAll(".qna-item");

        if (qnaListItems.length > 0 && noDataRow) {
          noDataRow.style.display = "none";
        }

        // 필터링 기능
        const periodBtns = document.querySelectorAll(".period-btn");
        const answerStatus = document.getElementById("answerStatus");

        // 현재 설정된 필터값 반영
        const urlParams = new URLSearchParams(window.location.search);
        const currentPeriod = urlParams.get("period") || "1";
        const currentStatus = urlParams.get("status") || "all";

        // 기간 버튼 초기 상태 설정
        periodBtns.forEach((btn) => {
          if (btn.getAttribute("data-period") === currentPeriod) {
            btn.classList.add("active");
          } else {
            btn.classList.remove("active");
          }
        });

        // 답변 상태 드롭다운 초기 상태 설정
        if (answerStatus) {
          answerStatus.value = currentStatus;
        }

        // 필터링 버튼 이벤트 리스너
        periodBtns.forEach((btn) => {
          btn.addEventListener("click", function () {
            periodBtns.forEach((b) => b.classList.remove("active"));
            this.classList.add("active");
            filterQna();
          });
        });

        if (answerStatus) {
          answerStatus.addEventListener("change", filterQna);
        }

        // 문의 내용 글자 수 카운트
        const questionText = document.getElementById("questionText");
        const textLength = document.querySelector(".text-length");

        if (questionText && textLength) {
          questionText.addEventListener("input", function () {
            const length = this.value.length;
            textLength.textContent = `${length}/500자`;

            // 500자 초과 방지
            if (length > 500) {
              this.value = this.value.substring(0, 500);
              textLength.textContent = "500/500자";
            }
          });
        }

        // QnA 폼 제출 전 유효성 검증
        const questionForm = document.getElementById("questionForm");
        if (questionForm) {
          questionForm.addEventListener("submit", function (e) {
            const productSelect = document.getElementById("productSelect");
            const title = document.getElementById("questionTitle");
            const content = document.getElementById("questionText");

            if (!productSelect.value) {
              e.preventDefault();
              alert("상품을 선택해주세요.");
              productSelect.focus();
              return;
            }

            if (!title.value.trim()) {
              e.preventDefault();
              alert("제목을 입력해주세요.");
              title.focus();
              return;
            }

            if (!content.value.trim()) {
              e.preventDefault();
              alert("문의 내용을 입력해주세요.");
              content.focus();
              return;
            }
          });
        }

        function filterQna() {
          const period = document
            .querySelector(".period-btn.active")
            .getAttribute("data-period");
          const status = answerStatus.value;

          location.href =
            "${pageContext.request.contextPath}/front?key=qna&methodName=myQnas&period=" +
            period +
            "&status=" +
            status;
        }
      });

      function openModal() {
        document.getElementById("questionModal").style.display = "block";
        // 모달 열릴 때 폼 초기화
        document.getElementById("questionForm").reset();
        document.querySelector(".text-length").textContent = "0/500자";
      }

      function closeModal() {
        document.getElementById("questionModal").style.display = "none";
      }
    </script>
  </body>
</html>
