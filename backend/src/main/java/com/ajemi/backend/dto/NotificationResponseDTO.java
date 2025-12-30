package com.ajemi.backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}

