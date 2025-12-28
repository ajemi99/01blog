package com.ajemi.backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ajemi.backend.service.FollowService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

     @PostMapping
    public void follow(
        @RequestParam Long followerId,
        @RequestParam Long followingId
    ) {
        followService.follow(followerId, followingId);
    }

    @DeleteMapping
    public void unfollow(
        @RequestParam Long followerId,
        @RequestParam Long followingId
    ) {
        followService.unfollow(followerId, followingId);
    }

    

}
