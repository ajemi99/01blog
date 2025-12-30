package com.ajemi.backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ajemi.backend.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ajemi.backend.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

     @PostMapping
    public void follow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam Long followingId
    ) {
        Long followerId = userDetails.getId();
        followService.follow(followerId, followingId);
    }
    
    
    @DeleteMapping
    public void unfollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam Long followingId
    ) {
         Long followerId = userDetails.getId();
        followService.unfollow(followerId, followingId);
    }

}
