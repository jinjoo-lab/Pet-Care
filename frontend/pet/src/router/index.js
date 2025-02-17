import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import SignUp from '@/views/SignUp.vue'
import Login from '@/views/Login.vue'  // 추가
import UpdateProfile from '@/views/UpdateProfile.vue'  // 추가
import MyPage from '../views/MyPage.vue'
import UpdatePassword from '../views/UpdatePassword.vue'
import DeleteAccount from '../views/DeleteAccount.vue'
import PetsitterRegister from '@/views/petsitter/Register.vue'
import PetsitterSchedule from '@/views/petsitter/Schedule.vue'
import PetList from '@/views/pet/PetList.vue'
import FindSitter from '@/views/pet/FindSitter.vue'
import PetsitterInfo from '@/views/petsitter/Info.vue'
import CodeMgtPage from '@/views/pet/CodeMgtPage.vue'
import store from '@/store'
import PaymentTest from '@/views/PaymentTest.vue'
const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/signup',
    name: 'signup',
    component: SignUp
  },
  {
    path: '/login',    // 추가
    name: 'login',
    component: Login
  },
  {
    path: '/profile',
    name: 'UpdateProfile',
    component: UpdateProfile
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: () => import('@/views/MyPage.vue')
  },
  {
    path: '/password',
    name: 'UpdatePassword',
    component: UpdatePassword
  },
  {
    path: '/delete-account',
    name: 'DeleteAccount',
    component: DeleteAccount
  },
  {
    path: '/petsitter/register',
    name: 'PetsitterRegister',
    component: PetsitterRegister,
    meta: { requiresAuth: true } // 로그인이 필요한 페이지
  },
  {
    path: '/petsitter/schedule',
    name: 'PetsitterSchedule',
    component: PetsitterSchedule,
    meta: { requiresAuth: true }
  },
  {
    path: '/pets',
    name: 'PetList',
    component: () => import('@/views/pet/PetList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/find-sitter',
    name: 'FindSitter',
    component: FindSitter,
    meta: { requiresAuth: true }
  },
  {
    path: '/petsitter/info',
    name: 'PetSitterInfo',
    component: () => import('@/views/petsitter/Info.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/code-management',
    name: 'CodeManagement',
    component: CodeMgtPage
  },
  {
    path: '/payment-test',
    name: 'PaymentTest',
    component: PaymentTest
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 네비게이션 가드 설정
router.beforeEach((to, from, next) => {
  const isLoggedIn = store.state.isLoggedIn
  
  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
  } else {
    next()
  }
})

export default router