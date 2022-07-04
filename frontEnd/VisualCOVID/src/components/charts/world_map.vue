<template>
  <el-card class="box-card">
    <div class='wrapper'>
      <div class='chart' id='chart'>
      </div>
    </div>
    <div class="tool-bar" id="map_toolbar">
      <el-descriptions title="Current Data Info">
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
      <span style="color:black;font-weight:bolder">Current Data Type:</span>
      <el-select v-model="value" placeholder="data type" @change="onSubmit">
        <el-option label="Total Death" value="total_2d_death"></el-option>
        <el-option label="Total Confirm" value="total_2d"></el-option>
      </el-select>
      <span style="color:black;font-weight:bolder;padding-left: 10px"> Current Graphics Type:</span>
      <el-select v-model="optionType" placeholder="data type" @change="switchGraphics">
        <el-option label="World Map" value="map"></el-option>
        <el-option label="Bar Graph" value="bar"></el-option>
      </el-select>
    </div>
  </el-card>
</template>

<script>
import $ from "jquery";
import {processPostmanAPIEarthData} from "../../utils/create-earth";
import * as echarts from "echarts";

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
      optionType:'map'
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
          that.summaryData = res.Global
          that.data = data
          that.DateCurr = that.summaryData.Date.split('T')[0]
          that.drawChart(that,data,that.optionType)
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
          text: 'The latest ranking chart of coronavirus (COVID-19) cases by country',
          top: 10,
          left: 'center',
          textStyle: {
            fontSize: 24,
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
.wrapper {
  width: 100%;
}
.wrapper .chart {
  width: 80%;
  margin:0 auto;
  height: 600px;
  /* background: url(../../public/static/bg.png) no-repeat; 背景图设置*/
  background-size: 100% 100%;
}
</style>
