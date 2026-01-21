package com.ajemi.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import  org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.CommentRequestDTO;
import com.ajemi.backend.dto.CommentResponseDTO;
import com.ajemi.backend.security.UserDetailsImpl;
import com.ajemi.backend.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // Ajouter un commentaire
    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDTO request
    ) {
        if (userDetails == null || userDetails.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CommentResponseDTO response = commentService.addComment(userDetails.getId(), request);
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

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
        @PathVariable @NonNull Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // if (userDetails == null || userDetails.getId() == null) {
        //      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        // }
        commentService.deleteComment(commentId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

}

