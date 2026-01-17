package com.ajemi.backend.dto;

import com.ajemi.backend.entity.Role.RoleName;

public record  InfoDto(Long id,
        String username,
        String email,
        Long following,
        Long followers,
        RoleName role
) {
    
}
