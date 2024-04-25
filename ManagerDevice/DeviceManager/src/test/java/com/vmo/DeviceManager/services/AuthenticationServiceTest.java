package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.jwt.JwtAuthenticationReponse;
import com.vmo.DeviceManager.jwt.RefreshTokenRequest;
import com.vmo.DeviceManager.jwt.SigninAuthen;
import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.implement.DepartmentServiceImpl;
import com.vmo.DeviceManager.services.implement.UserServiceImpl;
import com.vmo.DeviceManager.utils.EmailUtil;
import com.vmo.DeviceManager.utils.OtpUtil;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    JwtService jwtService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    OtpUtil otpUtil;
    @Mock
    EmailUtil emailUtil;
    @InjectMocks
    DepartmentServiceImpl departmentService;
    @InjectMocks
    AuthenticationService userService;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    UserRepository userRepository;



    @Test
    void signup_ValidAuthRequest_ReturnsUser() throws MessagingException {
        // Given
        // Mock dependencies: otpUtil, emailUtil, passwordEncoder, departmentService, userRepository

        when(otpUtil.generateOtp()).thenReturn("123456"); // Mock generated OTP

        doNothing().when(emailUtil).sendOtpEmail(anyString(), anyString()); // Mock sending OTP email


        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword"); // Mock password encryption


        int departmentId = 1;
        Department department = Department.builder()
                .departmentId(departmentId)
                .departmentName("Example Department")
                .build();
        when(departmentService.findById(departmentId)).thenReturn(department);




        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("example@example.com");
        authRequest.setFirstName("John");
        authRequest.setLastName("Doe");
        authRequest.setPassword("password");
        authRequest.setDepartmentId(1); // Set department ID as needed


        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mock user repository method
        // When
        User result = userService.signup(authRequest);

        // Then
        assertNotNull(result);


    }


    @Test
    void verifyAccount_ValidEmailAndOTP_ReturnsSuccessMessage() {
        // Given
        String email = "example@example.com";
        String otp = "123456";
        User user = new User();
        user.setEmail(email);
        user.setOtp(otp);
        user.setOtpTime(LocalDateTime.now().minusSeconds(30)); // Set OTP time within validity period
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        String result = userService.verifyAccount(email, otp);

        // Then
        assertEquals("OTP verified you can login", result);
        assertEquals(EstatusUser.Active, user.getStatus());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void verifyAccount_InvalidOTP_ReturnsErrorMessage() {
        // Given
        String email = "example@example.com";
        String otp = "123456";
        User user = new User();
        user.setEmail(email);
        user.setOtp(otp);
        user.setOtpTime(LocalDateTime.now().minusSeconds(30)); // Set OTP time within validity period
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        String result = userService.verifyAccount(email, "654321");

        // Then
        assertEquals("Please regenerate otp and try again", result);

    }

    @Test
    void verifyAccount_ExpiredOTP_ReturnsErrorMessage() {
        // Given
        String email = "example@example.com";
        String otp = "123456";
        User user = new User();
        user.setEmail(email);
        user.setOtp(otp);
        user.setOtpTime(LocalDateTime.now().minusMinutes(2)); // Set OTP time outside validity period
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        String result = userService.verifyAccount(email, otp);

        // Then
        assertEquals("Please regenerate otp and try again", result);
    }

    @Test
    void verifyAccount_UserNotFound_ReturnsErrorMessage() {
        // Given
        String email = "example@example.com";
        String otp = "123456";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.verifyAccount(email, otp);
        });
        assertEquals("User not found with this email: " + email, exception.getMessage());
    }

    @Test
    void regenerateOtp_ValidEmail_ReturnsSuccessMessage() throws MessagingException {
        // Given
        String email = "example@example.com";
        String otp = "123456";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(otpUtil.generateOtp()).thenReturn(otp);
        doNothing().when(emailUtil).sendOtpEmail(email, otp);

        // When
        String result = userService.regenerateOtp(email);

        // Then
        assertEquals("Email sent... please check account within 1 minute", result);
        assertEquals(otp, user.getOtp());
    }

    @Test
    void regenerateOtp_UserNotFound_ThrowsRuntimeException() {
        // Given
        String email = "example@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.regenerateOtp(email));
        assertEquals("User not found with this email: " + email, exception.getMessage());
    }

    @Test
    void regenerateOtp_FailedToSendEmail_ThrowsRuntimeException() throws MessagingException {
        // Given
        String email = "example@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(otpUtil.generateOtp()).thenReturn("123456");
        doThrow(MessagingException.class).when(emailUtil).sendOtpEmail(email, "123456");

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.regenerateOtp(email));
        assertEquals("Unable to send otp please try again", exception.getMessage());
    }

    @Test
    void signin_ValidCredentials_ReturnsJwtAuthenticationResponse() {
        // Given
        String email = "example@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setStatus(EstatusUser.Active);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        String jwt = "jwt-token";
        String refreshToken = "refresh-token";
        when(jwtService.generateToken(user)).thenReturn(jwt);
        when(jwtService.generateRefreshToken(anyMap(), eq(user))).thenReturn(refreshToken);

        // When
        SigninAuthen signinAuthen = new SigninAuthen(email, password);
        JwtAuthenticationReponse response = userService.signin(signinAuthen);

        // Then
        assertEquals(jwt, response.getToken());
    }

    @Test
    void signin_UserDeactivated_ThrowsIllegalArgumentException() {
        // Given
        String email = "example@example.com";
        String password = "password";
        User user = new User();
        user.setUserId(1);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(EstatusUser.Deactive);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When / Then
        SigninAuthen signinAuthen = new SigninAuthen(email, password);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.signin(signinAuthen));
        assertEquals("User account is deactivated", exception.getMessage());
    }


    @Test
    void signin_InvalidCredentials_ThrowsIllegalArgumentException() {
        // Given
        String email = "example@example.com";
        String password = "password";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        SigninAuthen signinAuthen = new SigninAuthen(email, password);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.signin(signinAuthen));
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void refreshToken_ValidToken_ReturnsJwtAuthenticationResponse() {
        // Given
        String token = "validRefreshToken";
        String email = "example@example.com";
        User user = new User();
        user.setEmail(email);

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.validateToken(token, user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newToken");

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(token);

        // When
        JwtAuthenticationReponse result = userService.refreshToken(refreshTokenRequest);

        // Then
        assertNotNull(result);
        assertEquals("newToken", result.getToken());
        assertEquals(token, result.getRefreshToken());
    }

    @Test
    void changePassword_UserNotFound_ThrowsRuntimeException() {
        // Given
        String email = "example@example.com";
        String password = "newPassword";
        String otp = "123456";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.changePassword(email, password, otp));
        assertEquals("User not found with this email: " + email, exception.getMessage());
    }

    @Test
    void changePassword_InvalidOrExpiredOtp_ReturnsEmptyJwtAuthenticationResponse() {
        // Given
        String email = "example@example.com";
        String password = "newPassword";
        String otp = "123456";
        User user = new User();
        user.setOtp(otp);
        user.setOtpTime(LocalDateTime.now().minusMinutes(2)); // Set otpTime to 2 minutes ago

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        JwtAuthenticationReponse result = userService.changePassword(email, password, otp);

        // Then
        assertNotNull(result);
        assertNull(result.getToken());
    }

    @Test
    void changePassword_ValidOtp_ReturnsJwtAuthenticationResponseWithNewToken() {
        // Given
        String email = "example@example.com";
        String password = "newPassword";
        String otp = "123456";
        User user = new User();
        user.setOtp(otp);
        user.setOtpTime(LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("newToken");
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");

        // When
        JwtAuthenticationReponse result = userService.changePassword(email, password, otp);

        // Then
        assertNotNull(result);
        assertEquals("newToken", result.getToken());
    }

    @Test
    void resetPassword_UserNotFound_ThrowsRuntimeException() {
        // Given
        String email = "example@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.resetPassword(email));
        assertEquals("User not found with this email: " + email, exception.getMessage());
    }

    @Test
    void resetPassword_Success_ReturnsSuccessMessage() throws MessagingException {
        // Given
        String email = "example@example.com";
        String newPassword = "newPassword";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("hashedNewPassword");
        doNothing().when(emailUtil).sendPassEmail(email, newPassword);
        when(jwtService.generateToken(user)).thenReturn("newToken");

        // When
        String result = userService.resetPassword(email);

        // Then
        assertEquals("New password sent your email... please check account within 1 minute", result);

    }


    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Test
    void generateRandomPassword_ReturnsPasswordWithCorrectLength() {
        // Given
        int length = 10;

        // When
        String password = AuthenticationService.generateRandomPassword(length);

        // Then
        assertEquals(length, password.length());
    }

    @Test
    void generateRandomPassword_ReturnsPasswordWithValidCharacters() {
        // Given
        int length = 10;

        // When
        String password = AuthenticationService.generateRandomPassword(length);

        // Then
        assertFalse(password.matches("[a-zA-Z0-9]+"));
    }
}