package com.example.SmartHome.repository;

import com.example.SmartHome.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GateRepository extends JpaRepository<Gate, Long> {
}