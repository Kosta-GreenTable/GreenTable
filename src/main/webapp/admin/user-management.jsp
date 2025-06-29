<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 관리 - 그린테이블 관리자</title>    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-user-management.css"></head>
<body>
    <div class="admin-container">
        <!-- 사이드바 포함 -->
        <jsp:include page="common/admin-sidebar.jsp" />

        <!-- 메인 내용 -->
        <main class="admin-content">
            <!-- 상단 헤더 포함 -->
            <jsp:include page="common/admin-top-header.jsp">
                <jsp:param name="pageTitle" value="회원 관리" />
            </jsp:include>

            <div class="user-management">
                <!-- 통계 카드 -->
                <div class="user-stats">
                    <div class="stat-card total">
                        <i class="fas fa-users"></i>
                        <h3>전체 회원</h3>
                        <p class="stat-number">${totalUsers}</p>
                    </div>
                    <div class="stat-card active">
                        <i class="fas fa-user-check"></i>
                        <h3>활성 회원</h3>
                        <p class="stat-number">${activeUsers}</p>
                    </div>
                    <div class="stat-card suspended">
                        <i class="fas fa-user-times"></i>
                        <h3>정지 회원</h3>
                        <p class="stat-number">${suspendedUsers}</p>
                    </div>
                </div>

                <!-- 검색 섹션 -->
                <div class="search-section">
                    <form class="search-form" action="${pageContext.request.contextPath}/front" method="get">
                        <input type="hidden" name="key" value="admin">
                        <input type="hidden" name="methodName" value="userList">
                        
                        <div class="form-group">
                            <label for="searchType">검색 유형</label>
                            <select id="searchType" name="searchType">
                                <option value="email" ${searchType == 'email' ? 'selected' : ''}>이메일</option>
                                <option value="name" ${searchType == 'name' ? 'selected' : ''}>이름</option>
                                <option value="phone" ${searchType == 'phone' ? 'selected' : ''}>전화번호</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="searchKeyword">검색어</label>
                            <input type="text" id="searchKeyword" name="searchKeyword" value="${searchKeyword}" placeholder="검색어를 입력하세요">
                        </div>
                        
                        <div class="form-group">
                            <label for="status">상태</label>
                            <select id="status" name="status">
                                <option value="">전체</option>
                                <option value="Y" ${status == 'Y' ? 'selected' : ''}>활성</option>
                                <option value="N" ${status == 'N' ? 'selected' : ''}>정지</option>
                            </select>
                        </div>
                        
                        <button type="submit" class="search-btn">
                            <i class="fas fa-search"></i> 검색
                        </button>
                    </form>
                </div>

                <!-- 회원 목록 테이블 -->
                <div class="user-table-section">
                    <div class="table-header">
                        <h3>회원 목록</h3>
                        <span>총 ${totalCount}명</span>
                    </div>
                    
                    <table class="user-table">
                        <thead>
                            <tr>
                                <th>번호</th>
                                <th>이메일</th>
                                <th>이름</th>
                                <th>전화번호</th>
                                <th>가입일</th>
                                <th>상태</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${userList}" varStatus="status">
                                <tr>
                                    <td>${(currentPage - 1) * 10 + status.index + 1}</td>
                                    <td>${user.email}</td>
                                    <td>${user.name}</td>
                                    <td>${user.phone}</td>
                                    <td>${user.insertDate}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.state == 'Y'}">
                                                <span class="status-badge status-active">활성</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge status-suspended">정지</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.state == 'Y'}">
                                                <button class="action-btn btn-suspend" onclick="suspendUser('${user.email}')">
                                                    정지
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="action-btn btn-activate" onclick="activateUser('${user.email}')">
                                                    활성화
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                        <button class="action-btn btn-detail" onclick="viewUserDetail('${user.email}')">
                                            상세
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            
                            <c:if test="${empty userList}">
                                <tr>
                                    <td colspan="7" style="text-align: center; padding: 2rem; color: #666;">
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
                                <a href="?key=admin&methodName=userList&page=${currentPage - 1}&searchType=${searchType}&searchKeyword=${searchKeyword}&status=${status}">이전</a>
                            </c:if>
                            
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <a href="?key=admin&methodName=userList&page=${i}&searchType=${searchType}&searchKeyword=${searchKeyword}&status=${status}" 
                                   class="${i == currentPage ? 'current' : ''}">${i}</a>
                            </c:forEach>
                            
                            <c:if test="${currentPage < totalPages}">
                                <a href="?key=admin&methodName=userList&page=${currentPage + 1}&searchType=${searchType}&searchKeyword=${searchKeyword}&status=${status}">다음</a>
                            </c:if>
                        </div>
                    </c:if>
                </div>
            </div>
        </main>
    </div>    <script>
        const contextPath = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/admin/js/admin-user-management.js"></script>
</body>
</html>
