package com.example.todoapp.controller;

import com.example.todoapp.dto.ResetPasswordRequest;
import com.example.todoapp.dto.UserLoginRequest;
import com.example.todoapp.dto.UserRegisterRequest;
import com.example.todoapp.security.JwtUtils;
import com.example.todoapp.service.OtpService;
import com.example.todoapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final OtpService otpService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, OtpService otpService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginRequest request) {
        return userService.authenticateUser(request);
    }

    @PostMapping("/forgot-password/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody EmailRequest request) {
        return otpService.sendOtpToEmail(request.getEmail());
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        return userService.resetPasswordWithOtp(request);
    }

    public static class JwtResponse {
        private String token;
        public JwtResponse(String token) { this.token = token; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    static class EmailRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}