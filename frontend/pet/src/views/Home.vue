<template>
    <header class="header">
      <div class="header-content">
        <!-- 메인 헤더 텍스트 -->
        <div class="header-text">
          <h1>반려동물과 함께하는 행복한 시간</h1>
          <p>전문적인 펫케어 서비스로 소중한 가족을 케어해드립니다</p>
          
          <!-- CTA 버튼 -->
          <div class="header-buttons">
            <router-link to="/services" class="btn primary-btn">서비스 알아보기</router-link>
            <router-link to="/consultation" class="btn secondary-btn">무료 상담</router-link>
          </div>
        </div>
  
        <!-- 헤더 이미지 -->
        <div class="header-image">
          <img src="@/assets/dog.webp" alt="Happy Pet" class="main-image">
        </div>
      </div>
  
      <!-- 주요 서비스 특징 -->
      <div class="feature-cards">
        <div class="feature-card">
          <i class="fas fa-heart"></i>
          <h3>전문 펫시터</h3>
          <p>검증된 전문가가 케어</p>
        </div>
        <div class="feature-card">
          <i class="fas fa-clock"></i>
          <h3>24/7 서비스</h3>
          <p>언제든 이용 가능</p>
        </div>
        <div class="feature-card">
          <i class="fas fa-shield-alt"></i>
          <h3>안전 보장</h3>
          <p>펫 케어 보험 적용</p>
        </div>
      </div>
    </header>
  </template>
  
  <script>
  import axios from 'axios'

  export default {
    name: 'Home',
    async created() {
      try {
        await axios.get('/api/v1/petsitter/init');
      } catch (error) {
        console.error('펫시터 초기화 실패:', error);
      }
    }
  }
  </script>
  
  <style scoped>
  .header {
    padding: 4rem 2rem;
    background: linear-gradient(to right, #f8f9fa, #e9ecef);
  }
  
  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    gap: 2rem;
  }
  
  .header-text {
    flex: 1;
  }
  
  .header-text h1 {
    font-size: 2.5rem;
    color: #333;
    margin-bottom: 1rem;
    line-height: 1.2;
  }
  
  .header-text p {
    font-size: 1.2rem;
    color: #666;
    margin-bottom: 2rem;
  }
  
  .header-buttons {
    display: flex;
    gap: 1rem;
  }
  
  .btn {
    padding: 0.8rem 1.5rem;
    border-radius: 8px;
    text-decoration: none;
    font-weight: 600;
    transition: all 0.3s ease;
  }
  
  .primary-btn {
    background-color: #007bff;
    color: white;
  }
  
  .primary-btn:hover {
    background-color: #0056b3;
  }
  
  .secondary-btn {
    background-color: white;
    color: #007bff;
    border: 2px solid #007bff;
  }
  
  .secondary-btn:hover {
    background-color: #f8f9fa;
  }
  
  .header-image {
    flex: 1;
    display: flex;
    justify-content: center;
  }
  
  .main-image {
    max-width: 100%;
    height: auto;
    border-radius: 10px;
  }
  
  .feature-cards {
    max-width: 1200px;
    margin: 3rem auto 0;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 2rem;
  }
  
  .feature-card {
    background: white;
    padding: 1.5rem;
    border-radius: 10px;
    text-align: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;
  }
  
  .feature-card:hover {
    transform: translateY(-5px);
  }
  
  .feature-card i {
    font-size: 2rem;
    color: #007bff;
    margin-bottom: 1rem;
  }
  
  .feature-card h3 {
    color: #333;
    margin-bottom: 0.5rem;
  }
  
  .feature-card p {
    color: #666;
  }
  
  /* 반응형 디자인 */
  @media (max-width: 768px) {
    .header-content {
      flex-direction: column;
      text-align: center;
    }
  
    .header-buttons {
      justify-content: center;
    }
  
    .feature-cards {
      grid-template-columns: 1fr;
      padding: 0 1rem;
    }
  }
  </style>