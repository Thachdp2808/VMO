package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.models.dto.DeviceDto;
import com.vmo.DeviceManager.services.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class AdminControllerTest {
    @InjectMocks
    private DeviceService deviceService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    AdminControllerTest(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Test
    void sayHello() {
    }

    @Test
    void searchUser() {
    }

    @Test
    void viewRequest() {
    }

    @Test
    void addDevice() {
        DeviceDto deviceDto = new DeviceDto();
        // Thay đổi behavior của deviceService.addDevice để trả về ResponseEntity.ok()
        when(deviceService.addDevice(deviceDto)).thenReturn(String.valueOf(ResponseEntity.ok().build()));
        // Act
        ResponseEntity<?> responseEntity = adminController.addDevice(deviceDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(deviceService, times(1)).addDevice(deviceDto);
    }

    @Test
    void updateDevice() {
    }

    @Test
    void dashboard() {
    }

    @Test
    void durationDay() {
    }

    @Test
    void approveRequest() {
    }

    @Test
    void rejectRequest() {
    }

    @Test
    void deActiveUser() {
    }

    @Test
    void updateProfile() {
    }
}