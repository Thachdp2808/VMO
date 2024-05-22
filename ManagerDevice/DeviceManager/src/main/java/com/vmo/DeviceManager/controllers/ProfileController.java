package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getUser()  {
        return ResponseEntity.ok(userService.getUser());

    }
    @PutMapping("/users")
    public ResponseEntity<?> updateProfile(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(userService.updateProfile(authRequest));
    }
}
