<template>

</template>

<script>
import {getAllCountriesData,
  messageBox1,
  nameProcess
} from "../../utils/timeLineDataProcess";
import $ from "jquery";

export default {
  name: "reliability",
  data(){
    return{
      CurrName:['United Kingdom'],
      countries: [{
        name: 'United Kingdom',
        slug: 'United Kingdom',},
        {
          name: 'United States of America',
          slug: 'United States of America',
        }],
      leading_digits:[]
    }
  },
  methods:{
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
    console.log(this.leading_digits)
  }
}
</script>

<style scoped>

</style>
