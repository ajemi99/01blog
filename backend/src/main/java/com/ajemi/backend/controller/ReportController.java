package com.ajemi.backend.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.ReportRequest;
import com.ajemi.backend.security.UserDetailsImpl;
import com.ajemi.backend.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {
    private final ReportService reportService;

@PostMapping(value = "/reports/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Map<String, String>> reportUser(
    @AuthenticationPrincipal UserDetailsImpl userDetails,
    @Valid @RequestBody ReportRequest request,
    @PathVariable Long postId
) {
    try {
        String username = userDetails.getUsername();
        reportService.reportPost(username, postId, request.getReason());
        
        // Farḍ JSON response nqiya
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(Map.of("message", "Report sent successfully"));
    } catch (Exception e) {
        // Ila tra chi mochkil wast l-service (b7al report rassek)
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json")
                .body(Map.of("message", e.getMessage()));
    }
}

}
