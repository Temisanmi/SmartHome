package com.example.SmartHome.controller;

import com.example.SmartHome.entity.Device;
import com.example.SmartHome.entity.Schedule;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.exception.ScheduleNotFoundException;
import com.example.SmartHome.repository.DeviceRepository;
import com.example.SmartHome.repository.ScheduleRepository;
import com.example.SmartHome.service.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;
    private final DeviceRepository deviceRepository;
    private final AccessControlService accessControlService;

    @GetMapping("/add")
    public String addSchedulePage(@RequestParam Long deviceId, Model model, Authentication authentication) {
        accessControlService.verifyDeviceOwnership(deviceId, authentication.getName());
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        model.addAttribute("device", device);
        model.addAttribute("deviceType", device.getClass().getSimpleName());
        return "add-schedule";
    }

    @PostMapping("/add")
    public String addSchedule(@RequestParam Long deviceId,
                              @RequestParam String scheduleType,
                              @RequestParam String action,
                              @RequestParam(required = false) String actionValue,
                              @RequestParam(required = false) String executeAt,
                              @RequestParam(required = false) String recurringTime,
                              @RequestParam(required = false) List<String> daysOfWeek,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(deviceId, authentication.getName());
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));

        Schedule schedule = new Schedule();
        schedule.setDevice(device);
        schedule.setScheduleType(scheduleType);
        schedule.setAction(action);
        schedule.setActionValue(actionValue);

        if ("ONE_TIME".equals(scheduleType)) {
            schedule.setExecuteAt(LocalDateTime.parse(executeAt));
        } else {
            schedule.setRecurringTime(LocalTime.parse(recurringTime));
            schedule.setDaysOfWeek(
                    (daysOfWeek == null || daysOfWeek.isEmpty()) ? "ALL" : String.join(",", daysOfWeek)
            );
        }
        scheduleRepository.save(schedule);
        redirectAttributes.addFlashAttribute("successMessage", "Schedule added for " + device.getName());
        return "redirect:/dashboard";
    }

    @PostMapping("/{id}/delete")
    public String deleteSchedule(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        accessControlService.verifyDeviceOwnership(schedule.getDevice().getId(), authentication.getName());
        scheduleRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Schedule removed");
        return "redirect:/dashboard";
    }
}
