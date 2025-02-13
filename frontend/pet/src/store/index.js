import { createStore } from 'vuex'

export default createStore({
  state: {
    isLoggedIn: false,
    userInfo: null
  },
  mutations: {
    setLoginStatus(state, status) {
      state.isLoggedIn = status
    },
    setUserInfo(state, userInfo) {
      state.userInfo = userInfo
    },
    updateUserInfo(state, { name, phone }) {
      if (state.userInfo) {
        state.userInfo = {
          ...state.userInfo,
          name,
          phone
        }
      }
    }
  },
  actions: {
    login({ commit }, userInfo) {
      commit('setUserInfo', userInfo)
      commit('setLoginStatus', true)
    },
    logout({ commit }) {
      commit('setUserInfo', null)
      commit('setLoginStatus', false)
    },
    updateProfile({ commit }, { name, phone }) {
      commit('updateUserInfo', { name, phone })
    }
  }
})