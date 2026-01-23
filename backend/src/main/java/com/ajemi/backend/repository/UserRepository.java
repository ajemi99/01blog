package com.ajemi.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajemi.backend.entity.Role;
import com.ajemi.backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);//3
     Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);//1

    boolean existsByUsername(String username);//2
     List<User> findAllByRole_Name(Role.RoleName name);
     List<User> findByUsernameContainingIgnoreCaseAndIdNot(String username,Long id);
}
