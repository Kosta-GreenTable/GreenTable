<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="context-path" content="${pageContext.request.contextPath}">
    <title>그린테이블 관리자 - 농가 관리</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/admin/css/admin-table-fix.css">
</head>
<body>
    <div class="admin-container">
        <!-- 사이드바 포함 -->
        <jsp:include page="common/admin-sidebar.jsp" />

        <!-- 메인 내용 -->
        <main class="admin-content">
            <!-- 상단 헤더 포함 -->
            <jsp:include page="common/admin-top-header.jsp">
                <jsp:param name="pageTitle" value="농가 관리" />
            </jsp:include>

            <div class="product-actions">
                <div style="display: flex; gap: 10px;">
                    <a href="${pageContext.request.contextPath}/front?key=farm&methodName=registForm" class="btn-primary">
                        <i class="fas fa-plus"></i> 새 농가 등록
                    </a>
                    <a href="${pageContext.request.contextPath}/front?key=admin&methodName=resetFarmId" class="btn-secondary">
                        <i class="fas fa-sync"></i> ID 시퀀스 리셋
                    </a>
                </div>
                    <div class="product-filter">
                        <select id="statusFilter">
                            <option value="all">모든 상태</option>
                            <option value="활성">활성</option>
                            <option value="종료">종료</option>
                        </select>
                        <div class="search-box">
                            <input type="text" id="farmSearch" placeholder="농가 검색">
                            <button class="search-btn"><i class="fas fa-search"></i></button>
                        </div>
                    </div>
                </div>

                <c:if test="${not empty message}">
                    <div class="alert alert-success" style="margin-bottom: 20px; padding: 10px; border-radius: 4px; background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb;">
                        ${message}
                    </div>
                </c:if>

                <div class="product-list-container">
                    <table class="admin-table product-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>이미지</th>
                                <th>농가명</th>
                                <th>카테고리</th>
                                <th>주소</th>
                                <th>계약상태</th>
                                <th class="action-column">관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="farm" items="${farmList}">
                                <tr class="farm-row" data-status="${farm.contractStatus}">
                                    <td>${farm.farmId}</td>
                                    <td class="product-image-cell">
                                        <!-- 이미지 경로 수정 및 fallback 개선 -->
                                        <img src="${pageContext.request.contextPath}/assets/images/farms/${farm.farmImg}" 
                                             alt="${farm.name}" 
                                             onerror="this.src='https://picsum.photos/seed/farm${farm.farmId}/200/150'">
                                    </td>
                                    <td>${farm.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty farm.category}">${farm.category}</c:when>
                                            <c:otherwise>일반</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${farm.address}</td>
                                    <td>
                                        <span class="status-badge ${farm.contractStatus == '활성' ? 'completed' : 'in-progress'}">
                                            ${farm.contractStatus}
                                        </span>
                                    </td>
                                    <td class="action-column">
                                        <div class="action-buttons">
                                            <a href="${pageContext.request.contextPath}/front?key=farm&methodName=detail&farmId=${farm.farmId}" class="btn-small btn-info" title="상세보기"><i class="fas fa-eye"></i></a>
                                            <a href="${pageContext.request.contextPath}/front?key=farm&methodName=updateForm&farmId=${farm.farmId}" class="btn-small btn-secondary" title="수정"><i class="fas fa-edit"></i></a>
                                            <c:choose>
                                                <c:when test="${farm.contractStatus eq '활성'}">
                                                    <button class="btn-small btn-warning end-contract" data-id="${farm.farmId}" title="계약종료"><i class="fas fa-ban"></i></button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button class="btn-small btn-danger delete-farm" data-id="${farm.farmId}" title="삭제"><i class="fas fa-trash-alt"></i></button>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        
                            <c:if test="${empty farmList}">
                                <tr>
                                    <td colspan="7" class="no-products">등록된 농가가 없습니다.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- 페이지네이션 -->
                <div class="pagination">
                    <c:if test="${not empty totalPages && totalPages > 1}">
                        <ul class="page-numbers">
                            <!-- 처음 페이지로 이동 -->
                            <c:if test="${currentPage > 1}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList&page=1" title="처음"><i class="fas fa-angle-double-left"></i></a>
                                </li>
                            </c:if>
                            
                            <!-- 이전 블록으로 이동 -->
                            <c:if test="${currentPage > pageCnt.blockcount}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList&page=${currentPage-pageCnt.blockcount}" title="이전"><i class="fas fa-angle-left"></i></a>
                                </li>
                            </c:if>
                            
                            <!-- 페이지 번호 표시 -->
                            <c:set var="startPage" value="${(((currentPage-1) / pageCnt.blockcount) * pageCnt.blockcount) + 1}" />
                            <c:set var="endPage" value="${startPage + pageCnt.blockcount - 1}" />
                            <c:if test="${endPage > totalPages}">
                                <c:set var="endPage" value="${totalPages}" />
                            </c:if>
                            
                            <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                <li class="${i == currentPage ? 'active' : ''}">
                                    <a href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList&page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            
                            <!-- 다음 블록으로 이동 -->
                            <c:if test="${endPage < totalPages}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList&page=${startPage + pageCnt.blockcount}" title="다음"><i class="fas fa-angle-right"></i></a>
                                </li>
                            </c:if>
                            
                            <!-- 마지막 페이지로 이동 -->
                            <c:if test="${currentPage < totalPages}">
                                <li>
                                    <a href="${pageContext.request.contextPath}/front?key=farm&methodName=adminList&page=${totalPages}" title="마지막"><i class="fas fa-angle-double-right"></i></a>
                                </li>
                            </c:if>
                        </ul>
                    </c:if>
                </div>
            </div>
        </main>
    </div>

    <!-- 계약 종료 확인 모달 -->
    <div id="endContractModal" class="modal">
      <div class="modal-content">
          <h3>계약 종료</h3>
          <p>이 농가와의 계약을 종료하시겠습니까?</p>
          <p>계약이 종료되면 활성 상태가 변경됩니다.</p>
          <form id="endContractForm" method="get" action="${pageContext.request.contextPath}/front">
              <input type="hidden" name="key" value="farm">
              <input type="hidden" name="methodName" value="updateStatus">
              <input type="hidden" name="farmId" id="endContractFarmId" value="">
              <input type="hidden" name="status" value="종료">
              <div class="modal-buttons">
                  <button type="submit" class="btn-warning">종료하기</button>
                  <button type="button" id="cancelEndContract" class="btn-secondary">취소</button>
              </div>
          </form>
      </div>
    </div>

    <!-- 농가 삭제 확인 모달 - 폼으로 변경 -->
    <div id="deleteFarmModal" class="modal">
      <div class="modal-content">
          <h3>농가 삭제</h3>
          <p>정말 이 농가를 삭제하시겠습니까?</p>
          <p>이 작업은 되돌릴 수 없습니다.</p>
          <form id="deleteFarmForm" method="get" action="${pageContext.request.contextPath}/front">
              <input type="hidden" name="key" value="farm">
              <input type="hidden" name="methodName" value="delete">
              <input type="hidden" name="farmId" id="deleteFarmId" value="">
              <div class="modal-buttons">
                  <button type="submit" class="btn-danger">삭제</button>
                  <button type="button" id="cancelDelete" class="btn-secondary">취소</button>
              </div>
          </form>
      </div>
    </div>

    <script src="${pageContext.request.contextPath}/admin/js/admin-script.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // 상태 필터링
            const statusFilter = document.getElementById('statusFilter');
            if (statusFilter) {
                statusFilter.addEventListener('change', function() {
                    const value = this.value;
                    const rows = document.querySelectorAll('.farm-row');
                    
                    rows.forEach(row => {
                        if (value === 'all' || row.getAttribute('data-status') === value) {
                            row.style.display = '';
                        } else {
                            row.style.display = 'none';
                        }
                    });
                });
            }
            
            // 농가 검색
            const farmSearch = document.getElementById('farmSearch');
            if (farmSearch) {
                farmSearch.addEventListener('input', function() {
                    const value = this.value.toLowerCase();
                    const rows = document.querySelectorAll('.farm-row');
                    
                    rows.forEach(row => {
                        const name = row.children[2].textContent.toLowerCase();
                        const address = row.children[4].textContent.toLowerCase();
                        
                        if (name.includes(value) || address.includes(value)) {
                            row.style.display = '';
                        } else {
                            row.style.display = 'none';
                        }
                    });
                });
            }
            
            // 계약 종료 버튼
            const endContractButtons = document.querySelectorAll('.end-contract');
            const endContractModal = document.getElementById('endContractModal');
            const cancelEndContract = document.getElementById('cancelEndContract');

            endContractButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const farmId = this.getAttribute('data-id');
                    console.log("계약 종료 버튼 클릭됨, farmId:", farmId);
                    
                    if (farmId && farmId.trim() !== '') {
                        // 폼에 farmId 값 설정
                        document.getElementById('endContractFarmId').value = farmId;
                        endContractModal.style.display = 'block';
                    } else {
                        alert('농가 ID를 찾을 수 없습니다.');
                    }
                });
            });

            if (cancelEndContract) {
                cancelEndContract.addEventListener('click', function() {
                    endContractModal.style.display = 'none';
                });
            }
            
            // 농가 삭제 버튼
            const deleteFarmButtons = document.querySelectorAll('.delete-farm');
            const deleteFarmModal = document.getElementById('deleteFarmModal');
            const cancelDelete = document.getElementById('cancelDelete');

            deleteFarmButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const farmId = this.getAttribute('data-id');
                    console.log("삭제 버튼 클릭됨, farmId:", farmId);
                    
                    if (farmId && farmId.trim() !== '') {
                        // 폼에 farmId 값 설정
                        document.getElementById('deleteFarmId').value = farmId;
                        deleteFarmModal.style.display = 'block';
                    } else {
                        alert('농가 ID를 찾을 수 없습니다.');
                    }
                });
            });

            if (cancelDelete) {
                cancelDelete.addEventListener('click', function() {
                    deleteFarmModal.style.display = 'none';
                });
            }
            
            // 모달 외부 클릭 시 닫기
            window.onclick = function(event) {
                if (event.target === endContractModal) {
                    endContractModal.style.display = 'none';
                }
                if (event.target === deleteFarmModal) {
                    deleteFarmModal.style.display = 'none';
                }
            }
            
            // 메시지 알림이 있으면 3초 후에 사라지게
            const alert = document.querySelector('.alert');
            if (alert) {
                setTimeout(() => {
                    alert.style.opacity = '0';
                    setTimeout(() => {
                        alert.style.display = 'none';
                    }, 500);
                }, 3000);
            }
        });
    </script>
</body>
</html>