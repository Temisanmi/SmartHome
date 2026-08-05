package com.example.SmartHome.controller;

import com.example.SmartHome.dto.response.DeviceLogResponse;
import com.example.SmartHome.dto.response.PageResponse;
import com.example.SmartHome.mapper.DeviceLogMapper;
import com.example.SmartHome.repository.DeviceLogRepository;
import com.example.SmartHome.service.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class DeviceLogController {
    private final DeviceLogRepository deviceLogRepository;
    private final AccessControlService accessControlService;

    @GetMapping("/device/{deviceId}")
    public PageResponse<DeviceLogResponse> getLogsForDevice(
            @PathVariable Long deviceId,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        accessControlService.verifyDeviceOwnership(deviceId, authentication.getName());

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        return PageResponse.from(deviceLogRepository.findByDeviceIdOrderByTimestampDesc(deviceId, pageable)
                        .map(DeviceLogMapper::toResponse)
        );
    }
}
