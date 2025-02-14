import { createStore } from 'vuex'
import axios from 'axios'

export default createStore({
  state: {
    isLoggedIn: false,
    userInfo: null,
    petSitterInfo: null
  },
  mutations: {
    setLoggedIn(state, value) {
      state.isLoggedIn = value
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
    },
    setPetSitterInfo(state, petSitterInfo) {
      state.petSitterInfo = petSitterInfo
    }
  },
  actions: {
    login({ commit }, userInfo) {
      commit('setUserInfo', userInfo)
      commit('setLoggedIn', true)
    },
    logout({ commit }) {
      commit('setUserInfo', null)
      commit('setLoggedIn', false)
    },
    updateProfile({ commit }, { name, phone }) {
      commit('updateUserInfo', { name, phone })
    },
    async fetchPetSitterInfo({ commit, state }) {
      try {
        if (!state.userInfo) return null
        
        const response = await axios.get(`/api/v1/petsitter/${state.userInfo.id}`)
        commit('setPetSitterInfo', response.data)
        return response.data
      } catch (error) {
        console.error('펫시터 정보 조회 실패:', error)
        return null
      }
    },
    async registerPetSitter({ commit }, petSitterInfo) {
      commit('setPetSitterInfo', petSitterInfo)
    },
    clearPetSitterInfo({ commit }) {
      commit('setPetSitterInfo', null)
    }
  },
  getters: {
    isPetSitter: state => !!state.petSitterInfo
  }
})