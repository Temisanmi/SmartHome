package com.example.SmartHome.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WaterHeater extends Device {
    private boolean powerOn = false;
    private double targetTemperature = 120.0;
    private double currentTemperature = 120.0;
}