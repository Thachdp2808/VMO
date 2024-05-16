package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.services.StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class StorageControllerTest {
    @Mock
    private StorageService service;
    @InjectMocks
    private StorageController storageController;

    @Test
    void uploadFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Mock file content".getBytes());

        // Set the type and id parameters
        String type = "device";
        int id = 123;

        // Mock the uploadFile method call in the service
        String expectedResponse = "File uploaded successfully";
        Mockito.when(service.uploadFile(file, type, id)).thenReturn(expectedResponse);

        // Call the controller method
        ResponseEntity<String> responseEntity = storageController.uploadFile(file, type, id);

        // Verify that the service method was called with the correct arguments
        Mockito.verify(service).uploadFile(file, type, id);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        Assertions.assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void downloadFile() {
        String fileName = "test.txt";

        // Mock the downloadFile method call in the service
        byte[] data = "Test file content".getBytes();
        Mockito.when(service.downloadFile(fileName)).thenReturn(data);

        // Call the controller method
        ResponseEntity<ByteArrayResource> responseEntity = storageController.downloadFile(fileName);

        // Verify that the service method was called with the correct file name
        Mockito.verify(service).downloadFile(fileName);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the content type
        String expectedContentType = "application/octet-stream";
        Assertions.assertEquals(expectedContentType, responseEntity.getHeaders().getContentType().toString());

        // Verify the content length
        Assertions.assertEquals(data.length, responseEntity.getHeaders().getContentLength());

        // Verify the content disposition header
        String expectedContentDisposition = "inline; filename=\"" + fileName + "\"";
        Assertions.assertEquals(expectedContentDisposition, responseEntity.getHeaders().get("Content-disposition").get(0));

        // Verify the response body
        byte[] responseBody = responseEntity.getBody().getByteArray();
        Assertions.assertArrayEquals(data, responseBody);
    }

    @Test
    void deleteFile() {
        String fileName = "test.txt";

        // Mock the deleteFile method call in the service
        Mockito.when(service.deleteFile(fileName)).thenReturn("File deleted successfully");

        // Call the controller method
        ResponseEntity<String> responseEntity = storageController.deleteFile(fileName);

        // Verify that the service method was called with the correct file name
        Mockito.verify(service).deleteFile(fileName);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        String responseBody = responseEntity.getBody();
        Assertions.assertEquals("File deleted successfully", responseBody);
    }
}