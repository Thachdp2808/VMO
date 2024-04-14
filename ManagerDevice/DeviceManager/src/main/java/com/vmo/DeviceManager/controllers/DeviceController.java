package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusRequest;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import com.vmo.DeviceManager.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;
    private final RequestService requestService;
    private final RequestDetailService requestDetailService;

    @GetMapping("/device")
    public ResponseEntity<?> getDevice(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                       @RequestParam(name = "type", required = false, defaultValue = "name") String type,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo){
        List<Device> devices = deviceService.getAllDevice();
        Page<Device> listDevice = deviceService.pageAndSearch(devices, keyword, type, pageNo);
        return listDevice.isEmpty() ? ResponseEntity.ok("Device does not exist") : ResponseEntity.ok(listDevice.getContent());
    }

    @PostMapping("/addRequest")
    public ResponseEntity<?> addRequest(@RequestBody RequestDto requestDto){
        Request request = new Request();
        request.setReason(requestDto.getReason());
        requestService.addRequest(request);

        requestDetailService.saveRequestDetail(request,requestDto);

        return ResponseEntity.ok("Add request success");
    }


}
