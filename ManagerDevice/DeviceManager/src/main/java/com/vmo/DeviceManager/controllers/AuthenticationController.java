package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.jwt.JwtAuthenticationReponse;
import com.vmo.DeviceManager.jwt.SigninAuthen;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authenticationService.signup(authRequest));
    }

    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,
                                                @RequestParam String otp) {
        return ResponseEntity.ok(authenticationService.verifyAccount(email, otp));
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(authenticationService.regenerateOtp(email), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationReponse> signin(@RequestBody SigninAuthen signinAuthen){
        return ResponseEntity.ok(authenticationService.signin(signinAuthen));
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String email,
                                            @RequestParam String otp,@RequestBody SigninAuthen signinAuthen){
        return ResponseEntity.ok(authenticationService.changePassword(email, signinAuthen.getPassword(), otp));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email){
        return ResponseEntity.ok(authenticationService.resetPassword(email));
    }
}
