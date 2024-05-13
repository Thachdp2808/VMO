package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.jwt.JwtAuthenticationReponse;
import com.vmo.DeviceManager.jwt.RefreshTokenRequest;
import com.vmo.DeviceManager.jwt.SigninAuthen;
import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.services.AuthenticationService;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final DeviceService deviceService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authenticationService.signup(authRequest));
    }

    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,
                                                @RequestParam String otp) {
        return new ResponseEntity<>(authenticationService.verifyAccount(email, otp), HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(authenticationService.regenerateOtp(email), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationReponse> signup(@RequestBody SigninAuthen signinAuthen){
        return ResponseEntity.ok(authenticationService.signin(signinAuthen));
    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam String email,
                                            @RequestParam String otp,@RequestBody  SigninAuthen signinAuthen){
        return ResponseEntity.ok(authenticationService.changePassword(email, signinAuthen.getPassword(), otp));
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<?> changePassword(@RequestParam String email){
        return ResponseEntity.ok(authenticationService.resetPassword(email));
    }
}
