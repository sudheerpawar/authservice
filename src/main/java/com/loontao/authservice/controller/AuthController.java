package com.loontao.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loontao.authservice.dto.LoginResponse;
import com.loontao.authservice.dto.LoginUserDto;
import com.loontao.authservice.dto.RegisterUserDto;
import com.loontao.authservice.entity.User;
import com.loontao.authservice.service.AuthenticationService;
import com.loontao.authservice.service.JwtService;
import com.loontao.authservice.service.UserService;


@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final UserService userService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        // Sign up the user
        User registeredUser;
        // user phone number is mandatory for sign up
        if (registerUserDto == null || registerUserDto.getPhoneNumber() == null || registerUserDto.getPhoneNumber().isEmpty()) {
            return ResponseEntity.badRequest().body("User details are required. Phone number is mandatory for sign up.");
        }
        try {
            registeredUser = authenticationService.signup(registerUserDto);
            // Check if registration is successful
            if (registeredUser != null && (registeredUser.getPhoneNumber() != null || !registeredUser.getPhoneNumber().isEmpty())) {
                // Trigger the webhook
                userService.triggerWebhook(registeredUser.getPhoneNumber());
                return ResponseEntity.ok(registeredUser);
            } else if (registeredUser == null)
            {
                return ResponseEntity.status(500).body("User phone number " + registerUserDto.getPhoneNumber() +  " already exists. Please Login or try with other phone number.");
            } else {
                return ResponseEntity.badRequest().body("User registration failed. Please try again.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during registration: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {

        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
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
