import Vue from 'vue'
import Router from 'vue-router'
import dashboard from "../pages/dashboard";
import home from "../pages/home";
import Earth from "../components/charts/threeDEarth";
import historyData from "../pages/history_data/index"
import VaccineData from "../pages/vaccineData/index"

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      component: dashboard,
      redirect: '/home',
      children: [
        {
          path: 'home',
          component: home,
          name: 'home',
        },
        {
          path: '3DEarth',
          component: Earth,
          name: '3DEarth',
        },
        {
          path: 'historyData',
          component: historyData,
          name: 'historyData',
        },
        {
          path:'VaccineData',
          component: VaccineData,
          name: 'VaccineData',
        }
      ]
    }
  ]
})
