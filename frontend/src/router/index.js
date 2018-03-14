import Vue from 'vue'
import Router from 'vue-router'

// import HelloWorld from '@/components/HelloWorld'
import Index from '@/components/Views/frontPage'
import VueCookies from 'vue-cookies';
Vue.use(Router)
Vue.use(VueCookies)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index
    }
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
  ]
})
