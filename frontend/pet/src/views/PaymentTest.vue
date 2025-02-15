<template>
    <div>
        <button @click="onPayment">
        결제하기
        </button>
    </div>
</template>

<script setup>
    const onPayment = () => {
/* 1. 가맹점 식별하기 */
    const { IMP } = window;
    IMP.init('imp50847785'); //고객사 식별코드 입력시 실제 결제가됨
/* 2. 결제 데이터 정의하기 */
    const data = {
        pg: 'uplus', 
        pay_method: 'card', // 결제수단
        merchant_uid: `mid_${new Date().getTime()}`, // 주문번호
        amount: 1000, // 결제금액
        name: '뉴진스 싸인', // 주문명
        buyer_name: '홍길동', // 구매자 이름
        buyer_tel: '01012341234', // 구매자 전화번호
        buyer_email: 'baroq8@gmail.com', // 구매자 이메일
        buyer_addr: '신사동 661-16', // 구매자 주소
        buyer_postcode: '06018' // 구매자 우편번호
    };
/* 4. 결제 창 호출하기 */
    IMP.request_pay(data, callback);
};

const callback = (response) => {
/* 3. 콜백 함수 정의하기 */
    const {
    success,
    merchant_uid,
    error_msg
    } = response;
    console.log(response);
    if (success) {alert('결제 성공');
    } else {
        alert(`결제 실패: ${error_msg}`);
    }
};
</script>