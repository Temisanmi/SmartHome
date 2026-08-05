package com.example.SmartHome.exception;

public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(Long id) {
        super("Schedule not found with id: " + id);
    }
}
