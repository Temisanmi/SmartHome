package com.example.SmartHome.controller;

import com.example.SmartHome.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {
    private final PasswordResetService passwordResetService;

    @Value("${app.dev-mode:false}")
    private boolean devMode;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(){
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String requestReset(@RequestParam String email, Model model){
        String rawToken = passwordResetService.initiateReset(email);
        model.addAttribute("message", "If an account exists for that email, a reset link has been sent.");
        if (devMode && rawToken != null) {
            model.addAttribute("devResetLink", "/reset-password?token=" + rawToken);
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model){
        if (!passwordResetService.isTokenValid(token)){
            model.addAttribute("error", "That reset link is invalid or expired");
            return "reset-password";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                Model model){
        if (!password.equals(confirmPassword)){
            model.addAttribute("token", token);
            model.addAttribute("error", "Passwords don't match.");
            return "reset-password";
        }
        boolean success = passwordResetService.completeReset(token, password);
        if (!success){
            model.addAttribute("error", "That reset link is invalid or expired");
            return "reset-password";
        }
        return "redirect:/login?reset";
    }
}