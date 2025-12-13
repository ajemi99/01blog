package com.ajemi.backend.dto;

import lombok.Data;

@Data
public class CommentRequestDTO {
    private Long postId;    // sur quel post on commente
    private String content; // le texte du commentaire
}
