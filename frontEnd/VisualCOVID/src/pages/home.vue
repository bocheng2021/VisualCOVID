<template>
  <el-container style="height: 100%">
    <el-main>
      <el-container class="home-header">
        <el-card class="info-card">
          <el-row :gutter="10">
            <el-col :span="12">
              <div class="info_image">
              <img class="info_image" :src="srcNewCase" alt="">
              </div>
            </el-col>
            <el-col :span="12">
              <div class="description_info">
                <br>New Case<br><br>
                <span style="color: #d9bb41">
                {{ summaryData.NewConfirmed }}
                </span>
              </div>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="info-card">
          <el-row :gutter="10">
            <el-col :span="12">
              <div class="info_image">
                <img class="info_image" :src="srcTotalCase" alt="">
              </div>
            </el-col>
            <el-col :span="12">
              <div class="description_info">
                <br>Total Cases<br><br>
                <span style="color: #008011">
                  {{ summaryData.TotalConfirmed }}
                </span>
              </div>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="info-card">
          <el-row :gutter="10">
            <el-col :span="12">
              <div class="info_image">
                <img class="info_image" :src="srcDeath" alt="">
              </div>
            </el-col>
            <el-col :span="12">
              <div class="description_info">
                <br>Total Death<br><br>
                <span style="color: #d50a32">
                {{ summaryData.TotalDeaths }}
                </span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-container>
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
import death from '../assets/image/Death.png'
import newCase from "../assets/image/new case.png"
import totalCase from "../assets/image/total case.png"
import {processInfo} from "../utils/processInfo";

export default {
  name: "Home",
  components: {
    'world_map':world_map,
    PieChart,
    'barChart': barChart,
    'continentBased':continentBased
  },
  data() {
    return {
      srcDeath: death,
      srcNewCase: newCase,
      srcTotalCase: totalCase,
      summaryData: {
        TotalConfirmed: null,
        TotalDeaths:null,
        NewConfirmed:null,
        NewDeaths:null
      },
    }
  },
  methods:{
    async processInfo(){
      this.summaryData = await processInfo()
    }
  },
  mounted() {
    this.processInfo()
  }
}
</script>

<style scoped>
.info-card{
  flex: auto;
  width: 33%;
  height: 180px;
  margin: 10px 10px 10px 10px;
}
.info_image{
  width: 70%;
  height: 100%;
  margin: 10px 10px 10px 10px;
}
.description_info{
  width: 100%;
  color: #545c64;
  font-size: 23px;
  font-weight: bolder;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  height: 100%;
  margin: 10px 10px 10px 10px;
}
</style>
