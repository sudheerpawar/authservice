package com.loontao.authservice.security;

import com.loontao.authservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to authorize incoming requests based on a JWT token.
 * Ensures the user is authenticated for protected endpoints.
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Filters incoming requests to authorize based on a JWT token.
     * @param request the HttpServletRequest.
     * @param response the HttpServletResponse.
     * @param filterChain the FilterChain.
     * @throws IOException if an input or output exception occurs.
     * @throws ServletException if a servlet exception occurs.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException, ServletException {
        // Extract the Authorization header
        String header = request.getHeader("Authorization");

        // Check if the Authorization header is valid and starts with "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Remove "Bearer " prefix

            try {
                // Validate the JWT token
                if (jwtUtil.validateToken(token)) {
                    // Extract the username (or other claims) from the token
                    String username = jwtUtil.extractClaim(token);

                    // Create an authentication object and set it in the SecurityContext
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, null);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                // Handle invalid token scenario (optional logging or response modification)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
                response.getWriter().flush();
                return;
            }
        }

        // Continue the filter chain for the next filters
        filterChain.doFilter(request, response);
    }
}
