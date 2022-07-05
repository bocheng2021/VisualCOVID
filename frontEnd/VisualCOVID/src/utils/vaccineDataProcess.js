import axios from "axios";
import country from "../assets/country/countryEN.json";
import {countryNameTransferForLocation, countryTransferForMap} from "./nameTransfer";

export async function vaccineDataProcess(DataType) {
  let countries = await axios.get('http://localhost:8081/getSumVaccineData')
  let i =0;
  let index =0;
  let res = []
  let nameList = country
  for (i in countries.data)
  {
    let countryName = countries.data[i].Country_Region;
    countryName = countryNameTransferForLocation(countryName);
    if (nameList[countryName]!=null)
    {
      const value0 = nameList[countryName][0];
      const value1 = nameList[countryName][1];
      const value2 = countries.data[i].Doses_admin;
      const value3 = countries.data[i].People_partially_vaccinated;
      const value4 = countries.data[i].People_fully_vaccinated;
      let inputName = countryTransferForMap(countryName)
      if (DataType==='fully')
      {
        res[index]={name:inputName,value:[value0,value1,value4],other:[value2,value3]};
      }
      else if (DataType==='partially')
      {
        res[index]={name:inputName,value:[value0,value1,value3],other:[value2,value4]};
      }
      else
      {
        res[index]={name:inputName,value:[value0,value1,value2],other:[value3,value4]};
      }
      index ++;
    }
  }
  return res
}

export async function getWorldSumVaccineData() {
  return await axios.get('http://localhost:8081/getWorldSumVaccineData')
}
