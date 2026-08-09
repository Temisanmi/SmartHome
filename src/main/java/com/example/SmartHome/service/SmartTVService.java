package com.example.SmartHome.service;

import com.example.SmartHome.entity.SmartTV;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.repository.SmartTVRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmartTVService {
    private final SmartTVRepository smartTVRepository;
    private final DeviceLogService deviceLogService;

    public SmartTV turnOn(Long deviceId) {
        SmartTV tv = smartTVRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(tv.isPowerOn());

        tv.setPowerOn(true);
        smartTVRepository.save(tv);

        deviceLogService.log(tv, "TURNED_ON", oldValue, "true");
        return tv;
    }

    public SmartTV turnOff(Long deviceId) {
        SmartTV tv = smartTVRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(tv.isPowerOn());

        tv.setPowerOn(false);
        smartTVRepository.save(tv);

        deviceLogService.log(tv, "TURNED_OFF", oldValue, "false");
        return tv;
    }

    public SmartTV setVolume(Long deviceId, int volume) {
        SmartTV tv = smartTVRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = String.valueOf(tv.getVolume());

        tv.setVolume(volume);
        smartTVRepository.save(tv);

        deviceLogService.log(tv, "VOLUME_CHANGED", oldValue, String.valueOf(volume));
        return tv;
    }

    public SmartTV setInput(Long deviceId, String input) {
        SmartTV tv = smartTVRepository.findById(deviceId).orElseThrow(() ->
                new DeviceNotFoundException(deviceId));

        String oldValue = tv.getCurrentInput();

        tv.setCurrentInput(input);
        smartTVRepository.save(tv);

        deviceLogService.log(tv, "INPUT_CHANGED", oldValue, input);
        return tv;
    }
}
