package com.example.SmartHome.repository;

import com.example.SmartHome.entity.Home;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<Home, Long> {
    @Override
    long count();
}
