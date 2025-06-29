const contextPath = document.body.dataset.contextPath || '';
const merchantUid = document.getElementById("merchantUid").value;


// 결제하기 버튼
document.getElementById("payBtn").addEventListener("click", async function(e) {
    e.preventDefault();
    console.log("결제 시작 - merchantUid: " + merchantUid);

    // 유효성 검사
    if (!validateForm()) return;

    // 결제 초기화 정보 요청
    await initPayment();
});

// 결제 초기화 함수
async function initPayment() {
    try {
        // 서버에서 결제 초기화 정보 가져오기
        const response = await fetch(`${contextPath}/ajax?key=orderRest&methodName=getPaymentInitInfo`);
        const data = await response.json();
        
        if (!data.success || !data.merchantUid) throw new Error("결제 초기화 실패");
        
        console.log("initPayment merchantUid:", data.merchantUid);
        requestPayment(data.merchantUid);
  
    } catch (error) {
        console.error('결제 초기화 오류:', error);
        Swal.fire({
            position: 'center',
            icon: 'error',
            title: '결제 초기화 오류',
            text: '결제 초기화 중 오류가 발생했습니다',
            confirmButtonText: '확인'
        });
    }
}

// 폼 유효성 검사
function validateForm() {
    // 필수 입력 필드 검사
    const requiredFields = document.querySelectorAll('[required]');
    for (const field of requiredFields) {
        if (!field.value.trim()) {
            Swal.fire({
                position: 'center',
                icon: 'warning',
                title: '입력 오류',
                text: '필수 항목을 모두 입력해 주세요.',
                confirmButtonText: '확인'
            });
            field.focus();
            return false;
        }
    }
    
    // 약관 동의 체크
    const termsChecks = document.querySelectorAll('.agree-checkbox');
    for (const check of termsChecks) {
        if (!check.checked) {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: '약관 동의 오류',
                text: '필수 약관에 동의해 주세요.',
                confirmButtonText: '확인'
            });
            check.focus();
            return false;
        }
    }
    
    // 비회원일 경우 비밀번호 유효성 검사
    const password = document.getElementById("password")?.value.trim() ?? '';
    const passwordConfirm = document.getElementById("passwordConfirm")?.value.trim() ?? '';
    if (password || passwordConfirm) {
        if (password !== passwordConfirm) {
            Swal.fire({
                title: '비밀번호 오류',
                text: '비밀번호와 확인이 일치하지 않습니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
            document.getElementById("passwordConfirm").focus();
            return false;
        }
        if (password.length < 4) {
            Swal.fire({
                title: '비밀번호 오류',
                text: '비밀번호는 최소 4자리 이상이어야 합니다.',
                icon: 'error',
                confirmButtonText: '확인'
            });
            document.getElementById("password").focus();
            return false;
        }
    }
 
    return true;
} //폼 유효성검사 끝


// 포트원 결제 요청 함수
function requestPayment(merchantUid) {
    // 결제버튼 비활성화 - 중복 결제 방지
    const payBtn = document.getElementById("payBtn");
    payBtn.disabled = true;

    // 주문 정보 계산
    const totalAmount = calculateTotalAmount();
    document.getElementById('totalAmount').value = totalAmount;
    
    // 주문자 정보 (null 방어)
    const buyerName = document.getElementById('name')?.value ?? '';
    const buyerTel = document.querySelector('select[name="phonePrefix"]')?.value ?? '' + "-" + 
                    document.getElementById('phone1')?.value ?? '' + "-" + 
                    document.getElementById('phone2')?.value ?? '';
    const buyerEmail = `${document.getElementById('email1')?.value.trim() ?? ''}@${document.getElementById('email2')?.value.trim() ?? ''}`;
    
    // 상품명 구성
    const productNames = document.querySelectorAll('.product-description h4');
    let orderName = '그린테이블 상품';
    if (productNames.length > 0) {
        orderName = productNames[0].textContent;
        if (productNames.length > 1) {
            orderName += ` 외 ${productNames.length - 1}건`;
        }
    }
    
    // 포트원 결제 요청
    const { IMP } = window;
    IMP.init('imp54237185');
    
    IMP.request_pay({
        pg: 'uplus',
        pay_method: 'card',
        merchant_uid: merchantUid,
        name: orderName,
        amount: totalAmount,
        buyer_name: buyerName,
        buyer_email: buyerEmail,
        buyer_tel: buyerTel,
    }, function(rsp) {
        if (rsp.success) {
            // 서버에 결제 검증 요청
            verifyPayment(rsp);
        } else {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: '결제 실패',
                text: '결제에 실패했습니다: ' + rsp.error_msg,
                confirmButtonText: '확인'
            });
            const payBtn = document.getElementById("payBtn");
            payBtn.disabled = false;
        }
    });
}

// 결제 검증 함수
async function verifyPayment(rsp) {
    try {
        const response = await fetch(`${contextPath}/ajax?key=orderRest&methodName=verifyPayment`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                impUid:      rsp.imp_uid,
                merchantUid: rsp.merchant_uid,
                amount:      rsp.paid_amount
              })
        });
        
        const data = await response.json();
        
        if (data.success) {
            // 결제 검증 성공 시 폼에 값 설정 후 제출
            document.getElementById('impUid').value = rsp.imp_uid;
            //document.getElementById('paymentMethod').value = 'rsp.pay_method';
            document.getElementById('totalAmount').value = rsp.paid_amount;
            document.getElementById('paymentStatus').value = '결제 성공';
            document.getElementById('orderForm').submit();
        } else {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: '결제 검증 실패',
                text: '결제 검증에 실패했습니다: ' + data.message,
                confirmButtonText: '확인'
            });
            const payBtn = document.getElementById("payBtn");
            payBtn.disabled = false;
            document.getElementById('impUid').value = '';
            document.getElementById('paymentMethod').value = '';
            document.getElementById('paymentStatus').value = '결제 실패';
        }
    } catch (error) {
        console.error('결제 검증 오류:', error);
        Swal.fire({
            position: 'center',
            icon: 'error',
            title: '검증 오류',
            text: '결제 검증 중 오류가 발생했습니다',
            confirmButtonText: '확인'
        });
        const payBtn = document.getElementById("payBtn");
        payBtn.disabled = false;
    }
}

// 총 결제 금액 계산
function calculateTotalAmount() {
    // 상품 가격 합계
    const totalPriceElement = document.querySelector('.total-price strong');
    if (totalPriceElement) {
        const cleaned = totalPriceElement.textContent.replace(/[^\d]/g, '');
        return isNaN(cleaned) ? 0 : parseInt(cleaned, 10); // 빈문자열이면 NaN 반환되므로 0으로 대체
    }
    return 0;
}
