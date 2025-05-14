<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>로그인 | Green Table</title>
        <link rel="stylesheet" href="${path}/css/user/styles.css" />
        <link rel="stylesheet" href="${path}/css/user/login.css" />
        <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
        />
    </head>
    <body>
        <!-- 헤더 컨테이너: header.html이 로드됩니다 -->
        <div id="header-container"><jsp:include page="/common/header.html"/></div>

        <!-- 메인 컨텐츠 - 로그인 섹션 -->
        <main class="login-container">
            <div class="login-wrapper">
                <h2 class="login-title">로그인</h2>

                <!-- 회원/비회원 탭 -->
                <div class="login-tabs">
                    <button class="tab-btn active" data-tab="member">
                        회원 로그인
                    </button>
                    <button class="tab-btn" data-tab="non-member">
                        비회원 주문조회
                    </button>
                </div>

                <!-- 회원 로그인 폼 -->
                <div class="tab-content active" id="member-tab">
                    <form class="login-form" id="member-login-form" action="${path}/front?key=user&methodName=login" method="POST">
                        <div class="input-group">
                            <label for="member-id">아이디(이메일)</label>
                            <input
                                type="email"
                                id="member-id"
                                name="email"
                                placeholder="이메일 주소를 입력해주세요"
                                required
                            />
                        </div>
                        <div class="input-group">
                            <label for="member-password">비밀번호</label>
                            <input
                                type="password"
                                id="member-password"
                                name="password"
                                placeholder="비밀번호를 입력해주세요"
                                required
                            />
                        </div>
                        <div class="login-options">
                            <div class="save-id">
                                <input
                                    type="checkbox"
                                    id="save-id"
                                    name="saveId"
                                />
                                <label for="save-id">아이디 저장</label>
                            </div>
                            <div class="login-links">
                                <a href="#" id="find-id-pw"
                                    >아이디/비밀번호 찾기</a
                                >
                                <span class="divider">|</span>
                                <a href="${path}/front?key=user&methodName=register" id="register">회원가입</a>
                            </div>
                        </div>
                        <button type="submit" class="login-btn">로그인</button>
                    </form>

                    <!-- 소셜 로그인 -->
                    <div class="social-login">
                        <h3>간편 로그인</h3>
                        <div class="social-btns">
                            <button class="social-btn kakao">
                                <img
                                    src="https://picsum.photos/seed/kakao/20/20"
                                    alt="카카오 로그인"
                                />
                                카카오 로그인
                            </button>
                            <button class="social-btn google">
                                <img
                                    src="https://picsum.photos/seed/google/20/20"
                                    alt="구글 로그인"
                                />
                                구글 로그인
                            </button>
                        </div>
                    </div>
                </div>

                <!-- 비회원 주문조회 폼 -->
                <div class="tab-content" id="non-member-tab">
                    <form class="login-form" id="non-member-login-form">
                        <div class="input-group">
                            <label for="order-name">주문자명</label>
                            <input
                                type="text"
                                id="order-name"
                                name="orderName"
                                placeholder="주문자명을 입력해주세요"
                                required
                            />
                        </div>
                        <div class="input-group">
                            <label for="order-number">주문번호</label>
                            <input
                                type="text"
                                id="order-number"
                                name="orderNumber"
                                placeholder="주문번호를 입력해주세요"
                                required
                            />
                        </div>
                        <div class="input-group">
                            <label for="order-password">비밀번호</label>
                            <input
                                type="password"
                                id="order-password"
                                name="orderPassword"
                                placeholder="주문 시 입력한 비밀번호를 입력해주세요"
                                required
                            />
                        </div>
                        <button type="submit" class="login-btn">
                            주문 조회하기
                        </button>
                    </form>
                    <div class="non-member-info">
                        <p>
                            <i class="fas fa-info-circle"></i>
                            비회원으로 주문하신 경우, 주문 시 입력하신 정보로
                            주문 조회가 가능합니다.
                        </p>
                    </div>
                </div>
            </div>
        </main>
        <!-- 푸터 컨테이너: footer.html이 로드됩니다 -->
        <div id="footer-container"><jsp:include page="/common/footer.html"/></div>

        <!-- 아이디/비밀번호 찾기 모달 -->
        <div class="modal" id="find-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>아이디/비밀번호 찾기</h3>
                    <button class="close-btn">&times;</button>
                </div>
                <div class="modal-tabs">
                    <button class="modal-tab active" data-tab="find-id">
                        아이디 찾기
                    </button>
                    <button class="modal-tab" data-tab="find-pw">
                        비밀번호 찾기
                    </button>
                </div>
                <div class="modal-body">
                    <!-- 아이디 찾기 -->
                    <div class="modal-tab-content active" id="find-id-content">
                        <form id="find-id-form">
                            <div class="input-group">
                                <label for="find-id-name">이름</label>
                                <input
                                    type="text"
                                    id="find-id-name"
                                    name="name"
                                    placeholder="이름을 입력해주세요"
                                    required
                                />
                            </div>
                            <div class="input-group">
                                <label for="find-id-email">이메일 인증</label>
                                <div class="verify-input">
                                    <input
                                        type="email"
                                        id="find-id-email"
                                        name="email"
                                        placeholder="이메일 주소를 입력해주세요"
                                        required
                                    />
                                    <button type="button" class="verify-btn">
                                        인증번호 받기
                                    </button>
                                </div>
                            </div>
                            <div class="input-group">
                                <label for="find-id-code">인증번호</label>
                                <div class="verify-input">
                                    <input
                                        type="text"
                                        id="find-id-code"
                                        name="code"
                                        placeholder="인증번호 6자리를 입력해주세요"
                                        required
                                    />
                                    <button type="button" class="verify-btn">
                                        인증확인
                                    </button>
                                </div>
                                <p class="verify-time">남은시간 05:00</p>
                            </div>
                            <button type="submit" class="login-btn">
                                아이디 찾기
                            </button>
                        </form>
                    </div>

                    <!-- 비밀번호 찾기 -->
                    <div class="modal-tab-content" id="find-pw-content">
                        <form id="find-pw-form">
                            <div class="input-group">
                                <label for="find-pw-id">아이디(이메일)</label>
                                <input
                                    type="email"
                                    id="find-pw-id"
                                    name="email"
                                    placeholder="이메일 주소를 입력해주세요"
                                    required
                                />
                            </div>
                            <div class="input-group">
                                <label for="find-pw-email">이메일 인증</label>
                                <div class="verify-input">
                                    <input
                                        type="email"
                                        id="find-pw-email"
                                        name="verifyEmail"
                                        placeholder="이메일 주소를 입력해주세요"
                                        required
                                    />
                                    <button type="button" class="verify-btn">
                                        인증번호 받기
                                    </button>
                                </div>
                            </div>
                            <div class="input-group">
                                <label for="find-pw-code">인증번호</label>
                                <div class="verify-input">
                                    <input
                                        type="text"
                                        id="find-pw-code"
                                        name="code"
                                        placeholder="인증번호 6자리를 입력해주세요"
                                        required
                                    />
                                    <button type="button" class="verify-btn">
                                        인증확인
                                    </button>
                                </div>
                                <p class="verify-time">남은시간 05:00</p>
                            </div>
                            <button type="submit" class="login-btn">
                                임시 비밀번호 발급
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="${path}/js/user/include.js"></script>
        <script src="${path}/js/user/script.js"></script>
        <script src="${path}/js/user/login.js"></script>
    </body>
</html>
