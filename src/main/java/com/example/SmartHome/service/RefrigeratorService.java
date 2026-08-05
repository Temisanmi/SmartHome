package com.example.SmartHome.service;

import com.example.SmartHome.entity.Refrigerator;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.RefrigeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefrigeratorService {
    @Autowired private RefrigeratorRepository refrigeratorRepository;
    @Autowired private DeviceLogService deviceLogService;

    public Refrigerator turnOn(Long deviceId) {
        Refrigerator fridge = refrigeratorRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(fridge.isPowerOn());

        fridge.setPowerOn(true);
        refrigeratorRepository.save(fridge);

        deviceLogService.log(fridge, "TURNED_ON", oldValue, "true");
        return fridge;
    }

    public Refrigerator turnOff(Long deviceId) {
        Refrigerator fridge = refrigeratorRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(fridge.isPowerOn());

        fridge.setPowerOn(false);
        refrigeratorRepository.save(fridge);

        deviceLogService.log(fridge, "TURNED_OFF", oldValue, "false");
        return fridge;
    }

    public Refrigerator setTargetTemperature(Long deviceId, double newTemp) {
        Refrigerator fridge = refrigeratorRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(fridge.getTargetTemperature());

        fridge.setTargetTemperature(newTemp);
        refrigeratorRepository.save(fridge);

        deviceLogService.log(fridge, "TARGET_TEMP_CHANGED", oldValue, String.valueOf(newTemp));
        return fridge;
    }
}
