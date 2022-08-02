import axios from "axios";
import country from "../assets/country/countryEN.json";
import {countryNameTransferForLocation, countryTransferForMap} from "./nameTransfer";

export async function reliabilityDataProcess()
{
  let data = await axios.get('http://localhost:8081/getAllReliabilityData')
  let nameList = country
  let i =0
  let index =0
  //the result set
  let res = []
  for (i in data.data)
  {
    let countryName = data.data[i].name;
    countryName = countryNameTransferForLocation(countryName);
    if (nameList[countryName]!=null)
    {
      const value0 = nameList[countryName][0];
      const value1 = nameList[countryName][1];
      const value2 = data.data[i].flag;
      const ssd = data.data[i].ssd;
      const mad = data.data[i].mad;
      const d_factor = data.data[i].d_factor;
      let inputName = countryTransferForMap(countryName)
      res[index]={name:inputName,value:[value0,value1,value2],other:[ssd.toFixed(4),
          mad.toFixed(4),d_factor.toFixed(4)]}
      index++
    }
  }
  return res
}
