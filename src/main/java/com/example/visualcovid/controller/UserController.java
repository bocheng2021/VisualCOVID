package com.example.visualcovid.controller;

import com.example.visualcovid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/test")
    public void test(){
        service.test();
    }
}
