package com.example.SmartHome.repository;

import com.example.SmartHome.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDeviceId(Long deviceId);
    List<Schedule> findByActiveTrue();
}