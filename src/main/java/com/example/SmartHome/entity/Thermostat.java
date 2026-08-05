package com.example.SmartHome.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Thermostat extends Device {
    private double targetTemperature = 35.0;
    private double currentTemperature = 35.0;
}
