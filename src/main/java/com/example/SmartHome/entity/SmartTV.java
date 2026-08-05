package com.example.SmartHome.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SmartTV extends Device {
    private boolean powerOn = false;
    private int volume = 20;
    private String currentInput = "NETFLIX";
}
