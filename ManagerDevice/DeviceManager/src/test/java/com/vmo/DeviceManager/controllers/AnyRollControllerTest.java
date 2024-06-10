package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.*;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestService;
import com.vmo.DeviceManager.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnyRollControllerTest {
    @Mock
    private DeviceService deviceService;
    @Mock
    private RequestService requestService;
    @Mock
    private UserService userService;
    @InjectMocks
    private AnyRollController anyRollController;

    @Test
    void getDevice() {
        String keyword = "keyword";
        List<String> type = Arrays.asList("type1", "type2");
        List<String> category = Arrays.asList("category1", "category2");
        List<String> status = Arrays.asList("status1", "status2");
        int pageNo = 1;
        int pageSize = 4;
        Page<Device> page = new PageImpl<>(Collections.emptyList());

        // Mock the service method call
        when(deviceService.pageAndSearch(keyword,status,  category, type, pageNo, pageSize)).thenReturn(page);

        // Call the controller method
        ResponseEntity<?> responseEntity = anyRollController.getDevice(keyword,status, type, category, pageNo, pageSize);
        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getCategory() {
        List<Category> categories = Arrays.asList(
                new Category(),
                new Category()
        );

        // Mock the service method call
        Mockito.when(deviceService.getAllCategory()).thenReturn(categories);

        // Call the controller method
        ResponseEntity<?> responseEntity = anyRollController.getCategory();

        // Verify that the service method was called
        Mockito.verify(deviceService).getAllCategory();

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify that the response body contains the expected categories
        List<Category> responseBody = (List<Category>) responseEntity.getBody();
        Assert.assertEquals(categories.size(), responseBody.size());
        Assert.assertEquals(categories, responseBody);
    }

    @Test
    void addRequest() {
        Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();

        // Create a mock User object for the updated user profile
        User user = User.builder()
                .userId(1)
                .email("thachdao53@gmail.com").firstName("dao").lastName("thach")
                .password("abcd")
                .phone(0).role(Erole.USER)
                .status(EstatusUser.Active)
                .department(department).build();
        RequestDto requestDto = new RequestDto();
        Request request = Request.builder().userCreated(user).build();

        // Mock the service method call
        Mockito.when(requestService.addRequest(requestDto)).thenReturn(String.valueOf(request));

        // Call the controller method
        ResponseEntity<?> responseEntity = anyRollController.addRequest(requestDto);

        // Verify that the service method was called
        Mockito.verify(requestService).addRequest(requestDto);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify that the response body contains the added request
        String responseBody = (String) responseEntity.getBody();
        Assert.assertEquals(request.toString(), responseBody);
    }

    @Test
    void sendRequest() {
        int requestId = 1;

        // Mock the service method call
        Mockito.when(requestService.sendRequest(requestId)).thenReturn(String.valueOf(requestId));

        // Call the controller method
        ResponseEntity<?> responseEntity = anyRollController.sendRequest(requestId);

        // Verify that the service method was called with the correct parameter
        Mockito.verify(requestService).sendRequest(requestId);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void deleteRequest() {
        int requestId = 1;

        // Mock the service method call
        Mockito.when(requestService.deleteRequest(requestId)).thenReturn(String.valueOf(requestId));

        // Call the controller method
        ResponseEntity<?> responseEntity = anyRollController.deleteRequest(requestId);

        // Verify that the service method was called with the correct parameter
        Mockito.verify(requestService).deleteRequest(requestId);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify any other assertions based on the expected result
    }

    @Test
    void updateRequest() {
        int requestId = 1;
        RequestDto requestDto = new RequestDto(); // Populate with appropriate data

        // Mock the service method call
        Mockito.when(requestService.updateRequest(requestId, requestDto)).thenReturn(String.valueOf(requestDto));

        // Call the controller method
        ResponseEntity<?> responseEntity = anyRollController.updateRequest(requestId, requestDto);

        // Verify that the service method was called with the correct parameters
        Mockito.verify(requestService).updateRequest(requestId, requestDto);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void logout() {
        String repo = "Logout successfully";
        Mockito.when(userService.logout()).thenReturn(repo);

        // Call the controller method
        ResponseEntity<?> responseEntity = anyRollController.logout();

        // Verify that the service method was called
        Mockito.verify(userService).logout();

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


}