// Admin User Management JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // 사용자 정지
    window.suspendUser = function(email) {
        if (confirm(`정말로 이 사용자를 정지하시겠습니까?`)) {
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
    window.activateUser = function(email) {
        if (confirm(`정말로 이 사용자를 활성화하시겠습니까?`)) {
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
        const form = document.querySelector('.search-form');
        if (form) {
            form.reset();
        }
    };
});
