import axios from "axios";

export function nameProcess(name){
  let url = 'http://localhost:8081/getCountrySlug?name='+name
  return axios.get(url)
}

export async function countryProcess() {
  let countries = await axios.get('http://localhost:8081/getCountry')
  let i =0;
  let option = []
  for (i in countries.data)
  {
    option[i] = {value:countries.data[i].country,label:countries.data[i].country}
  }
  return option
}

export function messageBox1(that) {
  const h = that.$createElement;
  that.$notify({
    title: 'No data Error',
    message: h('i', { style: 'color: teal'}, 'There is no relevant data!')
  });
}

