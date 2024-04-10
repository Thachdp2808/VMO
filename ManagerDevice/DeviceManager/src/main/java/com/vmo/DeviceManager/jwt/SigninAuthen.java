package com.vmo.DeviceManager.jwt;

import lombok.Data;

@Data
public class SigninAuthen {
    private String email;
    private String password;
}