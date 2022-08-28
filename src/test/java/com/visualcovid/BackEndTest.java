package com.visualcovid;

import com.visualcovid.controller.UserController;
import com.visualcovid.entity.CountryData;
import com.visualcovid.entity.ReliabilityData;
import com.visualcovid.entity.VaccineData;
import com.visualcovid.service.DataProcessService;
import com.visualcovid.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = VisualCovidApplication.class)
public class BackEndTest {

    @Autowired
    private UserController userController;

    @Test
    public void testGetCountry() {
        List<CountryData> list = userController.getCountry();
        System.out.println(list);
    }

    @Test
    public void testGetCountrySlugByName() {
        CountryData list = userController.getCountrySlugByName("United Kingdom");
        System.out.println(list);
    }

    @Test
    public void testGetSumVaccineData() {
        VaccineData[] list = userController.getSumVaccineData();
        System.out.println(Arrays.toString(list));
    }

    @Test
    public void testGetWorldSumVaccineData() {
        VaccineData list = userController.getWorldSumVaccineData();
        System.out.println(list);
    }

    @Test
    public void testGetTimeSeriesVaccineByCountry() {
        VaccineData[] list = userController.getTimeSeriesVaccineByCountry("China");
        System.out.println(Arrays.toString(list));
    }

    @Test
    public void testGetReliabilityDataByCountry() {
        ReliabilityData list = userController.getReliabilityDataByCountry("United Kingdom");
        System.out.println(list);
    }

}