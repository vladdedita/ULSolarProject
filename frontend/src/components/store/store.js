import Vue from 'vue'
import Vuex from "vuex";
Vue.use(Vuex);


const store = new Vuex.Store({

  state: {
    authorized :false,
    key:""
  },
  mutations: {
    authorize : state => state.authorized=true,
    unauthorize: state=> state.authorized=false,
    setKey(state,_key) {
      state.key = _key
    }
  },
  getters: {
    isAuthorized: state => state.authorized,
    getKey: state => state.key
}

});

export default store;
