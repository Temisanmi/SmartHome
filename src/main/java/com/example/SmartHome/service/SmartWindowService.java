package com.example.SmartHome.service;

import com.example.SmartHome.entity.SmartWindow;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.SmartWindowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmartWindowService {
    private final SmartWindowRepository smartWindowRepository;
    private final DeviceLogService deviceLogService;

    public SmartWindow setOpenPercentage(Long deviceId, int percentage) {
        SmartWindow window = smartWindowRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(window.getOpenPercentage());

        window.setOpenPercentage(percentage);
        window.setOpen(percentage > 0);

        smartWindowRepository.save(window);

        deviceLogService.log(window, "OPEN_PERCENTAGE_CHANGED", oldValue, String.valueOf(percentage));
        return window;
    }

    public SmartWindow close(Long deviceId) {
        return setOpenPercentage(deviceId, 0);
    }

    public SmartWindow openFully(Long deviceId) {
        return setOpenPercentage(deviceId, 100);
    }
}