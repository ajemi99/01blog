package com.ajemi.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.service.FollowService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import com.ajemi.backend.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    // Hna kheddam b PathVariable blast RequestParam
    @PostMapping("/{followingId}")
    public ResponseEntity<Map<String, Boolean>> toggleFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long followingId
    ) {
        Long followerId = userDetails.getId();
        
        // Ghadi n-tkhaylou l-service rjj3at true (Followed) aw false (Unfollowed)
        boolean isFollowing = followService.toggleFollow(followerId, followingId);
        
        return ResponseEntity.ok(Map.of("following", isFollowing));
    }
}