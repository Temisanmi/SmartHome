package com.example.SmartHome.service;

import com.example.SmartHome.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.dev-mode", havingValue = "true")
public class ConsolePasswordResetSender implements PasswordResetSender{
    @Override
    public void send(User user, String rawToken){
        log.info("Password reset link for {} ({}): /reset-password?token={} -valid for 10 minutes",
                user.getUsername(), user.getEmail(), rawToken);
    }
}
