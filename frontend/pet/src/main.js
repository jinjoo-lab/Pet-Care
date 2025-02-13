import { createApp } from 'vue'  // vue를 직접 import 하지 않고 createApp만 import
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'

// axios 기본 설정
axios.defaults.baseURL = 'http://localhost:8080'
axios.defaults.headers.common['Content-Type'] = 'application/json'
axios.defaults.withCredentials = true  // 쿠키 전송을 위해 필요

const app = createApp(App)
app.use(router)
app.use(store)
app.mount('#app')