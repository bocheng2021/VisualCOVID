import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from'./getters'
import * as mutations from'./mutations'
import * as actions from'./actions'
Vue.use(Vuex);
 
const store = new Vuex.Store({

  state:{
    uid:null,
    utype:null,
    status:null,
    isLogin:false,
    token:''
  },

  getters,
  mutations,
  actions
})
export default store;