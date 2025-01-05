package com.loontao.authservice.controller;

import com.loontao.authservice.dto.LoginRequest;
import com.loontao.authservice.dto.SignupRequest;
import com.loontao.authservice.service.UserService;
import com.loontao.authservice.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request,
                                     @RequestHeader(value = "Authorization", required = true) String token) {
        // Validate the token
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        if (!jwtUtil.validateToken(token)) {
            System.out.println("Invalid or expired token : " + token);
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        // Optionally, extract and verify the claim (e.g., ensure the token is for signup)
        String claim = jwtUtil.extractClaim(token);
        if (!"loontao".equals(claim)) {
            System.out.println("Token is not authorized for this operation : " + token);
            return ResponseEntity.status(403).body("Token is not authorized for this operation");
        }

        // Proceed with user registration
        String response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-signup-token")
    public ResponseEntity<?> generateSignupToken() {
    String token = jwtUtil.generateToken();
    return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
    
        // Extract username from the authentication object
        String authenticatedUsername = authentication.getName();
    
        // Generate a JWT token
        String token = jwtUtil.generateTokenUserName(authenticatedUsername);
    
        // Return the token in the response
        return ResponseEntity.ok("Bearer " + token);
    }
}


