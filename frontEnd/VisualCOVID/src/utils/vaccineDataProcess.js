import axios from "axios";
import country from "../assets/country/countryEN.json";
import countryPop from "../assets/country/country-by-population.json"
import {countryNameTransferForLocation, countryTransferForMap} from "./nameTransfer";

export async function vaccineDataProcess(DataType) {
  let countries = await axios.get('http://localhost:8081/getSumVaccineData')
  let i =0;
  let index =0;
  let res = []
  let nameList = country
  for (i in countries.data)
  {
    let countryName = countries.data[i].country_Region;
    countryName = countryNameTransferForLocation(countryName);
    if (nameList[countryName]!=null)
    {
      const value0 = nameList[countryName][0];
      const value1 = nameList[countryName][1];
      const value2 = countries.data[i].doses_admin;
      const value3 = countries.data[i].people_partially_vaccinated;
      const value4 = countries.data[i].people_fully_vaccinated;
      let inputName = countryTransferForMap(countryName)
      let pop =findPopulationByCountry(inputName)
      if (DataType==='fully')
      {
        res[index]={name:inputName,value:[value0,value1,value4/pop.toFixed(4)],
          other:[value2,value3,value4]};
      }
      else if (DataType==='partially')
      {
        res[index]={name:inputName,value:[value0,value1,value3/pop.toFixed(4)],other:[value2,value3,value4]};
      }
      else
      {
        res[index]={name:inputName,value:[value0,value1,value2/pop.toFixed(4)],other:[value2,value3,value4]};
      }
      index ++;
    }
  }
  return res
}

export async function processDynamicChart(countryList,vaccine_type)
{
  let i =0;
  let dataset={
    source:[
      ["name","date",vaccine_type],
    ]
  }
  for (i in countryList)
  {
    let res= await getVaccineTimeSeriesDataByCountry(countryList[i])
    let j =0;
    let pop;
    let pre =0;
    if (countryList[i]==='US')
    {
      pop =findPopulationByCountry('United States')
    }
    else
    {
      pop =findPopulationByCountry(countryList[i])
    }
    for (j in res.data)
    {
      /*dataset.source.push([res.data[j].Country_Region,res.data[j].Date,res.data[j].Doses_admin,
        res.data[j].People_partially_vaccinated,res.data[j].People_fully_vaccinated])*/
      if (vaccine_type==='fully_rate')
      {
        if (res.data[j].people_partially_vaccinated/pop<pre)
        {
          dataset.source.push([res.data[j].country_Region,res.data[j].date,pre])
        }
        else
        {
          dataset.source.push([res.data[j].country_Region,res.data[j].date,res.data[j].people_fully_vaccinated/pop])
          pre = res.data[j].people_fully_vaccinated/pop
        }
      }
      else if (vaccine_type==='partially_rate')
      {
        if (res.data[j].people_partially_vaccinated/pop<pre)
        {
          dataset.source.push([res.data[j].country_Region,res.data[j].date,pre])
        }
        else
        {
          dataset.source.push([res.data[j].country_Region,res.data[j].date,res.data[j].people_partially_vaccinated/pop])
          pre = res.data[j].people_partially_vaccinated/pop
        }
      }
      else
      {
        dataset.source.push([res.data[j].country_Region,res.data[j].date,res.data[j].doses_admin/pop])
      }
    }
  }
  return dataset
}

export function findPopulationByCountry(name)
{
  let i=0;
  let pop =0;
  for(i in countryPop)
  {
    if (countryPop[i].country===name)
    {
      pop = countryPop[i].population
      break;
    }
  }
  return pop
}

export async function getWorldSumVaccineData() {
  return await axios.get('http://localhost:8081/getWorldSumVaccineData')
}


export async function getVaccineTimeSeriesDataByCountry(countryName)
{
  return await axios.get('http://localhost:8081/getTimeSeriesVaccineByCountry?countryName='+countryName)
}
