export function countryTransferForMap(countryName)
{
  let inputName =countryName
  if(countryName==="United States of America")
  {
    inputName = "United States";
  }
  else if(countryName==="Russian Federation")
  {
    inputName = "Russia";
  }
  else if(countryName==="Congo (Brazzaville)")
  {
    inputName = "Congo";
  }
  else if(countryName==="Congo (Kinshasa)")
  {
    inputName = "Dem. Rep. Congo";
  }
  else if(countryName==="Viet Nam")
  {
    inputName = "Vietnam";
  }
  else if(countryName==="Korea (North)")
  {
    inputName = "Dem. Rep. Korea";
  }
  else if(countryName==="Korea (South)")
  {
    inputName = "Korea";
  }
  else if(countryName==="Central African Republic")
  {
    inputName = "Central African Rep.";
  }
  else if (countryName==="South Sudan")
  {
    inputName = "S. Sudan";
  }
  return inputName
}

export function countryNameTransferForLocation(countryName)
{
  let inputName =countryName
  if(countryName==="US")
  {
    inputName = "United States of America";
  }
  else if(countryName==="Vietnam")
  {
    inputName = "Viet Nam";
  }
  else if(countryName==="Korea, South")
  {
    inputName = "Korea (South)";
  }
  else if (countryName==="Russia")
  {
    inputName = "Russian Federation";
  }
  return inputName
}
