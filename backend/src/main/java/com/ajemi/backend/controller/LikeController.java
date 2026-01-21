package com.ajemi.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.security.UserDetailsImpl;
import  com.ajemi.backend.service.LikeService;

import  lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> toggleLike(
           @PathVariable @NonNull Long postId,
           @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        String result = likeService.toggleLike(postId, username);
        return ResponseEntity.ok(Map.of("status", result));  }

//     @GetMapping("/{postId}")
//     public ResponseEntity<?> getLikesCount(@PathVariable Long postId) {
//         int count = likeService.getLikesCount(postId);
//         return ResponseEntity.ok(Map.of("likesCount", count));    }
}
