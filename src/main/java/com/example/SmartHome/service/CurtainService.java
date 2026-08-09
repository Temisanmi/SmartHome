package com.example.SmartHome.service;

import com.example.SmartHome.entity.Curtain;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.CurtainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurtainService {
    private final CurtainRepository curtainRepository;
    private final DeviceLogService deviceLogService;

    public Curtain setOpenPercentage(Long deviceId, int percentage) {
        Curtain curtain = curtainRepository.findById(deviceId).orElseThrow(() ->
                        new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(curtain.getOpenPercentage());

        curtain.setOpenPercentage(percentage);
        curtainRepository.save(curtain);

        deviceLogService.log(curtain, "OPEN_PERCENTAGE_CHANGED", oldValue, String.valueOf(percentage));
        return curtain;
    }
}
