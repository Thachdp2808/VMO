package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import com.vmo.DeviceManager.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {
    @InjectMocks
    private ProfileController profileController;
    @Mock
    private UserService userService;

    @Test
    void getUser() {
        UserDto user = new UserDto(); // Assuming this is the user returned by the service
        Mockito.when(userService.getUser()).thenReturn( user);

        // Call the controller method
        ResponseEntity<?> responseEntity = profileController.getUser();

        // Verify that the service method was called
        Mockito.verify(userService).getUser();

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateProfile() {
        Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();
        AuthRequest authRequest = new AuthRequest("newUserName", "newPassword","newEmail",0,"newFirstName","newLastName",1);

        // Create a mock User object for the updated user profile
        User updatedUser = User.builder()
                .userId(7)
                .email("thachdao53@gmail.com").firstName("dao").lastName("thach")
                .password("abcd")
                .phone(0).role(Erole.USER)
                .status(EstatusUser.Active)
                .department(department).build();

        // Mock the service method call
        Mockito.when(userService.updateProfile(authRequest)).thenReturn(String.valueOf(updatedUser));

        // Call the controller method
        ResponseEntity<?> responseEntity = profileController.updateProfile(authRequest);

        // Verify that the service method was called with the correct arguments
        Mockito.verify(userService).updateProfile(authRequest);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }
}