package com.example.SmartHome.service;

import com.example.SmartHome.entity.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PowerMonitorService {
    public static class RoomPowerSummary {
        public String roomName;
        public int deviceCount;
        public int totalWatts;
    }

    public static class PowerSummary {
        public int totalDevicesOn;
        public int totalWatts;
        public List<RoomPowerSummary> roomBreakdown = new ArrayList<>();
    }

    public PowerSummary computeSummary(Home home) {
        PowerSummary summary = new PowerSummary();

        for (Room room : home.getRooms()) {
            RoomPowerSummary roomSummary = new RoomPowerSummary();
            roomSummary.roomName = room.getName();
            roomSummary.deviceCount = 0;
            roomSummary.totalWatts = 0;

            for (Device device : room.getDevices()) {
                if (isDeviceOn(device)) {
                    roomSummary.deviceCount++;
                    roomSummary.totalWatts += device.getPowerRatingWatts();
                    summary.totalDevicesOn++;
                    summary.totalWatts += device.getPowerRatingWatts();
                }
            }
            summary.roomBreakdown.add(roomSummary);
        }
        return summary;
    }

    private boolean isDeviceOn(Device device) {
        if (device instanceof SmartLight light) return light.isPowerOn();
        if (device instanceof SmartFan fan) return fan.isPowerOn();
        if (device instanceof AirConditioner ac) return ac.isPowerOn();
        if (device instanceof SmartTV tv) return tv.isPowerOn();
        if (device instanceof Refrigerator fridge) return fridge.isPowerOn();
        if (device instanceof WaterHeater heater) return heater.isPowerOn();
        return false;
    }
}
