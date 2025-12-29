package com.ajemi.backend.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.PostResponseDTO;
import com.ajemi.backend.service.PostService;

import lombok.RequiredArgsConstructor;
 @RestController
 @RequestMapping("/api/admin")
 @RequiredArgsConstructor
public class AdminController {
    private final PostService postService;
        @GetMapping("/posts")
public List<PostResponseDTO> getAllPostsForAdmin(Authentication authentication) {
    
    String username = authentication.getName();
    return postService.getAllPostsForAdmin(username);
}
}
