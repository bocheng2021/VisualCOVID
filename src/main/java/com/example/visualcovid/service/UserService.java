package com.example.visualcovid.service;

import com.example.visualcovid.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserService {

    @Autowired
    private UserMapper mapper;

    @Transactional
    public void test()
    {
        mapper.insertOne();
    }
}
