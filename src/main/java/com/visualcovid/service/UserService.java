package com.visualcovid.service;
import com.visualcovid.entity.VaccineData;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.springframework.core.io.Resource;
import com.alibaba.fastjson.JSONObject;
import com.visualcovid.entity.CountryData;
import com.visualcovid.mapper.UserMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.*;
import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Transactional
    public VaccineData[] getSumVaccineData()
    {
        return mapper.getSumVaccineData();
    }

    @Transactional
    public VaccineData getWorldSumVaccineData()
    {
        return mapper.getWorldSumVaccineData();
    }

    @Transactional
    public VaccineData[] getTimeSeriesVaccineDataByCountry(String countryName)
    {
        return mapper.getTimeSeriesVaccineDataByCountry(countryName);
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
    @Transactional
    public void insertTimeLineVaccineData()
    {
        File file = new File("D:\\Coding\\java_projects\\VisualCOVID\\src\\main\\" +
                "resources\\time_series_covid19_vaccine_global.csv");
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        CsvContainer csv = null;
        try {
            csv = csvReader.read(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pre = "";
        for (CsvRow row : csv.getRows()) {
            if (pre.equals(""))
            {
                pre=row.getField("Country_Region");
            }
            else if (!pre.equals(row.getField("Country_Region")) &&
                    (Objects.equals(row.getField("Province_State"), "")||
                            row.getField("Province_State").equals(row.getField("Country_Region"))))
            {
                String Country_Region = row.getField("Country_Region");
                String Date = row.getField("Date");
                String Doses_admin = row.getField("Doses_admin").equals("")? "0":row.getField("Doses_admin");
                String People_partially_vaccinated = row.getField("People_partially_vaccinated").equals("")? "0":
                        row.getField("People_partially_vaccinated");
                String People_fully_vaccinated = row.getField("People_fully_vaccinated").equals("")? "0":
                        row.getField("People_fully_vaccinated");
                String report_date = row.getField("People_fully_vaccinated").equals("")? "0":
                        row.getField("People_fully_vaccinated");
                mapper.insertTimeLineVaccineData(Country_Region, java.sql.Date.valueOf(Date),Doses_admin,
                        People_partially_vaccinated,People_fully_vaccinated,report_date);
                pre = Country_Region;
            }
        }
    }

    @Transactional
    public void insertVaccineData ()
    {
        File file = new File("D:\\Coding\\java_projects\\VisualCOVID\\src\\main\\" +
                "resources\\vaccine_data_global.csv");
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        CsvContainer csv = null;
        try {
            csv = csvReader.read(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pre = "";
        for (CsvRow row : csv.getRows()) {
            if (pre.equals(""))
            {
                pre=row.getField("Country_Region");
            }
            else if (!pre.equals(row.getField("Country_Region")))
            {
                String Country_Region = row.getField("Country_Region");
                String Date = row.getField("Date");
                String Doses_admin = row.getField("Doses_admin").equals("")? "0":row.getField("Doses_admin");
                String People_partially_vaccinated = row.getField("People_partially_vaccinated").equals("")? "0":
                        row.getField("People_partially_vaccinated");
                String People_fully_vaccinated = row.getField("People_fully_vaccinated").equals("")? "0":
                        row.getField("People_fully_vaccinated");
                mapper.insertVaccineData(Country_Region, java.sql.Date.valueOf(Date),Doses_admin,
                        People_partially_vaccinated,People_fully_vaccinated);
                pre = Country_Region;
            }
        }
    }

    public void insert_reliability(String name, String slug, double freq_1, double freq_2, double freq_3, double freq_4
            , double freq_5, double freq_6, double freq_7, double freq_8, double freq_9) {
        mapper.insert_reliability(name,slug,freq_1,freq_2,freq_3,freq_4,freq_5,freq_6,freq_7,freq_8,freq_9);
    }
}
