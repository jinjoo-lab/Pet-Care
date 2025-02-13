<template>
    <div class="update-password-container">
      <div class="password-section">
        <h1 class="logo">PetCare</h1>
        
        <form @submit.prevent="handleUpdatePassword" class="password-form">
            <div class="input-group">
            <label for="email">이메일</label>
            <input 
                type="email" 
                id="email" 
                :value="userInfo.email"
                readonly
            >
            </div>
  
          <div class="input-group">
            <label for="currentPassword">예전 비밀번호</label>
            <input 
              type="password" 
              id="currentPassword" 
              v-model="formData.currentPassword"
              required
            >
          </div>
  
          <div class="input-group">
            <label for="newPassword">새로운 비밀번호</label>
            <input 
              type="password" 
              id="newPassword" 
              v-model="formData.newPassword"
              required
            >
          </div>
  
          <button type="submit" class="submit-btn">변경</button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import { mapState } from 'vuex'
  import axios from 'axios'
  
  export default {
    name: 'UpdatePassword',
    data() {
      return {
        formData: {
          email: '',
          currentPassword: '',
          newPassword: ''
        }
      }
    },
    computed: {
      ...mapState(['userInfo'])
    },
    methods: {
      async handleUpdatePassword() {
        try {
          await axios.patch('/api/v1/members/passwords', {
            email: this.userInfo.email,
            oldPassword: this.formData.currentPassword,
            newPassword: this.formData.newPassword
          })
          alert('비밀번호가 성공적으로 변경되었습니다.')
          this.$router.push('/mypage')
        } catch (error) {
          console.error('비밀번호 변경 실패:', error)
          alert('비밀번호 변경에 실패했습니다. 현재 비밀번호를 확인해주세요.')
        }
      }
    },
    mounted() {
      if (this.userInfo) {
        this.formData.email = this.userInfo.email
      }
    }
  }
  </script>
  
  <style scoped>
  .update-password-container {
    max-width: 500px;
    margin: 3rem auto;
    padding: 0 2rem;
  }
  
  .password-section {
    background: white;
    border-radius: 12px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    padding: 2.5rem;
  }
  
  .logo {
    font-size: 2rem;
    font-weight: bold;
    color: #333;
    text-align: center;
    margin-bottom: 3rem;
  }
  
  .password-form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .input-group {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  label {
    color: #666;
    font-size: 1rem;
  }
  
  input {
    padding: 1rem;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    font-size: 1rem;
    transition: all 0.3s ease;
  }
  
  input:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.1);
  }
  
  input[readonly] {
    background-color: #f8f9fa;
    cursor: not-allowed;
  }
  
  .submit-btn {
    margin-top: 1rem;
    padding: 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .submit-btn:hover {
    background-color: #0056b3;
  }
  
  @media (max-width: 768px) {
    .update-password-container {
      margin: 1.5rem auto;
      padding: 0 1rem;
    }
  
    .password-section {
      padding: 1.5rem;
    }
  }
  </style>