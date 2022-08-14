<template>
  <el-container>
  <el-card class="box-card">
    <div class='chart' id='chart'>
    </div>
    <div class="tool-bar" id="map_toolbar">
      <el-row :gutter="40">
        <el-col :span="10">
          <span style="color:black;font-weight:bolder">Change Data Type:</span>
          <el-select v-model="value" placeholder="data type" @change="onSubmit" style="margin-top: 10px">
            <el-option label="Total Death" value="total_2d_death"></el-option>
            <el-option label="Total Confirm" value="total_2d"></el-option>
          </el-select>
        </el-col>
        <el-col :span="10">
          <span style="color:black;font-weight:bolder;">Change Graphics Type:</span>
          <el-select v-model="optionType" placeholder="data type" @change="switchGraphics" style="margin-top: 10px">
            <el-option label="World Map" value="map"></el-option>
            <el-option label="Bar Graph" value="bar"></el-option>
          </el-select>
        </el-col>
      </el-row>
    </div>
  </el-card>
    <el-card>
      <div class='roseChart' id='roseChart'>
      </div>
      <el-descriptions title="Current Data Info" style="font-size: 12px">
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
      </el-descriptions>
    </el-card>
  </el-container>
</template>

<script>
import $ from "jquery";
import {processPostmanAPIEarthData} from "../../utils/create-earth";
import * as echarts from "echarts";
import {processPostmanAPIPieData, processPostmanAPIRosePieData} from "../../utils/create-pie-chart";

export default {
  name: "world_map",
  data() {
    return {
      DateCurr:'',
      summaryData: {
        TotalConfirmed:[],
        TotalDeaths:[],
        NewConfirmed:[],
        NewDeaths:[]
      },
      data:[],
      toolBarText :"Total Confirmed: ",
      toolBarText2:"Total Deaths: ",
      value: 'total_2d',
      optionType:'map',
      MajorCountries: ['United Kingdom','United States of America','China','Japan','Russia','Austria','Canada','France','Germany'],
    };
  },
  methods: {
    onSubmit()
    {
      this.runWorldMap()
    },
    switchGraphics()
    {
      this.runWorldMap()
    },
    runWorldMap()
    {
      const that = this;
      $.ajax({
        type:'get',
        url:'https://api.covid19api.com/summary',
        dataType:'json',
        success :function (res)
        {
          let data = processPostmanAPIEarthData(res,that.value)
          let pieData = processPostmanAPIRosePieData(res,that.MajorCountries)
          that.summaryData = res.Global
          that.data = data
          that.DateCurr = that.summaryData.Date.split('T')[0]
          that.drawChart(that,data,that.optionType)
          that.drawRoseChart(pieData)
        }
      })
    },
    drawChart (context,data,optionType) {
      let chart = echarts.init(document.getElementById('chart'),'vintage',
        {locale:'EN'})
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      data.sort(function (a, b) {
        return a.value[2] - b.value[2];
      });
      // draw the chart
      let option = {
        // main title
        title: {
          text: 'Coronavirus (COVID-19) World Map Visualization',
          top: 10,
          left: 'center',
          textStyle: {
            fontSize: 18,
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
          trigger: 'item', // Trigger type, data item graph trigger,
          // mainly used in scatter charts, pie charts and other charts without category axes
          formatter: function (val) {
            if(val.data == null) return ;
            return '<span style="font-weight:bold">'+val.data.name +'</span>'+'<br/>'+
              context.toolBarText + '<span style="color:green;font-weight:bolder">'+val.data.value[2]+'</span>'+
              '<br/>'+context.toolBarText2+'<span style="color:red;font-weight:bolder">'+val.data.other[0]+'</span>'
          }
        },
        // Visual mapping component
        visualMap: {
          min: 0,
          max: 8000000,
          text:['max','min'],
          realtime: false,
          calculable: true,
          color: ['#0064d0','#c3e0ff'],
        },
        series: [
          {
            id: 'People Number',
            type: 'map', // type
            // Series name, used for tooltip display, legend filtering for
            // legends Used to specify the corresponding series when settingOption updates data and configuration items
            name: 'World Map',
            map: 'world', // map type
            roam: true,
            // Text labels on graphs
            label: {
              show: false // Whether to display the corresponding place name
            },
            zoom: 1.2,
            animationDurationUpdate: 1000,
            universalTransition: true,
            itemStyle: {
              borderWidth: 0.5,
              borderColor: '#000',
              borderType: 'solid'
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
            // name mapping
            nameMap: name,
            // Array of data contents in the map series Array items can be single values
            data: data
          }
        ]
      }
      const barOption = {
        title: {
          text: 'Latest (COVID-19) Case Ranking Chart by Country',
          top: 10,
          left: 'center',
          textStyle: {
            fontSize: 18,
            fontWeight: 600,
            color: '#000'
          }
        },
        xAxis: {
          type: 'value',
          name: 'Cases',
        },
        grid: {
          top: '12%',
          left: '1%',
          right: '10%',
          containLabel: true
        },
        calculable: true,
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            label: {
              show: true
            }
          }
        },
        dataZoom: [
          {
            show: true,
            start: 0,
            end: 100
          },
          {
            show: true,
            start: 94,
            end: 100,
            yAxisIndex: 0,
            filterMode: 'empty',
            width: 30,
            height: '80%',
            showDataShadow: false,
            left: '93%'
          }
        ],
        yAxis: {
          type: 'category',
          axisLabel: {
            rotate: 30
          },
          data: data.map(function (item) {
            return item.name;
          })
        },
        animationDurationUpdate: 1000,
        series: {
          type: 'bar',
          id: 'People Number',
          data: data.map(function (item) {
            return item.value[2];
          }),
          universalTransition: true,
          itemStyle: {
            color: '#1d86ee'
          }
        }
      };
      if(context.value==="total_2d_death")
      {
        option.visualMap.max = 100000
        option.visualMap.color = ['#FF6347','#ffad98']
        barOption.series.itemStyle.color='#ee1212'
        option.tooltip.formatter= function(val) {
          if(val.data == null) return ;
          return '<span style="font-weight:bold">'+val.data.name +'</span>'+'<br/>'
            +context.toolBarText+'<span style="color:Green;font-weight:bolder">'+val.data.other[0]+'</span><br/>'+
            context.toolBarText2 + '<span style="color:red;font-weight:bolder">'+val.data.value[2]+'</span>'
        }
      }
      if(optionType==='bar')
      {
        chart.setOption(barOption, true);
      }
      else
      {
        chart.setOption(option, true)
      }
    },
    drawRoseChart(data)
    {
      let chart = echarts.init(document.getElementById('roseChart'),'vintage',
        {locale:'EN'})
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option = {
        title: {
          text: 'Countries with the most deaths',
          left: 'center',
          top: 20,
          textStyle: {
            color: 'black'
          }
        },
        tooltip: {
          trigger: 'item'
        },
        visualMap: {
          show: false,
          min: 0,
          max: data[0].value*2,
          inRange: {
            colorLightness: [0, 1]
          }
        },
        series: [
          {
            name: 'Total death',
            type: 'pie',
            radius: '65%',
            center: ['50%', '50%'],
            data: data.sort(function (a, b) {
              return a.value - b.value;
            }),
            roseType: 'radius',
            label: {
              color: 'black'
            },
            labelLine: {
              lineStyle: {
                color: 'black'
              },
              smooth: 0.2,
              length: 10,
              length2: 20
            },
            itemStyle: {
              color: 'red',
              shadowBlur: 50,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            },
            animationType: 'scale',
            animationEasing: 'elasticOut',
            animationDelay: function (idx) {
              return Math.random() * 200;
            }
          }
        ]
      };
      option && chart.setOption(option);
    },
  },
  mounted() {
    this.runWorldMap()
  },
  props:{
    msg:{
      type:Object,
      default:() => {}
    }
  },
  watch:{
    msg:{
      deep: true,
      handler(val) {
        this.runWorldMap(val)
      }
    }
  },
}
</script>

<style scoped>
.chart {
  width: 100%;
  height: 400px;
  margin:0 auto;
}
.tool-bar{
  margin-top: 20px;
}
.roseChart {
  width: 100%;
  height: 400px;
  margin-top: 20px;
}
.el-card{
  width: 55%;
  margin: 10px 10px 20px 10px;
}
</style>
