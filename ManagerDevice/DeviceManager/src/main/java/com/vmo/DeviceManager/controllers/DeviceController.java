package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.services.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping("")
    public ResponseEntity<?> getDevice(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                       @RequestParam(name = "type", required = false, defaultValue = "name") String type,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo){
        List<Device> devices = deviceService.getAllDevice();
        Page<Device> listDevice = deviceService.pageAndSearch(devices, keyword, type, pageNo);
        return listDevice.isEmpty() ? ResponseEntity.ok("Device does not exist") : ResponseEntity.ok(listDevice.getContent());
    }

//    @GetMapping("/search")
//    public ResponseEntity<?> searchDevices(
//            ,
//            ) {
//
//        List<Device> devices;
//        // Truyền vào string return về List
//        Function<String, List<Device>> searchFunction =
//                type.equals("name") ? deviceService::searchByName :
//                type.equals("category") ? key -> deviceService.searchByCategory(Integer.parseInt(key)) :
//                type.equals("type") ? deviceService::searchByType : null;
//
//        if (searchFunction != null) {
//            List<Device> listDevice = deviceRepository.findAll();
//            devices = searchFunction.apply(listDevice,keyword);
//            return ResponseEntity.ok(devices.isEmpty() ? "Device does not exist" : devices);
//        } else {
//            return ResponseEntity.badRequest().body("Invalid search type");
//        }
//    }


}
