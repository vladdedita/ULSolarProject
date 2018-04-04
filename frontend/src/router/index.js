import Vue from 'vue'
import Router from 'vue-router'

// import HelloWorld from '@/components/HelloWorld'
import Index from '../components/Views/frontPage.vue'
import Statistics from '../components/Views/statisticsPage.vue'
import LocationPage from '../components/Views/locationPage.vue'
import ManagementPage from '../components/Views/managementPage.vue'
import vSelect from 'vue-select'

import VueCookies from 'vue-cookies';
import GoogleMaps from 'google-maps'

Vue.use(Router)
Vue.use(VueCookies)
Vue.use('v-select',vSelect)

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
