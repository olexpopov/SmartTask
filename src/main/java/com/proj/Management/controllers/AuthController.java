package com.proj.Management.controllers;

import com.proj.Management.models.User;
import com.proj.Management.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Data Transfer Object (DTO) for the incoming login request payload (JSON)
// Records are concise and ideal for DTOs in modern Java.
record LoginRequest(String email, String password) {}

@RestController
@RequestMapping("/api/auth") // Base path for authentication endpoints
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint 1: POST /api/auth/register (Existing endpoint)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User registrationRequest) {
        try {
            User registeredUser = userService.registerNewUser(registrationRequest);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed due to an internal error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Endpoint 2: POST /api/auth/login (The NEW endpoint)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Service handles authentication and returns the token string
            String token = userService.loginUser(loginRequest.email(), loginRequest.password());

            // Return the token to the client in a JSON structure
            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
        } catch (IllegalArgumentException e) {
            // Returns 401 Unauthorized status if credentials fail
            return new ResponseEntity<>("Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("Login failed due to an internal error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
