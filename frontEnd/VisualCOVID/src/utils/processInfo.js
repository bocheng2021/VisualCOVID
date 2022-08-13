import axios from "axios";

export async function processInfo(){
  let res = await axios.get('https://api.covid19api.com/summary')
  return res.data.Global
}
