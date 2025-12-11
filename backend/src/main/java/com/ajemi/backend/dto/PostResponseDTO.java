package com.ajemi.backend.dto;

import lombok.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String description;
    private String mediaUrl;
    private Instant createdAt;
    private String authorUsername;
     private LocalDateTime updatedAt;
}
