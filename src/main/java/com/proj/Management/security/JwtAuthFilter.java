package com.proj.Management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom filter that runs once per request to check for and validate a JWT token
 * in the Authorization header before the request hits the main controllers.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    // UserDetailsService is needed to load the user's details (password hash, roles)
    private final UserDetailsService userDetailsService;

    // Constructor Injection
    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Extract JWT from the 'Authorization: Bearer [token]' header
            String jwt = parseJwt(request);
            String username = null;

            // 2. Validate the token
            if (jwt != null && jwtUtil.validateToken(jwt)) {

                // Get username from token payload (e.g., alex@smarttask.com)
                username = jwtUtil.getUserNameFromJwtToken(jwt);

                // Check if the user is not already authenticated in the current context
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 3. Load user details (roles, encrypted password) using the email
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // 4. Create an authentication token
                    // The 'null' password is okay because we've already authenticated via the JWT signature.
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 5. Set authentication in Spring Security Context
                    // This tells Spring: "This user is logged in for the rest of this request."
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Log any failure to process the JWT, but continue the filter chain
            logger.error("Cannot set user authentication: {}", e);
        }

        // Proceed to the next filter in the chain (or the controller if this is the last one)
        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to extract the token string from the request header.
     * Looks for: Authorization: Bearer <token>
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Returns the token string after "Bearer "
        }
        return null;
    }
}
