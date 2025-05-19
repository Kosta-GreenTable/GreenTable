/**
 * 상품 상세 페이지 JavaScript
 */
document.addEventListener('DOMContentLoaded', function() {
    // 상품 삭제 모달
    setupDeleteModal();
});

/**
 * 상품 삭제 모달 설정
 */
function setupDeleteModal() {
    const modal = document.getElementById('deleteModal');
    const deleteButton = document.getElementById('deleteProductBtn');
    const cancelDelete = document.getElementById('cancelDelete');
    
    // 삭제 버튼 클릭 시 모달 표시
    if (deleteButton && modal) {
        deleteButton.addEventListener('click', function() {
            modal.style.display = 'block';
        });
    }
    
    // 취소 버튼 클릭 시 모달 닫기
    if (cancelDelete && modal) {
        cancelDelete.addEventListener('click', function() {
            modal.style.display = 'none';
        });
    }
    
    // 모달 바깥 클릭 시 닫기
    window.addEventListener('click', function(event) {
        if (modal && event.target === modal) {
            modal.style.display = 'none';
        }
    });
}