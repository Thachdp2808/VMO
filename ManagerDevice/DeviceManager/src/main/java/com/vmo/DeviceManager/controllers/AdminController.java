package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.dto.DeviceDto;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestService;
import com.vmo.DeviceManager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private UserService userService;
    private final RequestService requestService;
    private final DeviceService deviceService;

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
        List<Request> listRequest = requestService.getRequestAdmin();
        return listRequest.isEmpty() ? ResponseEntity.ok("Request does not found") : ResponseEntity.ok(listRequest);
    }
    @PostMapping("/addDevice")
    public ResponseEntity<?> addDevice(@RequestBody DeviceDto deviceDto){
        return ResponseEntity.ok(deviceService.addDevice(deviceDto));
    }

    @PostMapping("/updateDevice/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable int id, @RequestBody DeviceDto deviceDto){
       deviceService.updateDevice(id, deviceDto);
       return ResponseEntity.ok("Update success");
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> addRequest(@PathVariable int id){
        return ResponseEntity.ok(requestService.approveRequest(id));
    }

    @PostMapping("/deActive/{id}")
    public ResponseEntity<?> deActiveUser(@PathVariable int id){
        return ResponseEntity.ok(userService.deActiveUser(id));
    }

}
