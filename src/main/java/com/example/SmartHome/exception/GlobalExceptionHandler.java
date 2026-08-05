package com.example.SmartHome.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DeviceNotFoundException.class)
    public String handleDeviceNotFound(DeviceNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(UnauthorizedDeviceAccessException.class)
    public String handleUnauthorized(UnauthorizedDeviceAccessException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public String handleScheduleNotFound(ScheduleNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public String handleNotificationNotFound(NotificationNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(HomeNotFoundException.class)
    public ResponseEntity<String> handleHomeNotFound(HomeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Object handleUserNotFound(UserNotFoundException ex, HttpServletRequest request, Model model) {
        if (request.getRequestURI().startsWith("/api")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(RoomNotFoundException.class)
    public Object handleRoomNotFound(RoomNotFoundException ex, HttpServletRequest request, Model model) {
        if (request.getRequestURI().startsWith("/api")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}