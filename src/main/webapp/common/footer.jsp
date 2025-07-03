<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib uri="http://java.sun.com/jsp/jstl/core"
prefix="c" %> <%-- contextPath 변수 설정 (이미지 해결방법 적용) --%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<%
    String s3BaseUrl = System.getenv("S3_BASE_URL");
    if (s3BaseUrl == null) {
        s3BaseUrl = "https://greentable-images-your-region.s3.ap-northeast-2.amazonaws.com";
    }
    pageContext.setAttribute("s3BaseUrl", s3BaseUrl);
%>

<link rel="stylesheet" href="${path}/css/common/reset.css" />
<link rel="stylesheet" href="${path}/css/common/footer.css" />

<footer>
  <div class="footer-top">
    <ul class="footer-links">
      <li><a href="#">회사소개</a></li>
      <li><a href="#">이용약관</a></li>
      <li><a href="#">개인정보취급방침</a></li>
      <li><a href="#">이용안내</a></li>
    </ul>
    <div class="social-icons">
      <a href="#"
        ><img
          src="https://saladpanda.co.kr/web/upload/appfiles/ZaReJam3QiELznoZeGGkMG/1f817a003944bc1aa1b005822ad5ebe0.png"
          alt="Instagram"
      /></a>
      <a href="#"
        ><img
          src="https://atowertr6856.cdn-nhncommerce.com/data/skin/front/kaimen_pc_n/img/new/sns_you.png"
          alt="YouTube"
      /></a>
      <a href="#"
        ><img
          src="https://saladpanda.co.kr/web/upload/appfiles/ZaReJam3QiELznoZeGGkMG/da19b4af763831dc8eecbfb65f411334.png"
          alt="FaceBook"
      /></a>
      <a href="#"
        ><img
          src="https://saladpanda.co.kr/web/upload/appfiles/ZaReJam3QiELznoZeGGkMG/b1e5b4800814de764cc04664112f6d26.png"
          alt="KakaoTalk"
      /></a>
    </div>
  </div>
  <div class="footer-bottom">
    <div class="footer-logo">
      <a><img src="${path}/image/logo_2.png" alt="로고" /></a>
    </div>
    <div class="company-info">
      <div class="company-info-detail">
        <p>상호: (주)그린테이블 | 대표: 코스타 | TEL: 1877-1111</p>
        <p>
          사업자등록번호: 295-88-00110 | 통신판매업신고번호: 2025-경기성남-1004
        </p>
        <p>
          주소: 경기도 성남시 성남대로 34 하나프라자 6층 | E-Mail:
          cs@greentable.site
        </p>
      </div>
      <div class="copyright">
        <p>Copyright © 2025 GreenTable, All Rights Reserved.</p>
      </div>
    </div>
    <div class="support">
      <h3>고객센터</h3>
      <p class="customer-number">1800-0700</p>
      <p>평일 오전 9:00 ~ 오후 6:00</p>
      <p>토, 일, 공휴일 휴무</p>
      <p>카카오톡 @그린테이블 친구 추가하고 소식과 혜택을 받아보세요.</p>
    </div>
  </div>
</footer>
