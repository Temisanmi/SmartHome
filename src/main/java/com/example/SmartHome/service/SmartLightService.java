package com.example.SmartHome.service;

import com.example.SmartHome.entity.SmartLight;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.SmartLightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmartLightService {
    private final SmartLightRepository smartLightRepository;
    private final DeviceLogService deviceLogService;

    public SmartLight turnOn(Long deviceId) {
        SmartLight light = smartLightRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(light.isPowerOn());

        light.setPowerOn(true);
        smartLightRepository.save(light);

        deviceLogService.log(light, "TURNED_ON", oldValue, "true");
        return light;
    }

    public SmartLight turnOff(Long deviceId) {
        SmartLight light = smartLightRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(light.isPowerOn());

        light.setPowerOn(false);
        smartLightRepository.save(light);

        deviceLogService.log(light, "TURNED_OFF", oldValue, "false");
        return light;
    }

    public SmartLight setBrightness(Long deviceId, int brightness) {
        SmartLight light = smartLightRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(light.getBrightness());

        light.setBrightness(brightness);
        smartLightRepository.save(light);

        deviceLogService.log(light, "BRIGHTNESS_CHANGED", oldValue, String.valueOf(brightness));
        return light;
    }
}
