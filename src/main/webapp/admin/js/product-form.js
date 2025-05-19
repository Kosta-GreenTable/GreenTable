/**
 * 상품 등록/수정 폼 JavaScript
 */
document.addEventListener('DOMContentLoaded', function() {
    // 이미지 미리보기 기능
    setupImagePreviews();
    
    // 폼 취소 버튼
    setupCancelButton();
    
    // 폼 유효성 검사
    setupFormValidation();
});

/**
 * 이미지 미리보기 기능 설정
 */
function setupImagePreviews() {
    // 대표 이미지 미리보기
    const mainImageInput = document.getElementById('productImage0');
    const mainImagePreview = document.getElementById('mainImagePreview');
    
    if (mainImageInput && mainImagePreview) {
        mainImageInput.addEventListener('change', function() {
            previewImage(this, mainImagePreview.querySelector('img'));
        });
    }
    
    // 추가 이미지 미리보기
    for (let i = 1; i <= 3; i++) {
        const additionalImageInput = document.getElementById('productImage' + i);
        const additionalImagePreview = document.getElementById('additionalImagePreview' + i);
        
        if (additionalImageInput && additionalImagePreview) {
            additionalImageInput.addEventListener('change', function() {
                previewImage(this, additionalImagePreview.querySelector('img'));
            });
        }
    }
}

/**
 * 이미지 파일 미리보기 표시
 * @param {HTMLInputElement} input 파일 입력 요소
 * @param {HTMLImageElement} imgElement 이미지 요소
 */
function previewImage(input, imgElement) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        
        reader.onload = function(e) {
            imgElement.src = e.target.result;
        };
        
        reader.readAsDataURL(input.files[0]);
    }
}

/**
 * 취소 버튼 설정
 */
function setupCancelButton() {
    const cancelBtn = document.getElementById('cancelBtn');
    
    if (cancelBtn) {
        cancelBtn.addEventListener('click', function() {
            const confirmMessage = '입력한 내용이 저장되지 않습니다. 취소하시겠습니까?';
            
            if (confirm(confirmMessage)) {
                window.location.href = cancelBtn.getAttribute('data-return-url') || 
                                      window.location.origin + '/front?key=admin&methodName=productList';
            }
        });
    }
}

/**
 * 폼 유효성 검사 설정
 */
function setupFormValidation() {
    const productForm = document.getElementById('productForm');
    
    if (productForm) {
        productForm.addEventListener('submit', function(e) {
            // 필수 입력 필드 검사
            const name = document.getElementById('name');
            const category = document.getElementById('category');
            const price = document.getElementById('price');
            const stock = document.getElementById('stock');
            
            let isValid = true;
            
            if (!name.value.trim()) {
                alert('상품명을 입력해주세요.');
                name.focus();
                isValid = false;
                e.preventDefault();
                return;
            }
            
            if (!category.value) {
                alert('카테고리를 선택해주세요.');
                category.focus();
                isValid = false;
                e.preventDefault();
                return;
            }
            
            if (!price.value || parseInt(price.value) < 0) {
                alert('유효한 가격을 입력해주세요.');
                price.focus();
                isValid = false;
                e.preventDefault();
                return;
            }
            
            if (!stock.value || parseInt(stock.value) < 0) {
                alert('유효한 재고 수량을 입력해주세요.');
                stock.focus();
                isValid = false;
                e.preventDefault();
                return;
            }
            
            // 이미지 검사
            const mainImageInput = document.getElementById('productImage0');
            const isUpdateForm = window.location.href.includes('productUpdateForm');
            
            // 새로운 상품 등록 시에만 대표 이미지 필수
            if (!isUpdateForm && (!mainImageInput.files || !mainImageInput.files[0])) {
                alert('대표 이미지를 선택해주세요.');
                isValid = false;
                e.preventDefault();
                return;
            }
            
            // 이미지 파일 크기 및 유형 검사
            const imageInputs = [
                document.getElementById('productImage0'),
                document.getElementById('productImage1'),
                document.getElementById('productImage2'),
                document.getElementById('productImage3')
            ];
            
            for (const input of imageInputs) {
                if (input && input.files && input.files[0]) {
                    const file = input.files[0];
                    
                    // 파일 크기 제한 (5MB)
                    if (file.size > 5 * 1024 * 1024) {
                        alert('이미지 크기가 너무 큽니다. 5MB 이하의 이미지를 선택해주세요.');
                        input.focus();
                        isValid = false;
                        e.preventDefault();
                        return;
                    }
                    
                    // 이미지 유형 검사
                    const validTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/jpg'];
                    if (!validTypes.includes(file.type)) {
                        alert('유효한 이미지 파일이 아닙니다. JPG, PNG, GIF 형식의 이미지를 선택해주세요.');
                        input.focus();
                        isValid = false;
                        e.preventDefault();
                        return;
                    }
                }
            }
            
            // 폼 제출 전 확인
            if (isValid && !confirm('상품 정보를 저장하시겠습니까?')) {
                e.preventDefault();
            }
        });
    }
}