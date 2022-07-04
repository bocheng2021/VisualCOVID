<template>
  <el-container style="height: 100%">
    <el-main>
      <timeLineChart></timeLineChart>
      <compareTimeLine></compareTimeLine>
      <heatmap></heatmap>
    </el-main>
  </el-container>
</template>

<script>
import timeLineChart from '../../components/charts/timeLineChart'
import compareTimeLine from "../../components/charts/compareTimeLine";
import heatmap from "../../components/charts/heatmap";
export default {
  name: "index",
  components:{
    'timeLineChart':timeLineChart,
    'compareTimeLine':compareTimeLine,
    'heatmap':heatmap
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
