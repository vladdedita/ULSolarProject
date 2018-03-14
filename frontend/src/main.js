// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import VueSweetAlert2 from 'vue-sweetalert2';
Vue.config.productionTip = false;
Vue.use(VueSweetAlert2);
/* eslint-disable no-new */


<<<<<<< HEAD
window.ApiUrl = "http://localhost:8090";
window.ttnUrl=".data.thethingsnetwork.org/"
=======
<<<<<<< HEAD
window.ApiUrl = "http://localhost:8090";
window.ttnUrl=".data.thethingsnetwork.org/"
=======

>>>>>>> 144cc515b719d059d19d5c418d8152fb8ef731da
>>>>>>> 7e3e1682b882e96d4c92197d4706e1aec671b47c
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
