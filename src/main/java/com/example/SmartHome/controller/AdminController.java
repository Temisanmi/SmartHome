package com.example.SmartHome.controller;

import com.example.SmartHome.dto.request.AdminUpdateHomeRequest;
import com.example.SmartHome.dto.request.AdminUpdateUserRequest;
import com.example.SmartHome.entity.Home;
import com.example.SmartHome.entity.Role;
import com.example.SmartHome.entity.User;
import com.example.SmartHome.exception.UserNotFoundException;
import com.example.SmartHome.repository.AdminAuditLogRepository;
import com.example.SmartHome.repository.DeviceRepository;
import com.example.SmartHome.repository.HomeRepository;
import com.example.SmartHome.repository.RoomRepository;
import com.example.SmartHome.repository.UserRepository;
import com.example.SmartHome.service.AdminAuditService;
import com.example.SmartHome.service.PowerMonitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
        private final UserRepository userRepository;
        private final HomeRepository homeRepository;
        private final RoomRepository roomRepository;
        private final DeviceRepository deviceRepository;
        private final AdminAuditLogRepository adminAuditLogRepository;
        private final PowerMonitorService powerMonitorService;
        private final AdminAuditService adminAuditService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) String query,
                            Authentication authentication,
                            Model model) {
        int safePage = Math.max(page, 0);
        PageRequest pageable = PageRequest.of(safePage, 15, Sort.by("createdAt").descending());
        Page<User> users = (query == null || query.isBlank())
                ? userRepository.findAllByOrderByCreatedAtDesc(pageable)
                : userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByCreatedAtDesc(query.trim(), query.trim(), pageable);

        model.addAttribute("users", users);
        model.addAttribute("query", query == null ? "" : query.trim());
        model.addAttribute("activeUserCount", userRepository.countByActiveTrue());
        model.addAttribute("homeCount", homeRepository.count());
        model.addAttribute("roomCount", roomRepository.count());
        model.addAttribute("deviceCount", deviceRepository.count());
        model.addAttribute("recentActivity", adminAuditLogRepository.findTop5ByOrderByCreatedAtDesc());
        model.addAttribute("adminUsername", authentication.getName());
        return "admin-dashboard";
    }

    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        User user = findUser(id);
        populateUserDetailModel(model, user);
        return "admin-user-detail";
    }

    @PostMapping("/users/{id}/profile")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute AdminUpdateUserRequest request,
                             BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Enter a valid username and email address.");
            return "redirect:/admin/users/" + id;
        }
        User user = findUser(id);
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();
        if (!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "That username is already in use.");
            return "redirect:/admin/users/" + id;
        }
        if (!user.getEmail().equalsIgnoreCase(email) && userRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("errorMessage", "That email address is already in use.");
            return "redirect:/admin/users/" + id;
        }
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
        adminAuditService.record(authentication.getName(), "USER_PROFILE_UPDATED", user.getId(), "Updated username or email for " + username);
        redirectAttributes.addFlashAttribute("successMessage", "User details saved.");
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/users/{id}/home")
    public String updateHome(@PathVariable Long id, @Valid @ModelAttribute AdminUpdateHomeRequest request,
                             BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Enter valid home details and a non-negative tariff.");
            return "redirect:/admin/users/" + id;
        }
        User user = findUser(id);
        Home home = user.getHome();
        if (home == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "This user does not have a home to edit.");
            return "redirect:/admin/users/" + id;
        }
        home.setName(request.getName().trim());
        home.setAddress(request.getAddress().trim());
        home.setTariffPerKwh(request.getTariffPerKwh());
        homeRepository.save(home);
        adminAuditService.record(authentication.getName(), "HOME_UPDATED", user.getId(), "Updated home details for " + user.getUsername());
        redirectAttributes.addFlashAttribute("successMessage", "Home details saved.");
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/users/{id}/status")
    public String setUserStatus(@PathVariable Long id, @RequestParam boolean active,
                                Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = findUser(id);
        if (user.getUsername().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot suspend your own administrator account.");
            return "redirect:/admin/users/" + id;
        }
        user.setActive(active);
        userRepository.save(user);
        adminAuditService.record(authentication.getName(), active ? "USER_REACTIVATED" : "USER_SUSPENDED", user.getId(), user.getUsername());
        redirectAttributes.addFlashAttribute("successMessage", active ? "Account reactivated." : "Account suspended.");
        return "redirect:/admin/users/" + id;
    }

    @PostMapping("/users/{id}/role")
    public String updateRole(@PathVariable Long id, @RequestParam Role role,
                             Authentication authentication, RedirectAttributes redirectAttributes) {
        User user = findUser(id);
        if (user.getUsername().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You cannot change your own administrator role.");
            return "redirect:/admin/users/" + id;
        }
        if (user.getRole() == Role.ADMIN && role == Role.USER && userRepository.countByRoleAndActiveTrue(Role.ADMIN) <= 1) {
            redirectAttributes.addFlashAttribute("errorMessage", "The last active administrator cannot be demoted.");
            return "redirect:/admin/users/" + id;
        }
        user.setRole(role);
        userRepository.save(user);
        adminAuditService.record(authentication.getName(), "USER_ROLE_CHANGED", user.getId(), user.getUsername() + " is now " + role);
        redirectAttributes.addFlashAttribute("successMessage", "Account role updated.");
        return "redirect:/admin/users/" + id;
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void populateUserDetailModel(Model model, User user) {
        AdminUpdateUserRequest userRequest = new AdminUpdateUserRequest();
        userRequest.setUsername(user.getUsername());
        userRequest.setEmail(user.getEmail());
        model.addAttribute("userRequest", userRequest);
        model.addAttribute("managedUser", user);

        if (user.getHome() != null) {
            Home home = user.getHome();
            AdminUpdateHomeRequest homeRequest = new AdminUpdateHomeRequest();
            homeRequest.setName(home.getName());
            homeRequest.setAddress(home.getAddress());
            homeRequest.setTariffPerKwh(home.getTariffPerKwh() == null ? 0.0 : home.getTariffPerKwh());
            model.addAttribute("homeRequest", homeRequest);
            model.addAttribute("powerSummary", powerMonitorService.computeSummary(home));
        }
    }
}