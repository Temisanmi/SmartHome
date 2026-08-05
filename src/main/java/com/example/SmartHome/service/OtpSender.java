package com.example.SmartHome.service;

import com.example.SmartHome.entity.User;

public interface OtpSender {
    void send(User user, String code);
}
