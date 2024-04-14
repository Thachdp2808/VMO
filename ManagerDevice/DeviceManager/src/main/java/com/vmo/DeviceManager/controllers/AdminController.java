package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.services.RequestService;
import com.vmo.DeviceManager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private UserService userService;
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi Admin");
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam(name = "keyword", required = false, defaultValue = "") String name){
        List<UserDto> users = userService.searchUser(name);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/request")
    public ResponseEntity<?> viewRequest(){
        List<Request> listRequest = requestService.getAllByStatus();
        return listRequest.isEmpty() ? ResponseEntity.ok("Request does not found") : ResponseEntity.ok(listRequest);
    }

}
