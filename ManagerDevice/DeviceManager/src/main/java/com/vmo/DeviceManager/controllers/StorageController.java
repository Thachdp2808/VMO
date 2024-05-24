package com.vmo.DeviceManager.controllers;


import com.vmo.DeviceManager.services.StorageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
@SecurityRequirement(name = "bearerAuth")
public class StorageController {
    private final StorageService service;

    public StorageController(StorageService service) {
        this.service = service;
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                             @RequestParam(name = "type", required = false, defaultValue = "device") String type,
                                             @PathVariable int id) {
        return new ResponseEntity<>(service.uploadFile(file, type, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }
}
