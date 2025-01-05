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
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.findByUserId(request.getUserName()).isPresent()) {
            throw new RuntimeException("UserID already exists");
        }

        // Create new user entity
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUserId(request.getUserName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setPincode(request.getPincode());

        // Hash the password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save the user
        userRepository.save(user);

        return "User created successfully";
    }
}