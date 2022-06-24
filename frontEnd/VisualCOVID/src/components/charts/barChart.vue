<template>
  <el-card class="box-card">
    <h3 style="text-align: center;font-weight: bolder">Latest total confirmed and total deaths of COVID by country</h3>
    <div class='bar-chart' id='bar-chart'></div>
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
    </div>
  </el-card>
</template>

<script>
import * as echarts from "echarts";
import $ from "jquery";
import {processPostmanAPIBarData} from "../../utils/create-bar-charts";

export default {
  name: "barChart",
  data() {
    return {
      summaryData: {
        TotalConfirmed:[],
        TotalDeaths:[],
        NewConfirmed:[],
        NewDeaths:[]
      },
      DateCurr:[],
      DateSource:'Postman'
    };
  },
  methods:{
    runSummaryData()
    {
      const that = this;
      $.ajax({
        type:'get',
        url:'https://api.covid19api.com/summary',
        dataType:'json',
        success :function (res)
        {
          let data = processPostmanAPIBarData(res)
          that.summaryData = res.Global
          that.DateCurr = that.summaryData.Date.split('T')[0]
          that.drawSummaryData(data)
        }
      })
    },
    drawSummaryData(data)
    {
      let chart = echarts.init(document.getElementById('bar-chart'),'vintage',
        {locale:'EN'}
      )
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            label: {
              show: true
            }
          }
        },
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true,
            },
            dataView: {
              show: true,
              readOnly: false,
            },
            restore: {
              show: true,
            },
            saveAsImage: {
              show: true,
            }
          }
        },
        calculable: true,
        legend: {
          data: ['Data Type', 'Total Confirms', 'Total Deaths'],
          itemGap: 5,
        },
        grid: {
          top: '12%',
          left: '1%',
          right: '10%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: data.names
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: 'People Number',
            axisLabel: {
              formatter: function (a) {
                a = +a;
                return isFinite(a) ? echarts.format.addCommas(+a / 1000) : '';
              }
            }
          }
        ],
        dataZoom: [
          {
            show: true,
            start: 94,
            end: 100
          },
          {
            type: 'inside',
            start: 94,
            end: 100
          },
          {
            show: true,
            yAxisIndex: 0,
            filterMode: 'empty',
            width: 30,
            height: '80%',
            showDataShadow: false,
            left: '93%'
          }
        ],
        series: [
          {
            name: 'Total Confirms',
            type: 'bar',
            color: '#1bc925',
            data: data.confirms
          },
          {
            name: 'Total Deaths',
            type: 'bar',
            color: '#cb2525',
            data: data.deaths
          }
        ]
      };
      chart.setOption(option);
    }
  },
  mounted() {
    this.runSummaryData()
  },
}
</script>
<style scoped>
.bar-chart {
  width: 100%;
  height: 400px;
  margin-top: 20px;
}
.el-card{
  margin-top: 20px;
}
</style>
