package com.example.todoapp.service;

import com.example.todoapp.controller.AuthController;
import com.example.todoapp.dto.ResetPasswordRequest;
import com.example.todoapp.dto.UserLoginRequest;
import com.example.todoapp.dto.UserRegisterRequest;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       OtpService otpService,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> registerUser(@Valid UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    public ResponseEntity<?> authenticateUser(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new AuthController.JwtResponse(token));
    }

    public ResponseEntity<?> resetPasswordWithOtp(ResetPasswordRequest request) {
        boolean isOtpValid = otpService.verifyOtp(request.getEmail(), request.getOtpCode());
        if (!isOtpValid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email");
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        otpService.deleteOtp(request.getEmail());

        return ResponseEntity.ok("Password reset successfully");
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}