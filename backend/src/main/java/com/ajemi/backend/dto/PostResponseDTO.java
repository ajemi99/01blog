package com.ajemi.backend.dto;

import lombok.*;
import java.time.Instant;

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
    private int likes;
}
