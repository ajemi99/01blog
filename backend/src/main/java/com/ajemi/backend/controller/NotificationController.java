package com.ajemi.backend.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ajemi.backend.dto.NotificationResponseDTO;

import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.security.UserDetailsImpl;
import com.ajemi.backend.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ajemi.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // 📥 GET notifications
@GetMapping
public List<NotificationResponseDTO> getMyNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {

    User user = userRepository.findById( userDetails.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    return notificationService.getUserNotifications(user);
}

    // ✅ mark as read
    @PutMapping("/{id}/read")
    public void markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.markAsRead(id, user);
    }
}

