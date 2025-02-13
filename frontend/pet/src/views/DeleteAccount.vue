<template>
    <div class="delete-account-container">
      <div class="delete-section">
        <h1 class="logo">PetCare</h1>
        
        <div class="warning-box">
          <h2>대충 가지 말라는 말</h2>
          <p>회원 탈퇴 시 모든 정보가 삭제되며, 이 작업은 되돌릴 수 없습니다.</p>
        </div>
  
        <form @submit.prevent="handleDeleteAccount" class="delete-form">
          <button type="submit" class="delete-btn">회원 탈퇴</button>
        </form>
      </div>
    </div>
  </template>
  
  <script>
  import { mapState } from 'vuex'
  import axios from 'axios'
  
  export default {
    name: 'DeleteAccount',
    computed: {
      ...mapState(['userInfo'])
    },
    methods: {
      async handleDeleteAccount() {
        if (!confirm('정말로 탈퇴하시겠습니까?')) {
          return
        }
        
        try {
          await axios.delete(`/api/v1/members/${this.userInfo.id}`)
          this.$store.dispatch('logout')
          alert('회원 탈퇴가 완료되었습니다.')
          this.$router.push('/')
        } catch (error) {
          console.error('회원 탈퇴 실패:', error)
          alert('회원 탈퇴에 실패했습니다.')
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .delete-account-container {
    max-width: 500px;
    margin: 3rem auto;
    padding: 0 2rem;
  }
  
  .delete-section {
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
  
  .warning-box {
    background-color: #fff5f5;
    border: 1px solid #feb2b2;
    border-radius: 8px;
    padding: 2rem;
    margin-bottom: 2rem;
  }
  
  .warning-box h2 {
    color: #e53e3e;
    font-size: 1.25rem;
    margin-bottom: 1rem;
  }
  
  .warning-box p {
    color: #4a5568;
    line-height: 1.5;
  }
  
  .delete-form {
    text-align: center;
  }
  
  .delete-btn {
    padding: 1rem 2rem;
    background-color: #e53e3e;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .delete-btn:hover {
    background-color: #c53030;
  }
  
  @media (max-width: 768px) {
    .delete-account-container {
      margin: 1.5rem auto;
      padding: 0 1rem;
    }
  
    .delete-section {
      padding: 1.5rem;
    }
  }
  </style>