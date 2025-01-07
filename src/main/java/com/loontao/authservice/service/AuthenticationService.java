package com.loontao.authservice.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loontao.authservice.dto.LoginUserDto;
import com.loontao.authservice.dto.RegisterUserDto;
import com.loontao.authservice.entity.Role;
import com.loontao.authservice.entity.RoleEnum;
import com.loontao.authservice.entity.User;
import com.loontao.authservice.repository.RoleRepository;
import com.loontao.authservice.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder,
        RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User signup(RegisterUserDto input) {
        
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty()) {
            return null;
        }
          
        User user = new User()
                .setFullname(input.getFullname())
                .setEmailId(input.getEmailId())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setAddress(input.getAddress())
                .setCity(input.getCity())
                .setCountry(input.getCountry())
                .setPhoneNumber(input.getPhoneNumber())
                .setPincode(input.getPincode())
                .setRole(optionalRole.get());

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmailId(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmailId(input.getEmailId())
                .orElseThrow();
    }
}

