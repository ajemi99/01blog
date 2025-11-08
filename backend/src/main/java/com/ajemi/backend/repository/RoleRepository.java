package com.ajemi.backend.repository;

import com.ajemi.backend.entity.Role;
import com.ajemi.backend.entity.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}