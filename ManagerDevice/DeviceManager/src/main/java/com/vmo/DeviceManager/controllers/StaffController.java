package com.vmo.DeviceManager.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StaffController {
    @GetMapping("/user")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi User");
    }
}
