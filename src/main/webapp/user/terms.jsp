<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>이용약관 | Green Table</title>
        <link rel="stylesheet" href="${path}/css/user/styles.css" />
        <link rel="stylesheet" href="${path}/css/user/register.css" />
        <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
        />
    </head>
    <body>
        <!-- 헤더 컨테이너: header.html이 로드됩니다 -->
        <div id="header-container"></div>

        <!-- 메인 컨텐츠 - 이용약관 섹션 -->
        <main class="register-container">
            <div class="register-wrapper">
                <h2 class="register-title">이용약관</h2>

                <!-- 회원가입 단계 표시 -->
                <div class="register-step">
                    <div class="step active">
                        <div class="step-circle">1</div>
                        <div class="step-text">이용약관 동의</div>
                    </div>
                    <div class="step-line"></div>
                    <div class="step">
                        <div class="step-circle">2</div>
                        <div class="step-text">회원정보 입력</div>
                    </div>
                    <div class="step-line"></div>
                    <div class="step">
                        <div class="step-circle">3</div>
                        <div class="step-text">가입완료</div>
                    </div>
                </div>

                <!-- 이용약관 동의 영역 -->
                <div class="terms-container">
                    <form id="terms-form" method="get" action="${path }/front?key=user&methodName=register">
                    	<input type="hidden" name="key" value="user">
                    	<input type="hidden" name="methodName" value="register">
                        <!-- 전체 동의 체크박스 -->
                        <div class="terms-all-check">
                            <div class="checkbox-wrap">
                                <input
                                    type="checkbox"
                                    id="check-all"
                                    
                                />
                                <label for="check-all"
                                    >이용약관 및 개인정보보호 수집(선택)에 모두
                                    동의합니다.</label
                                >
                            </div>
                            <p class="terms-sub-text">
                                이용약관 및 개인정보보호 수집 및 이용에
                                동의합니다.
                            </p>
                        </div>

                        <!-- 이용약관 항목 -->
                        <div class="terms-item">
                            <div class="checkbox-wrap">
                                <input
                                    type="checkbox"
                                    id="terms-service"
                                   
                                    class="terms-checkbox"
                                    required
                                />
                                <label for="terms-service"
                                    >이용약관 동의 (필수)</label
                                >
                            </div>
                            <div class="terms-content">
                                <div class="terms-text-area" tabindex="0">
                                    이 약관은 (주)그린테이블(전자상거래
                                    사업자)가 운영하는 그린테이블 사이버 몰에서
                                    제공하는 인터넷 관련 서비스를 이용함에 있어
                                    사이버 몰과 이용자의 권리·의무 및 책임사항을
                                    규정함을 목적으로 합니다. ① 몰은 이용자가
                                    제공한 개인정보가 어떠한 용도와 방식으로
                                    이용되고 있으며, 개인정보보호를 위해 어떠한
                                    조치를 취하고 있는지를 개인정보처리방침을
                                    통해 소비자가 알 수 있도록 운영합니다. ②
                                    PC통신, 모바일 및 무선 등을 이용하는
                                    전자상거래에 대해 그 성질상 맞지 않는 경우는
                                    적용되지 않을 수 있습니다. ③ 회원가입 절차를
                                    완료하는 순간부터 회원은 본 약관에 동의한
                                    것으로 간주합니다.
                                </div>
                            </div>
                        </div>

                        <!-- 개인정보 수집 항목 -->
                        <div class="terms-item">
                            <div class="checkbox-wrap">
                                <input
                                    type="checkbox"
                                    id="terms-privacy"
                                    
                                    class="terms-checkbox"
                                    required
                                />
                                <label for="terms-privacy"
                                    >개인정보 수집 및 이용 동의 (필수)</label
                                >
                            </div>
                            <div class="terms-content">
                                <div class="terms-text-area" tabindex="0">
                                    이 약관은 (주)그린테이블(이하 '회사')가
                                    제공하는 서비스 이용과 관련하여 회사와 회원
                                    간의 권리, 의무 및 기타 필요한 사항을
                                    규정함을 목적으로 합니다. 1. 수집하는
                                    개인정보 항목 - 필수항목: 이메일(아이디),
                                    비밀번호, 이름, 주소, 휴대폰 번호, 생년월일
                                    - 선택항목: 일반전화 2. 개인정보의 수집 및
                                    이용 목적 - 회원제 서비스 이용, 본인 확인,
                                    가입 의사 확인, 부정 이용 방지 - 상품 배송,
                                    주문/배송 조회, 결제, 환불, 고객 상담 -
                                    이벤트 및 마케팅 활동 3. 개인정보의 보유 및
                                    이용 기간 - 회원 탈퇴 시까지 또는 회원
                                    가입일로부터 5년 - 관련 법령에 의한 정보
                                    보호 사유에 따라 일정 기간 보존
                                </div>
                            </div>
                        </div>

                        <!-- 개인정보 선택 수집 항목 -->
                        <div class="terms-item">
                            <div class="checkbox-wrap">
                                <input
                                    type="checkbox"
                                    id="terms-marketing"
                                    
                                    class="terms-checkbox"
                                />
                                <label for="terms-marketing"
                                    >개인정보 수집 및 이용 동의 (선택)</label
                                >
                            </div>
                            <div class="terms-content">
                                <div class="terms-text-area" tabindex="0">
                                    이 약관은 (주)그린테이블(이하 '회사')가
                                    제공하는 서비스 이용자의 개인정보 보호를
                                    위해 회사가 수집하는 선택적 개인정보에 관한
                                    사항을 규정합니다. 1. 수집하는 개인정보 항목
                                    - 선택항목: 마케팅 정보 수신 동의,
                                    이메일/SMS 수신 동의, 생년월일, 성별 2.
                                    개인정보의 수집 및 이용 목적 - 맞춤형 서비스
                                    제공 - 이벤트 및 프로모션 안내 - 신제품 및
                                    신규 서비스 안내 - 마케팅 및 광고에 활용 3.
                                    개인정보의 보유 및 이용 기간 - 회원 탈퇴 시
                                    또는 동의 철회 시까지 ※ 선택항목의 동의를
                                    거부하시더라도 기본 서비스 이용에는 제한이
                                    없습니다.
                                </div>
                            </div>
                        </div>

                        <!-- 위치정보 수집 및 이용 동의 -->
                        <div class="terms-item">
                            <div class="checkbox-wrap">
                                <input
                                    type="checkbox"
                                    id="terms-location"
                                   
                                    class="terms-checkbox"
                                />
                                <label for="terms-location"
                                    >위치정보 수집 및 이용 동의 (선택)</label
                                >
                            </div>
                            <div class="terms-content">
                                <div class="terms-text-area" tabindex="0">
                                    이 약관은 (주)그린테이블(이하 '회사')가
                                    제공하는 서비스 이용자의 위치정보를 수집하고
                                    이용함에 있어 필요한 사항을 규정합니다. 1.
                                    위치정보 수집 방법 - 이용자 단말기의 GPS
                                    센서, 와이파이, Bluetooth 수신 정보, 기지국
                                    정보 2. 위치정보의 수집 및 이용 목적 -
                                    이용자 위치 기반의 맞춤형 서비스 제공 - 상점
                                    위치 정보 제공, 주변 상점 안내 - 배송 현황
                                    제공 3. 위치정보의 보유 및 이용 기간 - 회원
                                    탈퇴 시 또는 동의 철회 시까지 ※ 선택항목의
                                    동의를 거부하시더라도 기본 서비스 이용에는
                                    제한이 없습니다. ※ 위치정보 이용 동의를
                                    거부할 경우, 위치 기반 서비스 이용이 제한될
                                    수 있습니다.
                                </div>
                            </div>
                        </div>

                        <!-- SMS 수신 동의 -->
                        <div class="terms-item">
                            <div class="checkbox-wrap">
                                <input
                                    type="checkbox"
                                    id="terms-sms"
                                    
                                    class="terms-checkbox"
                                />
                                <label for="terms-sms"
                                    >SMS 수신 동의 (선택)</label
                                >
                            </div>
                            <div class="terms-content">
                                <div class="terms-text-area" tabindex="0">
                                    이 약관은 (주)그린테이블(이하 '회사')가
                                    제공하는 서비스 이용자에게 SMS를 통한 정보
                                    제공을 위해 필요한 사항을 규정합니다. 1. SMS
                                    수신 정보 내용 - 주문/배송/결제/환불 등
                                    서비스 이용 관련 정보 - 이벤트, 프로모션,
                                    할인 혜택 등 마케팅 정보 - 새로운 상품 및
                                    서비스 안내 2. SMS 수신 정보 제공 목적 -
                                    서비스 이용 관련 주요 정보 제공 - 맞춤형
                                    혜택 및 프로모션 안내 3. SMS 수신 동의 철회
                                    - 마이페이지 > 회원정보 수정에서 SMS 수신
                                    동의를 철회할 수 있습니다. - 고객센터를 통해
                                    SMS 수신 동의를 철회할 수 있습니다. ※
                                    선택항목의 동의를 거부하시더라도 기본 서비스
                                    이용에는 제한이 없습니다.
                                </div>
                            </div>
                        </div>

                        <!-- 이메일 수신 동의 -->
                        <div class="terms-item">
                            <div class="checkbox-wrap">
                                <input
                                    type="checkbox"
                                    id="terms-email"
                                    
                                    class="terms-checkbox"
                                />
                                <label for="terms-email"
                                    >이메일 수신 동의 (선택)</label
                                >
                            </div>
                            <div class="terms-content">
                                <div class="terms-text-area" tabindex="0">
                                    이 약관은 (주)그린테이블(이하 '회사')가
                                    제공하는 서비스 이용자에게 이메일을 통한
                                    정보 제공을 위해 필요한 사항을 규정합니다.
                                    1. 이메일 수신 정보 내용 -
                                    주문/배송/결제/환불 등 서비스 이용 관련 정보
                                    - 이벤트, 프로모션, 할인 혜택 등 마케팅 정보
                                    - 새로운 상품 및 서비스 안내 - 회사 소식 및
                                    뉴스레터 2. 이메일 수신 정보 제공 목적 -
                                    서비스 이용 관련 주요 정보 제공 - 맞춤형
                                    혜택 및 프로모션 안내 - 회사와 이용자 간
                                    정보 전달 3. 이메일 수신 동의 철회 -
                                    마이페이지 > 회원정보 수정에서 이메일 수신
                                    동의를 철회할 수 있습니다. - 발송되는 이메일
                                    하단의 수신거부 링크를 통해 수신 동의를
                                    철회할 수 있습니다. - 고객센터를 통해 이메일
                                    수신 동의를 철회할 수 있습니다. ※ 선택항목의
                                    동의를 거부하시더라도 기본 서비스 이용에는
                                    제한이 없습니다.
                                </div>
                            </div>
                        </div>

                        <!-- 다음 단계 버튼 -->
                        <div class="btn-area">
                            <button type="button" class="cancel-btn">
                                취소
                            </button>
                            <button
                                type="submit"
                                id="terms-next-btn"
                                class="next-btn"
                                disabled
                            >
                                다음 단계
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </main>
        <!-- 푸터 컨테이너: footer.html이 로드됩니다 -->
        <div id="footer-container"></div>
        <script src="${path}/js/user/include.js"></script>
        <script src="${path}/js/user/script.js"></script>
        <script src="${path}/js/user/terms.js"></script>
    </body>
</html>
