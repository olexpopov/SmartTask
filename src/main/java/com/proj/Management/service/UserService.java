package com.proj.Management.service;
import com.proj.Management.security.JwtUtil;
import com.proj.Management.models.User;
import com.proj.Management.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final JwtUtil jwtUtil;
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(JwtUtil jwtUtil, UserRepo userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Handles the core logic for user registration
    public User registerNewUser(User newUser) {

        // 1. Check for duplicate email (Business Rule)
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email address already in use.");
        }

        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        return userRepository.save(newUser);
    }
    public String loginUser(String email,String plaintextPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials.");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(plaintextPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials.");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return token;

    }
}
