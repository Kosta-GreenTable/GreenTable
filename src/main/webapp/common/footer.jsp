<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 푸터 -->
<footer>
  <div class="footer-content">
    <div class="footer-column">
      <h3>그린테이블</h3>      <ul>
        <li><a href="<c:url value='/about.jsp'/>">회사 소개</a></li>
        <li><a href="<c:url value='/terms.jsp'/>">이용약관</a></li>
        <li><a href="<c:url value='/privacy.jsp'/>">개인정보처리방침</a></li>
        <li><a href="<c:url value='/partnership.jsp'/>">제휴문의</a></li>
      </ul>
    </div>
    <div class="footer-column">
      <h3>고객센터</h3>
      <ul>
        <li>전화: 1588-1234</li>
        <li>이메일: support@greentable.com</li>
        <li>주소: 서울시 강남구 그린로 123</li>
        <li>운영시간: 평일 9:00 - 18:00</li>
      </ul>
    </div>
    <div class="footer-column">
      <h3>SNS</h3>      <div class="social-links">
        <a href="https://facebook.com/greentable" target="_blank"><i class="fab fa-facebook-f"></i></a>
        <a href="https://instagram.com/greentable" target="_blank"><i class="fab fa-instagram"></i></a>
        <a href="https://twitter.com/greentable" target="_blank"><i class="fab fa-twitter"></i></a>
        <a href="https://youtube.com/greentable" target="_blank"><i class="fab fa-youtube"></i></a>
      </div>
    </div>
    <div class="footer-column">
      <h3>모바일 앱</h3>
      <div class="app-links">
        <a href="#"
          ><img
            src="https://picsum.photos/seed/app-store/120/40"
            alt="App Store"
        /></a>
        <a href="#"
          ><img
            src="https://picsum.photos/seed/google-play/120/40"
            alt="Google Play"
        /></a>
      </div>
    </div>
  </div>
  <div class="copyright">
    <p>© 2025 Green Table. All rights reserved.</p>
  </div>
</footer>