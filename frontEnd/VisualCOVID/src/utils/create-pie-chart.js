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
