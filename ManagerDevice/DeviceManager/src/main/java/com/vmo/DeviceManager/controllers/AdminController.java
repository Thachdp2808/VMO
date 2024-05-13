package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.dto.DeviceDto;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import com.vmo.DeviceManager.services.RequestService;
import com.vmo.DeviceManager.services.UserService;
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
    @Autowired
    private  DeviceService deviceService;

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi Admin");
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                        @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize){
        return ResponseEntity.ok(userService.pageAndSearch(keyword, pageNo, pageSize));
    }
    @GetMapping("/request")
    public ResponseEntity<?> viewRequest(){
        return ResponseEntity.ok(requestService.getRequestAdmin());
    }
    @PostMapping("/addDevice")
    public ResponseEntity<?> addDevice(@RequestBody DeviceDto deviceDto){
        return ResponseEntity.ok(deviceService.addDevice(deviceDto));
    }

    @PostMapping("/updateDevice/{id}")
    public ResponseEntity<?> updateDevice(@PathVariable int id, @RequestBody DeviceDto deviceDto){
       return ResponseEntity.ok(deviceService.updateDevice(id, deviceDto));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(){
        return ResponseEntity.ok(deviceService.getDashboard());
    }

    @GetMapping("/getDurationDay/{id}")
    public ResponseEntity<?> durationDay(@PathVariable int id){
        return ResponseEntity.ok(requestDetailService.getDurationDay(id));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable int id){
        return ResponseEntity.ok(requestService.approveRequest(id));
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<?> rejectRequest(@PathVariable int id){
        return ResponseEntity.ok(requestService.rejectRequest(id));
    }

    @PostMapping("/deActive/{id}")
    public ResponseEntity<?> deActiveUser(@PathVariable int id){
        return ResponseEntity.ok(userService.deActiveUser(id));
    }
    @PostMapping("/updateUser/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable int id, @RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(userService.updateUserById(id, authRequest));
    }

}
