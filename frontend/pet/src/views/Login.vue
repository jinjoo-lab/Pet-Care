<template>
    <div class="login-container">
      <h2>로그인</h2>
      <form @submit.prevent="handleLogin" class="login-form">
        <!-- 이메일 입력 -->
        <div class="form-group">
          <label for="email">이메일</label>
          <input 
            type="email" 
            id="email" 
            v-model="formData.email"
            required
            placeholder="이메일을 입력하세요"
          >
        </div>
  
        <!-- 비밀번호 입력 -->
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input 
            type="password" 
            id="password" 
            v-model="formData.password"
            required
            placeholder="비밀번호를 입력하세요"
          >
        </div>
  
        <!-- 로그인 버튼 -->
        <button 
          type="submit" 
          class="login-btn"
          :disabled="!isFormValid"
        >
          로그인
        </button>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios'
  import { mapActions } from 'vuex'
  
  export default {
    name: 'Login',
    data() {
      return {
        formData: {
          email: '',
          password: ''
        }
      }
    },
    computed: {
      isFormValid() {
        return this.formData.email && this.formData.password
      }
    },
    methods: {
      ...mapActions(['login']),
      async handleLogin() {
        if (!this.isFormValid) return
  
        try {
          const response = await axios.post('/api/v1/members/auth/login', {
            email: this.formData.email,
            password: this.formData.password
          })
          
          // store에 로그인 상태 저장
          this.login(response.data)
          this.$router.push('/')
        } catch (error) {
          console.error('로그인 실패:', error)
          // 에러 처리 로직 추가 가능
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .login-container {
    width: 100%;
    max-width: 400px;
    margin: 0 auto;
    margin-left: 15%; /* 더 왼쪽으로 이동 (20%에서 15%로 변경) */
    padding: 2rem;
  }
  
  .login-form {
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    padding: 2rem;
  }
  
  h2 {
    text-align: center;
    color: #333;
    margin-bottom: 2rem;
  }
  
  .form-group {
    margin-bottom: 1.5rem;
  }
  
  label {
    display: block;
    margin-bottom: 0.5rem;
    color: #333;
    font-weight: 500;
  }
  
  input {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
  }
  
  input:focus {
    outline: none;
    border-color: #007bff;
  }
  
  .login-btn {
    width: 100%;
    padding: 1rem;
    background: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .login-btn:hover {
    background: #0056b3;
  }
  
  .login-btn:disabled {
    background: #ccc;
    cursor: not-allowed;
  }
  
  @media (max-width: 768px) {
    .login-container {
      margin-left: auto; /* 모바일에서는 중앙 정렬 */
      padding: 1rem;
    }
  }
  </style>