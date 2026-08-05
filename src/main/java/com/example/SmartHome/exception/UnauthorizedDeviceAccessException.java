package com.example.SmartHome.exception;

public class UnauthorizedDeviceAccessException extends RuntimeException {
    public UnauthorizedDeviceAccessException() {
        super("You do not have permission to control this device");
    }
}
