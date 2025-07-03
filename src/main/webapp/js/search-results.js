/**
 * 검색 결과 페이지 관련 JavaScript
 */
document.addEventListener('DOMContentLoaded', function() {
    // 정렬 옵션 변경 이벤트 처리
    const sortSelect = document.getElementById('sort-select');
    if (sortSelect) {
        sortSelect.addEventListener('change', function() {
            const selectedOption = this.value;
            const currentUrl = new URL(window.location.href);
            
            // 기존 URL에 정렬 옵션 파라미터 추가/변경
            currentUrl.searchParams.set('sort', selectedOption);
            
            // 페이지 번호 초기화 (정렬 변경 시 첫 페이지로)
            currentUrl.searchParams.set('pageNo', '1');
            
            // 새 URL로 이동
            window.location.href = currentUrl.toString();
        });
    }

    // 맨 위로 버튼 기능
    const backToTopBtn = document.createElement('button');
    backToTopBtn.id = 'back-to-top';
    backToTopBtn.className = 'back-to-top-btn';
    backToTopBtn.innerHTML = '<i class="fas fa-arrow-up"></i>';
    document.body.appendChild(backToTopBtn);

    // 스크롤 이벤트 리스너
    window.addEventListener('scroll', function() {
        if (window.pageYOffset > 300) {
            backToTopBtn.classList.add('visible');
        } else {
            backToTopBtn.classList.remove('visible');
        }
    });

    // 버튼 클릭 이벤트
    backToTopBtn.addEventListener('click', function() {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });

    // 빠른 필터 기능 - 가격대별 필터링
    const filterContainer = document.createElement('div');
    filterContainer.className = 'price-filter-container';
    filterContainer.innerHTML = `
        <div class="price-filter-header">가격대 필터</div>
        <div class="price-filter-options">
            <button class="price-filter-btn" data-min="0" data-max="10000">~1만원</button>
            <button class="price-filter-btn" data-min="10000" data-max="20000">1만원~2만원</button>
            <button class="price-filter-btn" data-min="20000" data-max="30000">2만원~3만원</button>
            <button class="price-filter-btn" data-min="30000" data-max="50000">3만원~5만원</button>
            <button class="price-filter-btn" data-min="50000" data-max="100000000">5만원~</button>
            <button class="price-filter-btn" data-min="0" data-max="100000000">전체보기</button>
        </div>
    `;
    
    // 정렬 컨테이너 다음에 가격 필터 추가
    const sortOptionsSection = document.querySelector('.sort-options');
    if (sortOptionsSection) {
        sortOptionsSection.after(filterContainer);
        
        // 가격 필터 버튼 이벤트 처리
        const filterButtons = document.querySelectorAll('.price-filter-btn');
        filterButtons.forEach(button => {
            button.addEventListener('click', function() {
                const minPrice = parseInt(this.getAttribute('data-min'));
                const maxPrice = parseInt(this.getAttribute('data-max'));
                
                // 모든 상품 카드 선택
                const productCards = document.querySelectorAll('.product-card');
                
                // 선택된 필터 버튼 스타일 변경
                filterButtons.forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');
                
                // 각 상품 카드를 순회하며 가격 범위에 맞는지 확인
                productCards.forEach(card => {
                    const priceElement = card.querySelector('.final-price');
                    if (priceElement) {
                        // 가격 문자열에서 숫자만 추출 (예: "15,000원" -> 15000)
                        const priceText = priceElement.textContent;
                        const price = parseInt(priceText.replace(/[^0-9]/g, ''));
                        
                        if (price >= minPrice && price <= maxPrice) {
                            card.style.display = '';
                        } else {
                            card.style.display = 'none';
                        }
                    }
                });
                
                // 필터링 결과 카운트 업데이트
                const visibleCount = document.querySelectorAll('.product-card[style="display: none;"]').length;
                const totalCount = productCards.length;
                const searchResultCount = document.querySelector('.search-result-count');
                
                if (searchResultCount) {
                    if (minPrice === 0 && maxPrice === 100000000) {
                        searchResultCount.textContent = `총 ${totalCount}개의 상품이 검색되었습니다.`;
                    } else {
                        searchResultCount.textContent = `총 ${totalCount - visibleCount}개의 상품이 표시되고 있습니다. (전체 ${totalCount}개)`;
                    }
                }
            });
        });
    }

    // 상품 카드 애니메이션 효과
    const productCards = document.querySelectorAll('.product-card');
    
    // 페이지 로드 시 상품 카드에 순차적으로 애니메이션 적용
    productCards.forEach((card, index) => {
        // 초기 상태 설정
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
        
        // 순차적으로 표시
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 100 * index);
        
        // 호버 효과 개선
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-10px)';
            this.style.boxShadow = '0 15px 30px rgba(0, 0, 0, 0.15)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '';
        });
    });
    
    // 이미지 지연 로딩 구현
    const lazyImages = document.querySelectorAll('.product-image img');
    
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    const src = img.getAttribute('data-src');
                    
                    if (src) {
                        img.src = src;
                        img.removeAttribute('data-src');
                    }
                    
                    imageObserver.unobserve(img);
                }
            });
        });
        
        lazyImages.forEach(img => {
            // 데이터 속성이 있는 경우에만 관찰
            if (img.getAttribute('data-src')) {
                imageObserver.observe(img);
            }
        });
    }
});
