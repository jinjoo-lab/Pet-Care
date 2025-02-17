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
    setPetSitterInfo(state, info) {
      state.petSitterInfo = info;
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
        if (!state.userInfo) return;
        
        const response = await axios.get(`/api/v1/petsitter/${state.userInfo.id}`);
        commit('setPetSitterInfo', response.data);
      } catch (error) {
        console.error('펫시터 정보 조회 실패:', error);
        commit('setPetSitterInfo', null);
      }
    },
    async registerPetSitter({ commit, dispatch }, petSitterInfo) {
      try {
        // 펫시터 등록 API 호출
        const response = await axios.post('/api/v1/petsitter', petSitterInfo);
        // 등록 성공 후 펫시터 정보 조회하여 store에 저장
        await dispatch('fetchPetSitterInfo');
        return response;
      } catch (error) {
        console.error('펫시터 등록 실패:', error);
        throw error;
      }
    },
    clearPetSitterInfo({ commit }) {
      commit('setPetSitterInfo', null)
    }
  },
  getters: {
    isPetSitter: state => !!state.petSitterInfo
  }
})