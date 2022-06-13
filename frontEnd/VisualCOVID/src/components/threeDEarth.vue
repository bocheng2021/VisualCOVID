<template>
  <el-container style="height: 100%">
    <el-main>
      <div id="earth">
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
      type: "total",
      toolBarText :"Total Confirmed: ",
      toolBarText2:"Total Deaths: "
    }
  },
  methods: {
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
          context.getThreeDEarth(context,data)
        }
      })
    },
    resizeMainContainer(container) {
      container.style.width = window.innerWidth*0.75 + 'px';
      container.style.height = window.innerHeight + 'px';
    },
    getThreeDEarth(context, data)
    {
      const mainContainer = document.getElementById('earth');
      this.resizeMainContainer(mainContainer);
      const mainChart = echarts.init(mainContainer);
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
          /*layers: [{
              type: 'overlay',
              texture: cloud,
              shading: 'realistic',
              distance: 3
              }],*/
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
                show:true/*
                formatter: function (params) {
                  return params.name + ":" + "Total confirm:" + params.value[2];
                },
                textStyle:
                  {
                    color: 'rgb(255,255,255)',
                    borderWidth: 0,
                    borderColor: '#fff',
                    fontFamily: 'sans-serif',
                    fontSize: 18,
                    fontWeight: 700
                  },*/
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
      mainChart.setOption(option);
    }
  },
  mounted(){
    this.runWorldMapEN(this);
  },
}
</script>

<style scoped>

</style>
