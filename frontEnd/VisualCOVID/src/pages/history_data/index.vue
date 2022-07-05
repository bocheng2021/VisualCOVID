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
import timeLineChart from '../../components/charts/timeline/timeLineChart'
import compareTimeLine from "../../components/charts/timeline/compareTimeLine";
import heatmap from "../../components/charts/timeline/heatmap";
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
</style>
