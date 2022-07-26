<template>
  <el-card class="box-card">
    <div class='reliability_line' id='reliability_line'></div>
    <el-descriptions title="Data Info">
      <el-descriptions-item label="Data Sampling Time ">2022-7-25</el-descriptions-item>
      <el-descriptions-item label="Data Source ">
              <span style="color:black;font-weight:bolder">Postman</span>
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script>
import {getAllCountriesData,
} from "../../utils/timeLineDataProcess";
import $ from "jquery";
import * as echarts from "echarts";

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
    getReliabilityData()
    {
      const that = this
      let i =0
      let data =[]
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
      console.log(data)
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
          data: ['1', '2', '3', '4', '5', '6', '7','8','9']
        },
        yAxis: {
          type: 'value',
          name: 'Digit Frequency',
        },
        series: [
          {
            name: 'United Kingdom',
            type: 'line',
            stack: 'Total',
            data: data[0]
          },
          {
            name: 'United States of America',
            type: 'line',
            stack: 'Total',
            data: data[1]
          },
          {
            name: 'China',
            type: 'line',
            stack: 'Total',
            data: data[2]
          },
          {
            name: 'Japan',
            type: 'line',
            stack: 'Total',
            data: data[3]
          },
          {
            name: 'Austria',
            type: 'line',
            stack: 'Total',
            data: data[4]
          },
          {
            name: 'Canada',
            type: 'line',
            stack: 'Total',
            data: data[5]
          },
          {
            name: 'France',
            type: 'line',
            stack: 'Total',
            data: data[6]
          },
          {
            name: 'Germany',
            type: 'line',
            stack: 'Total',
            data: data[7]
          }
        ]
      };
      option && chart.setOption(option);
    },
    async processSlugData() {
      this.countries = await getAllCountriesData()
      const that= this
      let j =249
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
            let num = parseInt(res[i].Confirmed.toString().charAt(0))
            if(i==='0')
            {
              if (num!==0)
              {
                frequencyData[num-1]++
                count++
              }
            }
            else if(num!==0){
              frequencyData[num-1]++
              count++
            }
          }
          for (i in frequencyData)
          {
            frequencyData[i] = frequencyData[i]/count
          }
          console.log(name,slug,frequencyData)
          that.insertFreData(name,slug,frequencyData)
        },
      })

    },
    insertFreData(name,slug,frequencyData)
    {
      $.ajax({
        url: 'http://localhost:8081/insert_reliabilityData?name='+name+'&slug='+slug+'&freq_1='+frequencyData[0]
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
