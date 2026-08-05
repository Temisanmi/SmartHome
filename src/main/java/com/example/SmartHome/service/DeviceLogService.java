package com.example.SmartHome.service;

import com.example.SmartHome.entity.Device;
import com.example.SmartHome.entity.DeviceLog;
import com.example.SmartHome.repository.DeviceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceLogService {
    @Autowired private DeviceLogRepository deviceLogRepository;

    public void log(Device device, String eventType, String oldValue, String newValue) {
        DeviceLog logEntry = new DeviceLog();

        logEntry.setDevice(device);
        logEntry.setEventType(eventType);
        logEntry.setOldValue(oldValue);
        logEntry.setNewValue(newValue);

        deviceLogRepository.save(logEntry);
    }
}
