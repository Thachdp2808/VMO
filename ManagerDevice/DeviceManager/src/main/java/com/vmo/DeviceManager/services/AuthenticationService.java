package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.jwt.JwtAuthenticationReponse;
import com.vmo.DeviceManager.jwt.RefreshTokenRequest;
import com.vmo.DeviceManager.jwt.SigninAuthen;
import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.Erole;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentService departmentService;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public User signup(AuthRequest authRequest){
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setFirstName(authRequest.getFirstName());
        user.setLastName(authRequest.getLastName());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        Department department = departmentService.findById(authRequest.getDepartmentId());
        user.setDepartment(department);
        user.setRole(Erole.USER);
        return userRepository.save(user);
    }

    public JwtAuthenticationReponse signin(SigninAuthen signinAuthen){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinAuthen.getEmail(),signinAuthen.getPassword()));
        var user = userRepository.findByEmail(signinAuthen.getEmail()).orElseThrow(() -> new IllegalArgumentException("In valid email or password"));
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

}
