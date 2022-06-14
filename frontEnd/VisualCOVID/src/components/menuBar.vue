<template>
  <el-row class="tac">
    <el-col :span="24">
      <el-menu
        router
        default-active="2"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="home">
          <i class="el-icon-s-home"></i>
          <span slot="title">Home</span>
        </el-menu-item>
        <el-submenu
          v-for="item in menu"
          :index="item.id"
          :key="item.id"
        >
          <template slot="title">
            <i class="el-icon-menu"></i>
            <span v-text="item.name"></span>
          </template>
          <el-menu-item-group
            class="over-hide"
            v-for="sub in item.sub"
            :key="sub.componentName"
          >
            <el-menu-item :index="sub.componentName" v-text="sub.name">
            </el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-menu-item index="About" >
          <i class="el-icon-info"></i>
          <span slot="title">About</span>
        </el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
</template>

<style scoped>
.over-hide {
  overflow: hidden;
}
</style>

<script>
import menu from "../config/menuBarConfig";

export default {
  data() {
    return {
      menu: menu,
      menuSet: {
        Home: true,
        Function: true,
        About: true,
      },
    };
  },
 mounted() {
   const that = this;
    // this.$forceUpdate()
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>
<style scoped>
.el-row{
  overflow-y: auto;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
