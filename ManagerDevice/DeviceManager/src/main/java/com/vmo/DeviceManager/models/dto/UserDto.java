package com.vmo.DeviceManager.models.dto;

import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.Erole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private int userId;
    private String firstName;
    private String lastName;
    private int phone;
    private String email;
    private String address;
    private String jobTitle;
    private Erole role;
    private Department department;

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", role=" + role +
                '}';
    }
}
