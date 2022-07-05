import 'echarts/map/js/world.js'
import 'echarts-gl'
import country from "../assets/country/countryEN.json";
import countryCN from "../assets/country/country.json"
import {countryTransferForMap} from "./nameTransfer";
export function processPostmanAPIEarthData(data,type)
{
  let countriesData = data.Countries
  let nameList = country
  const res = []
  let i = 0;
  let index = 0;
  for(i in countriesData)
  {
    const countryName = countriesData[i].Country;
    if (nameList[countryName]!=null)
    {
      const value0 = nameList[countryName][0];
      const value1 = nameList[countryName][1];
      const value2 = countriesData[i].TotalConfirmed;
      const value3 = countriesData[i].TotalDeaths;
      let inputName = countryTransferForMap(countryName)
      if(type==="total_3d_1")
      {
        res[index]={name:countryName,value:[value0,value1,value2],other:[value3]};
      }
      else if(type==="total_3d_2")
      {
        res[index]={name:countryName,value:[value0,value1,value3],other:[value2]};
      }
      else if(type==="total_2d")
      {
        res[index]={name:inputName,value:[value0,value1,value2],other:[value3]};
      }
      else if(type==="total_2d_death")
      {
        res[index]={name:inputName,value:[value0,value1,value3],other:[value2]};
      }
      index ++;
    }
  }
  return res;
}
export function processTencentAPIEarthData(res01){
  //then get the disease data for the other country.
  res01=res01.data;
  let nameList=countryCN;
  //Preprocess the data.
  res01=JSON.parse(res01);

  /*
  //get the china data
  let res = await fetch_china();
  let json_str = res.data;
  let start = json_str.indexOf('{');
  let end = json_str.lastIndexOf('}');
  json_str = json_str.slice(start, end + 1);
  let Chinadata = JSON.parse(json_str);
  Chinadata = JSON.parse(Chinadata.data);
   */

  let data = res01.foreignList;
  //then get the disease data for the country.
  const DiseaseData = [];
  let i = 0;
  let index =0;
  //DiseaseData[0]={name:"中国",value:[nameList["中国"][0],nameList["中国"][1],Chinadata.chinaTotal.confirm]};
  for(i in data)//process the data.
  {
    const countryName = data[i].name;
    if(nameList[countryName]!=null && countryName!=="美国")
    {
      const value0 = nameList[countryName][0];
      const value1 = nameList[countryName][1];
      const value2 = data[i].confirm;
      DiseaseData[index]={name:countryName,value:[value0,value1,value2]};
      index++;
    }
    else if(countryName==="美国")
    {
      let children=data[i].children;
      let totalData=null;
      for(const j in children)
      {
        totalData=children[j].confirm+totalData;
      }
      DiseaseData[index]={
        name:countryName,
        value:[nameList[countryName][0], nameList[countryName][1], totalData]
      };
      index++;
    }
  }
  //DiseaseData[137]={name:"中国",value:[nameList["中国"][0],nameList["中国"][1],Chinadata.chinaTotal.confirm]};
  return DiseaseData;
}
