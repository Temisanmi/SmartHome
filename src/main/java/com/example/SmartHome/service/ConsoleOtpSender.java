package com.example.SmartHome.service;

import com.example.SmartHome.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsoleOtpSender implements OtpSender {
    @Override
    public void send(User user, String code) {
        log.info("OTP for {} ({}): {} — valid for 1 minute",
                user.getUsername(), user.getEmail(), code);
    }
}
