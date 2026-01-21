package com.ajemi.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import  org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.ReportRequest;
import com.ajemi.backend.security.UserDetailsImpl;
import com.ajemi.backend.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping(value = "/{postId}")
    public ResponseEntity<Map<String, String>> reportUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody ReportRequest request,
        @PathVariable @NonNull Long postId
    ) {
            reportService.reportPost(userDetails.getUsername(), postId, request.getReason());
                    
                return ResponseEntity.ok(Map.of("message", "Report sent successfully"));
    }
}
