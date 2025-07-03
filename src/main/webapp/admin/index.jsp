<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>

<%
// 데이터베이스 연결 및 통계 데이터 조회
Connection conn = null;
PreparedStatement pstmt = null;
ResultSet rs = null;

int totalUsers = 0;
int totalProducts = 0;
int totalFarms = 0;
int totalOrders = 0;
int todayOrders = 0;

List<Map<String, Object>> recentProducts = new ArrayList<>();
List<Map<String, Object>> recentFarms = new ArrayList<>();
List<Map<String, Object>> popularCategories = new ArrayList<>();

try {
    // DbUtil을 사용한 데이터베이스 연결
    conn = site.greentable.util.DbUtil.getConnection();
    
    // 1. 총 회원 수 조회
    pstmt = conn.prepareStatement("SELECT COUNT(*) as count FROM user_login");
    rs = pstmt.executeQuery();
    if (rs.next()) {
        totalUsers = rs.getInt("count");
    }
    rs.close();
    pstmt.close();
    
    // 2. 총 상품 수 조회
    pstmt = conn.prepareStatement("SELECT COUNT(*) as count FROM products");
    rs = pstmt.executeQuery();
    if (rs.next()) {
        totalProducts = rs.getInt("count");
    }
    rs.close();
    pstmt.close();
    
    // 3. 총 농가 수 조회
    pstmt = conn.prepareStatement("SELECT COUNT(*) as count FROM farms");
    rs = pstmt.executeQuery();
    if (rs.next()) {
        totalFarms = rs.getInt("count");
    }
    rs.close();
    pstmt.close();
    
    // 4. 총 주문 수 조회
    try {
        pstmt = conn.prepareStatement("SELECT COUNT(*) as count FROM orders");
        rs = pstmt.executeQuery();
        if (rs.next()) {
            totalOrders = rs.getInt("count");
        }
        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        totalOrders = 0;
    }
    
    // 5. 오늘 주문 수 조회
    try {
        pstmt = conn.prepareStatement("SELECT COUNT(*) as count FROM orders WHERE DATE(order_at) = CURDATE()");
        rs = pstmt.executeQuery();
        if (rs.next()) {
            todayOrders = rs.getInt("count");
        }
        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        todayOrders = 0;
    }
    
    // 6. 최근 등록된 상품 5개 조회
    try {
        pstmt = conn.prepareStatement("SELECT product_id, name, price FROM products ORDER BY product_id DESC LIMIT 5");
        rs = pstmt.executeQuery();
        while (rs.next()) {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", rs.getInt("product_id"));
            product.put("name", rs.getString("name"));
            product.put("price", rs.getInt("price"));
            recentProducts.add(product);
        }
        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // 7. 최근 등록된 농가 5개 조회
    try {
        pstmt = conn.prepareStatement("SELECT farm_id, name, description FROM farms ORDER BY farm_id DESC LIMIT 5");
        rs = pstmt.executeQuery();
        while (rs.next()) {
            Map<String, Object> farm = new HashMap<>();
            farm.put("farmId", rs.getInt("farm_id"));
            farm.put("name", rs.getString("name"));
            farm.put("description", rs.getString("description"));
            recentFarms.add(farm);
        }
        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    // 8. 카테고리별 상품 수 조회
    try {
        pstmt = conn.prepareStatement("SELECT category, COUNT(*) as count FROM products WHERE category IS NOT NULL AND category != '' GROUP BY category ORDER BY count DESC LIMIT 5");
        rs = pstmt.executeQuery();
        while (rs.next()) {
            Map<String, Object> category = new HashMap<>();
            category.put("name", rs.getString("category"));
            category.put("count", rs.getInt("count"));
            popularCategories.add(category);
        }
        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
} catch (Exception e) {
    e.printStackTrace();
} finally {
    try {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (conn != null) conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// JSP에서 사용할 수 있도록 request에 저장
request.setAttribute("totalUsers", totalUsers);
request.setAttribute("totalProducts", totalProducts);
request.setAttribute("totalFarms", totalFarms);
request.setAttribute("totalOrders", totalOrders);
request.setAttribute("todayOrders", todayOrders);
request.setAttribute("recentProducts", recentProducts);
request.setAttribute("recentFarms", recentFarms);
request.setAttribute("popularCategories", popularCategories);
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>그린테이블 관리자</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
    
    <style>
        .dashboard-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            gap: 20px;
            transition: transform 0.2s ease;
        }

        .stat-card:hover {
            transform: translateY(-2px);
        }

        .stat-icon {
            font-size: 32px;
            padding: 15px;
            border-radius: 10px;
            color: white;
        }

        .stat-icon.users { background: #3498db; }
        .stat-icon.products { background: #e74c3c; }
        .stat-icon.farms { background: #27ae60; }
        .stat-icon.orders { background: #f39c12; }
        .stat-icon.today { background: #9b59b6; }

        .stat-content h3 {
            margin: 0 0 5px 0;
            font-size: 14px;
            color: #666;
            text-transform: uppercase;
        }

        .stat-number {
            font-size: 24px;
            font-weight: bold;
            margin: 0;
            color: #333;
        }

        .recent-section {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
            margin-bottom: 30px;
        }

        .section-card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }

        .section-header {
            padding: 20px;
            border-bottom: 1px solid #eee;
            background: #f8f9fa;
        }

        .section-header h3 {
            margin: 0;
            font-size: 18px;
            color: #333;
        }

        .section-header h3 i {
            margin-right: 10px;
            color: #00c471;
        }

        .section-content {
            padding: 20px;
            max-height: 400px;
            overflow-y: auto;
        }

        .item-list {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .item {
            display: flex;
            align-items: center;
            gap: 15px;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
        }

        .item:last-child {
            border-bottom: none;
        }

        .item-info {
            flex: 1;
        }

        .item-info h4 {
            margin: 0 0 5px 0;
            font-size: 14px;
            color: #333;
            font-weight: 600;
        }

        .item-info p {
            margin: 0 0 3px 0;
            font-size: 12px;
            color: #666;
        }

        .item-info .price {
            color: #00c471;
            font-weight: 600;
        }

        .category-chart {
            display: grid;
            gap: 15px;
        }

        .category-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
        }

        .category-name {
            min-width: 80px;
            font-weight: 500;
            color: #333;
        }

        .category-bar {
            flex: 1;
            height: 10px;
            background: #e9ecef;
            border-radius: 5px;
            margin: 0 15px;
            overflow: hidden;
        }

        .category-fill {
            height: 100%;
            background: linear-gradient(90deg, #00c471, #28a745);
            border-radius: 5px;
            transition: width 0.3s ease;
        }

        .category-count {
            min-width: 50px;
            text-align: right;
            font-weight: bold;
            color: #00c471;
            font-size: 14px;
        }

        .no-data {
            text-align: center;
            color: #999;
            padding: 40px;
            font-style: italic;
        }

        .no-data i {
            font-size: 48px;
            color: #ddd;
            margin-bottom: 15px;
            display: block;
        }

        .no-data p {
            margin: 0;
            font-size: 16px;
        }

        @media (max-width: 768px) {
            .recent-section {
                grid-template-columns: 1fr;
            }

            .dashboard-stats {
                grid-template-columns: 1fr;
            }

            .item {
                flex-direction: column;
                align-items: flex-start;
                text-align: center;
            }
        }
    </style>
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
                <!-- 통계 카드 -->
                <div class="dashboard-stats">
                    <div class="stat-card">
                        <i class="fas fa-users stat-icon users"></i>
                        <div class="stat-content">
                            <h3>총 회원 수</h3>
                            <p class="stat-number">
                                <fmt:formatNumber value="${totalUsers}" pattern="#,###" />
                            </p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <i class="fas fa-box stat-icon products"></i>
                        <div class="stat-content">
                            <h3>등록된 상품</h3>
                            <p class="stat-number">
                                <fmt:formatNumber value="${totalProducts}" pattern="#,###" />
                            </p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <i class="fas fa-seedling stat-icon farms"></i>
                        <div class="stat-content">
                            <h3>등록된 농가</h3>
                            <p class="stat-number">
                                <fmt:formatNumber value="${totalFarms}" pattern="#,###" />
                            </p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <i class="fas fa-shopping-cart stat-icon orders"></i>
                        <div class="stat-content">
                            <h3>총 주문 수</h3>
                            <p class="stat-number">
                                <fmt:formatNumber value="${totalOrders}" pattern="#,###" />
                            </p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <i class="fas fa-calendar-day stat-icon today"></i>
                        <div class="stat-content">
                            <h3>오늘 주문 수</h3>
                            <p class="stat-number">
                                <fmt:formatNumber value="${todayOrders}" pattern="#,###" />
                            </p>
                        </div>
                    </div>
                </div>

                <!-- 최근 데이터 섹션 -->
                <div class="recent-section">
                    <!-- 최근 등록된 상품 -->
                    <div class="section-card">
                        <div class="section-header">
                            <h3><i class="fas fa-box"></i> 최근 등록된 상품</h3>
                        </div>
                        <div class="section-content">
                            <c:choose>
                                <c:when test="${not empty recentProducts}">
                                    <ul class="item-list">
                                        <c:forEach var="product" items="${recentProducts}">
                                            <li class="item">
                                                <div class="item-info">
                                                    <h4>${product.name}</h4>
                                                    <p class="price">
                                                        <fmt:formatNumber value="${product.price}" pattern="#,###" />원
                                                    </p>
                                                    <p>상품 ID: ${product.productId}</p>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-data">
                                        <i class="fas fa-inbox"></i>
                                        <p>등록된 상품이 없습니다.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- 최근 등록된 농가 -->
                    <div class="section-card">
                        <div class="section-header">
                            <h3><i class="fas fa-seedling"></i> 최근 등록된 농가</h3>
                        </div>
                        <div class="section-content">
                            <c:choose>
                                <c:when test="${not empty recentFarms}">
                                    <ul class="item-list">
                                        <c:forEach var="farm" items="${recentFarms}">
                                            <li class="item">
                                                <div class="item-info">
                                                    <h4>${farm.name}</h4>
                                                    <p>${farm.description}</p>
                                                    <p>농가 ID: ${farm.farmId}</p>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-data">
                                        <i class="fas fa-inbox"></i>
                                        <p>등록된 농가가 없습니다.</p>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <!-- 카테고리별 상품 통계 -->
                <div class="section-card">
                    <div class="section-header">
                        <h3><i class="fas fa-chart-bar"></i> 카테고리별 상품 현황</h3>
                    </div>
                    <div class="section-content">
                        <c:choose>
                            <c:when test="${not empty popularCategories}">
                                <div class="category-chart">
                                    <c:set var="maxCount" value="${popularCategories[0].count}" />
                                    <c:forEach var="category" items="${popularCategories}">
                                        <div class="category-item">
                                            <span class="category-name">${category.name}</span>
                                            <div class="category-bar">
                                                <div class="category-fill" style="width: ${category.count * 100 / maxCount}%"></div>
                                            </div>
                                            <span class="category-count">${category.count}개</span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="no-data">
                                    <i class="fas fa-chart-bar"></i>
                                    <p>카테고리 데이터가 없습니다.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/admin/js/admin-script.js"></script>
</body>
</html>
