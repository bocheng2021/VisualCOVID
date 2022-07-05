<template>
  <el-card class="box-card">
    <h3 style="text-align: center;font-weight: bolder">
      Comparison of historical new confirmed cases and deaths by country</h3>
    <div class='compare' id='compare'></div>
    <el-descriptions title="Current Data Info">
      <el-descriptions-item label="Current Country ">
        <span style="color:black;font-weight:bolder">
          {{CurrName[0]}}</span>
      </el-descriptions-item>
      <el-descriptions-item label="Current Select Country ">
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
import {countryProcess, messageBox1, nameProcess} from "../../../utils/timeLineDataProcess";
import * as echarts from "echarts";
import $ from "jquery";

export default {
  name: "compareTimeLine",
  data(){
    return{
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
    onSubmitCountry()
    {
      this.runCompareChart()
    },
    async runCompareChart() {
      const that =this
      let countryName = await nameProcess(this.CurrName[0]);
      let slug = countryName.data.slug
      $.ajax({
        type: 'get',
        url: 'https://api.covid19api.com/total/dayone/country/' + slug,
        dataType: 'json',
        success: function (res) {
          let i =0
          let LineData = []
          let barData = []
          let date = []
          for (i in res)
          {
            let tmp = res[i].Date.split('T')[0].split('-');
            date[i] = ([tmp[0], tmp[1], tmp[2]].join('/'));
            if(i==='0')
            {
              LineData[i]=res[i].Confirmed
              barData[i]=res[i].Deaths
            }
            else {
              LineData[i]=res[i].Confirmed - res[i-1].Confirmed;
              barData[i]=res[i].Deaths - res[i-1].Deaths;
            }
          }
          if (i===0)
          {
            messageBox1(that)
            that.CurrName=['United Kingdom']
            that.runCompareChart()
          }
          else
          {
            that.drawCompareChart(date,LineData,barData)
          }
        },
      })
    },
    drawCompareChart(date,LineData,barData)
    {
      let chart = echarts.init(document.getElementById('compare'),'vintage',
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
            type: 'shadow'
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
        legend: {
          show:true,
          data: ['New Confirms', 'New Death'],
          textStyle: {
            color: '#ccc'
          }
        },
        xAxis: {
          data: date,
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          }
        },
        yAxis: {
          splitLine: { show: true },
          axisLine: {
            lineStyle: {
              color: '#ccc'
            }
          }
        },
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
          }
        ],

        series: [
          {
            name: 'New Confirms',
            type: 'line',
            smooth: true,
            showAllSymbol: true,
            symbol: 'emptyCircle',
            symbolSize: 10,
            data: LineData
          },
          {
            name: 'New Death',
            type: 'bar',
            barWidth: 10,
            itemStyle: {
              borderRadius: 5,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#14c8d4' },
                { offset: 1, color: '#43eec6' }
              ])
            },
            data: barData
          },
          {
            name: 'New Confirms',
            type: 'bar',
            barGap: '-100%',
            barWidth: 10,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(20,200,212,0.5)' },
                { offset: 0.2, color: 'rgba(20,200,212,0.2)' },
                { offset: 1, color: 'rgba(20,200,212,0)' }
              ])
            },
            z: -12,
            data: LineData
          }
        ]
      }
      option && chart.setOption(option);
    }
  },
  mounted() {
    this.runCompareChart()
    countryProcess().then(res => {
      this.options=res
    });
  }
}
</script>

<style scoped>
.compare {
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
