// Admin Order Management JavaScript

// 주문 상세 정보 보기
function viewOrderDetail(orderNo) {
    fetch(contextPath + '/front?key=admin&methodName=orderDetail&orderNo=' + encodeURIComponent(orderNo))
        .then(response => response.text())
        .then(data => {
            document.getElementById('orderDetailContent').innerHTML = data;
            document.getElementById('orderDetailModal').style.display = 'block';
        })
        .catch(error => {
            Swal.fire('오류!', '주문 상세 정보를 불러오는데 실패했습니다.', 'error');
        });
}

// 주문 상세 모달 닫기
function closeOrderDetailModal() {
    document.getElementById('orderDetailModal').style.display = 'none';
}

// 주문 상태 업데이트
function updateOrderStatus(orderNo, currentStatus) {
    const statusOptions = {
        'PENDING': '확인됨',
        'CONFIRMED': '배송중',
        'SHIPPING': '배송완료'
    };

    const nextStatus = {
        'PENDING': 'CONFIRMED',
        'CONFIRMED': 'SHIPPING',
        'SHIPPING': 'DELIVERED'
    };

    if (!nextStatus[currentStatus]) {
        Swal.fire('알림', '변경할 수 있는 상태가 없습니다.', 'info');
        return;
    }

    Swal.fire({
        title: '주문 상태 변경',
        text: '주문 상태를 \'' + statusOptions[currentStatus] + '\'로 변경하시겠습니까?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#6c757d',
        confirmButtonText: '변경',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(contextPath + '/front', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'key=admin&methodName=updateOrderStatus&orderNo=' + encodeURIComponent(orderNo) + '&status=' + nextStatus[currentStatus]
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire('완료!', '주문 상태가 변경되었습니다.', 'success')
                        .then(() => location.reload());
                } else {
                    Swal.fire('오류!', data.message || '주문 상태 변경에 실패했습니다.', 'error');
                }
            })
            .catch(error => {
                Swal.fire('오류!', '서버 통신 중 오류가 발생했습니다.', 'error');
            });
        }
    });
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    const modal = document.getElementById('orderDetailModal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
};
                statusModal.style.display = 'none';
                location.reload();
            } else {
                alert(data.message || '주문 상태 업데이트에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('오류가 발생했습니다.');
        });
    };
    
    // 주문 상세 보기
    window.showOrderDetail = function(orderNo) {
        window.open(`${contextPath}/front?key=admin&methodName=orderDetail&orderNo=${encodeURIComponent(orderNo)}`, '_blank', 'width=1000,height=700');
    };
    
    // 주문 취소
    window.cancelOrder = function(orderNo) {
        if (confirm(`정말로 주문 ${orderNo}를 취소하시겠습니까?`)) {
            fetch(`${contextPath}/front?key=admin&methodName=updateOrderStatus`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `orderNo=${encodeURIComponent(orderNo)}&status=CANCELLED&memo=관리자에 의한 주문 취소`
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('주문이 취소되었습니다.');
                    location.reload();
                } else {
                    alert(data.message || '주문 취소에 실패했습니다.');
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
    
    // 주문 상태에 따른 색상 변경
    function updateStatusBadges() {
        const statusBadges = document.querySelectorAll('.status-badge');
        statusBadges.forEach(badge => {
            const status = badge.textContent.toLowerCase();
            badge.className = `status-badge status-${status}`;
        });
    }
    
    // 날짜 범위 검색 관련
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    
    if (startDateInput && endDateInput) {
        // 시작일이 종료일보다 늦을 수 없도록 제한
        startDateInput.addEventListener('change', function() {
            if (endDateInput.value && this.value > endDateInput.value) {
                endDateInput.value = this.value;
            }
        });
        
        endDateInput.addEventListener('change', function() {
            if (startDateInput.value && this.value < startDateInput.value) {
                startDateInput.value = this.value;
            }
        });
    }
    
    // 일괄 작업 관련
    const selectAllCheckbox = document.getElementById('selectAll');
    const orderCheckboxes = document.querySelectorAll('.order-checkbox');
    
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', function() {
            orderCheckboxes.forEach(checkbox => {
                checkbox.checked = this.checked;
            });
            updateBulkActions();
        });
    }
    
    orderCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            updateSelectAll();
            updateBulkActions();
        });
    });
    
    // 전체 선택 체크박스 상태 업데이트
    function updateSelectAll() {
        const checkedCount = document.querySelectorAll('.order-checkbox:checked').length;
        const totalCount = orderCheckboxes.length;
        
        if (selectAllCheckbox) {
            selectAllCheckbox.checked = checkedCount === totalCount;
            selectAllCheckbox.indeterminate = checkedCount > 0 && checkedCount < totalCount;
        }
    }
    
    // 일괄 작업 버튼 활성화/비활성화
    function updateBulkActions() {
        const checkedCount = document.querySelectorAll('.order-checkbox:checked').length;
        const bulkActionBtns = document.querySelectorAll('.bulk-action-btn');
        
        bulkActionBtns.forEach(btn => {
            btn.disabled = checkedCount === 0;
        });
    }
    
    // 일괄 상태 변경
    window.bulkUpdateStatus = function(status) {
        const checkedOrders = document.querySelectorAll('.order-checkbox:checked');
        if (checkedOrders.length === 0) {
            alert('상태를 변경할 주문을 선택해주세요.');
            return;
        }
        
        const statusText = getStatusText(status);
        if (confirm(`선택한 ${checkedOrders.length}개의 주문을 ${statusText} 상태로 변경하시겠습니까?`)) {
            const orderNos = Array.from(checkedOrders).map(cb => cb.value);
            
            Promise.all(orderNos.map(orderNo => 
                fetch(`${contextPath}/front?key=admin&methodName=updateOrderStatus`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `orderNo=${encodeURIComponent(orderNo)}&status=${encodeURIComponent(status)}&memo=일괄 상태 변경`
                }).then(response => response.json())
            )).then(results => {
                const successCount = results.filter(r => r.success).length;
                alert(`${successCount}개의 주문 상태가 변경되었습니다.`);
                location.reload();
            }).catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다.');
            });
        }
    };
    
    // 상태 텍스트 변환
    function getStatusText(status) {
        const statusMap = {
            'PENDING': '대기중',
            'PROCESSING': '처리중',
            'SHIPPED': '배송중',
            'DELIVERED': '배송완료',
            'CANCELLED': '취소됨'
        };
        return statusMap[status] || status;
    }
    
    // 엑셀 다운로드
    window.downloadExcel = function() {
        const params = new URLSearchParams(window.location.search);
        params.set('export', 'excel');
        window.location.href = `${contextPath}/front?key=admin&methodName=orderList&${params.toString()}`;
    };
    
    // 페이지 로드 시 초기화
    updateStatusBadges();
});
