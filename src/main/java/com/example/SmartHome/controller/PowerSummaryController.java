package com.example.SmartHome.controller;

import com.example.SmartHome.entity.Home;
import com.example.SmartHome.entity.User;
import com.example.SmartHome.exception.UserNotFoundException;
import com.example.SmartHome.repository.HomeRepository;
import com.example.SmartHome.repository.UserRepository;
import com.example.SmartHome.service.EnergyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PowerSummaryController {
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;
    private final EnergyHistoryService energyHistoryService;

    @GetMapping("/power-summary")
    public String powerSummaryPage(Model model, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Home home = user.getHome();

        EnergyHistoryService.EnergySummary summary =
                energyHistoryService.computeSummary(home, home.getTariffPerKwh());

        model.addAttribute("home", home);
        model.addAttribute("summary", summary);
        return "power-summary";
    }

    @PostMapping("/power-summary/tariff")
    public String updateTariff(@RequestParam Double tariffPerKwh, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Home home = user.getHome();
        home.setTariffPerKwh(tariffPerKwh);
        homeRepository.save(home);
        return "redirect:/power-summary";
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username));
    }
}