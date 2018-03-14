import Vue from 'vue'
import Router from 'vue-router'

// import HelloWorld from '@/components/HelloWorld'
import Index from '@/components/Views/frontPage'
<<<<<<< HEAD
import VueCookies from 'vue-cookies';
Vue.use(Router)
Vue.use(VueCookies)
=======

Vue.use(Router)
>>>>>>> 144cc515b719d059d19d5c418d8152fb8ef731da

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index
    }
<<<<<<< HEAD
    //
    // {
    //   path: '/measurements',
    //   name: 'measurements',
    //   component: Index
    // },
    // {
    //   path: '/auth',
    //   name: 'measurements',
    //   component: Index
    // }
=======

>>>>>>> 144cc515b719d059d19d5c418d8152fb8ef731da
  ]
})
