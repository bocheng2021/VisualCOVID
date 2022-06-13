<template>
  <el-container style="height: 100%">
    <el-main>
      <el-card class="box-card">
      <div class='wrapper'>
        <div class='chart' id='earth'>
        </div>
      </div>
      </el-card>
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
        <el-form :inline="false" :model="formInline" class="demo-form-inline">
          <el-form-item label="Select Data Type:" style="font-weight: bold">
            <el-select v-model="type" placeholder="data type" @change="onSubmit">
              <el-option label="Total Death" value="total_3d_2"></el-option>
              <el-option label="Total Confirm" value="total_3d_1"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-main>
  </el-container>
</template>

<script>
import * as echarts from 'echarts';
import 'echarts/map/js/world.js'
import 'echarts-gl'
import $ from "jquery";
import bg4 from "../assets/image/bg4.jpg";
import worldImg from "../assets/image/bg4.jpg";
import starfieldImg from "../assets/image/starfield.jpg";
import {processPostmanAPIEarthData, processTencentAPIEarthData} from "../utils/create-earth";

export default {
  name:"threeDEarth",
  data(){
    return{
      worldData:[],
      type: "total_3d_1",
      toolBarText :"Total Confirmed: ",
      toolBarText2:"Total Deaths: ",
      formInline: {
        user: '',
        region: ''
      },
      DateCurr:'',
      summaryData:[],
    }
  },
  methods: {
    onSubmit(){
      this.runWorldMapEN(this)
    },
    /*
    using tencent api, data comes from Johns Hopkins
     */
    runWorldMapCN(context)
    {
      $.ajax({
        type:'get',
        url:'https://view.inews.qq.com/g2/getOnsInfo?name=disease_foreign',
        dataType:'jsonp',
        success :function (res)
        {
          let data = processTencentAPIEarthData(res);
          context.getThreeDEarth(data)
        }
      })
    },
    /*
    using postman api
     */
    runWorldMapEN(context)
    {
      $.ajax({
        type:'get',
        url:'https://api.covid19api.com/summary',
        dataType:'json',
        success :function (res)
        {
          let data = processPostmanAPIEarthData(res,context.type)
          context.summaryData = res.Global
          context.DateCurr = context.summaryData.Date.split('T')[0]
          context.getThreeDEarth(context,data)
        }
      })
    },
    getThreeDEarth(context, data)
    {
      const mainChart = echarts.init(document.getElementById('earth'));
      window.addEventListener('resize', function () {
        mainChart.resize()
      })
      //initial the earth.
      let option =  {
        title: {
          text: '3D-Earth Coronavirus (COVID-19) Visualization',
          top: 10,
          left: 'center',
          textStyle: {
            fontSize: 20,
            fontWeight: 600,
            color: 'white'
          }
        },
        tooltip: {
          trigger: 'item',
          showDelay: 0,
          transitionDuration: 0.2,
          formatter: function (val) {
            if(val.data == null) return ;
            return '<span style="font-weight:bold">'+val.data.name +'</span><br/>'+
              context.toolBarText + '<span style="color:Green;font-weight:bolder">'+val.data.value[2]+'</span>'+
              '<br/>'+context.toolBarText2+'<span style="color:red;font-weight:bolder">'+val.data.other[0]+'</span>'
          }
        },
        visualMap: {
          //set the minimap for the data.
          show: true,
          calculable: true,
          min: 0,
          max: 80000000,
          text:['max','min'],
          inRange: {
            colorLightness: [1.0, 0.3]
            //the color range of the minimap
          },
          textStyle: {
            color: '#fff'
            //the color of the text minimap
          },
          controller: {
            inRange: {
              color: 'red'
              //the color of the visual map.
            }
          },
          outOfRange: {
            colorAlpha: 0
          }
        },
        globe: {
          baseTexture: bg4,
          displacementScale: 0.02,
          displacementQuality: 'high',
          environment: starfieldImg, //background texture
          heightTexture: bg4,
          shading: 'realistic',
          realisticMaterial: {
            roughness: 0.8,
            metalness: 0
          },
          light: {
            ambient: {
              intensity: 0.7
              //global main light
            },
            main: {
              intensity: 5,
              shadow: true
              //global shadow effect
            },
            ambientCubemap: {
              texture: "../assets/image/pisa.hdr",
              diffuseIntensity: 0.2
            }
          },
          postEffect: {
            enable: true,
            depthOfField:
              {
                focalRange: 100,
                enable: false,
                focalDistance: 110
              },
            SSAO: {
              enable: true,
              radius: 10
            }
          },
          temporalSuperSampling: {
            enable: true
          },
          viewControl: {
            autoRotate: false,
            autoRotateSpeed: 5,
            autoRotateAfterStill: 3,
            targetCoord: [-0.126608, 51.208425]
          }
        },
        series: [{
          type: 'bar3D',
          coordinateSystem: 'globe',
          shading: 'realistic',//add shadow effect to the bar.
          bevelSmoothness: 2,
          emphasis:
            {
              itemStyle: {
                color: 'red',
                borderColor: 'white',
              },
              label: {
                show:true
              },
            },
          barSize: 1.8,
          minHeight: 0.1,
          maxHeight: 2.0,
          data: data,
          itemStyle: {
            color: 'red'
          },
        }]
      };
      if (context.type==='total_3d_2')
      {
        option.visualMap.max = 100000
        option.tooltip.formatter= function(val) {
          if(val.data == null) return ;
          return '<span style="font-weight:bold">'+val.data.name +'</span>'+'<br/>'
            +context.toolBarText+'<span style="color:Green;font-weight:bolder">'+val.data.other[0]+'</span><br/>'+
            context.toolBarText2 + '<span style="color:red;font-weight:bolder">'+val.data.value[2]+'</span>'
        }
      }
      mainChart.setOption(option);
    }
  },
  mounted(){
    this.runWorldMapEN(this);
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
        this.runWorldMapEN(val)
      }
    }
  },
}
</script>

<style scoped>
.wrapper {
  width: 100%;
  background-color: black;
}
.wrapper .chart {
  width: 90%;
  margin:0 auto;
  height: 600px;
  background-color: black;
  background-size: 100% 100%;
}
.tool-bar{
  margin-top: 20px;
}
</style>
