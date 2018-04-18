import Vue from 'vue'
import Router from 'vue-router'
// import HelloWorld from '@/components/HelloWorld'
import Index from '../components/views/frontPage.vue'
import Statistics from '../components/views/statisticsPage.vue'
import LocationPage from '../components/views/locationPage.vue'
import ManagementPage from '../components/views/managementPage.vue'
import AboutPage from '../components/views/aboutPage.vue'
import vSelect from 'vue-select'

import VueCookies from 'vue-cookies';

Vue.use(Router)
Vue.use(VueCookies)
Vue.use('v-select', vSelect)

export default new Router({
  name: 'router',
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
    },
    {
      path: '/location',
      name: 'location-page',
      component: LocationPage
    },
    {
      path: '/management',
      name: 'management-page',
      component: ManagementPage
    },
    {
      path:'/about',
      name: 'about-page',
      component: AboutPage
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
  mode: 'history'
})
