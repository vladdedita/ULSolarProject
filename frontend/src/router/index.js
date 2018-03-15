import Vue from 'vue'
import Router from 'vue-router'

// import HelloWorld from '@/components/HelloWorld'
import Index from '../components/Views/frontPage.vue'
import Statistics from '../components/Views/statisticsPage.vue'
import VueCookies from 'vue-cookies';

Vue.use(Router)
Vue.use(VueCookies)

export default new Router({
  name:'router',
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index
    },
    {
      path: '/statistics',
      name: 'statistics',
      component: Statistics
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
  ],
  mode:'history'
})
