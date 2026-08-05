package com.example.SmartHome.mapper;

import com.example.SmartHome.dto.response.DeviceLogResponse;
import com.example.SmartHome.entity.DeviceLog;

public class DeviceLogMapper {
    public static DeviceLogResponse toResponse(DeviceLog log) {
        return new DeviceLogResponse(
                log.getId(),
                log.getDevice() != null ? log.getDevice().getId() : null,
                log.getEventType(),
                log.getOldValue(),
                log.getNewValue(),
                log.getTimestamp()
        );
    }
}
