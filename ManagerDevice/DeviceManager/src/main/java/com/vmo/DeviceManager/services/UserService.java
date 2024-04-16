package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService  {
     UserDto getUserById(int id);

     void deActiveUser(int userId);

     List<UserDto> searchUser(String keyword);

     void updateUserbyId(int id, AuthRequest authRequest);

}