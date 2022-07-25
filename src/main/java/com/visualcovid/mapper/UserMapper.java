package com.visualcovid.mapper;

import com.visualcovid.entity.CountryData;
import com.visualcovid.entity.VaccineData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;

@Mapper
public interface UserMapper {
    @Insert("insert into countries values(#{name},#{slug},#{iso})")
    void insertCountriesData(String name, String slug, String iso);

    @Select("select * from countries where Name=#{name}")
    CountryData getCountrySlugByName(String name);

    @Insert("insert into reliability values(#{name},#{slug},0,0,0,0,#{freq_1},#{freq_2},#{freq_3},#{freq_4},#{freq_5}," +
            "#{freq_6},#{freq_7},#{freq_8},#{freq_9})")
    void insert_reliability(String name, String slug, double freq_1,double freq_2,double freq_3,double freq_4,
                            double freq_5,double freq_6,double freq_7,double freq_8,double freq_9);

    @Insert("insert into vaccinedata values(#{CountryName},#{date},#{doses},#{partially_vaccinated},#{fully_vaccinated})")
    void insertVaccineData(String CountryName, Date date, String doses,
                           String partially_vaccinated,String fully_vaccinated);

    @Insert("insert into time_series_vaccine values(#{CountryName},#{date},#{doses},#{partially_vaccinated},#{fully_vaccinated},#{report_date})")
    void insertTimeLineVaccineData(String CountryName, Date date, String doses,
                           String partially_vaccinated,String fully_vaccinated,String report_date);

    @Select("select * from vaccinedata")
    VaccineData[] getSumVaccineData();

    @Select("select * from time_series_vaccine where Country_Region=#{countryName} and \n" +
            "Date >= '2021-02-15'")
    VaccineData[] getTimeSeriesVaccineDataByCountry(String countryName);


    @Select("select * from vaccinedata where Country_Region ='World'")
    VaccineData getWorldSumVaccineData();

}
