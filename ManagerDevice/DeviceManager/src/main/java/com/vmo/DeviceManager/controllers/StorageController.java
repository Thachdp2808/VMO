package com.vmo.DeviceManager.controllers;


import com.vmo.DeviceManager.services.StorageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
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

    @GetMapping("/show/{fileName}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        // Xác định kiểu MIME cho tệp dựa trên phần mở rộng của tên tệp
        String contentType = determineContentType(fileName);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", contentType)
                .header("Content-disposition", "inline; filename=\"" + fileName + "\"") // Thay đổi "attachment" thành "inline"
                .body(resource);
    }

    private String determineContentType(String fileName) {
        if (fileName.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.toLowerCase().endsWith(".bmp")) {
            return "image/bmp";
        } else {
            // Mặc định là application/octet-stream cho các kiểu tệp không xác định
            return "application/octet-stream";
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
    }
}
