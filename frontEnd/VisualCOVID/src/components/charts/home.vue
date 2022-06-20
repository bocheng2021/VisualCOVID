<template>
  <el-container style="height: 100%">
    <el-main>
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
          <el-select v-model="value" placeholder="data type" @change="onSubmit">
            <el-option label="Total Death" value="total_2d_death"></el-option>
            <el-option label="Total Confirm" value="total_2d"></el-option>
          </el-select>
        </div>
      </el-card>
      <barChart></barChart>
      <div class='chart' id='barChart'></div>
    </el-main>
  </el-container>
</template>

<script>
import barChart from './barChart'
import * as echarts from 'echarts';
import $ from 'jquery'
import {processPostmanAPIEarthData} from "../../utils/create-earth";

export default {
  name: "Home",
  components: {
    'barChart': barChart,
  },
  data() {
    return {
      DateCurr:'',
      summaryData: {
        TotalConfirmed:[],
        TotalDeaths:[],
        NewConfirmed:[],
        NewDeaths:[]
      },
      toolBarText :"Total Confirmed: ",
      toolBarText2:"Total Deaths: ",
      value: 'total_2d'
    };
  },
  methods: {
    onSubmit()
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
          that.DateCurr = that.summaryData.Date.split('T')[0]
          that.drawChart(that,data)
        }
      })
    },
    drawChart (context,data) {
      let chart = echarts.init(document.getElementById('chart'),'vintage',
        {locale:'EN'})
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
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
            type: 'map', // type
            // Series name, used for tooltip display, legend filtering for
            // legends Used to specify the corresponding series when settingOption updates data and configuration items
            name: 'World Map',
            mapType: 'world', // map type
            roam: true,
            // Text labels on graphs
            label: {
              show: false // Whether to display the corresponding place name
            },
            zoom: 1.2,
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
      if(context.value==="total_2d_death")
      {
        option.visualMap.max = 100000
        option.visualMap.color = ['#FF6347','#ffad98']
        option.tooltip.formatter= function(val) {
        if(val.data == null) return ;
        return '<span style="font-weight:bold">'+val.data.name +'</span>'+'<br/>'
          +context.toolBarText+'<span style="color:Green;font-weight:bolder">'+val.data.other[0]+'</span><br/>'+
          context.toolBarText2 + '<span style="color:red;font-weight:bolder">'+val.data.value[2]+'</span>'
      }
      }
      chart.setOption(option)
    },
    /*getMap() {
      const dom = document.getElementById('main');
      // 初始化图表
      //$(window).on('resize',function(){//
        //屏幕大小自适应，重置容器高宽
        //context.resizeMainContainer(dom);
        //myChart.resize();
      //});
      this.resizeMainContainer(dom);
      const myChart = echarts.init(dom, null, {
        renderer: 'canvas',
        useDirtyRect: false
      });
      const ROOT_PATH = 'https://fastly.jsdelivr.net/gh/apache/echarts-website@asf-site/examples';
      let option;
      myChart.showLoading();
      $.when(
        $.get(ROOT_PATH + '/data/asset/geo/USA.json'),
        $.getScript('https://fastly.jsdelivr.net/npm/d3-array'),
        $.getScript('https://fastly.jsdelivr.net/npm/d3-geo')
      ).done(function (res) {
        const usaJson = res[0];
        const projection = d3.geoAlbersUsa();
        myChart.hideLoading();
        echarts.registerMap('USA', usaJson);
        option = {
          title: {
            text: 'USA Population Estimates (2012)',
            subtext: 'Data from www.census.gov',
            sublink: 'http://www.census.gov/popest/data/datasets.html',
            left: 'right'
          },
          tooltip: {
            trigger: 'item',
            showDelay: 0,
            transitionDuration: 0.2
          },
          visualMap: {
            left: 'right',
            min: 500000,
            max: 38000000,
            inRange: {
              color: [
                '#313695',
                '#4575b4',
                '#74add1',
                '#abd9e9',
                '#e0f3f8',
                '#ffffbf',
                '#fee090',
                '#fdae61',
                '#f46d43',
                '#d73027',
                '#a50026'
              ]
            },
            text: ['High', 'Low'],
            calculable: true
          },
          toolbox: {
            show: true,
            //orient: 'vertical',
            left: 'left',
            top: 'top',
            feature: {
              dataView: { readOnly: false },
              restore: {},
              saveAsImage: {}
            }
          },
          series: [
            {
              name: 'USA PopEstimates',
              type: 'map',
              map: 'USA',
              projection: {
                project: function (point) {
                  return projection(point);
                },
                unproject: function (point) {
                  return projection.invert(point);
                }
              },
              emphasis: {
                label: {
                  show: true
                }
              },
              data: [
                { name: 'Alabama', value: 4822023 },
                { name: 'Alaska', value: 731449 },
                { name: 'Arizona', value: 6553255 },
                { name: 'Arkansas', value: 2949131 },
                { name: 'California', value: 38041430 },
                { name: 'Colorado', value: 5187582 },
                { name: 'Connecticut', value: 3590347 },
                { name: 'Delaware', value: 917092 },
                { name: 'District of Columbia', value: 632323 },
                { name: 'Florida', value: 19317568 },
                { name: 'Georgia', value: 9919945 },
                { name: 'Hawaii', value: 1392313 },
                { name: 'Idaho', value: 1595728 },
                { name: 'Illinois', value: 12875255 },
                { name: 'Indiana', value: 6537334 },
                { name: 'Iowa', value: 3074186 },
                { name: 'Kansas', value: 2885905 },
                { name: 'Kentucky', value: 4380415 },
                { name: 'Louisiana', value: 4601893 },
                { name: 'Maine', value: 1329192 },
                { name: 'Maryland', value: 5884563 },
                { name: 'Massachusetts', value: 6646144 },
                { name: 'Michigan', value: 9883360 },
                { name: 'Minnesota', value: 5379139 },
                { name: 'Mississippi', value: 2984926 },
                { name: 'Missouri', value: 6021988 },
                { name: 'Montana', value: 1005141 },
                { name: 'Nebraska', value: 1855525 },
                { name: 'Nevada', value: 2758931 },
                { name: 'New Hampshire', value: 1320718 },
                { name: 'New Jersey', value: 8864590 },
                { name: 'New Mexico', value: 2085538 },
                { name: 'New York', value: 19570261 },
                { name: 'North Carolina', value: 9752073 },
                { name: 'North Dakota', value: 699628 },
                { name: 'Ohio', value: 11544225 },
                { name: 'Oklahoma', value: 3814820 },
                { name: 'Oregon', value: 3899353 },
                { name: 'Pennsylvania', value: 12763536 },
                { name: 'Rhode Island', value: 1050292 },
                { name: 'South Carolina', value: 4723723 },
                { name: 'South Dakota', value: 833354 },
                { name: 'Tennessee', value: 6456243 },
                { name: 'Texas', value: 26059203 },
                { name: 'Utah', value: 2855287 },
                { name: 'Vermont', value: 626011 },
                { name: 'Virginia', value: 8185867 },
                { name: 'Washington', value: 6897012 },
                { name: 'West Virginia', value: 1855413 },
                { name: 'Wisconsin', value: 5726398 },
                { name: 'Wyoming', value: 576412 },
                { name: 'Puerto Rico', value: 3667084 }
              ]
            }
          ]
        };
        myChart.setOption(option);
      });

      if (option && typeof option === 'object') {
        myChart.setOption(option);
      }
    },*/
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
};
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
