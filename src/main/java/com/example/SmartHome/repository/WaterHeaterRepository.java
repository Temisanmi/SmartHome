package com.example.SmartHome.repository;

import com.example.SmartHome.entity.WaterHeater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterHeaterRepository extends JpaRepository<WaterHeater, Long> {
}