package com.example.SmartHome.controller;

import com.example.SmartHome.dto.request.CreateRoomRequest;
import com.example.SmartHome.dto.response.RoomResponse;
import com.example.SmartHome.entity.Home;
import com.example.SmartHome.entity.Room;
import com.example.SmartHome.exception.HomeNotFoundException;
import com.example.SmartHome.exception.RoomNotFoundException;
import com.example.SmartHome.mapper.RoomMapper;
import com.example.SmartHome.repository.HomeRepository;
import com.example.SmartHome.repository.RoomRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomRepository roomRepository;
    private final HomeRepository homeRepository;

    @PostMapping
    public RoomResponse createRoom(@Valid @RequestBody CreateRoomRequest request) {
        Home home = homeRepository.findById(request.getHomeId())
                .orElseThrow(() -> new HomeNotFoundException(request.getHomeId()));

        Room room = new Room();
        room.setName(request.getRoomName());
        room.setHome(home);
        Room savedRoom = roomRepository.save(room);
        return RoomMapper.toResponse(savedRoom);
    }

    @GetMapping("/home/{homeId}")
    public List<RoomResponse> getRoomsByHome(@PathVariable Long homeId) {
        return roomRepository.findByHomeId(homeId)
                .stream()
                .map(RoomMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RoomResponse getRoom(@PathVariable Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));
        return RoomMapper.toResponse(room);
    }
}