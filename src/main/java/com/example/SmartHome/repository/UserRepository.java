package com.example.SmartHome.repository;

import com.example.SmartHome.entity.User;
import com.example.SmartHome.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetTokenHash(String resetTokenHash);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByCreatedAtDesc(
            String username, String email, Pageable pageable);

    long countByActiveTrue();
    long countByRoleAndActiveTrue(Role role);
}
