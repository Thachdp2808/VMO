package com.vmo.DeviceManager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/login")
    public String testController(){
        return "login";
    }
}
