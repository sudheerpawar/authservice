package com.loontao.authservice.config;

import com.loontao.authservice.security.JwtAuthenticationFilter;
import com.loontao.authservice.security.JwtAuthorizationFilter;
import com.loontao.authservice.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless JWT-based authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/generate-signup-token", "/api/auth/login").permitAll() // Allow public access
                .anyRequest().authenticated() // Secure all other endpoints
            )
            .addFilter(new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtUtil))
            .addFilterBefore(new JwtAuthorizationFilter(jwtUtil), JwtAuthenticationFilter.class); // Ensure proper order

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
