package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.jwt.JwtAuthenticationReponse;
import com.vmo.DeviceManager.jwt.RefreshTokenRequest;
import com.vmo.DeviceManager.jwt.SigninAuthen;
import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.utils.EmailUtil;
import com.vmo.DeviceManager.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentService departmentService;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public User signup(AuthRequest authRequest){
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(authRequest.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setFirstName(authRequest.getFirstName());
        user.setLastName(authRequest.getLastName());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        Department department = departmentService.findById(authRequest.getDepartmentId());
        user.setDepartment(department);
        user.setStatus(EstatusUser.Deactive);
        user.setOtp(otp);
        user.setOtpTime(LocalDateTime.now());
        user.setRole(Erole.USER);
        return userRepository.save(user);
    }
    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setStatus(EstatusUser.Active);
            userRepository.save(user);
            return "OTP verified you can login";
        }
        return "Please regenerate otp and try again";
    }
    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpTime(LocalDateTime.now());
        userRepository.save(user);
        return "Email sent... please check account within 1 minute";
    }

    public JwtAuthenticationReponse signin(SigninAuthen signinAuthen){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinAuthen.getEmail(),signinAuthen.getPassword()));
        var user = userRepository.findByEmail(signinAuthen.getEmail()).orElseThrow(() -> new IllegalArgumentException("In valid email or password"));
        if (user.getStatus() == EstatusUser.Deactive) {
            throw new IllegalArgumentException("User account is deactivated");
        }
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);
        JwtAuthenticationReponse jwtAuthenticationReponse = new JwtAuthenticationReponse();
        jwtAuthenticationReponse.setToken(jwt);
        jwtAuthenticationReponse.setRefreshToken(refreshToken);
        return jwtAuthenticationReponse;
    }

    public JwtAuthenticationReponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String email = jwtService.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(email).orElseThrow();
        if(jwtService.validateToken(refreshTokenRequest.getToken(),user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationReponse jwtAuthenticationReponse = new JwtAuthenticationReponse();

            jwtAuthenticationReponse.setToken(jwt);
            jwtAuthenticationReponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationReponse;
        }
        return null;
    }

    public JwtAuthenticationReponse changePassword(String email, String password, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        JwtAuthenticationReponse jwtAuthenticationReponse = new JwtAuthenticationReponse();
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setPassword(passwordEncoder.encode(password));
            var jwt = jwtService.generateToken(user);
            jwtAuthenticationReponse.setToken(jwt);
            userRepository.save(user);
        }
        return jwtAuthenticationReponse;
    }
}
