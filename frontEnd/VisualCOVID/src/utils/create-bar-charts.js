export function test()
{
  const axios = require("axios");

  const options = {
    method: 'GET',
    url: 'https://covid-193.p.rapidapi.com/history',
    params: {country: 'usa'},
    headers: {
      'X-RapidAPI-Key': '347ab949b4msh10b962fa5b22f96p1e6bd5jsn2d1bdf27d549',
      'X-RapidAPI-Host': 'covid-193.p.rapidapi.com'
    }
  };

  axios.request(options).then(function (response) {
    console.log(response.data);
  }).catch(function (error) {
    console.error(error);
  });
}
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
