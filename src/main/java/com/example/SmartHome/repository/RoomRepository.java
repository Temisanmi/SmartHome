package com.example.SmartHome.repository;

import com.example.SmartHome.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHomeId(Long homeId);

    @Override
    long count();
}
