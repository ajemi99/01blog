package com.ajemi.backend.dto;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String description;
    private String mediaUrl;
    private Instant createdAt;
    private Long authorId;
    private String authorUsername;
     private LocalDateTime updatedAt;
      // ✅ جديد: واش user دار like
    private Boolean liked = false;

    // optional: عدد likes
    private int likesCount = 0;
    private int commentCount = 0;

}
