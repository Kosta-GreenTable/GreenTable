/**
 * 상품 문의 폼 자바스크립트
 */
document.addEventListener('DOMContentLoaded', function() {
    // 문의 내용 글자수 카운트
    const contentTextarea = document.getElementById('qnaContent');
    const textLengthDisplay = document.querySelector('.text-length');
    
    if (contentTextarea && textLengthDisplay) {
        // 초기 글자수 표시
        const initialLength = contentTextarea.value.length;
        textLengthDisplay.textContent = initialLength + '/500자';
        
        // 입력 이벤트에 따른 글자수 업데이트
        contentTextarea.addEventListener('input', function() {
            const currentLength = this.value.length;
            textLengthDisplay.textContent = currentLength + '/500자';
            
            // 500자 제한
            if (currentLength > 500) {
                this.value = this.value.substring(0, 500);
                textLengthDisplay.textContent = '500/500자';
                textLengthDisplay.style.color = '#ff6b6b'; // 초과시 빨간색으로 표시
            } else {
                textLengthDisplay.style.color = '#888'; // 정상일 때 원래 색상
            }
        });
    }
    
    // 폼 제출 전 유효성 검사
    const qnaForm = document.getElementById('qnaForm');
    
    if (qnaForm) {
        qnaForm.addEventListener('submit', function(e) {
            const titleInput = document.getElementById('qnaTitle');
            const contentInput = document.getElementById('qnaContent');
            
            // 제목 유효성 검사
            if (titleInput && titleInput.value.trim() === '') {
                e.preventDefault();
                alert('제목을 입력해주세요.');
                titleInput.focus();
                return false;
            }
            
            // 내용 유효성 검사
            if (contentInput && contentInput.value.trim() === '') {
                e.preventDefault();
                alert('문의 내용을 입력해주세요.');
                contentInput.focus();
                return false;
            }
            
            // 내용 길이 제한
            if (contentInput && contentInput.value.length > 500) {
                e.preventDefault();
                alert('문의 내용은 500자를 초과할 수 없습니다.');
                contentInput.focus();
                return false;
            }
            
            return true;
        });
    }
});
