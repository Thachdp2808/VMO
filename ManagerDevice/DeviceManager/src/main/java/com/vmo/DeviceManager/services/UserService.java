package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public interface UserService  {
     UserDto getUserById(int id) throws AccessDeniedException;

     String deActiveUser(int userId);

     Page<User> pageAndSearch( String keyword, Integer pageNo, Integer pageSize);

     String updateUserbyId(int id, AuthRequest authRequest);

}