package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/device")
public class DeviceController {
    //RestController trả về đối tượng hoặc file JSON/XML
    //Controller trả về view (Trang HTML)
    @Autowired
    DeviceRepository deviceRepository;
    @GetMapping("")
    List<Device> getAllDevice(){
       return deviceRepository.findAll();
    }

//    @GetMapping("")
//    ResponseEntity<ReponseObject> getDeviceById(@PathVariable int id){
//        Optional<Device> foundDevice = deviceRepository.findById(id);
//        return foundDevice.ifPresent() ?
//                ResponseEntity.status(HttpStatus.OK).body(
//                        new ReponseObject("OK","Query device successfully",foundDevice)
//                        );
//                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                        new ReponseObject("OK","Cannot find device with id = " + id,"")
//                );
//    }
}
