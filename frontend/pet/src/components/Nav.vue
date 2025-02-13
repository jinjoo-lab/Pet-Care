<template>
  <nav class="navbar">
    <div class="nav-logo">
      <router-link to="/" class="logo">
        PetCare
      </router-link>
    </div>
    
    <div class="nav-menu">
      <router-link to="/shop" class="nav-item">쇼핑몰</router-link>
      <router-link to="/pet-management" class="nav-item">내 펫 관리</router-link>
      <router-link to="/pet-sitting" class="nav-item">펫시팅</router-link>
    </div>

    <div class="nav-auth">
      <template v-if="!isLoggedIn">
        <router-link to="/login" class="auth-btn login">로그인</router-link>
        <router-link to="/signup" class="auth-btn signup">회원가입</router-link>
      </template>
      <template v-else>
        <div class="user-menu-container">
          <button @click="toggleMenu" class="user-name">{{ userInfo.name }}님</button>
          <button @click="handleLogout" class="auth-btn logout">로그아웃</button>
          <div v-show="showMenu" class="dropdown-menu">
            <router-link to="/mypage" class="menu-item">마이페이지</router-link>
            <router-link to="/password" class="menu-item">비밀번호 변경</router-link>
            <router-link to="/profile" class="menu-item">회원 정보 변경</router-link>
            <router-link to="/delete-account" class="menu-item delete-account">회원 탈퇴</router-link>
          </div>
        </div>
      </template>
    </div>
  </nav>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import axios from 'axios'

export default {
  name: 'Nav',
  data() {
    return {
      showMenu: false
    }
  },
  computed: {
    ...mapState(['isLoggedIn', 'userInfo'])
  },
  methods: {
    ...mapActions(['logout']),
    toggleMenu() {
      this.showMenu = !this.showMenu
    },
    async handleLogout() {
      try {
        await axios.get('/api/v1/members/auth/logout')
        this.logout()
        this.$router.push('/')
      } catch (error) {
        console.error('로그아웃 실패:', error)
      }
    }
  },
  mounted() {
    // 메뉴 외부 클릭 시 닫기
    document.addEventListener('click', (e) => {
      if (!this.$el.contains(e.target)) {
        this.showMenu = false
      }
    })
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-logo .logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
  text-decoration: none;
}

.nav-logo .logo:hover {
  color: #007bff;
  transition: color 0.3s ease;
}

.nav-menu {
  display: flex;
  gap: 2rem;
}

.nav-item {
  text-decoration: none;
  color: #333;
  font-weight: 500;
  padding: 0.5rem 1rem;
  transition: color 0.3s ease;
}

.nav-item:hover {
  color: #007bff;
}

.nav-auth {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.auth-btn {
  text-decoration: none;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.login {
  color: #007bff;
  border: 1px solid #007bff;
}

.login:hover {
  background-color: #007bff;
  color: white;
}

.signup {
  background-color: #007bff;
  color: white;
  border: 1px solid #007bff;
}

.signup:hover {
  background-color: #0056b3;
  border-color: #0056b3;
}

.user-menu-container {
  position: relative;
}

.user-name {
  background: none;
  border: none;
  color: #333;
  font-weight: 500;
  cursor: pointer;
  padding: 0.5rem 1rem;
  font-size: 1rem;
}

.user-name:hover {
  color: #007bff;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  min-width: 200px;
  margin-top: 0.5rem;
  z-index: 1000;
}

.menu-item {
  display: block;
  padding: 0.75rem 1rem;
  color: #333;
  text-decoration: none;
  transition: background-color 0.3s;
  border: none;
  width: 100%;
  text-align: left;
  background: none;
  font-size: 1rem;
  cursor: pointer;
}

.menu-item:hover {
  background-color: #f8f9fa;
  color: #007bff;
}

.menu-item.logout {
  color: #dc3545;
}

.menu-item.logout:hover {
  background-color: #f8f9fa;
  color: #c82333;
}

@media (max-width: 768px) {
  .navbar {
    padding: 1rem;
  }

  .nav-menu {
    gap: 1rem;
  }

  .nav-item {
    padding: 0.5rem;
  }
}
</style>