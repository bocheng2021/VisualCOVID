<template>
  <el-container style="height: 100%">
    <el-main>
      <world_map></world_map>
      <barChart></barChart>
      <pie-chart></pie-chart>
    </el-main>
  </el-container>
</template>

<script>
import barChart from '../components/charts/barChart'
import PieChart from "../components/charts/pieChart";
import world_map from "../components/charts/world_map";

export default {
  name: "Home",
  components: {
    'world_map':world_map,
    PieChart,
    'barChart': barChart,
  },
}
</script>

<style scoped>

</style>
