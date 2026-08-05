package com.example.SmartHome.exception;

public class HomeNotFoundException extends RuntimeException {
    public HomeNotFoundException(Long id) {
        super("Home not found with id: " + id);
    }
}
