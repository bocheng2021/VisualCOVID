package com.visualcovid.controller;

import com.visualcovid.entity.CountryData;
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

}
