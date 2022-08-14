export function processPostmanAPIPieData(data,type)
{
  let countriesData = data.Countries
  const CountryName = [];
  const seriesData = [];
  let i = 0;
  for(i in countriesData)
  {
    const countryName = countriesData[i].Country;
    let value2=[];
    if (type==='1')
    {
      value2 = countriesData[i].TotalConfirmed;
    }
    else {
      value2 = countriesData[i].TotalDeaths;
    }
    CountryName.push(countryName);
    seriesData.push({
      name: countryName,
      value: value2
    });
  }
  return {
    CountryName: CountryName,
    seriesData: seriesData
  };
}

export function processPostmanAPIRosePieData(data)
{
  let countriesData = data.Countries
  const seriesData = [];
  let i = 0;
  for(i in countriesData)
  {
    const countryName = countriesData[i].Country;
    let valueData = countriesData[i].TotalDeaths
    seriesData.push({
      value: valueData,
      name: countryName,
    });
  }
  seriesData.sort(function (a, b) {
    return b.value-a.value;
  });
  return seriesData.slice(0, 10)
}


