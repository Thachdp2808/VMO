package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    public void testGenerateRefreshToken() {
        Map<String, Object> extractClaims = new HashMap<>();
        extractClaims.put("claim1", "value1");
        extractClaims.put("claim2", "value2");

        when(userDetails.getUsername()).thenReturn("testUser");
        String refreshToken = jwtService.generateRefreshToken(extractClaims, userDetails);
        assertNotNull(refreshToken);
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
    public void testExtractClaim() {
        String token = "generatedToken";
        when(jwtService.extractClaim(token, claims -> claims.get("testClaim"))).thenReturn("testClaimValue");
        assertEquals("testClaimValue", jwtService.extractClaim(token, claims -> claims.get("testClaim")));
    }

    @Test
    public void testIsTokenExpired() {
        String token = "generatedToken";
        Date expiration = new Date(System.currentTimeMillis() - 1000); // Expired token
        System.out.println("Expiration Date: " + expiration);
        when(jwtService.extractExpiration(token)).thenReturn(expiration);
        boolean result = jwtService.isTokenExpired(token);
        System.out.println("Is Token Expired: " + result);
        assertEquals(true, result);
    }


    @Test
    public void testValidateToken() {
        String token = "generatedToken";
        UserDetails userDetails = User.builder()
                .email("exam@gmail.com").password(passwordEncoder.encode("abc")).build();
        when(jwtService.extractUsername(token)).thenReturn("testUser");
        when(jwtService.isTokenExpired(token)).thenReturn(false);
        assertEquals(true, jwtService.validateToken(token, userDetails));
    }
}