package com.vmo.DeviceManager.filter;

import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.JwtService;
import com.vmo.DeviceManager.services.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailService userDetailService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;



    @Test
    void doFilterInternal_ValidToken() throws ServletException, IOException {
//        // Given
//        String token = "valid_token";
//        String userEmail = "test@example.com";
//
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//        when(jwtService.extractUsername(token)).thenReturn(userEmail);
//
//        UserDetails userDetails = mock(UserDetails.class);
//        User user = new User();
//        user.setToken("dummyToken");
//
//        when(userDetailService.userDetailsService()).thenReturn(new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                return userDetails;
//            }
//        });
//
//        when(userRepository.findByEmail(userEmail)).thenReturn(java.util.Optional.of(user));
//
//        when(jwtService.validateToken(token, userDetails)).thenReturn(true);
//
//        // When
//        jwtAuthFilter.doFilterInternal(request, response, filterChain);
//
//        // Then
//        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidToken() throws ServletException, IOException {
//        String token = "invalid_token";
//
//        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
//        when(jwtService.extractUsername(token)).thenReturn(null);
//
//        // When
//        jwtAuthFilter.doFilterInternal(request, response, filterChain);
//
//        // Then
//        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
//        verifyNoInteractions(filterChain);
    }
}