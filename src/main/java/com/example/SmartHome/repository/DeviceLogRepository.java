package com.example.SmartHome.repository;

import com.example.SmartHome.entity.DeviceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceLogRepository extends JpaRepository<DeviceLog, Long> {
    Page<DeviceLog> findByDeviceIdOrderByTimestampDesc(Long deviceId, Pageable pageable);
    List<DeviceLog> findByDeviceIdOrderByTimestampAsc(Long deviceId);
}