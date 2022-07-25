<template>
  <el-container style="height: 100%">
    <el-main>
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
