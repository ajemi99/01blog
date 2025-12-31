package com.ajemi.backend.dto;

import java.time.LocalDateTime;

public record AdminReportResponseDTO(
        Long id,
        String reason,
        String reporterUsername,
        Long reporterId,
        String reportedUsername,
        Long reportedUserId,
        LocalDateTime createdAt
) {}
