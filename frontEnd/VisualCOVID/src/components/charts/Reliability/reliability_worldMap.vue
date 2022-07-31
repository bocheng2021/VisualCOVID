<template>
  <el-card class="box-card">
    <div class='wrapper'>
      <div class='reliability_worldMap' id='reliability_worldMap'>
      </div>
    </div>
    <el-descriptions title="Data Info">
      <el-descriptions-item label="Data Sampling Time ">2022-7-31</el-descriptions-item>
      <el-descriptions-item label="Data Source ">
        <span style="color:black;font-weight:bolder">Postman</span>
      </el-descriptions-item>
      <el-descriptions-item label="Reliability judgment ">
        SSD<=100 , MAD<=0.015 , d*<=0.2
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions title="Data Interpretation">
      <el-descriptions-item label="SSD ">
        <span style="color:black;font-weight:bolder">Sum of Squared Difference.</span>
      </el-descriptions-item>
      <el-descriptions-item label="MAD ">
        <span style="color:black;font-weight:bolder">Mean Absolute Deviation.</span>
      </el-descriptions-item>
      <el-descriptions-item label="d* factor ">
        <span style="color:black;font-weight:bolder"> The d* quantifies the distance between the sample and the cumulative distribution.</span>
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>
<script>
import * as echarts from "echarts";
import {reliabilityDataProcess} from "../../../utils/reliabilityDataProcess";

export default {
  name: "reliability_worldMap",
  methods:{
    async runReliabilityWorldMap() {
      let data = await reliabilityDataProcess()
      let context =this
      this.drawMap(context,data)
    },
    drawMap(context,data)
    {
      let chart = echarts.init(document.getElementById('reliability_worldMap'),'vintage',
        {locale:'EN'})
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option ={
        title: {
          text: 'Reliability of covid-19 data for countries around the world',
          top: 10,
          left: 'center',
          textStyle: {
            fontSize: 24,
            fontWeight: 600,
            color: '#000'
          }
        },
        grid: {
          width:'100%',
          height:'100%',
          left: '0%',
          right: '0%',
          bottom: '0%',
          containLabel: true
        },
        tooltip: {
          trigger: "item",
          formatter: function (val) {
            if (val.data == null) return;
            let label;
            if (val.data.value[2]===1)
            {
              label = "Moderate conformity"
            }
            else if (val.data.value[2]===2)
            {
              label = "Nonconformity"
            }
            else
            {
              label = "Full conformity"
            }
            return '<span style="font-weight:bold">'+val.data.name +':<br/>'+ label+'</span><br/>'
              +'SSD: '+ '<span style="color:#008011;font-weight:bolder">'+val.data.other[0]+'</span>'+
              '<br/> MAD: '+'<span style="color:#1e9680;font-weight:bolder">'+val.data.other[1]+'</span>'
              +'<br/> d* factor: '+'<span style="color:#1f39e0;font-weight:bolder">'+val.data.other[2]+'</span>';
          },
        },
        visualMap: {
          type: "piecewise",
          min: 0,
          max: 3,
          realtime: false,
          calculable: true,
          splitNumber: 3,
          itemWidth: 20,
          pieces: [
            { gte: 2, lte: 2, label: "Nonconformity", color: "#d50a32" },
            { gte: 1, lte: 1, label: "Moderate conformity", color: "#44acde" },
            { gte: 0, lte: 0, label: "Full conformity", color: "#65ee56" },
          ],
          textStyle: {
            color: "#737373",
          },
        },
        series: [
          {
            type: "map",
            name: 'World Map',
            mapType: "world",
            roam: true,
            label: {
              show: false,
            },
            zoom: 1.2,
            animationDurationUpdate: 1000,
            universalTransition: true,
            itemStyle: {
              borderWidth: 0.2,
              borderColor: '#000',
              borderType: "solid",
            },
            emphasis: {
              label: {
                show: true,
                color: '#fff'
              },
              itemStyle: {
                areaColor: '#FF6347' //map area color
              }
            },
            nameMap: name,
            data: data,
          },
        ],
      };
      chart.setOption(option)
    },
  },
  mounted() {
    this.runReliabilityWorldMap()
  }
}
</script>

<style scoped>
.wrapper .reliability_worldMap {
  width: 80%;
  margin:0 auto;
  height: 600px;
  background-size: 100% 100%;
}
.wrapper {
  width: 100%;
}
</style>
