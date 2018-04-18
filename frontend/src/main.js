// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import Vuex from 'vuex'
import App from './App';
import router from './router';
import store from './components/store/store'
import VueSweetAlert2 from 'vue-sweetalert2';
// import jQuery from 'jquery'


Vue.config.productionTip = false;
Vue.use(VueSweetAlert2);


/* eslint-disable */


window.ApiUrl = window.location.href;
window.ttnUrl = ".data.thethingsnetwork.org/";

new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>',
  store

})
