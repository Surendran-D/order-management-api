package com.services.order_management.controller;

import com.services.order_management.dto.LoginRequest;
import com.services.order_management.entity.User;
import com.services.order_management.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String result = authService.register(user);
        return ResponseEntity.ok(result);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }
}
