<template>
  <el-card class="box-card">
    <div class='bar-chart' id='bar-chart'></div>
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
      summaryData:[],
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
            magicType: {
              show: true,
              type: ['line', 'bar'],
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
          itemGap: 5
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
