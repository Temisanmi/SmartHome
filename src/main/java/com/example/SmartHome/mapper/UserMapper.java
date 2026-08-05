package com.example.SmartHome.mapper;

import com.example.SmartHome.dto.response.UserResponse;
import com.example.SmartHome.entity.User;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
