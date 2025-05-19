<!-- filepath: c:\Users\user\git\GreenTable\src\main\webapp\admin\farm-form.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>그린테이블 관리자 - 농가 등록</title>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/admin/css/admin-style.css"
    />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  </head>
  <body>
    <div class="admin-container">
      <!-- 사이드바 포함 -->
      <jsp:include page="common/admin-sidebar.jsp" />

      <!-- 메인 내용 -->
      <main class="admin-content">
        <!-- 상단 헤더 포함 -->
        <jsp:include page="common/admin-top-header.jsp">
          <jsp:param name="pageTitle" value="농가 등록" />
        </jsp:include>

        <form
          id="farmForm"
          action="${pageContext.request.contextPath}/front?key=farm&methodName=regist"
          method="post"
        >
          <div class="form-group">
            <label for="name">농가명 <span class="required">*</span></label>
            <input
              type="text"
              id="name"
              name="name"
              required
              class="form-control"
            />
          </div>

          <div class="form-group">
            <label for="description">설명</label>
            <textarea
              id="description"
              name="description"
              class="form-control"
              rows="5"
            ></textarea>
          </div>

          <div class="form-group">
            <label for="address">주소 <span class="required">*</span></label>
            <input
              type="text"
              id="address"
              name="address"
              required
              class="form-control"
            />
          </div>

          <div class="form-group">
            <label for="category">카테고리</label>
            <select id="category" name="category" class="form-control">
              <option value="일반">일반</option>
              <option value="채소">채소</option>
              <option value="과일">과일</option>
              <option value="곡물">곡물</option>
              <option value="축산물">축산물</option>
              <option value="유기농">유기농</option>
            </select>
          </div>

          <div class="form-group">
            <label for="farmImg">이미지 URL</label>
            <input
              type="text"
              id="farmImg"
              name="farmImg"
              class="form-control"
            />
            <small class="form-text text-muted"
              >이미지 URL을 입력하세요. (예: /images/farms/farm1.jpg)</small
            >
          </div>

          <div class="form-row">
            <div class="form-group col-md-6">
              <label for="latitude">위도</label>
              <input
                type="number"
                id="latitude"
                name="latitude"
                step="0.000001"
                class="form-control"
              />
            </div>
            <div class="form-group col-md-6">
              <label for="longitude">경도</label>
              <input
                type="number"
                id="longitude"
                name="longitude"
                step="0.000001"
                class="form-control"
              />
            </div>
          </div>

          <div class="form-group">
            <button type="submit" class="btn btn-primary">농가 등록</button>
            <a
              href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList"
              class="btn btn-secondary"
              >취소</a
            >
          </div>
        </form>
      </main>
    </div>

    <script>
      $(document).ready(function () {
        $("#farmForm").on("submit", function (e) {
          // 필수 필드 유효성 검사
          var name = $("#name").val().trim();
          var address = $("#address").val().trim();
          var latitude = $("#latitude").val();
          var longitude = $("#longitude").val();

          if (!name) {
            alert("농가명은 필수 입력 항목입니다.");
            $("#name").focus();
            e.preventDefault();
            return false;
          }

          if (!address) {
            alert("주소는 필수 입력 항목입니다.");
            $("#address").focus();
            e.preventDefault();
            return false;
          }

          // 위도 유효성 검사
          if (
            latitude &&
            (parseFloat(latitude) < -90 || parseFloat(latitude) > 90)
          ) {
            alert("위도는 -90에서 90 사이의 값이어야 합니다.");
            $("#latitude").focus();
            e.preventDefault();
            return false;
          }

          // 경도 유효성 검사
          if (
            longitude &&
            (parseFloat(longitude) < -180 || parseFloat(longitude) > 180)
          ) {
            alert("경도는 -180에서 180 사이의 값이어야 합니다.");
            $("#longitude").focus();
            e.preventDefault();
            return false;
          }
        });
      });
    </script>
  </body>
</html>
