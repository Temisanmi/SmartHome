package com.example.SmartHome.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Refrigerator extends Device {
    private boolean powerOn = true;
    private double targetTemperature = 37.0;
    private double currentTemperature = 37.0;
    private boolean doorOpen = false;
}
