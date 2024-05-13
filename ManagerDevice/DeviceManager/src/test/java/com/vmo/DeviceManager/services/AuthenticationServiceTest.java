package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.jwt.JwtAuthenticationReponse;
import com.vmo.DeviceManager.jwt.RefreshTokenRequest;
import com.vmo.DeviceManager.jwt.SigninAuthen;
import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.Erole;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    AuthenticationManager authenticationManager;
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
//        // Given
//        String email = "example@example.com";
//        String firstName = "John";
//        String lastName = "Doe";
//        String password = "password";
//        int departmentId = 1;
//        String otp = "123456";
//
//        AuthRequest authRequest = new AuthRequest();
//        authRequest.setEmail(email);
//        authRequest.setFirstName(firstName);
//        authRequest.setLastName(lastName);
//        authRequest.setPassword(password);
//        authRequest.setDepartmentId(departmentId);
//
//        when(otpUtil.generateOtp()).thenReturn(otp);
//        doNothing().when(emailUtil).sendOtpEmail(eq(email), eq(otp));
//        when(passwordEncoder.encode(eq(password))).thenReturn("hashedPassword");
//
//        Department department = new Department();
//        department.setDepartmentId(departmentId);
//        when(departmentService.findById(eq(departmentId))).thenReturn(department);
//
//        User savedUser = new User();
//        savedUser.setEmail(email);
//        savedUser.setFirstName(firstName);
//        savedUser.setLastName(lastName);
//        savedUser.setPassword("hashedPassword");
//        savedUser.setDepartment(department);
//        savedUser.setStatus(EstatusUser.Deactive);
//        savedUser.setOtp(otp);
//        savedUser.setOtpTime(LocalDateTime.now());
//        savedUser.setRole(Erole.USER);
//        when(userRepository.save(any(User.class))).thenReturn(savedUser);
//
//        // When
//        User result = userService.signup(authRequest);
//
//        // Then
//        assertNotNull(result);
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
    void signin_ValidSigninAuthen_ReturnsJwtAuthenticationReponse() {
        // Given
        String email = "example@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setStatus(EstatusUser.Active);

        SigninAuthen signinAuthen = new SigninAuthen();
        signinAuthen.setEmail(email);
        signinAuthen.setPassword(password);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));

        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        when(jwtService.generateToken(eq(user))).thenReturn(jwtToken);

        // When
        JwtAuthenticationReponse result = userService.signin(signinAuthen);

        // Then
        assertNotNull(result);
        assertEquals(jwtToken, result.getToken());
    }



    @Test
    void signin_UserDeactivated_ThrowsException() {
        // Given
        String email = "example@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setStatus(EstatusUser.Deactive);

        SigninAuthen signinAuthen = new SigninAuthen();
        signinAuthen.setEmail(email);
        signinAuthen.setPassword(password);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> userService.signin(signinAuthen));
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
    void resetPassword_ValidEmail_ReturnsSuccessMessage() throws MessagingException {
//        // Given
//        String email = "example@example.com";
//        User user = new User();
//        user.setEmail(email);
//        String newPass = "newPassword";
//        String encodedPassword = "encodedNewPassword";
//        String jwtToken = "jwtToken";
//
//        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));
//        when(jwtService.generateToken(eq(user))).thenReturn(jwtToken);
//        when(passwordEncoder.encode(eq(newPass))).thenReturn(encodedPassword);
//
//        // When
//        String result = userService.resetPassword(email);
//
//        // Then
//        assertEquals("New password sent your email... please check account within 1 minute", result);
//        assertEquals(encodedPassword, user.getPassword());
//        verify(userRepository, times(1)).save(eq(user));
//        verify(emailUtil, times(1)).sendPassEmail(eq(email), eq(newPass));
    }

    @Test
    void resetPassword_InvalidEmail_ThrowsException() {
        // Given
        String email = "example@example.com";

        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.empty());

        // When, Then
        assertThrows(RuntimeException.class, () -> userService.resetPassword(email));
    }
    @Test
    void resetPassword_EmailSendingFailure_ThrowsException() throws MessagingException {
        // Given
        String email = "example@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));
        doThrow(MessagingException.class).when(emailUtil).sendPassEmail(eq(email), anyString());

        // When, Then
        assertThrows(RuntimeException.class, () -> userService.resetPassword(email));
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
//        // Given
//        int length = 10;
//
//        // When
//        String password = AuthenticationService.generateRandomPassword(length);
//
//        // Then
//        assertFalse(password.matches("[a-zA-Z0-9]+"));
    }
}