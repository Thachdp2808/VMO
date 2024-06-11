package com.vmo.DeviceManager.models.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserDto {
    private String username;
    @Email(message = "Invalid Email")
    private String email;
    private int phone;
    private String firstName;
    private String lastName;
    private int departmentId;
    public void validateAndTrim() {
        if (username != null) {
            username = username.trim();
        }
        if (email != null) {
            email = email.trim();
        }
        if (firstName != null) {
            firstName = firstName.trim();
        }
        if (lastName != null) {
            lastName = lastName.trim();
        }
        // Không cần trim departmentId vì nó là một số nguyên
    }
}
