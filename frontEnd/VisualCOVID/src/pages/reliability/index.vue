<template>
  <el-container style="height: 100%">
    <el-main>
      <reliability_world-map></reliability_world-map>
      <reliability></reliability>
    </el-main>
  </el-container>
</template>

<script>
import Reliability from "../../components/charts/Reliability/reliability";
import Reliability_worldMap from "../../components/charts/Reliability/reliability_worldMap";
export default {
  name: "index",
  components: {Reliability_worldMap, Reliability}
}
</script>

<style scoped>
.wrapper {
  width: 100%;
}
</style>
