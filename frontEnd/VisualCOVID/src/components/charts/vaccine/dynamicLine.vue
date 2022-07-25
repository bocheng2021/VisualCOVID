<template>
  <el-card class="box-card">
    <h3 style="text-align: center;font-weight: bolder">
      Dynamic Time Series Vaccination rate in major countries since 2021</h3>
    <div class='dynamic' id='dynamic'></div>
    <span style="color:black;font-weight:bolder">Current Data Type:</span>
    <el-select v-model="dataType" placeholder="data type" @change="onSubmit">
      <el-option label="Partially Vaccinated Rate" value="partially_rate"></el-option>
      <el-option label="Fully Vaccinated Rate" value="fully_rate"></el-option>
      <el-option label="Doses Administered per person" value="capita_num"></el-option>
    </el-select>
  </el-card>
</template>

<script>
import * as echarts from "echarts";
import {processDynamicChart} from "../../../utils/vaccineDataProcess";

export default {
  name: "dynamicLine",
  data(){
    return{
      dataType: 'capita_num'
    }
  },
  methods:{
    onSubmit(){
      this.runDynamicChart()
    },
    async runDynamicChart()
    {
      const countries = [
        'China',
        'France',
        'Austria',
        'US',
        'United Kingdom',
        'Germany',
        'Canada',
        'Russia',
        'Japan'
      ];
      let dataset = await processDynamicChart(countries,this.dataType)
      this.drawDynamicChart(dataset,this.dataType,countries);
    },
    drawDynamicChart(dataset,dataType,countries){
      let chart = echarts.init(document.getElementById('dynamic'),'vintage',
        {locale:'EN'}
      )
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option;
      const datasetWithFilters = [];
      const seriesList = [];
      echarts.util.each(countries, function (country) {
        const datasetId = 'dataset_' + country;
        datasetWithFilters.push({
          id: datasetId,
          fromDatasetId: 'dataset_raw',
          transform: {
            type: 'filter',
            config: {
              and: [
                { dimension: 'name', '=': country },
              ]
            }
          }
        });
        seriesList.push({
          type: 'line',
          datasetId: datasetId,
          showSymbol: false,
          name: country,
          endLabel: {
            show: true,
            formatter: function (params) {
              return params.value[0] + ': ' + params.value[2];
            }
          },
          labelLayout: {
            moveOverlap: 'shiftY'
          },
          emphasis: {
            focus: 'series'
          },
          encode: {
            x: 'date',
            y: dataType,
          }
        });
      });
      option = {
        animationDuration: 10000,
        dataset: [
          {
            id: 'dataset_raw',
            source: dataset.source,
          },
          ...datasetWithFilters
        ],
        tooltip: {
          order: 'valueDesc',
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          nameLocation: 'middle'
        },
        yAxis: {
          name: 'Vaccinated Rate'
        },
        grid: {
          right: 140
        },
        series: seriesList
      };
      option && chart.setOption(option);
    }
  },
  mounted() {
    this.runDynamicChart()
  }
}
</script>

<style scoped>
.dynamic {
  width: 100%;
  height: 600px;
  margin-top: 20px;
}
.el-card{
  margin-top: 20px;
}
.el-descriptions{
  margin-top: 20px;
}
</style>
