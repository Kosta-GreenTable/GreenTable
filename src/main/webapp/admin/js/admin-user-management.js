// Admin User Management JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // 사용자 상세 모달 관련
    const userDetailModal = document.getElementById('userDetailModal');
    const closeModal = document.querySelector('.close');
    
    // 모달 닫기
    if (closeModal) {
        closeModal.onclick = function() {
            userDetailModal.style.display = 'none';
        };
    }
    
    // 모달 외부 클릭 시 닫기
    window.onclick = function(event) {
        if (event.target === userDetailModal) {
            userDetailModal.style.display = 'none';
        }
    };
    
    // 사용자 상세 보기
    window.showUserDetail = function(email) {
        fetch(`${contextPath}/front?key=admin&methodName=userDetail&email=${encodeURIComponent(email)}`)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const user = data.user;
                    document.getElementById('detailName').textContent = user.name || '-';
                    document.getElementById('detailEmail').textContent = user.email || '-';
                    document.getElementById('detailPhone').textContent = user.phone || '-';
                    document.getElementById('detailAddress').textContent = user.address || '-';
                    document.getElementById('detailJoinDate').textContent = user.joinDate || '-';
                    document.getElementById('detailStatus').textContent = user.status === 'Y' ? '활성' : '정지';
                    document.getElementById('detailStatus').className = `value status-${user.status === 'Y' ? 'active' : 'suspended'}`;
                    
                    userDetailModal.style.display = 'block';
                } else {
                    alert('사용자 정보를 불러올 수 없습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다.');
            });
    };
    
    // 사용자 정지
    window.suspendUser = function(email, userName) {
        if (confirm(`정말로 ${userName}님을 정지하시겠습니까?`)) {
            fetch(`${contextPath}/front?key=admin&methodName=suspendUser`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `email=${encodeURIComponent(email)}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('사용자가 정지되었습니다.');
                    location.reload();
                } else {
                    alert(data.message || '사용자 정지에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다.');
            });
        }
    };
    
    // 사용자 활성화
    window.activateUser = function(email, userName) {
        if (confirm(`정말로 ${userName}님을 활성화하시겠습니까?`)) {
            fetch(`${contextPath}/front?key=admin&methodName=activateUser`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `email=${encodeURIComponent(email)}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('사용자가 활성화되었습니다.');
                    location.reload();
                } else {
                    alert(data.message || '사용자 활성화에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다.');
            });
        }
    };
    
    // 검색 폼 초기화
    window.resetSearch = function() {
        document.getElementById('searchForm').reset();
    };
    
    // 전체 선택/해제
    const selectAllCheckbox = document.getElementById('selectAll');
    const userCheckboxes = document.querySelectorAll('.user-checkbox');
    
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            userCheckboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            updateBulkActions();
        });
    }
    
    // 개별 체크박스 변경
    userCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            updateSelectAll();
            updateBulkActions();
        });
    });
    
    // 전체 선택 체크박스 상태 업데이트
    function updateSelectAll() {
        const checkedCount = document.querySelectorAll('.user-checkbox:checked').length;
        const totalCount = userCheckboxes.length;
        
        if (selectAllCheckbox) {
            selectAllCheckbox.checked = checkedCount === totalCount;
            selectAllCheckbox.indeterminate = checkedCount > 0 && checkedCount < totalCount;
        }
    }
    
    // 일괄 작업 버튼 활성화/비활성화
    function updateBulkActions() {
        const checkedCount = document.querySelectorAll('.user-checkbox:checked').length;
        const bulkActionBtns = document.querySelectorAll('.bulk-action-btn');
        
        bulkActionBtns.forEach(btn => {
            btn.disabled = checkedCount === 0;
        });
    }
    
    // 일괄 정지
    window.bulkSuspendUsers = function() {
        const checkedUsers = document.querySelectorAll('.user-checkbox:checked');
        if (checkedUsers.length === 0) {
            alert('정지할 사용자를 선택해주세요.');
            return;
        }
        
        if (confirm(`선택한 ${checkedUsers.length}명의 사용자를 정지하시겠습니까?`)) {
            const emails = Array.from(checkedUsers).map(cb => cb.value);
            
            Promise.all(emails.map(email => 
                fetch(`${contextPath}/front?key=admin&methodName=suspendUser`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `email=${encodeURIComponent(email)}`
                }).then(response => response.json())
            )).then(results => {
                const successCount = results.filter(r => r.success).length;
                alert(`${successCount}명의 사용자가 정지되었습니다.`);
                location.reload();
            }).catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다.');
            });
        }
    };
    
    // 일괄 활성화
    window.bulkActivateUsers = function() {
        const checkedUsers = document.querySelectorAll('.user-checkbox:checked');
        if (checkedUsers.length === 0) {
            alert('활성화할 사용자를 선택해주세요.');
            return;
        }
        
        if (confirm(`선택한 ${checkedUsers.length}명의 사용자를 활성화하시겠습니까?`)) {
            const emails = Array.from(checkedUsers).map(cb => cb.value);
            
            Promise.all(emails.map(email => 
                fetch(`${contextPath}/front?key=admin&methodName=activateUser`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `email=${encodeURIComponent(email)}`
                }).then(response => response.json())
            )).then(results => {
                const successCount = results.filter(r => r.success).length;
                alert(`${successCount}명의 사용자가 활성화되었습니다.`);
                location.reload();
            }).catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다.');
            });
        }
    };
});
