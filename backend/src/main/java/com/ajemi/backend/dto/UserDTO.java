package com.ajemi.backend.dto;

import com.ajemi.backend.entity.User;

public record UserDTO(
        Long id,
        String username,
        String email,
        String role,
        Boolean banned
) {
    public UserDTO(User user) {
        this(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().getName().name(),
            user.isBanned()
        );
    }
}