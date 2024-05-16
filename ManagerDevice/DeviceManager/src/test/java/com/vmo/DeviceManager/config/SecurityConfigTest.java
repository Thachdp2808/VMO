package com.vmo.DeviceManager.config;

import com.vmo.DeviceManager.filter.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {
    @InjectMocks
    SecurityConfig securityConfig;


    @Test
    void securityFilterChain() {
//        // Mock the HttpSecurity object
//        HttpSecurity httpSecurity = mock(HttpSecurity.class);
//        when(httpSecurity.csrf()).thenReturn(httpConfigurer);
//        when(httpSecurity.authorizeHttpRequests()).thenReturn(httpConfigurer);
//        when(httpSecurity.sessionManagement()).thenReturn(httpConfigurer);
//
//        // Call the securityFilterChain method
//        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(httpSecurity);
//
//        // Verify that the appropriate configurations are applied
//        verify(httpSecurity).csrf(AbstractHttpConfigurer::disable);
//        verify(httpSecurity).authorizeHttpRequests();
//        verify(httpSecurity).sessionManagement();
//        verify(httpConfigurer).sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        verify(httpSecurity).authenticationProvider(any(AuthenticationProvider.class));
//        verify(httpSecurity).addFilterBefore(any(JwtAuthFilter.class), eq(UsernamePasswordAuthenticationFilter.class));
    }

    @Test
    void passwordEncoder() {
    }

    @Test
    void authenticationProvider() {
    }

    @Test
    void authenticationManager() {
    }
}