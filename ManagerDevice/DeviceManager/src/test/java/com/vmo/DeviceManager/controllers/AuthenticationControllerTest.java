package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.jwt.JwtAuthenticationReponse;
import com.vmo.DeviceManager.jwt.SigninAuthen;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.services.AuthenticationService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthenticationController authenticationController;


    @Test
    void verifyAccount() {
        String email = "example@example.com";
        String otp = "123456";

        // Mock the service method call
        Mockito.when(authenticationService.resetPassword(Mockito.anyString(), Mockito.anyString())).thenReturn("Verification successful");

        // Call the controller method
        ResponseEntity<String> responseEntity = authenticationController.verifyAccount(email, otp);

        // Verify that the service method was called with the correct arguments
        Mockito.verify(authenticationService).resetPassword(email, otp);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        String responseBody = responseEntity.getBody();
        Assert.assertEquals("Verification successful", responseBody);
    }

    @Test
    void regenerateOtp() {
        String email = "example@example.com";

        // Mock the service method call
        Mockito.when(authenticationService.regenerateOtp(Mockito.anyString())).thenReturn("OTP regenerated successfully");

        // Call the controller method
        ResponseEntity<String> responseEntity = authenticationController.regenerateOtp(email);

        // Verify that the service method was called with the correct argument
        Mockito.verify(authenticationService).regenerateOtp(email);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        String responseBody = responseEntity.getBody();
        Assert.assertEquals("OTP regenerated successfully", responseBody);
    }

    @Test
    void testSignin() {
        SigninAuthen signinAuthen = new SigninAuthen("example@example.com", "password");

        // Mock the service method call
        JwtAuthenticationReponse jwtAuthenticationReponse = new JwtAuthenticationReponse("token");
        Mockito.when(authenticationService.signin(signinAuthen)).thenReturn(jwtAuthenticationReponse);

        // Call the controller method
        ResponseEntity<JwtAuthenticationReponse> responseEntity = authenticationController.signin(signinAuthen);

        // Verify that the service method was called with the correct argument
        Mockito.verify(authenticationService).signin(signinAuthen);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        JwtAuthenticationReponse responseBody = responseEntity.getBody();
        Assert.assertEquals("token", responseBody.getToken());
    }




}