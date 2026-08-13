package com.example.SmartHome.service;

import com.example.SmartHome.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class MailOtpSender implements OtpSender {
    private final JavaMailSender mailSender;

    @Override
    public void send(User user, String code) {
        String htmlBody = """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: auto;">
                    <h2>Your Smart Home login code</h2>
                    <p>Hi %s,</p>
                    <p>Your one-time login code is:</p>
                    <p style="font-size: 28px; font-weight: bold; letter-spacing: 4px;">%s</p>
                    <p>This code expires in 2 minutes. If you didn't request this, you can ignore this email.</p>
                </div>
                """.formatted(user.getUsername(), code);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Your Smart Home login code");
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send OTP email", e);
        }
    }
}