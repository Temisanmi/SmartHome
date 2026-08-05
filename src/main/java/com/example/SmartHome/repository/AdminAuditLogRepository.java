package com.example.SmartHome.repository;

import com.example.SmartHome.entity.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, Long> {
    List<AdminAuditLog> findTop5ByOrderByCreatedAtDesc();
}