package com.example.SmartHome.security;

import com.example.SmartHome.entity.User;
import com.example.SmartHome.exception.UserNotFoundException;
import com.example.SmartHome.repository.UserRepository;
import com.example.SmartHome.service.OtpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OtpAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired private UserRepository userRepository;
    @Autowired private OtpService otpService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        otpService.generateAndSendOtp(user);

        request.getSession().setAttribute("otpVerified", false);
        response.sendRedirect("/verify-otp");
    }
}
