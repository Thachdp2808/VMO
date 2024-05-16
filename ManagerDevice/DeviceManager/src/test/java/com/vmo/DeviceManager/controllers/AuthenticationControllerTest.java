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
    void signup() {
        AuthRequest authRequest = new AuthRequest();
        User mockUser = new User();

        // Mock the service method call
        Mockito.when(authenticationService.signup(Mockito.any(AuthRequest.class))).thenReturn(mockUser);

        // Call the controller method
        ResponseEntity<User> responseEntity = authenticationController.signup(authRequest);

        // Verify that the service method was called with the correct argument
        Mockito.verify(authenticationService).signup(authRequest);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        User returnedUser = responseEntity.getBody();
        Assert.assertNotNull(returnedUser);
    }

    @Test
    void verifyAccount() {
        String email = "example@example.com";
        String otp = "123456";

        // Mock the service method call
        Mockito.when(authenticationService.verifyAccount(Mockito.anyString(), Mockito.anyString())).thenReturn("Verification successful");

        // Call the controller method
        ResponseEntity<String> responseEntity = authenticationController.verifyAccount(email, otp);

        // Verify that the service method was called with the correct arguments
        Mockito.verify(authenticationService).verifyAccount(email, otp);

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

    @Test
    void changePassword() {
        String email = "example@example.com";
        String otp = "123456";
        SigninAuthen signinAuthen = new SigninAuthen("newEmail","newPassword");
        JwtAuthenticationReponse jwtAuthenticationReponse = new JwtAuthenticationReponse();

        // Mock the service method call
        Mockito.when(authenticationService.changePassword(email, signinAuthen.getPassword(), otp)).thenReturn(jwtAuthenticationReponse);

        // Call the controller method
        ResponseEntity<?> responseEntity = authenticationController.changePassword(email, otp, signinAuthen);

        // Verify that the service method was called with the correct arguments
        Mockito.verify(authenticationService).changePassword(email, signinAuthen.getPassword(), otp);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        JwtAuthenticationReponse responseBody = (JwtAuthenticationReponse) responseEntity.getBody();
        Assert.assertEquals(jwtAuthenticationReponse, responseBody);
    }

    @Test
    void testResetPassword() {
        String email = "example@example.com";
        String newPassword = "newPassword";
        String resetPasswordResponse = "Password reset successful"; // Assuming this is the response from the service

        // Mock the service method call
        Mockito.when(authenticationService.resetPassword(email)).thenReturn(resetPasswordResponse);

        // Call the controller method
        ResponseEntity<?> responseEntity = authenticationController.resetPassword(email);

        // Verify that the service method was called with the correct argument
        Mockito.verify(authenticationService).resetPassword(email);

        // Verify that the controller returns a ResponseEntity with status code 200 OK
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        String responseBody = (String) responseEntity.getBody();
        Assert.assertEquals(resetPasswordResponse, responseBody);
    }
}