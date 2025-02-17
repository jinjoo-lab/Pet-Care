<template>
    <div class="mypage-container">
      <div class="profile-section">
        <h1 class="logo">PetCare</h1>
        
        <div class="profile-menu">
          <div class="menu-item">
            <div class="menu-content">
              <div class="info-group">
                <span class="label">이메일</span>
                <span class="value">{{ userInfo.email }}</span>
              </div>
            </div>
          </div>
  
          <div class="menu-item">
            <div class="menu-content">
              <div class="info-group">
                <span class="label">이름</span>
                <span class="value">{{ userInfo.name }}</span>
              </div>
            </div>
          </div>
  
          <div class="menu-item">
            <div class="menu-content">
              <div class="info-group">
                <span class="label">핸드폰 번호</span>
                <span class="value">{{ userInfo.phone }}</span>
              </div>
            </div>
          </div>
        </div>
  
        <div class="button-container">
          <router-link to="/pets" class="nav-button">
            <button class="pet-button">내 펫 보기</button>
          </router-link>
          <router-link to="/petsitter/info" class="nav-button">
            <button class="petsitter-button">펫시터 정보</button>
          </router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { mapState } from 'vuex'
  
  export default {
    name: 'MyPage',
    computed: {
      ...mapState(['userInfo'])
    }
  }
  </script>
  
  <style scoped>
  .mypage-container {
    max-width: 600px;
    margin: 3rem auto;
    padding: 0 2rem;
  }
  
  .profile-section {
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
  
  .profile-menu {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
    margin-bottom: 3rem;
  }
  
  .menu-item {
    padding: 1.5rem;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    transition: all 0.3s ease;
  }
  
  .menu-item:hover {
    border-color: #007bff;
    box-shadow: 0 2px 4px rgba(0, 123, 255, 0.1);
  }
  
  .menu-content {
    display: flex;
    align-items: center;
  }
  
  .info-group {
    display: flex;
    align-items: center;
    gap: 2rem;
    flex: 1;
  }
  
  .label {
    min-width: 100px;
    color: #666;
    font-size: 1rem;
  }
  
  .value {
    color: #333;
    font-size: 1rem;
    font-weight: 500;
  }
  
  .button-container {
    display: flex;
    gap: 1rem;
    margin-top: 2rem;
    justify-content: center;
  }
  
  .nav-button {
    text-decoration: none;
  }
  
  .pet-button, .petsitter-button {
    padding: 1rem 2rem;
    font-size: 1.1rem;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s ease;
  }
  
  .pet-button {
    background-color: #4CAF50;
    color: white;
  }
  
  .pet-button:hover {
    background-color: #45a049;
  }
  
  .petsitter-button {
    background-color: #2196F3;
    color: white;
  }
  
  .petsitter-button:hover {
    background-color: #1976D2;
  }
  
  @media (max-width: 768px) {
    .mypage-container {
      margin: 1.5rem auto;
      padding: 0 1rem;
    }
  
    .profile-section {
      padding: 1.5rem;
    }
  
    .info-group {
      flex-direction: column;
      align-items: flex-start;
      gap: 0.5rem;
    }
  
    .label {
      min-width: auto;
    }
  }
  </style>