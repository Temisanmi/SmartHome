package com.example.SmartHome.repository;

import com.example.SmartHome.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByRoomId(Long roomId);

    @Override
    long count();
}
