import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '../components/HelloWorld'
import dashboard from "../components/dashboard";
import home from "../components/home";
import Earth from "../components/threeDEarth";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/test',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path: '/',
      component: dashboard,
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
        }
      ]
    }
  ]
})
