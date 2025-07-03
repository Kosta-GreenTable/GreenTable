document.addEventListener('DOMContentLoaded', function() {
  const searchInput = document.getElementById('search-input');
  const searchButton = document.getElementById('search-button');
  
  // 검색창에 입력시 실행되는 이벤트
  searchInput.addEventListener('input', function() {
    const searchTerm = this.value.trim();
    if (searchTerm.length >= 1) { // 한 글자부터 검색 실행
      fetchSearchResults(searchTerm);
    } else {
      hideSearchResults();
    }
  });
  
  // 검색 버튼 클릭시 검색 페이지로 이동
  searchButton.addEventListener('click', function() {
    const searchTerm = searchInput.value.trim();
    if (searchTerm.length > 0) {
      window.location.href = `${contextPath}/front?key=product&methodName=search&query=${encodeURIComponent(searchTerm)}`;
    }
  });
  
  // 엔터키 검색 처리
  searchInput.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
      const searchTerm = this.value.trim();
      if (searchTerm.length > 0) {
        window.location.href = `${contextPath}/front?key=product&methodName=search&query=${encodeURIComponent(searchTerm)}`;
      }
      e.preventDefault();
    }
  });

  // 실시간 검색 결과 가져오기
  function fetchSearchResults(query) {
    fetch(`${contextPath}/front?key=product&methodName=searchAjax&query=${encodeURIComponent(query)}`)
      .then(response => response.json())
      .then(data => {
        displaySearchResults(data);
      })
      .catch(error => {
        console.error('검색 오류:', error);
      });
  }
  
  // 검색 결과 표시
  function displaySearchResults(products) {
    let resultsContainer = document.getElementById('search-results');
    
    if (!resultsContainer) {
      resultsContainer = document.createElement('div');
      resultsContainer.id = 'search-results';
      resultsContainer.className = 'search-results-dropdown';
      document.querySelector('.search-box').appendChild(resultsContainer);
    }
    
    if (products.length === 0) {
      resultsContainer.innerHTML = '<div class="no-results">검색 결과가 없습니다</div>';
      resultsContainer.style.display = 'block';
      return;
    }
    
    // S3 Base URL 가져오기
    const s3BaseUrl = window.s3BaseUrl || 'https://greentable-images-your-region.s3.ap-northeast-2.amazonaws.com';
    
    let html = '';
    products.forEach(product => {
      // S3 이미지 URL 구성
      let imageUrl;
      if (product.mainImageName && product.mainImageName.trim() !== '') {
        // mainImageName이 이미 전체 경로인 경우와 파일명만인 경우 모두 처리
        if (product.mainImageName.startsWith('http')) {
          imageUrl = product.mainImageName;
        } else {
          imageUrl = `${s3BaseUrl}/${product.mainImageName}`;
        }
      } else {
        imageUrl = `${s3BaseUrl}/products/no-image.jpg`;
      }
        
      html += `
        <div class="search-result-item" onclick="window.location.href='${contextPath}/front?key=product&methodName=detail&productId=${product.productId}'">
          <div class="search-result-image">
            <img src="${imageUrl}" alt="${product.name}" onerror="this.src='${s3BaseUrl}/products/no-image.jpg'">
          </div>
          <div class="search-result-info">
            <div class="search-result-name">${product.name}</div>
            <div class="search-result-category">${product.category || ''}</div>
            <div class="search-result-price">${formatPrice(product.price)}원</div>
          </div>
        </div>
      `;
    });
    
    resultsContainer.innerHTML = html;
    resultsContainer.style.display = 'block';
  }
  
  // 검색 결과 숨기기
  function hideSearchResults() {
    const resultsContainer = document.getElementById('search-results');
    if (resultsContainer) {
      resultsContainer.style.display = 'none';
    }
  }
  
  // 가격 포맷팅
  function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }
  
  // 바깥 영역 클릭시 검색 결과 숨기기
  document.addEventListener('click', function(event) {
    const searchBox = document.querySelector('.search-box');
    if (searchBox && !searchBox.contains(event.target)) {
      hideSearchResults();
    }
  });
});
