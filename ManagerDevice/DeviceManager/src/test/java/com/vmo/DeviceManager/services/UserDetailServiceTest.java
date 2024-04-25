package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.implement.DepartmentServiceImpl;
import com.vmo.DeviceManager.services.implement.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private DepartmentServiceImpl departmentService;
//    @Test
//    void userDetailsService_ReturnsCorrectUserDetailsService() {
//        // Given
//        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder, departmentService);
//
//        // When
//        UserDetailsService userDetailsService = userService.userDetailsService();
//
//        // Then
//        assertThat(userDetailsService).isNotNull();
//    }
//
//    @Test
//    void loadUserByUsername_UserExists_ReturnsUserDetails() {
//        // Given
//        String username = "test@example.com";
//        User user = User.builder()
//                .userId(1)
//                .email(username)
//                .password("password")
//                .build();
//        when(userRepository.findByEmail(username)).thenReturn(Optional.ofNullable(user));
//
//        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder, departmentService);
//        UserDetailsService userDetailsService = userService.userDetailsService();
//
//        // When
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//        // Then
//        assertThat(userDetails).isNotNull();
//        assertThat(userDetails.getUsername()).isEqualTo(username);
//        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
//    }
//
//    @Test
//    void loadUserByUsername_UserNotExists_ThrowsUsernameNotFoundException() {
//        // Given
//        String username = "nonexistent@example.com";
//        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
//
//        UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder, departmentService);
//        UserDetailsService userDetailsService = userService.userDetailsService();
//
//        // When, Then
//        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
//                .isInstanceOf(UsernameNotFoundException.class)
//                .hasMessageContaining("User not found");
//    }

}