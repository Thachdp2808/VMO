package com.vmo.DeviceManager.models.mapper;

import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.UserDto;
import lombok.*;


public class UserMapper {
    // Tái sử dụng code
    public static UserDto toUserDto(User user){
        UserDto tmp = new UserDto();
        tmp.setUserId(user.getUserId());
        tmp.setFirstName(user.getFirstName());
        tmp.setLastName(user.getLastName());
        tmp.setPhone(user.getPhone());
        tmp.setEmail(user.getEmail());
        tmp.setAddress(user.getAddress());
        tmp.setJobTitle(user.getJobTitle());
        tmp.setRole(user.getRole());
        tmp.setDepartment(user.getDepartment());
        return tmp;
    }
}
