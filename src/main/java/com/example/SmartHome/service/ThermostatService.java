package com.example.SmartHome.service;

import com.example.SmartHome.entity.Thermostat;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.ThermostatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThermostatService {
    private final ThermostatRepository thermostatRepository;
    private final DeviceLogService deviceLogService;

    public Thermostat setTargetTemperature(Long deviceId, double newTemp) {
        Thermostat thermostat = thermostatRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(thermostat.getTargetTemperature());

        thermostat.setTargetTemperature(newTemp);
        thermostatRepository.save(thermostat);

        deviceLogService.log(thermostat, "TARGET_TEMP_CHANGED", oldValue, String.valueOf(newTemp));
        return thermostat;
    }

    public Thermostat updateCurrentTemperature(Long deviceId, double currentTemp) {
        Thermostat thermostat = thermostatRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        thermostat.setCurrentTemperature(currentTemp);
        return thermostatRepository.save(thermostat);
    }
}
