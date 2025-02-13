<template>
  <div id="app">
    <Navbar />
    <main class="main-content">
      <transition name="fade" mode="out-in">
        <router-view></router-view>
      </transition>
    </main>
    <Footer />
  </div>
</template>

<script>
import Navbar from './components/Nav.vue'
import Footer from './components/Footer.vue'
import axios from 'axios'  // axios import 추가
import { mapActions } from 'vuex'

export default {
  name: 'App',
  components: {
    Navbar,
    Footer
  },
  methods: {
    ...mapActions(['login']),
    async checkSession() {
      try {
        const response = await axios.get('/api/v1/members/auth/check')
        if (response.data) {
          this.login(response.data)
        }
      } catch (error) {
        console.error('세션 체크 실패:', error)
      }
    }
  },
  mounted() {
    this.checkSession()
  }
}
</script>

<style>
/* 전역 리셋 및 기본 스타일 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  line-height: 1.6;
  color: #333;
  background-color: #f8f9fa;
}

/* 앱 컨테이너 */
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 메인 컨텐츠 영역 수정 */
.main-content {
  min-height: calc(100vh - 120px); /* 헤더/푸터 고려한 최소 높이 */
  padding: 40px 0;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #f0f0f0; /* 회색 배경으로 변경 */
}

/* 카드 스타일 수정 */
.card {
  background: white;
  border-radius: 12px; /* 모서리 더 부드럽게 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05); /* 그림자 더 부드럽게 */
  padding: 2rem;
  width: 100%;
  max-width: 1000px; /* 최대 너비 설정 */
  margin: 0 auto;
}

/* 컨테이너 스타일 수정 */
.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 40px; /* 좌우 여백 증가 */
}

/* 버튼 공통 스타일 */
.btn {
  display: inline-block;
  padding: 0.8rem 1.5rem;
  border-radius: 5px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  text-decoration: none;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #545b62;
}

/* 페이지 전환 애니메이션 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 반응형 디자인 수정 */
@media (max-width: 768px) {
  .main-content {
    padding: 20px 0;
  }

  .container {
    padding: 0 20px;
  }

  .card {
    padding: 1.5rem;
  }
}

/* 폼 공통 스타일 */
.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-group input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.1);
}

/* 유틸리티 클래스 */
.text-center {
  text-align: center;
}

.mt-1 { margin-top: 0.5rem; }
.mt-2 { margin-top: 1rem; }
.mt-3 { margin-top: 1.5rem; }
.mt-4 { margin-top: 2rem; }

.mb-1 { margin-bottom: 0.5rem; }
.mb-2 { margin-bottom: 1rem; }
.mb-3 { margin-bottom: 1.5rem; }
.mb-4 { margin-bottom: 2rem; }

/* 에러 메시지 스타일 */
.error-message {
  color: #dc3545;
  font-size: 0.875rem;
  margin-top: 0.25rem;
}

/* 성공 메시지 스타일 */
.success-message {
  color: #28a745;
  font-size: 0.875rem;
  margin-top: 0.25rem;
}

main {
  min-height: calc(100vh - 60px);  /* 헤더/푸터를 제외한 최소 높이 설정 */
  padding: 40px 0;  /* 상하 여백 추가 */
}

/* 반응형 여백 조정 */
@media (max-width: 768px) {
  .container {
    padding: 0 15px;
  }
  
  main {
    padding: 20px 0;
  }
}
</style>