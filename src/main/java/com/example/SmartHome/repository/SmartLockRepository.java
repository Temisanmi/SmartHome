package com.example.SmartHome.repository;

import com.example.SmartHome.entity.SmartLock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmartLockRepository extends JpaRepository<SmartLock, Long> {
}
