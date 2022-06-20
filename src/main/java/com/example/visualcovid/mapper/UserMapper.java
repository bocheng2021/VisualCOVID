package com.example.visualcovid.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into test values(1,1,1,'1')")
    void insertOne();
}
