package com.loontao.authservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loontao.authservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Attempts to authenticate the user based on the request body.
     *
     * @param request  the HttpServletRequest containing the user credentials.
     * @param response the HttpServletResponse.
     * @return the Authentication object if authentication is successful.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            
            // Parse username and password from the request body
            Map<String, String> credentials = new ObjectMapper().readValue(
            request.getInputStream(),
            new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
            );
            String username = credentials.get("username");
            String password = credentials.get("password");

            // Create an authentication token and delegate authentication
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body", e);
        }
    }

    /**
     * Called when authentication is successful.
     *
     * @param request    the HttpServletRequest.
     * @param response   the HttpServletResponse.
     * @param chain      the FilterChain.
     * @param authResult the Authentication object.
     * @throws IOException if an input or output exception occurs.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String username = authResult.getName();

        // Generate JWT token
        String token = jwtUtil.generateTokenUserName(username);

        // Set token in response header and body
        response.setHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.getWriter().flush();
    }

    /**
     * Called when authentication fails.
     *
     * @param request   the HttpServletRequest.
     * @param response  the HttpServletResponse.
     * @param exception the exception that caused authentication to fail.
     * @throws IOException if an input or output exception occurs.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Invalid username or password\"}");
        response.getWriter().flush();
    }
}
