package com.ajemi.backend.dto;

public record  InfoDto(Long id,
        String username,
        String email,
        Long following,
        Long followers
) {
    
}
