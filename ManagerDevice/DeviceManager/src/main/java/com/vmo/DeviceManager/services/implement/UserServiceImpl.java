package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.mapper.UserMapper;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.DepartmentService;
import com.vmo.DeviceManager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentService departmentService;

    public UserServiceImpl(PasswordEncoder passwordEncoder, DepartmentService departmentService) {
        this.passwordEncoder = passwordEncoder;
        this.departmentService = departmentService;
    }

    @Override
    public UserDto getUserById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exits"));
        return UserMapper.toUserDto(user) ;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> listUser = userRepository.findAll();
        List<UserDto> result = new ArrayList<>();
        for (User user : listUser){
            if(user.getFirstName().contains(keyword) || user.getLastName().contains(keyword)){
                result.add(UserMapper.toUserDto(user));
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void updateUserbyId(int id, AuthRequest authRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        existingUser.setDepartment(departmentService.findById(authRequest.getDepartmentId()));
        // Update existingUser with updatedUser's properties
        BeanUtils.copyProperties(authRequest, existingUser);
        userRepository.save(existingUser);

    }

}
