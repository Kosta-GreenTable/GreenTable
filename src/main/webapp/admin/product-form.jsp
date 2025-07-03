<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>그린테이블 관리자 - 상품 등록</title>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/admin/css/admin-style.css"
    />
  </head>
  <body>
    <div class="admin-container">
      <!-- 사이드바 포함 -->
      <jsp:include page="common/admin-sidebar.jsp" />

      <!-- 메인 내용 -->
      <main class="admin-content">
        <!-- 상단 헤더 포함 -->
        <jsp:include page="common/admin-top-header.jsp">
          <jsp:param name="pageTitle" value="상품 등록" />
        </jsp:include>

        <div class="product-form-container">
          <div class="form-navigation">
            <a
              href="${pageContext.request.contextPath}/front?key=admin&methodName=productList"
              class="back-link"
            >
              <i class="fas fa-arrow-left"></i> 상품 목록으로 돌아가기
            </a>
          </div>

          <form
            action="${pageContext.request.contextPath}/front?key=admin&methodName=productInsert"
            method="post"
            enctype="multipart/form-data"
            class="product-form"
            id="productForm"
          >
            <div class="form-section">
              <h3>기본 정보</h3>
              <div class="form-group">
                <label for="name">상품명 <span class="required">*</span></label>
                <input type="text" id="name" name="name" required />
              </div>
              <div class="form-group">
                <label for="subName">부제목</label>
                <input type="text" id="subName" name="subName" />
              </div>
              <div class="form-group">
                <label for="category"
                  >카테고리 <span class="required">*</span></label
                >
                <select id="category" name="category" required>
                  <option value="">카테고리 선택</option>
                  <option value="도시락">도시락</option>
                  <option value="샐러드">샐러드</option>
                  <option value="정기배송">정기배송</option>
                  <option value="베스트">베스트</option>
                </select>
              </div>
              <div class="form-group">
                <label for="price"
                  >가격 (원) <span class="required">*</span></label
                >
                <input type="number" id="price" name="price" min="0" required />
              </div>
              <div class="form-group">
                <label for="stock"
                  >재고 수량 <span class="required">*</span></label
                >
                <input type="number" id="stock" name="stock" min="0" required />
              </div>
              <div class="form-group">
                <label for="discountRate">할인율 (%)</label>
                <input
                  type="number"
                  id="discountRate"
                  name="discountRate"
                  min="0"
                  max="100"
                  value="0"
                />
              </div>
            </div>

            <div class="form-section">
              <h3>상품 상세 정보</h3>
              <div class="form-group">
                <label for="description">상품 설명</label>
                <textarea
                  id="description"
                  name="description"
                  rows="5"
                ></textarea>
              </div>
              <div class="form-group">
                <label for="ingredients">원재료</label>
                <input type="text" id="ingredients" name="ingredients" />
              </div>
              <div class="form-group">
                <label for="kcal">칼로리 (kcal)</label>
                <input type="number" id="kcal" name="kcal" min="0" value="0" />
              </div>
              <div class="form-group">
                <label for="amount">중량 (g)</label>
                <input
                  type="number"
                  id="amount"
                  name="amount"
                  min="0"
                  value="0"
                />
              </div>
              <div class="form-group">
                <label for="nutrition">보관 방법</label>
                <select id="nutrition" name="nutrition">
                  <option value="">보관 방법 선택</option>
                  <option value="냉장">냉장</option>
                  <option value="냉동">냉동</option>
                </select>
              </div>
            </div>

            <div class="form-section">
              <h3>상품 이미지</h3>
              <div class="image-upload-container">
                <div class="main-image-upload">
                  <h4>대표 이미지 <span class="required">*</span></h4>
                  <div class="image-preview" id="mainImagePreview">
                    <img
                      src="${s3BaseUrl}/products/no-image.jpg"
                      alt="대표 이미지 미리보기"
                    />
                  </div>
                  <input
                    type="file"
                    id="mainImage"
                    name="mainImage"
                    accept="image/*"
                    required
                  />
                  <label for="mainImage" class="image-upload-btn"
                    >이미지 선택</label
                  >
                  <p class="help-text">권장 크기: 500x500px, 최대 5MB</p>
                </div>

                <div class="additional-images">
                  <h4>추가 이미지</h4>
                  <div class="image-upload-grid">
                    <div class="image-item">
                      <div class="image-preview" id="additionalImagePreview1">
                        <img
                          src="${s3BaseUrl}/products/no-image.jpg"
                          alt="추가 이미지 1 미리보기"
                        />
                      </div>
                      <input
                        type="file"
                        id="image1"
                        name="image1"
                        accept="image/*"
                      />
                      <label for="image1" class="image-upload-btn"
                        >이미지 선택</label
                      >
                    </div>
                    <div class="image-item">
                      <div class="image-preview" id="additionalImagePreview2">
                        <img
                          src="${s3BaseUrl}/products/no-image.jpg"
                          alt="추가 이미지 2 미리보기"
                        />
                      </div>
                      <input
                        type="file"
                        id="image2"
                        name="image2"
                        accept="image/*"
                      />
                      <label for="image2" class="image-upload-btn"
                        >이미지 선택</label
                      >
                    </div>
                    <div class="image-item">
                      <div class="image-preview" id="additionalImagePreview3">
                        <img
                          src="${s3BaseUrl}/products/no-image.jpg"
                          alt="추가 이미지 3 미리보기"
                        />
                      </div>
                      <input
                        type="file"
                        id="image3"
                        name="image3"
                        accept="image/*"
                      />
                      <label for="image3" class="image-upload-btn"
                        >이미지 선택</label
                      >
                    </div>
                  </div>
                  <p class="help-text">
                    각 이미지 권장 크기: 500x500px, 최대 5MB
                  </p>
                </div>
              </div>
            </div>

            <div class="form-actions">
              <button type="submit" class="btn-primary">상품 등록</button>
              <button type="button" id="cancelBtn" class="btn-secondary">
                취소
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>

    <script src="${pageContext.request.contextPath}/admin/js/admin-script.js"></script>
    <script src="${pageContext.request.contextPath}/admin/js/product-form.js"></script>

    <script>
      document.addEventListener("DOMContentLoaded", function () {
        // 취소 버튼 이벤트
        const cancelBtn = document.getElementById("cancelBtn");
        if (cancelBtn) {
          cancelBtn.addEventListener("click", function () {
            if (
              confirm("작성 중인 내용이 사라집니다. 정말 취소하시겠습니까?")
            ) {
              window.location.href =
                "${pageContext.request.contextPath}/front?key=admin&methodName=productList";
            }
          });
        }

        // 이미지 미리보기 기능
        function setupImagePreview(inputId, previewId) {
          const input = document.getElementById(inputId);
          const preview = document.getElementById(previewId);

          if (input && preview) {
            input.addEventListener("change", function (e) {
              const file = e.target.files[0];
              if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                  const img = preview.querySelector("img");
                  if (img) {
                    img.src = e.target.result;
                  }
                };
                reader.readAsDataURL(file);
              }
            });
          }
        }

        // 모든 이미지 입력에 대해 미리보기 설정
        setupImagePreview("mainImage", "mainImagePreview");
        setupImagePreview("image1", "additionalImagePreview1");
        setupImagePreview("image2", "additionalImagePreview2");
        setupImagePreview("image3", "additionalImagePreview3");

        // 폼 유효성 검사
        const form = document.getElementById("productForm");
        if (form) {
          form.addEventListener("submit", function (e) {
            const name = document.getElementById("name").value.trim();
            const category = document.getElementById("category").value;
            const price = document.getElementById("price").value;
            const stock = document.getElementById("stock").value;

            if (!name) {
              alert("상품명을 입력해주세요.");
              document.getElementById("name").focus();
              e.preventDefault();
              return false;
            }

            if (!category) {
              alert("카테고리를 선택해주세요.");
              document.getElementById("category").focus();
              e.preventDefault();
              return false;
            }

            if (!price || price <= 0) {
              alert("올바른 가격을 입력해주세요.");
              document.getElementById("price").focus();
              e.preventDefault();
              return false;
            }

            if (!stock || stock < 0) {
              alert("올바른 재고 수량을 입력해주세요.");
              document.getElementById("stock").focus();
              e.preventDefault();
              return false;
            }

            // 폼 제출 확인
            if (!confirm("상품을 등록하시겠습니까?")) {
              e.preventDefault();
              return false;
            }
          });
        }

        // 숫자 입력 필드 유효성 검사
        const numberInputs = document.querySelectorAll('input[type="number"]');
        numberInputs.forEach((input) => {
          input.addEventListener("input", function () {
            if (this.value < 0) {
              this.value = 0;
            }
          });
        });
      });
    </script>
  </body>
</html>
