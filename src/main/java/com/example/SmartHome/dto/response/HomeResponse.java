package com.example.SmartHome.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponse {
    private Long id;
    private String name;
    private String address;
    private Long ownerId;
    private String ownerUsername;
    private LocalDateTime createdAt;
}
