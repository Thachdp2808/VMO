package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import com.vmo.DeviceManager.models.mapper.UserMapper;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.DepartmentService;
import com.vmo.DeviceManager.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
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
    public UserDto getUserById(int id) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getUserId() != id) {
            throw new AccessDeniedException("Access denied");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Account does not exits"));
        return UserMapper.toUserDto(user) ;
    }

    @Override
    public String deActiveUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Account does not exits"));
        user.setStatus(EstatusUser.Deactive);
        userRepository.save(user);
        return "Update successful";
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
    public String updateUserbyId(int id, AuthRequest authRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if (currentUser.getUserId() != id) {
            return "Access denied";
        }
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        authRequest.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        existingUser.setDepartment(departmentService.findById(authRequest.getDepartmentId()));
        try {
            // Update existingUser with updatedUser's properties
            BeanUtils.copyProperties(authRequest, existingUser);
        } catch (Exception e){
            throw new RuntimeException("Can not update your user please try again");
        }
        userRepository.save(existingUser);
        return "Update successful";
    }

}
