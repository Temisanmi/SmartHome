package com.example.SmartHome.controller;

import com.example.SmartHome.dto.request.CreateHomeRequest;
import com.example.SmartHome.dto.request.CreateUserRequest;
import com.example.SmartHome.entity.Home;
import com.example.SmartHome.entity.Notification;
import com.example.SmartHome.entity.User;
import com.example.SmartHome.entity.Role;
import com.example.SmartHome.exception.NotificationNotFoundException;
import com.example.SmartHome.exception.UserNotFoundException;
import com.example.SmartHome.repository.HomeRepository;
import com.example.SmartHome.repository.NotificationRepository;
import com.example.SmartHome.repository.UserRepository;
import com.example.SmartHome.service.PowerMonitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PowerMonitorService powerMonitorService;
    private final NotificationRepository notificationRepository;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new CreateUserRequest());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") CreateUserRequest request,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            User user = new User();

            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(user);
            return "redirect:/login";

        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "That username or email is already taken.");
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboardPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username));

        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }

        Home home = user.getHome();

        if (home == null) {
            model.addAttribute("homeRequest", new CreateHomeRequest());
            return "create-home";
        }

        model.addAttribute("home", home);
        model.addAttribute("powerSummary", powerMonitorService.computeSummary(home));
        model.addAttribute("notifications", notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId()));
        return "dashboard";
    }

    @GetMapping("profile")
    public String profilePage(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username));

        model.addAttribute("user", user);
        model.addAttribute("home", user.getHome());
        return "profile";
    }

    @GetMapping("/create-home")
    public String createHomePage(Model model) {
        model.addAttribute("homeRequest", new CreateHomeRequest());
        return "create-home";
    }
    @PostMapping("/create-home")
    public String createHome(@Valid @ModelAttribute("homeRequest") CreateHomeRequest request,
                             BindingResult bindingResult,
                             Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "create-home";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username));

        Home home = new Home();
        home.setName(request.getHomeName());
        home.setAddress(request.getHomeAddress());
        home.setOwner(user);
        homeRepository.save(home);

        return "redirect:/dashboard";
    }

    @PostMapping("/notifications/{id}/dismiss")
    public String dismissNotification(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        notification.setRead(true);
        notificationRepository.save(notification);
        return "redirect:/dashboard";
    }
}