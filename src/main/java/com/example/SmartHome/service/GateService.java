package com.example.SmartHome.service;

import com.example.SmartHome.entity.Gate;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.GateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateService {
    @Autowired private GateRepository gateRepository;
    @Autowired private DeviceLogService deviceLogService;

    public Gate open(Long deviceId) {
        Gate gate = gateRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(gate.isOpen());

        gate.setOpen(true);
        gateRepository.save(gate);

        deviceLogService.log(gate, "OPENED", oldValue, "true");
        return gate;
    }

    public Gate close(Long deviceId) {
        Gate gate = gateRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(gate.isOpen());

        gate.setOpen(false);
        gateRepository.save(gate);

        deviceLogService.log(gate, "CLOSED", oldValue, "false");
        return gate;
    }
}