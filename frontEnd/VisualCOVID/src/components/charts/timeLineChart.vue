<template>
  <el-card class="box-card">
    <div class='timeLine' id='timeLine'></div>
    <el-descriptions title="Current Data Info">
      <el-descriptions-item label="Date Type">
        <el-select v-model="DateCurr" placeholder="data type" @change="onSubmit">
          <el-option label="Total Death" value="Total_death"></el-option>
          <el-option label="Total Confirm" value="Total_confirm"></el-option>
          <el-option label="New Confirm" value="New_Confirm"></el-option>
          <el-option label="Total Active" value="Total_Active"></el-option>
          <el-option label="Total Recovered" value="Total_Recovered"></el-option>
        </el-select>
      </el-descriptions-item>
      <el-descriptions-item label="Current Country ">
        <span style="color:black;font-weight:bolder">
          {{CurrName[0]}}</span>
      </el-descriptions-item>
      <el-descriptions-item label="Current Country ">
        <div class="block">
          <el-cascader
            placeholder="Input country name.."
            v-model="CurrName"
            :options="options"
            filterable @change="onSubmitCountry"></el-cascader>
        </div>
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script>
import $ from "jquery";
import {countryProcess, messageBox1, nameProcess} from "../../utils/timeLineDataProcess";
import * as echarts from "echarts";
import {test} from "../../utils/create-bar-charts";

export default {
  name: "timeLineChart",
  data(){
    return{
      DateCurr:'Total_confirm',
      CurrName:['United Kingdom'],
      options: [{
        value: 'United Kingdom',
        label: 'United Kingdom',},
        {
          value: 'United States of America',
          label: 'United States of America',
        }]
    }
  },
  methods:{
    onSubmit()
    {
      this.runtimeLineChart()
    },
    onSubmitCountry()
    {
      this.runtimeLineChart()
    },
    async runtimeLineChart() {
      const that =this
      let countryName = await nameProcess(this.CurrName[0]);
      let slug = countryName.data.slug
      $.ajax({
        type: 'get',
        url: 'https://api.covid19api.com/total/dayone/country/' + slug,
        dataType: 'json',
        success: function (res) {
          let i =0
          let data = []
          let date = []
          for (i in res)
          {
            let tmp = res[i].Date.split('T')[0].split('-');
            date[i] = ([tmp[0], tmp[1], tmp[2]].join('/'));
            if(that.DateCurr==="Total_confirm")
            {
              data[i]=res[i].Confirmed;
            }
            else if(that.DateCurr==="New_Confirm")
            {
              if(i==='0')
              {
                data[i]=res[i].Confirmed
              }
              else {
                data[i]=res[i].Confirmed - res[i-1].Confirmed;
              }
            }
            else if(that.DateCurr==="Total_death")
            {
              data[i]=res[i].Deaths;
            }
            else if(that.DateCurr==="Total_Active")
            {
              data[i]=res[i].Active;
            }
            else if(that.DateCurr==="Total_Recovered")
            {
              data[i]=res[i].Recovered;
            }
          }
          if (i===0)
          {
            messageBox1(that)
            that.CurrName=['United Kingdom']
            that.runtimeLineChart()
          }
          else
          {
            that.drawTimeLineChart(date,data)
          }
        },
      })
    },
    drawTimeLineChart(date,data)
    {
      let chart = echarts.init(document.getElementById('timeLine'),'vintage',
        {locale:'EN'}
      )
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option = {
        tooltip: {
          trigger: 'axis',
          position: function (pt) {
            return [pt[0], '10%'];
          }
        },
        title: {
          left: 'center',
          text: 'Covid-19 Timeline Chart'
        },
        toolbox: {
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            restore: {},
            saveAsImage: {}
          }
        },
        xAxis: {
          name:'Date',
          type: 'category',
          boundaryGap: false,
          data: date
        },
        yAxis: {
          name:'People Number',
          type: 'value',
          boundaryGap: [0, '100%']
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 10
          },
          {
            start: 0,
            end: 10
          }
        ],
        series: [
          {
            name: 'Disease Data',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: 'rgb(255, 70, 131)'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(255, 158, 68)'
                },
                {
                  offset: 1,
                  color: 'rgb(255, 70, 131)'
                }
              ])
            },
            data: data
          }
        ]
      };
      option && chart.setOption(option);
    },
  },
  mounted() {
    this.runtimeLineChart()
    countryProcess().then(res => {
      this.options=res
    });
    //test()
  }
}
</script>

<style scoped>
.timeLine {
  width: 100%;
  height: 400px;
  margin-top: 20px;
}
.el-card{
  margin-top: 20px;
}
.el-descriptions{
  margin-top: 20px;
}
</style>
