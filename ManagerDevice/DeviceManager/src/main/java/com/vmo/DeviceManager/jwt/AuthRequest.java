package com.vmo.DeviceManager.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String departmentId;
    private String apartmentId;
}