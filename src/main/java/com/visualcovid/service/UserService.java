package com.visualcovid.service;
import org.springframework.core.io.Resource;
import com.alibaba.fastjson.JSONObject;
import com.visualcovid.entity.CountryData;
import com.visualcovid.mapper.UserMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.*;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {
    public static List<CountryData> countryList = new ArrayList<>();
    @Autowired
    private UserMapper mapper;

    @Transactional
    public CountryData getCountrySlug(String name)
    {
        return mapper.getCountrySlugByName(name);
    }

    @Value("classpath:countries.json")
    private Resource countriesResource;

    @PostConstruct
    public List<CountryData> getCountryData(){
        try {
            String json = IOUtils.toString(countriesResource.getInputStream(), StandardCharsets.UTF_8);
            countryList = JSONObject.parseArray(json, CountryData.class);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return countryList;

    }
}
