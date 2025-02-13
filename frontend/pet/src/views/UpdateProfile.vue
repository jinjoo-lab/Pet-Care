<template>
    <div class="update-profile-container">
      <h2>회원 정보 변경</h2>
      <form @submit.prevent="handleUpdate" class="update-form">
        <!-- 이름 변경 -->
        <div class="form-group">
          <label for="name">이름</label>
          <input 
            type="text" 
            id="name" 
            v-model="formData.name"
            required
            placeholder="이름을 입력하세요"
          >
        </div>
  
        <!-- 전화번호 변경 -->
        <div class="form-group">
          <label for="phone">전화번호</label>
          <input 
            type="tel" 
            id="phone" 
            v-model="formData.phone"
            required
            placeholder="전화번호를 입력하세요"
          >
        </div>
  
        <!-- 변경 버튼 -->
        <button type="submit" class="update-btn">변경</button>
      </form>
    </div>
  </template>
  
  <script>
  import { mapState } from 'vuex'
  import axios from 'axios'
  
  export default {
    name: 'UpdateProfile',
    data() {
      return {
        formData: {
          id: 0,  // 서버에서 현재 로그인된 사용자의 ID를 사용
          name: '',
          phone: ''
        }
      }
    },
    computed: {
      ...mapState(['userInfo'])
    },
    methods: {
      async handleUpdate() {
        try {
          const response = await axios.put('/api/v1/members', {
            id: this.userInfo.id,  // 현재 로그인된 사용자의 ID
            name: this.formData.name,
            phone: this.formData.phone
          })
          
          // Vuex store 업데이트
          this.$store.commit('setUserInfo', {
            ...this.userInfo,
            name: this.formData.name,
            phone: this.formData.phone
          })
          
          alert('회원 정보가 성공적으로 변경되었습니다.')
          this.$router.push('/')
        } catch (error) {
          console.error('회원 정보 변경 실패:', error)
          alert('회원 정보 변경에 실패했습니다.')
        }
      }
    },
    mounted() {
      // 현재 사용자 정보로 폼 초기화
      if (this.userInfo) {
        this.formData.id = this.userInfo.id
        this.formData.name = this.userInfo.name
        this.formData.phone = this.userInfo.phone
      }
    }
  }
  </script>
  
  <style scoped>
  .update-profile-container {
    max-width: 500px;
    margin: 2rem auto;
    padding: 2rem;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
    box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.1);
  }
  
  .update-btn {
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
  
  .update-btn:hover {
    background: #0056b3;
  }
  
  @media (max-width: 768px) {
    .update-profile-container {
      margin: 1rem;
      padding: 1rem;
    }
  }
  </style>