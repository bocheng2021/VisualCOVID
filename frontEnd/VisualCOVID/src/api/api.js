import axios from 'axios'


// 根据省份,城市名称去获取相关数据
//https://api.inews.qq.com/newsqa/v1/query/pubished/daily/list?province=湖北&city=武汉&
export function fetch_city(province, city) {
    return axios.get("http://192.168.0.105:8080/data/city", {
        params: {
            province: province,
            city: city
        },
    })
}

// 根据省份名称去获取相关数据
export function fetch_province(province) {
    return axios.get("http://192.168.0.105:8080/data/province", {
        params: {
            province: province
        },
    })
}


// 获取中国相关数据
// https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5&callback=jQuery34108520079424019378_1584427298434&_=1584427298435
export function fetch_china() {
    return axios.get("https://view.inews.qq.com/g2/getOnsInfo?name=disease_h5&callback=jQuery34108520079424019378_1584427298434&_=1584427298435")

}

// 获取全世界的数据
//https://view.inews.qq.com/g2/getOnsInfo?name=disease_foreign
export function fetch_world() {
    return axios.get("https://view.inews.qq.com/g2/getOnsInfo?name=disease_foreign")
}

// 获取世界名字(本地json)
export function fetch_worldName() {
    return axios.get("../country/country.json")
}
