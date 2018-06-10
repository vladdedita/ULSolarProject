import Vue from 'vue'
import Router from 'vue-router'
// import HelloWorld from '@/components/HelloWorld'
import Auth from '../components/views/authPage.vue'
import Front from '../components/views/frontPage.vue'
import Statistics from '../components/views/statisticsPage.vue'
import LocationPage from '../components/views/locationPage.vue'
import ManagementPage from '../components/views/managementPage.vue'
import AboutPage from '../components/views/aboutPage.vue'
import vSelect from 'vue-select'

import VueCookies from 'vue-cookies';

Vue.use(Router)
Vue.use(VueCookies)
Vue.use('v-select', vSelect)
import axios from 'axios'
export default new Router({
  name: 'router',
  routes: [
    {
      path: '/',
      name: 'auth',
      component: Auth
    },
    {
      path: '/front',
      name: 'front',
      component: Front
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
  mode: 'history',
  methods:{
    customPush(path) {

      axios.post(window.ApiUrl + 'isAuth/',
        {
         key:this.$store.getKey
        })
        .then(response => {
          if(response.data.authorized===true)
          {
            this.push(path)
          }
          else
            this.push("/")
        })
        .catch(e => {
          console.log("ERROR:", e);
        })
    },

    }

})
