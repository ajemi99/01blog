package com.ajemi.backend.dto;
import lombok.Data;

@Data
public class UserSearchDTO {
    private Long id;
    private String username;
    private boolean isFollowing;
}
