package com.proj.Management.service;

import com.proj.Management.models.User;
import com.proj.Management.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService interface.
 * Required to load user-specific data (like roles and password hash)
 * during both JWT validation and initial login process.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo  userRepository;

    public UserDetailsServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by their username (which is the email in this project)
     * and returns a Spring Security UserDetails object.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Fetch the user from the database using the email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2. Convert your custom User entity into a Spring Security UserDetails object
        // The password field must contain the HASHED password for security checks.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getUserRole()) // Pass the user's role (e.g., "USER", "ADMIN")
                .build();
    }
}
