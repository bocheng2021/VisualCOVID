<template>
  <el-card class="box-card">
    <h3 style="font-weight: bolder;text-align: center">Percentage of COVID cases by country worldwide</h3>
    <div class='pie-chart' id='pie-chart'></div>
    <div class="tool-bar" id="bar_toolbar">
      <el-descriptions title="Data Info">
        <el-descriptions-item label="Date ">{{DateCurr}}</el-descriptions-item>
        <el-descriptions-item label="Total Confirmed ">
              <span style="color:green;font-weight:bolder">
                {{summaryData.TotalConfirmed}}</span>
        </el-descriptions-item>
        <el-descriptions-item label="Total Deaths ">
              <span style="color:red;font-weight:bolder">
                {{summaryData.TotalDeaths}}</span>
        </el-descriptions-item>
        <el-descriptions-item label="New Confirmed ">
              <span style="color:green;font-weight:bolder">
                {{summaryData.NewConfirmed}}</span>
        </el-descriptions-item>
        <el-descriptions-item label="New Deaths ">
              <span style="color:red;font-weight:bolder">
              {{summaryData.NewDeaths}}</span>
        </el-descriptions-item>
        <el-descriptions-item label="Data Source ">
              <span style="color:black;font-weight:bolder">
              {{DateSource}}</span>
        </el-descriptions-item>
      </el-descriptions>
      <span style="color:black;font-weight:bolder">Current Data Type:  </span>
      <el-select v-model="dataType" placeholder="data type" @change="onSubmit">
        <el-option label="Total Death" value="2"></el-option>
        <el-option label="Total Confirm" value="1"></el-option>
      </el-select>
    </div>
  </el-card>
</template>

<script>
import * as echarts from "echarts";
import $ from "jquery";
import {processPostmanAPIPieData} from "../../utils/create-pie-chart";

export default {
  name: "pieChart",
  data(){
    return{
      dataType:'1',
      DateCurr:'',
      DateSource:'Postman',
      summaryData: {
        TotalConfirmed:[],
        TotalDeaths:[],
        NewConfirmed:[],
        NewDeaths:[]
      },
    }
  },
  methods:{
    onSubmit()
    {
      this.processData();
    },
    processData()
    {
      const that = this;
      $.ajax({
        type:'get',
        url:'https://api.covid19api.com/summary',
        dataType:'json',
        success :function (res)
        {
          let data = processPostmanAPIPieData(res,that.dataType)
          that.summaryData = res.Global
          that.DateCurr = that.summaryData.Date.split('T')[0]
          that.drawPieChart(data)
        }
      })
    },
    drawPieChart(data){
      let chart = echarts.init(document.getElementById('pie-chart'),'vintage',
        {locale:'EN'}
      )
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/><span style="color:black;font-weight:bolder">{b}</span>: {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          right: 10,
          top: 20,
          bottom: 20,
          data: data.CountryName
        },
        series: [
          {
            name: 'Country',
            type: 'pie',
            radius: '55%',
            center: ['40%', '50%'],
            data: data.seriesData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      option && chart.setOption(option);
    }
  },
  mounted() {
    this.processData()
  },
}
</script>

<style scoped>
.pie-chart {
  width: 100%;
  height: 500px;
  margin-top: 20px;
}
.el-card{
  margin: 10px 10px 10px 10px;
}
</style>
