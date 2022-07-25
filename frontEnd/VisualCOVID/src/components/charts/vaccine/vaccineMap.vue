<template>
  <el-card class="box-card">
  <div class='wrapper'>
    <div class='vaccineMap' id='vaccineMap'>
    </div>
  </div>
    <div class="tool-bar" id="map_toolbar">
      <el-descriptions title="Current Data Info">
        <el-descriptions-item label="Date ">{{summaryData.Date}}</el-descriptions-item>
        <el-descriptions-item label="Total Does Administered">
              <span style="color:green;font-weight:bolder">
                {{summaryData.does_admin}}</span>
        </el-descriptions-item>
        <el-descriptions-item label="Total Fully Vaccinated ">
              <span style="color:#0a6f7a;font-weight:bolder">
                {{summaryData.People_fully_vaccinated}}</span>
        </el-descriptions-item>
        <el-descriptions-item label="Total Partially Vaccinated ">
              <span style="color:#83b2ef;font-weight:bolder">
                {{summaryData.People_partially_vaccinated}}</span>
        </el-descriptions-item>
        <el-descriptions-item label="Data Source" class="source">
                <el-link :underline='false' v-bind:href="'https://coronavirus.jhu.edu/about/how-to-use-our-data'">
                  <span style="color:#1d86ee;font-weight:bolder">
                    Johns Hopkins (Click here to find the data source)
                  </span>
                </el-link>
        </el-descriptions-item>
      </el-descriptions>
      <span style="color:black;font-weight:bolder">Current Data Type:</span>
      <el-select v-model="value" placeholder="data type" @change="onSubmit">
        <el-option label="Doses administered" value="Doses"></el-option>
        <el-option label="Fully Vaccinated" value="fully"></el-option>
        <el-option label="Partially Vaccinated" value="partially"></el-option>
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
import * as echarts from "echarts";
import {getWorldSumVaccineData, vaccineDataProcess} from "../../../utils/vaccineDataProcess";

export default {
  name: "vaccineMap",
  data() {
    return {
      data:[],
      summaryData:{
        Date: '',
        does_admin:'',
        People_partially_vaccinated:'',
        People_fully_vaccinated:''
      },
      toolBarText:"Doses administered per person: ",
      toolBarText2:"Fully Vaccinated Rate: ",
      toolBarText3 :"Partially Vaccinated Rate: ",
      optionType:'map',
      value:'Doses'
    };
  },
  methods:{
    onSubmit(){
      this.runVaccineWorldMap()
    },
    switchGraphics(){
      this.runVaccineWorldMap()
    },
    async runVaccineWorldMap()
    {
      let data = await vaccineDataProcess(this.value)
      let res = await getWorldSumVaccineData()
      this.summaryData.Date = res.data.date
      this.summaryData.does_admin = res.data.doses_admin
      this.summaryData.People_partially_vaccinated = res.data.people_partially_vaccinated
      this.summaryData.People_fully_vaccinated = res.data.people_fully_vaccinated
      let context =this
      this.drawChart(context,data)
    },
    drawChart (context,data) {
      let chart = echarts.init(document.getElementById('vaccineMap'),'vintage',
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
          text: 'Coronavirus (COVID-19) Vaccination World Map Visualization',
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
            return '<span style="font-weight:bold">'+val.data.name +'</span><br/>'+'<span style="font-weight:bold">'
              +context.toolBarText+parseFloat(val.data.value[2].toFixed(4))+'</span><br/>'
              +'Doses administered: '+ '<span style="color:green;font-weight:bolder">'+val.data.other[0]+'</span>'+
              '<br/> Fully Vaccinated: '+'<span style="color:#0a6f7a;font-weight:bolder">'+val.data.other[2]+'</span>'
              +'<br/> Partially Vaccinated: '+'<span style="color:#83b2ef;font-weight:bolder">'+val.data.other[1]+'</span>'
          }
        },
        // Visual mapping component
        visualMap: {
          min: 0,
          max: 2,
          text:['max','min'],
          realtime: false,
          calculable: true,
          color: ['#2cd300','#a5da97','#ffad98','#a92020'],
        },
        series: [
          {
            id: 'Vaccine number',
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
          text: 'Latest Vaccination Ranking by Country',
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
          name: 'Vaccine number',
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
          id: 'Vaccine number',
          data: data.map(function (item) {
            return item.value[2];
          }),
          universalTransition: true,
          itemStyle: {
            color: '#2acc19'
          }
        }
      };
      if(this.value==="fully")
      {
        option.visualMap.color = ['#0a6f7a','#d1ebef','#ffad98','#a92020']
        option.visualMap.max = 1
        option.tooltip.formatter =function (val) {
        if(val.data == null) return ;
        return '<span style="font-weight:bold">'+val.data.name +'</span><br/>'+'<span style="font-weight:bold">'
          +context.toolBarText2+parseFloat(val.data.value[2].toFixed(4))+'</span><br/>'
          +'Doses administered: '+ '<span style="color:green;font-weight:bolder">'+val.data.other[0]+'</span>'+
          '<br/> Fully Vaccinated: '+'<span style="color:#0a6f7a;font-weight:bolder">'+val.data.other[2]+'</span>'
          +'<br/> Partially Vaccinated: '+'<span style="color:#83b2ef;font-weight:bolder">'+val.data.other[1]+'</span>'
      }
        barOption.series.itemStyle.color='#0a6f7a'
      }
      else if(this.value==="partially")
      {
        option.visualMap.color = ['#83b2ef','#eaf1f3','#ffad98','#a92020']
        option.visualMap.max = 1
        option.tooltip.formatter =function (val) {
          if(val.data == null) return ;
          return '<span style="font-weight:bold">'+val.data.name +'</span><br/>'+'<span style="font-weight:bold">'
            +context.toolBarText3+parseFloat(val.data.value[2].toFixed(4))+'</span><br/>'
            +'Doses administered: '+ '<span style="color:green;font-weight:bolder">'+val.data.other[0]+'</span>'+
            '<br/> Fully Vaccinated: '+'<span style="color:#0a6f7a;font-weight:bolder">'+val.data.other[2]+'</span>'
            +'<br/> Partially Vaccinated: '+'<span style="color:#83b2ef;font-weight:bolder">'+val.data.other[1]+'</span>'
        }
        barOption.series.itemStyle.color='#83b2ef'
      }
      if(context.optionType==='bar')
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
    this.runVaccineWorldMap();
  }
}
</script>

<style scoped>
.wrapper {
  width: 100%;
}
.wrapper .vaccineMap {
  width: 80%;
  margin:0 auto;
  height: 600px;
  background-size: 100% 100%;
}
</style>
