package com.example.SmartHome.service;

import com.example.SmartHome.entity.User;
import com.example.SmartHome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {
    @Autowired private UserRepository userRepository;
    @Autowired private OtpSender otpSender;

    private static final long OTP_VALID_MINUTES = 1;
    private final SecureRandom random = new SecureRandom();

    public void generateAndSendOtp(User user) {
        String code = String.format("%06d", random.nextInt(1_000_000));

        user.setOtpCode(code);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));
        userRepository.save(user);

        otpSender.send(user, code);
    }

    public boolean verifyOtp(User user, String inputCode) {
        if (user.getOtpCode() == null || user.getOtpExpiry() == null) return false;
        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) return false;

        boolean matches = user.getOtpCode().equals(inputCode);
        if (matches) {
            user.setOtpCode(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
        }
        return matches;
    }
}
