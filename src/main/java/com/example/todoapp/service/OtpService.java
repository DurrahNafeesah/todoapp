package com.example.todoapp.service;

import com.example.todoapp.entity.Otp;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.OtpRepository;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ Make sure this import is added

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // ✅ ADD THIS ANNOTATION HERE
    @Transactional
    public ResponseEntity<?> sendOtpToEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete existing OTP for this email
        otpRepository.deleteByEmail(email);

        // Generate new OTP
        String otpCode = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(otpCode);
        otp.setExpiryDate(expiryDate);

        otpRepository.save(otp);

        // Send email
        String subject = "Password Reset OTP";
        String message = "Your OTP code for password reset is: " + otpCode;
        emailService.sendSimpleMessage(email, subject, message);
        return null;
    }

    public boolean verifyOtp(String email, String otpCode) {
        Optional<Otp> optionalOtp = otpRepository.findByEmail(email);
        if (optionalOtp.isEmpty()) return false;

        Otp otp = optionalOtp.get();
        if (otp.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        return otp.getCode().equals(otpCode);
    }

    public void deleteOtp(String email) {
        otpRepository.findByEmail(email).ifPresent(otpRepository::delete);
    }
}
