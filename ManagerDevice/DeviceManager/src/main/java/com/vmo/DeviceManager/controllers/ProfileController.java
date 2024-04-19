package com.vmo.DeviceManager.controllers;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.services.UserService;
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
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserbyId(@PathVariable int id) throws AccessDeniedException {
        return ResponseEntity.ok(userService.getUserById(id));

    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateUserbyId(@PathVariable int id,@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(userService.updateUserbyId(id,authRequest));
    }
}
