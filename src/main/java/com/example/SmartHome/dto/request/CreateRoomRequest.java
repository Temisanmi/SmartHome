package com.example.SmartHome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequest {
    @NotBlank(message = "Room name is required")
    private String roomName;

    @NotNull(message = "Home id is required")
    private Long homeId;
}
