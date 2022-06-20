export function processPostmanAPIBarData(data)
{
  let countriesData = data.Countries
  const tmp = []
  let i = 0;
  let index = 0;
  for(i in countriesData)
  {
    const countryName = countriesData[i].Country;
    const value2 = countriesData[i].TotalConfirmed;
    const value3 = countriesData[i].TotalDeaths;
    tmp[index]={names:countryName,confirms:value2,deaths:value3};
    index ++;
  }
  i =0;
  index = 0;
  let res = {
    names:[],
    confirms:[],
    deaths:[]
  }
  for(i in tmp)
  {
    res.names[index]=tmp[i].names;
    res.confirms[index]=tmp[i].confirms;
    res.deaths[index]=tmp[i].deaths;
    index ++;
  }
  return res;
}
