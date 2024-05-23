package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.DeviceDto;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestDetailService;
import com.vmo.DeviceManager.services.RequestService;
import com.vmo.DeviceManager.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {
    @Mock
    private DeviceService deviceService;
    @Mock
    private RequestService requestService;
    @Mock
    private RequestDetailService requestDetailService;

    @InjectMocks
    private AdminController adminController;
    @Mock
    private UserService userService;

    @Test
    void sayHello() {
        ResponseEntity<String> response = adminController.sayHello();

        // Assert the response
        assertEquals("Hi Admin", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void searchUser() {
        int pageNo = 1;
        int pageSize = 5;
        List<User> userList = Arrays.asList(/* Populate user list */);
        int totalElements = userList.size();
        Page<User> page = new PageImpl<>(userList, PageRequest.of(pageNo - 1, pageSize), totalElements);
        when(userService.pageAndSearch(anyString(), anyInt(), anyInt()))
                .thenReturn(page);

        // Call the method
        ResponseEntity<?> response = adminController.searchUser("test", 1, 5);

        // Assert the response
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void viewRequest() {
//        List<Request> dummyRequests = new ArrayList<>();
//        dummyRequests.add(new Request());
//        dummyRequests.add(new Request());
//        // Add more dummy requests as needed
//
//        // Mock the getRequestAdmin() method to return the dummy requests
//        when(requestService.getRequestAdmin()).thenReturn(dummyRequests);
//
//        // Call the viewRequest() method
//        ResponseEntity<?> responseEntity = adminController.viewRequest();
//
//        // Assert that the response entity is not null
//        assertNotNull(responseEntity);
//
//        // Assert that the status code is OK (200)
//        assertEquals(200, responseEntity.getStatusCodeValue());
//
//        // Assert that the body of the response entity contains the dummy requests
//        List<Request> responseBody = (List<Request>) responseEntity.getBody();
//        assertNotNull(responseBody);
//        assertEquals(dummyRequests.size(), responseBody.size());
        // You can further assert the contents of the responseBody if needed
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
        int deviceId = 1; // Sample device ID
        DeviceDto deviceDto = new DeviceDto(/* Add constructor arguments here */);
        // Add sample data to the deviceDto as needed

        // Prepare expected response
        Device updatedDevice = new Device(/* Add constructor arguments here */);
        // Populate updatedDevice with the expected values after update

        // Mock the updateDevice() method to return the updated device
        when(deviceService.updateDevice(deviceId, deviceDto)).thenReturn(String.valueOf(updatedDevice));

        // Call the updateDevice() method
        ResponseEntity<?> responseEntity = adminController.updateDevice(deviceId, deviceDto);

        // Assert that the response entity is not null
        assertNotNull(responseEntity);

        // Assert that the status code is OK (200)
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void dashboard() {
        String expectedDashboardData = "Sample dashboard data"; // Replace this with your expected dashboard data

        // Mock the getDashboard() method to return the expected dashboard data
        when(deviceService.getDashboard()).thenReturn(expectedDashboardData);

        // Call the dashboard() method
        ResponseEntity<?> responseEntity = adminController.dashboard();

        // Assert that the response entity is not null
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Assert that the body of the response entity contains the expected dashboard data
        String responseBody = (String) responseEntity.getBody();
        assertEquals(expectedDashboardData, responseBody);
    }

    @Test
    void durationDay() {
        int requestId = 123; // Replace this with the ID of the request for which you want to get the duration

        // Mock the getDurationDay() method to return the expected duration
        String expectedDuration = "5"; // Replace this with your expected duration
        when(requestDetailService.getDurationDay()).thenReturn(expectedDuration);

        // Call the durationDay() method
        ResponseEntity<?> responseEntity = adminController.durationDay();

        // Assert that the response entity is not null
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Assert that the body of the response entity contains the expected duration
        String responseBody = (String) responseEntity.getBody();
        assertEquals(expectedDuration, responseBody);
    }

    @Test
    void approveRequest() {
        int requestId = 123; // Replace this with the ID of the request to be approved
        String expectedMessage = "Request approved successfully"; // Replace this with your expected message

        // Mock the approveRequest() method to return the expected message
        when(requestService.approveRequest(requestId)).thenReturn(expectedMessage);

        // Call the approveRequest() method
        ResponseEntity<?> responseEntity = adminController.approveRequest(requestId);

        // Assert that the response entity is not null
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Assert that the body of the response entity contains the expected message
        String responseBody = (String) responseEntity.getBody();
        assertEquals(expectedMessage, responseBody);
    }

    @Test
    void rejectRequest() {
        int requestId = 123; // Replace this with the ID of the request to be rejected
        String expectedMessage = "Request rejected successfully"; // Replace this with your expected message

        // Mock the rejectRequest() method to return the expected message
        when(requestService.rejectRequest(requestId)).thenReturn(expectedMessage);

        // Call the rejectRequest() method
        ResponseEntity<?> responseEntity = adminController.rejectRequest(requestId);

        // Assert that the response entity is not null
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Assert that the body of the response entity contains the expected message
        String responseBody = (String) responseEntity.getBody();
        assertEquals(expectedMessage, responseBody);
    }

    @Test
    void deActiveUser() {
        int userId = 123; // Replace this with the ID of the user to be deactivated
        String expectedMessage = "User deactivated successfully"; // Replace this with your expected message

        // Mock the deActiveUser() method to return the expected message
        when(userService.deActiveUser(userId)).thenReturn(expectedMessage);

        // Call the deActiveUser() method
        ResponseEntity<?> responseEntity = adminController.deActiveUser(userId);

        // Assert that the response entity is not null
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Assert that the body of the response entity contains the expected message
        String responseBody = (String) responseEntity.getBody();
        assertEquals(expectedMessage, responseBody);
    }

    @Test
    void updateProfile() {
        int userId = 123; // Replace this with the ID of the user to be updated
        AuthRequest authRequest = new AuthRequest(); // Create an AuthRequest object with the required data
        // Replace this with your expected updated user object
        AuthRequest expectedUpdatedUser = new AuthRequest("UpdatedName","Password", "UpdatedEmail",0,"UpdateFirstName", "UpdateLastName", 1);

        // Mock the updateUserById() method to return the expected updated user
        when(userService.updateUserById(userId, authRequest)).thenReturn(String.valueOf(expectedUpdatedUser));

        // Call the updateProfile() method
        ResponseEntity<?> responseEntity = adminController.updateProfile(userId, authRequest);

        // Assert that the response entity is not null
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}