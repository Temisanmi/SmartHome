package com.example.SmartHome.service;

import com.example.SmartHome.entity.Device;
import com.example.SmartHome.entity.DeviceLog;
import com.example.SmartHome.entity.Home;
import com.example.SmartHome.entity.Refrigerator;
import com.example.SmartHome.entity.Room;
import com.example.SmartHome.entity.WaterHeater;
import com.example.SmartHome.repository.DeviceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class EnergyHistoryService {
    @Autowired private DeviceLogRepository deviceLogRepository;

    private static final long LEFT_ON_ALERT_HOURS = 8;

    public static class DeviceEnergy {
        public String deviceName;
        public String roomName;
        public double kWh;
    }

    public static class RoomEnergy {
        public String roomName;
        public double kWh;
    }

    public static class DayEnergy {
        public LocalDate date;
        public double kWh;
    }

    public static class LeftOnAlert {
        public String deviceName;
        public String roomName;
        public long hoursOn;
    }

    public static class EnergySummary {
        public double kWhToday;
        public double kWhThisMonth;
        public double kWhLastMonth;
        public Double percentChangeVsLastMonth;
        public DeviceEnergy topDeviceThisMonth;
        public RoomEnergy topRoomThisMonth;
        public List<RoomEnergy> roomBreakdownThisMonth = new ArrayList<>();
        public List<DayEnergy> dailyBreakdownThisMonth = new ArrayList<>();
        public List<LeftOnAlert> leftOnAlerts = new ArrayList<>();
        public Double estimatedCostThisMonth;
    }

    public EnergySummary computeSummary(Home home, Double tariffPerKwh) {
        EnergySummary summary = new EnergySummary();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfThisMonth = now.toLocalDate().withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfLastMonth = startOfThisMonth.minusMonths(1);
        LocalDateTime endOfLastMonth = startOfThisMonth;

        Map<String, Double> deviceKwhThisMonth = new LinkedHashMap<>();
        Map<String, String> deviceRoomName = new HashMap<>();
        Map<String, Double> roomKwhThisMonth = new LinkedHashMap<>();
        Map<LocalDate, Double> dailyKwhThisMonth = new TreeMap<>();

        double totalToday = 0, totalThisMonth = 0, totalLastMonth = 0;

        for (Room room : home.getRooms()) {
            for (Device device : room.getDevices()) {
                List<DeviceLog> events = deviceLogRepository.findByDeviceIdOrderByTimestampAsc(device.getId());
                int watts = device.getPowerRatingWatts();

                totalToday += computeKwhInRange(events, watts, startOfToday, now, now);
                double kwhThisMonth = computeKwhInRange(events, watts, startOfThisMonth, now, now);
                totalThisMonth += kwhThisMonth;
                totalLastMonth += computeKwhInRange(events, watts, startOfLastMonth, endOfLastMonth, now);

                if (kwhThisMonth > 0) {
                    deviceKwhThisMonth.merge(device.getName(), kwhThisMonth, Double::sum);
                    deviceRoomName.put(device.getName(), room.getName());
                    roomKwhThisMonth.merge(room.getName(), kwhThisMonth, Double::sum);
                }

                for (LocalDate day = startOfThisMonth.toLocalDate(); !day.isAfter(now.toLocalDate()); day = day.plusDays(1)) {
                    double dayKwh = computeKwhInRange(events, watts, day.atStartOfDay(), day.plusDays(1).atStartOfDay(), now);
                    if (dayKwh > 0) dailyKwhThisMonth.merge(day, dayKwh, Double::sum);
                }

                if (!(device instanceof Refrigerator) && !(device instanceof WaterHeater) && !events.isEmpty()) {
                    DeviceLog last = events.get(events.size() - 1);
                    if ("TURNED_ON".equals(last.getEventType())) {
                        long hoursOn = ChronoUnit.HOURS.between(last.getTimestamp(), now);
                        if (hoursOn >= LEFT_ON_ALERT_HOURS) {
                            LeftOnAlert alert = new LeftOnAlert();
                            alert.deviceName = device.getName();
                            alert.roomName = room.getName();
                            alert.hoursOn = hoursOn;
                            summary.leftOnAlerts.add(alert);
                        }
                    }
                }
            }
        }

        summary.kWhToday = totalToday;
        summary.kWhThisMonth = totalThisMonth;
        summary.kWhLastMonth = totalLastMonth;
        if (totalLastMonth > 0) {
            summary.percentChangeVsLastMonth = ((totalThisMonth - totalLastMonth) / totalLastMonth) * 100;
        }

        deviceKwhThisMonth.entrySet().stream().max(Map.Entry.comparingByValue()).ifPresent(e -> {
            DeviceEnergy de = new DeviceEnergy();
            de.deviceName = e.getKey();
            de.roomName = deviceRoomName.get(e.getKey());
            de.kWh = e.getValue();
            summary.topDeviceThisMonth = de;
        });

        roomKwhThisMonth.entrySet().stream().max(Map.Entry.comparingByValue()).ifPresent(e -> {
            RoomEnergy re = new RoomEnergy();
            re.roomName = e.getKey();
            re.kWh = e.getValue();
            summary.topRoomThisMonth = re;
        });

        roomKwhThisMonth.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> {
                    RoomEnergy re = new RoomEnergy();
                    re.roomName = e.getKey();
                    re.kWh = e.getValue();
                    summary.roomBreakdownThisMonth.add(re);
                });

        for (var entry : dailyKwhThisMonth.entrySet()) {
            DayEnergy de = new DayEnergy();
            de.date = entry.getKey();
            de.kWh = entry.getValue();
            summary.dailyBreakdownThisMonth.add(de);
        }

        if (tariffPerKwh != null) {
            summary.estimatedCostThisMonth = totalThisMonth * tariffPerKwh;
        }
        return summary;
    }

    private double computeKwhInRange(List<DeviceLog> events, int wattage,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, LocalDateTime now) {
        LocalDateTime effectiveEnd = rangeEnd.isAfter(now) ? now : rangeEnd;
        boolean isOn = false;
        LocalDateTime lastTimestamp = null;
        long totalSeconds = 0;

        for (DeviceLog event : events) {
            if (event.getTimestamp().isAfter(effectiveEnd)) break;
            if (isOn && lastTimestamp != null) {
                LocalDateTime overlapStart = lastTimestamp.isBefore(rangeStart) ? rangeStart : lastTimestamp;
                LocalDateTime overlapEnd = event.getTimestamp().isAfter(effectiveEnd) ? effectiveEnd : event.getTimestamp();
                if (overlapEnd.isAfter(overlapStart)) {
                    totalSeconds += ChronoUnit.SECONDS.between(overlapStart, overlapEnd);
                }
            }
            lastTimestamp = event.getTimestamp();
            if ("TURNED_ON".equals(event.getEventType())) isOn = true;
            else if ("TURNED_OFF".equals(event.getEventType())) isOn = false;
        }

        if (isOn && lastTimestamp != null) {
            LocalDateTime overlapStart = lastTimestamp.isBefore(rangeStart) ? rangeStart : lastTimestamp;
            if (effectiveEnd.isAfter(overlapStart)) {
                totalSeconds += ChronoUnit.SECONDS.between(overlapStart, effectiveEnd);
            }
        }
        return (totalSeconds / 3600.0) * wattage / 1000.0;
    }
}