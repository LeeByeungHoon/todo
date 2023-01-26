package com.example.todo.userapi.controller;


import com.example.todo.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserApi {
    @Autowired
    private UserService userService;
}
