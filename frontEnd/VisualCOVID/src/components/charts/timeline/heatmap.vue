<template>
  <el-card class="box-card">
    <h3 style="text-align: center;font-weight: bolder">
      History New Confirmed or New Deaths Data in Heatmap</h3>
    <div class='heatmap' id='heatmap'></div>
    <el-descriptions title="Current Data Info">
      <el-descriptions-item label="Date Type">
        <el-select v-model="DateCurr" placeholder="data type" @change="onSubmit">
          <el-option label="New Confirm" value="New_Confirm"></el-option>
          <el-option label="New Deaths" value="New_Deaths"></el-option>
        </el-select>
      </el-descriptions-item>
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
import $ from "jquery";
import * as echarts from "echarts";

export default {
  name: "heatmap",
  data(){
    return{
      CurrName:['United Kingdom'],
      DateCurr:'New_Confirm',
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
      this.runHeatmap()
    },
    onSubmit()
    {
      this.runHeatmap()
    },
    async runHeatmap() {
      const that =this
      let countryName = await nameProcess(this.CurrName[0]);
      let slug = countryName.data.slug
      $.ajax({
        type: 'get',
        url: 'https://api.covid19api.com/total/dayone/country/' + slug,
        dataType: 'json',
        success: function (res) {
          let i =0
          let idx =0
          let data = {
            data1:[],
            data2:[],
            data3:[]
          }
          let date = []
          let pre = ' '
          for (i in res)
          {
            let tmp = res[i].Date.split('T')[0].split('-');
            date[i] = ([tmp[0], tmp[1], tmp[2]].join('-'));
            if (pre===' ')
            {
              pre = tmp[0]
            }
            else if (tmp[0]!==pre)
            {
              pre = tmp[0]
              idx++
            }
            if(that.DateCurr==="New_Confirm")
            {
              if(idx===0)
              {
                if(i==='0')
                {
                  data.data1.push([date[i],res[i].Confirmed])
                }
                else {
                  data.data1.push([date[i],res[i].Confirmed - res[i-1].Confirmed])
                }
              }
              else if(idx ===1)
              {
                data.data2.push([date[i],res[i].Confirmed - res[i-1].Confirmed])
              }
              else
              {
                data.data3.push([date[i],res[i].Confirmed - res[i-1].Confirmed])
              }
            }
            else if(that.DateCurr==="New_Deaths")
            {
              if(idx===0)
              {
                if(i==='0')
                {
                  data.data1.push([date[i],res[i].Deaths])
                }
                else {
                  data.data1.push([date[i],res[i].Deaths - res[i-1].Deaths])
                }
              }
              else if(idx ===1)
              {
                data.data2.push([date[i],res[i].Deaths - res[i-1].Deaths])
              }
              else
              {
                data.data3.push([date[i],res[i].Deaths - res[i-1].Deaths])
              }
            }
          }
          console.log(Math.max.apply(null,data.data1))
          if (i===0)
          {
            messageBox1(that)
            that.CurrName=['United Kingdom']
            that.runHeatmap()
          }
          else
          {
            that.drawHeatmap(data)
          }
        },
      })
    },
    drawHeatmap(data){
      let chart = echarts.init(document.getElementById('heatmap'),'vintage',
        {locale:'EN'}
      )
      window.addEventListener('resize', function () {
        chart.resize()
      })
      chart.hideLoading();
      let option = {
        tooltip: {
          position: 'top',
          formatter: function (p) {
            const format = echarts.format.formatTime('yyyy-MM-dd', p.data[0]);
            return '<span style="color:black;font-weight:bolder">'+format + ': ' +p.data[1]+'</span>';
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
        visualMap: {
          min: 0,
          max: this.compareArr(data),
          calculable: true,
          orient: 'horizontal',
          left: 'center',
          top: 'top'
        },
        calendar: [
          {
            range: '2020',
            cellSize: ['auto', 20]
          },
          {
            top: 260,
            range: '2021',
            cellSize: ['auto', 20]
          },
          {
            top: 450,
            range: '2022',
            cellSize: ['auto', 20],
            right: 5
          }
        ],
        series: [
          {
            type: 'heatmap',
            coordinateSystem: 'calendar',
            calendarIndex: 0,
            data: data.data1
          },
          {
            type: 'heatmap',
            coordinateSystem: 'calendar',
            calendarIndex: 1,
            data: data.data2
          },
          {
            type: 'heatmap',
            coordinateSystem: 'calendar',
            calendarIndex: 2,
            data: data.data3
          }
        ]
      };
      option && chart.setOption(option);
    },
    compareArr(arr){
      let num1 = 0
      let num2 =0
      let num3 =0
      for(let i=0; i<arr.data1.length; i++){
        if(arr.data1[i][1]>num1){
          num1=arr.data1[i][1];
        }
      }
      for(let i=0; i<arr.data2.length; i++){
        if(arr.data2[i][1]>num2){
          num2=arr.data2[i][1];
        }
      }
      for(let i=0; i<arr.data3.length; i++){
        if(arr.data3[i][1]>num3){
          num3=arr.data3[i][1];
        }
      }
      return (num1+num2+num3)/3
    }
  },
  mounted() {
    this.runHeatmap()
    countryProcess().then(res => {
      this.options=res
    });
  }
}
</script>

<style scoped>
.heatmap {
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
