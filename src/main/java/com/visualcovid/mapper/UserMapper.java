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

    @Insert("insert into vaccinedata values(#{CountryName},#{date},#{doses},#{partially_vaccinated},#{fully_vaccinated})")
    void insertVaccineData(String CountryName, Date date, String doses,
                           String partially_vaccinated,String fully_vaccinated);

    @Insert("insert into time_series_vaccine values(#{CountryName},#{date},#{doses},#{partially_vaccinated},#{fully_vaccinated},#{report_date})")
    void insertTimeLineVaccineData(String CountryName, Date date, String doses,
                           String partially_vaccinated,String fully_vaccinated,String report_date);

    @Select("select * from vaccinedata")
    VaccineData[] getSumVaccineData();

    @Select("select * from time_series_vaccine where Country_Region=#{countryName}")
    VaccineData[] getTimeSeriesVaccineDataByCountry(String countryName);


    @Select("select * from vaccinedata where Country_Region ='World'")
    VaccineData getWorldSumVaccineData();

}
