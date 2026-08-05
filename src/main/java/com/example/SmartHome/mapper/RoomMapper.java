package com.example.SmartHome.mapper;

import com.example.SmartHome.dto.response.RoomResponse;
import com.example.SmartHome.entity.Room;

public class RoomMapper {
    public static RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getHome() != null ? room.getHome().getId() : null,
                room.getDevices() != null ? room.getDevices().size() : 0
        );
    }
}
