package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.dto.UserDto;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import com.vmo.DeviceManager.models.mapper.UserMapper;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.DepartmentService;
import com.vmo.DeviceManager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentService departmentService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentService = departmentService;
    }

    @Override
    public UserDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        User user = userRepository.findById(currentUser.getUserId()).orElseThrow(() -> new RuntimeException("Account does not exits"));
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
    public Page<User> pageAndSearch(String keyword, Integer pageNo, Integer pageSize) {
        if (pageNo == null || pageNo <= 0) {
            throw new IllegalArgumentException("Invalid page number");
        }
        if (pageSize == null || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page size");
        }

        List<User> listUser = userRepository.findAll();
        int size = 0;
        List<User> list = searchByKeyword(listUser,keyword );
        size = list.size();


        // Phân trang và trả về kết quả
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageSize), size);
        list = list.subList(start, end);
        return new PageImpl<>(list, pageable, size);
    }
    public List<User> searchByKeyword(List<User> listUser, String keyword) {
        Predicate<User> searchPredicate = user ->
                user.getFirstName().toUpperCase().contains(keyword.toUpperCase()) ||
                        user.getLastName().toUpperCase().contains(keyword.toUpperCase()) ||
                        user.getEmail().toUpperCase().contains(keyword.toUpperCase());

        return listUser.stream()
                .filter(searchPredicate)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String updateProfile( AuthRequest authRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        User existingUser = userRepository.findById(currentUser.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + currentUser.getUserId()));
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
    @Override
    public String logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        currentUser.setToken("");
        userRepository.save(currentUser);
        return "Logout successfully";
    }
    @Override
    public String updateUserById(int id, AuthRequest authRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole() == Erole.USER){
            return "Permission denied";
        }
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if(authRequest.getPassword() == null){
            authRequest.setPassword(existingUser.getPassword());
        }
        if(authRequest.getEmail() == null){
            authRequest.setEmail(existingUser.getEmail());
        }
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
