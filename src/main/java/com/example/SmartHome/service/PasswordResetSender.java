package com.example.SmartHome.service;

import com.example.SmartHome.entity.User;

public interface PasswordResetSender {
    void send(User user, String rawToken);
}
