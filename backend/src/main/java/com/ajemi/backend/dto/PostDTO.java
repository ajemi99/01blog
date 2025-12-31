package com.ajemi.backend.dto;

import com.ajemi.backend.entity.Post;

public record PostDTO(
        Long id,
        String description,
        String author,
        String createdAt
) {
    public PostDTO(Post post) {
        this(
            post.getId(),
            post.getDescription(),
            post.getAuthor().getUsername(),
            post.getCreatedAt().toString()
        );
    }
}

