<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:set var="user" value="${sessionScope.loginUser}" />

<!-- 로그인 체크 -->
<c:if test="${empty sessionScope.loginUser}">
    <jsp:forward page="/user/auth-required.jsp" />
</c:if>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>회원정보 수정 | Green Table</title>
    <link rel="stylesheet" href="${path}/css/common/styles.css" />
    <link rel="stylesheet" href="${path}/css/user/mypage.css" />
    <link rel="stylesheet" href="${path}/css/user/myinfo.css" />
    <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
    <script
        src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="${path}/js/user/register.js"></script>
    <script src="${path}/js/user/myinfo.js"></script>
</head>
<body data-context-path="${path}"> <!-- data-context-path 설정 -->
    <jsp:include page="/common/header.jsp" />

    <!-- 메인 컨텐츠 -->
    <main class="mypage-container">
        <h1 class="page-title">회원정보 수정</h1>
        <div class="mypage-content">
            <div class="mypage-sidebar">
                <div class="user-profile">
                    <div class="profile-image">
                        <i class="fas fa-user-circle"></i>
                    </div>
                    <div class="user-info">
                        <p class="user-name">${sessionScope.loginUser.userInfoDto.userName}님</p>
                        <button class="profile-edit-btn">회원정보수정</button>
                    </div>
                </div>                <nav class="sidebar-menu">
                    <h3>나의 쇼핑정보</h3>
                    <ul>
                        <li><a href="${path}/front?key=mypage&methodName=mypage">주문/배송 조회</a></li>
                        <li><a href="${path}/user/mycancel.jsp">취소/반품 내역</a></li>
                        <li><a href="${path}/user/mypoint.jsp">적립금 내역</a></li>
                        <li><a href="${path}/user/mycoupon.jsp">쿠폰 내역</a></li>
                        <li><a href="${path}/front?key=review&methodName=myReviews">상품 리뷰</a></li>
                        <li><a href="${path}/front?key=qna&methodName=myQnas">상품 문의</a></li>
                    </ul>

                    <h3>나의 계정설정</h3>
                    <ul>
                        <li class="active"><a href="${path}/user/myinfo.jsp">회원정보 수정</a></li>
                    </ul>
                </nav>
            </div>            <c:if test="${empty sessionScope.loginUser}">
                <jsp:forward page="/user/auth-required.jsp" />
            </c:if>

            <!-- 회원정보 수정 메인 폼 -->
            <div class="mypage-main">
                <section class="member-info-section">
                    <div class="section-header">
                        <h3>회원정보 수정</h3>
                        <p>연락처 등의 정보를 정확하게 입력해주세요.</p>
                    </div>
                    <div class="register-form-container">
                        <form id="memberInfoForm" class="register-form" method="post">
                            <!-- 이메일과 이름 -->
                            <input type="hidden" id="userId" name="userId" value="${user.userId}" />
                            <div class="form-group email-name-group">
                                <label for="userEmail">이메일</label> 
                                <input type="text" id="userEmail" name="userEmail" value="${user.email}" readonly />
                                <span class="input-guide">이메일은 변경이 불가능합니다.</span>
                            </div>

                            <div class="form-group email-name-group">
                                <label for="userName">이름</label> 
                                <input type="text" id="userName" name="userName" value="${user.userInfoDto.userName}" readonly /> 
                                <span class="input-guide">이름은 변경이 불가능합니다. (개명하셨을 경우, 고객센터로 연락주세요.)</span>
                            </div>

                            <!-- 비밀번호와 비밀번호 확인 -->
                            <div class="form-group password-group">
                                <label for="userPassword">비밀번호</label> 
                                <input type="password" id="userPassword" name="password" placeholder="변경할 비밀번호 입력" />
                                <span class="input-guide">※ 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합하여 10~16자리로 입력해주세요.</span>
                            </div>

                            <div class="form-group password-group">
                                <label for="userPasswordConfirm">비밀번호 확인</label> 
                                <input type="password" id="userPasswordConfirm" name="passwordConfirm" placeholder="변경할 비밀번호 입력 확인" /> 
                                <span class="input-guide">※ 위에 입력한 비밀번호를 다시 한번 입력해주세요.</span>
                            </div>                            <!-- 휴대전화 -->
                            <div class="form-group">
                                <label for="mobile">휴대전화<span class="required">*</span></label>
                                <div class="phone-group">
                                    <select id="mobile-first" required>
                                        <option value="010" ${fn:startsWith(user.userInfoDto.phone, '010') ? 'selected' : ''}>010</option>
                                        <option value="011" ${fn:startsWith(user.userInfoDto.phone, '011') ? 'selected' : ''}>011</option>
                                        <option value="016" ${fn:startsWith(user.userInfoDto.phone, '016') ? 'selected' : ''}>016</option>
                                        <option value="017" ${fn:startsWith(user.userInfoDto.phone, '017') ? 'selected' : ''}>017</option>
                                        <option value="018" ${fn:startsWith(user.userInfoDto.phone, '018') ? 'selected' : ''}>018</option>
                                        <option value="019" ${fn:startsWith(user.userInfoDto.phone, '019') ? 'selected' : ''}>019</option>
                                    </select> 
                                    <span class="phone-dash">-</span> 
                                    <input type="text" id="mobile-middle" maxlength="4" placeholder="XXXX" 
                                           value="${not empty user.userInfoDto.phone ? fn:substring(user.userInfoDto.phone, 3, 7) : ''}" required />
                                    <span class="phone-dash">-</span> 
                                    <input type="text" id="mobile-last" maxlength="4" placeholder="XXXX" 
                                           value="${not empty user.userInfoDto.phone ? fn:substring(user.userInfoDto.phone, 7, 11) : ''}" required />
                                </div>
                            </div>

                            <!-- 주소 -->
                            <div class="form-group address-group">
                                <label for="address">주소<span class="required">*</span></label>
                                <div class="input-with-button">
                                    <input type="text" id="zipCode" name="zipCode" placeholder="우편번호" 
                                           value="${user.userInfoDto.zipCode > 0 ? user.userInfoDto.zipCode : ''}" readonly required />
                                    <button type="button" class="find-address-btn">주소찾기</button>
                                </div>
                                <input type="text" id="address1" name="address1" placeholder="기본주소" 
                                       value="${user.userInfoDto.address}" readonly required /> 
                                <input type="text" id="address2" name="address2" placeholder="상세주소를 입력하세요" 
                                       value="${user.userInfoDto.detailAddress}" required />
                            </div>

                            <!-- 버튼 영역 -->
                            <div class="btn-area">
                                <button type="button" class="btn-withdraw">회원 탈퇴</button>
                                <div class="btn-right-group">
                                    <button type="reset" class="btn-cancel">취소</button>
                                    <button type="button" class="btn-save">저장</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </section>
            </div>
        </div>

        <!-- 회원 탈퇴 모달 -->
        <div id="withdrawModal" class="modal" style="display: none;">
            <div class="modal-content">
                <span class="close-modal">&times;</span>
                <h2>회원 탈퇴</h2>
                <p>탈퇴를 원하시면 아래 내용을 입력해주세요.</p>

                <label> 
                    <input type="checkbox" id="withdrawAgree" /> 탈퇴에 동의합니다.
                </label>

                <div>
                    <label for="withdrawPassword">비밀번호 입력</label> 
                    <input type="password" id="withdrawPassword" placeholder="비밀번호 입력" />
                </div>

                <div>
                    <label for="withdrawReason">탈퇴 사유</label> 
                    <select id="withdrawReason">
                        <option value="">선택하세요</option>
                        <option value="이용 빈도 낮음">이용 빈도 낮음</option>
                        <option value="서비스 불만">서비스 불만</option>
                        <option value="개인 정보 우려">개인 정보 우려</option>
                        <option value="기타">기타</option>
                    </select>
                </div>

                <div>
                    <label for="withdrawReasonDetail">상세 사유 (선택)</label> 
                    <input type="text" id="withdrawReasonDetail" />
                </div>

                <div class="modal-buttons">
                    <button class="btn-withdraw-confirm">탈퇴하기</button>
                    <button class="btn-cancel">취소</button>
                </div>
            </div>
        </div>

    </main>
</body>
</html>
