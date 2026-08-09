package com.example.SmartHome.service;

import com.example.SmartHome.entity.AirConditioner;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.AirConditionerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirConditionerService {
    private final AirConditionerRepository airConditionerRepository;
    private final DeviceLogService deviceLogService;

    public AirConditioner turnOn(Long deviceId) {
        AirConditioner ac = airConditionerRepository.findById(deviceId).orElseThrow(()
                -> new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(ac.isPowerOn());

        ac.setPowerOn(true);
        airConditionerRepository.save(ac);

        deviceLogService.log(ac, "TURNED_ON", oldValue, "true");
        return ac;
    }

    public AirConditioner turnOff(Long deviceId) {
        AirConditioner ac = airConditionerRepository.findById(deviceId).orElseThrow(()
                -> new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(ac.isPowerOn());

        ac.setPowerOn(false);
        airConditionerRepository.save(ac);

        deviceLogService.log(ac, "TURNED_OFF", oldValue, "false");
        return ac;
    }

    public AirConditioner setTargetTemperature(Long deviceId, double newTemp) {
        AirConditioner ac = airConditionerRepository.findById(deviceId).orElseThrow(()
                -> new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(ac.getTargetTemperature());

        ac.setTargetTemperature(newTemp);
        airConditionerRepository.save(ac);

        deviceLogService.log(ac, "TARGET_TEMP_CHANGED", oldValue, String.valueOf(newTemp));
        return ac;
    }

    public AirConditioner setMode(Long deviceId, String mode) {
        AirConditioner ac = airConditionerRepository.findById(deviceId).orElseThrow(()
                -> new DeviceNotFoundException(deviceId));

        String oldValue = ac.getMode();

        ac.setMode(mode);
        airConditionerRepository.save(ac);

        deviceLogService.log(ac, "MODE_CHANGED", oldValue, mode);
        return ac;
    }
}
