<template>
  <el-card class="box-card">
    <h3 style="text-align: center;font-weight: bolder">
      Lasted COVID outbreak distribution by continent</h3>
  <div class="continent" id="continent"></div>
  </el-card>
</template>

<script>

import {processContinentData} from "../../utils/processContinentData";
import * as echarts from "echarts";

export default {
  name: "continentBased",
  data(){
    return{
    }
  },
  methods:{
    runContinentData()
    {
      const that = this;
      const axios = require("axios");
      const options = {
        method: 'GET',
        url: 'https://covid-193.p.rapidapi.com/statistics',
        headers: {
          'X-RapidAPI-Key': '347ab949b4msh10b962fa5b22f96p1e6bd5jsn2d1bdf27d549',
          'X-RapidAPI-Host': 'covid-193.p.rapidapi.com'
        }
      };
      axios.request(options).then(function (response) {
        let data = processContinentData(response.data.response)
        that.drawChart(data)
      }).catch(function (error) {
        console.error(error);
      });
    },
    drawChart(data)
    {
      let chart = echarts.init(document.getElementById('continent'),'vintage',
        {locale:'EN'}
      )
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option = {
        legend: {
          right: '10%',
          top: '3%',
          data: ['North-America', 'Africa','Oceania','Asia','South-America','Europe']
        },
        toolbox: {
          show: true,
          feature: {
            mark: {
              show: true,
            },
            restore: {
              show: true,
            },
            saveAsImage: {
              show: true,
            }
          }
        },
        tooltip: {
          trigger: 'item', // Trigger type, data item graph trigger,
          // mainly used in scatter charts, pie charts and other charts without category axes
          formatter: function (val) {
            if(val.data == null) return ;
            return '<span style="font-weight:bold">'+val.data[2] +'</span><br/>'+'<span style="font-weight:bold;' +
              'color: darkgreen">' + 'Total Confirmed: ' +val.data[0] +'</span><br/>'+'<span style="font-weight:bold;' +
              'color: brown">'+ 'Total Deaths: '+val.data[1]+'</span>'
          }
        },
        grid: {
          left: '8%',
          top: '10%'
        },
        xAxis: {
          name:'Total Confirmed',
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          }
        },
        yAxis: {
          name:'Total Deaths',
          splitLine: {
            lineStyle: {
              type: 'dashed'
            }
          },
          scale: true
        },
        series: [
          {
            name: 'North-America',
            data: data.North_America,
            type: 'scatter',
            symbolSize: function (data) {
              return Math.sqrt(data[1])/10;
            },
            emphasis: {
              focus: 'series',
              label: {
                show: true,
                formatter: function (param) {
                  return param.data[2];
                },
                position: 'top'
              }
            },
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgba(120, 36, 50, 0.5)',
              shadowOffsetY: 5,
              color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                {
                  offset: 0,
                  color: 'rgb(251, 118, 123)'
                },
                {
                  offset: 1,
                  color: 'rgb(204, 46, 72)'
                }
              ])
            }
          },
          {
            name: 'Oceania',
            data: data.Oceania,
            type: 'scatter',
            symbolSize: function (data) {
              return Math.sqrt(data[1])/10;
            },
            emphasis: {
              focus: 'series',
              label: {
                show: true,
                formatter: function (param) {
                  return param.data[2];
                },
                position: 'top'
              }
            },
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgb(210,109,65)',
              shadowOffsetY: 5,
              color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                {
                  offset: 0,
                  color: 'rgb(229,169,120)'
                },
                {
                  offset: 1,
                  color: 'rgb(169,64,32)'
                }
              ])
            }
          },
          {
            name: 'Asia',
            data: data.Asia,
            type: 'scatter',
            symbolSize: function (data) {
              return Math.sqrt(data[1]) / 10;
            },
            emphasis: {
              focus: 'series',
              label: {
                show: true,
                formatter: function (param) {
                  return param.data[2];
                },
                position: 'top'
              }
            },
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgb(224,212,171)',
              shadowOffsetY: 5,
              color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                {
                  offset: 0,
                  color: 'rgb(224,221,52)'
                },
                {
                  offset: 1,
                  color: 'rgb(93,89,17)'
                }
              ])
            }
          },
          {
            name: 'Africa',
            data: data.Africa,
            type: 'scatter',
            symbolSize: function (data) {
              return Math.sqrt(data[1]) / 10;
            },
            emphasis: {
              focus: 'series',
              label: {
                show: true,
                formatter: function (param) {
                  return param.data[2];
                },
                position: 'top'
              }
            },
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgba(5,15,35,0.55)',
              shadowOffsetY: 5,
              color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                {
                  offset: 0,
                  color: 'rgb(194,221,248)'
                },
                {
                  offset: 1,
                  color: 'rgb(17,40,63)'
                }
              ])
            }
          },
          {
            name: 'South-America',
            data: data.South_America,
            type: 'scatter',
            symbolSize: function (data) {
              return Math.sqrt(data[1]) / 10;
            },
            emphasis: {
              focus: 'series',
              label: {
                show: true,
                formatter: function (param) {
                  return param.data[2];
                },
                position: 'top'
              }
            },
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgba(25, 100, 150, 0.5)',
              shadowOffsetY: 5,
              color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                {
                  offset: 0,
                  color: 'rgb(129, 227, 238)'
                },
                {
                  offset: 1,
                  color: 'rgb(25, 183, 207)'
                }
              ])
            }
          },
          {
            name: 'Europe',
            data: data.Europe,
            type: 'scatter',
            symbolSize: function (data) {
              return Math.sqrt(data[1]) / 10;
            },
            emphasis: {
              focus: 'series',
              label: {
                show: true,
                formatter: function (param) {
                  return param.data[2];
                },
                position: 'top'
              }
            },
            itemStyle: {
              shadowBlur: 10,
              shadowColor: 'rgb(59,124,51)',
              shadowOffsetY: 5,
              color: new echarts.graphic.RadialGradient(0.4, 0.3, 1, [
                {
                  offset: 0,
                  color: 'rgb(149,231,129)'
                },
                {
                  offset: 1,
                  color: 'rgb(64,114,58)'
                }
              ])
            }
          }
        ]
      };

      option && chart.setOption(option);
    }
  },
  mounted() {
    this.runContinentData()
  }
}
</script>

<style scoped>
.continent {
  width: 100%;
  height: 300px;
  margin-top: 20px;
}
.el-card{
  margin: 20px 10px 20px 10px;
}
.el-descriptions{
  margin-top: 20px;
}
</style>
