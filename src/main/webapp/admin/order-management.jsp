<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">    <title>주문 관리 - 그린테이블 관리자</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-order-management.css">
</head>
<body>
    <div class="admin-container">
        <!-- 사이드바 포함 -->
        <jsp:include page="common/admin-sidebar.jsp" />

        <!-- 메인 내용 -->
        <main class="admin-content">
            <!-- 상단 헤더 포함 -->
            <jsp:include page="common/admin-top-header.jsp">
                <jsp:param name="pageTitle" value="주문 관리" />
            </jsp:include>

            <div class="order-management">
                <!-- 통계 카드 -->
                <div class="order-stats">
                    <div class="stat-card total">
                        <i class="fas fa-shopping-cart"></i>
                        <h3>전체 주문</h3>
                        <p class="stat-number">${totalOrders}</p>
                    </div>
                    <div class="stat-card pending">
                        <i class="fas fa-clock"></i>
                        <h3>대기 중</h3>
                        <p class="stat-number">${pendingOrders}</p>
                    </div>
                    <div class="stat-card completed">
                        <i class="fas fa-check-circle"></i>
                        <h3>완료</h3>
                        <p class="stat-number">${completedOrders}</p>
                    </div>
                    <div class="stat-card cancelled">
                        <i class="fas fa-times-circle"></i>
                        <h3>취소</h3>
                        <p class="stat-number">${cancelledOrders}</p>
                    </div>
                </div>

                <!-- 검색 섹션 -->
                <div class="search-section">
                    <form class="search-form" action="${pageContext.request.contextPath}/front" method="get">
                        <input type="hidden" name="key" value="admin">
                        <input type="hidden" name="methodName" value="orderList">
                        
                        <div class="form-group">
                            <label for="searchType">검색 유형</label>
                            <select id="searchType" name="searchType">
                                <option value="orderNo" ${searchType == 'orderNo' ? 'selected' : ''}>주문번호</option>
                                <option value="userEmail" ${searchType == 'userEmail' ? 'selected' : ''}>주문자 이메일</option>
                                <option value="userName" ${searchType == 'userName' ? 'selected' : ''}>주문자명</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="searchKeyword">검색어</label>
                            <input type="text" id="searchKeyword" name="searchKeyword" value="${searchKeyword}" placeholder="검색어를 입력하세요">
                        </div>
                        
                        <div class="form-group">
                            <label for="status">주문 상태</label>
                            <select id="status" name="status">
                                <option value="">전체</option>
                                <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>대기중</option>
                                <option value="CONFIRMED" ${status == 'CONFIRMED' ? 'selected' : ''}>확인됨</option>
                                <option value="SHIPPING" ${status == 'SHIPPING' ? 'selected' : ''}>배송중</option>
                                <option value="DELIVERED" ${status == 'DELIVERED' ? 'selected' : ''}>배송완료</option>
                                <option value="CANCELLED" ${status == 'CANCELLED' ? 'selected' : ''}>취소</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="startDate">시작일</label>
                            <input type="date" id="startDate" name="startDate" value="${startDate}">
                        </div>
                        
                        <div class="form-group">
                            <label for="endDate">종료일</label>
                            <input type="date" id="endDate" name="endDate" value="${endDate}">
                        </div>
                        
                        <button type="submit" class="search-btn">
                            <i class="fas fa-search"></i> 검색
                        </button>
                    </form>
                </div>

                <!-- 주문 목록 테이블 -->
                <div class="order-table-section">
                    <div class="table-header">
                        <h3>주문 목록</h3>
                        <span>총 ${totalCount}건</span>
                    </div>
                    
                    <table class="order-table">
                        <thead>
                            <tr>
                                <th>주문번호</th>
                                <th>주문자</th>
                                <th>주문일시</th>
                                <th>주문금액</th>
                                <th>상태</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orderList}">
                                <tr>
                                    <td>${order.orderNo}</td>
                                    <td>
                                        <div>${order.userName}</div>
                                        <div style="font-size: 0.9rem; color: #666;">${order.userEmail}</div>
                                    </td>
                                    <td>
                                        <fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm" />
                                    </td>
                                    <td class="price">
                                        <fmt:formatNumber value="${order.totalAmount}" pattern="#,###" />원
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${order.status == 'PENDING'}">
                                                <span class="status-badge status-pending">대기중</span>
                                            </c:when>
                                            <c:when test="${order.status == 'CONFIRMED'}">
                                                <span class="status-badge status-confirmed">확인됨</span>
                                            </c:when>
                                            <c:when test="${order.status == 'SHIPPING'}">
                                                <span class="status-badge status-shipping">배송중</span>
                                            </c:when>
                                            <c:when test="${order.status == 'DELIVERED'}">
                                                <span class="status-badge status-delivered">배송완료</span>
                                            </c:when>
                                            <c:when test="${order.status == 'CANCELLED'}">
                                                <span class="status-badge status-cancelled">취소</span>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <button class="action-btn btn-detail" onclick="viewOrderDetail('${order.orderNo}')">
                                            상세
                                        </button>
                                        <c:if test="${order.status != 'CANCELLED' && order.status != 'DELIVERED'}">
                                            <button class="action-btn btn-update" onclick="updateOrderStatus('${order.orderNo}', '${order.status}')">
                                                상태변경
                                            </button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            
                            <c:if test="${empty orderList}">
                                <tr>
                                    <td colspan="6" style="text-align: center; padding: 2rem; color: #666;">
                                        검색 결과가 없습니다.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>

                    <!-- 페이지네이션 -->
                    <c:if test="${totalPages > 1}">
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="?key=admin&methodName=orderList&page=${currentPage - 1}&searchType=${searchType}&searchKeyword=${searchKeyword}&status=${status}&startDate=${startDate}&endDate=${endDate}">이전</a>
                            </c:if>
                            
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a href="?key=admin&methodName=orderList&page=${i}&searchType=${searchType}&searchKeyword=${searchKeyword}&status=${status}&startDate=${startDate}&endDate=${endDate}" 
                                   class="${i == currentPage ? 'current' : ''}">${i}</a>
                            </c:forEach>
                            
                            <c:if test="${currentPage < totalPages}">
                                <a href="?key=admin&methodName=orderList&page=${currentPage + 1}&searchType=${searchType}&searchKeyword=${searchKeyword}&status=${status}&startDate=${startDate}&endDate=${endDate}">다음</a>
                            </c:if>
                        </div>
                    </c:if>
                </div>
            </div>
        </main>
    </div>

    <!-- 주문 상세 모달 -->
    <div id="orderDetailModal" class="order-detail-modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>주문 상세 정보</h2>
                <span class="close" onclick="closeOrderDetailModal()">&times;</span>
            </div>
            <div id="orderDetailContent">
                <!-- 주문 상세 정보가 여기에 로드됩니다 -->
            </div>
        </div>
    </div>    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        const contextPath = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/admin/js/admin-order-management.js"></script>
</body>
</html>
