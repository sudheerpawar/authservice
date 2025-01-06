package com.loontao.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.loontao.authservice.dto.SignupRequest;
import com.loontao.authservice.model.User;
import com.loontao.authservice.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createUser(SignupRequest request) {
        // Check if email or userId already exists
        if (userRepository.findByEmailId(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user entity
        User user = new User();
        user.setEmailId(request.getEmail());
        user.setUsername(request.getUserName());
        user.setPhoneNumber(request.getPhone());
        user.setAddress(request.getAddress());
        user.setPincode(request.getPincode());

        // Hash the password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save the user
        userRepository.save(user);

        return "User created successfully";
    }
}