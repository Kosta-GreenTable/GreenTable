/**
 * 상품 목록 JavaScript
 */
document.addEventListener('DOMContentLoaded', function() {
    // 필터링 및 검색 기능
    setupFiltering();
    
    // 상품 삭제 모달
    setupDeleteModal();
});

/**
 * 상품 목록 필터링 및 검색 기능 설정
 */
function setupFiltering() {
    const categoryFilter = document.getElementById('categoryFilter');
    const productSearch = document.getElementById('productSearch');
    const searchBtn = document.querySelector('.search-btn');
    
    // 카테고리 필터링
    if (categoryFilter) {
        categoryFilter.addEventListener('change', function() {
            filterProducts();
        });
    }
    
    // 검색 기능
    if (searchBtn) {
        searchBtn.addEventListener('click', function() {
            filterProducts();
        });
    }
    
    if (productSearch) {
        productSearch.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                filterProducts();
                e.preventDefault();
            }
        });
    }
}

/**
 * 상품 목록 필터링
 */
function filterProducts() {
    const categoryFilter = document.getElementById('categoryFilter');
    const productSearch = document.getElementById('productSearch');
    const productRows = document.querySelectorAll('.product-row');
    
    const selectedCategory = categoryFilter ? categoryFilter.value : 'all';
    const searchTerm = productSearch ? productSearch.value.toLowerCase() : '';
    
    productRows.forEach(row => {
        const category = row.getAttribute('data-category');
        const productName = row.querySelector('td:nth-child(3)').textContent.toLowerCase();
        
        let showByCategory = selectedCategory === 'all' || category === selectedCategory;
        let showBySearch = !searchTerm || productName.includes(searchTerm);
        
        // 카테고리와 검색어 모두 일치해야 표시
        if (showByCategory && showBySearch) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
    
    // 필터링 결과가 없는 경우 메시지 표시
    const visibleRows = [...productRows].filter(row => row.style.display !== 'none');
    const tableBody = document.querySelector('.product-table tbody');
    
    if (visibleRows.length === 0 && tableBody) {
        // 이미 "결과 없음" 메시지가 있는지 확인
        let noResultsRow = tableBody.querySelector('.no-results');
        if (!noResultsRow) {
            noResultsRow = document.createElement('tr');
            noResultsRow.className = 'no-results';
            noResultsRow.innerHTML = '<td colspan="8" class="no-products">검색 결과가 없습니다.</td>';
            tableBody.appendChild(noResultsRow);
        }
    } else {
        // "결과 없음" 메시지가 있으면 제거
        const noResultsRow = tableBody.querySelector('.no-results');
        if (noResultsRow) {
            noResultsRow.remove();
        }
    }
}

/**
 * 상품 삭제 모달 설정
 */
function setupDeleteModal() {
    const modal = document.getElementById('deleteModal');
    const deleteButtons = document.querySelectorAll('.delete-product');
    const cancelDelete = document.getElementById('cancelDelete');
    const confirmDelete = document.getElementById('confirmDelete');
    
    // 삭제 버튼 클릭 시 모달 표시
    deleteButtons.forEach(button => {
        button.addEventListener('click', function() {
            const productId = this.getAttribute('data-id');
            
            if (modal) {
                modal.style.display = 'block';
                
                // 확인 버튼에 productId 설정
                if (confirmDelete) {
                    confirmDelete.setAttribute('data-id', productId);
                    
                    // href 속성이 있으면 업데이트
                    if (confirmDelete.hasAttribute('href')) {
                        const baseUrl = confirmDelete.getAttribute('href').split('&productId=')[0];
                        confirmDelete.setAttribute('href', baseUrl + '&productId=' + productId);
                    }
                }
            }
        });
    });
      // 취소 버튼 클릭 시 모달 닫기
    if (cancelDelete) {
        cancelDelete.addEventListener('click', function() {
            modal.style.display = 'none';
        });
    }
    
    // 확인 버튼 클릭 시 삭제 요청 전송
    if (confirmDelete) {
        confirmDelete.addEventListener('click', function() {
            const productId = this.getAttribute('data-id');
            if (productId) {
                // 컨텍스트 경로 가져오기
                const contextPath = document.querySelector('meta[name="context-path"]')?.getAttribute('content') || '';
                // 삭제 요청 URL 생성
                window.location.href = contextPath + '/front?key=admin&methodName=productDelete&productId=' + productId;
            }
        });
    }
    
    // 모달 바깥 클릭 시 닫기
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
}