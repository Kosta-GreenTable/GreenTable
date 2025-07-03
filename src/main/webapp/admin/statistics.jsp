<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>통계 관리 - 그린테이블 관리자</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-statistics.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/date-fns@1.30.1/index.min.js"></script>
  </head>  <body>
    <div class="admin-container">
        <!-- 사이드바 포함 -->
        <jsp:include page="common/admin-sidebar.jsp">
            <jsp:param name="currentPage" value="statistics" />
        </jsp:include>        <!-- 메인 내용 -->
        <main class="admin-content">
            <!-- 상단 헤더 포함 -->
            <jsp:include page="common/admin-top-header.jsp">
                <jsp:param name="pageTitle" value="통계 관리" />
            </jsp:include>

        <div class="statistics-dashboard">
          <!-- 통계 요약 카드 -->
          <div class="stats-summary">
            <div class="stat-card">
              <div class="stat-icon">
                <i class="fas fa-won-sign"></i>
              </div>
              <div class="stat-content">
                <h3>총 매출</h3>
                <p class="stat-number" id="totalRevenue">₩0</p>
                <span class="stat-period">전체 기간</span>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon">
                <i class="fas fa-shopping-cart"></i>
              </div>
              <div class="stat-content">
                <h3>총 주문</h3>
                <p class="stat-number" id="totalOrders">0</p>
                <span class="stat-period">전체 기간</span>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon">
                <i class="fas fa-users"></i>
              </div>
              <div class="stat-content">
                <h3>총 회원</h3>
                <p class="stat-number" id="totalUsers">0</p>
                <span class="stat-period">전체 기간</span>
              </div>
            </div>

            <div class="stat-card">
              <div class="stat-icon">
                <i class="fas fa-box"></i>
              </div>
              <div class="stat-content">
                <h3>인기 상품</h3>
                <p class="stat-number" id="topProduct">-</p>
                <span class="stat-period">판매량 기준</span>
              </div>
            </div>
          </div>

          <!-- 차트 섹션 -->
          <div class="charts-section">
            <!-- 일별 매출 통계 -->
            <div class="chart-container">
              <div class="chart-header">
                <h3><i class="fas fa-chart-line"></i> 일별 매출 통계</h3>
                <div class="chart-controls">
                  <input type="date" id="startDate" />
                  <input type="date" id="endDate" />
                  <button onclick="updateDailySalesChart()" class="btn-update">
                    <i class="fas fa-sync-alt"></i> 업데이트
                  </button>
                </div>
              </div>
              <div class="chart-wrapper">
                <canvas id="dailySalesChart"></canvas>
              </div>
            </div>

            <!-- 월별 매출 통계 -->
            <div class="chart-container">
              <div class="chart-header">
                <h3><i class="fas fa-calendar-alt"></i> 월별 매출 통계</h3>
                <div class="chart-controls">
                  <select id="yearSelect">
                    <option value="2024">2024년</option>
                    <option value="2025" selected>2025년</option>
                  </select>
                  <button
                    onclick="updateMonthlySalesChart()"
                    class="btn-update"
                  >
                    <i class="fas fa-sync-alt"></i> 업데이트
                  </button>
                </div>
              </div>
              <div class="chart-wrapper">
                <canvas id="monthlySalesChart"></canvas>
              </div>
            </div>

            <!-- 인기 상품 통계 -->
            <div class="chart-container">
              <div class="chart-header">
                <h3><i class="fas fa-trophy"></i> 인기 상품 TOP 10</h3>
                <div class="chart-controls">
                  <select id="topProductsLimit">
                    <option value="5">TOP 5</option>
                    <option value="10" selected>TOP 10</option>
                    <option value="20">TOP 20</option>
                  </select>
                  <button onclick="updateTopProductsChart()" class="btn-update">
                    <i class="fas fa-sync-alt"></i> 업데이트
                  </button>
                </div>
              </div>
              <div class="chart-wrapper">
                <canvas id="topProductsChart"></canvas>
              </div>
            </div>

            <!-- 카테고리별 통계 -->
            <div class="chart-container">
              <div class="chart-header">
                <h3><i class="fas fa-tags"></i> 카테고리별 매출</h3>
              </div>
              <div class="chart-wrapper">
                <canvas id="categoryChart"></canvas>
              </div>
            </div>

            <!-- 주문 상태별 통계 -->
            <div class="chart-container">
              <div class="chart-header">
                <h3><i class="fas fa-chart-pie"></i> 주문 상태별 분포</h3>
              </div>
              <div class="chart-wrapper">
                <canvas id="orderStatusChart"></canvas>          </div>
        </main>
    </div>
      </div>
    </div>

    <script>
      const contextPath = "${pageContext.request.contextPath}";
    </script>
    <script src="${pageContext.request.contextPath}/admin/js/admin-statistics.js"></script>
  </body>
</html>
