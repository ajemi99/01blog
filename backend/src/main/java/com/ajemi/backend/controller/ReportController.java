package com.ajemi.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ajemi.backend.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import com.ajemi.backend.dto.ReportRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ajemi.backend.service.ReportService;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/reports")
    public ResponseEntity<?> reportUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody ReportRequest request
) {
    
    String username = userDetails.getUsername();
    reportService.reportUser(
        username,
        request.getUserId(),
        request.getReason()
    );

    return ResponseEntity.ok("Report sent");
}
}
