package com.example.SmartHome.service;

import com.example.SmartHome.entity.AdminAuditLog;
import com.example.SmartHome.repository.AdminAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuditService {
    private final AdminAuditLogRepository adminAuditLogRepository;

    public void record(String adminUsername, String action, Long targetUserId, String details) {
        AdminAuditLog auditLog = new AdminAuditLog();
        auditLog.setAdminUsername(adminUsername);
        auditLog.setAction(action);
        auditLog.setTargetUserId(targetUserId);
        auditLog.setDetails(details);
        adminAuditLogRepository.save(auditLog);
    }
}
