package com.ajemi.backend.dto;

import java.time.LocalDateTime;

public record AdminReportResponseDTO(
        Long id,
        String reason,
        String reporterUsername,
        String reportedUsername,
        Long postId,
        LocalDateTime createdAt
) {}
