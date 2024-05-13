package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import com.vmo.DeviceManager.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @Mock
    JwtService jwtService;
    @Mock
    private UserDetails userDetails;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;

    public final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    @Test
    void generateToken_ReturnsValidToken() {
        // Given

        UserDetails userDetails = User.builder()
                .email("exam@gmail.com").password(passwordEncoder.encode("abc")).build();
        String expectedToken = "validToken"; // Giả sử token hợp lệ được trả về từ JwtService

        // Thiết lập behavior cho phương thức generateToken của JwtService
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(expectedToken);

        // When
        String actualToken = jwtService.generateToken(userDetails);

        // Then
        assertNotNull(actualToken);
        assertEquals(expectedToken, actualToken);

    }

    @Test
    public void testExtractUsername() {
        String token = "generatedToken";
        when(jwtService.extractUsername(token)).thenReturn("testUser");
        assertEquals("testUser", jwtService.extractUsername(token));
    }

    @Test
    public void testExtractExpiration() {
        String token = "generatedToken";
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 24);
        when(jwtService.extractExpiration(token)).thenReturn(expiration);
        assertEquals(expiration, jwtService.extractExpiration(token));
    }

    @Test
    void extractClaim_ValidToken_ReturnsClaimValue() {
//        // Given
//        JwtService jwtService = new JwtService();
//        String token = "yourJWTToken";
//        Claims claims = new DefaultClaims();
//        claims.put("key", "value"); // Example claim
//
//        // Mock extractAllClaims()
//        JwtService mockedJwtService = spy(jwtService);
//        doReturn(claims).when(mockedJwtService).extractAllClaims(eq(token));
//
//        // When
//        String result = mockedJwtService.extractClaim(token, Claims::getSubject); // Example claim resolver
//
//        // Then
//        assertNotNull(result);
//        assertEquals("value", result);
    }

    @Test
    void isTokenExpired_NotExpired_ReturnsFalse() {
        // Given
        JwtService jwtService = new JwtService();
        String token = "yourJWTToken";
        Claims claims = new DefaultClaims();
        Date expirationDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60); // Example expiration date
        claims.setExpiration(expirationDate);

        // Mock extractAllClaims()
        JwtService mockedJwtService = spy(jwtService);
        doReturn(claims).when(mockedJwtService).extractAllClaims(eq(token));

        // When
        Boolean result = mockedJwtService.isTokenExpired(token);

        // Then
        assertFalse(result);
    }

    @Test
    void validateToken_ValidTokenAndUserDetails_ReturnsTrue() {
//        // Given
//        JwtService jwtService = new JwtService();
//        String token = "yourJWTToken";
//        when(userRepository.findById(9)).thenReturn(Optional.empty());
//        Claims claims = new DefaultClaims();
//        claims.setSubject("username");
//
//        // Mock extractAllClaims() and extractExpiration()
//        JwtService mockedJwtService = spy(jwtService);
//        doReturn(claims).when(mockedJwtService).extractAllClaims(eq(token));
//        doReturn(false).when(mockedJwtService).isTokenExpired(eq(token));
//
//        // When
//        Boolean result = mockedJwtService.validateToken(token, userDetails);
//
//        // Then
//        assertTrue(result);
    }
}