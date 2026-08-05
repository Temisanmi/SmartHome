package com.example.SmartHome.service;

import com.example.SmartHome.entity.*;
import com.example.SmartHome.repository.NotificationRepository;
import com.example.SmartHome.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleExecutorService {
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SmartLightService smartLightService;
    @Autowired private ThermostatService thermostatService;
    @Autowired private SmartLockService smartLockService;
    @Autowired private SmartFanService smartFanService;
    @Autowired private AirConditionerService airConditionerService;
    @Autowired private SmartWindowService smartWindowService;
    @Autowired private SmartTVService smartTVService;
    @Autowired private RefrigeratorService refrigeratorService;
    @Autowired private CurtainService curtainService;
    @Autowired private WaterHeaterService waterHeaterService;
    @Autowired private GateService gateService;

    @Scheduled(fixedRate = 60000)
    public void runDueSchedules() {
        LocalDateTime now = LocalDateTime.now();
        List<Schedule> activeSchedules = scheduleRepository.findByActiveTrue();

        for (Schedule schedule : activeSchedules) {
            if ("ONE_TIME".equals(schedule.getScheduleType())) {
                if (!schedule.getExecuteAt().isAfter(now)) {
                    execute(schedule);
                    schedule.setActive(false);
                    schedule.setLastExecutedDate(now.toLocalDate());
                    scheduleRepository.save(schedule);
                }
            } else if ("RECURRING".equals(schedule.getScheduleType())) {
                LocalTime nowTime = now.toLocalTime();
                boolean timeMatches = nowTime.getHour() == schedule.getRecurringTime().getHour()
                        && nowTime.getMinute() == schedule.getRecurringTime().getMinute();
                boolean alreadyRanToday = schedule.getLastExecutedDate() != null
                        && schedule.getLastExecutedDate().equals(now.toLocalDate());
                String todayAbbrev = now.getDayOfWeek().toString().substring(0, 3);
                boolean dayMatches = "ALL".equals(schedule.getDaysOfWeek())
                        || schedule.getDaysOfWeek().contains(todayAbbrev);

                if (timeMatches && dayMatches && !alreadyRanToday) {
                    execute(schedule);
                    schedule.setLastExecutedDate(now.toLocalDate());
                    scheduleRepository.save(schedule);
                }
            }
        }
    }
    private void execute(Schedule schedule) {
        Device device = schedule.getDevice();
        String action = schedule.getAction();
        String value = schedule.getActionValue();
        Long id = device.getId();
        String deviceName = device.getName();
        User owner = device.getRoom().getHome().getOwner();

        if (action.startsWith("SET_") && (value == null || value.isBlank())) {
            return;
        }

        String actionDescription = describeAction(action, value);

        if (device instanceof SmartLight) {
            switch (action) {
                case "TURN_ON" -> smartLightService.turnOn(id);
                case "TURN_OFF" -> smartLightService.turnOff(id);
                case "SET_BRIGHTNESS" -> smartLightService.setBrightness(id, Integer.parseInt(value));
            }
        } else if (device instanceof Thermostat) {
            if ("SET_TARGET_TEMP".equals(action)) {
                thermostatService.setTargetTemperature(id, Double.parseDouble(value));
            }
        } else if (device instanceof SmartLock) {
            switch (action) {
                case "LOCK" -> smartLockService.lock(id);
                case "UNLOCK" -> smartLockService.unlock(id);
            }
        } else if (device instanceof SmartFan) {
            switch (action) {
                case "TURN_ON" -> smartFanService.turnOn(id);
                case "TURN_OFF" -> smartFanService.turnOff(id);
                case "SET_SPEED" -> smartFanService.setSpeed(id, Integer.parseInt(value));
            }
        } else if (device instanceof AirConditioner) {
            switch (action) {
                case "TURN_ON" -> airConditionerService.turnOn(id);
                case "TURN_OFF" -> airConditionerService.turnOff(id);
                case "SET_TARGET_TEMP" -> airConditionerService.setTargetTemperature(id, Double.parseDouble(value));
                case "SET_MODE" -> airConditionerService.setMode(id, value);
            }
        } else if (device instanceof SmartWindow) {
            if ("SET_OPEN_PERCENTAGE".equals(action)) {
                smartWindowService.setOpenPercentage(id, Integer.parseInt(value));
            }
        } else if (device instanceof SmartTV) {
            switch (action) {
                case "TURN_ON" -> smartTVService.turnOn(id);
                case "TURN_OFF" -> smartTVService.turnOff(id);
                case "SET_VOLUME" -> smartTVService.setVolume(id, Integer.parseInt(value));
                case "SET_INPUT" -> smartTVService.setInput(id, value);
            }
        }  else if (device instanceof Refrigerator) {
            switch (action) {
                case "TURN_ON" -> refrigeratorService.turnOn(id);
                case "TURN_OFF" -> refrigeratorService.turnOff(id);
                case "SET_TARGET_TEMP" -> refrigeratorService.setTargetTemperature(id, Double.parseDouble(value));
            }
        } else if (device instanceof Curtain) {
            if ("SET_OPEN_PERCENTAGE".equals(action)) {
                curtainService.setOpenPercentage(id, Integer.parseInt(value));
            }
        } else if (device instanceof WaterHeater) {
            switch (action) {
                case "TURN_ON" -> waterHeaterService.turnOn(id);
                case "TURN_OFF" -> waterHeaterService.turnOff(id);
                case "SET_TARGET_TEMP" -> waterHeaterService.setTargetTemperature(id, Double.parseDouble(value));
            }
        } else if (device instanceof Gate) {
            switch (action) {
                case "OPEN" -> gateService.open(id);
                case "CLOSE" -> gateService.close(id);
            }
        }
        Notification notification = new Notification();
        notification.setUser(owner);
        notification.setMessage(deviceName + " " + actionDescription + " (scheduled)");
        notificationRepository.save(notification);
    }
    private String describeAction(String action, String value) {
        return switch (action) {
            case "TURN_ON" -> "turned on";
            case "TURN_OFF" -> "turned off";
            case "LOCK" -> "locked";
            case "UNLOCK" -> "unlocked";
            case "OPEN" -> "opened";
            case "CLOSE" -> "closed";
            case "SET_BRIGHTNESS" -> "brightness set to " + value + "%";
            case "SET_SPEED" -> "speed set to " + value;
            case "SET_TARGET_TEMP" -> "target set to " + value + "°";
            case "SET_MODE" -> "mode set to " + value;
            case "SET_VOLUME" -> "volume set to " + value;
            case "SET_INPUT" -> "input set to " + value;
            case "SET_OPEN_PERCENTAGE" -> "set to " + value + "% open";
            default -> "updated";
        };
    }
}
