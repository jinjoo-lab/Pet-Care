import { createApp } from 'vue'  // vue를 직접 import 하지 않고 createApp만 import
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import VCalendar from 'v-calendar'
import 'v-calendar/style.css'

// axios 기본 설정
axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.headers.common['Content-Type'] = 'application/json'
axios.defaults.withCredentials = true  // 쿠키 전송을 위해 필요

const app = createApp(App)

// 결제 함수를 전역으로 등록
app.config.globalProperties.$payment = async function(price) {
  const { IMP } = window;
  IMP.init('imp50847785');

  // store에서 사용자 정보 가져오기
  const userInfo = store.state.userInfo;

  const data = {
    pg: 'uplus',
    pay_method: 'card',
    merchant_uid: `mid_${new Date().getTime()}`,
    amount: price,
    name: '펫시팅 서비스',
    buyer_name: userInfo.name,
    buyer_tel: userInfo.phone,
    buyer_email: userInfo.email,
    buyer_addr: '서울특별시 강남구 삼성동', // 기본 주소 유지
    buyer_postcode: '123-456' // 기본 우편번호 유지
  };

  return new Promise((resolve, reject) => {
    IMP.request_pay(data, callback);
    function callback(response) {
      if (response.success) {
        resolve(response);
      } else {
        reject(new Error(response.error_msg));
      }
    }
  });
};

app.use(router)
app.use(store)
app.use(VCalendar, {})
app.mount('#app')