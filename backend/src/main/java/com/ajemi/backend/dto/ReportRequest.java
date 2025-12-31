package com.ajemi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ReportRequest {
    private Long userId;
    @NotBlank(message = "Reason is required")
    private String reason;
}