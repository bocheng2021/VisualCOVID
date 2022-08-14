<template>
  <el-container>
    <el-header class="header">
      <Header/></el-header>
    <el-container>
      <el-aside width="200px">
        <menuBar></menuBar>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import menuBar from '../components/menuBar'
import Header from '../components/header'

export default {
  name: 'dashboard',
  components: {
    'menuBar': menuBar,
    'Header':Header
  }
}
</script>

<style scoped>
.header {
  background-color: #2B4570;
  color: #fff;
  line-height: 60px;
}
.el-row{
  height: 100%;
  margin: 0;
  padding: 0;
}
.el-aside{
  height: auto;
  background-color: #545c64;
}
.el-main{
  background-color: #f7f8fa;
}
</style>
