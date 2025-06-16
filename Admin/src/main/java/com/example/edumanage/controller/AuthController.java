package com.example.edumanage.controller;

import com.example.edumanage.model.Admin;
import com.example.edumanage.model.User;
import com.example.edumanage.service.AdminService;
import com.example.edumanage.service.CustomUserDetailsService;
import com.example.edumanage.service.UserService;
import com.example.edumanage.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private com.example.edumanage.repository.UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            User user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not found"));
            }

            String jwt = jwtUtil.generateToken(username, user.getRole(), user.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("role", user.getRole());
            response.put("id", user.getId());
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during login"));
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> adminDetails) {
        String username = adminDetails.get("username");
        String password = adminDetails.get("password");
        String name = adminDetails.get("name");

        // Validate input
        if (username == null || password == null || name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Username, password, and name are required"));
        }

        // Check if username already exists
        if (userRepository.findByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Username already exists"));
        }

        try {
            // Create User with ROLE_ADMIN
            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // Will be encoded in UserService
            user.setRole("ROLE_ADMIN");
            user = userService.createUser(user);

            // Create Admin linked to the User
            Admin admin = adminService.createAdmin(name, user);

            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Admin registered successfully");
            response.put("userId", user.getId());
            response.put("adminId", admin.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during registration: " + e.getMessage()));
        }
    }
}