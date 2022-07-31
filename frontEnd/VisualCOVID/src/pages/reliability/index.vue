<template>
  <el-container style="height: 100%">
    <el-main>
      <h3>Leading Digital Frequencies for Major Countries Based on New Cases</h3>
      <reliability></reliability>
    </el-main>
  </el-container>
</template>

<script>
import Reliability from "../../components/charts/reliability";
export default {
  name: "index",
  components: {Reliability}
}
</script>

<style scoped>
.wrapper {
  width: 100%;
}
</style>
