package com.vmo.DeviceManager.jwt;

import com.vmo.DeviceManager.exceptions.model.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigninAuthen {
    private String email;
    private String password;


}