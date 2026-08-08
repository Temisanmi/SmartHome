package com.example.SmartHome.controller;

import com.example.SmartHome.entity.User;
import com.example.SmartHome.entity.Role;
import com.example.SmartHome.exception.UserNotFoundException;
import com.example.SmartHome.repository.UserRepository;
import com.example.SmartHome.service.OtpService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class OtpController {
    private final UserRepository userRepository;
    private final OtpService otpService;

    @Value("${app.dev-mode:false}")
    private boolean devMode;

    @GetMapping("/verify-otp")
    public String verifyOtpPage(HttpSession session, Model model) {
        if (devMode) {
            model.addAttribute("devOtp", session.getAttribute("devOtp"));
        }
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String code, HttpSession session,
                            Authentication authentication, Model model) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (otpService.verifyOtp(user, code)) {
            session.setAttribute("otpVerified", true);
            return user.getRole() == Role.ADMIN
                    ? "redirect:/admin/dashboard"
                    : "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid or expired code. Please try again.");
        if (devMode) {
            model.addAttribute("devOtp", session.getAttribute("devOtp"));
        }
        return "verify-otp";
    }

    @PostMapping("/verify-otp/resend")
    public String resendOtp(Authentication authentication, HttpSession session) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        otpService.generateAndSendOtp(user);
        if (devMode) {
            session.setAttribute("devOtp", user.getOtpCode());
        }
        return "redirect:/verify-otp";
    }
}