import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import SignUp from '@/views/SignUp.vue'
import Login from '@/views/Login.vue'  // 추가
import UpdateProfile from '@/views/UpdateProfile.vue'  // 추가
import MyPage from '../views/MyPage.vue'
import UpdatePassword from '../views/UpdatePassword.vue'
import DeleteAccount from '../views/DeleteAccount.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
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
      component: MyPage
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
    }
  ]
})

export default router