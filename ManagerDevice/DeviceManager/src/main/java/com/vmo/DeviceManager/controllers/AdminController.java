package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.DeviceDto;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.services.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")

public class AdminController {

    private final UserService userService;
    private final RequestService requestService;
    private final RequestDetailService requestDetailService;
    private final DeviceService deviceService;
    private final DepartmentService departmentService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi Admin");
    }
    @GetMapping("/search-user")
    public ResponseEntity<?> searchUser(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize){
        return ResponseEntity.ok(userService.pageAndSearch(keyword, pageNo, pageSize));
    }
    @GetMapping("/requests")
    public ResponseEntity<?> viewRequestAdmin(@RequestParam(name = "status", required = false) List<String> status,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize){
        return ResponseEntity.ok(requestService.pageAndSearchRequest(requestService.getRequestAdmin(),status, pageNo, pageSize));
    }
    @PostMapping("/devices")
    public ResponseEntity<?> addDevice(@RequestBody DeviceDto deviceDto){
        return ResponseEntity.ok(deviceService.addDevice(deviceDto));
    }
    @GetMapping("/departments")
    public ResponseEntity<?> viewDepartment(){
        return ResponseEntity.ok(departmentService.findAll());
    }

    @PutMapping("/devices/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable int id, @RequestBody DeviceDto deviceDto){
       return ResponseEntity.ok(deviceService.updateDevice(id, deviceDto));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(){
        return ResponseEntity.ok(deviceService.getDashboard());
    }

    @GetMapping("/duration-day")
    public ResponseEntity<?> durationDay(){
        return ResponseEntity.ok(requestDetailService.getDurationDay());
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable int id){
        return ResponseEntity.ok(requestService.approveRequest(id));
    }
    @GetMapping("/upload-file-dashboard")
    public ResponseEntity<?> uploadFileDashboard(){
        return ResponseEntity.ok(requestDetailService.uploadFileDashboard());
    }


    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectRequest(@PathVariable int id){
        return ResponseEntity.ok(requestService.rejectRequest(id));
    }

    @PostMapping("/return-device/{id}")
    public ResponseEntity<?> returnDevice(@PathVariable int id){
        return ResponseEntity.ok(requestService.returnDevice(id));
    }

    @PostMapping("/de-active/{id}")
    public ResponseEntity<?> deActiveUser(@PathVariable int id){
        return ResponseEntity.ok(userService.deActiveUser(id));
    }
    @PutMapping("/update-users/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable int id, @RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(userService.updateUserById(id, authRequest));
    }
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authenticationService.saveUser(authRequest));
    }

}
