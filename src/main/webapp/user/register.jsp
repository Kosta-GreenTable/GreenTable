<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>회원가입 | Green Table</title>
<link rel="stylesheet" href="${path }/css/user/styles.css" />
<link rel="stylesheet" href="${path }/css/user/register.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" />
</head>
<body>
	<!-- 헤더 컨테이너 -->
	<jsp:include page="/common/header.html" />

	<!-- 메인 컨텐츠 - 회원가입 섹션 -->
	<main class="register-container">
		<div class="register-wrapper">
			<h2 class="register-title">회원가입</h2>

			<!-- 회원가입 단계 표시 -->
			<div class="register-step">
				<div class="step completed">
					<div class="step-circle">1</div>
					<div class="step-text">이용약관 동의</div>
				</div>
				<div class="step-line completed"></div>
				<div class="step active">
					<div class="step-circle">2</div>
					<div class="step-text">회원정보 입력</div>
				</div>
				<div class="step-line"></div>
				<div class="step">
					<div class="step-circle">3</div>
					<div class="step-text">가입완료</div>
				</div>
			</div>

			<!-- 회원가입 폼 -->
			<div class="register-form-container">
				<form id="register-form" method="post"
					action="register-success.html">
					<div class="form-group">
						<label for="email">이메일 (아이디)<span
							class="required">*</span></label>
						<div class="input-with-button">
							<input type="email" id="email" name="email"
								placeholder="이메일을 입력하세요" required />
							<button type="button" class="verify-email-btn">이메일 인증</button>
						</div>
						<p class="form-help">※ 이메일 형식으로 입력해주세요. (예:
							example@greentable.com)</p>
					</div>

					<div class="form-group hidden" id="verification-code-group">
						<label for="verification-code">인증번호<span
							class="required">*</span></label>
						<div class="input-with-button">
							<input type="text" id="verification-code"
								name="verification-code" placeholder="인증번호를 입력하세요" />
							<button type="button" class="verify-code-btn">인증확인</button>
						</div>
						<p class="verification-time">
							남은시간: <span id="timer">05:00</span>
						</p>
					</div>

					<div class="form-group">
						<label for="password">비밀번호<span
							class="required">*</span></label> <input type="password" id="password"
							name="password" placeholder="비밀번호를 입력하세요" required />
						<p class="form-help">※ 영문 대/소문자, 숫자, 특수문자 중 2가지 이상 조합하여
							10~16자리로 입력해주세요.</p>
					</div>

					<div class="form-group">
						<label for="password-confirm">비밀번호 확인<span
							class="required">*</span></label> <input type="password"
							id="password-confirm" name="password-confirm"
							placeholder="비밀번호를 다시 입력하세요" required />
					</div>

					<div class="form-group">
						<label for="name">이름<span class="required">*</span></label> <input
							type="text" id="name" name="name" placeholder="이름을 입력하세요"
							required />
					</div>

					<div class="form-group">
						<label for="address">주소<span class="required">*</span></label>
						<div class="address-group">
							<div class="input-with-button">
								<input type="text" id="zipCode" name="zipCode"
									placeholder="우편번호" readonly required />
								<button type="button" class="find-address-btn">주소찾기</button>
							</div>
							<input type="text" id="address1" name="address1"
								placeholder="기본주소" readonly required /> <input
								type="text" id="address2" name="address2"
								placeholder="상세주소를 입력하세요" required />
						</div>
					</div>

				

					<div class="form-group">
						<label for="mobile">휴대전화<span
							class="required">*</span></label>
						<div class="phone-group">
							<select id="mobile-first"  required>
								<option value="010">010</option>
								<option value="011">011</option>
								<option value="016">016</option>
								<option value="017">017</option>
								<option value="018">018</option>
								<option value="019">019</option>
							</select> <span class="phone-dash">-</span> <input type="text"
								id="mobile-middle"  maxlength="4"
								placeholder="XXXX" required /> <span
								class="phone-dash">-</span> <input type="text" id="mobile-last"
								 maxlength="4" placeholder="XXXX" required />
						</div>
					</div>

				

					<!-- 회원가입 버튼 -->
					<div class="btn-area">
						<button type="button" class="cancel-btn">취소</button>
						<button type="submit" class="submit-btn">회원가입</button>
					</div>
				</form>
			</div>
		</div>
	</main>

	<!-- 푸터 컨테이너 -->
	<jsp:include page="/common/footer.html" />

	<!-- 우편번호 검색 모달 -->
	<div id="address-modal" class="modal">
		<div class="modal-content">
			<div class="modal-header">
				<h3>주소 찾기</h3>
				<button type="button" class="close-btn">&times;</button>
			</div>
			<div class="modal-body">
				<div class="search-address">
					<input type="text" id="search-zipcode"
						placeholder="도로명주소 또는 지번주소를 입력하세요" />
					<button type="button" id="search-address-btn">검색</button>
				</div>
				<div class="address-list">
					<p class="no-result">검색어를 입력하신 후 검색해 주세요.</p>
					<ul id="address-results">
						<!-- 검색 결과가 여기에 표시됩니다 -->
					</ul>
				</div>
			</div>
		</div>
	</div>

	<!-- 이메일 인증 완료 모달 -->
	<div id="email-verify-modal" class="modal">
		<div class="modal-content">
			<div class="modal-header">
				<h3>이메일 인증 완료</h3>
				<button type="button" class="close-btn">&times;</button>
			</div>
			<div class="modal-body">
				<div class="verify-success">
					<i class="fas fa-check-circle"></i>
					<p>이메일 인증이 완료되었습니다.</p>
				</div>
				<div class="modal-btn-area">
					<button type="button" class="modal-confirm-btn">확인</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 가입완료 페이지 대신 모달로 표시 -->
	<div id="register-success-modal" class="modal">
		<div class="modal-content">
			<div class="modal-header">
				<h3>회원가입 완료</h3>
				<button type="button" class="close-btn">&times;</button>
			</div>
			<div class="modal-body">
				<div class="register-success">
					<i class="fas fa-check-circle"></i>
					<p>회원가입이 완료되었습니다.</p>
					<p>그린테이블의 회원이 되신 것을 환영합니다!</p>
					<p>로그인 후 다양한 혜택을 누려보세요.</p>
				</div>
				<div class="modal-btn-area">
					<button type="button" id="go-login-btn" class="modal-confirm-btn">
						로그인하기</button>
					<button type="button" id="go-main-btn" class="modal-sub-btn">
						메인으로 가기</button>
				</div>
			</div>
		</div>
	</div>
	<script src="${path }/js/user/script.js"></script>
	<script src="${path }/js/user/register.js"></script>
</body>
</html>
