package com.loontao.authservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loontao.authservice.entity.User;
import com.loontao.authservice.service.UserService;

@RequestMapping("/users")
@RestController
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/allUsers")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/getUserFromPhone")
    public ResponseEntity<?> getUserFromPhone(@RequestParam String phoneNumber) {

        // Validate the phone number
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body("Phone number is required and cannot be empty.");
        }

        // Fetch user from service
        User user = userService.getCustomerFromPhone(phoneNumber);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found for phone number: " + phoneNumber);
        } else {
            return ResponseEntity.ok(user);
        }
    }
    
}