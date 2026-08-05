package com.example.SmartHome.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AirConditioner extends Device {
    private boolean powerOn = false;
    private double targetTemperature = 20.0;
    private String mode = "COOL";
}
