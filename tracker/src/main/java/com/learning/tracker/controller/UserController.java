package com.learning.tracker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
public class UserController {

    @PostMapping("/register")
    public String register() {
        // Actions to register a user
        return "User registered successfully";
    }

    @PostMapping("/login")
    public String login(){
        // Actions to login a user
        return "User logged in successfully";
    }

    @GetMapping("/user-profile")
    public String getUserProfile() {
        // Actions to get user profile
        return "User profile details";
    }

}
