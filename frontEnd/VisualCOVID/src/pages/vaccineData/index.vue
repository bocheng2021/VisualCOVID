<template>
  <el-container style="height: 100%">
    <el-main>
      <vaccineMap></vaccineMap>
    </el-main>
  </el-container>
</template>

<script>
import vaccineMap from "../../components/charts/vaccine/vaccineMap";

export default {
  name: "index",
  components:{
    'vaccineMap':vaccineMap
  }
}
</script>

<style scoped>
.wrapper {
  width: 100%;
}
.wrapper .chart {
  width: 80%;
  margin:0 auto;
  height: 600px;
  /* background: url(../../public/static/bg.png) no-repeat; 背景图设置*/
  background-size: 100% 100%;
}
</style>
