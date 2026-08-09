package com.example.SmartHome.service;

import com.example.SmartHome.entity.WaterHeater;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.WaterHeaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaterHeaterService {
    private final WaterHeaterRepository waterHeaterRepository;
    private final DeviceLogService deviceLogService;

    public WaterHeater turnOn(Long deviceId) {
        WaterHeater heater = waterHeaterRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(heater.isPowerOn());

        heater.setPowerOn(true);
        waterHeaterRepository.save(heater);

        deviceLogService.log(heater, "TURNED_ON", oldValue, "true");
        return heater;
    }

    public WaterHeater turnOff(Long deviceId) {
        WaterHeater heater = waterHeaterRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(heater.isPowerOn());

        heater.setPowerOn(false);
        waterHeaterRepository.save(heater);

        deviceLogService.log(heater, "TURNED_OFF", oldValue, "false");
        return heater;
    }

    public WaterHeater setTargetTemperature(Long deviceId, double newTemp) {
        WaterHeater heater = waterHeaterRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(heater.getTargetTemperature());

        heater.setTargetTemperature(newTemp);
        waterHeaterRepository.save(heater);

        deviceLogService.log(heater, "TARGET_TEMP_CHANGED", oldValue, String.valueOf(newTemp));
        return heater;
    }
}