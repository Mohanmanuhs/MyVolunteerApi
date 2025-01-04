package com.example.MyVolunteer_api.controller;

import com.example.MyVolunteer_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;




}
