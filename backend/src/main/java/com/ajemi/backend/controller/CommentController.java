package com.ajemi.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.CommentRequestDTO;
import com.ajemi.backend.dto.CommentResponseDTO;
import com.ajemi.backend.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Ajouter un commentaire
    @PostMapping("/add/{userId}")
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Long userId,
            @RequestBody CommentRequestDTO request
    ) {
        CommentResponseDTO response = commentService.addComment(userId, request);
        return ResponseEntity.ok(response);
    }

    // Récupérer tous les commentaires d'un post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPost(
            @PathVariable Long postId
    ) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}/user/{userId}")
    public ResponseEntity<?> deleteComment(
        @PathVariable Long commentId,
        @PathVariable Long userId
    ) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.ok(Map.of("message", "Commentaire supprimé"));
    }

}

