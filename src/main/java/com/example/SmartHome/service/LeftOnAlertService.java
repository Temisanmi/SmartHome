package com.example.SmartHome.service;

import com.example.SmartHome.entity.*;
import com.example.SmartHome.repository.DeviceLogRepository;
import com.example.SmartHome.repository.HomeRepository;
import com.example.SmartHome.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeftOnAlertService {
    @Autowired private HomeRepository homeRepository;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private DeviceLogRepository deviceLogRepository;

    private static final long ALERT_THRESHOLD_HOURS = 8;

    @Scheduled(fixedRate = 3600000)
    public void checkForLeftOnDevices() {
        for (Home home : homeRepository.findAll()) {
            if (home.getOwner() == null) continue;

            for (Room room : home.getRooms()) {
                for (Device device : room.getDevices()) {
                    if (device instanceof Refrigerator || device instanceof WaterHeater) continue;

                    List<DeviceLog> events = deviceLogRepository.findByDeviceIdOrderByTimestampAsc(device.getId());
                    if (events.isEmpty()) continue;

                    DeviceLog last = events.get(events.size() - 1);
                    if (!"TURNED_ON".equals(last.getEventType())) continue;

                    long hoursOn = ChronoUnit.HOURS.between(last.getTimestamp(), LocalDateTime.now());
                    if (hoursOn < ALERT_THRESHOLD_HOURS) continue;

                    boolean alreadyNotified = notificationRepository
                            .existsByRelatedDeviceIdAndCreatedAtAfter(device.getId(), last.getTimestamp());
                    if (alreadyNotified) continue;

                    Notification notification = new Notification();
                    notification.setUser(home.getOwner());
                    notification.setMessage(device.getName() + " in " + room.getName()
                            + " has been on for " + hoursOn + "+ hours");
                    notification.setRelatedDeviceId(device.getId());
                    notificationRepository.save(notification);
                }
            }
        }
    }
}
