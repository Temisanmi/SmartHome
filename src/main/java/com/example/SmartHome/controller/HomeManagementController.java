package com.example.SmartHome.controller;

import com.example.SmartHome.entity.*;
import com.example.SmartHome.exception.DeviceNotFoundException;
import com.example.SmartHome.exception.RoomNotFoundException;
import com.example.SmartHome.exception.UserNotFoundException;
import com.example.SmartHome.repository.*;
import com.example.SmartHome.service.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class HomeManagementController {
    private final AccessControlService accessControlService;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final SmartLightRepository smartLightRepository;
    private final ThermostatRepository thermostatRepository;
    private final SmartLockRepository smartLockRepository;
    private final SmartFanRepository smartFanRepository;
    private final AirConditionerRepository airConditionerRepository;
    private final SmartWindowRepository smartWindowRepository;
    private final SmartTVRepository smartTVRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final CurtainRepository curtainRepository;
    private final WaterHeaterRepository waterHeaterRepository;
    private final GateRepository gateRepository;

    @PostMapping("/rooms/add")
    public String addRoom(@RequestParam String name, Authentication authentication) {
        User user = getCurrentUser(authentication);
        Home home = user.getHome();

        Room room = new Room();
        room.setName(name);
        room.setHome(home);
        roomRepository.save(room);

        return "redirect:/dashboard";
    }

    @GetMapping("/devices/add")
    public String addDevicePage(@RequestParam Long roomId, Model model,Authentication authentication) {
        accessControlService.verifyRoomOwnership(roomId, authentication.getName() );
        Room room = roomRepository.findById(roomId).orElseThrow(() ->
                new RoomNotFoundException(roomId));
        model.addAttribute("room", room);
        return "add-device";
    }

    @PostMapping("/devices/add")
    public String addDevice(@RequestParam Long roomId,
                            @RequestParam String deviceType,
                            @RequestParam String name,
                            Authentication authentication) {
        accessControlService.verifyRoomOwnership(roomId, authentication.getName());
        Room room = roomRepository.findById(roomId).orElseThrow(() ->
                new RoomNotFoundException(roomId));

        switch (deviceType) {
            case "LIGHT" -> {
                SmartLight light = new SmartLight();
                light.setName(name);
                light.setRoom(room);
                light.setPowerRatingWatts(20);
                smartLightRepository.save(light);
            }
            case "THERMOSTAT" -> {
                Thermostat thermostat = new Thermostat();
                thermostat.setName(name);
                thermostat.setRoom(room);
                thermostat.setPowerRatingWatts(5);
                thermostatRepository.save(thermostat);
            }
            case "LOCK" -> {
                SmartLock lock = new SmartLock();
                lock.setName(name);
                lock.setRoom(room);
                lock.setPowerRatingWatts(0);
                smartLockRepository.save(lock);
            }
            case "FAN" -> {
                SmartFan fan = new SmartFan();
                fan.setName(name);
                fan.setRoom(room);
                fan.setPowerRatingWatts(60);
                smartFanRepository.save(fan);
            }
            case "AC" -> {
                AirConditioner ac = new AirConditioner();
                ac.setName(name);
                ac.setRoom(room);
                ac.setPowerRatingWatts(2500);
                airConditionerRepository.save(ac);
            }
            case "WINDOW" -> {
                SmartWindow window = new SmartWindow();
                window.setName(name);
                window.setRoom(room);
                window.setPowerRatingWatts(15);
                smartWindowRepository.save(window);
            }
            case "TV" -> {
                SmartTV tv = new SmartTV();
                tv.setName(name);
                tv.setRoom(room);
                tv.setPowerRatingWatts(170);
                smartTVRepository.save(tv);
            }
            case "FRIDGE" -> {
                Refrigerator fridge = new Refrigerator();
                fridge.setName(name);
                fridge.setRoom(room);
                fridge.setPowerRatingWatts(250);
                refrigeratorRepository.save(fridge);
            }
            case "CURTAIN" -> {
                Curtain curtain = new Curtain();
                curtain.setName(name);
                curtain.setRoom(room);
                curtain.setPowerRatingWatts(0);
                curtainRepository.save(curtain);
            }
            case "WATERHEATER" -> {
                WaterHeater heater = new WaterHeater();
                heater.setName(name);
                heater.setRoom(room);
                heater.setPowerRatingWatts(4000);
                waterHeaterRepository.save(heater);
            }
            case "GATE" -> {
                Gate gate = new Gate();
                gate.setName(name);
                gate.setRoom(room);
                gate.setPowerRatingWatts(0);
                gateRepository.save(gate);
            }
        }
        return "redirect:/dashboard";
    }

    private User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username));
    }

    @PostMapping("/devices/{id}/delete")
    public String deleteDevice(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var device = deviceRepository.findById(id).orElseThrow(() ->
                new DeviceNotFoundException(id));
        String name = device.getName();
        deviceRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", name + " removed");
        return "redirect:/dashboard";
    }
}