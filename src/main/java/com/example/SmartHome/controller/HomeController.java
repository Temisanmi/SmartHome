package com.example.SmartHome.controller;

import com.example.SmartHome.dto.request.CreateHomeRequest;
import com.example.SmartHome.dto.response.HomeResponse;
import com.example.SmartHome.entity.Home;
import com.example.SmartHome.entity.User;
import com.example.SmartHome.exception.HomeNotFoundException;
import com.example.SmartHome.exception.UserNotFoundException;
import com.example.SmartHome.mapper.HomeMapper;
import com.example.SmartHome.repository.HomeRepository;
import com.example.SmartHome.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/homes")
public class HomeController {
    private final HomeRepository homeRepository;
    private final UserRepository userRepository;

    @PostMapping
    public HomeResponse createHome(@Valid @RequestBody CreateHomeRequest request) {
        Home home = new Home();
        home.setName(request.getHomeName());
        home.setAddress(request.getHomeAddress());
        Home savedHome = homeRepository.save(home);
        return HomeMapper.toResponse(savedHome);
    }

    @GetMapping
    public List<HomeResponse> getAllHomes() {
        return homeRepository.findAll()
                .stream()
                .map(HomeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public HomeResponse getHome(@PathVariable Long id) {
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new HomeNotFoundException(id));
        return HomeMapper.toResponse(home);
    }

    @PutMapping("/{id}/owner/{userId}")
    public HomeResponse setOwner(@PathVariable Long id, @PathVariable Long userId) {
        Home home = homeRepository.findById(id)
                .orElseThrow(() -> new HomeNotFoundException(id));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        home.setOwner(user);
        Home savedHome = homeRepository.save(home);
        return HomeMapper.toResponse(savedHome);
    }
}