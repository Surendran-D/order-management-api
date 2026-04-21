package com.services.order_management.service;

import com.services.order_management.entity.User;
import com.services.order_management.repository.UserRepository;
import com.services.order_management.util.JWTUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // Register
    public String register(User user) {
        // Email already exists check
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }

        // Password encode பண்ணி save
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("CUSTOMER");
        userRepository.save(user);

        return "User registered successfully!";
    }

    // Login
    public String login(String email, String password) {
        // User exists check
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Password verify
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid password!";
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
}
