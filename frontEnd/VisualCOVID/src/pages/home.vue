<template>
  <el-container style="height: 100%">
    <el-main>
      <world_map></world_map>
      <barChart></barChart>
      <continentBased></continentBased>
      <pie-chart></pie-chart>
    </el-main>
  </el-container>
</template>

<script>
import barChart from '../components/charts/barChart'
import PieChart from "../components/charts/pieChart";
import world_map from "../components/charts/world_map";
import continentBased from "../components/charts/continentBubbleChart"

export default {
  name: "Home",
  components: {
    'world_map':world_map,
    PieChart,
    'barChart': barChart,
    'continentBased':continentBased
  },
}
</script>

<style scoped>

</style>
