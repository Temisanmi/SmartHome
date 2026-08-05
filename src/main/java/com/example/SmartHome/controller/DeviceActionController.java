package com.example.SmartHome.controller;

import com.example.SmartHome.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/devices")
public class DeviceActionController {
    private final RefrigeratorService refrigeratorService;
    private final CurtainService curtainService;
    private final WaterHeaterService waterHeaterService;
    private final GateService gateService;
    private final SmartLightService smartLightService;
    private final ThermostatService thermostatService;
    private final SmartLockService smartLockService;
    private final SmartFanService smartFanService;
    private final AirConditionerService airConditionerService;
    private final SmartWindowService smartWindowService;
    private final SmartTVService smartTVService;
    private final AccessControlService accessControlService;

    @PostMapping("/light/{id}/toggle-on")
    public String lightTurnOn(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var light = smartLightService.turnOn(id);
        redirectAttributes.addFlashAttribute("successMessage", light.getName() + " turned on");
        return "redirect:/dashboard";
    }
    @PostMapping("/light/{id}/toggle-off")
    public String lightTurnOff(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var light = smartLightService.turnOff(id);
        redirectAttributes.addFlashAttribute("successMessage", light.getName() + " turned off");
        return "redirect:/dashboard";
    }
    @PostMapping("/light/{id}/brightness")
    public String lightBrightness(@PathVariable Long id, @RequestParam int value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var light = smartLightService.setBrightness(id, value);
        redirectAttributes.addFlashAttribute("successMessage", light.getName() + " brightness set to " + value + "%");
        return "redirect:/dashboard";
    }


    @PostMapping("/thermostat/{id}/target-temp")
    public String thermostatTemp(@PathVariable Long id, @RequestParam double value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var thermostat = thermostatService.setTargetTemperature(id, value);
        redirectAttributes.addFlashAttribute("successMessage", thermostat.getName() + " target set to " + value + "°");
        return "redirect:/dashboard";
    }


    @PostMapping("/lock/{id}/lock")
    public String lock(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var lock = smartLockService.lock(id);
        redirectAttributes.addFlashAttribute("successMessage", lock.getName() + " locked");
        return "redirect:/dashboard";
    }
    @PostMapping("/lock/{id}/unlock")
    public String unlock(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var unlock = smartLockService.unlock(id);
        redirectAttributes.addFlashAttribute("successMessage", unlock.getName() + " unlocked");
        return "redirect:/dashboard";
    }


    @PostMapping("/fan/{id}/toggle-on")
    public String fanOn(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var fan = smartFanService.turnOn(id);
        redirectAttributes.addFlashAttribute("successMessage", fan.getName() + " turned on");
        return "redirect:/dashboard";
    }
    @PostMapping("/fan/{id}/toggle-off")
    public String fanOff(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var fan = smartFanService.turnOff(id);
        redirectAttributes.addFlashAttribute("successMessage", fan.getName() + " turned off");
        return "redirect:/dashboard";
    }
    @PostMapping("/fan/{id}/speed")
    public String fanSpeed(@PathVariable Long id, @RequestParam int value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var fan = smartFanService.setSpeed(id, value);
        redirectAttributes.addFlashAttribute("successMessage", fan.getName() + " speed set to " + value);
        return "redirect:/dashboard";
    }


    @PostMapping("/ac/{id}/toggle-on")
    public String acOn(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var ac = airConditionerService.turnOn(id);
        redirectAttributes.addFlashAttribute("successMessage", ac.getName() + " turned on");
        return "redirect:/dashboard";
    }
    @PostMapping("/ac/{id}/toggle-off")
    public String acOff(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var ac = airConditionerService.turnOff(id);
        redirectAttributes.addFlashAttribute("successMessage", ac.getName() + " turned off");
        return "redirect:/dashboard";
    }
    @PostMapping("/ac/{id}/target-temp")
    public String acTemp(@PathVariable Long id, @RequestParam double value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var ac = airConditionerService.setTargetTemperature(id, value);
        redirectAttributes.addFlashAttribute("successMessage", ac.getName() + " target set to " + value + "°");
        return "redirect:/dashboard";
    }
    @PostMapping("/ac/{id}/mode")
    public String acMode(@PathVariable Long id, @RequestParam String value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var ac = airConditionerService.setMode(id, value);
        redirectAttributes.addFlashAttribute("successMessage", ac.getName() + " mode set to " + value);
        return "redirect:/dashboard";
    }


    @PostMapping("/window/{id}/open-percentage")
    public String windowSet(@PathVariable Long id, @RequestParam int value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var window = smartWindowService.setOpenPercentage(id, value);
        redirectAttributes.addFlashAttribute("successMessage", window.getName() + " set to " + value + "% open");
        return "redirect:/dashboard";
    }


    @PostMapping("/tv/{id}/toggle-on")
    public String tvOn(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var tv = smartTVService.turnOn(id);
        redirectAttributes.addFlashAttribute("successMessage", tv.getName() + " turned on");
        return "redirect:/dashboard";
    }
    @PostMapping("/tv/{id}/toggle-off")
    public String tvOff(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var tv = smartTVService.turnOff(id);
        redirectAttributes.addFlashAttribute("successMessage", tv.getName() + " turned off");
        return "redirect:/dashboard";
    }
    @PostMapping("/tv/{id}/volume")
    public String tvVolume(@PathVariable Long id, @RequestParam int value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var tv = smartTVService.setVolume(id, value);
        redirectAttributes.addFlashAttribute("successMessage", tv.getName() + " volume set to " + value);
        return "redirect:/dashboard";
    }
    @PostMapping("/tv/{id}/input")
    public String tvInput(@PathVariable Long id, @RequestParam String value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var tv = smartTVService.setInput(id, value);
        redirectAttributes.addFlashAttribute("successMessage", tv.getName() + " input set to " + value);
        return "redirect:/dashboard";
    }


    @PostMapping("/fridge/{id}/toggle-on")
    public String fridgeOn(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var fridge = refrigeratorService.turnOn(id);
        redirectAttributes.addFlashAttribute("successMessage", fridge.getName() + " turned on");
        return "redirect:/dashboard";
    }
    @PostMapping("/fridge/{id}/toggle-off")
    public String fridgeOff(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var fridge = refrigeratorService.turnOff(id);
        redirectAttributes.addFlashAttribute("successMessage", fridge.getName() + " turned off");
        return "redirect:/dashboard";
    }
    @PostMapping("/fridge/{id}/target-temp")
    public String fridgeTemp(@PathVariable Long id, @RequestParam double value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var fridge = refrigeratorService.setTargetTemperature(id, value);
        redirectAttributes.addFlashAttribute("successMessage", fridge.getName() + " target set to " + value + "°");
        return "redirect:/dashboard";
    }


    @PostMapping("/curtain/{id}/open-percentage")
    public String curtainSet(@PathVariable Long id, @RequestParam int value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var curtain = curtainService.setOpenPercentage(id, value);
        redirectAttributes.addFlashAttribute("successMessage", curtain.getName() + " set to " + value + "% open");
        return "redirect:/dashboard";
    }


    @PostMapping("/waterheater/{id}/toggle-on")
    public String waterHeaterOn(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var heater = waterHeaterService.turnOn(id);
        redirectAttributes.addFlashAttribute("successMessage", heater.getName() + " turned on");
        return "redirect:/dashboard";
    }
    @PostMapping("/waterheater/{id}/toggle-off")
    public String waterHeaterOff(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var heater = waterHeaterService.turnOff(id);
        redirectAttributes.addFlashAttribute("successMessage", heater.getName() + " turned off");
        return "redirect:/dashboard";
    }
    @PostMapping("/waterheater/{id}/target-temp")
    public String waterHeaterTemp(@PathVariable Long id, @RequestParam double value, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var heater = waterHeaterService.setTargetTemperature(id, value);
        redirectAttributes.addFlashAttribute("successMessage", heater.getName() + " target set to " + value + "°");
        return "redirect:/dashboard";
    }


    @PostMapping("/gate/{id}/open")
    public String gateOpen(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var gate = gateService.open(id);
        redirectAttributes.addFlashAttribute("successMessage", gate.getName() + " opened");
        return "redirect:/dashboard";
    }
    @PostMapping("/gate/{id}/close")
    public String gateClose(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        accessControlService.verifyDeviceOwnership(id, authentication.getName());
        var gate = gateService.close(id);
        redirectAttributes.addFlashAttribute("successMessage", gate.getName() + " closed");
        return "redirect:/dashboard";
    }
}