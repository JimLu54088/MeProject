import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null, //token的初始值為null
  }),
  getters: {
    isAuthenticated: (state) => !!state.token, //如果token不是null就回傳true，代表已登入
  },
  actions: {
    setToken(token) {
      this.token = token; //將token設定為傳入的token
    },
    clearToken() {
      this.token = null; //將token清空
    },
  },
  persist: true,//啟用pinia持久化
});