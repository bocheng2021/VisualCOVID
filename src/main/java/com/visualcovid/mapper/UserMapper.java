package com.visualcovid.mapper;

import com.visualcovid.entity.CountryData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into countries values(#{name},#{slug},#{iso})")
    void insertCountriesData(String name, String slug, String iso);

    @Select("select * from countries where Name=#{name}")
    CountryData getCountrySlugByName(String name);

}
