package com.visualcovid.controller;

import com.visualcovid.entity.CountryData;
import com.visualcovid.entity.VaccineData;
import com.visualcovid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/getCountrySlugByDataBase")
    public CountryData getCountrySlug(String name){
        return service.getCountrySlug(name);
    }

    @GetMapping("/getCountrySlug")
    public CountryData getCountrySlugByName(String name){
        List<CountryData> list = service.getCountryData();
        CountryData country = null;
        for (CountryData u: list) {
            if (u.getCountry().equals(name)){
                country = u;
                break;
            }
        }
        return country;
    }

    @GetMapping("/getCountry")
    public List<CountryData> getCountry(){
        return service.getCountryData();
    }

    @GetMapping("/insertVaccineData")
    public void insertVaccineData(){
        service.insertVaccineData();
    }

    @GetMapping("/insertTimelineVaccineData")
    public void insertTimelineVaccineData(){
        service.insertTimeLineVaccineData();
    }

    @GetMapping("/getSumVaccineData")
    public VaccineData[] getSumVaccineData()
    {
        return service.getSumVaccineData();
    }

    @GetMapping("/getWorldSumVaccineData")
    public VaccineData getWorldSumVaccineData()
    {
        return service.getWorldSumVaccineData();
    }

    @GetMapping("/getTimeSeriesVaccineByCountry")
    public VaccineData[] getTimeSeriesVaccineByCountry(String countryName)
    {
        return service.getTimeSeriesVaccineDataByCountry(countryName);
    }

    /*
    most data inserted on 2022/7/25
    *
    */
    @GetMapping("/insert_reliabilityData")
    public void insert_reliability(String name, String slug, double freq_1,double freq_2,double freq_3,double freq_4,
                                   double freq_5,double freq_6,double freq_7,double freq_8,double freq_9)
    {
        service.insert_reliability(name,slug,freq_1,freq_2,freq_3,freq_4,freq_5,freq_6,freq_7,freq_8,freq_9);
    }
}
