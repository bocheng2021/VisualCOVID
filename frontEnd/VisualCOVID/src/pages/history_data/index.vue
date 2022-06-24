<template>
  <el-container style="height: 100%">
    <el-main>
      <timeLineChart></timeLineChart>
    </el-main>
  </el-container>
</template>

<script>
import timeLineChart from '../../components/charts/timeLineChart'
export default {
  name: "index",
  components:{
    'timeLineChart':timeLineChart
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
