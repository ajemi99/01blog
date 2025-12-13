package com.ajemi.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentResponseDTO {
    private Long id;
    private String content;
    private String username;     // nom de l'utilisateur
    private LocalDateTime createdAt;
}
