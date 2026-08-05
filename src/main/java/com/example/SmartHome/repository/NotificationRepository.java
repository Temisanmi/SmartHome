package com.example.SmartHome.repository;

import com.example.SmartHome.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByRelatedDeviceIdAndCreatedAtAfter(Long relatedDeviceId, LocalDateTime after);
}