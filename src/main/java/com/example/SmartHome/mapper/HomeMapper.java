package com.example.SmartHome.mapper;

import com.example.SmartHome.dto.response.HomeResponse;
import com.example.SmartHome.entity.Home;

public class HomeMapper {
    public static HomeResponse toResponse(Home home) {
        return new HomeResponse(
                home.getId(),
                home.getName(),
                home.getAddress(),
                home.getOwner() != null ? home.getOwner().getId() : null,
                home.getOwner() != null ? home.getOwner().getUsername() : null,
                home.getCreatedAt()
        );
    }
}
