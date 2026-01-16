package com.ajemi.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.NotificationResponseDTO;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.security.UserDetailsImpl;
import com.ajemi.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // 📥 GET notifications
// NotificationController.java
@GetMapping
public ResponseEntity<Page<NotificationResponseDTO>> getMyNotifications(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    User user = userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Page<NotificationResponseDTO> result = notificationService.getUserNotifications(user, page, size);
    
    return ResponseEntity.ok(result);
}

    // ✅ mark as read
    @PutMapping("/{id}/read")
    public void markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userRepository.findById( userDetails.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        notificationService.markAsRead(id, user);
    }
        @GetMapping("/unread-count")
    public Long getUnreadCount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("k-------------------------------------------------------------------------------k");
        return notificationService.getUnreadCount(userDetails.getId());
    }
    @PutMapping("/read-all")
    public void markAllAsRead(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 1. Jbed l-user m-login
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. 3ayat l-logic dial l-service
        notificationService.markAllAsRead(user);

    }
}

