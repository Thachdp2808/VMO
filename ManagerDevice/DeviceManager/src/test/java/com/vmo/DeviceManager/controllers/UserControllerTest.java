package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private DeviceService deviceService;
    @Mock
    private RequestService requestService;
    @InjectMocks
    private UserController userController;


    @Test
    void sayHello() {
        ResponseEntity<String> responseEntity = userController.sayHello();

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        String responseBody = responseEntity.getBody();
        Assertions.assertEquals("Hi User", responseBody);
    }

    @Test
    void getMyDevice() {
        // Mock a list of devices that the service should return
        List<Device> devices = new ArrayList<>();
        // Add some devices to the list

        // Mock the service method call
        Mockito.when(deviceService.getMyDevice()).thenReturn(devices);

        // Call the controller method
        ResponseEntity<?> responseEntity = userController.getMyDevice();

        // Verify that the service method was called
        Mockito.verify(deviceService).getMyDevice();

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        List<Device> responseBody = (List<Device>) responseEntity.getBody();
        Assertions.assertEquals(devices.size(), responseBody.size());
    }


}