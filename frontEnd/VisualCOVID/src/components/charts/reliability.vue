<template>
  <el-card class="box-card">
    <div class='reliability_line' id='reliability_line'></div>
    <el-descriptions title="Data Info">
      <el-descriptions-item label="Data Sampling Time ">2022-7-31</el-descriptions-item>
      <el-descriptions-item label="Data Source ">
              <span style="color:black;font-weight:bolder">Postman</span>
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script>
import {getAllCountriesData} from "../../utils/timeLineDataProcess";
import $ from "jquery";
import * as echarts from "echarts";
let j = 0;
//<el-descriptions-item label="Process Data">
//<el-button type="primary" round @click="onSubmit()">Process One</el-button>
//</el-descriptions-item>
export default {
  name: "reliability",
  data(){
    return{
      CurrCountries:['United Kingdom','United States of America','China','Japan','Austria','Canada','France','Germany'],
      countries: [{
        name: 'United Kingdom',
        slug: 'United Kingdom',},
        {
          name: 'United States of America',
          slug: 'United States of America',
        }],
    }
  },
  methods:{
    onSubmit()
    {
      this.processSlugData()
      j++
      console.log(j)
    },
    getReliabilityData()
    {
      const that = this
      let i = 0
      let data = []
      for ( i in this.CurrCountries)
      {
        $.ajax({
          type: 'get',
          url: 'http://localhost:8081/getReliabilityDataByCountry?name=' + that.CurrCountries[i],
          dataType: 'json',
          success: function (res) {
            data.push([res.freq_1,res.freq_2,res.freq_3,res.freq_4,
              res.freq_5,res.freq_6,res.freq_7,res.freq_8,res.freq_9])
            if (data.length===that.CurrCountries.length){
              that.drawFreqChart(data)
            }
          },
        })
      }
    },
    drawFreqChart(data)
    {
      let chart = echarts.init(document.getElementById('reliability_line'),'vintage',
        {locale:'EN'}
      )
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option = {
        title: {
          text: 'Frequency Line Chart'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['United Kingdom','United States of America','China','Japan','Austria','Canada','France','Germany']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {},
            dataView: {
              show: true,
              readOnly: false,
            },
          }
        },
        xAxis: {
          type: 'category',
          name: 'Digit',
          boundaryGap: false,
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9']
        },
        yAxis: {
          type: 'value',
          name: 'Digit Frequency',
        },
        series: [
          {
            name: 'United Kingdom',
            type: 'line',
            data: data[0]
          },
          {
            name: 'United States of America',
            type: 'line',
            data: data[1]
          },
          {
            name: 'China',
            type: 'line',
            data: data[2]
          },
          {
            name: 'Japan',
            type: 'line',
            data: data[3]
          },
          {
            name: 'Austria',
            type: 'line',
            data: data[4]
          },
          {
            name: 'Canada',
            type: 'line',
            data: data[5]
          },
          {
            name: 'France',
            type: 'line',
            data: data[6]
          },
          {
            name: 'Germany',
            type: 'line',
            data: data[7]
          }
        ]
      };
      option && chart.setOption(option);
    },
    async processSlugData() {
      this.countries = await getAllCountriesData()
      const that= this
      let slug = this.countries[j].slug
      let name = this.countries[j].name
      $.ajax({
        type: 'get',
        url: 'https://api.covid19api.com/total/dayone/country/' + slug,
        dataType: 'json',
        success: function (res) {
          let i =0
          let frequencyData = [0,0,0,0,0,0,0,0,0]
          let count = 0
          for (i in res)
          {
            if(i==='0')
            {
              let num = parseInt(res[i].Confirmed.toString().charAt(0))
              if (num!==0)
              {
                frequencyData[num-1]++
                count++
              }
            }
            else {
              let num = parseInt((res[i].Confirmed - res[i-1].Confirmed).toString().charAt(0))
              if (num!==0)
              {
                frequencyData[num-1]++
                count++
              }
            }
          }
          for (i in frequencyData)
          {
            frequencyData[i] = frequencyData[i]/count
          }
          console.log(name,slug,count,frequencyData)
          that.insertFreData(name,slug,count,frequencyData)
        },
      })

    },
    insertFreData(name,slug,number,frequencyData)
    {
      $.ajax({
        url: 'http://localhost:8081/insert_reliabilityData?name='+name+'&slug='+slug+'&number='+number+'&freq_1='+frequencyData[0]
        +'&freq_2='+frequencyData[1]+'&freq_3='+frequencyData[2]+'&freq_4='+frequencyData[3]+'&freq_5='+frequencyData[4]
          +'&freq_6='+frequencyData[5]+'&freq_7='+frequencyData[6]+'&freq_8='+frequencyData[7]+'&freq_9='+frequencyData[8],
        type: "get",
        dataType: "json",
        success: function (res) {
          console.log('success!')
        },
      });
    }
  },
  mounted() {
    this.getReliabilityData()
  }
}
</script>

<style scoped>
.reliability_line {
  width: 100%;
  height: 400px;
  margin-top: 20px;
}
.el-card{
  margin-top: 20px;
}
</style>
