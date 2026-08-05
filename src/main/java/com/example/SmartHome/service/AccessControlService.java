package com.example.SmartHome.service;

import com.example.SmartHome.entity.Device;
import com.example.SmartHome.entity.Room;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.exception.RoomNotFoundException;
import com.example.SmartHome.exception.UnauthorizedDeviceAccessException;
import com.example.SmartHome.repository.DeviceRepository;
import com.example.SmartHome.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {
    @Autowired private DeviceRepository deviceRepository;
    @Autowired private RoomRepository roomRepository;

    public void verifyDeviceOwnership(Long deviceId, String username) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String ownerUsername = device.getRoom().getHome().getOwner().getUsername();

        if (!ownerUsername.equals(username)) {
            throw new UnauthorizedDeviceAccessException();
        }
    }

    public void verifyRoomOwnership(Long roomId, String username) {
        Room room = roomRepository.findById(roomId).orElseThrow(() ->
                new RoomNotFoundException(roomId));

        String ownerUsername = room.getHome().getOwner().getUsername();

        if (!ownerUsername.equals(username)) {
            throw new UnauthorizedDeviceAccessException();
        }
    }
}
