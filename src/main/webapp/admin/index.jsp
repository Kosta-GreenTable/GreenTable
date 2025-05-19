<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>그린테이블 관리자</title>
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
          <jsp:param name="pageTitle" value="대시보드" />
        </jsp:include>

        <div class="dashboard">
          <div class="dashboard-stats">
            <div class="stat-card">
              <i class="fas fa-users stat-icon"></i>
              <div class="stat-content">
                <h3>회원 수</h3>
                <p class="stat-number">1,234</p>
                <p class="stat-label">전월 대비 +5.2%</p>
              </div>
            </div>
            <div class="stat-card">
              <i class="fas fa-shopping-cart stat-icon"></i>
              <div class="stat-content">
                <h3>주문 수</h3>
                <p class="stat-number">568</p>
                <p class="stat-label">전월 대비 +2.8%</p>
              </div>
            </div>
            <div class="stat-card">
              <i class="fas fa-won-sign stat-icon"></i>
              <div class="stat-content">
                <h3>매출액</h3>
                <p class="stat-number">15,670,000원</p>
                <p class="stat-label">전월 대비 +8.1%</p>
              </div>
            </div>
            <div class="stat-card">
              <i class="fas fa-box stat-icon"></i>
              <div class="stat-content">
                <h3>상품 수</h3>
                <p class="stat-number">85</p>
                <p class="stat-label">신규 상품 +3</p>
              </div>
            </div>
          </div>

          <div class="dashboard-charts">
            <div class="chart">
              <h3>일별 주문 통계</h3>
              <div class="chart-placeholder">
                <img
                  src="https://via.placeholder.com/600x300?text=일별+주문+통계+차트"
                  alt="일별 주문 통계 차트"
                />
              </div>
            </div>
            <div class="chart">
              <h3>카테고리별 판매 비율</h3>
              <div class="chart-placeholder">
                <img
                  src="https://via.placeholder.com/600x300?text=카테고리별+판매+비율+차트"
                  alt="카테고리별 판매 비율 차트"
                />
              </div>
            </div>
          </div>

          <div class="dashboard-recent">
            <div class="recent-orders">
              <h3>최근 주문</h3>
              <table class="admin-table">
                <thead>
                  <tr>
                    <th>주문번호</th>
                    <th>고객명</th>
                    <th>주문일자</th>
                    <th>금액</th>
                    <th>상태</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>ORD-2023001</td>
                    <td>홍길동</td>
                    <td>2023-05-13</td>
                    <td>54,000원</td>
                    <td>
                      <span class="status-badge completed">배송완료</span>
                    </td>
                  </tr>
                  <tr>
                    <td>ORD-2023002</td>
                    <td>김철수</td>
                    <td>2023-05-12</td>
                    <td>32,000원</td>
                    <td>
                      <span class="status-badge in-progress">배송중</span>
                    </td>
                  </tr>
                  <tr>
                    <td>ORD-2023003</td>
                    <td>이영희</td>
                    <td>2023-05-12</td>
                    <td>78,500원</td>
                    <td>
                      <span class="status-badge preparing">배송준비중</span>
                    </td>
                  </tr>
                  <tr>
                    <td>ORD-2023004</td>
                    <td>박민준</td>
                    <td>2023-05-11</td>
                    <td>128,000원</td>
                    <td>
                      <span class="status-badge completed">배송완료</span>
                    </td>
                  </tr>
                  <tr>
                    <td>ORD-2023005</td>
                    <td>최지호</td>
                    <td>2023-05-11</td>
                    <td>45,000원</td>
                    <td>
                      <span class="status-badge completed">배송완료</span>
                    </td>
                  </tr>
                </tbody>
              </table>
              <a
                href="${pageContext.request.contextPath}/front?key=admin&methodName=orderList"
                class="more-link"
                >더보기 <i class="fas fa-angle-right"></i
              ></a>
            </div>

            <div class="recent-products">
              <h3>인기 상품</h3>
              <ul class="product-list">
                <li class="product-item">
                  <img
                    src="https://via.placeholder.com/50x50?text=상품1"
                    alt="인기상품1"
                  />
                  <div class="product-info">
                    <h4>불고기 포케 샐러드 275g</h4>
                    <p>판매량: 230개</p>
                  </div>
                </li>
                <li class="product-item">
                  <img
                    src="https://via.placeholder.com/50x50?text=상품2"
                    alt="인기상품2"
                  />
                  <div class="product-info">
                    <h4>닭가슴살 샐러드 250g</h4>
                    <p>판매량: 185개</p>
                  </div>
                </li>
                <li class="product-item">
                  <img
                    src="https://via.placeholder.com/50x50?text=상품3"
                    alt="인기상품3"
                  />
                  <div class="product-info">
                    <h4>그린 디톡스 주스 350ml</h4>
                    <p>판매량: 152개</p>
                  </div>
                </li>
                <li class="product-item">
                  <img
                    src="https://via.placeholder.com/50x50?text=상품4"
                    alt="인기상품4"
                  />
                  <div class="product-info">
                    <h4>단백질 도시락 320g</h4>
                    <p>판매량: 128개</p>
                  </div>
                </li>
                <li class="product-item">
                  <img
                    src="https://via.placeholder.com/50x50?text=상품5"
                    alt="인기상품5"
                  />
                  <div class="product-info">
                    <h4>스패니시 닭가슴살 도시락 300g</h4>
                    <p>판매량: 115개</p>
                  </div>
                </li>
              </ul>
              <a
                href="${pageContext.request.contextPath}/front?key=admin&methodName=popularProducts"
                class="more-link"
                >더보기 <i class="fas fa-angle-right"></i
              ></a>
            </div>
          </div>
        </div>
      </main>
    </div>

    <script src="${pageContext.request.contextPath}/admin/js/admin-script.js"></script>
  </body>
</html>
