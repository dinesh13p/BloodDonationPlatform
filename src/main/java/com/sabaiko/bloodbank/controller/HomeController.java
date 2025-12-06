package com.sabaiko.bloodbank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        // Always redirect to login page
        // After successful login, Spring Security will redirect to appropriate dashboard
        return "redirect:/auth/login";
    }
}


