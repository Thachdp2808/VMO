package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Apartment;
import com.vmo.DeviceManager.models.AuthRequest;
import com.vmo.DeviceManager.models.Erole;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signup(AuthRequest authRequest){
        User user = new User();
        user.setEmail(authRequest.getEmail());
        user.setFirstName(authRequest.getFirstName());
        user.setLastName(authRequest.getLastName());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setApartment(new Apartment("address","Du18"));
        user.setRole(Erole.ROLE_STAFF);
        return userRepository.save(user);
    }

}
