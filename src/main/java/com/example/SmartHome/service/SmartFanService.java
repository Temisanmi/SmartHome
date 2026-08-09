package com.example.SmartHome.service;

import com.example.SmartHome.entity.SmartFan;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.SmartFanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmartFanService {
    private final SmartFanRepository smartFanRepository;
    private final DeviceLogService deviceLogService;

    public SmartFan turnOn(Long deviceId) {
        SmartFan fan = smartFanRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(fan.isPowerOn());

        fan.setPowerOn(true);
        smartFanRepository.save(fan);

        deviceLogService.log(fan, "TURNED_ON", oldValue, "true");
        return fan;
    }

    public SmartFan turnOff(Long deviceId) {
        SmartFan fan = smartFanRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(fan.isPowerOn());

        fan.setPowerOn(false);
        smartFanRepository.save(fan);

        deviceLogService.log(fan, "TURNED_OFF", oldValue, "false");
        return fan;
    }

    public SmartFan setSpeed(Long deviceId, int speed) {
        SmartFan fan = smartFanRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(fan.getSpeed());

        fan.setSpeed(speed);
        smartFanRepository.save(fan);

        deviceLogService.log(fan, "SPEED_CHANGED", oldValue, String.valueOf(speed));
        return fan;
    }
}
