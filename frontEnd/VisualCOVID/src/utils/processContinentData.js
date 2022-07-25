export function processContinentData(data)
{
  let res= {
    Africa:[],
    North_America:[],
    Oceania:[],
    Asia:[],
    South_America:[],
    Europe:[]
  }
  let i =0
  for (i in data){
    if (data[i].continent!=null && data[i].deaths.total!=null &&data[i].continent!==data[i].country)
    {
      if (data[i].continent==="Africa"){
        res.Africa.push([data[i].cases.total,data[i].deaths.total,data[i].country])
      }
      else if (data[i].continent==="North-America")
      {
        res.North_America.push([data[i].cases.total,data[i].deaths.total,data[i].country])
      }
      else if (data[i].continent==="Oceania")
      {
        res.Oceania.push([data[i].cases.total,data[i].deaths.total,data[i].country])
      }
      else if (data[i].continent==="Asia")
      {
        res.Asia.push([data[i].cases.total,data[i].deaths.total,data[i].country])
      }
      else if (data[i].continent==="South-America")
      {
        res.South_America.push([data[i].cases.total,data[i].deaths.total,data[i].country])
      }
      else
      {
        res.Europe.push([data[i].cases.total,data[i].deaths.total,data[i].country])
      }
    }
  }
  return res
}

