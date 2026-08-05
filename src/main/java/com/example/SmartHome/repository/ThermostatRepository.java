package com.example.SmartHome.repository;

import com.example.SmartHome.entity.Thermostat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThermostatRepository extends JpaRepository<Thermostat, Long> {
}
