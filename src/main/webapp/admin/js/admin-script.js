/**
 * 관리자 패널 공통 JavaScript
 */
document.addEventListener('DOMContentLoaded', function() {
    // 로그아웃 버튼 이벤트 리스너 등록
    const logoutBtn = document.querySelector('.btn-danger');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function() {
            if (confirm('로그아웃 하시겠습니까?')) {
                // 로그아웃 처리
                window.location.href = '/';
            }
        });
    }

    // 모바일 환경에서 사이드바 토글 기능 추가 (필요시)
    // ...

    // 현재 페이지 활성화 표시 (URL 기반으로 자동 감지)
    highlightActiveMenu();
});

/**
 * 현재 페이지에 해당하는 메뉴를 활성화 상태로 표시
 */
function highlightActiveMenu() {
    const currentPath = window.location.pathname;
    const currentSearch = window.location.search;
    
    const navLinks = document.querySelectorAll('.admin-nav li a');
    
    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        
        // URL에 해당 메뉴의 경로가 포함되어 있으면 활성화
        if ((currentPath + currentSearch).includes(href) && href !== '/admin') {
            link.parentElement.classList.add('active');
        }
    });
}