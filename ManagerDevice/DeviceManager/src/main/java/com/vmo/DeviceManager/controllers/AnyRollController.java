package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import com.vmo.DeviceManager.services.RequestService;
import com.vmo.DeviceManager.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AnyRollController {
    private final DeviceService deviceService;
    private final RequestService requestService;
    private final UserService userService;

    @GetMapping("/devices")
    public ResponseEntity<?> getDevice(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                       @RequestParam(name = "type", required = false) List<String> type,
                                       @RequestParam(name = "category", required = false) List<String> category,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize){
        return ResponseEntity.ok(deviceService.pageAndSearch( keyword,category, type, pageNo, pageSize));
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategory(){
        return ResponseEntity.ok(deviceService.getAllCategory());
    }


    @PostMapping("/requests")
    public ResponseEntity<?> addRequest(@RequestBody RequestDto requestDto){
        return ResponseEntity.ok(requestService.addRequest(requestDto));
    }

    @PostMapping("/send-request/{id}")
    public ResponseEntity<?> sendRequest(@PathVariable int id){
        return ResponseEntity.ok(requestService.sendRequest(id));
    }

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable int id){
        return ResponseEntity.ok(requestService.deleteRequest(id));
    }
    @PutMapping("/requests/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable int id, @RequestBody RequestDto requestDto){
        return ResponseEntity.ok(requestService.updateRequest(id, requestDto));
    }



    @GetMapping("/logout-account")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(userService.logout());
    }

    @GetMapping("/requests")
    public ResponseEntity<?> viewMyRequest(@RequestParam(name = "status", required = false) List<String> status,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize){
        return ResponseEntity.ok(requestService.pageAndSearchRequest(requestService.getRequestByCreatedUser(),status, pageNo, pageSize));
    }
}
