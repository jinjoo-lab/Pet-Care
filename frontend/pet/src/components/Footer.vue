<template>
    <footer class="footer">
      <div class="footer-content">
        <!-- 연락처 정보 -->
        <div class="contact-info">
          <h3>Contact Us</h3>
          <p><i class="fas fa-envelope"></i> email@example.com</p>
          <p><i class="fas fa-phone"></i> +82 10-1234-5678</p>
          <p><i class="fas fa-map-marker-alt"></i> 서울특별시 강남구</p>
        </div>
  
        <!-- 소셜 미디어 아이콘 -->
        <div class="social-icons">
          <a href="#" class="social-icon"><i class="fab fa-facebook"></i></a>
          <a href="#" class="social-icon"><i class="fab fa-twitter"></i></a>
          <a href="#" class="social-icon"><i class="fab fa-instagram"></i></a>
          <a href="#" class="social-icon"><i class="fab fa-github"></i></a>
        </div>
      </div>
    </footer>
  </template>
  
  <style scoped>
  .footer {
    background-color: #333;
    color: white;
    padding: 2rem 0;
    margin-top: 2rem;
  }
  
  .footer-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .contact-info h3 {
    margin-bottom: 1rem;
  }
  
  .contact-info p {
    margin: 0.5rem 0;
  }
  
  .social-icons {
    display: flex;
    gap: 1rem;
  }
  
  .social-icon {
    color: white;
    font-size: 1.5rem;
    transition: color 0.3s ease;
  }
  
  .social-icon:hover {
    color: #007bff;
  }
  </style>