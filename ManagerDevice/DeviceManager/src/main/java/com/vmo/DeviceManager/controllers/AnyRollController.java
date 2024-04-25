package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import com.vmo.DeviceManager.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AnyRollController {
    private final DeviceService deviceService;
    private final RequestService requestService;
    private final RequestDetailService requestDetailService;

    @GetMapping("/device")
    public ResponseEntity<?> getDevice(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                       @RequestParam(name = "type", required = false, defaultValue = "name") String type,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize){
        List<Device> devices = deviceService.getAllDevice();
        return ResponseEntity.ok(deviceService.pageAndSearch(devices, keyword, type, pageNo, pageSize));
    }

    @PostMapping("/addRequest")
    public ResponseEntity<?> addRequest(@RequestBody RequestDto requestDto){
        return ResponseEntity.ok(requestService.addRequest(requestDto));
    }

    @PostMapping("/sendRequest/{id}")
    public ResponseEntity<?> sendRequest(@PathVariable int id){
        return ResponseEntity.ok(requestService.sendRequest(id));
    }
    @PostMapping("/deleteRequest/{id}")
    public ResponseEntity<?> viewMyRequest(@PathVariable int id){
        boolean check = requestService.deleteRequest(id);
        System.out.println(check);
        return check? ResponseEntity.ok("Delete request success") : ResponseEntity.ok("Delete request fail");
    }
    @PostMapping("/updateRequest/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable int id, @RequestBody RequestDto requestDto){
        requestService.updateRequest(id, requestDto);
        return ResponseEntity.ok("Delete request success");
    }

}
