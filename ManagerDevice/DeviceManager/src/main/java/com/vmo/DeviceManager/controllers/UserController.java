package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final DeviceService deviceService;

    @GetMapping("")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hi User");
    }
    @GetMapping("/mydevice")
    public ResponseEntity<?> getDevice(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                       @RequestParam(name = "type", required = false, defaultValue = "name") String type,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo){
        List<Device> devices = deviceService.getAllDevice();
        Page<Device> listDevice = deviceService.pageAndSearch(devices, keyword, type, pageNo);
        return listDevice.isEmpty() ? ResponseEntity.ok("Device does not exist") : ResponseEntity.ok(listDevice.getContent());
    }

}
