package com.example.SmartHome.service;

import com.example.SmartHome.entity.SmartLock;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.SmartLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmartLockService {
    @Autowired private SmartLockRepository smartLockRepository;
    @Autowired private DeviceLogService deviceLogService;

    public SmartLock lock(Long deviceId) {
        SmartLock lock = smartLockRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(lock.isLocked());

        lock.setLocked(true);
        smartLockRepository.save(lock);

        deviceLogService.log(lock, "LOCKED", oldValue, "true");
        return lock;
    }

    public SmartLock unlock(Long deviceId) {
        SmartLock unlock = smartLockRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(unlock.isLocked());

        unlock.setLocked(false);
        smartLockRepository.save(unlock);

        deviceLogService.log(unlock, "UNLOCKED", oldValue, "false");
        return unlock;
    }
}
