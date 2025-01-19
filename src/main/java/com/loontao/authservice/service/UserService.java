package com.loontao.authservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.loontao.authservice.dto.RegisterUserDto;
import com.loontao.authservice.dto.WebhookPayload;
import com.loontao.authservice.entity.Role;
import com.loontao.authservice.entity.RoleEnum;
import com.loontao.authservice.entity.User;
import com.loontao.authservice.exceptions.UserNotFoundException;
import com.loontao.authservice.repository.RoleRepository;
import com.loontao.authservice.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    private RestTemplate restTemplate;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
    
    public User createAdministrator(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        if (optionalRole.isEmpty()) {
            return null;
        }

        var user = new User()
                .setFullname(input.getFullname())
                .setEmailId(input.getEmailId())
                .setPassword(passwordEncoder.encode(input.getPassword()))
                .setRole(optionalRole.get());

        return userRepository.save(user);
    }

    public User getCustomerFromPhone(String phoneNumber) {
        // Validate phone number input
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty.");
        }
    
        // Fetch user from the repository
        return userRepository.findByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new UserNotFoundException("User not found for phone number: " + phoneNumber));
    }

    public void triggerWebhook(String phoneNumber) {
        String webhookUrl = "https://intapos.com:1437/api/webhooks/loontao";

        // Payload for the webhook
        WebhookPayload payload = new WebhookPayload("new_customer", phoneNumber);

        // Send POST request to the webhook URL
        try {
            restTemplate.postForEntity(webhookUrl, payload, String.class);
            System.out.println("Webhook triggered successfully.");
        } catch (Exception e) {
            System.err.println("Failed to trigger webhook: " + e.getMessage());
        }
    }
}